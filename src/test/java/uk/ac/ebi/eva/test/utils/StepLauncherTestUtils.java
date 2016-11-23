package uk.ac.ebi.eva.test.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.AbstractStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.StepRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


public class StepLauncherTestUtils {

    private static final long JOB_PARAMETER_MAXIMUM = 1000000;

    /**
     * Logger
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private JobLauncher jobLauncher;

    private Step step;

    private JobRepository jobRepository;

    private StepRunner stepRunner;

    /**
     * The Job instance that can be manipulated (e.g. launched) in this utility.
     *
     * @param step the {@link AbstractStep} to use
     */
    @Autowired
    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * The {@link JobRepository} to use for creating new {@link JobExecution}
     * instances.
     *
     * @param jobRepository a {@link JobRepository}
     */
    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * @return the step repository
     */
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    /**
     * @return the step
     */
    public Step getStep() {
        return step;
    }

    /**
     * A {@link JobLauncher} instance that can be used to launch jobs.
     *
     * @param jobLauncher a step launcher
     */
    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    /**
     * @return the step launcher
     */
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    /**
     * @return a new JobParameters object containing only a parameter for the
     * current timestamp, to ensure that the step instance will be unique.
     */
    public JobParameters getUniqueJobParameters() {
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("random", new JobParameter((long) (Math.random() * JOB_PARAMETER_MAXIMUM)));
        return new JobParameters(parameters);
    }

    /**
     * Convenient method for subclasses to grab a {@link StepRunner} for running
     * steps by name.
     *
     * @return a {@link StepRunner}
     */
    protected StepRunner getStepRunner() {
        if (this.stepRunner == null) {
            this.stepRunner = new StepRunner(getJobLauncher(), getJobRepository());
        }
        return this.stepRunner;
    }

    /**
     * Launch just the specified step in the step. A unique set of JobParameters
     * will automatically be generated. An IllegalStateException is thrown if
     * there is no Step with the given name.
     *
     * @return JobExecution
     */
    public JobExecution launchStep() {
        return this.launchStep(this.getUniqueJobParameters(), null);
    }

    /**
     * Launch just the specified step in the step. A unique set of JobParameters
     * will automatically be generated. An IllegalStateException is thrown if
     * there is no Step with the given name.
     *
     * @param jobExecutionContext An ExecutionContext whose values will be
     *                            loaded into the Job ExecutionContext prior to launching the step.
     * @return JobExecution
     */
    public JobExecution launchStep(ExecutionContext jobExecutionContext) {
        return this.launchStep(this.getUniqueJobParameters(), jobExecutionContext);
    }

    /**
     * Launch just the specified step in the step. An IllegalStateException is
     * thrown if there is no Step with the given name.
     *
     * @param jobParameters The JobParameters to use during the launch
     * @return JobExecution
     */
    public JobExecution launchStep(JobParameters jobParameters) {
        return this.launchStep(jobParameters, null);
    }

    /**
     * Launch just the specified step in the step. An IllegalStateException is
     * thrown if there is no Step with the given name.
     *
     * @param jobParameters       The JobParameters to use during the launch
     * @param jobExecutionContext An ExecutionContext whose values will be
     *                            loaded into the Job ExecutionContext prior to launching the step.
     * @return JobExecution
     */
    public JobExecution launchStep(JobParameters jobParameters, ExecutionContext jobExecutionContext) {
        return getStepRunner().launchStep(step, jobParameters, jobExecutionContext);
    }

}
