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
package uk.ac.ebi.eva.pipeline.t2d.repository;

import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

import java.util.List;

import static uk.ac.ebi.eva.pipeline.t2d.configuration.T2dDataSourceConfiguration.T2D_TRANSACTION_MANAGER;

public interface T2dRepository {

    @Transactional(T2D_TRANSACTION_MANAGER)
    void createTable(String tableName, String... statisticParameters);

    @Transactional(T2D_TRANSACTION_MANAGER)
    void storeStatistics(String tableName, T2dStatistics statistics, String[] idKeys);

    @Transactional(T2D_TRANSACTION_MANAGER)
    void storeStatistics(String tableName, List<? extends T2dStatistics> statistics, String[] idKeys);

    @Transactional(T2D_TRANSACTION_MANAGER)
    void storeVariantId(Variant variant, String tableName);

}
