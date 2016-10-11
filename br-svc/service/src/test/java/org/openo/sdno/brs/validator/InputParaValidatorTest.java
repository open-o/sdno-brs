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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.validator.InputParaValidator.InputParaCheck;

import mockit.Mock;
import mockit.MockUp;

/**
 * InputParaValidator test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class InputParaValidatorTest {

    private InputParaValidator inputParaValidator = new InputParaValidator();

    @Test
    public void testValidate() {
        assertTrue(null == inputParaValidator.validate(null, false));
    }

    @Test
    public void testInputParamsCheckNoErr() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validate(Object validData, boolean isEdit) {
                if(isEdit)
                    return null;
                else
                    return new ArrayList<RuleErrorInfo>();
            }
        };

        InputParaCheck.inputParamsCheck(null, false);
        InputParaCheck.inputParamsCheck(null, true);
    }

    @Test(expected = ServiceException.class)
    public void testInputParamsCheckErrWithoutLastErrParam() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validate(Object validData, boolean isEdit) {
                List<RuleErrorInfo> errInfo = new ArrayList<RuleErrorInfo>();
                errInfo.add(new RuleErrorInfo());
                return errInfo;
            }
        };

        InputParaCheck.inputParamsCheck(null, false);
    }

    @Test(expected = ServiceException.class)
    public void testInputParamsCheckErrWithLastErrParam() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validate(Object validData, boolean isEdit) {
                List<RuleErrorInfo> errInfo = new ArrayList<RuleErrorInfo>();
                RuleErrorInfo ruleErrorInfo = new RuleErrorInfo();
                List<String> lstErrParam = new ArrayList<String>();
                lstErrParam.add("parameter A err");
                lstErrParam.add("parameter B err");
                ruleErrorInfo.setLstErrParam(lstErrParam);
                errInfo.add(ruleErrorInfo);
                return errInfo;
            }
        };

        InputParaCheck.inputParamsCheck(null, false);
    }

    @Test
    public void testInputAttrCheckNoErr() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validateField(String field, Object value, Class<?> type) {
                if(value == null)
                    return null;
                else
                    return new ArrayList<RuleErrorInfo>();
            }
        };

        InputParaCheck.inputAttrCheck("attrName", null, String.class);
        InputParaCheck.inputAttrCheck("attrName", "value", String.class);
    }

    @Test(expected = ServiceException.class)
    public void testInputAttrCheckErrWithoutLastErrParam() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validateField(String field, Object value, Class<?> type) {
                List<RuleErrorInfo> errInfo = new ArrayList<RuleErrorInfo>();
                RuleErrorInfo ruleErrorInfo = new RuleErrorInfo();
                ruleErrorInfo.toString();
                errInfo.add(ruleErrorInfo);
                return errInfo;
            }
        };

        InputParaCheck.inputAttrCheck("attrName", "value", String.class);
    }

    @Test(expected = ServiceException.class)
    public void testInputAttrCheckErrWithLastErrParam() throws ServiceException {
        new MockUp<InputParaValidator>() {

            @Mock
            public List<RuleErrorInfo> validateField(String field, Object value, Class<?> type) {
                List<RuleErrorInfo> errInfo = new ArrayList<RuleErrorInfo>();
                RuleErrorInfo ruleErrorInfo = new RuleErrorInfo();
                List<String> lstErrParam = new ArrayList<String>();
                lstErrParam.add("parameter A err");
                lstErrParam.add("parameter B err");
                ruleErrorInfo.setLstErrParam(lstErrParam);
                ruleErrorInfo.toString();
                errInfo.add(ruleErrorInfo);
                return errInfo;
            }
        };

        InputParaCheck.inputAttrCheck("attrName", "value", String.class);
    }

}
