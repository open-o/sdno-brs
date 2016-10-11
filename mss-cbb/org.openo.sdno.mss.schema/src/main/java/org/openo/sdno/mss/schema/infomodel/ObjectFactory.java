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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openo.sdno.model.infomodel package. <br>
 * <p>
 * An ObjectFactory allows you to construct new instances of the Java representation for XML
 * content. The Java representation of XML content can consist of schema derived interfaces and
 * classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Constructor<br>
     * <p>
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: org.openo.sdno.model.infomodel
     * </p>
     * 
     * @since SDNO 0.5
     */
    public ObjectFactory() {
        // Constructor
    }

    /**
     * Create a Extension instance.<br>
     * 
     * @return Extension instance created
     * @since SDNO 0.5
     */
    public Extension createExtension() {
        return new Extension();
    }

    /**
     * Create a Extension.Property instance.<br>
     * 
     * @return Extension.Property instance created
     * @since SDNO 0.5
     */
    public Extension.Property createExtensionProperty() {
        return new Extension.Property();
    }

    /**
     * Create a Info model instance.<br>
     * 
     * @return Info model instance created
     * @since SDNO 0.5
     */
    public Infomodel createInfomodel() {
        return new Infomodel();
    }

    /**
     * Create a Basic instance.<br>
     * 
     * @return Basic instance created
     * @since SDNO 0.5
     */
    public Basic createBasic() {
        return new Basic();
    }

    /**
     * Create a infomodel.Property instance.<br>
     * 
     * @return infomodel.Property instance created
     * @since SDNO 0.5
     */
    public org.openo.sdno.mss.schema.infomodel.Property createProperty() {
        return new org.openo.sdno.mss.schema.infomodel.Property();
    }

    /**
     * Create a Inventory instance.<br>
     * 
     * @return Inventory instance created
     * @since SDNO 0.5
     */
    public Inventory createInventory() {
        return new Inventory();
    }

}
