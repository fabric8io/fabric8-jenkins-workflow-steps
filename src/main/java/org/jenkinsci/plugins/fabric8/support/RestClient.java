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

import hudson.model.TaskListener;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import static org.jenkinsci.plugins.fabric8.workflowsteps.Steps.log;

/**
 */
public class RestClient {
    private static final CloseableHttpClient singletonHttpClient;

    static {
        Collection<BasicHeader> headers = new ArrayList<BasicHeader>();
        //headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        singletonHttpClient = HttpClients.custom().setDefaultHeaders(headers).build();
    }

    public static <T extends HttpRequestBase> CloseableHttpResponse executeRequest(HttpHost host, T request)
            throws IOException {
        return singletonHttpClient.execute(host, request);
    }

    public static HttpHost getHttpHost(TaskListener listener, String name) throws UnknownHostException {
        return getHost(listener, name, "http");
    }

    public static HttpHost getHttpsHost(TaskListener listener, String name) throws UnknownHostException {
        return getHost(listener, name, "https");
    }

    public static HttpHost getHost(TaskListener listener, String name, String protocol) throws UnknownHostException {
        String envPrefix = name.toUpperCase().replace('-', '_');
        String envHost = envPrefix + "_SERVICE_HOST";
        String envPort = envPrefix + "_SERVICE_PORT";
        String host = System.getenv(envHost);
        String portText = System.getenv(envPort);
        if (host == null || portText == null) {
            log(listener, "No service " + name + "  is running!!!");
            return null;
        }
        int portNumber = Integer.parseInt(portText);
        return new HttpHost(host, portNumber, protocol);
    }

    public static String parse(CloseableHttpResponse response) throws IOException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            builder.append("status: "+ response.getStatusLine());
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                builder.append(line);
                builder.append("/n");
            }
            return builder.toString();
        } finally {
            response.close();
        }
    }
}
