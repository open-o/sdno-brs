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
 * ITBrsLink test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsLinkTest extends TestManager {

    private static final String CREATE_Z_END_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/createportz.json";

    private static final String CREATE_Z_END_ME_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/createnez.json";

    private static final String CREATE_A_END_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/createporta.json";

    private static final String CREATE_A_END_ME_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/createnea.json";

    private static final String DELETE_PORT_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/deleteport.json";

    private static final String DELETE_NE_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/deletene.json";

    private static final String CREATE_PATH = "src/integration-test/resources/brstestcase/brsLinkTestCase/create.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsLinkTestCase/get.json";

    private static final String GET_LIST_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/getlist.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsLinkTestCase/getdelete.json";

    private static final String UPDATE_PATH = "src/integration-test/resources/brstestcase/brsLinkTestCase/update.json";

    private static final String DELETE_PATH = "src/integration-test/resources/brstestcase/brsLinkTestCase/delete.json";

    private String aEndMeUuid = null;

    private String aEndUuid = null;

    private String zEndMeUuid = null;

    private String zEndUuid = null;

    @Test
    public void linkCreateSuccessTest() {

        String uuid = null;
        try {
            setPreResource();

            HttpRquestResponse httpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(CREATE_PATH)));
            HttpRequest request = setLinkRequest(aEndMeUuid, aEndUuid, zEndMeUuid, zEndUuid, httpObject);
            BrsChecker linkChecker = new BrsChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(request, linkChecker);
            Map<String, Map<String, String>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
            uuid = responseMap.get("topologicalLink").get("id");
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteLink(uuid);
            deletePreSetResource();
        }
    }

    @Test
    public void linkGetSuccessTest() {
        String uuid = null;
        try {
            setPreResource();

            uuid = createLink(this.aEndMeUuid, this.aEndUuid, this.zEndMeUuid, this.zEndUuid, CREATE_PATH);

            doTest(uuid, new File(GET_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteLink(uuid);
            deletePreSetResource();
        }
    }

    @Test
    public void linkGetListSuccessTest() {
        String uuid = null;
        try {
            setPreResource();

            uuid = createLink(aEndMeUuid, aEndUuid, zEndMeUuid, zEndUuid, CREATE_PATH);

            doTest(uuid, new File(GET_LIST_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteLink(uuid);
            deletePreSetResource();
        }
    }

    @Test
    public void linkUpdateSuccessTest() {
        String uuid = null;
        try {
            setPreResource();

            uuid = createLink(aEndMeUuid, aEndUuid, zEndMeUuid, zEndUuid, CREATE_PATH);

            doTest(uuid, new File(UPDATE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteLink(uuid);
            deletePreSetResource();
        }
    }

    @Test
    public void linkDeleteSuccessTest() {
        String uuid = null;
        try {
            setPreResource();

            uuid = createLink(aEndMeUuid, aEndUuid, zEndMeUuid, zEndUuid, CREATE_PATH);

            doTest(uuid, new File(DELETE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deletePreSetResource();
        }
    }

    private void setPreResource() throws ServiceException {
        this.aEndMeUuid = createNe(CREATE_A_END_ME_PATH);

        this.aEndUuid = createPort(aEndMeUuid, CREATE_A_END_PATH);

        this.zEndMeUuid = createNe(CREATE_Z_END_ME_PATH);

        this.zEndUuid = createPort(aEndMeUuid, CREATE_Z_END_PATH);
    }

    private void deletePreSetResource() {
        deletePort(this.aEndUuid);
        deleteNe(this.aEndMeUuid);

        deletePort(this.zEndUuid);
        deleteNe(this.zEndMeUuid);
    }

    private String createPort(String neUuid, String path) throws ServiceException {
        File createFile = new File(path);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse = SendUtil.doSend(setPortRequest(neUuid, httpObject));
        Map<String, Map<String, String>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("logicalTerminationPoint").get("id");
    }

    private void deletePort(String uuid) {
        try {
            File deletePortFile = new File(DELETE_PORT_PATH);
            HttpRquestResponse deleteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deletePortFile));
            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("objectId", deleteHttpObject.getRequest().getUri(), uuid));
            SendUtil.doSend(deleteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private HttpRequest setPortRequest(String neUuid, HttpRquestResponse httpObject) {
        HttpRequest request = httpObject.getRequest();
        Map<String, Map<String, Object>> requestMap = JsonUtil.fromJson(request.getData(), Map.class);
        Map<String, Object> requestParamMap = requestMap.get("logicalTerminationPoint");
        requestParamMap.put("meID", neUuid);
        requestMap.remove("logicalTerminationPoint");
        requestMap.put("logicalTerminationPoint", requestParamMap);
        request.setData(JsonUtil.toJson(requestMap));
        return request;
    }

    private String createNe(String path) throws ServiceException {
        File createNeFile = new File(path);
        String contentNe = FileUtils.readFromJson(createNeFile);
        HttpRquestResponse neHttpObject = HttpModelUtils.praseHttpRquestResponse(contentNe);
        HttpResponse neResponse = HttpModelUtils.convertResponse(SendUtil.doSend(neHttpObject.getRequest()));
        Map<String, Map<String, String>> neResponseMap = JsonUtil.fromJson(neResponse.getData(), Map.class);
        return neResponseMap.get("managedElement").get("id");
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

    private String createLink(String aEndMeUuid, String aEndUuid, String zEndMeUuid, String zEndUuid, String path)
            throws ServiceException {
        File createFile = new File(path);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse =
                SendUtil.doSend(setLinkRequest(aEndMeUuid, aEndUuid, zEndMeUuid, zEndUuid, httpObject));
        Map<String, Map<String, String>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("topologicalLink").get("id");
    }

    private void deleteLink(String uuid) {
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

    private HttpRequest setLinkRequest(String aEndMeUuid, String aEndUuid, String zEndMeUuid, String zEndUuid,
            HttpRquestResponse httpObject) {
        HttpRequest request = httpObject.getRequest();
        Map<String, Map<String, Object>> requestMap = JsonUtil.fromJson(request.getData(), Map.class);
        Map<String, Object> requestParamMap = requestMap.get("topologicalLink");
        requestParamMap.put("aEnd", aEndUuid);
        requestParamMap.put("zEnd", zEndUuid);
        requestParamMap.put("aEndME", aEndMeUuid);
        requestParamMap.put("zEndME", zEndMeUuid);
        requestMap.remove("topologicalLink");
        requestMap.put("topologicalLink", requestParamMap);
        request.setData(JsonUtil.toJson(requestMap));
        return request;
    }

    private void doTest(String uuid, File testFile) throws ServiceException {

        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
        BrsChecker linkChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, linkChecker);
    }

}
