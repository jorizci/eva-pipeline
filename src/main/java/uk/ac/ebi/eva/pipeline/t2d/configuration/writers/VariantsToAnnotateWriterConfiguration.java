package uk.ac.ebi.eva.pipeline.t2d.configuration.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.VariantsToAnnotateWriter;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANTS_TO_ANNOTATE_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
public class VariantsToAnnotateWriterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(VariantsToAnnotateWriterConfiguration.class);

    @Bean(T2D_VARIANTS_TO_ANNOTATE_WRITER)
    public VariantsToAnnotateWriter variantsToAnnotateWriter() {
        logger.debug("Building '" + T2D_VARIANTS_TO_ANNOTATE_WRITER + "'");
        return new VariantsToAnnotateWriter();
    }

}
