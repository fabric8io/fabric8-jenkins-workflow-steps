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
package org.jenkinsci.plugins.fabric8.prometheus;

import hudson.model.Item;
import hudson.model.Job;
import hudson.model.Run;
import hudson.util.RunList;
import io.prometheus.client.Collector;
import jenkins.model.Jenkins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 */
public class JobCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> samples = new ArrayList<MetricFamilySamples>();

        Jenkins jenkins = Jenkins.getInstance();
        StringBuilder buffer = new StringBuilder();
        if (jenkins != null) {
            List<Item> items = jenkins.getAllItems();
            if (items != null) {
                for (Item item : items) {
                    Collection<? extends Job> jobs = item.getAllJobs();
                    if (jobs != null) {
                        for (Job job : jobs) {
                            appendJobMetrics(samples, jenkins, item, job);
                        }
                    }
                }
            }
        }
        return samples;
    }

    protected void appendJobMetrics(List<MetricFamilySamples> mfsList, Jenkins jenkins, Item item, Job job) {
        List<String> labelNames = Arrays.asList("job");
        List<String> labelValues = Arrays.asList(job.getName());
        String fullname = "jenkins_builds";

        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
/*
        Run lastCompletedBuild = job.getLastCompletedBuild();
        if (lastCompletedBuild != null) {
            samples.add(new MetricFamilySamples.Sample(fullname + "_lastBuildTime", labelNames, labelValues, lastCompletedBuild.getDuration()));
        }
*/
        long count = 0;
        long duration = 0;
        RunList<Run> builds = job.getBuilds();
        if (builds != null) {
            for (Run build : builds) {
                if (!build.isBuilding()) {
                    count++;
                    duration += build.getDuration();
                }
            }
        }
        samples.add(new MetricFamilySamples.Sample(fullname + "_buildTime_sum", labelNames, labelValues, duration));
        samples.add(new MetricFamilySamples.Sample(fullname + "_buildTime_count", labelNames, labelValues, count));

        MetricFamilySamples mfs = new MetricFamilySamples(fullname, Type.SUMMARY, "Jenkins build times", samples);
        mfsList.add(mfs);
    }

}
