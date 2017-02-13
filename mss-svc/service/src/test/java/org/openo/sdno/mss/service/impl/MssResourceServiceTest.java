/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.sdno.mss.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog.ChangeSet.PreConditions;
import org.openo.sdno.model.liquibasemodel.ObjectFactory;
import org.openo.sdno.mss.bucket.dao.dbinfo.DBInfo4OSSPl;
import org.openo.sdno.mss.combine.intf.InvDataService;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.ModelJedisCache;
import org.openo.sdno.mss.dao.model.entity.ModelRedisEntity;
import org.openo.sdno.mss.init.DbIniter;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.openo.sdno.mss.init.modelprocess.DataModelProcess;
import org.openo.sdno.mss.init.util.BucketStaticUtil;
import org.openo.sdno.mss.service.intf.MssResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import liquibase.exception.LiquibaseException;
import mockit.Mock;
import mockit.MockUp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/META-INF/mybatis/testservice.xml",
                "file:src/test/resources/META-INF/mybatis/testMybatisConfig.xml"})
public class MssResourceServiceTest {

    @Autowired
    MssResourceService mssResourceService;

    @Autowired
    InvDataService dataResImpl;

    @Test
    public void testSpring() throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dsrcResID", "testName1");
        map.put("showName", "testName2");
        map.put("dsrcID", "testName3");
        map.put("domainType", "testName4");
        map.put("solutionType", "testName5");
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        values.add(map);
        InvRespEntity<List<Map<String, Object>>> result =
                dataResImpl.add("inventory", "PerfInventory_Process_Statistics", values);

        Map<String, Object> resultMap = result.getData().get(0);
        String uuid = (String)resultMap.get("uuid");
        result = dataResImpl.get("inventory", "PerfInventory_Process_Statistics", uuid, "dsrcResID");
        resultMap = result.getData().get(0);
        System.out.println("test result of add");
        for(String key : resultMap.keySet()) {
            System.out.println(key + "=" + resultMap.get(key));
        }

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("dsrcResID", "oahuy1");
        map1.put("showName", "oahuy2");
        map1.put("dsrcID", "oahuy3");
        map1.put("domainType", "oahuy4");
        map1.put("solutionType", "oahuy5");
        InvRespEntity<Map<String, Object>> result1 =
                dataResImpl.update("inventory", "PerfInventory_Process_Statistics", uuid, map1);
        Map<String, Object> resultMap1 = result1.getData();
        // uuid = (String)resultMap1.get("uuid");
        System.out.println("test result of update");
        System.out.println("uuid" + "=" + uuid);
        result = dataResImpl.get("inventory", "PerfInventory_Process_Statistics", uuid, "dsrcResID");
        resultMap = result.getData().get(0);
        for(String key : resultMap.keySet()) {
            System.out.println(key + "=" + resultMap.get(key));
        }

        InvRespEntity<Boolean> result2 = dataResImpl.delete("inventory", "PerfInventory_Process_Statistics", uuid);
        System.out.println("test result of delete");
        System.out.println("is delete ok" + ":" + result2.getData());

    }

    @BeforeClass
    public static void prepare() throws LiquibaseException, SQLException, IOException, CloneNotSupportedException {

        new MockUp<ModelJedisCache>() {

            @Mock
            ModelRedisEntity getCacheEntity(String bktName) {
                return null;
            }

            @Mock
            boolean getCahce() {
                return true;
            }

            @Mock
            void putCacheEntity(String bktName, ModelRedisEntity entity) {
                return;
            }
        };
        new MockUp<DBInfo4OSSPl>() {

            @Mock
            String getUrl() {
                return "jdbc:h2:mem:inventory;MODE=MySQL";
            }

            @Mock
            String getDriver() {
                return "org.h2.Driver";
            }

            @Mock
            public String getUser() {
                return "ossdbuser";
            }

            @Mock
            public String getPassword() {
                return "123456";
            }
        };

        new MockUp<DBParam>() {

            @Mock
            String getUrl() {
                return "jdbc:h2:mem:inventory;MODE=MySQL";
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

        new MockUp<DataModelProcess>() {

            @Mock
            public PreConditions getPrecondIndex(ObjectFactory factory, String indexName, String tableName) {

                return new PreConditions();
            }

        };

        DBParam dbParam = new DBParam();
        dbParam.setDbName("inventory");
        dbParam.setDbUser("ossdbuser");
        String key = "123456";
        dbParam.setDbPwd(key.toCharArray());
        DbIniter initer = new DbIniter();
        System.setProperty("init.approot", System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "test" + File.separator + "resources");
        initer.init(dbParam);
    }

}
