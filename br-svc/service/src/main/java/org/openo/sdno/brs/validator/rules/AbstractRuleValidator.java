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

import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.sdno.brs.validator.ValidateTask;

/**
 * Class of rule validator.<br>
 * 
 * @param <T> Annotation type
 * @param <D> Data type
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public abstract class AbstractRuleValidator<T, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRuleValidator.class);

    /**
     * Whether can be set null.
     */
    protected boolean nullable;

    /**
     * Whether can be null when editing.
     */
    protected boolean isEditNull;

    protected String paramName;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param rule Annotation rule info
     */
    public AbstractRuleValidator(T rule) {
        Method[] methods = rule.getClass().getMethods();
        Boolean tmpBool = (Boolean)getMethodValue(rule, "nullable", methods);
        Boolean tmpIsEditNull = (Boolean)getMethodValue(rule, "isEditNull", methods);
        nullable = tmpBool == null ? true : tmpBool;
        isEditNull = tmpIsEditNull == null ? true : tmpIsEditNull;
        paramName = (String)getMethodValue(rule, "paramName", methods);

    }

    private Object getMethodValue(Object rule, String methodName, Method[] methods) {
        Object value = null;
        for(Method m : methods) {
            if(methodName.equals(m.getName())) {
                try {
                    value = m.invoke(rule);
                } catch(ReflectiveOperationException | IllegalArgumentException e) {
                    LOGGER.error(MessageFormat.format("Failed on get value of {0}.{1}", rule, methodName), e);
                }
                break;
            }
        }
        return value;
    }

    /**
     * Validate the input data.<br>
     * 
     * @param task Validating task
     * @param attrNameKey Key value of attribute
     * @param data The data to be validated
     * @param isEdit Edit flag, some rules need special processing when editing, such as
     *            authentication parameters can be empty when editing.
     * @since SDNO 0.5
     */
    public abstract void validate(ValidateTask task, String attrNameKey, D data, boolean isEdit);

    public String getParamName() {
        return paramName;
    }
}
