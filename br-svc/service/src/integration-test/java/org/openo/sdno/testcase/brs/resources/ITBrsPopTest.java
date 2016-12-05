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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.PopMO;
import org.openo.sdno.exception.HttpCode;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.replace.PathReplace;
import org.openo.sdno.testframework.testmanager.TestManager;

public class ITBrsPopTest extends TestManager {

    private static final String CREATE_TESTCASE_PATH =
            "src/integration-test/resources/brstestcase/brsPopTestCase/create.json";

    private static final String DELETE_TESTCASE_PATH =
            "src/integration-test/resources/brstestcase/brsPopTestCase/delete.json";

    private static final String UPDATE_TESTCASE_PATH =
            "src/integration-test/resources/brstestcase/brsPopTestCase/update.json";

    private static final String QUERY_TESTCASE_PATH =
            "src/integration-test/resources/brstestcase/brsPopTestCase/query.json";

    @Test
    public void popSuccessTest() throws ServiceException {

        HttpResponse createResponse = execTestCase(new File(CREATE_TESTCASE_PATH), new StatusSuccessCheck());
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> responseMap = JsonUtil.fromJson(createResponse.getData(), Map.class);
        assertTrue(null != responseMap.get(Constant.POP_KEY));
        String uuid = responseMap.get(Constant.POP_KEY).get("id");
        assertTrue(!StringUtils.isEmpty(uuid));

        HttpRquestResponse httpQueryObject = HttpModelUtils.praseHttpRquestResponseFromFile(QUERY_TESTCASE_PATH);
        HttpRequest queryRequest = httpQueryObject.getRequest();
        queryRequest.setUri(PathReplace.replaceUuid("object_id", queryRequest.getUri(), uuid));
        execTestCase(queryRequest, new StatusSuccessCheck());

        HttpRquestResponse httpUpdateObject = HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_TESTCASE_PATH);
        HttpRequest updateRequest = httpUpdateObject.getRequest();
        updateRequest.setUri(PathReplace.replaceUuid("object_id", updateRequest.getUri(), uuid));
        execTestCase(updateRequest, new StatusSuccessCheck());

        HttpResponse queryUpdateResponse = execTestCase(queryRequest, new StatusSuccessCheck());
        Map<String, PopMO> updateResultMap =
                JsonUtil.fromJson(queryUpdateResponse.getData(), new TypeReference<Map<String, PopMO>>() {});
        PopMO queryPop = updateResultMap.get(Constant.POP_KEY);
        assertTrue(null != queryPop);
        assertTrue("description1".equals(queryPop.getDescription()));
        assertTrue("pop1".equals(queryPop.getName()));

        HttpRquestResponse httpDeleteObject = HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_TESTCASE_PATH);
        HttpRequest deleteRequest = httpDeleteObject.getRequest();
        deleteRequest.setUri(PathReplace.replaceUuid("object_id", deleteRequest.getUri(), uuid));
        execTestCase(deleteRequest, httpDeleteObject.getResponse());

        HttpResponse queryDeleteResponse = execTestCase(queryRequest, new StatusSuccessCheck());
        Map<String, Map<String, Object>> queryDeleteResultMap = JsonUtil.fromJson(queryDeleteResponse.getData(),
                new TypeReference<Map<String, Map<String, Object>>>() {});
        assertTrue(queryDeleteResultMap.get(Constant.POP_KEY).isEmpty());
    }

    private class StatusSuccessCheck implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            return HttpCode.isSucess(response.getStatus());
        }

    }

}
