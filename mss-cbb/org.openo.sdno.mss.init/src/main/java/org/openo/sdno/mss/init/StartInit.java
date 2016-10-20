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

import java.io.IOException;
import java.sql.SQLException;

import org.openo.sdno.mss.init.dbinfo.DBParam;

import liquibase.exception.LiquibaseException;

/**
 * Database Initialize Class.<br>
 *
 * @author
 * @version SDNO 0.5 2016-6-30
 */
public class StartInit {

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

        DBParam dbParam = new DBParam();
        dbParam.setDbName("brsdb");
        dbParam.setDbPwd("root".toCharArray());
        dbParam.setDbType("mysql");
        dbParam.setDbUser("root");
        dbParam.setHost("localhost");
        dbParam.setPort(3306);
        DbIniter brsiniter = new DbIniter();
        brsiniter.init(dbParam);

        dbParam.setDbName("vxlandb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter vxlaniniter = new DbIniter();
        vxlaniniter.init(dbParam);

        dbParam.setDbName("compositevpndb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter compositevpndbiniter = new DbIniter();
        compositevpndbiniter.init(dbParam);

        dbParam.setDbName("l3vpn");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter l3vpniniter = new DbIniter();
        l3vpniniter.init(dbParam);

        dbParam.setDbName("l2vpn");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter l2vpniniter = new DbIniter();
        l2vpniniter.init(dbParam);

        dbParam.setDbName("osdriverdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter osdriveriniter = new DbIniter();
        osdriveriniter.init(dbParam);

        dbParam.setDbName("ipsecdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter ipseciniter = new DbIniter();
        ipseciniter.init(dbParam);

        dbParam.setDbName("acbranchdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter acbranchiniter = new DbIniter();
        acbranchiniter.init(dbParam);

        dbParam.setDbName("vpcdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter vpciniter = new DbIniter();
        vpciniter.init(dbParam);

        dbParam.setDbName("servicechaindb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter servicechaininiter = new DbIniter();
        servicechaininiter.init(dbParam);

        dbParam.setDbName("scdriverdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter dcdriveriniter = new DbIniter();
        dcdriveriniter.init(dbParam);

        dbParam.setDbName("nslcmdb");
        dbParam.setDbPwd("root".toCharArray());
        DbIniter nslcminiter = new DbIniter();
        nslcminiter.init(dbParam);
    }
}
