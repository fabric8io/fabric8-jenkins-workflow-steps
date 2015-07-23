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
import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;

import static org.jenkinsci.plugins.fabric8.workflowsteps.Systems.getEnvOrIfBlankDefault;

/**
 * Sends a message to hubot on the current projects room by discovering the
 * `fabric8.yml` file in the current working directory
 */
public class HubotProjectStep extends Fabric8Step {
    private final String message;

    @DataBoundConstructor
    public HubotProjectStep(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(Execution.class);
        }

        @Override
        public String getFunctionName() {
            return "hubotProject";
        }

        @Override
        public String getDisplayName() {
            return "Sends a message to the default project chat room for a project";
        }
    }

    public static class Execution extends AbstractSynchronousStepExecution<String> {

        @javax.inject.Inject
        private transient HubotProjectStep step;
        @StepContextParameter
        private transient TaskListener listener;
        @StepContextParameter
        private transient FilePath workspace;

        @Override
        protected String run() throws Exception {
            String room = DevOps.getProjectRoom(listener, workspace);
            if (Strings.isNullOrBlank(room)) {
                room = getEnvOrIfBlankDefault("FABRIC8_DEFAULT_HUBOT_ROOM", "fabric8_default");
            }
            return HubotClient.notify(listener, room, step.getMessage());
        }

        private static final long serialVersionUID = 1L;
    }
}
