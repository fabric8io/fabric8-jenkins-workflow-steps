/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.fabric8.rest;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.UnprotectedRootAction;
import org.jenkinsci.plugins.fabric8.dto.JobMetricDTO;
import org.jenkinsci.plugins.fabric8.support.Callback;
import org.jenkinsci.plugins.fabric8.support.JSONHelper;
import org.jenkinsci.plugins.fabric8.support.Jobs;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Extension
public class MetricsAction implements UnprotectedRootAction {
    //private CollectorRegistry collectorRegistry;
    //private JobCollector jobCollector = new JobCollector();

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Fabric8 Metrics";
    }

    @Override
    public String getUrlName() {
        return "fabric8";
    }

    public Object doMetrics() {
        final List<JobMetricDTO> answer = new ArrayList<JobMetricDTO>();
        Jobs.forEachJob(new Callback<Job>() {
            @Override
            public void invoke(Job job) {
                JobMetricDTO dto = JobMetricDTO.createJobMetricsDTO(job);
                if (dto != null) {
                    answer.add(dto);
                }
            }
        });
        return JSONHelper.jsonResponse(answer);
    }
}
