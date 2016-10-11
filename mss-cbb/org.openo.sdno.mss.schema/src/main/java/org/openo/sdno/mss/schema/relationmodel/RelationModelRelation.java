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
 * This is XML model class of Relation.<br>
 * <p>
 * This XML model has four attributes: source: the source data model name destination: the
 * destination data
 * model name type: the relation type owner: the owner of the relation
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class RelationModelRelation implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlAttribute(name = "src")
    protected String src;

    @XmlAttribute(name = "dst")
    protected String dst;

    @XmlAttribute(name = "type", required = true)
    protected Relationtype type;

    @XmlAttribute(name = "owner")
    protected String owner;

    /**
     * Get source attribute.<br>
     * 
     * @return source attribute
     * @since SDNO 0.5
     */
    public String getSrc() {
        return src;
    }

    /**
     * Set source attribute.<br>
     * 
     * @param value String Object
     * @since SDNO 0.5
     */
    public void setSrc(String value) {
        this.src = value;
    }

    /**
     * Get destination attribute.<br>
     * 
     * @return destination attribute
     * @since SDNO 0.5
     */
    public String getDst() {
        return dst;
    }

    /**
     * Set destination attribute.<br>
     * 
     * @param value String Object
     * @since SDNO 0.5
     */
    public void setDst(String value) {
        this.dst = value;
    }

    /**
     * Get type attribute.<br>
     * 
     * @return type attribute
     * @since SDNO 0.5
     */
    public Relationtype getType() {
        return type;
    }

    /**
     * Set type attribute.<br>
     * 
     * @param value String Object
     * @since SDNO 0.5
     */
    public void setType(Relationtype value) {
        this.type = value;
    }

    /**
     * Get owner attribute.<br>
     * 
     * @return owner attribute
     * @since SDNO 0.5
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set owner attribute.<br>
     * 
     * @param value String Object
     * @since SDNO 0.5
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param thisLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other Relation object
     * @param strategy compare strategy
     * @return true if this object equals to other object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof RelationModelRelation)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final RelationModelRelation that = (RelationModelRelation)object;
        {
            String lhsSrc;
            lhsSrc = this.getSrc();
            String rhsSrc;
            rhsSrc = that.getSrc();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "src", lhsSrc),
                    LocatorUtils.property(thatLocator, "src", rhsSrc), lhsSrc, rhsSrc)) {
                return false;
            }
        }
        {
            String lhsDst;
            lhsDst = this.getDst();
            String rhsDst;
            rhsDst = that.getDst();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "dst", lhsDst),
                    LocatorUtils.property(thatLocator, "dst", rhsDst), lhsDst, rhsDst)) {
                return false;
            }
        }
        {
            Relationtype lhsType;
            lhsType = this.getType();
            Relationtype rhsType;
            rhsType = that.getType();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType),
                    LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            String lhsOwner;
            lhsOwner = this.getOwner();
            String rhsOwner;
            rhsOwner = that.getOwner();
            if(!strategy.equals(LocatorUtils.property(thisLocator, "owner", lhsOwner),
                    LocatorUtils.property(thatLocator, "owner", rhsOwner), lhsOwner, rhsOwner)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param object other object
     * @return true if this object equals to other object
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
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
        int thisHashCode = 1;
        {
            String theSrc;
            theSrc = this.getSrc();
            thisHashCode = strategy.hashCode(LocatorUtils.property(locator, "src", theSrc), thisHashCode, theSrc);
        }
        {
            String theDst;
            theDst = this.getDst();
            thisHashCode = strategy.hashCode(LocatorUtils.property(locator, "dst", theDst), thisHashCode, theDst);
        }
        {
            Relationtype theType;
            theType = this.getType();
            thisHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), thisHashCode, theType);
        }
        {
            String theOwner;
            theOwner = this.getOwner();
            thisHashCode = strategy.hashCode(LocatorUtils.property(locator, "owner", theOwner), thisHashCode, theOwner);
        }
        return thisHashCode;
    }

    /**
     * Get hash code by INSTANCE HashCodeStrategy.<br>
     * 
     * @return hash code of this object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy jaxbHashCodestrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, jaxbHashCodestrategy);
    }

    /**
     * Get XML description of current Relation object.<br>
     * 
     * @return XML description of current object
     * @since SDNO 0.5
     */
    public String toString() {
        final ToStringStrategy toStrStrategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder stringBuilder = new StringBuilder();
        append(null, stringBuilder, toStrStrategy);
        return stringBuilder.toString();
    }

    /**
     * Append current object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer String buffer current object appended to
     * @param toStrStrategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy toStrStrategy) {
        toStrStrategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, toStrStrategy);
        toStrStrategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    /**
     * Append all attributes to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param buffer String buffer all attributes appended to
     * @param toStrStrategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy toStrStrategy) {
        {
            String theSrc;
            theSrc = this.getSrc();
            toStrStrategy.appendField(locator, this, "src", buffer, theSrc);
        }
        {
            String theDst;
            theDst = this.getDst();
            toStrStrategy.appendField(locator, this, "dst", buffer, theDst);
        }
        {
            Relationtype theType;
            theType = this.getType();
            toStrStrategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            String theOwner;
            theOwner = this.getOwner();
            toStrStrategy.appendField(locator, this, "owner", buffer, theOwner);
        }
        return buffer;
    }

}
