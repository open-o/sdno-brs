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

/**
 * ValidateTask test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ValidateTaskTest {

    @Test
    public void testAddError() {
        ValidateTask validateTask = new ValidateTask(null, "parameter");
        validateTask.addError("attrNameKey", "errorMsgKey", "paramName", "params");
        List<RuleErrorInfo> resultLst = validateTask.getLstErr();
        assertTrue("attrNameKey".equals(resultLst.get(0).getLstAttrPath().get(0)));
        assertTrue("errorMsgKey".equals(resultLst.get(0).getErrorMsgKey()));
        assertTrue("paramName".equals(resultLst.get(0).getParamName()));
        assertTrue(Arrays.asList("params").equals(resultLst.get(0).getLstErrParam()));
    }

    @Test
    public void testAddErrorAttrNameKeyNull() {
        ValidateTask validateTask = new ValidateTask(null, "parameter");
        validateTask.addError(null, "errorMsgKey", "paramName", "params");
        List<RuleErrorInfo> resultLst = validateTask.getLstErr();
        assertTrue(resultLst.get(0).getLstAttrPath().isEmpty());
        assertTrue("errorMsgKey".equals(resultLst.get(0).getErrorMsgKey()));
        assertTrue("paramName".equals(resultLst.get(0).getParamName()));
        assertTrue(Arrays.asList("params").equals(resultLst.get(0).getLstErrParam()));
    }

    @Test
    public void testAddErrorParamsNull() {
        ValidateTask validateTask = new ValidateTask(null, "parameter");
        validateTask.addError("attrNameKey", "errorMsgKey", "paramName", null);
        List<RuleErrorInfo> resultLst = validateTask.getLstErr();
        assertTrue("attrNameKey".equals(resultLst.get(0).getLstAttrPath().get(0)));
        assertTrue("errorMsgKey".equals(resultLst.get(0).getErrorMsgKey()));
        assertTrue("paramName".equals(resultLst.get(0).getParamName()));
        assertTrue(null == resultLst.get(0).getLstErrParam());
    }

    @Test
    public void testAddErrorParamsEmpty() {
        ValidateTask validateTask = new ValidateTask(null, "parameter");
        validateTask.addError("attrNameKey", "errorMsgKey", "paramName", "");
        List<RuleErrorInfo> resultLst = validateTask.getLstErr();
        assertTrue("attrNameKey".equals(resultLst.get(0).getLstAttrPath().get(0)));
        assertTrue("errorMsgKey".equals(resultLst.get(0).getErrorMsgKey()));
        assertTrue("paramName".equals(resultLst.get(0).getParamName()));
        assertTrue(Arrays.asList("").equals(resultLst.get(0).getLstErrParam()));
    }

}
