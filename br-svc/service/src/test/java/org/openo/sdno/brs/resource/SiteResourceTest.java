/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.model.SiteMO;
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
public class SiteResourceTest {

    @Mocked
    HttpServletRequest request;

    @Autowired
    SiteResource siteResource;

    private String jsonMap =
            "{\"site\":{\"id\":\"uuid\",\"name\":\"Site1\",\"type\":\"tenant_site\",\"tenantID\":\"tenantid\",\"location\":\"testlocation\"}}";

    private SiteMO siteMO = buildSiteMO();

    @Before
    public void setUp() throws Exception {

        new MockRestUtils();
    }

    @Test
    public void testAddSite() throws ServiceException {

        new MockRestfulProxyAdd();

        Map<String, Object> map = (Map<String, Object>)siteResource.addSite(request);

        assertTrue(map.containsKey(Constant.SITE_KEY));
    }

    @Test
    public void testUpdateSite() throws ServiceException {

        new MockRestfulProxyUpdate();

        Map<String, Object> map = (Map<String, Object>)siteResource.updateSite("siteid", request);

        assertTrue(map.containsKey(Constant.SITE_KEY));
    }

    @Test
    public void testGetSite() throws ServiceException {

        new MockRestfulProxyUpdate();

        Map<String, Object> map = (Map<String, Object>)siteResource.getSite("uuid", request);

        assertTrue(map.containsKey(Constant.SITE_KEY));
    }

    @Test
    public void testGetSiteList() throws ServiceException {

        new MockRestfulProxyAdd();
        new MockHttpServletRequest();

        siteResource.getSiteList(request);

    }

    @Test
    public void testDeleteSite() throws ServiceException {

        new MockRestfulProxyUpdate();

        Map<String, Object> map = (Map<String, Object>)siteResource.deleteSite("uuid", request);

        assertTrue(map.containsValue(true));
    }

    private class MockRestfulProxyAdd extends MockUp<RestfulProxy> {

        @Mock
        public RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECTS_KEY, Arrays.asList(siteMO));

            PageQueryResult<Map<String, Object>> pageResult = new PageQueryResult<Map<String, Object>>();
            pageResult.setObjects(map);

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(pageResult));

            return response;
        }

        @Mock
        public RestfulResponse post(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECTS_KEY, Arrays.asList(siteMO));

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

                PageQueryResult<List<SiteMO>> pageResult = new PageQueryResult<List<SiteMO>>();
                pageResult.setObjects(new ArrayList<SiteMO>());

                response.setResponseJson(JsonUtil.toJson(pageResult));

                return response;
            } else {

                map.put(Constant.OBJECT_KEY, siteMO);
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
            map.put(Constant.OBJECTS_KEY, Arrays.asList(siteMO));

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(map));

            return response;
        }

        @Mock
        public RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.OBJECT_KEY, siteMO);

            RestfulResponse response = new RestfulResponse();
            response.setStatus(200);
            response.setResponseJson(JsonUtil.toJson(map));

            return response;
        }
    }

    private class MockResQuery extends MockUp<ResWithRelationQueryServiceImpl> {

        @Mock
        public <T extends RootEntity> T getResourceByID(String objectID, Class<T> classType) throws ServiceException {
            return (T)siteMO;
        }
    }

    private class MockRestUtils extends MockUp<RestUtils> {

        @Mock
        public String getRequestBody(HttpServletRequest request) {
            return jsonMap;
        }
    }

    private class MockHttpServletRequest extends MockUp<HttpServletRequest> {

        @Mock
        public String getQueryString() {
            return jsonMap;
        }
    }

    private SiteMO buildSiteMO() {

        Map<String, SiteMO> managedElementMap = JsonUtil.fromJson(jsonMap, Map.class);

        Object objME = managedElementMap.get(Constant.SITE_KEY);
        String strContent = JsonUtil.toJson(objME);

        return JsonUtil.fromJson(strContent, SiteMO.class);
    }

}
