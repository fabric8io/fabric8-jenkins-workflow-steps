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
package org.jenkinsci.plugins.fabric8.support;

import org.jenkinsci.plugins.workflow.actions.StageAction;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

import java.util.List;

/**
 * Helper methods for working with flow nodes
 */
public class FlowNodes {
    /**
     * Returns true if the given node is a stage node
     */
    public static boolean isStageNode(FlowNode node) {
        return node != null && node.getAction(StageAction.class) != null;
    }

    /**
     * Traverses through all the nodes invoking the given callback
     */
    public static void forEach(FlowExecution execution, Callback<FlowNode> callback) {
        if (execution != null) {
            forEach(execution.getCurrentHeads(), callback);
        }
    }

    /**
     * Traverses through all the nodes invoking the given callback
     */
    public static void forEach(List<FlowNode> nodes, Callback<FlowNode> callback) {
        if (nodes != null) {
            for (FlowNode node : nodes) {
                callback.invoke(node);
                forEach(node.getParents(), callback);
            }
        }
    }

    public static boolean isExecuted(FlowNode node, long startTime) {
        return startTime > 0;
    }
}
