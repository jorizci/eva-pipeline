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

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;
import uk.ac.ebi.eva.pipeline.t2d.io.mappers.T2dStatisticsLineMapper;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

import static uk.ac.ebi.eva.pipeline.t2d.utils.T2dFileUtils.getStudyProperties;

public class T2dStatisticsReader extends FlatFileItemReader<T2dStatistics> {

    private final T2dStatisticsLineMapper lineMapper;
    private Resource resource;

    public T2dStatisticsReader() {
        //Set line mapper and configure reader to skip header
        lineMapper = new T2dStatisticsLineMapper();
        setLineMapper(lineMapper);
        setLinesToSkip(1);
    }

    @Override
    protected void doOpen() throws Exception {
        //Configure line mapper.
        lineMapper.setHeaderParameters(getStudyProperties(new String[]{resource.getFile().getAbsolutePath()}));
        super.doOpen();
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        super.setResource(resource);
    }
}
