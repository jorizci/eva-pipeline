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

import org.opencb.biodata.models.variant.VariantSource;
import org.opencb.opencga.storage.core.variant.VariantStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.io.readers.UnwindingItemStreamReader;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.readers.VcfReaderT2d;

import java.io.IOException;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VCF_READER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
public class T2dVcfReaderConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(T2dVcfReaderConfiguration.class);

    @Bean(T2D_VCF_READER)
    @StepScope
    public ItemStreamReader<Variant> t2dVcfReader(JobOptions jobOptions) throws IOException {
        logger.debug("Building '" + T2D_VCF_READER + "'");
        VariantSource variantSource = (VariantSource) jobOptions.getVariantOptions().get(VariantStorageManager.VARIANT_SOURCE);
        VcfReaderT2d vcfReader = new VcfReaderT2d(jobOptions.getVepInput(), variantSource);
        return new UnwindingItemStreamReader<>(vcfReader);
    }

}
