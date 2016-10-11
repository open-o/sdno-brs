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
 * IpRuleValidator test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class IpRuleValidatorTest {

    @Test
    public void testValidateNull() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IpRule ipRule = new IpRuleImpl();
        IpRuleValidator ipRuleValidator = new IpRuleValidator(ipRule);
        ipRuleValidator.validate(taskResult, "ipData", null, true);
        task.addError("ipData", ErrorMessageKey.NOT_NULLABLE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateEmpty() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IpRule ipRule = new IpRuleImpl();
        IpRuleValidator ipRuleValidator = new IpRuleValidator(ipRule);
        ipRuleValidator.validate(taskResult, "ipData", "", true);
        task.addError("ipData", ErrorMessageKey.NOT_NULLABLE, "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidateNotMatch() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IpRule ipRule = new IpRuleImpl();
        IpRuleValidator ipRuleValidator = new IpRuleValidator(ipRule);
        ipRuleValidator.validate(taskResult, "ipData", "1.2.3.4/35", true);
        task.addError("ipData", ErrorMessageKey.NOT_MATCH_IP_ADDR, "1.2.3.4/35", "validate data");
        assertTrue(task.equals(taskResult));
    }

    @Test
    public void testValidate() {
        ValidateTask taskResult = new ValidateTask(null, null);
        ValidateTask task = taskResult;
        IpRule ipRule = new IpRuleImpl();
        IpRuleValidator ipRuleValidator = new IpRuleValidator(ipRule);
        ipRuleValidator.validate(taskResult, "ipData", "1.2.3.4/5,6.7.8.9/10", true);
        assertTrue(task.equals(taskResult));
    }
}

class IpRuleImpl implements IpRule {

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public String type() {
        return "ipv4";
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public String paramName() {
        return "validate data";
    }

}
