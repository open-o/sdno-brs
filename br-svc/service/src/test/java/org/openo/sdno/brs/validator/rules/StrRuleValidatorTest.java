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

package org.openo.sdno.brs.validator.rules;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.openo.sdno.brs.validator.ErrorMessageKey;
import org.openo.sdno.brs.validator.ValidateTask;

/**
 * StrRuleValidator test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class StrRuleValidatorTest {

    @Test
    public void testValidateEmptyEditFalse() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", null, false);
        task.addError("strData", ErrorMessageKey.NOT_NULLABLE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateEmptyEditTrue() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", null, true);
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateOutOfRange() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", "Abcd1", false);
        task.addError("strData", ErrorMessageKey.STR_OUT_OF_RANGE, "validate data", "1-2");
        task.addError("strData", ErrorMessageKey.REGEX_NOT_MATCH, "validate data", "\\d");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateNotMatch1() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", "a1", false);
        task.addError("strData", ErrorMessageKey.REGEX_NOT_MATCH, "validate data", "\\d");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateNotMatch2() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", "aa", false);
        task.addError("strData", ErrorMessageKey.REGEX_NOT_MATCH, "validate data", "\\d");
        task.addError("strData", ErrorMessageKey.REGEX_NOT_MATCH, "validate data", "\\d");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidate() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrRule strRule = new StrRuleImpl();
        StrRuleValidator ruleValidator = new StrRuleValidator(strRule);
        ruleValidator.validate(taskResult, "strData", "1", false);
        assertTrue(task.equals(taskResult));
    }

}

class StrRuleImpl implements StrRule {

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public String range() {
        return "1-2";
    }

    @Override
    public String regex() {
        return "\\d";
    }

    @Override
    public String defaultValue() {
        return null;
    }

    @Override
    public boolean matches() {
        return true;
    }

    @Override
    public String paramName() {
        return "validate data";
    }

}
