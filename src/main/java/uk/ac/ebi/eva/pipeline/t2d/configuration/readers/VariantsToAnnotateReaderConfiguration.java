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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.readers.VariantsToAnnotateReader;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANTS_TO_ANNOTATE_READER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dDataSourceConfiguration.class})
public class VariantsToAnnotateReaderConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(VariantsToAnnotateReaderConfiguration.class);

    @Bean(T2D_VARIANTS_TO_ANNOTATE_READER)
    @StepScope
    public VariantsToAnnotateReader variantsToAnnotateReader() {
        logger.debug("Building '" + T2D_VARIANTS_TO_ANNOTATE_READER + "'");
        return new VariantsToAnnotateReader();
    }

}
