package uk.ac.ebi.eva.pipeline.t2d.configuration.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration;
import uk.ac.ebi.eva.pipeline.t2d.io.writers.T2dStatisticsWriter;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_STATISTICS_WRITER;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dDataSourceConfiguration.class})
public class T2dStatisticsWriterConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(T2dStatisticsWriterConfiguration.class);

    @Bean(T2D_STATISTICS_WRITER)
    @StepScope
    public ItemWriter<? super T2dStatistics> t2dStatisticsWriter() {
        logger.debug("Building '" + T2D_STATISTICS_WRITER + "'");
        return new T2dStatisticsWriter();
    }

}
