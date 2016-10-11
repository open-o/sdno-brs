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

package org.openo.sdno.mss.init.dbinfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for initialization parameter of database.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class DBParam implements Cloneable {

    private String host;

    private int port;

    private String dbType;

    private String dbName;

    private String dbUser;

    private char[] dbPwd;

    private static final Map<String, String> DRIVER_CLASS = new HashMap<String, String>();

    static {
        DRIVER_CLASS.put("mysql", "com.mysql.jdbc.Driver");
        DRIVER_CLASS.put("sybase", "com.sybase.jdbc3.jdbc.SybDriver");
        DRIVER_CLASS.put("oracle", "oracle.jdbc.driver.OracleDriver");
        DRIVER_CLASS.put("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DRIVER_CLASS.put("db2", "com.ibm.db2.jcc.DB2Driver");
        DRIVER_CLASS.put("gauss", "org.postgresql.Driver");
    }

    private static final Map<String, String> CONN_URL_PATTERN = new HashMap<String, String>();

    static {
        CONN_URL_PATTERN.put("mysql", "jdbc:mysql://{ip}:{port}/{dbname}?"
                + "useUnicode=true&characterEncoding=utf-8&noAccessToProcedureBodies=true");
        CONN_URL_PATTERN.put("sybase", "jdbc:sybase:Tds:{ip}:{port}/{dbname}");
        CONN_URL_PATTERN.put("oracle", "jdbc:oracle:thin:@{ip}:{port}:{dbname}");
        CONN_URL_PATTERN.put("sqlserver", "jdbc:sqlserver://{ip}:{port};databaseName={dbname}");
        CONN_URL_PATTERN.put("db2", "jdbc:db2://{ip}:{port}/{dbname}");
        CONN_URL_PATTERN.put("gauss", "jdbc:postgresql://{ip}:{port}/{dbname}");
    }

    /**
     * @return Returns the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host The host to set.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return Returns the dbType.
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * @param dbType The dbType to set.
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * @return Returns the dbName.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName The dbName to set.
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return Returns the dbUser.
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @param dbUser The dbUser to set.
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * @return Returns the dbPwd.
     */
    public char[] getDbPwd() {
        return dbPwd;
    }

    /**
     * @param dbPwd The dbPwd to set.
     */
    public void setDbPwd(char[] dbPwd) {
        this.dbPwd = dbPwd;
    }

    /**
     * Return the driver class of the Corresponding DB.<br>
     * 
     * @return driver URL of the given DB.
     * @since SDNO 0.5
     */
    @SuppressWarnings("static-access")
    public String getDriver() {
        return this.DRIVER_CLASS.get(this.dbType);
    }

    /**
     * @return The URL of the DB.
     * @since SDNO 0.5
     */
    public String getUrl() {
        String urlPattern = CONN_URL_PATTERN.get(this.dbType.toLowerCase());
        return urlPattern.replace("{ip}", this.host).replace("{port}", String.valueOf(this.port)).replace("{dbname}",
                this.dbName);
    }

    /**
     * clear the pass word in the memory.<br>
     * 
     * @since SDNO 0.5
     */
    public void destroyPassword() {
        if(null != this.dbPwd) {
            for(int i = 0; i < this.dbPwd.length; i++) {
                this.dbPwd[i] = '\0';
            }
        }
    }

    /**
     * Deep clone method.<br>
     * 
     * @return DBParam copied.
     * @since SDNO 0.5
     */
    @Override
    public DBParam clone() throws CloneNotSupportedException {

        super.clone();
        DBParam dst = new DBParam();
        dst.setDbName(this.getDbName());
        dst.setDbPwd(String.valueOf(this.getDbPwd()).toCharArray());
        dst.setDbType(this.getDbType());
        dst.setHost(this.getHost());
        dst.setPort(this.getPort());
        dst.setDbUser(this.getDbUser());

        return dst;
    }

}
