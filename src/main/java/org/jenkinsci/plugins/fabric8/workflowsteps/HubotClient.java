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
package org.jenkinsci.plugins.fabric8.workflowsteps;

import hudson.model.TaskListener;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static org.jenkinsci.plugins.fabric8.workflowsteps.Steps.log;

/**
 */
public class HubotClient {
    public static final String ROOM_SLUG_PREFIX = "%23";

    public static String notify(TaskListener listener, String room, String message) throws java.io.IOException {
        log(listener, "Hubot sending to room " + room + " => " + message);
        if (Strings.isNullOrBlank(room)) {
            log(listener, "you must specify a room!");
            return null;
        }
        if (Strings.isNullOrBlank(message)) {
            log(listener, "you must specify a message!");
            return null;
        }
        HttpHost host = RestClient.getHttpHost(listener, "hubot-web-hook");
        if (host != null) {
            String roomWithSlugPrefix = room;
            if (roomWithSlugPrefix.startsWith("#")) {
                roomWithSlugPrefix = roomWithSlugPrefix.substring(1);
            }
            if (!roomWithSlugPrefix.startsWith(ROOM_SLUG_PREFIX)) {
                roomWithSlugPrefix =  ROOM_SLUG_PREFIX + roomWithSlugPrefix;
            }
            HttpPost post = new HttpPost("/hubot/notify/" + roomWithSlugPrefix);
            log(listener, "Posting to " + host.toString() + " url: " + post.getURI());

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("message", message));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            CloseableHttpResponse response = RestClient.executeRequest(host, post);
            String result = RestClient.parse(response);
            log(listener, "Result: " + result);
            return result;
        } else {
            log(listener, "No service found!");
            return null;
        }
    }
}
