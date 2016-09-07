/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

    private static final String MYBAITS_PROPERTIES_PATH = "/META-INF/conf/bucket.properties";

    private static String INVTABLEPREFIX = null;

    private static String EXTENSIONTABLEPOSTFIX = null;

    private static String RELATIONTABLEPOSTFIX = null;

    private static String CHANGESETAUTHOR = null;

    private static String DEFAULTSCHEMA = null;

    private static String[] BASICTABLEFIXEDCOLIMN = null;

    private static String[] EXRENSIONTABLECOLUMN = null;

    private static String[] EXTENDINDEXS = null;

    private static String[] RELATIONTABLECOLUMN = null;

    private static HashMap<String, Integer> RELATIONTYPEVALUES = null;

    private static String[] RELATIONINDEXS = null;

    private static String INFOMODELPREFIX = null;

    private static String DATAMODELPREFIX = null;

    private static String RELAMODELPREFIX = null;

    private static final PropertiesUtil INSTANCE = new PropertiesUtil();

    private PropertiesUtil() {
        URL url = getClass().getResource(MYBAITS_PROPERTIES_PATH);
        initProperties(url);
    }

    public String getINVTABLEPREFIX() {
        return INVTABLEPREFIX;
    }

    public String getEXTENSIONTABLEPOSTFIX() {
        return EXTENSIONTABLEPOSTFIX;
    }

    public String getCHANGESETAUTHOR() {
        return CHANGESETAUTHOR;
    }

    public String getDEFAULTSCHEMA() {
        return DEFAULTSCHEMA;
    }

    public String[] getBasicTableFixedColumn() {
        return BASICTABLEFIXEDCOLIMN;
    }

    public String[] getExtensionTableColumn() {
        return EXRENSIONTABLECOLUMN;
    }

    public String getINFOMODELPREFIX() {
        return INFOMODELPREFIX;
    }

    public String getDATAMODELPREFIX() {
        return DATAMODELPREFIX;
    }

    public String getRELAMODELPREFIX() {
        return RELAMODELPREFIX;
    }

    public String[] getRELATIONTABLECOLUMN() {
        return RELATIONTABLECOLUMN;
    }

    public String[] getRELATIONINDEXS() {
        return RELATIONINDEXS;
    }

    public Map<String, Integer> getRELATIONTYPEVALUES() {
        return RELATIONTYPEVALUES;
    }

    public String getRELATIONTABLEPOSTFIX() {
        return RELATIONTABLEPOSTFIX;
    }

    public String[] getEXTENDINDEXS() {
        return EXTENDINDEXS;
    }

    /**
     * Get a singleton of PropertiesUtil. <br/>
     * 
     * @return a singleton of PropertiesUtil.
     * @since SDNO 0.5
     */
    public static PropertiesUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Initialization from property file. <br/>
     * 
     * @param url the url of configure file.
     * @since SDNO 0.5
     */
    private static void initProperties(URL url) {
        if(null == url) {
            LOGGER.error("Can Load empty path.");
            return;
        }

        Properties invProperties = getProperties(url);
        if(null == invProperties || invProperties.isEmpty()) {
            LOGGER.error("Load invsvr.properties failed !");
            return;
        }

        INVTABLEPREFIX = invProperties.getProperty("tableprefix");
        EXTENSIONTABLEPOSTFIX = invProperties.getProperty("exttablepostfix");
        RELATIONTABLEPOSTFIX = invProperties.getProperty("relationtablepostfix");
        CHANGESETAUTHOR = invProperties.getProperty("changesetauthor");
        DEFAULTSCHEMA = invProperties.getProperty("defaultschema");
        BASICTABLEFIXEDCOLIMN = invProperties.getProperty("basictablefixedcolumn").split("/");
        EXRENSIONTABLECOLUMN = invProperties.getProperty("exttablecolumn").split("/");
        EXTENDINDEXS = invProperties.getProperty("exttableindex").split("/");
        RELATIONTABLECOLUMN = invProperties.getProperty("relationtablecolumn").split("/");
        RELATIONINDEXS = invProperties.getProperty("relationtableindex").split("/");

        INFOMODELPREFIX = invProperties.getProperty("infomodelprefix");
        DATAMODELPREFIX = invProperties.getProperty("datamodelprefix");
        RELAMODELPREFIX = invProperties.getProperty("relamodelprefix");
        RELATIONTYPEVALUES = getRelationTypeValues(invProperties.getProperty("relationtypevalue"));
    }

    /**
     * Parse the relation type and its value from property. <br/>
     * 
     * @param property the string contain type and its value num in style like "type=int".
     * @return Map contain type and its value.
     * @since SDNO 0.5
     */
    private static HashMap<String, Integer> getRelationTypeValues(String property) {
        HashMap<String, Integer> relationTypeKeyValue = new HashMap<String, Integer>();
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
     * Get Properties from web url. <br/>
     * 
     * @param configUrl url of the configure file.
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
     * Turn filename to url path. <br/>
     * 
     * @param cfgFilename name of the configure file.
     * @return url of the configure file.
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
