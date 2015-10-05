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

import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Result;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class WorkflowRuns {
    public static String getResultText(WorkflowRun run) {
        if (run != null) {
            Result result = run.getResult();
            if (result != null) {
                return result.toString();
            }
        }
        return null;
    }

    public static Map<String, Object> getBuildParameters(WorkflowRun build) {
        List<ParametersAction> actions = build.getActions(ParametersAction.class);
        if (actions != null) {
            Map<String, Object> answer = new HashMap<String, Object>();
            for (ParametersAction action : actions) {
                List<ParameterValue> parameters = action.getParameters();
                if (parameters != null) {
                    for (ParameterValue parameter : parameters) {
                        String name = parameter.getName();
                        Object value = parameter.getValue();
                        answer.put(name, value);
                    }
                }
            }
            return answer;
        }
        return null;
    }
}
