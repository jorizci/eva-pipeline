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
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;
import uk.ac.ebi.eva.pipeline.t2d.repository.DatasetMetadataRepository;

import java.util.List;

public class T2dStatisticsWriter implements ItemWriter<T2dStatistics> {

    @Autowired
    private DatasetMetadataRepository datasetMetadataRepository;

    private final String database;

    public T2dStatisticsWriter(String database) {
        this.database = database;
    }

    @Override
    public void write(List<? extends T2dStatistics> items) throws Exception {
        datasetMetadataRepository.storeStatistics(database, items);
    }

}
