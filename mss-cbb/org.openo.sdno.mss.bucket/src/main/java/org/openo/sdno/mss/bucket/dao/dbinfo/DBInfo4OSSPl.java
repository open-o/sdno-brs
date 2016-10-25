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

package org.openo.sdno.mss.bucket.dao.dbinfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class of data source information acquisition.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-18
 */
public class DBInfo4OSSPl implements IDBInfo {

    private static final String DRIVER_CLASS_NAME = "driverClassName";

    private static final String HOST = "jdbcHost";

    private static final String PORT = "jdbcPort";

    private static final String USER_NAME = "jdbcUsername";

    private static final String PWD = "jdbcPassword";

    private static final String DB_NAME = "bucketsys";

    private Map<String, String> dbInfoMap = new HashMap<String, String>();

    private static final String CONFIG_PATH = "webapps/ROOT/WEB-INF/classes/jdbc.properties";

    private static final String URL_CONFIG = "jdbc:mysql://%s:%s/%s?useUnicode=true&amp;characterEncoding=utf-8";

    @Override
    public String getDriver() {
        return this.dbInfoMap.get(DRIVER_CLASS_NAME);
    }

    @Override
    public String getUrl() {
        return String.format(URL_CONFIG, this.dbInfoMap.get(HOST), this.dbInfoMap.get(PORT), DB_NAME);
    }

    @Override
    public String getUser() {
        return this.dbInfoMap.get(USER_NAME);
    }

    @Override
    public String getPassword() {
        return this.dbInfoMap.get(PWD);
    }

    @Override
    public void destroyPassword() {
        this.dbInfoMap.clear();
    }

    @Override
    public void init() {
        this.dbInfoMap = getDataFromPropertiesFile(CONFIG_PATH);
    }

    /**
     * Destroy password.<br>
     * 
     * @since SDNO 0.5
     */
    public void destroy() {
        this.destroyPassword();
    }

    private static Map<String, String> getDataFromPropertiesFile(String path) {
        Map<String, String> dataFromMap = new HashMap<String, String>();
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(path);
            props.load(in);
            dataFromMap.put(DRIVER_CLASS_NAME, props.getProperty("jdbc.driver"));
            dataFromMap.put(HOST, props.getProperty("jdbc.host"));
            dataFromMap.put(PORT, props.getProperty("jdbc.port"));
            dataFromMap.put(USER_NAME, props.getProperty("jdbc.username"));
            dataFromMap.put(PWD, props.getProperty("jdbc.password"));
            in.close();
        } catch(IOException e) {
            dataFromMap.put(DRIVER_CLASS_NAME, "com.mysql.jdbc.Driver");
            dataFromMap.put(HOST, "localhost");
            dataFromMap.put(PORT, "3306");
            dataFromMap.put(USER_NAME, "root");
            dataFromMap.put(PWD, "root");
        }
        return dataFromMap;
    }
}
