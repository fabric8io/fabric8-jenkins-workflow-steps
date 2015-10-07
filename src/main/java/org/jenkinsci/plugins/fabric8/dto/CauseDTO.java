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

import hudson.model.Cause;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CauseDTO extends DtoSupport {
    private final String shortDescription;
    private String userId;
    private String userName;
    private String addr;
    private String note;
    private String upstreamProject;
    private String upstreamUrl;

    public static List<CauseDTO> createCauseDTOList(List<Cause> causes) {
        List<CauseDTO> answer = new ArrayList<CauseDTO>();
        for (Cause cause : causes) {
            CauseDTO dto = new CauseDTO(cause);
            answer.add(dto);
        }
        return answer;
    }

    public CauseDTO(Cause cause) {
        this.shortDescription = cause.getShortDescription();
        if (cause instanceof Cause.UserIdCause) {
            Cause.UserIdCause aCause = (Cause.UserIdCause) cause;
            this.userId = aCause.getUserId();
            this.userName = aCause.getUserName();
        } else if (cause instanceof Cause.RemoteCause) {
            Cause.RemoteCause aCause = (Cause.RemoteCause) cause;
            this.addr = aCause.getAddr();
            this.note = aCause.getNote();
        } else if (cause instanceof Cause.UpstreamCause) {
            Cause.UpstreamCause aCause = (Cause.UpstreamCause) cause;
            this.upstreamProject = aCause.getUpstreamProject();
            this.upstreamUrl = aCause.getUpstreamUrl();
        }
    }

    @Override
    public String toString() {
        return "CauseDTO{" +
                "shortDescription='" + shortDescription + '\'' +
                '}';
    }

    public String getAddr() {
        return addr;
    }

    public String getNote() {
        return note;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getUpstreamProject() {
        return upstreamProject;
    }

    public String getUpstreamUrl() {
        return upstreamUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}

