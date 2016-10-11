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

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
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
 * ITBrsController test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsControllerTest extends TestManager {

    private static final String CREATE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/create.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsControllerTestCase/get.json";

    private static final String UPDATE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/update.json";

    private static final String DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/delete.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/getdelete.json";

    @Test
    public void controllerCreateSuccessTest() {
        String uuid = null;
        try {
            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            BrsChecker controllerChecker = new BrsChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(httpObject.getRequest(), controllerChecker);
            uuid = createResponse.getData();

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteController(uuid);
        }
    }

    @Test
    public void controllerUpdateSuccessTest() {
        String uuid = null;
        try {
            uuid = createController();

            doTest(uuid, new File(UPDATE_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteController(uuid);
        }
    }

    @Test
    public void controllerGetSuccessTest() {
        String uuid = null;
        try {
            uuid = createController();

            doTest(uuid, new File(GET_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteController(uuid);
        }
    }

    @Test
    public void controllerDeleteSuccessTest() {
        try {
            String uuid = createController();

            doTest(uuid, new File(DELETE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private String createController() throws ServiceException {
        File createFile = new File(CREATE_PATH);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse = SendUtil.doSend(httpObject.getRequest());
        return createResponse.getResponseContent();
    }

    private void doTest(String uuid, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
        BrsChecker controllerChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, controllerChecker);
    }

    private void deleteController(String uuid) {
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
