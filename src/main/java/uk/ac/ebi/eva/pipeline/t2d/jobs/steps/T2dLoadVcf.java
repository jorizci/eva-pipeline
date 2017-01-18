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
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.configuration.readers.T2dVcfReaderConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.configuration.writers.T2dCompositeVariantWriter;

import java.io.IOException;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_COMPOSITE_WRITER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_VCF_STEP;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VCF_READER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dVcfReaderConfiguration.class, T2dCompositeVariantWriter.class})
public class T2dLoadVcf {

    private static final Logger logger = LoggerFactory.getLogger(T2dLoadVcf.class);

    @Autowired
    @Qualifier(T2D_VCF_READER)
    private ItemStreamReader<Variant> t2dVcfReader;

    @Autowired
    @Qualifier(T2D_COMPOSITE_WRITER)
    private CompositeItemWriter<Variant> compositeItemWriter;

    @Bean(T2D_LOAD_VCF_STEP)
    public Step t2dLoadStep(StepBuilderFactory stepBuilderFactory, JobOptions jobOptions) throws IOException {
        logger.debug("Building '" + T2D_LOAD_VCF_STEP + "'");
        Boolean canRestart = jobOptions.getPipelineOptions().getBoolean(JobParametersNames.CONFIG_RESTARTABILITY_ALLOW);

        return stepBuilderFactory.get(T2D_LOAD_VCF_STEP).<Variant, Variant>chunk(10)
                .reader(t2dVcfReader)
                .writer(compositeItemWriter)
                .allowStartIfComplete(canRestart)
                .build();
    }

}
