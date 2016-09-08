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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.impl.InvDataHandler;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.init.util.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

public class InvDataServiceTest {

    private InvDataService invDataService = new InvDataService();

    private InvDataHandler invDataHandler = new InvDataHandler();

    private InvRespEntity<List<Map<String, Object>>> invRespList = new InvRespEntity<List<Map<String, Object>>>();

    private InvRespEntity<Map<String, Object>> invRespMap = new InvRespEntity<Map<String, Object>>();

    private InvRespEntity<Boolean> invRespBool = new InvRespEntity<Boolean>();

    private List<Object> respList = new ArrayList<Object>();

    private List<Map<String, Object>> respListMap = new ArrayList<Map<String, Object>>();

    @Before
    public void setUp() throws Exception {

        invDataService.setDataHandler(invDataHandler);

        new MockUp<InvDataHandler>() {

            @Mock
            public InvRespEntity<List<Map<String, Object>>> batchAdd(String resType, List<Map<String, Object>> values) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> batchGet(final String resType, final String attr,
                    final String filter, final String filterEx) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> getSplitPage(String resType, String attr, String filter,
                    String filterEx, String sortAttrName, boolean isAsc, Object refValue, String refUnique) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<Boolean> batchDelete(final String resType, List<String> uuidList) {
                return invRespBool;
            }

            @Mock
            public InvRespEntity<Map<String, Object>> update(String resType, String uuid, Map<String, Object> value) {
                return invRespMap;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> batchUpdate(String resType,
                    List<Map<String, Object>> values) {
                return invRespList;
            }

            @Mock
            public InvRespEntity<List<Map<String, Object>>> commQueryGet(String resType, List<String> attrsList,
                    List<HashMap<String, Object>> joinAttrList, String filterDsc, String filterData,
                    List<String> sortList, String pageNumber, String pageCapacity) {
                return invRespList;
            }

            @Mock
            public List<Object> commQueryGetCount(String resType, List<HashMap<String, Object>> joinAttrList,
                    String filterDsc, String filterData) {
                return respList;
            }

            @Mock
            public List<Object> queryRelationDataCount(String resType, List<String> attrsList, String filterStr,
                    List<String> sortList, String pageNum, String pageSize) {
                return respList;
            }

            @Mock
            public List<Map<String, Object>> queryRelationData(String resType, List<String> attrsList, String filterStr,
                    List<String> sortList, String pageNum, String pageSize) {
                return respListMap;
            }

            @Mock
            public Boolean exist(String resType, String attrName, Object attrVal) {
                return true;
            }
        };

        new MockUp<ValidUtil>() {

            @Mock
            public void checkResType(String resType) {
            }

            @Mock
            public void checkAttributes(String resType, String attributes, boolean checkHiddenAttr) {
            }

            @Mock
            public void checkFilter(String resType, String filter) {
            }

            @Mock
            public void checkSort(String resType, String attributes, String sortAttrName, Object refValue) {
            }

            @Mock
            public void checkSplitPage(String sortAttrName, Object refValue, Object uniqueValue) {
            }
        };

        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, String> getNonEmptyValuesInfoModelMap() {
                return new HashMap<String, String>();
            }
        };
    }

    @Test
    public void testAdd() {
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, String> getNonEmptyValuesInfoModelMap() {
                Map<String, String> valueMap = new HashMap<String, String>();
                valueMap.put("bkt-res-key", "key");
                return valueMap;
            }

            @Mock
            public Map<String, List<String>> getNonEmptyPropertyInfoModelMap() {
                List<String> tmpList = new ArrayList<String>();
                tmpList.add("key2");
                Map<String, List<String>> valueMap = new HashMap<String, List<String>>();
                valueMap.put("bkt-res", tmpList);
                return valueMap;
            }
        };

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key2", "value");
        List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
        tmpList.add(map);

