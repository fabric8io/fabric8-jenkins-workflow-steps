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


import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import javax.inject.Inject;

/**
 * Base class
 */
public abstract class Fabric8StepExecution<T extends Fabric8Step> extends AbstractSynchronousStepExecution<Object> {
    private static final long serialVersionUID = 1L;

    @Inject
    protected transient T step;

    @StepContextParameter private transient TaskListener listener;
}