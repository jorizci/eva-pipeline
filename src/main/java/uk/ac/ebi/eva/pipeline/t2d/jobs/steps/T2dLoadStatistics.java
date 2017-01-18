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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.configuration.readers.T2dMultiStatisticFilesReaderConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.configuration.writers.T2dStatisticsWriterConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_STATISTICS_STEP;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_MULTI_STATISTIC_FILES_READER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_STATISTICS_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dMultiStatisticFilesReaderConfiguration.class, T2dStatisticsWriterConfiguration.class})
public class T2dLoadStatistics {

    private static final Logger logger = LoggerFactory.getLogger(T2dLoadStatistics.class);

    @Autowired
    @Qualifier(T2D_MULTI_STATISTIC_FILES_READER)
    private MultiResourceItemReader<T2dStatistics> multipleStatisticsWriter;

    @Autowired
    @Qualifier(T2D_STATISTICS_WRITER)
    private ItemWriter<? super T2dStatistics> t2dStatisticsWriter;

    @Bean(T2D_LOAD_STATISTICS_STEP)
    public Step t2dLoadStatistics(StepBuilderFactory stepBuilderFactory, JobOptions jobOptions) throws Exception {
        logger.debug("Building '" + T2D_LOAD_STATISTICS_STEP + "'");
        Boolean canRestart = jobOptions.getPipelineOptions().getBoolean(JobParametersNames.CONFIG_RESTARTABILITY_ALLOW);

        return stepBuilderFactory.get(T2D_LOAD_STATISTICS_STEP).<T2dStatistics, T2dStatistics>chunk(50000)
                .reader(multipleStatisticsWriter)
                .writer(t2dStatisticsWriter)
                .allowStartIfComplete(canRestart)
                .build();
    }


}
