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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import org.jenkinsci.plugins.fabric8.dto.BuildDTO;
import org.jenkinsci.plugins.fabric8.dto.JobDTO;
import org.jenkinsci.plugins.fabric8.dto.JobMetricDTO;
import org.jenkinsci.plugins.fabric8.dto.StageDTO;
import org.jenkinsci.plugins.fabric8.support.Callback;
import org.jenkinsci.plugins.fabric8.support.FlowNodes;
import org.jenkinsci.plugins.fabric8.support.JSONHelper;
import org.jenkinsci.plugins.fabric8.support.Jobs;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 */
@Extension
public class JobAction extends ActionSupport<Job> {

    public String getUrlName() {
        return "fabric8";
    }

    @Override
    public Class<Job> type() {
        return Job.class;
    }

    @Nonnull
    @Override
    public Collection<JobAction> createFor(@Nonnull Job target) {
        try {
            JobAction action = getClass().newInstance();
            action.setTarget(target);
            List<JobAction> answer = new ArrayList<JobAction>();
            answer.add(action);
            return answer;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Object doMetrics() {
        JobMetricDTO dto = JobMetricDTO.createJobMetricsDTO(getTarget());
        return JSONHelper.jsonResponse(dto);
    }
}
