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
 * This is XML model class of Relation model.<br>
 * <p>
 * This XML model has one attribute: relation: Relation list of relation model
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"relation"})
@XmlRootElement(name = "relationmodel")
public class Relationmodel implements Serializable, Equals, HashCode, ToString {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected List<RelationModelRelation> relation;

    /**
     * Get relation attribute.<br>
     * 
     * @return relation attribute
     * @since SDNO 0.5
     */
    public List<RelationModelRelation> getRelation() {
        if(relation == null) {
            relation = new ArrayList<RelationModelRelation>();
        }
        return this.relation;
    }

    /**
     * Check whether this object equals to other object.<br>
     * 
     * @param thisObjLocator the ObjectLocator of this object
     * @param thatLocator the ObjectLocator of other object
     * @param object other inventory object
     * @param strategy compare strategy
     * @return true if this object equals to other object,false otherwise
     * @since SDNO 0.5
     */
    @Override
    public boolean equals(ObjectLocator thisObjLocator, ObjectLocator thatLocator, Object object,
            EqualsStrategy strategy) {
        if(!(object instanceof Relationmodel)) {
            return false;
        }
        if(this.equals(object)) {
            return true;
        }
        final Relationmodel that = (Relationmodel)object;
        {
            List<RelationModelRelation> thisRelation =
                    ((this.relation != null) && (!this.relation.isEmpty())) ? this.getRelation() : null;
            List<RelationModelRelation> thatRelation =
                    ((that.relation != null) && (!that.relation.isEmpty())) ? that.getRelation() : null;
            if(!strategy.equals(LocatorUtils.property(thisObjLocator, "relation", thisRelation),
                    LocatorUtils.property(thatLocator, "relation", thatRelation), thisRelation, thatRelation)) {
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
        final EqualsStrategy jaxbEqualsStrategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, jaxbEqualsStrategy);
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
        int resultHashCode = 1;
        {
            List<RelationModelRelation> thisRelation;
            thisRelation = ((this.relation != null) && (!this.relation.isEmpty())) ? this.getRelation() : null;
            resultHashCode = strategy.hashCode(LocatorUtils.property(locator, "relation", thisRelation), resultHashCode,
                    thisRelation);
        }
        return resultHashCode;
    }

    /**
     * Get hash code by INSTANCE HashCodeStrategy.<br>
     * 
     * @return hash code of this object
     * @since SDNO 0.5
     */
    public int hashCode() {
        final HashCodeStrategy curHashCodestrategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, curHashCodestrategy);
    }

    /**
     * Get XML description of inventory object.<br>
     * 
     * @return XML description of current object
     * @since SDNO 0.5
     */
    public String toString() {
        final StringBuilder resultStringbuffer = new StringBuilder();
        append(null, resultStringbuffer, JAXBToStringStrategy.INSTANCE);
        return resultStringbuffer.toString();
    }

    /**
     * Append current object to buffer.<br>
     * 
     * @param locator ObjectLocator object
     * @param appendStringBuffer String buffer current object appended to
     * @param strategy toString Strategy
     * @return String buffer after appending
     * @since SDNO 0.5
     */
    public StringBuilder append(ObjectLocator locator, StringBuilder appendStringBuffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, appendStringBuffer);
        appendFields(locator, appendStringBuffer, strategy);
        strategy.appendEnd(locator, this, appendStringBuffer);
        return appendStringBuffer;
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
            List<RelationModelRelation> thisRelation;
            thisRelation = ((this.relation != null) && (!this.relation.isEmpty())) ? this.getRelation() : null;
            strategy.appendField(locator, this, "relation", buffer, thisRelation);
        }
        return buffer;
    }
}
