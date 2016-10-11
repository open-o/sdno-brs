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

package org.openo.sdno.brs.util.http;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.ControllerMo;
import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.framework.container.util.PageQueryResult;

import mockit.Mock;
import mockit.MockUp;

/**
 * ResponseUtils test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ResponseUtilsTest {

    @Test
    public void testGetDataModelFromReqStrOk() throws ServiceException {
        ControllerMo oldController = new ControllerMo();
        oldController.setName("name");
        oldController.setId("1");
        oldController.setCreatetime(123456789L);

        Map<String, ControllerMo> map = new HashMap<String, ControllerMo>();
        map.put(Constant.CONTROLLER, oldController);

        String requestStr = null;

        try {
            requestStr = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        ControllerMo newController =
                HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.CONTROLLER, ControllerMo.class);
        assertTrue(oldController.getName().equals(newController.getName()));
        assertTrue(oldController.getId().equals(newController.getId()));
        assertTrue(oldController.getCreatetime().equals(newController.getCreatetime()));
    }

    @Test(expected = ServiceException.class)
    public void testGetDataModelFromReqStrException() throws ServiceException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.CONTROLLER, "test");

        String requestStr = null;

        try {
            requestStr = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.CONTROLLER, ControllerMo.class);
    }

    @Test
    public void testAssembleListRspDataOk() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        List<Object> objList = new ArrayList<>();
        SiteMO siteMO1 = new SiteMO();
        siteMO1.setName("site1");
        siteMO1.setId("1");

        List<Map<String, String>> relationList1 = new ArrayList<>();
        Map<String, String> relationMap1 = new HashMap<String, String>();
        relationMap1.put(Constant.RELATION_ID, "dstId1");
        relationMap1.put(Constant.RELATION_DSTTYPE, "site");
        relationList1.add(relationMap1);

        Map<String, String> relationMap2 = new HashMap<String, String>();
        relationMap2.put(Constant.RELATION_ID, "dstId2");
        relationMap2.put(Constant.RELATION_DSTTYPE, "site");
        relationList1.add(relationMap2);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put(Constant.RELATION, relationList1);

        objList.add(siteMO1);
        objList.add(map1);

        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setCurrentPage(0);
        pageQueryResult.setTotal(100);
        pageQueryResult.setPageSize(20);
        pageQueryResult.setTotalPageNum(5);
        pageQueryResult.setObjects(objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        List<SiteMO> respDataList = HttpResponseUtil.assembleListRspData(responseContent, pageRsp, SiteMO.class);
        assertTrue(pageRsp.getCurrentPageNum() == pageQueryResult.getCurrentPage());
        assertTrue(pageRsp.getPageSize() == pageQueryResult.getPageSize());
        assertTrue(pageRsp.getTotalNum() == pageQueryResult.getTotal());
        assertTrue(pageRsp.getTotalPageNum() == pageQueryResult.getTotalPageNum());

        boolean bMatch = false;
        for(SiteMO temp : respDataList) {
            if(siteMO1.getId().equals(temp.getId()) && siteMO1.getName().equals(temp.getName())) {
                bMatch = true;
                break;
            }
        }
        assertTrue(bMatch);
    }

    @Test(expected = ServiceException.class)
    public void testAssembleListRspDataExceptionWhenObjectsIsNull() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();
        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setObjects(null);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.assembleListRspData(responseContent, pageRsp, SiteMO.class);
    }

    @Test
    public void testAssembleListRspDataWhenObjectsIsNotList() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        PageQueryResult<String> pageQueryResult = new PageQueryResult<String>();
        pageQueryResult.setObjects("");

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        List<SiteMO> respDataList = HttpResponseUtil.assembleListRspData(responseContent, pageRsp, SiteMO.class);
        assertTrue(CollectionUtils.isEmpty(respDataList));
    }

    @Test(expected = ServiceException.class)
    public void testAssembleListRspDataWhenObjectsIsNotMap() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        List<Object> objList = new ArrayList<>();
        objList.add("test");

        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setObjects(objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.assembleListRspData(responseContent, pageRsp, SiteMO.class);
    }

    @Test
    public void testAssembleRspDataOk() throws ServiceException {
        List<Object> objList = new ArrayList<>();
        SiteMO siteMO = new SiteMO();
        siteMO.setName("site1");
        siteMO.setId("1");

        objList.add(siteMO);

        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        map.put(Constant.OBJECTS_KEY, objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        @SuppressWarnings("unchecked")
        List<SiteMO> respDataList = (List<SiteMO>)HttpResponseUtil.assembleRspData(responseContent, SiteMO.class);
        boolean bMatch = false;
        for(SiteMO temp : respDataList) {
            if(siteMO.getId().equals(temp.getId()) && siteMO.getName().equals(temp.getName())) {
                bMatch = true;
                break;
            }
        }
        assertTrue(bMatch);
    }

    @Test(expected = ServiceException.class)
    public void testAssembleRspDataExceptionWhenObjectsIsNull() throws ServiceException {
        new MockUp<JsonUtil>() {

            @Mock
            public <T> T unMarshal(String jsonstr, Class<T> type) throws IOException {
                return null;
            }
        };

        List<Object> objList = new ArrayList<>();
        SiteMO siteMO = new SiteMO();
        siteMO.setName("site1");
        siteMO.setId("1");

        objList.add(siteMO);

        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        map.put(Constant.OBJECTS_KEY, objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.assembleRspData(responseContent, SiteMO.class);
    }

    @Test(expected = ServiceException.class)
    public void testAssembleRspDataExceptionWhenObjectsIsNotList() throws ServiceException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.OBJECTS_KEY, "test");

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.assembleRspData(responseContent, SiteMO.class);
    }

    @Test(expected = ServiceException.class)
    public void testAssembleRspDataExceptionWhenObjectsIsNotMap() throws ServiceException {
        List<Object> objList = new ArrayList<>();
        objList.add("test");

        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        map.put(Constant.OBJECTS_KEY, objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(map);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpResponseUtil.assembleRspData(responseContent, SiteMO.class);
    }

    @Test
    public void testAssembleListRspWithRelationDataOk() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        List<Object> objList = new ArrayList<>();
        SiteMO siteMO = new SiteMO();
        siteMO.setName("site1");
        siteMO.setId("1");

        objList.add(siteMO);

        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setCurrentPage(0);
        pageQueryResult.setTotal(100);
        pageQueryResult.setPageSize(20);
        pageQueryResult.setTotalPageNum(5);
        pageQueryResult.setObjects(objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        List<SiteMO> respDataList =
                HttpRelationUtil.assembleListRspWithRelationData(responseContent, pageRsp, SiteMO.class);
        boolean bMatch = false;
        for(SiteMO temp : respDataList) {
            if(siteMO.getId().equals(temp.getId()) && siteMO.getName().equals(temp.getName())) {
                bMatch = true;
                break;
            }
        }
        assertTrue(bMatch);

        assertTrue(pageRsp.getCurrentPageNum() == pageQueryResult.getCurrentPage());
        assertTrue(pageRsp.getPageSize() == pageQueryResult.getPageSize());
        assertTrue(pageRsp.getTotalNum() == pageQueryResult.getTotal());
        assertTrue(pageRsp.getTotalPageNum() == pageQueryResult.getTotalPageNum());
    }

    @Test
    public void testAssembleListRspWithRelationDataExceptionWhenObjectsIsNull() throws ServiceException {
        new MockUp<PageQueryResult<List<Object>>>() {

            @Mock
            public List<Object> getObjects() {
                return null;
            }
        };

        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        List<Object> objList = new ArrayList<>();
        SiteMO siteMO = new SiteMO();
        siteMO.setName("site1");
        siteMO.setId("1");

        objList.add(siteMO);

        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setObjects(objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        List<SiteMO> respDataList =
                HttpRelationUtil.assembleListRspWithRelationData(responseContent, pageRsp, SiteMO.class);
        assertTrue(CollectionUtils.isEmpty(respDataList));
    }

    @Test
    public void testAssembleListRspWithRelationDataExceptionWhenObjectsIsNotList() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        PageQueryResult<String> pageQueryResult = new PageQueryResult<String>();
        pageQueryResult.setObjects("test");

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        List<SiteMO> respDataList =
                HttpRelationUtil.assembleListRspWithRelationData(responseContent, pageRsp, SiteMO.class);
        assertTrue(CollectionUtils.isEmpty(respDataList));
    }

    @Test(expected = ServiceException.class)
    public void testAssembleListRspWithRelationDataExceptionWhenObjectsIsNotMap() throws ServiceException {
        @SuppressWarnings("rawtypes")
        PageResponseData pageRsp = new PageResponseData();

        List<Object> objList = new ArrayList<>();
        objList.add("test");

        PageQueryResult<List<Object>> pageQueryResult = new PageQueryResult<List<Object>>();
        pageQueryResult.setObjects(objList);

        String responseContent = null;
        try {
            responseContent = JsonUtil.marshal(pageQueryResult);
        } catch(IOException e) {
            assertTrue(false);
        }

        HttpRelationUtil.assembleListRspWithRelationData(responseContent, pageRsp, SiteMO.class);
    }

    @Test
    public void testGetFilterValueExceptionWhenQueryMapIsNull() throws IOException {
        Map<String, String> filterMap = new HashMap<String, String>();

        String result = HttpResponseUtil.getFilterValue(null, SiteMO.class);
        assertTrue("{}".equals(result));

        result = HttpResponseUtil.getFilterValue(filterMap, SiteMO.class);
        assertTrue("{}".equals(result));

    }

    @Test
    public void testGetFilterValueOk() throws IOException {
        Map<String, String> filterMap = new HashMap<String, String>();
        filterMap.put("type", "111");

        String result = HttpResponseUtil.getFilterValue(filterMap, SiteMO.class);
        assertTrue(result.contains("typeValue"));
    }
}
