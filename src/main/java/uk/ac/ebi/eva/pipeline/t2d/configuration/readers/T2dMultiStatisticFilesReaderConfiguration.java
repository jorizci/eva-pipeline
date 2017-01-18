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
package uk.ac.ebi.eva.pipeline.t2d.configuration.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.configuration.writers.T2dStatisticsWriterConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.readers.T2dStatisticsReader;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

import java.util.Arrays;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_MULTI_STATISTIC_FILES_READER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_STATISTICS_READER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dStatisticsReaderConfiguration.class})
public class T2dMultiStatisticFilesReaderConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(T2dStatisticsWriterConfiguration.class);

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_STATISTICS + ":#{new String[]{}}}")
    private String[] t2dStatisticsFiles;

    @Autowired
    @Qualifier(T2D_STATISTICS_READER)
    private T2dStatisticsReader t2dStatisticsReader;

    @Bean(T2D_MULTI_STATISTIC_FILES_READER)
    @StepScope
    public MultiResourceItemReader<T2dStatistics> multiResourceItemReader() {
        logger.debug("Building '" + T2D_MULTI_STATISTIC_FILES_READER + "'");
        Resource[] resources = Arrays.stream(t2dStatisticsFiles).map(FileSystemResource::new).toArray(Resource[]::new);
        MultiResourceItemReader<T2dStatistics> multipleItemReader = new MultiResourceItemReader<>();
        multipleItemReader.setResources(resources);
        multipleItemReader.setDelegate(t2dStatisticsReader);
        return multipleItemReader;
    }
}
