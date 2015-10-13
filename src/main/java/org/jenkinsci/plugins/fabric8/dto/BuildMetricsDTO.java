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

import hudson.model.Run;
import org.jenkinsci.plugins.fabric8.support.Runs;

/**
 */
public class BuildMetricsDTO extends DtoSupport {
    private final String id;
    private final int number;
    private final String displayName;
    private final String result;
    private final long duration;
    private final long estimatedDuration;
    private final long timeInMillis;
    private final String url;

    public BuildMetricsDTO(String id, int number, String displayName, String result, long duration, long estimatedDuration, long timeInMillis, String url) {
        this.displayName = displayName;
        this.id = id;
        this.number = number;
        this.result = result;
        this.duration = duration;
        this.estimatedDuration = estimatedDuration;
        this.timeInMillis = timeInMillis;
        this.url = url;
    }

    public static BuildMetricsDTO createBuildMetricsDTO(Run build) {
        if (build == null) {
            return null;
        }
        String result = Runs.getResultText(build);
        return new BuildMetricsDTO(build.getId(), build.getNumber(), build.getDisplayName(),
                result, build.getDuration(), build.getEstimatedDuration(),
                build.getTimeInMillis(), build.getUrl());
    }

    @Override
    public String toString() {
        return "BuildMetricsDTO{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }


    public String getResult() {
        return result;
    }

    public int getNumber() {
        return number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    public long getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getUrl() {
        return url;
    }

}
