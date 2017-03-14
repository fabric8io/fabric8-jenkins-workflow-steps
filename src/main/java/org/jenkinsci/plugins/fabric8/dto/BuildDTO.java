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

import com.google.common.base.Objects;
import hudson.model.Run;
import org.jenkinsci.plugins.fabric8.support.Runs;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class BuildDTO extends DtoSupport {
    private final String id;
    private final int number;
    private final String displayName;
    private final boolean building;
    private final String result;
    private final long duration;
    private final long estimatedDuration;
    private final long timeInMillis;
    private final String summary;
    private final String url;
    private final Map<String, Object> parameters;
    private List<StageDTO> stages = new ArrayList<StageDTO>();
    private List<GitChangeSetDTO> changeList;
    private List<CauseDTO> causes;

    public BuildDTO(String id, int number, String displayName, boolean building, String result, long duration, long estimatedDuration, long timeInMillis, String summary, String url, Map<String, Object> parameters) {
        this.id = id;
        this.number = number;
        this.displayName = displayName;
        this.building = building;
        this.result = result;
        this.duration = duration;
        this.estimatedDuration = estimatedDuration;
        this.timeInMillis = timeInMillis;
        this.summary = summary;
        this.url = url;
        this.parameters = parameters;
    }

    public static BuildDTO createBuildDTO(WorkflowJob job, WorkflowRun build) {
        if (build == null) {
            return null;
        }
        Run.Summary summary = build.getBuildStatusSummary();
        String summaryMessage = summary.message;
        String result = Runs.getResultText(build);
        Map<String,Object> parameters = Runs.getBuildParameters(build);

        long timeInMillis = build.getTimeInMillis();
        long buildDuration = build.getDuration();
        if (buildDuration == 0L) {
            buildDuration = System.currentTimeMillis() - timeInMillis;
        }
        BuildDTO dto = new BuildDTO(build.getId(), build.getNumber(), build.getDisplayName(), build.isBuilding(),
                result, buildDuration, build.getEstimatedDuration(),
                timeInMillis, summaryMessage, build.getUrl(), parameters);
        dto.setCauses(CauseDTO.createCauseDTOList(build.getCauses()));
        dto.setChangeList(GitChangeSetDTO.createGitChangeSetDTOList(build.getChangeSets()));
        return dto;
    }

    @Override
    public String toString() {
        return "BuildDTO{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    public void addStage(StageDTO stage) {
        for (StageDTO aStage : stages) {
            if (Objects.equal(aStage.getId(), stage.getId())) {
                // already added!
                return;
            }
        }
        stages.add(stage);
    }

    public boolean isBuilding() {
        return building;
    }

    public String getResult() {
        return result;
    }

    public int getNumber() {
        return number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    public long getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public List<StageDTO> getStages() {
        return stages;
    }

    public String getUrl() {
        return url;
    }

    public void setStages(List<StageDTO> stages) {
        this.stages = stages;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }


    public void setChangeList(List<GitChangeSetDTO> changeList) {
        this.changeList = changeList;
    }

    public List<GitChangeSetDTO> getChangeList() {
        return changeList;
    }

    public void setCauses(List<CauseDTO> causes) {
        this.causes = causes;
    }

    public List<CauseDTO> getCauses() {
        return causes;
    }
}
