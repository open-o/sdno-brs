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

package org.openo.sdno.mss.schema.datamodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * This is XML model class of filter.<br>
 * <p>
 * this XML model has two attributes: name: the name of the filter value: the content value of the
 * filter
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 23, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class DataModelFilter implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "value", required = true)
    protected String value;

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
     * @param value name value
     * @since SDNO 0.5
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get value attribute.<br>
     * 
     * @return value attribute
     * @since SDNO 0.5
     */
    public String getValue() {
        return value;
    }

    /**
     * Set value attribute.<br>
     * 
     * @param value value content
     * @since SDNO 0.5
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Check whether this filter object equals to other object.<br>
     * 
     * @param thisLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other filter object
     * @param strategy compare strategy
     * @return true if this object equals to other object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof DataModelFilter)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final DataModelFilter that = (DataModelFilter)object;
        {
            String lhsName = this.getName();
            String rhsName = that.getName();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName),
                    LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        {
            String lhsValue = this.getValue();
            String rhsValue = that.getValue();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue),
                    LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether current filter equals to other filter.<br>
     * 
     * @param theOtherObject other filter object
     * @return true if current filter equals to object
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object theOtherObject) {
        return equals(null, null, theOtherObject, JAXBEqualsStrategy.INSTANCE);
    }

    /**
     * Get hash code of filter by locator and strategy.<br>
     * 
     * @param locator ObjectLocator object
     * @param strategy HashCodeStrategy object
     * @return hash code of filter
     * @since SDNO 0.5
     */
    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            String thisObjName = this.getName();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name", thisObjName), currentHashCode,
                    thisObjName);
        }
        {
            String thisObjValue = this.getValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", thisObjValue), currentHashCode,
                    thisObjValue);
        }
        return currentHashCode;
    }

    /**
     * Get hash code of filter by INSTANCE strategy.<br>
     * 
     * @return hash code of filter
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy strategyInstance = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategyInstance);
    }

    /**
     * Get XML description of current filter object.<br>
     * 
     * @return XML description of current filter object
     * @since SDNO 0.5
     */
    public String toString() {
        final ToStringStrategy jaxbStrategyInstance = JAXBToStringStrategy.INSTANCE;
        final StringBuilder retStringBuilder = new StringBuilder();
        append(null, retStringBuilder, jaxbStrategyInstance);
        return retStringBuilder.toString();
    }

    /**
     * Append this object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param appendedBuffer String buffer appended to
     * @param strategy toString strategy
     * @return string buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder appendedBuffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, appendedBuffer);
        appendFields(locator, appendedBuffer, strategy);
        strategy.appendEnd(locator, this, appendedBuffer);
        return appendedBuffer;
    }

    /**
     * Append all attributes to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer String buffer appended to
     * @param strategy toString strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            String theName;
            theName = this.getName();
            strategy.appendField(locator, this, "name", buffer, theName);
        }
        {
            String theValue;
            theValue = this.getValue();
            strategy.appendField(locator, this, "value", buffer, theValue);
        }
        return buffer;
    }
}
