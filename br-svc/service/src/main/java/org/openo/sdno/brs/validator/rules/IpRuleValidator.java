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

import org.openo.sdno.brs.validator.ErrorMessageKey;
import org.openo.sdno.brs.validator.ValidateTask;

/**
 * Validator class of IP rule.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class IpRuleValidator extends AbstractRuleValidator<IpRule, String> {

    private static final String IPV4_REG =
            "^(([1-9]\\d?)|(1\\d\\d|2[0-4]\\d)|(25[0-5]))((\\.(([0-9]\\d?)|(1\\d\\d|2[0-4]\\d)|(25[0-5]))){3})((/(\\d|[1-2]\\d|3[0-2]))?)$";

    private static final String IPV6_REG = "^\\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))"
            + "|(([0-9A-Fa-f]{1,4}:){6}(:|((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?"
            + "\\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25[0-5]|2[0-4]\\d|[01]?"
            + "\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|"
            + "(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})"
            + "(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|"
            + "(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})"
            + "(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|"
            + "(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})"
            + "(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)"
            + "(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?"
            + "\\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}"
            + "((:((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|[01]?\\d{1,2})){3})?)|"
            + "((:[0-9A-Fa-f]{1,4}){1,2})))|(((25[0-5]|2[0-4]\\d|[01]?\\d{1,2})(\\.(25[0-5]|2[0-4]"
            + "\\d|[01]?\\d{1,2})){3})))(%.+)?\\s*$";

    private static Map<String, String> regMap = new HashMap<String, String>();

    private final String type;

    static {
        regMap.put("ipv4", IPV4_REG);
        regMap.put("ipv6", IPV6_REG);
    }

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param rule IP rule
     */
    public IpRuleValidator(IpRule rule) {
        super(rule);
        this.type = rule.type();
    }

    @Override
    public void validate(ValidateTask task, String attrNameKey, String data, boolean isEdit) {
        if(data == null || data.isEmpty()) {
            if(!nullable) {
                task.addError(attrNameKey, ErrorMessageKey.NOT_NULLABLE, paramName);
            }
            return;
        }

        boolean isMatch = false;
        String[] ipTypes = type.split(",");
        for(String ipType : ipTypes) {
            String reg = regMap.get(ipType);
            if(reg == null) {
                continue;
            }

            isMatch |= data.matches(reg);
        }

        if(!isMatch) {
            task.addError(attrNameKey, ErrorMessageKey.NOT_MATCH_IP_ADDR, data, paramName);
        }
    }
}
