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
 * This is XML model class of Index.<br>
 * <p>
 * This XML model has two attributes: name: the index name value: the index field name isunique:
 * whether this filed is unique
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class DataModelIndex implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "value", required = true)
    protected String value;

    @XmlAttribute(name = "isunique")
    protected Boolean isunique;

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
     * @param value name content
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
     * @param value value
     * @since SDNO 0.5
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get is unique attribute.<br>
     * 
     * @return is unique attribute
     * @since SDNO 0.5
     */
    public Boolean isIsunique() {
        return isunique;
    }

    /**
     * Set is unique attribute.<br>
     * 
     * @param value is unique
     * @since SDNO 0.5
     */
    public void setIsunique(Boolean value) {
        this.isunique = value;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param thisLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other index object
     * @param strategy compare strategy
     * @return true if this object equals to other object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof DataModelIndex)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final DataModelIndex that = (DataModelIndex)object;
        {
            String thisName = this.getName();
            String thatName = that.getName();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "name", thisName),
                    LocatorUtils.property(thatLocator, "name", thatName), thisName, thatName)) {
                return false;
            }
        }
        {
            String thisValue = this.getValue();
            String thatValue = that.getValue();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "value", thisValue),
                    LocatorUtils.property(thatLocator, "value", thatValue), thisValue, thatValue)) {
                return false;
            }
        }
        {
            Boolean thisIsunique = this.isIsunique();
            Boolean thatIsunique = that.isIsunique();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "isunique", thisIsunique),
                    LocatorUtils.property(thatLocator, "isunique", thatIsunique), thisIsunique, thatIsunique)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether current index object equals to other object.<br>
     * 
     * @param object other Object
     * @return true if current index object equals to other index object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object object) {
        final EqualsStrategy currentStrategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, currentStrategy);
    }

    /**
     * Get hash code by locator and strategy.<br>
     * 
     * @param locator ObjectLocator object
     * @param strategy HashCodeStrategy object
     * @return hash code of current object
     * @since SDNO 0.5
     */
    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            String thisName = this.getName();
            currentHashCode =
                    strategy.hashCode(LocatorUtils.property(locator, "name", thisName), currentHashCode, thisName);
        }
        {
            String thisValue = this.getValue();
            currentHashCode =
                    strategy.hashCode(LocatorUtils.property(locator, "value", thisValue), currentHashCode, thisValue);
        }
        {
            Boolean thisIsunique = this.isIsunique();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isunique", thisIsunique),
                    currentHashCode, thisIsunique);
        }
        return currentHashCode;
    }

    /**
     * Get hash code by INSTANCE HashCodeStrategy.<br>
     * 
     * @return hash code of current object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy currentStrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, currentStrategy);
    }

    /**
     * Get XML description of current index object.<br>
     * 
     * @return XML description of current object
     * @since SDNO 0.5
     */
    public String toString() {
        final ToStringStrategy jaxbToStringStrategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder stringBuffer = new StringBuilder();
        append(null, stringBuffer, jaxbToStringStrategy);
        return stringBuffer.toString();
    }

    /**
     * Append current object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param stringBuilder String buffer current object appended to
     * @param toStringStrategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder stringBuilder, ToStringStrategy toStringStrategy) {
        toStringStrategy.appendStart(locator, this, stringBuilder);
        appendFields(locator, stringBuilder, toStringStrategy);
        toStringStrategy.appendEnd(locator, this, stringBuilder);
        return stringBuilder;
    }

    /**
     * Append all attributes to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer String buffer all attributes appended to
     * @param strategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {

        strategy.appendField(locator, this, "name", buffer, this.getName());
        strategy.appendField(locator, this, "value", buffer, this.getValue());
        strategy.appendField(locator, this, "isunique", buffer, this.isIsunique());

        return buffer;
    }

}
