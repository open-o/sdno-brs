/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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

package org.openo.sdno.mss.service.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.bucket.dao.pojo.RelationPojo;
import org.openo.sdno.mss.bucket.dao.pojo.ResourcePojo;
import org.openo.sdno.mss.bucket.impl.RelationHandlerImpl;
import org.openo.sdno.mss.bucket.impl.ResourceHandlerImpl;
import org.openo.sdno.mss.dao.pojo.InvCrossTablePojo;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Basic;
import org.openo.sdno.mss.schema.infomodel.Extension;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mockit.Mock;
import mockit.MockUp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext.xml",
                "classpath*:META-INF/spring/service.xml", "classpath*:META-INF/spring/mss_dao.xml",
                "classpath*:spring/service.xml"})
public class MssSvcResourceTest {

    @Autowired
    MssSvcResource mssSvcResource;

    @Test
    public void testGetResource() throws ServiceException {

        new MockResourceHandlerImpl();
        new MockRelationHandlerImpl();
        new MockInvCrossTablePojo();

        mssSvcResource.getResouce("brsdb", "managedelement", "objectId", "all");
    }

    private class MockResourceHandlerImpl extends MockUp<ResourceHandlerImpl> {

        @Mock
        public List<ResourcePojo> getBucketResources(String bktName) {

            Infomodel infoModel = new Infomodel();
            Datamodel dataModel = new Datamodel();

            infoModel.setName("managedelement");
            infoModel.setBasic(new Basic());
            infoModel.setExtension(new Extension());
            dataModel.setName("dataname");

            ResourcePojo resourcePojo = new ResourcePojo();
            resourcePojo.setImspec(JsonUtil.toJson(infoModel));
            resourcePojo.setDmspec(JsonUtil.toJson(dataModel));
            resourcePojo.setBktName("brsdb");

            return Arrays.asList(resourcePojo);
        }
    }

    private class MockRelationHandlerImpl extends MockUp<RelationHandlerImpl> {

        @Mock
        public RelationPojo getRelation(String bktName) {

            Map<String, RelationModelRelation> map = new HashMap<String, RelationModelRelation>();
            map.put("key", new RelationModelRelation());

            RelationPojo relationPojo = new RelationPojo();
            relationPojo.setRmspec(JsonUtil.toJson(map));

            return relationPojo;
        }
    }

    private class MockInvCrossTablePojo extends MockUp<InvCrossTablePojo> {

        @Mock
        public List<Map<String, Object>> getData(SqlSession session) {

            Map<String, Object> map = new HashMap<String, Object>();

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list.add(map);

            return list;
        }
    }
}
