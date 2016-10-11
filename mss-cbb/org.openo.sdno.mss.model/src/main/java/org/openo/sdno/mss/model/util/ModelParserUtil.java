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

package org.openo.sdno.mss.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.openo.sdno.mss.schema.SchemaUtil;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.openo.sdno.mss.schema.relationmodel.Relationmodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Model parser class, parse model from its configure file. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ModelParserUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelParserUtil.class);

    private static final ModelParserUtil INSTANCE = new ModelParserUtil();

    private final Map<String, Infomodel> infoModels = new ConcurrentHashMap<String, Infomodel>();

    private final Map<String, Datamodel> dataModels = new ConcurrentHashMap<String, Datamodel>();

    private final Map<String, RelationModelRelation> relationModels =
            new ConcurrentHashMap<String, RelationModelRelation>();

    private ModelParserUtil() {

    }

    public Map<String, Infomodel> getInfoModels() {
        return infoModels;
    }

    public Map<String, Datamodel> getDataModels() {
        return dataModels;
    }

    public Map<String, RelationModelRelation> getRelationModels() {
        return relationModels;
    }

    /**
     * Get a singleton of this class. <br>
     * 
     * @return singleton of this class.
     * @since SDNO 0.5
     */
    public static ModelParserUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Clear the cache. <br>
     * 
     * @since SDNO 0.5
     */
    private void clearRamCache() {
        infoModels.clear();
        dataModels.clear();
        relationModels.clear();
    }

    /**
     * Parse the model file. <br>
     * 
     * @param files List of file to be parsed.
     * @since SDNO 0.5
     */
    public void parseModel(List<File> files) {
        if(null == files || files.isEmpty()) {
            LOGGER.error("modelFiles List is empty");
            return;
        }

        clearRamCache();

        for(File file : files) {
            parseModel(file);
        }

    }

    /**
     * Parse a single file. <br>
     * 
     * @param file wait to be parsed.
     * @since SDNO 0.5
     */
    private void parseModel(File file) {

        if(!file.exists() || !file.isFile() || !file.getName().endsWith(".xml")) {
            LOGGER.error("invalid model file input" + file.getName());
            return;
        }

        String fileName = file.getName();

        int index = fileName.indexOf('_');
        if(-1 >= index) {
            LOGGER.error("name of model file [" + fileName + "] is invalid");
            return;
        }

        String prefixFileName = fileName.substring(0, index);

        try {
            File modelFile = new File(file.getPath());
            if(prefixFileName.equalsIgnoreCase(ModelUtil.getInfoModelPrefix())) {
                parseInfoModel(modelFile);
            } else if(prefixFileName.equalsIgnoreCase(ModelUtil.getDataModelPrefix())) {
                parseDataModel(modelFile);
            } else if(prefixFileName.equalsIgnoreCase(ModelUtil.getRelationModelPrefix())) {
                parseRelationModel(modelFile);
            }
        } catch(JAXBException e) {
            LOGGER.error("Exception when parse mode file", e);
        }
    }

    /**
     * Parse the information model XML file to get a information model. <br>
     * 
     * @param file model file.
     * @throws JAXBException if model file is invalid.
     * @since SDNO 0.5
     */
    private void parseInfoModel(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.openo.sdno.mss.schema.infomodel.Inventory.class);
        if(!SchemaUtil.validateInfoModel(jaxbContext, file)) {
            LOGGER.error("check model {} failed", file.getName());
            return;
        }

        org.openo.sdno.mss.schema.infomodel.Inventory invenModel = null;
        FileInputStream fis = null;

        try {
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory spFactory = SaxParserFactories.newSecurityInstance();

            XMLReader xr = spFactory.newSAXParser().getXMLReader();
            fis = new FileInputStream(file);
            SAXSource src = new SAXSource(xr, new InputSource(fis));

            invenModel = (org.openo.sdno.mss.schema.infomodel.Inventory)jaxbUnmarshaller.unmarshal(src);
        } catch(SAXException | ParserConfigurationException | FileNotFoundException e) {
            LOGGER.error("Failed to parse info model", e);
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch(IOException e) {
                LOGGER.error("Faild to close FileInputStream when parse info model", e);
            }
        }

        if(null == invenModel) {
            return;
        }

        saveInfoModel(invenModel.getInfomodel());
    }

    /**
     * Save model to cache. <br>
     * 
     * @param infoModelList List of parsed model
     * @since SDNO 0.5
     */
    private void saveInfoModel(List<Infomodel> infoModelList) {
        for(Infomodel model : infoModelList) {
            this.infoModels.put(model.getName(), model);
        }
    }

    /**
     * Parse the datamodel file. <br>
     * 
     * @param file file wait to be parsed.
     * @throws JAXBException if the file is invalid.
     * @since SDNO 0.5
     */
    private void parseDataModel(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.openo.sdno.mss.schema.datamodel.Inventory.class);
        if(!SchemaUtil.validateDataModel(jaxbContext, file)) {
            LOGGER.error("check model {} failed", file.getName());
            return;
        }

        org.openo.sdno.mss.schema.datamodel.Inventory invDataModel = null;
        FileInputStream fis = null;
        try {
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory spFactory = SaxParserFactories.newSecurityInstance();

            XMLReader xr = spFactory.newSAXParser().getXMLReader();
            fis = new FileInputStream(file);
            SAXSource src = new SAXSource(xr, new InputSource(fis));

            invDataModel = (org.openo.sdno.mss.schema.datamodel.Inventory)jaxbUnmarshaller.unmarshal(src);
        } catch(SAXException | ParserConfigurationException | FileNotFoundException e) {
            LOGGER.error("Failed to parse data model", e);
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch(IOException e) {
                LOGGER.error("Faild to close FileInputStream when parse data model", e);
            }
        }

        if(null == invDataModel) {
            return;
        }

        saveDataModel(invDataModel.getDatamodel());
    }

    /**
     * Save datamodel to cache. <br>
     * 
     * @param dataModelList List of parsed model.
     * @since SDNO 0.5
     */
    private void saveDataModel(List<Datamodel> dataModelList) {
        for(Datamodel model : dataModelList) {
            this.dataModels.put(model.getName(), model);
        }
    }

    /**
     * Parse the relation model from file. <br>
     * 
     * @param file file need to be parsed.
     * @throws JAXBException if the file is invalid.
     * @since SDNO 0.5
     */
    private void parseRelationModel(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(org.openo.sdno.mss.schema.relationmodel.Inventory.class);
        if(!SchemaUtil.validateRelationModel(jaxbContext, file)) {
            LOGGER.error("check model {} failed", file.getName());
            return;
        }

        org.openo.sdno.mss.schema.relationmodel.Inventory invRelaModel = null;
        FileInputStream fis = null;
        try {
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory spFactory = SaxParserFactories.newSecurityInstance();

            XMLReader xr = spFactory.newSAXParser().getXMLReader();
            fis = new FileInputStream(file);
            SAXSource src = new SAXSource(xr, new InputSource(fis));

            invRelaModel = (org.openo.sdno.mss.schema.relationmodel.Inventory)jaxbUnmarshaller.unmarshal(src);
        } catch(SAXException | ParserConfigurationException | FileNotFoundException e) {
            LOGGER.error("Failed to parse relation model", e);
        } finally {
            try {
                if(fis != null) {
                    fis.close();
                }
            } catch(IOException e) {
                LOGGER.error("Faild to close FileInputStream when parse relation model", e);
            }
        }

        if(null == invRelaModel) {
            LOGGER.error("invRelaModel is null, file: {}", file.getPath());
            return;
        }

        saveRelationModel(invRelaModel.getRelationmodel());

    }

    /**
     * Save relationmodel to cache. <br>
     * 
     * @param relationModelList List of parsed model.
     * @since SDNO 0.5
     */
    private void saveRelationModel(List<Relationmodel> relationModelList) {
        for(Relationmodel model : relationModelList) {
            for(RelationModelRelation relation : model.getRelation()) {
                this.relationModels.put(relation.getSrc() + "-" + relation.getDst(), relation);
            }
        }
    }
}
