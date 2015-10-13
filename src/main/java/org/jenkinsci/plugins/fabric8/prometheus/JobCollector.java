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

import hudson.model.Job;
import hudson.model.Run;
import hudson.util.RunList;
import io.prometheus.client.Collector;
import io.prometheus.client.Summary;
import org.jenkinsci.plugins.fabric8.support.Callback;
import org.jenkinsci.plugins.fabric8.support.FlowNodes;
import org.jenkinsci.plugins.fabric8.support.Jobs;
import org.jenkinsci.plugins.fabric8.support.Runs;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jenkinsci.plugins.fabric8.support.FlowNodes.getSortedStageNodes;

/**
 */
public class JobCollector extends Collector {
    private static final String DEFAULT_NAMESPACE = "default";

    private String fullname = "builds";
    private double[] buildHistogramBuckets = { 1000, 30000, 60000 };
    private String subsystem = "jenkins";
    private String namespace;

    public JobCollector() {
        namespace = System.getenv("KUBERNETES_NAMESPACE");
        if (namespace == null || namespace.length() == 0) {
            namespace = DEFAULT_NAMESPACE;
        }
    }

    @Override
    public List<MetricFamilySamples> collect() {
        final List<MetricFamilySamples> samples = new ArrayList<MetricFamilySamples>();

        Jobs.forEachJob(new Callback<Job>() {
            @Override
            public void invoke(Job job) {
                appendJobMetrics(samples, job);
            }
        });
        return samples;
    }

    protected void appendJobMetrics(List<MetricFamilySamples> mfsList, Job job) {
        String[] labelNameArray = {"job"};
        String[] labelValueArray = {job.getName()};

/*
        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
        List<String> labelNames = Arrays.asList(labelNameArray);
        List<String> labelValues = Arrays.asList(labelValueArray);
        Run lastCompletedBuild = job.getLastCompletedBuild();
        if (lastCompletedBuild != null) {
            samples.add(new MetricFamilySamples.Sample(fullname + "_lastBuildTime", labelNames, labelValues, lastCompletedBuild.getDuration()));
        }
*/
/*
        Histogram histogram = Histogram.build().
                name(fullname + "_duration_histogram").
                subsystem(subsystem).namespace(namespace).
                labelNames(labelNameArray).
                help("Histogram of Jenkins build times by job").
                buckets(buildHistogramBuckets).
                create();
*/

        Summary summary = Summary.build().
                name(fullname + "_duration_milliseconds_summary").
                subsystem(subsystem).namespace(namespace).
                labelNames(labelNameArray).
                help("Summary of Jenkins build times in milliseconds by Job").
                create();

        Map<String,Summary> stageCollectorMap = new HashMap<String, Summary>();

        RunList<Run> builds = job.getBuilds();
        if (builds != null) {
            for (Run build : builds) {
                if (Runs.includeBuildInMetrics(build)) {
                    long buildDuration = build.getDuration();

                    //histogram.labels(labelValueArray).observe(buildDuration);
                    summary.labels(labelValueArray).observe(buildDuration);

                    if (build instanceof WorkflowRun) {
                        WorkflowRun workflowRun = (WorkflowRun) build;
                        List<FlowNode> stages = getSortedStageNodes(workflowRun.getExecution());
                        for (FlowNode stage : stages) {
                            observeStage(stageCollectorMap, job, build, stage);
                        }
                    }
                }
            }
        }
/*
        samples.add(new MetricFamilySamples.Sample(fullname + "_duration_milliseconds_sum", labelNames, labelValues, duration));
        samples.add(new MetricFamilySamples.Sample(fullname + "_duration_milliseconds_count", labelNames, labelValues, count));
*/
/*
        MetricFamilySamples mfs = new MetricFamilySamples(fullname, Type.SUMMARY, "Jenkins build times", samples);
        mfsList.add(mfs);
*/

        mfsList.addAll(summary.collect());
        Collection<Summary> stageCollectors = stageCollectorMap.values();
        for (Summary stageCollector : stageCollectorMap.values()) {
            mfsList.addAll(stageCollector.collect());
        }
        //mfsList.addAll(histogram.collect());
    }

    private void observeStage(Map<String, Summary> histogramMap, Job job, Run build, FlowNode stage) {
        String jobName = job.getName();
        String stageName = stage.getDisplayName();
        String[] labelNameArray = {"job", "stage"};
        String[] labelValueArray = {jobName, stageName};

        String key = jobName + "_" + stageName;
        Summary collector = histogramMap.get(key);
        if (collector == null) {
            collector = Summary.build().name(fullname + "_stage_duration_milliseconds_summary").
                    subsystem(subsystem).namespace(namespace).
                            labelNames(labelNameArray).
                    help("Summary of Jenkins build times by Job and Stage").
                    create();
            histogramMap.put(key, collector);
        }
        long duration = FlowNodes.getStageDuration(stage);
        collector.labels(labelValueArray).observe(duration);
    }

}
