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
 * IntRuleValidator test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class IntRuleValidatorTest {

    @Test
    public void testValidateMax() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IntRule intRule = new IntRuleImpl();
        IntRuleValidator ruleValidator = new IntRuleValidator(intRule);
        ruleValidator.validate(taskResult, "intData", 11, true);
        task.addError("intData", ErrorMessageKey.INT_EXCEED_MAX_VALUE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateMin() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IntRule intRule = new IntRuleImpl();
        IntRuleValidator ruleValidator = new IntRuleValidator(intRule);
        ruleValidator.validate(taskResult, "intData", 0, true);
        task.addError("intData", ErrorMessageKey.INT_LESS_THAN_MIN_VALUE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateBetween() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IntRule intRule = new IntRuleImpl();
        IntRuleValidator ruleValidator = new IntRuleValidator(intRule);
        ruleValidator.validate(taskResult, "intData", 5, true);
        assertTrue(task.equals(taskResult));
    }
}

class IntRuleImpl implements IntRule {

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public int max() {
        return 10;
    }

    @Override
    public int min() {
        return 1;
    }

    @Override
    public String paramName() {
        return "validate data";
    }
}
