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
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

/**
 */
public class NodeDTO extends DtoSupport {
    private final String id;
    private final String url;
    private final StatusDTO status;
    private final ErrorDTO error;
    private final long duration;
    private final long startTime;

    public static NodeDTO createNodeDTO(FlowNode node) {
        if (node != null) {
            String url = FlowNodes.getNodeUrlOrNull(node);
            long startTime = TimingAction.getStartTime(node);
            ErrorDTO error = ErrorDTO.createErrorDTO(node);
            FlowNode endNode = FlowNodes.getNextNode(node);
            long duration = 0;
            if (endNode != null) {
                duration = FlowNodes.getDuration(node, endNode);
            } else {
                endNode = node;
            }
            StatusDTO status = StatusDTO.valueOf(endNode);
            return new NodeDTO(node.getId(), url, startTime, status, error, duration);
        }
        return null;
    }

    public NodeDTO(String id, String url, long startTime, StatusDTO status, ErrorDTO error, long duration) {
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.error = error;
        this.url = url;
    }

    @Override
    public String toString() {
        return "NodeDTO{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                ", url='" + url + '\'' +
                '}';
    }

    public String getId() {
        return id;
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

    public long getDuration() {
        return duration;
    }


}
