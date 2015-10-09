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

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class LogsDTO extends DtoSupport  {
    private final boolean completed;
    private final long start;
    private final long returnedLength;
    private final boolean backwards;
    private final long logLength;
    private List<String> lines;
    private boolean lineSplit;

    public LogsDTO(boolean completed, long start, long returnedLength, boolean backwards, long logLength) {
        this.completed = completed;
        this.start = start;
        this.returnedLength = returnedLength;
        this.backwards = backwards;
        this.logLength = logLength;
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getReturnedLength() {
        return returnedLength;
    }

    public long getLogLength() {
        return logLength;
    }

    public void setLogText(String text) {
        setLines(new ArrayList<String>());
        if (text != null && text.length() > 0) {
            lineSplit = !backwards && isCompleted() ? false : true;
            String[] split = text.split("\n");
            if (split != null && split.length > 0) {
                setLines(Arrays.asList(split));
            }
            if (backwards) {
                if (text.startsWith("\n") || text.startsWith("\n\r")) {
                    lineSplit = false;
                }
            } else {
                if (text.endsWith("\n") || text.endsWith("\n\r")) {
                    lineSplit = false;
                }
            }
        }
    }

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public long getStart() {
        return start;
    }

    public boolean isBackwards() {
        return backwards;
    }

    public boolean isLineSplit() {
        return lineSplit;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }
}
