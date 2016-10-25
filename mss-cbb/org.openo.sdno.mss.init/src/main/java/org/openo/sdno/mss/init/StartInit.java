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

package org.openo.sdno.mss.init;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openo.sdno.mss.init.dbinfo.DBParam;

import liquibase.exception.LiquibaseException;

/**
 * Database Initialize Class.<br>
 *
 * @author
 * @version SDNO 0.5 2016-6-30
 */
public class StartInit {

    private static final String CONFIG_PATH = "webapps/ROOT/WEB-INF/classes/jdbc.properties";

    private static final String HOST = "jdbcHost";

    private static final String PORT = "jdbcPort";

    private static final String USER_NAME = "jdbcUsername";

    private static final String PWD = "jdbcPassword";

    /**
     * Constructor<br>
     *
     * @since SDNO 0.5
     */
    private StartInit() {
        // do nothing
    }

    /**
     * Execution Enter Point of Database Initialization.<br>
     *
     * @param args parameter list
     * @throws SQLException when initialize database failed
     * @throws LiquibaseException when initialize database failed
     * @throws IOException when initialize database failed
     * @throws CloneNotSupportedException when when initialize database failed
     * @since SDNO 0.5
     */
    public static void main(String[] args)
            throws LiquibaseException, SQLException, IOException, CloneNotSupportedException {
        Map<String, String> dbInfoMap = getDataFromPropertiesFile(CONFIG_PATH);
        DBParam dbParam = new DBParam();
        dbParam.setDbType("mysql");
        dbParam.setHost(dbInfoMap.get(HOST));
        dbParam.setPort(Integer.parseInt(dbInfoMap.get(PORT)));
        dbParam.setDbUser(dbInfoMap.get(USER_NAME));
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());

        dbParam.setDbName("brsdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter brsiniter = new DbIniter();
        brsiniter.init(dbParam);

        dbParam.setDbName("vxlandb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter vxlaniniter = new DbIniter();
        vxlaniniter.init(dbParam);

        dbParam.setDbName("compositevpndb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter compositevpndbiniter = new DbIniter();
        compositevpndbiniter.init(dbParam);

        dbParam.setDbName("l3vpn");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter l3vpniniter = new DbIniter();
        l3vpniniter.init(dbParam);

        dbParam.setDbName("l2vpn");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter l2vpniniter = new DbIniter();
        l2vpniniter.init(dbParam);

        dbParam.setDbName("osdriverdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter osdriveriniter = new DbIniter();
        osdriveriniter.init(dbParam);

        dbParam.setDbName("ipsecdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter ipseciniter = new DbIniter();
        ipseciniter.init(dbParam);

        dbParam.setDbName("acbranchdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter acbranchiniter = new DbIniter();
        acbranchiniter.init(dbParam);

        dbParam.setDbName("vpcdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter vpciniter = new DbIniter();
        vpciniter.init(dbParam);

        dbParam.setDbName("servicechaindb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter servicechaininiter = new DbIniter();
        servicechaininiter.init(dbParam);

        dbParam.setDbName("scdriverdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter dcdriveriniter = new DbIniter();
        dcdriveriniter.init(dbParam);

        dbParam.setDbName("nslcmdb");
        dbParam.setDbPwd(dbInfoMap.get(PWD).toCharArray());
        DbIniter nslcminiter = new DbIniter();
        nslcminiter.init(dbParam);
    }

    private static Map<String, String> getDataFromPropertiesFile(String path) {

        Map<String, String> dataFromMap = new HashMap<String, String>();
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(path);
            props.load(in);
            dataFromMap.put(HOST, props.getProperty("jdbc.host"));
            dataFromMap.put(PORT, props.getProperty("jdbc.port"));
            dataFromMap.put(USER_NAME, props.getProperty("jdbc.username"));
            dataFromMap.put(PWD, props.getProperty("jdbc.password"));
            in.close();
        } catch(IOException e) {
            dataFromMap.put(HOST, "localhost");
            dataFromMap.put(PORT, "3306");
            dataFromMap.put(USER_NAME, "root");
            dataFromMap.put(PWD, "root");
        }
        return dataFromMap;
    }
}
