/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.pipeline.t2d.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.jobs.flows.AnnotationFlowOptional;
import uk.ac.ebi.eva.pipeline.listeners.VariantOptionsConfigurerListener;
import uk.ac.ebi.eva.pipeline.t2d.jobs.flows.T2dLoadStatisticsFlow;
import uk.ac.ebi.eva.pipeline.t2d.jobs.steps.T2dLoadVcf;
import uk.ac.ebi.eva.pipeline.t2d.jobs.steps.T2dPrepareDatabase;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_JOB;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_STATISTICS_FLOW;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_VCF_STEP;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_PREPARE_DATABASE_STEP;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.VEP_ANNOTATION_OPTIONAL_FLOW;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({AnnotationFlowOptional.class, T2dLoadStatisticsFlow.class, T2dLoadVcf.class, T2dPrepareDatabase.class})
public class LoadVcfT2dJob {

    private static final Logger logger = LoggerFactory.getLogger(LoadVcfT2dJob.class);

    //job default settings
    private static final boolean INCLUDE_SAMPLES = false;
    private static final boolean COMPRESS_GENOTYPES = false;
    private static final boolean CALCULATE_STATS = true;
    private static final boolean INCLUDE_STATS = true;

    @Autowired
    @Qualifier(VEP_ANNOTATION_OPTIONAL_FLOW)
    private Flow annotationFlowOptional;

    @Autowired
    @Qualifier(T2D_LOAD_STATISTICS_FLOW)
    private Flow t2dOptionalLoadStatistics;

    @Autowired
    @Qualifier(T2D_LOAD_VCF_STEP)
    private Step t2dLoadStep;

    @Autowired
    @Qualifier(T2D_PREPARE_DATABASE_STEP)
    private Step t2dPrepareDatabaseStep;

    @Bean(T2D_JOB)
    public Job aggregatedJobT2d(JobBuilderFactory jobBuilderFactory) {
        logger.debug("Building variant job - t2d");

        JobBuilder jobBuilder = jobBuilderFactory
                .get(T2D_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(aggregatedJobListener());

        FlowJobBuilder builder = jobBuilder
                .flow(t2dPrepareDatabaseStep)
                .next(t2dLoadStep)
                .next(t2dOptionalLoadStatistics)
                .next(annotationFlowOptional)
                .end();

        return builder.build();
    }

    @Bean
    @Scope("prototype")
    public JobExecutionListener aggregatedJobListener() {
        return new VariantOptionsConfigurerListener(INCLUDE_SAMPLES,
                COMPRESS_GENOTYPES,
                CALCULATE_STATS,
                INCLUDE_STATS);
    }

}
