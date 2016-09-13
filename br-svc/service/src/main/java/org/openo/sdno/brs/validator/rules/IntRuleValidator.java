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

import org.openo.sdno.brs.validator.ErrorMessageKey;
import org.openo.sdno.brs.validator.ValidateTask;

/**
 * Validator class of integer rule.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class IntRuleValidator extends AbstractRuleValidator<IntRule, Integer> {

    private final int max;

    private final int min;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param rule Integer rule
     */
    public IntRuleValidator(IntRule rule) {
        super(rule);

        this.max = rule.max();

        this.min = rule.min();
    }

    @Override
    public void validate(ValidateTask task, String attrNameKey, Integer data, boolean isEdit) {
        if(data > this.max) {
            task.addError(attrNameKey, ErrorMessageKey.INT_EXCEED_MAX_VALUE, paramName);
        }

        if(data < this.min) {
            task.addError(attrNameKey, ErrorMessageKey.INT_LESS_THAN_MIN_VALUE, paramName);
        }
    }
}
