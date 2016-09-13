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

package org.openo.sdno.brs.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of error info for validating.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class RuleErrorInfo {

    /**
     * Collection of paths of error attributes.
     */
    private final List<String> lstAttrPath = new ArrayList<String>();

    /**
     * The resource file key which is the error message corresponding to.
     */
    private String errorMsgKey;

    private List<String> lstErrParam;

    private String paramName;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public RuleErrorInfo() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("lstAttrPath:");
        for(String str : lstAttrPath) {
            sb.append('/').append(str);
        }
        sb.append(";errorMsgKey:").append(errorMsgKey).append(";lstErrParam:").append(lstErrParam);
        return sb.toString();
    }

    public String getErrorMsgKey() {
        return errorMsgKey;
    }

    public void setErrorMsgKey(String errorMsgKey) {
        this.errorMsgKey = errorMsgKey;
    }

    public List<String> getLstErrParam() {
        return lstErrParam;
    }

    public void setLstErrParam(List<String> lstErrParam) {
        this.lstErrParam = lstErrParam;
    }

    public List<String> getLstAttrPath() {
        return lstAttrPath;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
