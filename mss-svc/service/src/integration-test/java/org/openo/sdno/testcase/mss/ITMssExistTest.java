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
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

/**
 * ITMssExist test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ITMssExistTest extends TestManager {

    private static final String CREATE_EXIST_PATH =
            "src/integration-test/resources/msstestcase/mssExistTestCase/exist.json";

    private static final String CREATE_UNEXIST_PATH =
            "src/integration-test/resources/msstestcase/mssExistTestCase/unexist.json";

    private static final String CREATE_RESOURCE_PATH =
            "src/integration-test/resources/msstestcase/mssExistTestCase/createresource.json";

    private static final String DELETE_RESOURCE_PATH =
            "src/integration-test/resources/msstestcase/mssExistTestCase/deleteresource.json";

    @Test
    public void resourceExistTest() throws ServiceException {
        String uuid = null;
        try {
            File createFile = new File(CREATE_RESOURCE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
            request.setUri(
                    PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));

            RestfulResponse createResponse = SendUtil.doSend(request);
            Map<String, List<Map<String, String>>> responseMap =
                    JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
            uuid = responseMap.get("objects").get(0).get("id");

            File checkFile = new File(CREATE_EXIST_PATH);
            doTest(checkFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    @Test
    public void resourceUnexistTest() {
        String uuid = null;
        try {
            File createFile = new File(CREATE_RESOURCE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
            request.setUri(
                    PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));

            RestfulResponse createResponse = SendUtil.doSend(request);
            Map<String, List<Map<String, String>>> responseMap =
                    JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
            uuid = responseMap.get("objects").get(0).get("id");

            File checkFile = new File(CREATE_UNEXIST_PATH);
            doTest(checkFile);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteResource(uuid);
        }
    }

    private void doTest(File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("bucket-name", httpObject.getRequest().getUri(), "brsdb"));
        request.setUri(PathReplace.replaceUuid("resource-type-name", httpObject.getRequest().getUri(), "controller"));
        MssChecker resourceChecker = new MssChecker(httpObject.getResponse());
        resourceChecker.setExpectedResponse(httpObject.getResponse());
        execTestCase(request, resourceChecker);
    }

    private void deleteResource(String uuid) {
        try {
            File deleteControllerFile = new File(DELETE_RESOURCE_PATH);
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
