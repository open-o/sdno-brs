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

package org.openo.sdno.mss.dao.pojo;

import java.math.BigInteger;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.sdno.mss.init.util.ValidUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Property;

/**
 * Inventory attribute entity POJO. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class InvAttrEntityPojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvAttrEntityPojo.class);

    /**
     * Attribute name.
     */
    private String attrName;

    /**
     * Attribute value.
     */
    private Object attrValue;

    /**
     * Attribute type.
     */
    private String attrType;

    /**
     * Data type.
     */
    private Datatype dataType;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param attrName
     */
    public InvAttrEntityPojo(String attrName) {
        this.attrName = attrName;
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param attrName
     * @param attrValue
     */
    public InvAttrEntityPojo(String attrName, Object attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param property
     */
    public InvAttrEntityPojo(Property property) {
        this.attrName = property.getName();
        this.dataType = property.getType();

        switch(dataType) {
            case INTEGER:
                attrType = "int";
                break;
            case STRING:
                attrType = "varchar(" + property.getLength() + ")";
                break;
            case DECIMAL: {
                StringBuilder sb = new StringBuilder();
                sb.append("decimal");
                buildPrecision(sb, property.getLength(), property.getScale());
                attrType = sb.toString();
                break;
            }
            case DATETIME:
                attrType = "date";
                break;
            case FLOAT: {
                StringBuilder sb = new StringBuilder();
                sb.append("float");
                buildPrecision(sb, property.getLength(), property.getScale());
                attrType = sb.toString();
                break;
            }
            case DOUBLE: {
                attrType = "double";
                break;
            }
            default:
                LOGGER.warn("Not support yet: " + property.getType());
                break;
        }
    }

    /**
     * Set value.<br>
     * 
     * @param attrValue
     * @return
     * @since SDNO 0.5
     */
    public InvAttrEntityPojo buildValue(Object attrValue) {
        this.attrValue = attrValue;
        return this;
    }

    /**
     * Set name of attribute. <br>
     * 
     * @param attrName
     * @return
     * @since SDNO 0.5
     */
    public InvAttrEntityPojo setAttrName(String attrName) {
        this.attrName = attrName;
        return this;
    }

    /**
     * Handle the Scaling and accuracy of the string.<br>
     * 
     * @param type Data type.
     * @param length Length of the string.
     * @return
     */
    private void buildPrecision(StringBuilder sb, BigInteger length, BigInteger scale) {
        if(length == null || scale == null) {
            return;
        }

        if(ValidUtil.checkPropertyPrecision(length.intValue(), scale.intValue())) {
            sb.append('(').append(length).append(',').append(scale).append(')');
        }
    }

    /**
     * Get the attribute name. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * Get the attribute. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public Object getAttrValue() {
        return attrValue;
    }

    /**
     * Get attribute type. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public String getAttrType() {
        return attrType;
    }

    /**
     * Get data type. <br>
     * 
     * @return Data type.
     * @since SDNO 0.5
     */
    public Datatype getDataType() {
        return dataType;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(attrName);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof InvAttrEntityPojo)) {
            return false;
        }

        InvAttrEntityPojo pojo = (InvAttrEntityPojo)obj;
        return ObjectUtils.equals(attrName, pojo.attrName) && ObjectUtils.equals(attrValue, pojo.attrValue)
                && ObjectUtils.equals(dataType, pojo.dataType);
    }

    @Override
    public String toString() {
        return "attrName = " + attrName + ", attrValue = " + attrValue + ", attrType = " + attrType;
    }
}
