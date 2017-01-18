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
package uk.ac.ebi.eva.pipeline.t2d.io.readers;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.eva.pipeline.t2d.entity.VariantsToAnnotate;
import uk.ac.ebi.eva.pipeline.t2d.repository.VariantsToannotateRepository;

import java.util.HashMap;
import java.util.Map;

public class VariantsToAnnotateReader extends RepositoryItemReader<VariantsToAnnotate> {

    @Autowired
    private VariantsToannotateRepository variantsToannotateRepository;

    public VariantsToAnnotateReader() {
        setRepository(variantsToannotateRepository);
        setMethodName("findAll");
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        setSort(sorts);
    }
}
