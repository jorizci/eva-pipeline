package uk.ac.ebi.eva.pipeline.t2d.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.ebi.eva.pipeline.Application;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "uk.ac.ebi.eva.pipeline.t2d.repository", entityManagerFactoryRef =
        "t2dEntityManagerFactory",
        transactionManagerRef = "t2dTransactionManager")
public class T2dDataSourceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(T2dDataSourceConfiguration.class);
    public static final String T2D_PERSISTENCE_UNIT = "t2dPersistenceUnit";
    private static final String T2D_DATASOURCE_PREFIX = "t2d.datasource";
    private static final String T2D_JPA_PROPERTIES_PREFIX = "t2d.jpa.properties";
    private static final String ENTITY_BASE_PACKAGE = "uk.ac.ebi.eva.pipeline.t2d.entity";
    public static final String T2D_TRANSACTION_MANAGER = "t2dTransactionManager";

    @Bean
    @ConfigurationProperties(prefix = T2D_DATASOURCE_PREFIX)
    DataSource t2dDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @PersistenceContext(unitName = T2D_PERSISTENCE_UNIT)
    LocalContainerEntityManagerFactoryBean t2dEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String,Object> properties = flatten(t2dHibernateProperties());
        return builder
                .dataSource(t2dDataSource())
                .properties(properties)
                .persistenceUnit(T2D_PERSISTENCE_UNIT)
                .packages(ENTITY_BASE_PACKAGE).build();
    }

    private Map<String, Object> flatten(Map<String, Object> map) {
        HashMap<String,Object> flattenedMap = new HashMap<>();
        for(Map.Entry<String,Object> entry: map.entrySet()){
            if(entry.getValue() instanceof Map<?,?>){
                Map<String, Object> flattenedEntries = flatten((Map<String, Object>) entry.getValue());
                for(Map.Entry<String,Object> flattenedEntry : flattenedEntries.entrySet()){
                    flattenedMap.put(entry.getKey()+"."+flattenedEntry.getKey(),flattenedEntry.getValue());
                }
            }else{
                flattenedMap.put(entry.getKey(),entry.getValue());
            }
        }
        return flattenedMap;
    }

    @Bean(T2D_TRANSACTION_MANAGER)
    PlatformTransactionManager t2dTransactionManager(EntityManagerFactoryBuilder builder) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(t2dEntityManagerFactory(builder).getObject());
        transactionManager.setDataSource(t2dDataSource());
        return transactionManager;
    }

    @Bean
    @ConfigurationProperties(prefix = T2D_JPA_PROPERTIES_PREFIX)
    Map<String, Object> t2dHibernateProperties() {
        return new HashMap<>();
    }

}
