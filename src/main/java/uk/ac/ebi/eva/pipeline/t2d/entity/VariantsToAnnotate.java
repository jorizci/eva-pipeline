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
package uk.ac.ebi.eva.pipeline.t2d.entity;

import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.model.IVariant;
import uk.ac.ebi.eva.pipeline.t2d.utils.VariantUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VARIANTS_TO_ANNOTATE")
public class VariantsToAnnotate implements IVariant {

    @Id
    @Column(name = "ID")
    public String id;

    @Column(nullable = false)
    private String chromosome;

    private int start;

    private int end;

    private String referenceAllele;

    private String effectAllele;

    VariantsToAnnotate() {

    }

    public VariantsToAnnotate(Variant variant) {
        Variant vepCompatibleVariant = variant.copyInEnsemblFormat();
        id = VariantUtils.getVariantId(vepCompatibleVariant);
        chromosome = vepCompatibleVariant.getChromosome();
        start = vepCompatibleVariant.getStart();
        end = vepCompatibleVariant.getEnd();
        referenceAllele = vepCompatibleVariant.getReference();
        effectAllele = vepCompatibleVariant.getAlternate();
    }

    @Override
    public String getChr() {
        return chromosome;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public String getRefAlt() {
        return String.format("%s/%s", referenceAllele, effectAllele);
    }

    @Override
    public String getStrand() {
        return "+";
    }
}
