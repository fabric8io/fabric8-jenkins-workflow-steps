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

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.StringWriter;

/**
 */
public class MetricsRequest {
    public static HttpResponse jsonResponse(final CollectorRegistry collectorRegistry) {
        return new HttpResponse() {
            @Override
            public void generateResponse(StaplerRequest request, StaplerResponse response, Object node) throws IOException, ServletException {
                response.setStatus(StaplerResponse.SC_OK);
                response.setContentType(TextFormat.CONTENT_TYPE_004);

                StringWriter buffer = new StringWriter();
                TextFormat.write004(buffer, collectorRegistry.metricFamilySamples());
                buffer.close();
                response.getWriter().write(buffer.toString());
            }
        };
    }
}
