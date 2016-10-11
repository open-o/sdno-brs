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

package org.openo.sdno.mss.dao.multi;

import java.util.Map;

import org.openo.sdno.framework.container.util.DBInfoReader;
import org.openo.sdno.framework.container.util.DefaultEnvUtil;
import org.openo.sdno.mss.dao.constant.InvDbType;

/**
 * Database information for OSS platform. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class DBInfo4OSSPlatForm implements IDBInfo {

    public static final String SERVER_NAME = "serverName";

    public static final String PWD = "passwd";

    public static final String USER = "user";

    public static final String PORT = "port";

    private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";

    public static final String DEFAULT_IP = "localhost";

    private static final String DB_NAME = "bucketsys";

    private String dbName = "";

    private String driver = "";

    private String url = "";

    private String user = null;

    private String pwd = null;

    /**
     * Get inventory database type. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public static InvDbType getType() {
        return InvDbType.SYBASE;
    }

    @Override
    public String getDriver() {
        return this.driver;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getUserName() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getDBName() {
        return this.dbName;
    }

    @Override
    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public void destroyPassword() {
        if(null != this.pwd) {
            this.pwd = null;
        }
    }

    @Override
    public void init(String path) {
        this.driver = DEFAULT_DRIVER;

        String jsonName = DefaultEnvUtil.getAppName() + "-" + DefaultEnvUtil.getAppVersion() + ".json";
        Map<String, Map<String, Object>> dbInfoMap = DBInfoReader.readDbInfo(jsonName);
        if(dbInfoMap.isEmpty()) {
            return;
        }

        Map<String, Object> bucketSysMap = dbInfoMap.get(DB_NAME);

        String dbNodeIP = (String)bucketSysMap.get(SERVER_NAME);
        if(null == dbNodeIP) {
            dbNodeIP = DEFAULT_IP;
        }

        String dbPort = bucketSysMap.get(PORT).toString();

        this.url = "jdbc:mysql://" + dbNodeIP + ":" + dbPort + "/" + this.dbName
                + "?useUnicode=true&characterEncoding=UTF-8";

        this.user = (String)bucketSysMap.get(USER);

        String dbEncodePwd = (String)bucketSysMap.get(PWD);

        this.pwd = dbEncodePwd;
    }
}
