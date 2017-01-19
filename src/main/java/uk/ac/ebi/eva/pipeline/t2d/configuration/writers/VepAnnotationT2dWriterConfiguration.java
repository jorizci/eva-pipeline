package uk.ac.ebi.eva.pipeline.t2d.configuration.writers;

import org.opencb.biodata.models.variant.annotation.VariantAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.VepAnnotationT2dWriter;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.VARIANT_ANNOTATION_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import(T2dDataSourceConfiguration.class)
public class VepAnnotationT2dWriterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(VepAnnotationT2dWriterConfiguration.class);

    @Bean(VARIANT_ANNOTATION_WRITER)
    @StepScope
    public ItemWriter<VariantAnnotation> variantAnnotationItemWriterT2d() {
        logger.debug("Building '" + VARIANT_ANNOTATION_WRITER + "'");
        return new VepAnnotationT2dWriter();
    }

}

