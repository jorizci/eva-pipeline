package uk.ac.ebi.eva.pipeline.t2d.jobs.steps;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration.T2D_PERSISTENCE_UNIT;
import static uk.ac.ebi.eva.test.utils.TestFileUtils.getResource;

@RunWith(SpringRunner.class)
@ActiveProfiles({Application.T2D_PROFILE})
@ContextConfiguration(classes = {LoadVcfT2dJob.class, T2dTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestPropertySource({"classpath:application-t2d.properties"})
public class T2dLoadVcfTest {

    private static final String SMALL_20 = "/small20.vcf.gz";

    @Autowired
    private JobOptions jobOptions;

    @Autowired
    @PersistenceContext(unitName = T2D_PERSISTENCE_UNIT)
    private EntityManager entityManager;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Ignore
    @Test
    public void testLoadVcf() {
        jobOptions.setInputVcf(getResource(SMALL_20).getAbsolutePath());
        //First we initializate database appropriatedly
        JobExecution jobExecution = jobLauncherTestUtils.launchStep(BeanNames.T2D_PREPARE_DATABASE_STEP);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
        JobExecution jobExecutionLoadStep = jobLauncherTestUtils.launchStep(BeanNames.T2D_LOAD_VCF_STEP);
        assertEquals(COMPLETED, jobExecutionLoadStep.getExitStatus());
    }

}
