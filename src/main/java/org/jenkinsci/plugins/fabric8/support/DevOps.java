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

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.TaskListener;

import java.io.IOException;

import static org.jenkinsci.plugins.fabric8.workflowsteps.Steps.log;


/**
 * Some helper methods for working with steps
 */
public class DevOps {
    public static String loadFabric8YFile(TaskListener listener, FilePath workspace) throws IOException, InterruptedException {
        FilePath ymlFile = workspace.child("fabric8.yml");
        if (ymlFile.exists() && !ymlFile.isDirectory()) {
            return ymlFile.readToString();
        }
        return null;
    }

    public static String yamlValue(String yamlFile, String name) {
        if (yamlFile != null) {
            String[] lines = yamlFile.split("\n");
            if (lines != null) {
                String prefix = name + ":";
                for (String line : lines) {
                    String text = line.trim();
                    if (text.startsWith(prefix)) {
                        return text.substring(prefix.length()).trim();
                    }
                }
            }
        }
        return null;
    }

    public static String getProjectRoom(TaskListener listener, FilePath workspace) throws IOException, InterruptedException {
        String yaml = loadFabric8YFile(listener, workspace);
        return yamlValue(yaml, "");
    }

    public static String getJobName(TaskListener listener, EnvVars envVars) {
        return envVars.get("JOB_NAME");
    }

    public static String getBuildNumber(TaskListener listener, EnvVars envVars) {
        String answer = envVars.get("BUILD_NUMBER");
        if (answer == null) {
            log(listener, "No BUILD_NUMBER!");
            return "1";
        }
        return answer;
    }
}
