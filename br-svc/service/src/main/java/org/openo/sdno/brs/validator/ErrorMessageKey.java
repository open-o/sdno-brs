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

/**
 * Class that defines the key value of the error messages.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public final class ErrorMessageKey {

    /**
     * Failed on getting value of attribute.
     */
    public static final String GET_VALUE_FAILED = "brs.attrCheckMsg.GetValueFailed";

    /**
     * Failed to create attribute validator. Type:{0}; attribute{1}; comment:{2}.
     */
    public static final String CREATE_VALIDATOR_FAILED = "brs.attrCheckMsg.CreateValidatorFailed";

    /**
     * Initialize attribute {0} failed for not find method {1}.
     */
    public static final String INIT_ATTR_FAILED = "brs.attrCheckMsg.InitAttrFailed";

    /**
     * Can not be null.
     */
    public static final String NOT_NULLABLE = "brs.attrCheckMsg.NotNullable";

    /**
     * Enumeration value out of range.
     */
    public static final String SIMPLE_ENUM_OUT_OF_RANGE = "brs.attrCheckMsg.SimpleEnumOutOfRange";

    /**
     * String length out of range, the correct range is {0}.
     */
    public static final String STR_OUT_OF_RANGE = "brs.attrCheckMsg.StrOutOfRange";

    /**
     * The string format is not correct, the correct format is {0}
     */
    public static final String REGEX_NOT_MATCH = "brs.attrCheckMsg.RegexNotMatch";

    /**
     * Value exceeds the maximum value.
     */
    public static final String INT_EXCEED_MAX_VALUE = "brs.attrCheckMsg.ExceedMaxValue";

    /**
     * Value is less than the minimum value.
     */
    public static final String INT_LESS_THAN_MIN_VALUE = "brs.attrCheckMsg.LessThanMinValue";

    /**
     * IP address do not match
     */
    public static final String NOT_MATCH_IP_ADDR = "brs.attrCheckMsg.IpAddrNotMatch";

    private ErrorMessageKey() {

    }
}
