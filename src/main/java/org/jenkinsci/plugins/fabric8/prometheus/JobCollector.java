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
import io.prometheus.client.Histogram;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.fabric8.support.FlowNodes;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jenkinsci.plugins.fabric8.support.FlowNodes.getSortedStageNodes;

/**
 */
public class JobCollector extends Collector {
    String fullname = "jenkins_builds";
    private double[] buildHistogramBuckets = { 1000, 30000, 60000 };

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> samples = new ArrayList<MetricFamilySamples>();

        Jenkins jenkins = Jenkins.getInstance();
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
        String[] labelNameArray = {"job"};
        String[] labelValueArray = {job.getName()};
        List<String> labelNames = Arrays.asList(labelNameArray);
        List<String> labelValues = Arrays.asList(labelValueArray);

        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
/*
        Run lastCompletedBuild = job.getLastCompletedBuild();
        if (lastCompletedBuild != null) {
            samples.add(new MetricFamilySamples.Sample(fullname + "_lastBuildTime", labelNames, labelValues, lastCompletedBuild.getDuration()));
        }
*/
        Histogram histogram = Histogram.build().
                name(fullname + "_duration_histogram").
                labelNames(labelNameArray).
                help("Histogram of Jenkins build times by job").
                buckets(buildHistogramBuckets).
                create();

        Map<String,Histogram> jobHistograms = new HashMap<String, Histogram>();

        long count = 0;
        long duration = 0;
        RunList<Run> builds = job.getBuilds();
        if (builds != null) {
            for (Run build : builds) {
                if (!build.isBuilding()) {
                    count++;
                    long buildDuration = build.getDuration();
                    duration += buildDuration;

                    histogram.labels(labelValueArray).observe(buildDuration);

                    if (build instanceof WorkflowRun) {
                        WorkflowRun workflowRun = (WorkflowRun) build;
                        List<FlowNode> stages = getSortedStageNodes(workflowRun.getExecution());
                        for (FlowNode stage : stages) {
                            observeStage(jobHistograms, job, build, stage);
                        }
                    }
                }
            }
        }
        samples.add(new MetricFamilySamples.Sample(fullname + "_duration_milliseconds_sum", labelNames, labelValues, duration));
        samples.add(new MetricFamilySamples.Sample(fullname + "_duration_milliseconds_count", labelNames, labelValues, count));
        MetricFamilySamples mfs = new MetricFamilySamples(fullname, Type.SUMMARY, "Jenkins build times", samples);
        Collection<Histogram> histograms = jobHistograms.values();
        for (Histogram stageHistograms : jobHistograms.values()) {
            mfsList.addAll(stageHistograms.collect());
        }
        mfsList.add(mfs);

        mfsList.addAll(histogram.collect());
    }

    private void observeStage(Map<String, Histogram> histogramMap, Job job, Run build, FlowNode stage) {
        String jobName = job.getName();
        String stageName = stage.getDisplayName();
        String[] labelNameArray = {"job", "stage"};
        String[] labelValueArray = {jobName, stageName};

        String key = jobName + "_" + stageName;
        Histogram histogram = histogramMap.get(key);
        if (histogram == null) {
            histogram = Histogram.build().name(fullname + "_stage_duration_milliseconds_histogram").
                            labelNames(labelNameArray).
                    help("Histogram of Jenkins build times by job and stage").
                    buckets(buildHistogramBuckets).
                    create();
            histogramMap.put(key, histogram);
        }
        long duration = FlowNodes.getStageDuration(stage);
        histogram.labels(labelValueArray).observe(duration);
    }

}
