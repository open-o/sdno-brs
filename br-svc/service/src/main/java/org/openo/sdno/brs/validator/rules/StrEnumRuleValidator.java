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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.brs.validator.ErrorMessageKey;
import org.openo.sdno.brs.validator.ValidateTask;

/**
 * Validator class of string enumeration rule.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class StrEnumRuleValidator extends AbstractRuleValidator<StrEnumRule, String> {

    /**
     * Description of string range.
     */
    private String rangeDesc;

    /**
     * Collection of enumeration values.
     */
    private Map<String, String> mapEnum;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param rule String enumeration rule
     */
    public StrEnumRuleValidator(StrEnumRule rule) {
        super(rule);

        if(rule.range() != null && !rule.range().isEmpty()) {
            rangeDesc = rule.range();
            mapEnum = new HashMap<String, String>();
            for(String item : rangeDesc.split(",")) {
                String newValue = item.trim();
                mapEnum.put(newValue, newValue);
            }
        }
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

        if(mapEnum != null && !mapEnum.containsKey(data)) {
            task.addError(attrNameKey, ErrorMessageKey.SIMPLE_ENUM_OUT_OF_RANGE, paramName, rangeDesc);
        }
    }

}
