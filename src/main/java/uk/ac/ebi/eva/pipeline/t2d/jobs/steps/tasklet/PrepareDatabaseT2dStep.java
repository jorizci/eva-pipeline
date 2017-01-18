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
package uk.ac.ebi.eva.pipeline.t2d.jobs.steps.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.entity.DatasetMetadata;
import uk.ac.ebi.eva.pipeline.t2d.entity.DatasetPhenotypeToTable;
import uk.ac.ebi.eva.pipeline.t2d.entity.Phenotype;
import uk.ac.ebi.eva.pipeline.t2d.entity.Property;
import uk.ac.ebi.eva.pipeline.t2d.entity.PropertyToDataset;
import uk.ac.ebi.eva.pipeline.t2d.exceptions.DatasetAlreadyExists;
import uk.ac.ebi.eva.pipeline.t2d.repository.DatasetMetadataRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.DatasetPhenotypeToTableRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.PhenotypeRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.PropertyRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.PropertyToDatasetAndPhenotypeRepository;
import uk.ac.ebi.eva.pipeline.t2d.repository.PropertyToDatasetRepository;
import uk.ac.ebi.eva.pipeline.t2d.utils.T2dFileUtils;

public class PrepareDatabaseT2dStep implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(PrepareDatabaseT2dStep.class);

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_GENERATOR + "}")
    private String t2dStudyGenerator;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_TYPE + "}")
    private String t2dStudyType;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_VERSION + "}")
    private int t2dStudyVersion;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_PHENOTYPE + ":#{null}}")
    private String t2dStudyPhenotype;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_STATISTICS + ":#{new String[]{}}}")
    private String[] t2dStatisticsFiles;

    @Autowired
    private DatasetMetadataRepository datasetMetadataRepository;

    @Autowired
    private DatasetPhenotypeToTableRepository datasetPhenotypeToTableRepository;

    @Autowired
    private PhenotypeRepository phenotypeRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyToDatasetRepository propertyToDatasetRepository;

    @Autowired
    private PropertyToDatasetAndPhenotypeRepository propertyToDatasetAndPhenotypeRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        DatasetMetadata datasetMetadata = generateMetadata();
        prepareTable(datasetMetadata);
        preparePropertiesFromStatisticFiles(datasetMetadata, t2dStatisticsFiles);
        return RepeatStatus.FINISHED;
    }

    private void preparePropertiesFromStatisticFiles(DatasetMetadata datasetMetadata, String... t2dStatisticsFiles) {
        if (t2dStudyPhenotype == null) {
            for (String file : t2dStatisticsFiles) {
                preparePropertiesDataset(datasetMetadata, T2dFileUtils.getStudyProperties(file));
            }
        } else {
            for (String file : t2dStatisticsFiles) {
                preparePropertiesDatasetPhenotype(datasetMetadata, T2dFileUtils.getStudyProperties(file));
            }
        }
    }

    private void preparePropertiesDataset(DatasetMetadata datasetMetadata, String... properties) {
        for (String propertyId : properties) {
            prepareProperty(propertyId);
            propertyToDatasetRepository.save(new PropertyToDataset(datasetMetadata, propertyId));
        }
    }

    private void preparePropertiesDatasetPhenotype(DatasetMetadata datasetMetadata, String... properties) {
        for (String propertyId : properties) {
            prepareProperty(propertyId);
            propertyToDatasetRepository.save(new PropertyToDataset(datasetMetadata, propertyId));
        }
    }

    private void prepareProperty(String propertyId) {
        if (!propertyRepository.exists(propertyId)) {
            logger.info("Property type '" + propertyId + "' doesn't exist, creating a new property");
            propertyRepository.save(new Property(propertyId));
        }
    }

    private void prepareTable(DatasetMetadata datasetMetadata) {
        String tableName = datasetMetadata.getTableName();
        if (t2dStudyPhenotype != null) {
            DatasetPhenotypeToTable datasetPhenotypeToTable = generateDatasetPhenotype(datasetMetadata,
                    t2dStudyPhenotype);
            tableName = datasetPhenotypeToTable.getTableName();
        }
        datasetMetadataRepository.createTable(tableName, T2dFileUtils.getStudyProperties(t2dStatisticsFiles));
    }

    private DatasetPhenotypeToTable generateDatasetPhenotype(DatasetMetadata datasetMetadata, String phenotypeId) {
        preparePhenotypeTable(phenotypeId);

        DatasetPhenotypeToTable datasetPhenotypeToTable = new DatasetPhenotypeToTable(datasetMetadata,
                t2dStudyPhenotype);
        datasetPhenotypeToTableRepository.save(datasetPhenotypeToTable);
        return datasetPhenotypeToTable;
    }

    private void preparePhenotypeTable(String phenotypeId) {
        if (!phenotypeRepository.exists(phenotypeId)) {
            logger.info("Phenotype type '" + phenotypeId + "' doesn't exist, creating a new phenotypeId");
            phenotypeRepository.save(new Phenotype(phenotypeId));
        }
    }

    private DatasetMetadata generateMetadata() throws DatasetAlreadyExists {
        DatasetMetadata datasetMetadata = new DatasetMetadata(t2dStudyGenerator, t2dStudyType, t2dStudyVersion);
        datasetMetadataRepository.save(datasetMetadata);

        return datasetMetadata;
    }
}
