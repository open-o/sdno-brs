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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is XML model class of extension data in info model.<br>
 * <p>
 * This XML model has one attribute: property: extension properties of info model
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"property"})
@XmlRootElement(name = "extension")
public class Extension implements Serializable {

    private static final long serialVersionUID = 123456L;

    protected List<Extension.Property> property;

    /**
     * Get property attribute.<br>
     * 
     * @return property attribute
     * @since SDNO 0.5
     */
    public List<Extension.Property> getProperty() {
        if(property == null) {
            property = new ArrayList<Extension.Property>();
        }
        return this.property;
    }

    /**
     * This is XML model class of Extension Property in info model.<br>
     * <p>
     * This XML model has six attributes: name: the name of property type: the data type of property
     * length: the length of property scale: the scale of property encrypt: if the property is
     * encrypted empty: can be set to null or not
     * </p>
     * 
     * @author
     * @version SDNO 0.5 May 19, 2016
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Property implements Serializable {

        private static final long serialVersionUID = 123456L;

        @XmlAttribute(name = "name", required = true)
        protected String name;

        @XmlAttribute(name = "type", required = true)
        protected Datatype type;

        @XmlAttribute(name = "length")
        protected Integer length;

        @XmlAttribute(name = "scale")
        protected Integer scale;

        @XmlAttribute(name = "encrypt")
        protected BigInteger encrypt;

        @XmlAttribute(name = "empty")
        protected String empty;

        /**
         * Get name attribute.<br>
         * 
         * @return name attribute
         * @since SDNO 0.5
         */
        public String getName() {
            return name;
        }

        /**
         * Set name attribute.<br>
         * 
         * @param value String Object
         * @since SDNO 0.5
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Get type attribute.<br>
         * 
         * @return type attribute
         * @since SDNO 0.5
         */
        public Datatype getType() {
            return type;
        }

        /**
         * Set type attribute.<br>
         * 
         * @param value Data type object
         * @since SDNO 0.5
         */
        public void setType(Datatype value) {
            this.type = value;
        }

        /**
         * Get length attribute.<br>
         * 
         * @return length attribute
         * @since SDNO 0.5
         */
        public Integer getLength() {
            return length;
        }

        /**
         * Set length attribute.<br>
         * 
         * @param value Integer object
         * @since SDNO 0.5
         */
        public void setLength(Integer value) {
            this.length = value;
        }

        /**
         * Get scale attribute.<br>
         * 
         * @return scale attribute
         * @since SDNO 0.5
         */
        public Integer getScale() {
            return scale;
        }

        /**
         * Set scale attribute.<br>
         * 
         * @param value Integer object
         * @since SDNO 0.5
         */
        public void setScale(Integer value) {
            this.scale = value;
        }

        /**
         * Get encrypt attribute.<br>
         * 
         * @return encrypt attribute
         * @since SDNO 0.5
         */
        public BigInteger getEncrypt() {
            if(encrypt == null) {
                return BigInteger.ZERO;
            } else {
                return encrypt;
            }
        }

        /**
         * Set encrypt attribute.<br>
         * 
         * @param value BigInteger object
         * @since SDNO 0.5
         */
        public void setEncrypt(BigInteger value) {
            this.encrypt = value;
        }

        /**
         * Get empty attribute.<br>
         * 
         * @return empty attribute
         * @since SDNO 0.5
         */
        public String getEmpty() {
            return empty;
        }

        /**
         * Set empty attribute.<br>
         * 
         * @param value String object
         * @since SDNO 0.5
         */
        public void setEmpty(String value) {
            this.empty = value;
        }

    }

}
