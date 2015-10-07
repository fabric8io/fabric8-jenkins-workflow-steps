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

import hudson.plugins.git.GitChangeSet;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class GitChangeSetDTO extends DtoSupport {
    private final String id;
    private final Collection<String> affectedPaths;
    private final String branch;
    private final UserDTO userDTO;
    private final String authorName;
    private final String comment;
    private final String commitId;
    private final String date;
    private final String message;
    private final String revision;
    private final long timestamp;

    public static List<GitChangeSetDTO> createGitChangeSetDTOList(List<ChangeLogSet<? extends ChangeLogSet.Entry>> changeSet) {
        List<GitChangeSetDTO> answer = new ArrayList<GitChangeSetDTO>();
        if (changeSet != null) {
            for (ChangeLogSet<? extends ChangeLogSet.Entry> change : changeSet) {
                for (Entry entry : change) {
                    if (entry instanceof GitChangeSet) {
                        GitChangeSet gitChangeSet = (GitChangeSet) entry;
                        GitChangeSetDTO dto = createChangeDTO(gitChangeSet);
                        answer.add(dto);
                    }
                }
            }
        }
        return answer;
    }

    public static GitChangeSetDTO createChangeDTO(GitChangeSet changeSet) {
        return new GitChangeSetDTO(changeSet.getId(),
                changeSet.getAffectedPaths(),
                changeSet.getBranch(),
                UserDTO.createUserDTO(changeSet.getAuthor()),
                changeSet.getAuthorName(),
                changeSet.getComment(),
                changeSet.getCommitId(),
                changeSet.getDate(),
                changeSet.getMsg(),
                changeSet.getRevision(),
                changeSet.getTimestamp());
    }

    public GitChangeSetDTO(String id, Collection<String> affectedPaths, String branch, UserDTO userDTO, String authorName, String comment, String commitId, String date, String message, String revision, long timestamp) {
        this.id = id;
        this.affectedPaths = affectedPaths;
        this.branch = branch;
        this.userDTO = userDTO;
        this.authorName = authorName;
        this.comment = comment;
        this.commitId = commitId;
        this.date = date;
        this.message = message;
        this.revision = revision;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GitChangeSetDTO{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Collection<String> getAffectedPaths() {
        return affectedPaths;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBranch() {
        return branch;
    }

    public String getComment() {
        return comment;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getRevision() {
        return revision;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
