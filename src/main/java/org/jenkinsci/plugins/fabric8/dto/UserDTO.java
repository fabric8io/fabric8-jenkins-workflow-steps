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

import hudson.model.User;

/**
 */
public class UserDTO extends DtoSupport {
    private final String displayName;
    private final String id;
    private final String url;
    private final String fullName;
    private final String description;

    public static UserDTO createUserDTO(User user) {
        if (user != null) {
            return new UserDTO(user);
        }
        return null;
    }

    public UserDTO(User user) {
        this.displayName = user.getDisplayName();
        this.id = user.getId();
        this.url = user.getUrl();
        this.fullName = user.getFullName();
        this.description = user.getDescription();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
