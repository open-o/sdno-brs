/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.mss.dao.exception;

/**
 * Method not support Exception class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class MethodNotSupportException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 21394561207854L;

    private String methodName = "";

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param methodName method name
     */
    public MethodNotSupportException(String methodName) {
        super("Method not support: " + methodName);
        this.methodName = methodName;
    }

    /**
     * Get methodName attribute.<br>
     * 
     * @return methodName attribute
     * @since SDNO 0.5
     */
    public String getMethodName() {
        return methodName;
    }

}
