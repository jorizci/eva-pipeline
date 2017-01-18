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
package uk.ac.ebi.eva.pipeline.t2d.jobs.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.jobs.steps.tasklet.PrepareDatabaseT2dStep;
import uk.ac.ebi.eva.utils.TaskletUtils;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_PREPARE_DATABASE_STEP;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dDataSourceConfiguration.class})
public class T2dPrepareDatabase {

    private static final Logger logger = LoggerFactory.getLogger(T2dPrepareDatabase.class);

    @Bean
    @StepScope
    public PrepareDatabaseT2dStep prepareDatabaseT2dStep() {
        return new PrepareDatabaseT2dStep();
    }

    @Bean(T2D_PREPARE_DATABASE_STEP)
    public Step prepareDatabaseT2d(StepBuilderFactory stepBuilderFactory, JobOptions jobOptions) {
        logger.debug("Building '" + T2D_PREPARE_DATABASE_STEP + "'");
        return TaskletUtils.generateStep(stepBuilderFactory, T2D_PREPARE_DATABASE_STEP, prepareDatabaseT2dStep(), jobOptions);
    }

}
