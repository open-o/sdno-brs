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
import javax.xml.bind.annotation.XmlAttribute;
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
 * This is XML model class of the data model.<br>
 * <p>
 * The data model has three attributes: name: the data model name index: the index of the data model
 * filter: filter rule of the data model
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"index", "filter"})
@XmlRootElement(name = "datamodel")
public class Datamodel implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected List<DataModelIndex> index;

    @XmlElement(required = true)
    protected List<DataModelFilter> filter;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the index property.<br>
     * 
     * @return index of data model
     * @since SDNO 0.5
     */
    public List<DataModelIndex> getIndex() {
        if(index == null) {
            index = new ArrayList<DataModelIndex>();
        }
        return this.index;
    }

    /**
     * Gets the value of the filter property.<br>
     * 
     * @return filter of data model
     * @since SDNO 0.5
     */
    public List<DataModelFilter> getFilter() {
        if(filter == null) {
            filter = new ArrayList<DataModelFilter>();
        }
        return this.filter;
    }

    /**
     * Get the name value.<br>
     * 
     * @return name value
     * @since SDNO 0.5
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name value.<br>
     * 
     * @param value name value
     * @since SDNO 0.5
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param thisLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other object
     * @param strategy compare strategy
     * @return true if this object equals to other object, false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof Datamodel)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final Datamodel that = (Datamodel)object;
        {
            List<DataModelIndex> lhsIndex = this.obtainModelIndex();
            List<DataModelIndex> rhsIndex = that.obtainModelIndex();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "index", lhsIndex),
                    LocatorUtils.property(thatLocator, "index", rhsIndex), lhsIndex, rhsIndex)) {
                return false;
            }
        }
        {
            List<DataModelFilter> lhsFilter = this.obtainModellFilter();
            List<DataModelFilter> rhsFilter = that.obtainModellFilter();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "filter", lhsFilter),
                    LocatorUtils.property(thatLocator, "filter", rhsFilter), lhsFilter, rhsFilter)) {
                return false;
            }
        }
        {
            String lhsName = this.getName();
            String rhsName = that.getName();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName),
                    LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
                return false;
            }
        }
        return true;
    }

    private List<DataModelIndex> obtainModelIndex() {
        if((this.index != null) && (!this.index.isEmpty())) {
            return this.getIndex();
        }
        return null;
    }

    private List<DataModelFilter> obtainModellFilter() {
        if((this.filter != null) && (!this.filter.isEmpty())) {
            return this.getFilter();
        }
        return null;
    }

    /**
     * Check whether data model equals to other data model.<br>
     * 
     * @param object other data model object
     * @return true if this data model equals to other data model, false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    /**
     * Get hash code by locator and strategy<br>
     * 
     * @param locator ObjectLocator Object
     * @param strategy HashCodeStrategy Object
     * @return hashCode of the Object
     * @since SDNO 0.5
     */
    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<DataModelIndex> theIndex;
            theIndex = ((this.index != null) && (!this.index.isEmpty())) ? this.getIndex() : null;
            currentHashCode =
                    strategy.hashCode(LocatorUtils.property(locator, "index", theIndex), currentHashCode, theIndex);
        }
        {
            List<DataModelFilter> theFilter;
            theFilter = ((this.filter != null) && (!this.filter.isEmpty())) ? this.getFilter() : null;
            currentHashCode =
                    strategy.hashCode(LocatorUtils.property(locator, "filter", theFilter), currentHashCode, theFilter);
        }
        {
            String theName;
            theName = this.getName();
            currentHashCode =
                    strategy.hashCode(LocatorUtils.property(locator, "name", theName), currentHashCode, theName);
        }
        return currentHashCode;
    }

    /**
     * Get hash code,use INSTANCE HashCodeStrategy.<br>
     * 
     * @return hashCode of the Object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy jaxbStrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, jaxbStrategy);
    }

    /**
     * Get XML description string of the Object.<br>
     * 
     * @return XML description of the Object
     * @since SDNO 0.5
     */
    public String toString() {
        final ToStringStrategy jaxbToStringstrategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, jaxbToStringstrategy);
        return buffer.toString();
    }

    /**
     * Append this object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer string buffer appended to
     * @param strStrategy to string strategy
     * @return string buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strStrategy) {
        strStrategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strStrategy);
        strStrategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    /**
     * Append all attributes to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer string buffer appended to
     * @param strategy toString strategy
     * @return string buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<DataModelIndex> theIndex = this.obtainModelIndex();
            strategy.appendField(locator, this, "index", buffer, theIndex);
        }
        {
            List<DataModelFilter> theFilter = this.obtainModellFilter();
            strategy.appendField(locator, this, "filter", buffer, theFilter);
        }
        {
            strategy.appendField(locator, this, "name", buffer, this.getName());
        }
        return buffer;
    }
}
