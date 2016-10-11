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
 * StrEnumRuleValidator test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class StrEnumRuleValidatorTest {

    @Test
    public void testValidateEmptyEditTrue() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrEnumRule strEnumRule = new StrEnumRuleImpl();
        StrEnumRuleValidator ruleValidator = new StrEnumRuleValidator(strEnumRule);
        ruleValidator.validate(taskResult, "strEnumData", null, true);
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateEmptyEditFalse() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrEnumRule strEnumRule = new StrEnumRuleImpl();
        StrEnumRuleValidator ruleValidator = new StrEnumRuleValidator(strEnumRule);
        ruleValidator.validate(taskResult, "strEnumData", null, false);
        task.addError("strEnumData", ErrorMessageKey.NOT_NULLABLE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateSimpleEnumOutOfRange() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrEnumRule strEnumRule = new StrEnumRuleImpl();
        StrEnumRuleValidator ruleValidator = new StrEnumRuleValidator(strEnumRule);
        ruleValidator.validate(taskResult, "strEnumData", "d", true);
        task.addError("strEnumData", ErrorMessageKey.SIMPLE_ENUM_OUT_OF_RANGE, "validate data", "a,b,c");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidate() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        StrEnumRule strEnumRule = new StrEnumRuleImpl();
        StrEnumRuleValidator ruleValidator = new StrEnumRuleValidator(strEnumRule);
        ruleValidator.validate(taskResult, "strEnumData", "a", true);
        assertTrue(task.equals(taskResult));
    }

}

class StrEnumRuleImpl implements StrEnumRule {

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
        return "a,b,c";
    }

    @Override
    public String paramName() {
        return "validate data";
    }
}
