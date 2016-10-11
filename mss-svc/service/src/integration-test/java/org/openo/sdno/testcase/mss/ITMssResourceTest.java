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

package org.openo.sdno.testcase.mss;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testcase.mss.checker.MssChecker;
import org.openo.sdno.testcase.mss.checker.SendUtil;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

/**
 * ITMssResource test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ITMssResourceTest extends TestManager {

    private static final String CREATE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/create.json";

    private static final String BATCH_UPDATE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/batchupdate.json";

    private static final String GET_PATH = "src/integration-test/resources/msstestcase/mssResourceTestCase/get.json";

    private static final String BATCH_GET_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/batchget.json";

    private static final String GET_RELATION_DATA_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/getrelationdata.json";

    private static final String UPDATE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/update.json";

    private static final String DELETE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/delete.json";

    private static final String BATCH_DELETE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/batchdelete.json";

    private static final String DELETE_RESOURCE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/deleteresource.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/msstestcase/mssResourceTestCase/getdelete.json";

    @Test
    public void resourceCreateSuccessTest() {
        String uuid = null;
        try {
            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
            request.setUri(
                    PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));
            MssChecker resourceChecker = new MssChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(request, resourceChecker);
            Map<String, List<Map<String, String>>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
            uuid = responseMap.get("objects").get(0).get("id");

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceUpdateSuccessTest() {
        String uuid = null;
        try {
            uuid = createResource();

            doTest(uuid, new File(UPDATE_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceBatchUpdateSuccessTest() {
        String uuid = null;
        try {
            uuid = createResource();

            doTest(null, new File(BATCH_UPDATE_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceGetSuccessTest() {
        String uuid = null;
        try {
            uuid = createResource();

            doTest(uuid, new File(GET_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceBatchGetSuccessTest() {
        String uuid = null;
        try {
            uuid = createResource();

            doTest(uuid, new File(BATCH_GET_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceGetRelationDataSuccessTest() {
        String uuid = null;
        try {
            uuid = createResource();

            doTest(uuid, new File(GET_RELATION_DATA_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceDeleteSuccessTest() {
        try {
            String uuid = createResource();

            doTest(uuid, new File(DELETE_RESOURCE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    @Test
    public void resourceBatchDeleteSuccessTest() {
        try {
            String uuid = createResource();

            doTest(uuid, new File(BATCH_DELETE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private void doTest(String uuid, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
        request.setUri(PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
        MssChecker controllerChecker = new MssChecker(httpObject.getResponse());
        execTestCase(request, controllerChecker);
    }

    private String createResource() throws ServiceException {
        File createFile = new File(CREATE_PATH);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
        request.setUri(PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));
        RestfulResponse createResponse = SendUtil.doSend(request);
        Map<String, List<Map<String, String>>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("objects").get(0).get("id");
    }

    private void deleteResource(String uuid) {
        try {
            File deleteControllerFile = new File(DELETE_PATH);
            HttpRquestResponse deleteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteControllerFile));
            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("objectId", deleteHttpObject.getRequest().getUri(), uuid));

            SendUtil.doSend(deleteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

}
