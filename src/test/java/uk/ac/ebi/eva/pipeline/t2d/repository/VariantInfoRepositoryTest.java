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
package uk.ac.ebi.eva.pipeline.t2d.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.pipeline.t2d.entity.VariantInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class VariantInfoRepositoryTest {

    private static final String VARIANT_ID = "4_15482477_A_G";
    private static final String CLOSEST_GENE = "CC2D2A";
    private static final String DBSNP_ID = "rs10000250";
    private static final String CHROMOSOME = "4";
    private static final Integer POSITION = 15482477;
    private static final String REFERENCE_ALLELE = "A";
    private static final String EFFECT_ALLELE = "G";
    private static final String GENE = "CC2D2A";
    private static final String CONSEQUENCE = "intron_variant";

    @Autowired
    private VariantInfoRepository repository;

    @Test
    public void saveTest() {
        VariantInfo variantInfo = new VariantInfo();
        variantInfo.setVariantId(VARIANT_ID);
        variantInfo.setClosestGene(CLOSEST_GENE);
        variantInfo.setDbsnpId(DBSNP_ID);
        variantInfo.setChromosome(CHROMOSOME);
        variantInfo.setPosition(POSITION);
        variantInfo.setReferenceAllele(REFERENCE_ALLELE);
        variantInfo.setEffectAllele(EFFECT_ALLELE);
        variantInfo.setGene(GENE);
        variantInfo.setConsequence(CONSEQUENCE);
        variantInfo.setPolyPhenPred(null);
        variantInfo.setSiftPred(null);

        repository.save(variantInfo);
        assertNotNull(repository.findOne(VARIANT_ID));
        VariantInfo dbVariantInfo = repository.findOne(VARIANT_ID);
        assertEquals(CLOSEST_GENE,dbVariantInfo.getClosestGene());
        assertEquals(DBSNP_ID,dbVariantInfo.getDbsnpId());
        assertEquals(CHROMOSOME ,dbVariantInfo.getChromosome());
        assertEquals(POSITION ,dbVariantInfo.getPosition());
        assertEquals(REFERENCE_ALLELE ,dbVariantInfo.getReferenceAllele());
        assertEquals(EFFECT_ALLELE ,dbVariantInfo.getEffectAllele());
        assertEquals(GENE ,dbVariantInfo.getGene());
        assertEquals(CONSEQUENCE,dbVariantInfo.getConsequence());

    }

}
