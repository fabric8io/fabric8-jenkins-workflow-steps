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

import org.jenkinsci.plugins.workflow.actions.StageAction;
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

import java.io.IOException;

/**
 */
public class StageDTO extends DtoSupport {
    private final String id;
    private final String url;
    private final String stageName;
    private final long startTime;
    private final StatusDTO status;
    private final ErrorDTO error;

    public static StageDTO createStageDTO(FlowNode node) {
        if (node != null) {
            StageAction action = node.getAction(StageAction.class);
            if (action != null) {
                String url = null;
                try {
                    url = node.getUrl();
                } catch (IOException e) {
                    System.out.println("Failed to create node URL: " + e);
                }
                long startTime = TimingAction.getStartTime(node);
                StatusDTO status = StatusDTO.valueOf(node, startTime);
                ErrorDTO error = ErrorDTO.createErrorDTO(node);
                return new StageDTO(node.getId(), url, action.getStageName(), startTime, status, error);
            }
        }
        return null;
    }

    public StageDTO(String id, String url, String stageName, long startTime, StatusDTO status, ErrorDTO error) {
        this.id = id;
        this.url = url;
        this.stageName = stageName;
        this.startTime = startTime;
        this.status = status;
        this.error = error;
    }

    @Override
    public String toString() {
        return "StageDTO{" +
                "error=" + error +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", stageName='" + stageName + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getStageName() {
        return stageName;
    }

    public String getUrl() {
        return url;
    }

    public long getStartTime() {
        return startTime;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public ErrorDTO getError() {
        return error;
    }
}
