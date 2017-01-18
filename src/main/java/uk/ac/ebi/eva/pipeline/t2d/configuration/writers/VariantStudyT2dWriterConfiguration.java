package uk.ac.ebi.eva.pipeline.t2d.configuration.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.VariantStudyT2dWriter;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANT_STUDY_T2D_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import(T2dDataSourceConfiguration.class)
public class VariantStudyT2dWriterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(VariantStudyT2dWriterConfiguration.class);

    @Bean(T2D_VARIANT_STUDY_T2D_WRITER)
    @JobScope
    public VariantStudyT2dWriter variantStudyT2dWritter() {
        logger.debug("Building '" + T2D_VARIANT_STUDY_T2D_WRITER + "'");
        return new VariantStudyT2dWriter();
    }

}
