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
package uk.ac.ebi.eva.pipeline.configuration;

/**
 * This class contains the name definition for the beans that are injected through the pipeline.
 */
public class BeanNames {

    public static final String GENE_READER = "gene-reader";
    public static final String NON_ANNOTATED_VARIANTS_READER = "non-annotated-variants-reader";
    public static final String VARIANT_ANNOTATION_READER = "variant-annotation-reader";
    public static final String VARIANT_READER = "variant-reader";

    public static final String GENE_WRITER = "gene-writer";
    public static final String VEP_INPUT_WRITER = "vep-input-writer";
    public static final String VARIANT_ANNOTATION_WRITER = "variant-annotation-writer";
    public static final String VARIANT_WRITER = "variant-writer";

    public static final String ANNOTATION_SKIP_STEP_DECIDER = "annotation-skip-step-decider";
    public static final String STATISTICS_SKIP_STEP_DECIDER = "statistics-skip-step-decider";

    public static final String VEP_ANNOTATION_FLOW = "vep-annotation-flow";
    public static final String VEP_ANNOTATION_OPTIONAL_FLOW = "vep-annotation-optional.flow";
    public static final String PARALLEL_STATISTICS_AND_ANNOTATION = "parallel-statistics-and-annotation-flow";
    public static final String CALCULATE_STATISTICS_FLOW = "calculate-statistics-flow";
    public static final String CALCULATE_STATISTICS_OPTIONAL_FLOW = "calculate-statistics-optional-flow";

    public static final String LOAD_VEP_ANNOTATION_STEP = "load-vep-annotation-step";
    public static final String CALCULATE_STATISTICS_STEP = "calculate-statistics-step";
    public static final String CREATE_DATABASE_INDEXES_STEP = "create-database-indexes-step";
    public static final String GENES_LOAD_STEP = "genes-load-step";
    public static final String GENERATE_VEP_ANNOTATION_STEP = "generate-vep-annotation";
    public static final String LOAD_STATISTICS_STEP = "load-statistics-step";
    public static final String LOAD_VARIANTS_STEP = "load-variants-step";
    public static final String LOAD_FILE_STEP = "load-file-step";
    public static final String GENERATE_VEP_INPUT_STEP = "generate-vep-input-step";

    public static final String AGGREGATED_VCF_JOB = "aggregated-vcf-job";
    public static final String ANNOTATE_VARIANTS_JOB = "annotate-variants-job";
    public static final String INIT_DATABASE_JOB = "init-database-job";
    public static final String GENOTYPED_VCF_JOB = "genotyped-vcf-job";
    public static final String CALCULATE_STATISTICS_JOB = "calculate-statistics-job";

    //t2d bean names
    public static final String T2D_JOB = "t2d-job";
    public static final String T2D_LOAD_STATISTICS_FLOW = "t2d-load-statistics-flow";

    public static final String T2D_LOAD_VCF_STEP = "t2d-load-vcf-step";
    public static final String T2D_PREPARE_DATABASE_STEP = "t2d-prepare-database-step";
    public static final String T2D_LOAD_STATISTICS_STEP = "t2d-load-statistics-step";
//    public static final String T2D_GENERATE_VEP_INPUT_STEP = "t2d-generate-vep-input-step";

    public static final String T2D_STATISTICS_READER = "t2d-statistics-reader";
    public static final String T2D_VCF_READER = "t2d-vcf-reader";
    public static final String T2D_VARIANTS_TO_ANNOTATE_READER = "t2d-variants-to-annotate-reader";
    public static final String T2D_MULTI_STATISTIC_FILES_READER = "t2d-multi-statistic-files-reader";

    public static final String T2D_STATISTICS_WRITER = "t2d-statistics-writer";
    public static final String T2D_VARIANTS_TO_ANNOTATE_WRITER = "t2d-variants-to-annotate-writer";
    public static final String T2D_VARIANT_STUDY_T2D_WRITER = "t2d-variant-study-writer";
    public static final String T2D_COMPOSITE_WRITER = "t2d-composite-writer";
//    public static final String T2D_VEP_ANNOTATION_WRITER = "t2d-vep-annotation-writer";

}
