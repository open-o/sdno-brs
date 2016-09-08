/*
 * Copyright 2016, Huawei Technologies Co., Ltd.
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

package org.openo.sdno.mss.combine.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.impl.InvRelationDataHandler;

import mockit.Mock;
import mockit.MockUp;

public class InvRelationDataServiceTest {

    private InvRelationDataService invRelationDataService = new InvRelationDataService();

    private InvRelationDataHandler invRelationDataHandler = new InvRelationDataHandler();

    private InvRespEntity<List<Map<String, Object>>> invRespList = new InvRespEntity<List<Map<String, Object>>>();

    private InvRespEntity<Boolean> invRespBool = new InvRespEntity<Boolean>();

    @Before
    public void setUp() throws Exception {

        invRelationDataService.setRelationDataHandler(invRelationDataHandler);

        new MockUp<InvRelationDataHandler>() {

            @Mock
            public InvRespEntity<List<Map<String, Object>>> get(final String relationType, final String queryType,
                    final String srcUuid, final String dstUuid, final String srcAttribute, final String dstAttribute,
                    final String serviceType, final String refUnique) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> getRelationData(final String srcResType,
                    final String dstResType, final String srcUuids, final String dstUuids) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> add(final String relationType,
                    List<Map<String, Object>> values) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<Boolean> delete(final String relationType, final String srcUuid, final String dstUuid,
                    final String reltype) {
                return invRespBool;
            }
        };
    }

    @Test
    public void testGetRelationData() {
        assertEquals(invRespList, invRelationDataService.get("bucketname", "relationType", "queryType", "srcUuid",
                "dstUuid", "srcAttr", "dstAttr", "serviceType", "refUnique"));
    }

    @Test
    public void testGet() {
        assertEquals(invRespList,
                invRelationDataService.get("bucketname", "srcResType", "dstResType", "srcUuids", "dstUuids"));
    }

    @Test
    public void testAdd() {
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        assertEquals(invRespList, invRelationDataService.add("bucketname", "relationType", values));
    }

    @Test
    public void testDelete() {
        assertEquals(invRespBool,
                invRelationDataService.delete("bucketname", "relationType", "srcUuid", "dstUuid", "reltype"));
    }

}
