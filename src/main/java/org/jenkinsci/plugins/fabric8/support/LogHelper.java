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

import org.jenkinsci.plugins.fabric8.dto.LogsDTO;
import org.jenkinsci.plugins.fabric8.support.hack.AnnotatedLargeText;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.framework.io.LineEndNormalizingWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 */
public class LogHelper {

    public static final int DEFAULT_SIZE = 1024 * 10;
    public static final int MAX_SIZE = 1024 * 300;

    public static HttpResponse jsonResponse(final AnnotatedLargeText text, boolean building, boolean html) throws IOException {
        StaplerRequest req = Stapler.getCurrent().getCurrentRequest();
        if (html) {
            req.setAttribute("html", Boolean.valueOf(true));
        }


        long start = getLongParameter(req, "start", 0);
        long first = getLongParameter(req, "first", -1);
        int size = getIntParameter(req, "size", DEFAULT_SIZE);
        boolean tail = getBoolParameter(req, "tail");

        if (text.length() < start) {
            start = 0;  // text rolled over
        }
        if (size < 0) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
        boolean backwards = false;
        long logLength = text.length();
        if (tail) {
            if (start == logLength) {
                // lets return earlier data!
                // if we've data to be read before 'first'
                if (first > 0) {
                    backwards = true;
                    start = first - size;
                    if (first < size) {
                        // lets not return too much data as we're getting the first little chunk
                        size = (int) first;
                    }
                }
            }
            // lets support jumping to the end of the file if its long
            if (start == 0) {
                long end = start + size;
                if (end < logLength) {
                    start = logLength - size;
                }
            }
        }
        if (start < 0) {
            start = 0;
        }
        StringWriter buffer = new StringWriter();
        long textSize = text.writeLogTo(start, size, new LineEndNormalizingWriter(buffer));
        boolean completed = text.isComplete();
        long returnedLength = textSize - start;
        LogsDTO logsDTO = new LogsDTO(completed, start, returnedLength, backwards, logLength);
        logsDTO.setLogText(buffer.toString());
        return JSONHelper.jsonResponse(logsDTO);
    }

    public static int getIntParameter(StaplerRequest req, String name, int defaultValue) {
        String text = req.getParameter(name);
        if (isNotBlank(text)) {
            return Integer.parseInt(text);
        }
        return defaultValue;
    }

    public static long getLongParameter(StaplerRequest req, String name, int defaultValue) {
        String text = req.getParameter(name);
        if (isNotBlank(text)) {
            return Long.parseLong(text);
        }
        return defaultValue;
    }

    public static boolean getBoolParameter(StaplerRequest req, String name) {
        String text = req.getParameter(name);
        if (isNotBlank(text) && (text.toLowerCase().startsWith("t") || text.equals("1"))) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String text) {
        return text != null && text.length() > 0;
    }
}
