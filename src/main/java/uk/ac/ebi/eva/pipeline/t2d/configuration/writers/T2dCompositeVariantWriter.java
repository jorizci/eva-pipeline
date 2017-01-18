package uk.ac.ebi.eva.pipeline.t2d.configuration.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.VariantStudyT2dWriter;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.VariantsToAnnotateWriter;

import java.util.Arrays;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_COMPOSITE_WRITER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANTS_TO_ANNOTATE_WRITER;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_VARIANT_STUDY_T2D_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({VariantsToAnnotateWriterConfiguration.class, VariantStudyT2dWriterConfiguration.class})
public class T2dCompositeVariantWriter {

    private static final Logger logger = LoggerFactory.getLogger(VariantsToAnnotateWriterConfiguration.class);

    @Autowired
    @Qualifier(T2D_VARIANTS_TO_ANNOTATE_WRITER)
    private VariantsToAnnotateWriter variantsToAnnotateWriter;

    @Bean(T2D_COMPOSITE_WRITER)
    @StepScope
    public CompositeItemWriter<Variant> compositeItemWriter() {
        logger.debug("Building '" + T2D_COMPOSITE_WRITER + "'");
        CompositeItemWriter<Variant> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(variantsToAnnotateWriter));
        return writer;
    }

}
