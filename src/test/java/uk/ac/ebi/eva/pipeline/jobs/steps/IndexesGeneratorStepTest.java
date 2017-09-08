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
package uk.ac.ebi.eva.pipeline.jobs.steps;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DuplicateKeyException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.pipeline.configuration.BeanNames;
import uk.ac.ebi.eva.pipeline.jobs.DatabaseInitializationJob;
import uk.ac.ebi.eva.pipeline.jobs.steps.tasklets.IndexesGeneratorStep;
import uk.ac.ebi.eva.pipeline.parameters.JobOptions;
import uk.ac.ebi.eva.test.configuration.BatchTestConfiguration;
import uk.ac.ebi.eva.test.rules.TemporaryMongoRule;

import static org.junit.Assert.assertEquals;


/**
 * Test {@link IndexesGeneratorStep}
 */
@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:initialize-database.properties"})
@ContextConfiguration(classes = {DatabaseInitializationJob.class, BatchTestConfiguration.class})
public class IndexesGeneratorStepTest {

    @Rule
    public TemporaryMongoRule mongoRule = new TemporaryMongoRule();

    @Autowired
    public JobOptions jobOptions;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Before
    public void setUp() throws Exception {
        jobOptions.loadArgs();
    }

    @Test
    public void testIndexesAreCreated() throws Exception {
        jobOptions.setDbName(mongoRule.getRandomTemporaryDatabaseName());

        String dbCollectionGenesName = jobOptions.getDbCollectionsFeaturesName();
        JobExecution jobExecution = jobLauncherTestUtils.launchStep(BeanNames.CREATE_DATABASE_INDEXES_STEP);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        DBCollection genesCollection = mongoRule.getCollection(jobOptions.getDbName(), dbCollectionGenesName);
        assertEquals("[{ \"v\" : 1 , \"key\" : { \"_id\" : 1} , \"name\" : \"_id_\" , \"ns\" : \"" +
                        jobOptions.getDbName() + "." + dbCollectionGenesName +
                        "\"}, { \"v\" : 1 , \"key\" : { \"name\" : 1} , \"name\" : \"name_1\" , \"ns\" : \"" +
                        jobOptions.getDbName() + "." + dbCollectionGenesName + "\" , \"sparse\" : true , \"background\" : true}]",
                genesCollection.getIndexInfo().toString());
    }

    @Test(expected = DuplicateKeyException.class)
    public void testNoDuplicatesCanBeInserted() throws Exception {
        jobOptions.setDbName(mongoRule.getRandomTemporaryDatabaseName());
        String dbCollectionGenesName = jobOptions.getDbCollectionsFeaturesName();
        JobExecution jobExecution = jobLauncherTestUtils.launchStep(BeanNames.CREATE_DATABASE_INDEXES_STEP);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        DBCollection genesCollection = mongoRule.getCollection(jobOptions.getDbName(), dbCollectionGenesName);
        genesCollection.insert(new BasicDBObject("_id", "example_id"));
        genesCollection.insert(new BasicDBObject("_id", "example_id"));
    }
}
