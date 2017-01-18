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
package uk.ac.ebi.eva.pipeline.t2d.io.writers;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.t2d.entity.VariantInfo;
import uk.ac.ebi.eva.pipeline.t2d.repository.VariantInfoRepository;
import uk.ac.ebi.eva.pipeline.t2d.utils.VariantUtils;

import java.util.ArrayList;
import java.util.List;


public class VariantInfoT2dWriter implements ItemWriter<Variant> {

    @Autowired
    private VariantInfoRepository variantInfoRepository;

    @Override
    public void write(List<? extends Variant> items) throws Exception {
        List<VariantInfo> variantInfos = generateVariantInfos(items);
        variantInfoRepository.save(variantInfos);
    }

    private List<VariantInfo> generateVariantInfos(List<? extends Variant> variants) {
        List<VariantInfo> variantInfos = new ArrayList<>();
        for (Variant variant : variants) {
            variantInfos.add(VariantUtils.convertToVariantInfo(variant));
        }
        return variantInfos;
    }
}
