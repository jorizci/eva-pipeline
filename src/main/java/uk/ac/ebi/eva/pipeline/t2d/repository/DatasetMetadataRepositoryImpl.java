package uk.ac.ebi.eva.pipeline.t2d.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.t2d.entity.DatasetMetadata;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;
import uk.ac.ebi.eva.pipeline.t2d.utils.VariantUtils;

import javax.persistence.EntityManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration.T2D_TRANSACTION_MANAGER;

public class DatasetMetadataRepositoryImpl implements T2dRepository {

    private static final Logger logger = LoggerFactory.getLogger(DatasetMetadataRepositoryImpl.class);

    private final EntityManager entityManager;

    @Autowired
    public DatasetMetadataRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(DatasetMetadata.class);
    }

    @Override
    @Modifying
    @Transactional(T2D_TRANSACTION_MANAGER)
    public void createTable(String tableName, String... statisticParameters) {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (`U_VAR_ID` VARCHAR(191), `VAR_ID` TEXT";
        for (String staticParameter : statisticParameters) {
            query += ", " + staticParameter + " TEXT";
        }
        query += ", PRIMARY KEY (`U_VAR_ID`)";
//        query += ", KEY `VAR_ID` (`VAR_ID`(191)) ";
        query += ")";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Override
    public void storeStatistics(String tableName, T2dStatistics statistics, String[] idKeys) {
        List<String> keys = new ArrayList<>(statistics.getStatisticsKeys(idKeys));

        String query = "REPLACE into " + tableName + " (U_VAR_ID, VAR_ID ";
        for (String key : keys) {
            query += ", " + key;
        }
        String variantId = statistics.getVariantId(idKeys);
        query += ") values('" + calculeSha256(variantId) + "', '" + variantId + "'";
        for (String key : keys) {
            query += ", '" + statistics.getStatistic(key) + "'";
        }
        query += ")";
        entityManager.createNativeQuery(query).executeUpdate();
    }

    private String calculeSha256(String variantId) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(variantId.getBytes());
            byte[] digest = messageDigest.digest();

            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return new String();
        }
    }

    @Override
    public void storeStatistics(String tableName, List<? extends T2dStatistics> statistics, String[] idKeys) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> keys = new ArrayList<>(statistics.get(0).getStatisticsKeys(idKeys));
        appendQueryStart(stringBuilder, tableName, keys);
        appendValues(stringBuilder, keys, statistics.get(0), idKeys);

        for (int i = 1; i < statistics.size(); i++) {
            T2dStatistics statistic = statistics.get(i);
            List<String> currentObjectKeys = new ArrayList<>(statistics.get(0).getStatisticsKeys(idKeys));

            //If keys have changed, execute current batch and create a new one, otherwise add more values.
            if (!keys.containsAll(currentObjectKeys)) {
                appendQueryEnd(stringBuilder, keys);
                executeQuery(stringBuilder);

                keys = currentObjectKeys;
                stringBuilder = new StringBuilder();
                appendQueryStart(stringBuilder, tableName, keys);
            } else {
                stringBuilder.append(",");
            }

            appendValues(stringBuilder, keys, statistic, idKeys);
        }
        appendQueryEnd(stringBuilder, keys);
        executeQuery(stringBuilder);
    }

    private void appendQueryEnd(StringBuilder stringBuilder, List<String> keys) {
        stringBuilder.append(" ON DUPLICATE KEY UPDATE ");
        stringBuilder.append(keys.get(0)).append(" = VALUES(").append(keys.get(0)).append(")");
        for (int i = 1; i < keys.size(); i++) {
            stringBuilder.append(",");
            stringBuilder.append(keys.get(i)).append(" = VALUES(").append(keys.get(i)).append(")");
        }
    }

    private void appendValues(StringBuilder stringBuilder, List<String> keys, T2dStatistics t2dStatistic,
                              String[] idKeys) {
        String variantId = t2dStatistic.getVariantId(idKeys);
        stringBuilder.append("('").append(calculeSha256(variantId)).append("', '")
                .append(variantId).append("'");
        for (String key : keys) {
            stringBuilder.append(",'").append(t2dStatistic.getStatistic(key)).append("'");
        }
        stringBuilder.append(")");
    }

    private void executeQuery(StringBuilder stringBuilder) {
        String query = stringBuilder.toString();
        logger.debug("New write " + new Date());
        entityManager.createNativeQuery(query).executeUpdate();
    }

    private void appendQueryStart(StringBuilder stringBuilder, String tableName, List<String> keys) {
        stringBuilder.append("INSERT into ").append(tableName).append(" (U_VAR_ID, VAR_ID ");
        for (String key : keys) {
            stringBuilder.append(", ").append(key);
        }
        stringBuilder.append(") values ");
    }


    @Override
    @Modifying
    @Transactional(T2D_TRANSACTION_MANAGER)
    public void storeVariantId(Variant variant, String tableName) {
        String variantId = VariantUtils.getVariantId(variant);
        String query = "INSERT INTO " + tableName + " (U_VAR_ID, VAR_ID) VALUES ('" + calculeSha256(variantId) + "', '"
                + variantId + "')";
        entityManager.createNativeQuery(query).executeUpdate();
    }

}
