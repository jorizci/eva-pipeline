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
package uk.ac.ebi.eva.pipeline.t2d.jobs.deciders;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class SkipT2dLoadStatisticsDecider implements JobExecutionDecider {

    public static final String SKIP_STEP = "SKIP_STEP";
    public static final String DO_STEP = "DO_STEP";

    private final boolean skip;

    public SkipT2dLoadStatisticsDecider(String[] statisticFiles) {
        this.skip = statisticFiles.length == 0;
    }

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (skip) {
            return new FlowExecutionStatus(SKIP_STEP);
        } else {
            return new FlowExecutionStatus(DO_STEP);
        }
    }
}
