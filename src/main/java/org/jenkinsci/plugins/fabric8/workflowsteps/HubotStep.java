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
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Sends a message to hubot
 */
public class HubotStep extends Fabric8Step {
    private final String room;
    private final String message;

    @DataBoundConstructor
    public HubotStep(String room, String message) {
        this.room = room;
        this.message = message;
    }

    public String getRoom() {
        return room;
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
            return "hubot";
        }

        @Override
        public String getDisplayName() {
            return "Sends a message to a hubot chat room";
        }
    }

    public static class Execution extends AbstractSynchronousStepExecution<String> {

        @javax.inject.Inject
        private transient HubotStep step;
        @StepContextParameter
        private transient TaskListener listener;

        @Override
        protected String run() throws Exception {
            return HubotClient.notify(listener, step.getRoom(), step.getMessage());
        }

        private static final long serialVersionUID = 1L;
   }
}
