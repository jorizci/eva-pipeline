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
package uk.ac.ebi.eva.pipeline.t2d.jobs.flows;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import uk.ac.ebi.eva.pipeline.Application;
import uk.ac.ebi.eva.pipeline.parameters.JobParametersNames;
import uk.ac.ebi.eva.pipeline.t2d.jobs.deciders.SkipT2dLoadStatisticsDecider;
import uk.ac.ebi.eva.pipeline.t2d.jobs.steps.T2dLoadStatistics;

import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_STATISTICS_FLOW;
import static uk.ac.ebi.eva.pipeline.configuration.BeanNames.T2D_LOAD_STATISTICS_STEP;

@Configuration
@Profile(Application.T2D_PROFILE)
@EnableBatchProcessing
@Import({T2dLoadStatistics.class})
public class T2dLoadStatisticsFlow {

    @Value("${" + JobParametersNames.T2D_INPUT_STUDY_STATISTICS + ":#{new String[]{}}}")
    private String[] t2dStatisticsFiles;

    @Autowired
    @Qualifier(T2D_LOAD_STATISTICS_STEP)
    private Step t2dLoadStatistics;

    @Bean(T2D_LOAD_STATISTICS_FLOW)
    Flow t2dLoadStatisticsFlow() {
        SkipT2dLoadStatisticsDecider skipT2dLoadStatisticsDecider = new SkipT2dLoadStatisticsDecider(t2dStatisticsFiles);

        return new FlowBuilder<Flow>(T2D_LOAD_STATISTICS_FLOW)
                .start(skipT2dLoadStatisticsDecider).on(SkipT2dLoadStatisticsDecider.DO_STEP)
                .to(t2dLoadStatistics)
                .from(skipT2dLoadStatisticsDecider).on(SkipT2dLoadStatisticsDecider.SKIP_STEP)
                .end(BatchStatus.COMPLETED.toString())
                .build();
    }

}
