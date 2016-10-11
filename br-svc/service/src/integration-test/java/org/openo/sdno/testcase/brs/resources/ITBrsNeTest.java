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

package org.openo.sdno.testcase.brs.resources;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testcase.brs.checker.BrsChecker;
import org.openo.sdno.testcase.brs.checker.SendUtil;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

/**
 * ITBrsNe test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsNeTest extends TestManager {

    private static final String CREATE_PATH = "src/integration-test/resources/brstestcase/brsNeTestCase/create.json";

    private static final String CREATE_CONTROLLER_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/createcontroller.json";

    private static final String CREATE_SITE_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/createsite.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsNeTestCase/get.json";

    private static final String GET_PARAM_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/getparam.json";

    private static final String GET_TENANT_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/gettenant.json";

    private static final String UPDATE_PATH = "src/integration-test/resources/brstestcase/brsNeTestCase/update.json";

    private static final String DELETE_PATH = "src/integration-test/resources/brstestcase/brsNeTestCase/delete.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/getdelete.json";

    private static final String DELETE_CONTROLLER_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/deletecontroller.json";

    private static final String DELETE_SITE_PATH =
            "src/integration-test/resources/brstestcase/brsNeTestCase/deletesite.json";

    private List<String> controllerUuidList = new ArrayList<String>();

    private List<String> siteUuidList = new ArrayList<String>();

    @Test
    public void neCreateSuccessTest() {
        String uuid = null;
        try {
            createControllerAndSite();

            HttpRquestResponse httpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(CREATE_PATH)));
            BrsChecker neChecker = new BrsChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(setRequest(httpObject), neChecker);
            Map<String, Map<String, String>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
            uuid = responseMap.get("managedElement").get("id");

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(uuid);

            deleteControllerAndSite();
        }
    }

    @Test
    public void neUpdateSuccessTest() {
        String uuid = null;
        try {
            uuid = createNe();

            File updateFile = new File(UPDATE_PATH);
            doTest(uuid, updateFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(uuid);

            deleteControllerAndSite();
        }
    }

    @Test
    public void neGetSuccessTest() {
        String uuid = null;
        try {
            uuid = createNe();

            File queryFile = new File(GET_PATH);
            doTest(uuid, queryFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(uuid);

            deleteControllerAndSite();
        }
    }

    @Test
    public void neGetListWithTenantSuccessTest() {
        String uuid = null;
        try {
            uuid = createNe();

            File queryFile = new File(GET_TENANT_PATH);
            doTest(uuid, queryFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(uuid);

            deleteControllerAndSite();
        }
    }

    @Test
    public void neGetListWithParamSuccessTest() {
        String uuid = null;
        try {
            uuid = createNe();

            File queryFile = new File(GET_PARAM_PATH);
            doTest(uuid, queryFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(uuid);

            deleteControllerAndSite();
        }
    }

    @Test
    public void neDeleteSuccessTest() {
        try {
            String uuid = createNe();

            File deleteFile = new File(DELETE_PATH);
            doTest(uuid, deleteFile);

            File queryDeleteFile = new File(GET_DELETE_PATH);
            doTest(uuid, queryDeleteFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {

            deleteControllerAndSite();
        }
    }

    private String createNe() throws ServiceException {
        createControllerAndSite();

        File createFile = new File(CREATE_PATH);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse = SendUtil.doSend(setRequest(httpObject));
        Map<String, Map<String, String>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("managedElement").get("id");
    }

    private HttpRequest setRequest(HttpRquestResponse httpObject) {
        HttpRequest request = httpObject.getRequest();
        Map<String, Map<String, Object>> requestMap = JsonUtil.fromJson(request.getData(), Map.class);
        Map<String, Object> requestParamMap = requestMap.get("managedElement");
        requestParamMap.put("controllerID", controllerUuidList);
        requestParamMap.put("siteID", siteUuidList);
        requestMap.remove("managedElement");
        requestMap.put("managedElement", requestParamMap);
        request.setData(JsonUtil.toJson(requestMap));
        return request;
    }

    private void deleteNe(String uuid) {
        try {
            File deleteNeFile = new File(DELETE_PATH);
            HttpRquestResponse deleteHttpObject;

            deleteHttpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteNeFile));

            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("object_id", deleteHttpObject.getRequest().getUri(), uuid));
            SendUtil.doSend(deleteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private void deleteControllerAndSite() {
        try {
            File deleteControllerFile = new File(DELETE_CONTROLLER_PATH);
            HttpRquestResponse deleteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteControllerFile));
            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("objectId", deleteHttpObject.getRequest().getUri(),
                    controllerUuidList.get(0)));
            SendUtil.doSend(deleteRequest);

            File deleteSiteFile = new File(DELETE_SITE_PATH);
            HttpRquestResponse deleteSiteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteSiteFile));
            HttpRequest deleteSiteRequest = deleteSiteHttpObject.getRequest();
            deleteSiteRequest.setUri(PathReplace.replaceUuid("objectId", deleteSiteHttpObject.getRequest().getUri(),
                    siteUuidList.get(0)));
            SendUtil.doSend(deleteSiteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private void createControllerAndSite() throws ServiceException {
        HttpRquestResponse controllerHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(CREATE_CONTROLLER_PATH)));
        HttpResponse controllerResponse =
                HttpModelUtils.convertResponse(SendUtil.doSend(controllerHttpObject.getRequest()));
        String controllerUuid = controllerResponse.getData();
        controllerUuidList.add(controllerUuid);

        HttpRquestResponse siteHttpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(CREATE_SITE_PATH)));
        HttpResponse siteResponse = HttpModelUtils.convertResponse(SendUtil.doSend(siteHttpObject.getRequest()));
        Map<String, Map<String, String>> siteResponseMap = JsonUtil.fromJson(siteResponse.getData(), Map.class);
        String siteUuid = siteResponseMap.get("site").get("id");
        siteUuidList.add(siteUuid);
    }

    private void doTest(String uuid, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("object_id", httpObject.getRequest().getUri(), uuid));
        BrsChecker neChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, neChecker);
    }
}
