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
package uk.ac.ebi.eva.pipeline.t2d.io.readers;

import org.opencb.biodata.models.variant.VariantSource;
import org.springframework.batch.item.file.FlatFileItemReader;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.io.mappers.VcfLineMapper;
import uk.ac.ebi.eva.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VcfReaderT2d extends FlatFileItemReader<List<Variant>> {

    private final File inputVcf;
    private final VcfLineMapper vcfLineMapper;

    public VcfReaderT2d(String inputVcf, VariantSource source) throws IOException {
        this.inputVcf = new File(inputVcf);
        this.vcfLineMapper = new VcfLineMapper(source);
        setLineMapper(vcfLineMapper);
    }

    @Override
    protected void doOpen() throws Exception {
        setResource(FileUtils.getResource(inputVcf));
        super.doOpen();
    }
}
