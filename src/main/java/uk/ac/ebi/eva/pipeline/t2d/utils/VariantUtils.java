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
package uk.ac.ebi.eva.pipeline.t2d.utils;

import org.opencb.biodata.models.variant.annotation.ConsequenceType;
import org.opencb.biodata.models.variant.annotation.VariantAnnotation;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.t2d.entity.VariantInfo;

import java.util.List;
import java.util.Set;

public class VariantUtils {

    private static final String RS = "rs";
    private static final String SS = "ss";

    public static String getVariantId(Variant entity) {
        Variant ensambleVariant = entity.copyInEnsemblFormat();
        return generateVariantId(ensambleVariant.getChromosome(), ensambleVariant.getStart(),
                ensambleVariant.getReference(), ensambleVariant.getAlternate());
    }

    private static String generateVariantId(String chromosome, int start, String reference, String alternate) {
        return chromosome + "_" + start + "_" + reference + "_" + alternate;
    }

    public static String getVariantId(VariantAnnotation annotation) {
        return generateVariantId(annotation.getChromosome(), annotation.getStart(), annotation.getReferenceAllele(),
                annotation.getAlternativeAllele());
    }


    public static String getDbsnpId(Variant variant) {
        Set<String> ids = variant.getIds();
        String temptativeId = null;
        for (String id : ids) {
            if (id.startsWith(RS)) {
                return id;
            }
            if (id.startsWith(SS)) {
                temptativeId = id;
            }
        }
        return temptativeId;
    }

    public static String getConsequence(List<ConsequenceType> consequenceTypes) {
        if (consequenceTypes.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getSoNames(consequenceTypes.get(0)));

        for (int i = 0; i < consequenceTypes.size(); i++) {
            stringBuilder.append(",").append(getSoNames(consequenceTypes.get(0)));
        }
        return stringBuilder.toString();
    }

    private static String getSoNames(ConsequenceType consequenceType) {
        if (consequenceType.getSoTerms().isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(consequenceType.getSoTerms().get(0).getSoName());

        for (int i = 1; i < consequenceType.getSoTerms().size(); i++) {
            stringBuilder.append(",").append(consequenceType.getSoTerms().get(i).getSoName());
        }
        return stringBuilder.toString();
    }

    public static String getClosestGene(VariantAnnotation annotation) {
        // TODO
        return null;
    }

    public static String getGene(VariantAnnotation annotation) {
        // TODO
        return null;
    }

    public static String getSiftPrediction(VariantAnnotation annotation) {
        if (annotation.getProteinSubstitutionScores() != null && annotation.getProteinSubstitutionScores()
                .getSiftEffect() != null) {
            return annotation.getProteinSubstitutionScores().getSiftEffect().toString().toLowerCase();
        }
        return null;
    }

    public static String getPolyphenPrediction(VariantAnnotation annotation) {
        if (annotation.getProteinSubstitutionScores() != null && annotation.getProteinSubstitutionScores()
                .getPolyphenEffect() != null) {
            return annotation.getProteinSubstitutionScores().getPolyphenEffect().toString().toLowerCase();
        }
        return null;
    }

    public static VariantInfo convertToVariantInfo(VariantAnnotation annotation) {
        VariantInfo variantInfo = new VariantInfo();
        variantInfo.setVariantId(getVariantId(annotation));
        variantInfo.setClosestGene(getClosestGene(annotation));
        variantInfo.setDbsnpId(annotation.getId());
        variantInfo.setChromosome(annotation.getChromosome());
        variantInfo.setPosition(annotation.getStart());
        variantInfo.setReferenceAllele(annotation.getReferenceAllele());
        variantInfo.setEffectAllele(annotation.getAlternativeAllele());
        // transcript annot NO
        variantInfo.setGene(getGene(annotation));
        // Condel - NO
        variantInfo.setConsequence(getConsequence(annotation.getConsequenceTypes()));
        variantInfo.setPolyPhenPred(getPolyphenPrediction(annotation));
        // Protein change NO
        variantInfo.setSiftPred(getSiftPrediction(annotation));
        // MostDelScore - NO
        // Display Name always null
        return variantInfo;
    }

    public static VariantInfo convertToVariantInfo(Variant variant) {
        VariantInfo variantInfo = new VariantInfo();
        variantInfo.setVariantId(getVariantId(variant));
        // Closest gene
        variantInfo.setDbsnpId(getDbsnpId(variant));
        variantInfo.setChromosome(variant.getChromosome());
        variantInfo.setPosition(variant.getStart());
        variantInfo.setReferenceAllele(variant.getReference());
        variantInfo.setEffectAllele(variant.getAlternate());
        // transcript annot NO
        // Gene - NO requires annotation
        // Condel - NO
        // Consequence NO requires annotation
        // PolyPhenPred NO requires annotation
        // Protein change NO
        // Sift pred NO requires annotation
        // MostDelScore - NO
        // Display Name always null
        return variantInfo;
    }
}
