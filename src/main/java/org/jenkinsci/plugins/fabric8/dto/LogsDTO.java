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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class LogsDTO extends DtoSupport  {
    private final boolean completed;
    private final long textSize;
    private final long logLength;
    private List<String> lines;

    public LogsDTO(boolean completed, long textSize, long logLength) {
        this.completed = completed;
        this.textSize = textSize;
        this.logLength = logLength;
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getTextSize() {
        return textSize;
    }

    public long getLogLength() {
        return logLength;
    }

    public void setLogText(String text) {
        setLines(new ArrayList<String>());
        if (text != null) {
            String[] split = text.split("\n");
            if (split != null && split.length > 0) {
                setLines(Arrays.asList(split));
            }
        }
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }
}
