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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
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
 * Send utility test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsCommonParamTest extends TestManager {

    private static final String CREATE_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/create.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/get.json";

    private static final String GET2_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/get2.json";

    private static final String UPDATE_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/update.json";

    private static final String DELETE_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/delete.json";

    private static final String DELETE2_PATH = "src/integration-test/resources/brstestcase/brsCommonParam/delete2.json";

    private static final String CONTROLLER_CREATE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/create.json";

    private static final String CONTROLLER_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsControllerTestCase/delete.json";

    private String controlleruuid = "";

    @Before
    public void createController() {

        try {
            File createFile = new File(CONTROLLER_CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            BrsChecker commParamChecker = new BrsChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(httpObject.getRequest(), commParamChecker);
            controlleruuid = createResponse.getData();

            if(controlleruuid.isEmpty()) {
                assertFalse(true); // Initialization or creating controller is failed
            }
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    // Execute all success test cases
    @Test
    public void commParamSuccessTest() {

        try {

            // 1: Create Common Parameter entry for the controller
            HttpResponse createResponse = doTest(controlleruuid, null, new File(CREATE_PATH));
            String paramId = createResponse.getData();

            // 2: Update Common Parameter entry for the controller
            doTest(controlleruuid, paramId, new File(UPDATE_PATH));

            // 3: Get Common Parameter entry for the controller and paramId
            doTest(controlleruuid, paramId, new File(GET2_PATH));
            doTest(controlleruuid, null, new File(GET_PATH));

            // 4: Delete Common Parameter entry using controller and paramId
            doTest(controlleruuid, paramId, new File(DELETE2_PATH));

            createResponse = doTest(controlleruuid, null, new File(CREATE_PATH));
            paramId = createResponse.getData();

            // 5: Delete Common Parameter entry using only controllerID
            doTest(controlleruuid, null, new File(DELETE_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    // Execute all failure test cases
    @Test
    public void commParamFailureTest() {

        File folder = new File("src/integration-test/resources/brstestcase/brsCommonParam");
        File[] listOfFiles = folder.listFiles();

        for(File file : listOfFiles) {

            if(file.isFile() && file.getName().contains("failure")) {

                sendFailureTest(file, controlleruuid, null);

            }
        }

    }

    // Do Success Test
    private HttpResponse doTest(String uuid, String paramId, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));

        if(null != paramId) {
            request.setUri(PathReplace.replaceUuid("paramId", httpObject.getRequest().getUri(), paramId));
        }
        BrsChecker commParamChecker = new BrsChecker(httpObject.getResponse());
        return execTestCase(request, commParamChecker);
    }

    // Do Failure Test
    private void sendFailureTest(File file, String uuid, String paramId) {
        try {

            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(file));
            HttpRequest request = httpObject.getRequest();
            request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
            request.setUri(PathReplace.replaceUuid("paramId", httpObject.getRequest().getUri(), paramId));
            SendUtil.doSend(request);
            assertFalse(true);
        } catch(ServiceException e) {
            // this function is used for sending failure cases, exception should be thrown
        }
    }

    @After
    public void deleteController() {
        try {
            doTest(controlleruuid, null, new File(CONTROLLER_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        }

    }

}
