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

import org.jenkinsci.plugins.fabric8.support.FlowNodes;
import org.jenkinsci.plugins.workflow.actions.StageAction;
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

/**
 */
public class StageDTO extends NodeDTO {
    private final String stageName;

    public static StageDTO createStageDTO(FlowNode node) {
        if (node != null) {
            StageAction action = node.getAction(StageAction.class);
            if (action != null) {
                String url = FlowNodes.getNodeUrlOrNull(node);
                long startTime = TimingAction.getStartTime(node);
                ErrorDTO error = ErrorDTO.createErrorDTO(node);
                FlowNode endNode = FlowNodes.getNextStageNode(node);
                long duration = FlowNodes.getDuration(node, endNode);
                if (endNode == null) {
                    endNode = node;
                }
                StatusDTO status = StatusDTO.valueOf(endNode);
                return new StageDTO(node.getId(), url, action.getStageName(), startTime, status, error, duration);
            }
        }
        return null;
    }

    public StageDTO(String id, String url, String stageName, long startTime, StatusDTO status, ErrorDTO error, long duration) {
        super(id, url, startTime, status, error, duration);
        this.stageName = stageName;
    }

    @Override
    public String toString() {
        return "StageDTO{" +
                ", id='" + getId() + '\'' +
                ", url='" + getUrl() + '\'' +
                ", stageName='" + stageName + '\'' +
                ", status=" + getStatus() +
                '}';
    }

    public String getStageName() {
        return stageName;
    }

}
