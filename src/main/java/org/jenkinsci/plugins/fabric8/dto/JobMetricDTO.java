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
package org.jenkinsci.plugins.fabric8.dto;

import hudson.model.Job;
import hudson.model.Run;
import hudson.util.RunList;
import org.jenkinsci.plugins.fabric8.support.Runs;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class JobMetricDTO extends DtoSupport{
    private final String displayName;
    private final String url;
    private final long estimatedDuration;
    private final boolean building;
    private List<BuildMetricsDTO> builds = new ArrayList<BuildMetricsDTO>();

    public static JobMetricDTO createJobMetricsDTO(Job job) {
        if (job != null) {
            JobMetricDTO answer = new JobMetricDTO(job.getDisplayName(), job.getUrl(), job.getEstimatedDuration(), job.isBuilding());
            RunList<Run> builds = job.getBuilds();
            if (builds != null) {
                for (Run build : builds) {
                    if (Runs.includeBuildInMetrics(build)) {
                        BuildMetricsDTO buildDto =  BuildMetricsDTO.createBuildMetricsDTO(build);
                        if (buildDto != null) {
                            answer.addBuild(buildDto);
                        }
                    }
                }
            }
            return answer;
        }
        return null;
    }

    public JobMetricDTO(String displayName, String url, long estimatedDuration, boolean building) {
        this.displayName = displayName;
        this.url = url;
        this.estimatedDuration = estimatedDuration;
        this.building = building;
    }

    @Override
    public String toString() {
        return "JobMetricDTO{" +
                "displayName='" + displayName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void addBuild(BuildMetricsDTO buildDTO) {
        builds.add(buildDTO);
    }

    public boolean isBuilding() {
        return building;
    }

    public List<BuildMetricsDTO> getBuilds() {
        return builds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getUrl() {
        return url;
    }
}
