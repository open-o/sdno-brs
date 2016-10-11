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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 * This is XML model class of Inventory in relation model.<br>
 * <p>
 * This XML model has one attribute: relation model: relation model list in Inventory
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"relationmodel"})
@XmlRootElement(name = "inventory")
public class Inventory implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected List<Relationmodel> relationmodel;

    /**
     * Get relation model attribute.<br>
     * 
     * @return relation model attribute
     * @since SDNO 0.5
     */
    public List<Relationmodel> getRelationmodel() {
        if(relationmodel == null) {
            relationmodel = new ArrayList<Relationmodel>();
        }
        return this.relationmodel;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param thisLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other inventory object
     * @param strategy compare strategy
     * @return true if this object equals to other object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof Inventory)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final Inventory that = (Inventory)object;
        {
            List<Relationmodel> lhsRelationmodel =
                    ((this.relationmodel != null) && (!this.relationmodel.isEmpty())) ? this.getRelationmodel() : null;
            List<Relationmodel> rhsRelationmodel =
                    ((that.relationmodel != null) && (!that.relationmodel.isEmpty())) ? that.getRelationmodel() : null;
            if(!strategy.equals(LocatorUtils.property(thisLocator, "relationmodel", lhsRelationmodel),
                    LocatorUtils.property(thatLocator, "relationmodel", rhsRelationmodel), lhsRelationmodel,
                    rhsRelationmodel)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param otherObject other object
     * @return true if this object equals to other object
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object otherObject) {
        return equals(null, null, otherObject, JAXBEqualsStrategy.INSTANCE);
    }

    /**
     * Get hash code by locator and strategy.<br>
     * 
     * @param locator ObjectLocator object
     * @param strategy HashCodeStrategy object
     * @return hash code of this object
     * @since SDNO 0.5
     */
    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int retHashCode = 1;
        {
            List<Relationmodel> theRelationmodel =
                    ((this.relationmodel != null) && (!this.relationmodel.isEmpty())) ? this.getRelationmodel() : null;
            retHashCode = strategy.hashCode(LocatorUtils.property(locator, "relationmodel", theRelationmodel),
                    retHashCode, theRelationmodel);
        }
        return retHashCode;
    }

    /**
     * Get hash code by INSTANCE HashCodeStrategy.<br>
     * 
     * @return hash code of this object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy currentHashCodeStrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, currentHashCodeStrategy);
    }

    /**
     * Get XML description of current inventory object.<br>
     * 
     * @return XML description of current object
     * @since SDNO 0.5
     */
    public String toString() {
        final StringBuilder retStringBuffer = new StringBuilder();
        append(null, retStringBuffer, JAXBToStringStrategy.INSTANCE);
        return retStringBuffer.toString();
    }

    /**
     * Append current object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param appendBuffer String buffer current object appended to
     * @param strategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder appendBuffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, appendBuffer);
        appendFields(locator, appendBuffer, strategy);
        strategy.appendEnd(locator, this, appendBuffer);
        return appendBuffer;
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
        {
            List<Relationmodel> currentRelationmodel =
                    ((this.relationmodel != null) && (!this.relationmodel.isEmpty())) ? this.getRelationmodel() : null;
            strategy.appendField(locator, this, "relationmodel", buffer, currentRelationmodel);
        }
        return buffer;
    }

}
