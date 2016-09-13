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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.brs.validator.rules.AbstractRuleValidator;

/**
 * Class for attribute validating.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class AttrValidateInfo {

    /**
     * Attribute name.
     */
    private String attributeName;

    /**
     * Rule list for attribute validating.
     */
    private List<AbstractRuleValidator> lstRuleValidator = new ArrayList<AbstractRuleValidator>();

    /**
     * Method for getting value from object.
     */
    private Method getMethod;

    /**
     * Parameter name.
     */
    private String paramName;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param attributeName Attribute name
     * @param getMethod Method for getting value from object
     * @param lstRuleValidator Rule list for attribute validating
     * @param paramName Parameter name
     */
    public AttrValidateInfo(String attributeName, Method getMethod, List<AbstractRuleValidator> lstRuleValidator,
            String paramName) {
        super();
        this.attributeName = attributeName;
        this.getMethod = getMethod;
        this.lstRuleValidator = lstRuleValidator;
        this.paramName = paramName;
    }

    /**
     * Get attribute value.<br>
     * 
     * @param validData The object which contains the attribute.
     * @return Attribute value
     * @throws IllegalAccessException if get attribute value failed
     * @throws InvocationTargetException if get attribute value failed
     * @since SDNO 0.5
     */
    public Object getAttrValue(Object validData) throws IllegalAccessException, InvocationTargetException {
        return getMethod.invoke(validData);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List<AbstractRuleValidator> getLstRuleValidator() {
        return lstRuleValidator;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public String getParamName() {
        return paramName;
    }
}
