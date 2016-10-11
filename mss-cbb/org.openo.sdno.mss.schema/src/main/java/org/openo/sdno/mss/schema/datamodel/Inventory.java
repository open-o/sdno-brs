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
 * This is XML model class of inventory in data model.<br>
 * <p>
 * This XML model has one attribute: datamodel:datamodel of Inventory
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"datamodel"})
@XmlRootElement(name = "inventory")
public class Inventory implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected List<Datamodel> datamodel;

    /**
     * Get datamodel attribute.<br>
     * 
     * @return datamodel attribute
     * @since SDNO 0.5
     */
    public List<Datamodel> getDatamodel() {
        if(datamodel == null) {
            datamodel = new ArrayList<Datamodel>();
        }
        return this.datamodel;
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
            List<Datamodel> thisDataModel;
            thisDataModel = ((this.datamodel != null) && (!this.datamodel.isEmpty())) ? this.getDatamodel() : null;
            List<Datamodel> thatDataModel;
            thatDataModel = ((that.datamodel != null) && (!that.datamodel.isEmpty())) ? that.getDatamodel() : null;
            if(!strategy.equals(LocatorUtils.property(thisLocator, "datamodel", thisDataModel),
                    LocatorUtils.property(thatLocator, "datamodel", thatDataModel), thisDataModel, thatDataModel)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether this object equals to other object<br>
     * 
     * @param object other object
     * @return true if this object equals to other object
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object object) {
        return equals(null, null, object, JAXBEqualsStrategy.INSTANCE);
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
        int curHashCode = 1;
        {
            List<Datamodel> currentDataMOdel;
            currentDataMOdel = ((this.datamodel != null) && (!this.datamodel.isEmpty())) ? this.getDatamodel() : null;
            curHashCode = strategy.hashCode(LocatorUtils.property(locator, "datamodel", currentDataMOdel), curHashCode,
                    currentDataMOdel);
        }
        return curHashCode;
    }

    /**
     * Get hash code by INSTANCE HashCodeStrategy.<br>
     * 
     * @return hash code of this object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy hashCodestrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, hashCodestrategy);
    }

    /**
     * Get XML description of current inventory object.<br>
     * 
     * @return XML description of current object
     * @since SDNO 0.5
     */
    public String toString() {
        final ToStringStrategy toStringStrategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder sb = new StringBuilder();
        append(null, sb, toStringStrategy);
        return sb.toString();
    }

    /**
     * Append current object to buffer.<br>
     * 
     * @param objectLocator ObjectLocator object
     * @param strBuilder String buffer current object appended to
     * @param strategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator objectLocator, StringBuilder strBuilder, ToStringStrategy strategy) {
        strategy.appendStart(objectLocator, this, strBuilder);
        appendFields(objectLocator, strBuilder, strategy);
        strategy.appendEnd(objectLocator, this, strBuilder);
        return strBuilder;
    }

    /**
     * Append all attributes to buffer.<br>
     * 
     * @param objLocator ObjectLocator object
     * @param stringBuilder String buffer all attributes appended to
     * @param strategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder appendFields(ObjectLocator objLocator, StringBuilder stringBuilder,
            ToStringStrategy strategy) {
        {
            List<Datamodel> theDatamodel;
            theDatamodel = ((this.datamodel != null) && (!this.datamodel.isEmpty())) ? this.getDatamodel() : null;
            strategy.appendField(objLocator, this, "datamodel", stringBuilder, theDatamodel);
        }
        return stringBuilder;
    }

}
