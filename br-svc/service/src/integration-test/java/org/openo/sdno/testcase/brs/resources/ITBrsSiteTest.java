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
 * ITBrsSite test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsSiteTest extends TestManager {

    private static final String CREATE_PATH = "src/integration-test/resources/brstestcase/brsSiteTestCase/create.json";

    private static final String GET_PATH = "src/integration-test/resources/brstestcase/brsSiteTestCase/get.json";

    private static final String GET_LIST_PATH =
            "src/integration-test/resources/brstestcase/brsSiteTestCase/getlist.json";

    private static final String UPDATE_PATH = "src/integration-test/resources/brstestcase/brsSiteTestCase/update.json";

    private static final String DELETE_PATH = "src/integration-test/resources/brstestcase/brsSiteTestCase/delete.json";

    private static final String GET_DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsSiteTestCase/getdelete.json";

    @Test
    public void siteCreateSuccessTest() {
        String uuid = null;
        try {
            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            BrsChecker siteChecker = new BrsChecker(httpObject.getResponse());

            HttpResponse createResponse = execTestCase(httpObject.getRequest(), siteChecker);
            Map<String, Map<String, String>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
            uuid = responseMap.get("site").get("id");
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteSite(uuid);
        }
    }

    @Test
    public void siteUpdateSuccessTest() {
        String uuid = null;
        try {
            uuid = createSite();

            doTest(uuid, new File(UPDATE_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteSite(uuid);
        }
    }

    @Test
    public void siteGetSuccessTest() {
        String uuid = null;
        try {
            uuid = createSite();

            doTest(uuid, new File(GET_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteSite(uuid);
        }
    }

    @Test
    public void siteGetListSuccessTest() {
        String uuid = null;
        try {
            uuid = createSite();

            doTest(uuid, new File(GET_LIST_PATH));

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteSite(uuid);
        }
    }

    @Test
    public void siteDeleteSuccessTest() {
        try {
            String uuid = createSite();

            doTest(uuid, new File(DELETE_PATH));

            doTest(uuid, new File(GET_DELETE_PATH));
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private String createSite() throws ServiceException {
        File createFile = new File(CREATE_PATH);
        String content = FileUtils.readFromJson(createFile);
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
        RestfulResponse createResponse = SendUtil.doSend(httpObject.getRequest());
        Map<String, Map<String, String>> responseMap =
                JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
        return responseMap.get("site").get("id");
    }

    private void deleteSite(String uuid) {
        try {
            File deleteSiteFile = new File(DELETE_PATH);
            HttpRquestResponse deleteHttpObject =
                    HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(deleteSiteFile));
            HttpRequest deleteRequest = deleteHttpObject.getRequest();
            deleteRequest.setUri(PathReplace.replaceUuid("objectId", deleteHttpObject.getRequest().getUri(), uuid));
            SendUtil.doSend(deleteRequest);
        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    private void doTest(String uuid, File testFile) throws ServiceException {
        HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(testFile));
        HttpRequest request = httpObject.getRequest();
        request.setUri(PathReplace.replaceUuid("objectId", httpObject.getRequest().getUri(), uuid));
        BrsChecker siteChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, siteChecker);
    }
}
