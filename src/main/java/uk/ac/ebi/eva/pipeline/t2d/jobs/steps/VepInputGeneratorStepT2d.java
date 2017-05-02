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
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.configuration.writers.VepInputFlatFileWriterConfiguration;
import uk.ac.ebi.eva.pipeline.model.IVariant;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.configuration.readers.VariantsToAnnotateReaderConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.readers.VariantsToAnnotateReader;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.GENERATE_VEP_INPUT_STEP;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANTS_TO_ANNOTATE_READER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.VEP_INPUT_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({VariantsToAnnotateReaderConfiguration.class, VepInputFlatFileWriterConfiguration.class})
public class VepInputGeneratorStepT2d {

    private static final Logger logger = LoggerFactory.getLogger(VepInputGeneratorStepT2d.class);

    @Autowired
    @Qualifier(T2D_VARIANTS_TO_ANNOTATE_READER)
    private VariantsToAnnotateReader reader;

    @Autowired
    @Qualifier(VEP_INPUT_WRITER)
    private ItemStreamWriter<IVariant> writer;

    @Bean(GENERATE_VEP_INPUT_STEP)
    public Step variantsAnnotGenerateInputBatchStep(StepBuilderFactory stepBuilderFactory, JobOptions jobOptions) {
        logger.debug("Building '" + GENERATE_VEP_INPUT_STEP + "' - t2d version");
        Boolean canRestart = jobOptions.getPipelineOptions().getBoolean(JobParametersNames.CONFIG_RESTARTABILITY_ALLOW);

        return stepBuilderFactory.get(GENERATE_VEP_INPUT_STEP).<IVariant, IVariant>chunk(10000)
                .reader(reader)
                .writer(writer)
                .allowStartIfComplete(canRestart)
                .build();
    }

}
