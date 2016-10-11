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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * String enumeration rule.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrEnumRule {

    /**
     * Check if the null is allowed.<br>
     * 
     * @return True if allowed, false if not.
     * @since SDNO 0.5
     */
    public boolean nullable() default true;

    /**
     * Get the range of string value.<br>
     * 
     * @return the range of string value
     * @since SDNO 0.5
     */
    public String range();

    /**
     * Get parameter name.<br>
     * 
     * @return Parameter name
     * @since SDNO 0.5
     */
    public String paramName() default "";
}
