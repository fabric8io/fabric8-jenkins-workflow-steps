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

import org.jenkinsci.plugins.workflow.actions.ErrorAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

/**
 */
public class ErrorDTO extends DtoSupport {
    private final String message;
    private final String className;

    public static ErrorDTO createErrorDTO(FlowNode node) {
        if (node != null) {
            return createErrorDTO(node.getError());
        }
        return null;
    }

    public static ErrorDTO createErrorDTO(ErrorAction error) {
        if (error != null) {
            Throwable throwable = error.getError();
            if (throwable != null) {
                return new ErrorDTO(throwable.getMessage(), throwable.getClass().getName());
            }
        }
        return null;
    }

    public ErrorDTO(String message, String className) {
        this.message = message;
        this.className = className;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "className='" + className + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }
}
