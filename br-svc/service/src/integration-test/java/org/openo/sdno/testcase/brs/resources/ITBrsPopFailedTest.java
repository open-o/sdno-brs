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

import java.io.File;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.testcase.brs.checker.BrsChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;
import org.openo.sdno.testframework.util.file.FileUtils;

public class ITBrsPopFailedTest extends TestManager {

    private static final String UPDATE_UNEXIST_PATH =
            "src/integration-test/resources/brstestcase/brsPopFailedTestCase/updateunexist.json";

    private static final String DELETE_FORMATWRONG_PATH =
            "src/integration-test/resources/brstestcase/brsPopFailedTestCase/deleteformatwrong.json";

    @Test
    public void updateUnExistTest() throws ServiceException {
        HttpRquestResponse httpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(UPDATE_UNEXIST_PATH)));
        HttpRequest request = httpObject.getRequest();
        BrsChecker popChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, popChecker);
    }

    @Test
    public void deleteFormatWrongTest() throws ServiceException {
        HttpRquestResponse httpObject =
                HttpModelUtils.praseHttpRquestResponse(FileUtils.readFromJson(new File(DELETE_FORMATWRONG_PATH)));
        HttpRequest request = httpObject.getRequest();
        BrsChecker popChecker = new BrsChecker(httpObject.getResponse());
        execTestCase(request, popChecker);
    }

}
