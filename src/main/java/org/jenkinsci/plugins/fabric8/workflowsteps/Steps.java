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
package org.jenkinsci.plugins.fabric8.workflowsteps;

import hudson.model.Build;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Queue;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.VariableResolver;
import org.jenkinsci.plugins.fabric8.support.Strings;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

/**
 * Some helper methods for working with steps
 */
public class Steps {
    public static void log(TaskListener listener, String message) {
        if (listener != null) {
            PrintStream logger = listener.getLogger();
            if (logger != null) {
                logger.println(message);
            }
        }
    }

    /**
     * Returns the value of tne named build parameter or the default value
     */
    public static Object buildParameterOrNull(TaskListener listener, FlowNode flowNode, Build build, String parameter, String defaultValue) {
        if (Strings.isNullOrBlank(parameter)) {
            log(listener, "No parameter name!");
        } else {
            if (build != null) {
                Map<String, String> buildVariables = build.getBuildVariables();
                log(listener, "Build variables: " + buildVariables);
                VariableResolver variableResolver = build.getBuildVariableResolver();
                if (variableResolver != null) {
                    Object value = variableResolver.resolve(parameter);
                    if (value != null) {
                        log(listener, "Resolved build variable: " + value);
                        //return value;
                    }
                }
                if (buildVariables != null) {
                    String value = buildVariables.get(parameter);
                    if (value != null) {
                        log(listener, "found build variable: " + value);
                        //return value;
                    }
                }

            } else {
                log(listener, "No build!");
            }

            ParametersAction action = null;
            if (build != null) {
                action = build.getAction(ParametersAction.class);
            } else {
                log(listener, "No build!");
            }
            if (action == null) {
                if (flowNode == null) {
                    log(listener, "No FlowNode!");
                } else {
                    action = flowNode.getAction(ParametersAction.class);
                }
            }

            if (action == null) {
                log(listener, "No ParametersAction could be found!");
            } else {
                ParameterValue parameterValue = action.getParameter(parameter);
                if (parameterValue == null) {
                    log(listener, "No ParameterValue could be found for '" + parameter + "'");
                } else {
                    return parameterValue.getValue();
                }
            }
        }
        return defaultValue;
    }

    public static Run<?, ?> $build(FlowExecution execution) throws IOException {
        if (execution != null) {
            FlowExecutionOwner owner = execution.getOwner();
            if (owner != null) {
                Queue.Executable qe = owner.getExecutable();
                if (qe instanceof Run) {
                    return (Run) qe;
                }
            }
        }
        return null;
    }


    public static Object buildParameterOrNull(TaskListener listener, FlowExecution flowExecution, String parameter, String defaultValue) throws IOException {
        if (Strings.isNullOrBlank(parameter)) {
            log(listener, "No parameter name!");
        } else {
            Run<?, ?> run = $build(flowExecution);
            ParametersAction action = null;
            if (run != null) {
                action = run.getAction(ParametersAction.class);
            } else {
                log(listener, "No $build could be found from flowExecution " + flowExecution);
            }
            if (action == null) {
                log(listener, "No ParametersAction could be found!");
            } else {
                ParameterValue parameterValue = action.getParameter(parameter);
                if (parameterValue == null) {
                    log(listener, "No ParameterValue could be found for '" + parameter + "'");
                } else {
                    return parameterValue.getValue();
                }
            }
        }
        return defaultValue;
    }
}
