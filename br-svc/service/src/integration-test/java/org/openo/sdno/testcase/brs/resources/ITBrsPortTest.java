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
 * ITBrsPort test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsPortTest extends TestManager {

    private static final String CREATE_PATH = "src/integration-test/resources/brstestcase/brsPortTestCase/create.json";

    private static final String CREATE_NE_PATH =
            "src/integration-test/resources/brstestcase/brsPortTestCase/createne.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsPortTestCase/get.json";

    private static final String GET_LIST_PATH =
            "src/integration-test/resources/brstestcase/brsPortTestCase/getlist.json";

    private static final String GET_EMPTY_LIST_PATH =
            "src/integration-test/resources/brstestcase/brsPortTestCase/getlistempty.json";

    private static final String UPDATE_PATH = "src/integration-test/resources/brstestcase/brsPortTestCase/update.json";

    private static final String DELETE_PATH = "src/integration-test/resources/brstestcase/brsPortTestCase/delete.json";

    private static final String DELETE_NE_PATH =
            "src/integration-test/resources/brstestcase/brsPortTestCase/deletene.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsPortTestCase/getdelete.json";

    @Test
    public void portCreateSuccessTest() {
        String neUuid = null;
        String uuid = null;
        try {
            neUuid = createNe();

            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);

            HttpRequest request = setRequest(neUuid, httpObject);

            BrsChecker siteChecker = new BrsChecker(httpObject.getResponse());
            HttpResponse createResponse = execTestCase(request, siteChecker);
            Map<String, Map<String, String>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
            uuid = responseMap.get("logicalTerminationPoint").get("id");

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePort(uuid);
            deleteNe(neUuid);
        }
    }

    @Test
    public void portUpdateSuccessTest() {
        String neUuid = null;
        String uuid = null;
        try {
            neUuid = createNe();

            uuid = createPort(neUuid);

            doTest(uuid, new File(UPDATE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePort(uuid);
            deleteNe(neUuid);
        }
    }

    @Test
    public void portGetSuccessTest() {
        String neUuid = null;
        String uuid = null;
        try {
            neUuid = createNe();

            uuid = createPort(neUuid);

            doTest(uuid, new File(GET_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePort(uuid);
            deleteNe(neUuid);
        }
    }

    @Test
    public void portGetListSuccessTest() {
        String neUuid = null;
        String uuid = null;
        try {
            neUuid = createNe();

            uuid = createPort(neUuid);

            doTest(uuid, new File(GET_LIST_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePort(uuid);
            deleteNe(neUuid);
        }
    }

    @Test
    public void portGetListwithParamEmptySuccessTest() {
        String neUuid = null;
        String uuid = null;
        try {
            neUuid = createNe();

            uuid = createPort(neUuid);

            doTest(uuid, new File(GET_EMPTY_LIST_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePort(uuid);
            deleteNe(neUuid);
        }
    }

    @Test
    public void portDeleteSuccessTest() {
        String neUuid = null;
        try {
            neUuid = createNe();

            String uuid = createPort(neUuid);

            doTest(uuid, new File(DELETE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid);
        }
    }

    private HttpRequest setRequest(String neUuid, HttpRquestResponse httpObject) {
        HttpRequest request = httpObject.getRequest();
        Map<String, Map<String, Object>> requestMap = JsonUtil.fromJson(request.getData(), Map.class);
        Map<String, Object> requestParamMap = requestMap.get("logicalTerminationPoint");
        requestParamMap.put("meID", neUuid);
        requestMap.remove("logicalTerminationPoint");
        requestMap.put("logicalTerminationPoint", requestParamMap);
        request.setData(JsonUtil.toJson(requestMap));
        return request;
    }

    private String createPort(String neUuid) throws ServiceException {
        File createFile = new File(CREATE_PATH);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse = SendUtil.doSend(setRequest(neUuid, httpObject));
        Map<String, Map<String, String>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("logicalTerminationPoint").get("id");
    }

    private void deletePort(String uuid) {
        try {
            File deletePortFile = new File(DELETE_PATH);
            HttpRquestResponse deleteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deletePortFile));
            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("objectId", deleteHttpObject.getRequest().getUri(), uuid));
            SendUtil.doSend(deleteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private void deleteNe(String neUuid) {
        try {
            File deleteNeFile = new File(DELETE_NE_PATH);
            HttpRquestResponse deleteNeHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteNeFile));
            HttpRequest deleteNeRequest = deleteNeHttpObject.getRequest();
            deleteNeRequest
                    .setUri(PathReplace.replaceUuid("object_id", deleteNeHttpObject.getRequest().getUri(), neUuid));
            SendUtil.doSend(deleteNeRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private String createNe() throws ServiceException {
        File createNeFile = new File(CREATE_NE_PATH);
        String contentNe = FileUtils.readFromJson(createNeFile);
        HttpRquestResponse neHttpObject = HttpModelUtils.praseHttpRquestResponse(contentNe);
        HttpResponse neResponse = HttpModelUtils.convertResponse(SendUtil.doSend(neHttpObject.getRequest()));
        Map<String, Map<String, String>> neResponseMap = JsonUtil.fromJson(neResponse.getData(), Map.class);
        return neResponseMap.get("managedElement").get("id");
    }

    private void doTest(String uuid, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
        BrsChecker siteChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, siteChecker);
    }
}
