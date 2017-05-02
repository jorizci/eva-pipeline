package uk.ac.ebi.eva.pipeline.t2d;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.configuration.BeanNames;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.t2d.jobs.LoadVcfT2dJob;
import uk.ac.ebi.eva.test.configuration.t2d.T2dTestConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.ANNOTATE_VARIANTS_JOB;
import static uk.ac.ebi.eva.pipeline.parameters.JobParametersNames.INPUT_VCF;
import static uk.ac.ebi.eva.pipeline.parameters.JobParametersNames.INPUT_VCF_AGGREGATION;
import static uk.ac.ebi.eva.pipeline.parameters.JobParametersNames.T2D_INPUT_STUDY_PHENOTYPE;
import static uk.ac.ebi.eva.pipeline.parameters.JobParametersNames.T2D_INPUT_STUDY_STATISTICS;
import static uk.ac.ebi.eva.pipeline.parameters.JobParametersNames.T2D_INPUT_STUDY_STATISTICS_IDS;
import static uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration.T2D_PERSISTENCE_UNIT;

@RunWith(SpringRunner.class)
@ActiveProfiles({Application.T2D_PROFILE})
@ContextConfiguration(classes = {LoadVcfT2dJob.class, T2dTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestPropertySource({"classpath:application-t2d.properties"})
public class OxfordLoadData {

    private static final String NORMAL_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.ALL.SUMMARY.txt";
    private static final String NORMAL_STATISTICS_IDS = "#var_id";
    private static final String BMI_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.BMI.SUMMARY.txt";
    private static final String DBP_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.DBP.SUMMARY.txt";
    private static final String FG_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.FG.SUMMARY.txt";
    private static final String FI_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.FI.SUMMARY.txt";
    private static final String HDL_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.HDL.SUMMARY.txt";
    private static final String HEIGHT_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.HEIGHT.SUMMARY.txt";
    private static final String HYPERTENSION_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.HYPERTENSION.SUMMARY.txt";
    private static final String LDL_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.LDL.SUMMARY.txt";
    private static final String PP_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.PP.SUMMARY.txt";
    private static final String SBP_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.SBP.SUMMARY.txt";
    private static final String TC_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.TC.SUMMARY.txt";
    private static final String TG_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.TG.SUMMARY.txt";
    private static final String WHR_STATISTICS = "/media/sf_dumps/oxford_dataset/OBB.WHR.SUMMARY.txt";
    private static final String VCF_FILE = "/media/sf_dumps/oxford_dataset/OBB.exomechip.aggregate.vcf.gz";
    private static final String STATISTICS_IDS = "CHROM, POS, REF, ALT";

    @Autowired
    private JobOptions jobOptions;

    @Autowired
    @PersistenceContext(unitName = T2D_PERSISTENCE_UNIT)
    private EntityManager entityManager;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void insertNonPhenotypicStatistics(){
        System.setProperty(T2D_INPUT_STUDY_STATISTICS, NORMAL_STATISTICS);
        System.setProperty(T2D_INPUT_STUDY_STATISTICS_IDS, NORMAL_STATISTICS_IDS);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_PREPARE_DATABASE_STEP);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_LOAD_STATISTICS_STEP);
    }

    @Test
    public void insertStatistics() {
        insertStatistics("BMI", BMI_STATISTICS);
        insertStatistics("DBP", DBP_STATISTICS);
        insertStatistics("FG", FG_STATISTICS);
        insertStatistics("FI", FI_STATISTICS);
        insertStatistics("HDL", HDL_STATISTICS);
        insertStatistics("HEIGHT", HEIGHT_STATISTICS);
    }

    @Test
    public void insertStatistics2() {
        insertStatistics("HYPERTENSION", HYPERTENSION_STATISTICS);
        insertStatistics("LDL", LDL_STATISTICS);
        insertStatistics("PP", PP_STATISTICS);
        insertStatistics("SBP", SBP_STATISTICS);
        insertStatistics("TC", TC_STATISTICS);
        insertStatistics("TG", TG_STATISTICS);
        insertStatistics("WHR", WHR_STATISTICS);
    }

    @Test
    public void loadVcf() {
        System.setProperty(INPUT_VCF, VCF_FILE);
        System.setProperty(INPUT_VCF_AGGREGATION, "BASIC");
        jobOptions.setInputVcf(VCF_FILE);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_PREPARE_DATABASE_STEP);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_LOAD_VCF_STEP);
    }

    @Test
    public void annotateVcf(){
        //jobLauncherTestUtils.launchStep(BeanNames.GENERATE_VEP_INPUT_STEP);
        //jobLauncherTestUtils.launchStep(BeanNames.GENERATE_VEP_ANNOTATION_STEP);
        jobLauncherTestUtils.launchStep(BeanNames.LOAD_VEP_ANNOTATION_STEP);
    }

    private void insertStatistics(String phenotype, String statisticsFile) {
        System.setProperty(T2D_INPUT_STUDY_PHENOTYPE, phenotype);
        System.setProperty(T2D_INPUT_STUDY_STATISTICS, statisticsFile);
        System.setProperty(T2D_INPUT_STUDY_STATISTICS_IDS, STATISTICS_IDS);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_PREPARE_DATABASE_STEP);
        jobLauncherTestUtils.launchStep(BeanNames.T2D_LOAD_STATISTICS_STEP);
    }

}
