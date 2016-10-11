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

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.brs.validator.ErrorMessageKey;
import org.openo.sdno.brs.validator.SingleRange;
import org.openo.sdno.brs.validator.ValidateTask;

/**
 * Validator class of string rule.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class StrRuleValidator extends AbstractRuleValidator<StrRule, String> {

    private SingleRange range;

    private Pattern regex;

    private final boolean matches;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param rule String rule
     */
    public StrRuleValidator(StrRule rule) {
        super(rule);

        // Initialize range.
        if(!StringUtils.isEmpty(rule.range())) {
            range = SingleRange.parse(rule.range());
        }

        // Init regex.
        if(!StringUtils.isEmpty(rule.regex())) {
            regex = Pattern.compile(rule.regex());
        }

        matches = rule.matches();
    }

    @Override
    public void validate(ValidateTask task, String attrNameKey, String data, boolean isEdit) {
        if(StringUtils.isEmpty(data)) {
            if(data == null && isEdit && isEditNull) {
                return;
            }

            if(!nullable) {
                task.addError(attrNameKey, ErrorMessageKey.NOT_NULLABLE, paramName);
            }
            return;
        }

        if(range != null && !range.contains(data.length())) {
            task.addError(attrNameKey, ErrorMessageKey.STR_OUT_OF_RANGE, paramName, range.toString());
        }

        if(regex != null) {
            if(!regex.matcher(data).find()) {
                task.addError(attrNameKey, ErrorMessageKey.REGEX_NOT_MATCH, paramName, regex.toString());
            }

            if(matches && !regex.matcher(data).matches()) {
                task.addError(attrNameKey, ErrorMessageKey.REGEX_NOT_MATCH, paramName, regex.toString());
            }
        }
    }
}
