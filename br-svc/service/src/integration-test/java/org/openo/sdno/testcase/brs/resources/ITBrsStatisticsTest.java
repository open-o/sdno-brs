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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testcase.brs.checker.SendUtil;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

/**
 * ITBrsStatistics test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ITBrsStatisticsTest extends TestManager {

    private static final String CREATE_PATH =
            "src/integration-test/resources/brstestcase/brsStatisticsTestCase/create.json";

    private static final String DELETE_PATH =
            "src/integration-test/resources/brstestcase/brsStatisticsTestCase/delete.json";

    private static final String COUNT_RESOURCE =
            "src/integration-test/resources/brstestcase/brsStatisticsTestCase/count.json";

    private static final String COUNT_EMPTY_RESOURCE =
            "src/integration-test/resources/brstestcase/brsStatisticsTestCase/countempty.json";

    private static final String COUNT_DEFAULT_RESOURCE =
            "src/integration-test/resources/brstestcase/brsStatisticsTestCase/countdefault.json";

    @Test
    public void countGetSuccessTest() {
        String uuid = null;
        try {
            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            RestfulResponse createResponse = SendUtil.doSend(httpObject.getRequest());
            Map<String, Map<String, String>> responseMap =
                    JsonUtil.fromJson(createResponse.getResponseContent(), Map.class);
            uuid = responseMap.get("site").get("id");

            File countFile = new File(COUNT_RESOURCE);
            String countContent = FileUtils.readFromJson(countFile);
            HttpRquestResponse countHttpObject = HttpModelUtils.praseHttpRquestResponse(countContent);
            RestfulResponse countResponse = SendUtil.doSend(countHttpObject.getRequest());

            assertEquals(200, countResponse.getStatus());
            assertEquals("1", countResponse.getResponseContent());

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteSite(uuid);
        }
    }

    @Test
    public void countGetEmptyTest() {
        try {
            File countFile = new File(COUNT_EMPTY_RESOURCE);
            String countContent = FileUtils.readFromJson(countFile);
            HttpRquestResponse countHttpObject = HttpModelUtils.praseHttpRquestResponse(countContent);
            RestfulResponse countResponse = SendUtil.doSend(countHttpObject.getRequest());

            assertEquals(200, countResponse.getStatus());
            assertEquals("0", countResponse.getResponseContent());

        } catch(ServiceException e) {
            assertFalse(true);
        }
    }

    @Test
    public void countGetFailedTest() {
        try {
            File countFile = new File(COUNT_DEFAULT_RESOURCE);
            String countContent = FileUtils.readFromJson(countFile);
            HttpRquestResponse countHttpObject = HttpModelUtils.praseHttpRquestResponse(countContent);
            RestfulResponse countResponse = SendUtil.doSend(countHttpObject.getRequest());

            assertEquals(500, countResponse.getStatus());

        } catch(ServiceException e) {
            assertFalse(true);
        }
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
}