        assertEquals(invRespList, invDataService.add("bkt", "res", tmpList));
    }

    @Test
    public void testBatchGetQueryAll() throws ServiceException {
        assertEquals(invRespList, invDataService.batchGet("bktName", "acceptHeader", "all", "resType", "attr", "filter",
                "filterEx", null, "sortType", null, "refUnique"));
    }

    @Test
    public void testBatchGet() throws ServiceException {
        assertEquals(invRespList, invDataService.batchGet("bktName", "acceptHeader", "", "resType", "attr", "filter",
                "filterEx", null, "sortType", null, "refUnique"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBatchGetRefValueNotNull() throws ServiceException {
        invDataService.batchGet("bktName", "acceptHeader", "queryType", "resType", "attr", "filter", "filterEx", null,
                "sortType", "refValue", "refUnique");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBatchSortAttrNotNull() throws ServiceException {
        invDataService.batchGet("bktName", "acceptHeader", "queryType", "resType", "attr", "filter", "filterEx",
                "sortAttrName", "sortType", "refValue", "refUnique");
    }

    @Test
    public void testBatchDelete() throws ServiceException {
        List<String> uuidList = new ArrayList<String>();
        assertEquals(invRespBool, invDataService.batchDelete("bktName", "resType", uuidList));
    }

    @Test
    public void testUpdate() throws ServiceException {
        Map<String, Object> value = new HashMap<String, Object>();
        assertEquals(invRespMap, invDataService.update("bkt", "res", "uuid", value));
    }

    @Test
    public void testBatchUpdate() throws ServiceException {
        Map<String, Object> value = new HashMap<String, Object>();
        assertEquals(invRespList, invDataService.batchUpdate("bktName", "resType", Arrays.asList(value)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommQueryGetAttrNull() throws ServiceException {
        invDataService.commQueryGet("bktName", "resType", null, "joinAttr", "filterDsc", "filterData", "sort",
                "pageNumber", "pageCapacity");
    }

    @Test
    public void testCommQueryGet() throws ServiceException {
        List<String> tmpList = new ArrayList<String>();
        tmpList.add("value");
        String listStr = JsonUtil.toJson(tmpList);

        HashMap<String, Object> map = new HashMap<String, Object>();
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        map.put("key", "value");
        mapList.add(map);
        String mapListStr = JsonUtil.toJson(mapList);

        assertEquals(invRespList, invDataService.commQueryGet("bktName", "resType", listStr, mapListStr, "filterDsc",
                "filterData", listStr, "pageNumber", "pageCapacity"));
    }

    @Test
    public void testCommQueryGetCount() throws ServiceException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        map.put("key", "value");
        mapList.add(map);
        String mapListStr = JsonUtil.toJson(mapList);

        assertEquals(respList,
                invDataService.commQueryGetCount("bktName", "resType", mapListStr, "filterDsc", "filterData"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryRelationDataCountAttrNull() throws ServiceException {
        invDataService.queryRelationDataCount("bktName", "resType", null, "filter", "sort", "pageNum", "pageSize");
    }

    @Test
    public void testQueryRelationDataCount() throws ServiceException {
        List<String> tmpList = new ArrayList<String>();
        tmpList.add("value");
        String listStr = JsonUtil.toJson(tmpList);
        assertEquals(respList, invDataService.queryRelationDataCount("bktName", "resType", listStr, "filter", listStr,
                "pageNum", "pageSize"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryRelationDataAttrNull() throws ServiceException {
        invDataService.queryRelationData("bktName", "resType", null, "filter", "sort", "pageNum", "pageSize");
    }

    @Test
    public void testQueryRelationData() throws ServiceException {
        List<String> tmpList = new ArrayList<String>();
        tmpList.add("value");
        String listStr = JsonUtil.toJson(tmpList);

        assertEquals(respListMap, invDataService.queryRelationData("bktName", "resType", listStr, "filter", listStr,
                "pageNum", "pageSize"));
    }

    @Test
    public void testExist() throws ServiceException {
        assertTrue(invDataService.exist("bktName", "resType", "attrName", "attrVal"));
    }

}
