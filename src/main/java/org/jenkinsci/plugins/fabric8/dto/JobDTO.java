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

import org.jenkinsci.plugins.workflow.job.WorkflowJob;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class JobDTO {
    private final String displayName;
    private final String url;
    private final long estimatedDuration;
    private final boolean building;
    private List<BuildDTO> builds = new ArrayList<BuildDTO>();

    public static JobDTO createJobDTO(WorkflowJob job) {
        if (job != null) {
            return new JobDTO(job.getDisplayName(), job.getUrl(), job.getEstimatedDuration(), job.isBuilding());
        }
        return null;
    }

    public JobDTO(String displayName, String url, long estimatedDuration, boolean building) {
        this.displayName = displayName;
        this.url = url;
        this.estimatedDuration = estimatedDuration;
        this.building = building;
    }

    @Override
    public String toString() {
        return "JobDTO{" +
                "displayName='" + displayName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void addBuild(BuildDTO buildDTO) {
        builds.add(buildDTO);
    }

    public boolean isBuilding() {
        return building;
    }

    public List<BuildDTO> getBuilds() {
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
