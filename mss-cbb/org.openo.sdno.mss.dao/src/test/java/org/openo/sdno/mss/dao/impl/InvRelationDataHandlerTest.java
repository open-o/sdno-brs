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

package org.openo.sdno.mss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.BaseModel;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.model.RelationGraphMgrUtil;
import org.openo.sdno.mss.dao.pojo.InvRelationTablePojo;
import org.openo.sdno.mss.schema.infomodel.Basic;
import org.openo.sdno.mss.schema.infomodel.Infomodel;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvRelationDataHandler test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class InvRelationDataHandlerTest {

    private final String relationType = "srcType-desType";

    private final String queryType = "test";

    private final String srcUuid = "src-1234";

    private final String dstUuid = "des-1234";

    private final String srcAttribute = "all";

    private final String dstAttribute = "all";

    private final String serviceType = "test";

    private final String refUnique = "";

    @Before
    public void setUp() throws Exception {
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getInfoModelMap() {
                HashMap<String, Infomodel> map = new HashMap<String, Infomodel>();
                Infomodel infomodel = new Infomodel();
                infomodel.setBasic(new Basic());
                map.put("srcType", infomodel);
                map.put("desType", infomodel);
                return map;
            }
        };
    }

    /**
     * Test get() result when target uuid list is empty!.
     *
     * @since SDNO 0.5
     */
    @Test
    public void testGetNoneTarget() {

        new MockUp<RelationGraphMgrUtil>() {

            @Mock
            public List<String> findPathBetweenRes(String srcRes, String dstRes) {
                ArrayList<String> paths = new ArrayList<String>();
                paths.add("srcType-desType");
                return paths;
            }
        };

        new MockUp<InvRelationTablePojo>() {

            @Mock
            public List<Map<String, Object>> getData(SqlSession session) {
                List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
                return map;
            }

        };

        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        // Get use empty srcUuid.
        BaseModel baseModel = new BaseModel("", dstUuid, srcAttribute, dstAttribute, queryType, serviceType);
        InvRespEntity<List<Map<String, Object>>> relationships =
                invRelationDataHandler.get(relationType, refUnique, baseModel);

        Assert.assertTrue(relationships.isSuccess());
        Assert.assertEquals(relationships.getData().size(), 0);
    }

    /**
     * Test get() result when none path be found.
     *
     * @since SDNO 0.5
     */
    @Test
    public void testGetNonePath() {

        new MockUp<RelationGraphMgrUtil>() {

            @Mock
            public List<String> findPathBetweenRes(String srcRes, String dstRes) {

                return new ArrayList<String>();
            }
        };

        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        // Get use empty destUuid.
        BaseModel baseModel = new BaseModel(srcUuid, "", srcAttribute, dstAttribute, queryType, serviceType);
        InvRespEntity<List<Map<String, Object>>> relationships =
                invRelationDataHandler.get(relationType, refUnique, baseModel);

        Assert.assertTrue(relationships.isSuccess());
        Assert.assertEquals(relationships.getData().size(), 0);

    }

    /**
     * Test get() result when UUIDs are both empty.
     *
     * @since SDNO 0.5
     */
    @Test
    public void testGetUuidBothEmpty() {

        try {
            InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

            // Get use empty UUIDs.
            BaseModel baseModel = new BaseModel("", "", srcAttribute, dstAttribute, queryType, serviceType);
            invRelationDataHandler.get(relationType, refUnique, baseModel);

            Assert.assertTrue(false);
        } catch(IllegalArgumentException e) {
            String message = e.getMessage();
            Assert.assertTrue(message.contains("both empty"));
        }

    }

    /**
     * Test get() result when UUIDs are both not nonempty.
     *
     * @since SDNO 0.5
     */
    @Test
    public void testGetUuidBothNonempty() {
        try {

            InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

            // Get use empty destUuid.
            BaseModel baseModel = new BaseModel(srcUuid, dstUuid, srcAttribute, dstAttribute, queryType, serviceType);
            invRelationDataHandler.get(relationType, refUnique, baseModel);

            Assert.assertTrue(false);
        } catch(IllegalArgumentException e) {
            String message = e.getMessage();
            Assert.assertTrue(message.contains("both have"));
        }
    }

    @Test
    public void testAddOneDataSuccess() {

        new MockUp<InvRelationDataHandlerImpl>() {

            @Mock
            public int doAdd(SqlSession session, InvRelationTablePojo relationPojo, Map<String, Object> valueMap) {

                return 1;
            }
        };

        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("key", null);
        boolean result = invRelationDataHandler.addOneData(null, new InvRelationTablePojo("String"), valueMap);
        Assert.assertTrue(result);
    }

    @Test
    public void testAddOneDataFailed() {

        new MockUp<InvRelationDataHandlerImpl>() {

            @Mock
            public int doAdd(SqlSession session, InvRelationTablePojo relationPojo, Map<String, Object> valueMap) {

                return 0;
            }
        };

        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("key", null);
        boolean result = invRelationDataHandler.addOneData(null, new InvRelationTablePojo("String"), valueMap);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteEmptyTypes() {
        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("key", null);
        invRelationDataHandler.delete("-", srcUuid, dstUuid, null);
        Assert.assertTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteUuidsEmpty() {
        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("key", null);
        invRelationDataHandler.delete("srcType-dstType", "", "", null);
        Assert.assertTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteDesEmpty() {
        InvRelationDataHandlerImpl invRelationDataHandler = new InvRelationDataHandlerImpl();

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("key", null);
        invRelationDataHandler.delete("srcType-", srcUuid, dstUuid, null);
        Assert.assertTrue(false);
    }

}
