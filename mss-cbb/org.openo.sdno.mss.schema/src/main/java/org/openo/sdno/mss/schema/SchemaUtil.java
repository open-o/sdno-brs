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

package org.openo.sdno.mss.schema;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Schema static class that verify model file.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public final class SchemaUtil {

    /**
     * Logger handler
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaUtil.class);

    private SchemaUtil() {

    }

    /**
     * Verify the validity of Info Model.<br>
     * 
     * @param jaxbCongtext JAXBContext Object
     * @param file info model file
     * @return true if the model is valid, false otherwise
     * @since SDNO 0.5
     */
    public static boolean validateInfoModel(JAXBContext jaxbCongtext, File file) {
        if(null == jaxbCongtext || null == file) {
            LOGGER.error("jaxbCongtext or file cannt be null");
            return false;
        }

        return validate(jaxbCongtext, file,
                SchemaUtil.class.getClassLoader().getResource("META-INF/models/infomodel-1.0.xsd"));
    }

    /**
     * Verify the validity of Data Model.<br>
     * 
     * @param jaxbCongtext JAXBContext Object
     * @param file data model file
     * @return true if the model is valid, false otherwise
     * @since SDNO 0.5
     */
    public static boolean validateDataModel(JAXBContext jaxbCongtext, File file) {
        if(null == jaxbCongtext || null == file) {
            LOGGER.error("jaxbCongtext or file cannt be null");
            return false;
        }

        return validate(jaxbCongtext, file,
                SchemaUtil.class.getClassLoader().getResource("META-INF/models/datamodel-1.0.xsd"));
    }

    /**
     * Verify the validity of Relation Model.<br>
     * 
     * @param jaxbCongtext JAXBContext Object
     * @param file relation model file
     * @return true if the model is valid, false otherwise
     * @since SDNO 0.5
     */
    public static boolean validateRelationModel(JAXBContext jaxbCongtext, File file) {
        if(null == jaxbCongtext || null == file) {
            LOGGER.error("jaxbCongtext or file cannt be null");
            return false;
        }

        return validate(jaxbCongtext, file,
                SchemaUtil.class.getClassLoader().getResource("META-INF/models/relationmodel-1.0.xsd"));
    }

    /**
     * Verify the validity of XML File.<br>
     * 
     * @param jaxbCongtext the JAXBContext Object
     * @param file XML file
     * @param xsdUrl URL that represents a schema.
     * @return true if the file is valid, false otherwise
     * @since SDNO 0.5
     */
    private static boolean validate(JAXBContext jaxbCongtext, File file, URL xsdUrl) {
        SchemaFactory schemaFactory = null;
        Schema schema = null;
        Source xmlFile = new StreamSource(file);
        try {
            schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = schemaFactory.newSchema(xsdUrl);
            Validator validator = schema.newValidator();
            DocumentBuilderFactory db = newSecuDocBuilderFactory();
            db.setNamespaceAware(true);

            DocumentBuilder builder = db.newDocumentBuilder();
            Document doc = builder.parse(file);

            DOMSource source = new DOMSource(doc);
            DOMResult result = new DOMResult();

            validator.validate(source, result);
            LOGGER.debug(xmlFile.getSystemId() + " is valid");
        } catch(Exception ex) {
            LOGGER.error(xmlFile.getSystemId() + " is NOT valid", ex);
            return false;
        }
        return true;
    }

    /**
     * Create BuilderFactory.<br>
     * 
     * @return DocumentBuilderFactory object created
     * @since SDNO 0.5
     */
    private static DocumentBuilderFactory newSecuDocBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Map<String, Boolean> featureMap = new HashMap<String, Boolean>();
        featureMap.put("http://xml.org/sax/features/external-general-entities", false);
        featureMap.put("http://xml.org/sax/features/external-parameter-entities", false);
        featureMap.put("http://apache.org/xml/features/disallow-doctype-decl", true);
        featureMap.put("XMLConstants.FEATURE_SECURE_PROCESSING", true);

        for(Map.Entry<String, Boolean> entry : featureMap.entrySet()) {
            try {
                factory.setFeature(entry.getKey(), entry.getValue());
            } catch(ParserConfigurationException e) {
                LOGGER.error("ParserConfigurationException occurs.", e);
                LOGGER.error("FAILED to set feature " + entry.getKey() + " to " + entry.getValue());
            }
        }
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setValidating(true);

        return factory;
    }

}
