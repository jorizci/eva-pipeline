package uk.ac.ebi.eva.pipeline.t2d.jobs.steps;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.configuration.BeanNames;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.t2d.jobs.LoadVcfT2dJob;
import uk.ac.ebi.eva.test.configuration.t2d.T2dTestConfiguration;
import uk.ac.ebi.eva.test.rules.PipelineTemporaryFolderRule;
import uk.ac.ebi.eva.test.utils.TestFileUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration.T2D_PERSISTENCE_UNIT;
import static uk.ac.ebi.eva.test.utils.TestFileUtils.getResource;

@RunWith(SpringRunner.class)
@ActiveProfiles({Application.T2D_PROFILE})
@ContextConfiguration(classes = {LoadVcfT2dJob.class, T2dTestConfiguration.class})
@TestPropertySource({"classpath:application-t2d.properties"})
public class T2dAnnotationTest {

    private static final String MOCK_VEP = "/mockvep.pl";

    @Rule
    public PipelineTemporaryFolderRule temporaryFolderRule = new PipelineTemporaryFolderRule();

    @Autowired
    private JobOptions jobOptions;

    @Autowired
    @PersistenceContext(unitName = T2D_PERSISTENCE_UNIT)
    private EntityManager entityManager;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void generateVepInput() throws IOException {
        jobOptions.setInputVcf(getResource(jobOptions.getInputVcf()).getAbsolutePath());
        File mockVep = temporaryFolderRule.newGzipFile(String.join("",Files.readAllLines(getResource(MOCK_VEP)
                        .toPath(),StandardCharsets.UTF_8)));
        jobOptions.setVepOutput(mockVep.getAbsolutePath());
        //First we initializate database appropriatedly
        JobExecution jobExecution = jobLauncherTestUtils.launchStep(BeanNames.T2D_PREPARE_DATABASE_STEP);
        assertEquals(COMPLETED, jobExecution.getExitStatus());
        JobExecution jobExecutionLoadStep = jobLauncherTestUtils.launchStep(BeanNames.T2D_LOAD_VCF_STEP);
        assertEquals(COMPLETED, jobExecutionLoadStep.getExitStatus());
        JobExecution jobExecutionGenerateVepInput = jobLauncherTestUtils.launchStep(BeanNames.GENERATE_VEP_INPUT_STEP);
        assertEquals(COMPLETED, jobExecutionGenerateVepInput.getExitStatus());
        JobExecution jobExecutionLoadAnnotation = jobLauncherTestUtils.launchStep(BeanNames.LOAD_VEP_ANNOTATION_STEP);
        assertEquals(COMPLETED, jobExecutionLoadAnnotation.getExitStatus());
    }

}
