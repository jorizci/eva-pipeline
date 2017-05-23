package uk.ac.ebi.eva.commons.models.mongo.entity.subdocuments;

import com.mongodb.BasicDBObject;
import org.opencb.biodata.models.feature.Genotype;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.models.data.VariantStats;

import java.util.HashMap;
import java.util.Map;

public class VariantStatsMongo {

    public final static String COHORT_ID = "cid";

    public final static String STUDY_ID = "sid";

    public final static String FILE_ID = "fid";

    public final static String MAF_FIELD = "maf";

    public final static String MGF_FIELD = "mgf";

    public final static String MAFALLELE_FIELD = "mafAl";

    public final static String MGFGENOTYPE_FIELD = "mgfGt";

    public final static String MISSALLELE_FIELD = "missAl";

    public final static String MISSGENOTYPE_FIELD = "missGt";

    public final static String NUMGT_FIELD = "numGt";

    @Field(STUDY_ID)
    private String studyId;

    @Field(FILE_ID)
    private String fileId;

    @Field(COHORT_ID)
    private String cohortId;

    @Field(MAF_FIELD)
    private float maf;

    @Field(MGF_FIELD)
    private float mgf;

    @Field(MAFALLELE_FIELD)
    private String mafAllele;

    @Field(MGFGENOTYPE_FIELD)
    private String mgfGenotype;

    @Field(MISSALLELE_FIELD)
    private int missingAlleles;

    @Field(MISSGENOTYPE_FIELD)
    private int missingGenotypes;

    @Field(NUMGT_FIELD)
    private Map<String, Integer> numGt;

    public VariantStatsMongo(String studyId, String fileId, String cohortId, VariantStats stats) {
        this.studyId = studyId;
        this.fileId = fileId;
        this.cohortId = cohortId;
        this.maf = stats.getMaf();
        this.mgf = stats.getMgf();
        this.mafAllele = stats.getMafAllele();
        this.mgfGenotype = stats.getMgfGenotype();
        this.missingAlleles = stats.getMissingAlleles();
        this.missingGenotypes = stats.getMissingGenotypes();
        this.numGt = buildGenotypes(stats.getGenotypesCount());

    }

    private Map<String, Integer> buildGenotypes(Map<Genotype, Integer> genotypesCount) {
        Map<String,Integer> genotypes = new HashMap<>();
        for (Map.Entry<Genotype, Integer> g : genotypesCount.entrySet()) {
            String genotypeStr = g.getKey().toString().replace(".", "-1");
            genotypes.put(genotypeStr, g.getValue());
        }
        return genotypes;
    }
}