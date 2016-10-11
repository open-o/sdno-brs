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

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the
 * org.openo.sdno.model.relationmodel package.<br>
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
     * for package:org.openo.sdno.model.relationmodel.
     * </p>
     * 
     * @since SDNO 0.5
     */
    public ObjectFactory() {
        // Constructor
    }

    /**
     * Create a instance of Relation model.<br>
     * 
     * @return Relation model instance created
     * @since SDNO 0.5
     */
    public Relationmodel createRelationmodel() {
        return new Relationmodel();
    }

    /**
     * Create a instance of Relation.<br>
     * 
     * @return Relation instance created
     * @since SDNO 0.5
     */
    public RelationModelRelation createRelationmodelRelation() {
        return new RelationModelRelation();
    }

    /**
     * Create a instance of Inventory.<br>
     * 
     * @return Inventory instance created
     * @since SDNO 0.5
     */
    public Inventory createInventory() {
        return new Inventory();
    }

}
