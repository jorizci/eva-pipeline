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

import org.opencb.biodata.models.variant.annotation.VariantAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.eva.pipeline.t2d.entity.VariantInfo;
import uk.ac.ebi.eva.pipeline.t2d.repository.VariantInfoRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.VariantsToannotateRepository;

import java.util.ArrayList;
import java.util.List;

import static uk.ac.ebi.eva.pipeline.t2d.utils.VariantUtils.convertToVariantInfo;

public class VepAnnotationT2dWriter implements ItemWriter<VariantAnnotation> {

    private final static Logger logger = LoggerFactory.getLogger(VepAnnotationT2dWriter.class);

    @Autowired
    private VariantInfoRepository variantInfoRepository;

    @Autowired
    private VariantsToannotateRepository variantsToannotateRepository;

    @Override
    public void write(List<? extends VariantAnnotation> annotations) throws Exception {
        List<VariantInfo> variantInfos = new ArrayList<>();
        List<String> variantIds = new ArrayList<>();
        for (VariantAnnotation annotation : annotations) {
            logger.info("Annotation: "+convertToVariantInfo(annotation).getVariantId()+"  "
                    +annotation.getChromosome()+" "+annotation.getStart()+" "+annotation.getEnd());
            variantInfos.add(convertToVariantInfo(annotation));
        }
        variantInfoRepository.save(variantInfos);


//        for (VariantInfo variantInfo : variantInfos) {
//            try {
//                variantsToannotateRepository.delete(variantInfo.getVariantId());
//            }catch (Exception e){
//                logger.info("Variant not in annotate list '"+variantInfo.getVariantId()+"'");
//            }
//        }
    }

}
