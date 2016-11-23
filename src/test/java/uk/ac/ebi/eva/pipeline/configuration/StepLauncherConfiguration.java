package uk.ac.ebi.eva.pipeline.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestDatabaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import uk.ac.ebi.eva.test.utils.StepLauncherTestUtils;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@Import({TestDatabaseAutoConfiguration.class})
//@Import({BatchAutoConfiguration.class, EmbeddedDataSourceConfiguration.class })
public class StepLauncherConfiguration {

    private static final String HSQL = "hsql";



//    @Autowired
//    private DataSource datasource;
//
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//    @Bean
//    JobRepositoryFactoryBean jobRepositoryFactoryBean() {
//        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
//        jobRepositoryFactoryBean.setDataSource(datasource);
//        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
//        jobRepositoryFactoryBean.setDatabaseType(HSQL);
//        return jobRepositoryFactoryBean;
//    }
//
//    @Bean
//    JobLauncher jobLauncher() throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepositoryFactoryBean().getObject());
//        return jobLauncher;
//    }

    @Bean
    StepLauncherTestUtils stepLauncherTestUtils() {
        return new StepLauncherTestUtils();
    }

}
