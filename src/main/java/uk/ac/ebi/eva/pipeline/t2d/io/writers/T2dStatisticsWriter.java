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
package uk.ac.ebi.eva.pipeline.t2d.io.writers;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;
import uk.ac.ebi.eva.pipeline.t2d.repository.DatasetMetadataRepository;

import java.util.List;

public class T2dStatisticsWriter implements ItemWriter<T2dStatistics> {

    @Autowired
    private DatasetMetadataRepository datasetMetadataRepository;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_GENERATOR + "}")
    private String t2dStudyGenerator;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_TYPE + "}")
    private String t2dStudyType;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_VERSION + "}")
    private int t2dStudyVersion;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_PHENOTYPE + ":#{null}}")
    private String t2dStudyPhenotype;

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_STATISTICS_IDS + ":#{new String[]{}}}")
    private String[] idKeys;

    @Override
    public void write(List<? extends T2dStatistics> items) throws Exception {
        datasetMetadataRepository.storeStatistics(generateStudyTableName(), items,idKeys);
    }

    private String generateStudyTableName() {
        String tableName = t2dStudyType + "_" + t2dStudyGenerator + "_mdv" + t2dStudyVersion;
        if (t2dStudyPhenotype == null) {
            return tableName.toUpperCase();
        } else {
            return (tableName + "__" + t2dStudyPhenotype).toUpperCase();
        }
    }

}
