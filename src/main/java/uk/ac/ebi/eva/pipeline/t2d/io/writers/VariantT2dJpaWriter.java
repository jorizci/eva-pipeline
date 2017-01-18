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

import org.opencb.biodata.models.variant.VariantSource;
import uk.ac.ebi.eva.commons.models.data.Variant;
import uk.ac.ebi.eva.pipeline.t2d.utils.T2dInsertStatisticsQueryGenerator;

import javax.persistence.EntityManagerFactory;

/**
 * Created by jorizci on 03/11/16.
 */
public class VariantT2dJpaWriter extends JpaQueryWriter<Variant> {

    public VariantT2dJpaWriter(EntityManagerFactory entityManagerFactory, VariantSource variantSource) {
        super(entityManagerFactory, new T2dInsertStatisticsQueryGenerator(variantSource));
    }
}
