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
package uk.ac.ebi.eva.test.configuration.t2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@AutoConfigureDataJpa
@PropertySource({"annotation-job.properties"})
@EnableConfigurationProperties(value=BatchProperties.class)
public class T2dTestConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(T2dTestConfiguration.class);
    private static final String IN_MEMORY_DATASOURCE = "inMemoryDatasource";

    @Bean
    public JobOptions jobOptions() {
        return new JobOptions();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }

    @Bean
    private static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Autowired
    private BatchProperties properties;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    protected void initialize() {
        if (this.properties.getInitializer().isEnabled()) {
            String platform = "hsqldb";
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            String schemaLocation = this.properties.getSchema();
            schemaLocation = schemaLocation.replace("@@platform@@", platform);
            populator.addScript(this.resourceLoader.getResource(schemaLocation));
            populator.setContinueOnError(true);
            DatabasePopulatorUtils.execute(populator, inMemoryDatasource());
        }
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository());
        return simpleJobLauncher;
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(inMemoryDatasource());
        jobRepositoryFactoryBean.setTransactionManager(inMemoryTransactionManager());
        jobRepositoryFactoryBean.setDatabaseType("hsql");
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager inMemoryTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean(name = IN_MEMORY_DATASOURCE)
    @Primary
    public DataSource inMemoryDatasource() {
        return DataSourceBuilder.create().driverClassName("org.hsqldb.jdbcDriver")
                .url("jdbc:hsqldb:mem:testdb;DB_CLOSE_ON_EXIT=FALSE").username("sa").password("").build();
    }

    @Bean
    public BatchConfigurer configurer(@Qualifier(IN_MEMORY_DATASOURCE) DataSource dataSource){
        return new DefaultBatchConfigurer(dataSource);
    }

}
