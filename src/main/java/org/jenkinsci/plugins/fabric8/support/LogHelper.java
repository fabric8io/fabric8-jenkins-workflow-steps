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
    public static final int MAX_SIZE = 1024 * 1024;

    public static HttpResponse jsonResponse(final AnnotatedLargeText text, boolean building) throws IOException {
        StaplerRequest req = Stapler.getCurrent().getCurrentRequest();
        long start = 0;
        int size = DEFAULT_SIZE;

        String startText = req.getParameter("start");
        if (startText != null) {
            start = Long.parseLong(startText);
        }
        String sizeText = req.getParameter("size");
        if (sizeText != null) {
            size = Integer.parseInt(sizeText);
        }
        if (text.length() < start) {
            start = 0;  // text rolled over
        }
        if (size < 0) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
        StringWriter buffer = new StringWriter();
        long textSize = text.writeLogTo(start, size, new LineEndNormalizingWriter(buffer));
        boolean completed = text.isComplete();
        long logLength = text.length();
        LogsDTO logsDTO = new LogsDTO(completed, textSize, logLength);
        logsDTO.setLogText( buffer.toString());
        return JSONHelper.jsonResponse(logsDTO);
    }
}
