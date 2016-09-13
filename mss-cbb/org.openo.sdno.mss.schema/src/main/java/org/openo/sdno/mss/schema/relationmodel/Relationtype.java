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

package org.openo.sdno.mss.schema.relationmodel;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Relation Type for relation model<br>
 * <p>
 * Three relation types are defined : composition/aggregation/association composition: a special
 * relationship type of aggregation aggregation: a relationship between two classes. When object of
 * one class has an (*has*) object of another, if second is a part of first (containment
 * relationship) then we called that there is an aggregation between two classes association: a
 * relationship between two classes, where one class use another
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlType(name = "relationtype")
@XmlEnum
public enum Relationtype {

    @XmlEnumValue("composition")
    COMPOSITION("composition"), @XmlEnumValue("aggregation")
    AGGREGATION("aggregation"), @XmlEnumValue("association")
    ASSOCIATION("association");

    private final String value;

    /**
     * Constructor<br>
     * <p>
     * Relation type instance can be constructed by v
     * </p>
     * 
     * @since SDNO 0.5
     * @param v relation type string
     */
    Relationtype(String v) {
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
     * Get Relation type by value<br>
     * 
     * @param v Relation type
     * @return Relation type instance returned
     * @since SDNO 0.5
     */
    public static Relationtype fromValue(String v) {
        for(Relationtype c : Relationtype.values()) {
            if(c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
