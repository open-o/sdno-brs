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

package org.openo.sdno.brs.resource;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.ManagedElementMO;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.service.impl.ResWithRelationQueryServiceImpl;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext.xml",
                "classpath*:META-INF/spring/service.xml", "classpath*:spring/service.xml"})
public class ManagedElementResourceTest {

    @Mocked
    HttpServletRequest request;

    @Autowired
    ManagedElementResource neResource;

    private String jsonMap =
            "{\"managedElement\":{\"id\":\"neid\",\"name\":\"Ne1\",\"description\":\"TestNetworkElement\",\"productName\":\"SNC\",\"isVirtual\":\"true\",\"source\":\"os\",\"version\":\"V1R3\",\"logicID\":\"849494949\",\"phyNeID\":\"8965969966906\",\"nativeID\":\"networkelementlid\",\"managementDomainID\":\"44444444444444\",\"ipAddress\":\"192.168.1.1\",\"adminState\":\"active\",\"operState\":\"up\",\"tenantID\":\"testTenantID\"}}";

    private ManagedElementMO managedElement = buildManagedElementMO();

    @Before
    public void setUp() throws Exception {

        new MockRestUtils();
    }

    @Test
    public void testAddManagedElement() throws ServiceException {

        new MockRestfulProxyAdd();

        Map<String, Object> map = (Map<String, Object>)neResource.addManagedElement(request);

        assertTrue(map.containsKey(Constant.MANAGEDELEMENT_KEY));
    }

    @Test
    public void testUpdateManagedElement() throws ServiceException {

        new MockRestfulProxyUpdate();

        Map<String, Object> map = (Map<String, Object>)neResource.updateManagedElement("neid", request);

        assertTrue(map.containsKey(Constant.MANAGEDELEMENT_KEY));
    }

    @Test
    public void testGetManagedElement() throws ServiceException {

        new MockRestfulProxyUpdate();

        Map<String, Object> map = (Map<String, Object>)neResource.getManagedElement("uuid");

        assertTrue(map.containsKey(Constant.MANAGEDELEMENT_KEY));
    }

    @Test
    public void testGetManagedElementList() throws ServiceException {

        new MockRestfulProxyAdd();

        List<ManagedElementMO> meinfo = (List<ManagedElementMO>)neResource.getManagedElementList("tenant", request);

        assertTrue(meinfo.isEmpty());
    }

    @Test
    public void testDelManagedElement() throws ServiceException {

        new MockRestfulProxyUpdate();

        Integer status = (Integer)neResource.delManagedElement("uuid", request);

        assertTrue(200 == status);
    }

    private class MockRestfulProxyAdd extends MockUp<RestfulProxy> {

        @Mock
        public RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {

            PageQueryResult<List<ManagedElementMO>> pageResult = new PageQueryResult<List<ManagedElementMO>>();
            pageResult.setObjects(new ArrayList<ManagedElementMO>());

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(pageResult));

            return response;
        }

        @Mock
        public RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECTS_KEY, Arrays.asList(managedElement));

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(map));

            return response;
        }
    }

    private class MockRestfulProxyUpdate extends MockUp<RestfulProxy> {

        @Mock
        public RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);

            Map<String, Object> map = new HashMap<String, Object>();

            if(uri.contains("relationships")) {
                map.put("relationships", new ArrayList<Relation>());
                response.setResponseJson(JsonUtil.toJson(map));
            } else if(uri.contains("topologicallink")) {

                PageQueryResult<List<ManagedElementMO>> pageResult = new PageQueryResult<List<ManagedElementMO>>();
                pageResult.setObjects(new ArrayList<ManagedElementMO>());

                response.setResponseJson(JsonUtil.toJson(pageResult));

                return response;
            } else {

                map.put(Constant.OBJECT_KEY, managedElement);
                response.setResponseJson(JsonUtil.toJson(map));
            }

            return response;
        }

        @Mock
        public RestfulResponse delete(String uri, RestfulParametes restParametes) throws ServiceException {

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);

            return response;
        }

        @Mock
        public RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECTS_KEY, Arrays.asList(managedElement));

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(map));

            return response;
        }

        @Mock
        public RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECT_KEY, managedElement);

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(map));

            return response;
        }
    }

    private class MockResQuery extends MockUp<ResWithRelationQueryServiceImpl> {

        @Mock
        public <T extends RootEntity> T getResourceByID(String objectID, Class<T> classType) throws ServiceException {
            return (T)managedElement;
        }
    }

    private class MockRestUtils extends MockUp<RestUtils> {

        @Mock
        public String getRequestBody(HttpServletRequest request) {
            return jsonMap;
        }
    }

    private ManagedElementMO buildManagedElementMO() {

        Map<String, ManagedElementMO> managedElementMap = JsonUtil.fromJson(jsonMap, Map.class);

        Object objME = managedElementMap.get(Constant.MANAGEDELEMENT_KEY);
        String strContent = JsonUtil.toJson(objME);

        return JsonUtil.fromJson(strContent, ManagedElementMO.class);
    }
}
