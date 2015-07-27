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

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.fabric8.support.DevOps;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.jenkinsci.plugins.fabric8.support.HubotClient;

/**
 * Sends a message to hubot
 */
public class HubotApproveStep extends Fabric8Step {
    private final String message;

    @DataBoundConstructor
    public HubotApproveStep(String message) {
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
            return "hubotApprove";
        }

        @Override
        public String getDisplayName() {
            return "Sends a message with proceed/abort instructions to the hubot chat room for a project";
        }
    }

    public static class Execution extends AbstractSynchronousStepExecution<String> {

        @javax.inject.Inject
        private transient HubotApproveStep step;
        @StepContextParameter
        private transient TaskListener listener;
        @StepContextParameter
        private transient FilePath workspace;
        @StepContextParameter
        private transient EnvVars envVars;

        @Override
        protected String run() throws Exception {
            String room = HubotClient.getProjectRoom(listener, workspace);
            String jobName = DevOps.getJobName(listener, envVars);
            String buildNumber = DevOps.getBuildNumber(listener, envVars);
            String message = step.getMessage() + "\n" +
                    "    to Proceed reply:  fabric8 jenkins proceed job " + jobName + " build " + buildNumber + "\n" +
                    "    to Abort reply:    fabric8 jenkins abort job " + jobName + " build " + buildNumber + "\n";
            return HubotClient.notify(listener, room, message);
        }

        private static final long serialVersionUID = 1L;
   }
}
