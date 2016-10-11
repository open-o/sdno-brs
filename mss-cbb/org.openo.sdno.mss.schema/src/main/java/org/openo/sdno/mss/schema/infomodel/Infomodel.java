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
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This is XML model class of info model in info model.<br>
 * <p>
 * This XML model has five attributes: basic: basic property of info model extension: extension
 * property of info model name: the name of info model data model: the name of data model related to
 * info model version: the version of info model
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "infomodel")
public class Infomodel implements Serializable {

    private static final long serialVersionUID = 123456L;

    @XmlElement(required = true)
    protected Basic basic;

    @XmlElement(required = true)
    protected Extension extension;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "datamodel", required = true)
    protected String datamodel;

    @XmlAttribute(name = "version", required = true)
    protected BigDecimal version;

    /**
     * Get basic attribute.<br>
     * 
     * @return basic attribute
     * @since SDNO 0.5
     */
    public Basic getBasic() {
        return basic;
    }

    /**
     * Set basic attribute.<br>
     * 
     * @param value basic object
     * @since SDNO 0.5
     */
    public void setBasic(Basic value) {
        this.basic = value;
    }

    /**
     * Get extension attribute.<br>
     * 
     * @return extension attribute
     * @since SDNO 0.5
     */
    public Extension getExtension() {
        return extension;
    }

    /**
     * Set extension attribute.<br>
     * 
     * @param value Extension object
     * @since SDNO 0.5
     */
    public void setExtension(Extension value) {
        this.extension = value;
    }

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
     * @param value String
     * @since SDNO 0.5
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Get data model attribute.<br>
     * 
     * @return data model attribute
     * @since SDNO 0.5
     */
    public String getDatamodel() {
        return datamodel;
    }

    /**
     * Set datamodel attribute.<br>
     * 
     * @param value String
     * @since SDNO 0.5
     */
    public void setDatamodel(String value) {
        this.datamodel = value;
    }

    /**
     * Get Version attribute.<br>
     * 
     * @return Version attribute
     * @since SDNO 0.5
     */
    public BigDecimal getVersion() {
        return version;
    }

    /**
     * Set Version attribute.<br>
     * 
     * @param value BigDecimal Object
     * @since SDNO 0.5
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

}
