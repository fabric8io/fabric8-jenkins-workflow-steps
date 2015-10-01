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
import org.jenkinsci.plugins.workflow.actions.ErrorAction;
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException;

/**
 */
public enum StatusDTO {
    SUCCESS,
    INTERUPTED,
    FAILED,
    PENDING,
    NOT_STARTED;


    public static StatusDTO valueOf(FlowNode node, long startTime) {
        if (FlowNodes.isExecuted(node, startTime)) {
            return valueOf(node.getError());
        } else {
            return NOT_STARTED;
        }
    }

    public static StatusDTO valueOf(FlowNode node) {
        return valueOf(node, TimingAction.getStartTime(node));
    }

    public static StatusDTO valueOf(ErrorAction errorAction) {
        if (errorAction == null) {
            return SUCCESS;
        }
        return valueOf(errorAction.getError());
    }

    public static StatusDTO valueOf(Throwable exception) {
        if (exception instanceof FlowInterruptedException) {
            return INTERUPTED;
        }
        return FAILED;
    }

}
