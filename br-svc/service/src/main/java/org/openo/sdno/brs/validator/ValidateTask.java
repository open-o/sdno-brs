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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Class of validating task.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class ValidateTask {

    /**
     * Stack of validating trace.
     */
    private final Stack<ValidateTraceItem> stackValidTrace = new Stack<ValidateTraceItem>();

    /**
     * Collection of error info of validating.
     */
    private final List<RuleErrorInfo> lstErr = new ArrayList<RuleErrorInfo>();

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param validator Validator of input parameter
     * @param validData The object to be validated
     */
    public ValidateTask(InputParaValidator validator, Object validData) {
        super();
        stackValidTrace.push(new ValidateTraceItem(null, validData));
    }

    /**
     * Add error message.<br>
     * 
     * @param attrNameKey Key value of attribute
     * @param errorMsgKey Key value of error message
     * @param paramName Parameter name
     * @param params Collection of error parameters
     * @since SDNO 0.5
     */
    public void addError(String attrNameKey, String errorMsgKey, String paramName, String... params) {
        RuleErrorInfo err = new RuleErrorInfo();
        for(ValidateTraceItem traceItem : stackValidTrace) {
            if(traceItem.getAttrNameKey() != null) {
                err.getLstAttrPath().add(traceItem.getAttrNameKey());
            }
        }
        if(attrNameKey != null) {
            err.getLstAttrPath().add(attrNameKey);
        }

        err.setErrorMsgKey(errorMsgKey);
        err.setParamName(paramName);
        if(params != null && params.length > 0) {
            err.setLstErrParam(Arrays.asList(params));
        }

        lstErr.add(err);
    }

    public Stack<ValidateTraceItem> getStackValidTrace() {
        return stackValidTrace;
    }

    public List<RuleErrorInfo> getLstErr() {
        return lstErr;
    }
}
