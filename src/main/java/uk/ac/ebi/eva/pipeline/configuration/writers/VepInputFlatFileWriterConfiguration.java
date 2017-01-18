/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.pipeline.configuration.writers;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.eva.pipeline.io.writers.VepInputFlatFileWriter;
import uk.ac.ebi.eva.pipeline.model.IVariant;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.VEP_INPUT_WRITER;

@Configuration
public class VepInputFlatFileWriterConfiguration {

    @Bean(VEP_INPUT_WRITER)
    @StepScope
    public ItemStreamWriter<IVariant> vepInputFlatFileWriter(JobOptions jobOptions) {
        return new VepInputFlatFileWriter(jobOptions.getVepInput());
    }

}
