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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testcase.mss.checker.MssChecker;
import org.openo.sdno.testcase.mss.checker.Relation;
import org.openo.sdno.testcase.mss.checker.SendUtil;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

/**
 * ITMssRelation test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ITMssRelationTest extends TestManager {

    private static final String CREATE_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/create.json";

    private static final String CREATE_FAILED_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/createfailed.json";

    private static final String CREATE_EMPTY_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/creatempty.json";

    private static final String GET_PATH = "src/integration-test/resources/msstestcase/mssRelationTestCase/get.json";

    private static final String DELETE_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/delete.json";

    private static final String DELETE_FAILED_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/deletefailed.json";

    private static final String CREATE_NE_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/createne.json";

    private static final String DELETE_NE_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/deletene.json";

    private static final String CREATE_NE_RELATION_PATH =
            "src/integration-test/resources/msstestcase/mssRelationTestCase/createnerelation.json";

    @Test
    public void relationCreateSuccessTest() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_PATH);

            File createFile = new File(CREATE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String requestJson = setRequest("testdomainID", neUuid);
            request.setData(requestJson);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationCreateFailedTest1() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_PATH);

            File createFile = new File(CREATE_FAILED_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String requestJson = setRequest("testdomainID", neUuid);
            request.setData(requestJson);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationCreateFailedTest2() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_PATH);

            File createFile = new File(CREATE_EMPTY_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);
        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationGetSuccessTest() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_RELATION_PATH);

            File createFile = new File(GET_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String url = request.getUri() + "dst_type=" + "managementdomain&" + "src_ids=" + neUuid + "&dst_ids="
                    + "testdomainID";
            request.setUri(url);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationGetEmptyTest() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_RELATION_PATH);

            File createFile = new File(GET_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String url = request.getUri() + "dst_type=" + "managementdomain";
            request.setUri(url);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationDeleteSuccessTest() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_RELATION_PATH);

            File createFile = new File(DELETE_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String url = request.getUri() + "dst_type=" + "managementdomain&" + "src_id=" + neUuid + "&dst_id="
                    + "testdomainID&" + "relationship_name=association";
            request.setUri(url);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    @Test
    public void relationDeleteFailedTest() {
        String neUuid = null;
        try {
            neUuid = createNe(CREATE_NE_RELATION_PATH);

            File createFile = new File(DELETE_FAILED_PATH);
            String content = FileUtils.readFromJson(createFile);
            HttpRquestResponse httpObject = HttpModelUtils.praseHttpRquestResponse(content);
            HttpRequest request = httpObject.getRequest();
            String url = request.getUri() + "src_id=" + neUuid + "&relationship_name=association";
            request.setUri(url);
            MssChecker relationChecker = new MssChecker(httpObject.getResponse());

            execTestCase(request, relationChecker);

        } catch(ServiceException e) {
            assertFalse(true);
        } finally {
            deleteNe(neUuid, DELETE_NE_PATH);
        }
    }

    private void deleteNe(String neUuid, String path) {
        try {
            File deleteNeFile = new File(path);
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

    private String createNe(String path) throws ServiceException {
        File createNeFile = new File(path);
        String contentNe = FileUtils.readFromJson(createNeFile);
        HttpRquestResponse neHttpObject = HttpModelUtils.praseHttpRquestResponse(contentNe);
        HttpResponse neResponse = HttpModelUtils.convertResponse(SendUtil.doSend(neHttpObject.getRequest()));
        Map<String, Map<String, String>> neResponseMap = JsonUtil.fromJson(neResponse.getData(), Map.class);
        return neResponseMap.get("managedElement").get("id");
    }

    private String setRequest(String dstId, String neUuid) throws ServiceException {
        List<Relation> lstRelation = new ArrayList<Relation>();
        Relation relation = new Relation();
        relation.setSrcId(neUuid);
        relation.setDstId(dstId);
        relation.setDstType("managementdomain");
        relation.setRelation("association");
        lstRelation.add(relation);
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("relationships", lstRelation);
        return JsonUtil.toJson(requestMap);
    }
}
