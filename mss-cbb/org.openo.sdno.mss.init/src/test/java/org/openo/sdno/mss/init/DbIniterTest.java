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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog.ChangeSet.PreConditions;
import org.openo.sdno.model.liquibasemodel.ObjectFactory;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.openo.sdno.mss.init.modelprocess.DataModelProcess;
import org.openo.sdno.mss.init.util.BucketStaticUtil;

import liquibase.exception.LiquibaseException;
import mockit.Mock;
import mockit.MockUp;

public class DbIniterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        new MockUp<DataModelProcess>() {

            @Mock
            public PreConditions getPrecondIndex(ObjectFactory factory, String indexName, String tableName) {

                return new PreConditions();
            }

        };

        new MockUp<BucketStaticUtil>() {

            @Mock
            public String getBucketChangeLogDirPath() {
                return System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "resources" + File.separator + "bucket" + File.separator + "changesets";
            }

            @Mock
            public String getBucketRootPath() {
                return System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "resources" + File.separator + "bucket";
            }

            @Mock
            public String getChangeLogName() {
                return "changelog.xml";
            }

        };

        new MockUp<DBParam>() {

            @Mock
            String getUrl() {
                String url = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "resources" + File.separator + "bucket" + File.separator + "databsefile";
                return "jdbc:h2:mem:init;MODE=MySQL";
            }

            @Mock
            String getDriver() {
                return "org.h2.Driver";
            }

            @Mock
            public String getDbUser() {
                return "ossdbuser";
            }

            @Mock
            public char[] getDbPwd() {
                return "123456".toCharArray();
            }

            @Mock
            public String getDbType() {
                return "mysql";
            }

            @Mock
            public String getDbName() {
                return "inventory";
            }
        };
    }

    @Test
    public void testInitNormal() throws LiquibaseException, SQLException, IOException, CloneNotSupportedException {
        DBParam dbParam = new DBParam();
        dbParam.setDbName("init");
        dbParam.setDbUser("ossdbuser");
        String key = "123456";
        dbParam.setDbPwd(key.toCharArray());
        DbIniter initer = new DbIniter();
        initer.init(dbParam);
    }

    public void moveFiles(List<String> folders, String current) throws IOException {
        for(String s : folders) {
            File src = new File(s);
            String d = s.replace(current, "elements");
            File des = new File(d);
            FileUtils.copyFile(src, des);

        }

    }
}
