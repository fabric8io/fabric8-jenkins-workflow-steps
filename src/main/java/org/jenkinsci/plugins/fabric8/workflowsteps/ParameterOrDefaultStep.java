/**
 * Copyright 2005-2015 Red Hat, Inc.
 * <p/>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.jenkinsci.plugins.fabric8.workflowsteps;

import hudson.Extension;
import hudson.model.Build;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.fabric8.support.HubotClient;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Returns the current parameter value or the default value
 */
public class ParameterOrDefaultStep extends Fabric8Step {
    private final String parameter;
    private final String defaultValue;

    @DataBoundConstructor
    public ParameterOrDefaultStep(String parameter, String defaultValue) {
        this.parameter = parameter;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getParameter() {
        return parameter;
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(Execution.class);
        }

        @Override
        public String getFunctionName() {
            return "parameterOrDefault";
        }

        @Override
        public String getDisplayName() {
            return "Get the build parameter or return the default value if there is no build parameter";
        }
    }

    public static class Execution extends AbstractSynchronousStepExecution<Object> {

        @javax.inject.Inject
        private transient ParameterOrDefaultStep step;
        @StepContextParameter
        private transient TaskListener listener;
        @StepContextParameter
        private transient FlowExecution flowExecution;

        @Override
        protected Object run() throws Exception {
            return Steps.buildParameterOrNull(listener, flowExecution, step.getParameter(), step.getDefaultValue());
        }

        private static final long serialVersionUID = 1L;
   }
}
