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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is XML model class of basic data in info model.<br>
 * <p>
 * This XML model has one attribute: property: basic property of info model
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"property"})
@XmlRootElement(name = "basic")
public class Basic implements Serializable {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected List<Property> property;

    /**
     * Get property attribute.<br>
     * 
     * @return property attribute
     * @since SDNO 0.5
     */
    public List<Property> getProperty() {
        if(property == null) {
            property = new ArrayList<Property>();
        }
        return this.property;
    }

}
