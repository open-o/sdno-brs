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

package org.openo.sdno.mss.schema.infomodel;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Data Type class for property in info model<br>
 * <p>
 * Six Data types are defined in this class.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlType(name = "datatype")
@XmlEnum
public enum Datatype {

    @XmlEnumValue("string")
    STRING("string"), @XmlEnumValue("integer")
    INTEGER("integer"), @XmlEnumValue("decimal")
    DECIMAL("decimal"), @XmlEnumValue("datetime")
    DATETIME("datetime"), @XmlEnumValue("float")
    FLOAT("float"), @XmlEnumValue("double")
    DOUBLE("double");

    private final String value;

    /**
     * Constructor<br>
     * <p>
     * Data type can be created by value.
     * </p>
     * 
     * @since SDNO 0.5
     * @param v String value to create data type
     */
    Datatype(String v) {
        value = v;
    }

    /**
     * Get value attribute.<br>
     * 
     * @return value attribute
     * @since SDNO 0.5
     */
    public String value() {
        return value;
    }

    /**
     * Get Data Type by String value.<br>
     * 
     * @param v String value to define which Data Type is returned
     * @return Data type instance
     * @since SDNO 0.5
     */
    public static Datatype fromValue(String v) {
        for(Datatype c : Datatype.values()) {
            if(c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
