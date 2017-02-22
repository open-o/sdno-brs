/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Property initialization class, read configure file and get values of the specific key. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    private static PropertiesUtil instance = null;

    private final String mybatisPropertiePath = "/META-INF/conf/bucket.properties";

    private String invTablePrefix = null;

    private String extensionTablePostfix = null;

    private String relationTablePostfix = null;

    private String changeSetAuthor = null;

    private String defaultSchema = null;

    private String[] basicTableFixedColumn = null;

    private String[] extensionTableColumn = null;

    private String[] extendedIndexs = null;

    private String[] relationTableColumn = null;

    private HashMap<String, Integer> relationTypeValus = null;

    private String[] relationIndexs = null;

    private String infoModelPrefix = null;

    private String dataModelPrefix = null;

    private String relaModelPrefix = null;

    private PropertiesUtil() {
        URL url = getClass().getResource(mybatisPropertiePath);
        initProperties(url);
    }

    public String getINVTABLEPREFIX() {
        return invTablePrefix;
    }

    public String getEXTENSIONTABLEPOSTFIX() {
        return extensionTablePostfix;
    }

    public String getCHANGESETAUTHOR() {
        return changeSetAuthor;
    }

    public String getDEFAULTSCHEMA() {
        return defaultSchema;
    }

    public String[] getBasicTableFixedColumn() {
        return basicTableFixedColumn;
    }

    public String[] getExtensionTableColumn() {
        return extensionTableColumn;
    }

    public String getINFOMODELPREFIX() {
        return infoModelPrefix;
    }

    public String getDATAMODELPREFIX() {
        return dataModelPrefix;
    }

    public String getRELAMODELPREFIX() {
        return relaModelPrefix;
    }

    public String[] getRELATIONTABLECOLUMN() {
        return relationTableColumn;
    }

    public String[] getRELATIONINDEXS() {
        return relationIndexs;
    }

    public Map<String, Integer> getRELATIONTYPEVALUES() {
        return relationTypeValus;
    }

    public String getRELATIONTABLEPOSTFIX() {
        return relationTablePostfix;
    }

    public String[] getEXTENDINDEXS() {
        return extendedIndexs;
    }

    /**
     * Get a singleton of PropertiesUtil. <br/>
     * 
     * @return a singleton of PropertiesUtil.
     * @since SDNO 0.5
     */
    public synchronized static PropertiesUtil getInstance() {
        if(instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }

    /**
     * Initialization from property file. <br/>
     * 
     * @param url the URL of configure file.
     * @since SDNO 0.5
     */
    private void initProperties(URL url) {
        if(null == url) {
            LOGGER.error("Can Load empty path.");
            return;
        }

        Properties invProperties = getProperties(url);
        if(null == invProperties || invProperties.isEmpty()) {
            LOGGER.error("Load invsvr.properties failed !");
            return;
        }

        invTablePrefix = invProperties.getProperty("tableprefix");
        extensionTablePostfix = invProperties.getProperty("exttablepostfix");
        relationTablePostfix = invProperties.getProperty("relationtablepostfix");
        changeSetAuthor = invProperties.getProperty("changesetauthor");
        defaultSchema = invProperties.getProperty("defaultschema");
        basicTableFixedColumn = invProperties.getProperty("basictablefixedcolumn").split("/");
        extensionTableColumn = invProperties.getProperty("exttablecolumn").split("/");
        extendedIndexs = invProperties.getProperty("exttableindex").split("/");
        relationTableColumn = invProperties.getProperty("relationtablecolumn").split("/");
        relationIndexs = invProperties.getProperty("relationtableindex").split("/");

        infoModelPrefix = invProperties.getProperty("infomodelprefix");
        dataModelPrefix = invProperties.getProperty("datamodelprefix");
        relaModelPrefix = invProperties.getProperty("relamodelprefix");
        relationTypeValus = getRelationTypeValues(invProperties.getProperty("relationtypevalue"));
    }

    /**
     * Parse the relation type and its value from property. <br/>
     * 
     * @param property the string contain type and its value number in style like "type=integer".
     * @return Map contain type and its value.
     * @since SDNO 0.5
     */
    private static HashMap<String, Integer> getRelationTypeValues(String property) {
        HashMap<String, Integer> relationTypeKeyValue = new HashMap<>();
        if(StringUtils.isEmpty(property)) {
            LOGGER.warn("Relationship Type enumeration is empty");
            return relationTypeKeyValue;
        }
        String[] keyValues = property.split("/");
        for(String keyValueStr : keyValues) {
            String[] nameValue = keyValueStr.split("=");
            if(!ArrayUtils.isEmpty(nameValue) && nameValue.length == 2) {
                relationTypeKeyValue.put(nameValue[0], Integer.valueOf(nameValue[1]));
            }
        }
        return relationTypeKeyValue;
    }

    /**
     * Get properties from configure file. <br/>
     * 
     * @param cfgFileName name of the configure file.
     * @return Properties parsed from the file.
     * @since SDNO 0.5
     */
    public static Properties getProperties(String cfgFileName) {

        Properties cfgProperties = new Properties();
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(new File(cfgFileName));

            cfgProperties.load(stream);

        } catch(FileNotFoundException e) {
            LOGGER.error("File not found.", e);
        } catch(IOException e) {
            LOGGER.error("Load stream fail.", e);
        } finally {
            try {
                if(null != stream) {
                    stream.close();
                }
            } catch(IOException e) {
                LOGGER.info("Close Stream Fail!", e);
            }
        }

        return cfgProperties;

    }

    /**
     * Get Properties from web URL. <br/>
     * 
     * @param configUrl URL of the configure file.
     * @return Properties parsed from web.
     * @since SDNO 0.5
     */
    public static Properties getProperties(URL configUrl) {
        if(null == configUrl) {
            return null;
        }

        InputStream in = null;
        Properties properties = new Properties();

        try {
            in = configUrl.openStream();
            properties.load(in);
        } catch(IOException ex) {
            LOGGER.error("config failed, configURL = " + configUrl, ex);
            throw new IllegalArgumentException(ex);
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch(IOException ex) {
                    LOGGER.info("close stream failed", ex);
                }
            }
        }

        return properties;
    }

    /**
     * Turn filename to URL path. <br/>
     * 
     * @param cfgFilename name of the configure file.
     * @return URL of the configure file.
     * @since SDNO 0.5
     */
    public static URL getURL(String cfgFilename) {
        File file = new File(cfgFilename);
        try {
            return file.toURI().toURL();
        } catch(MalformedURLException ex) {
            LOGGER.error("config failed, configFilename = " + cfgFilename, ex);
            return null;
        }
    }
}
