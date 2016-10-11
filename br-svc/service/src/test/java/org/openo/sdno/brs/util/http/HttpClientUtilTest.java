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

package org.openo.sdno.brs.util.http;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;

import mockit.Mock;
import mockit.MockUp;

/**
 * HttpClientUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class HttpClientUtilTest {

    private String URL = "/rest/test/v1/ipsec";

    private String HEAD_ACCEPT = "accept";

    private String HEAD_CONTENT_TYPE = "content-type";

    private String ACCEPT_VALUE = "application/json";

    private String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    @Test
    public void testGet() throws ServiceException {
        new MockUp<RestfulProxy>() {

            @Mock
            public RestfulResponse get(String uri, RestfulParametes restParametes) throws ServiceException {
                if(ACCEPT_VALUE.equals(restParametes.get(HEAD_ACCEPT))
                        && CONTENT_TYPE_VALUE.equals(restParametes.get(HEAD_CONTENT_TYPE))) {
                    RestfulResponse response = new RestfulResponse();
                    response.setStatus(HttpStatus.SC_OK);
                    return response;
                }

                return null;
            }
        };

        Map<String, String> httpHeaders = new HashMap<String, String>();
        httpHeaders.put(HEAD_ACCEPT, ACCEPT_VALUE);
        httpHeaders.put(HEAD_CONTENT_TYPE, CONTENT_TYPE_VALUE);
        RestfulResponse response = HttpClientUtil.get(URL, httpHeaders);
        assertTrue(response != null);

        response = HttpClientUtil.get(URL, new HashMap<String, String>());
        assertTrue(response == null);
    }

    @Test
    public void testPost() throws ServiceException {
        new MockUp<RestfulProxy>() {

            @Mock
            public RestfulResponse post(String uri, RestfulParametes restParametes) {
                if(restParametes.getRawData() != null) {
                    RestfulResponse response = new RestfulResponse();
                    response.setStatus(HttpStatus.SC_OK);
                    return response;
                }

                return null;
            }
        };

        RestfulResponse response = HttpClientUtil.post(URL, null);
        assertTrue(response == null);

        response = HttpClientUtil.post(URL, "{\"key1\":\"value1\"}");
        assertTrue(response != null);
    }

    @Test
    public void testPut() throws ServiceException {
        new MockUp<RestfulProxy>() {

            @Mock
            public RestfulResponse put(String uri, RestfulParametes restParametes) throws ServiceException {
                if(restParametes.getRawData() != null) {
                    RestfulResponse response = new RestfulResponse();
                    response.setStatus(HttpStatus.SC_OK);
                    return response;
                }

                return null;
            }
        };

        RestfulResponse response = HttpClientUtil.put(URL, null);
        assertTrue(response == null);

        response = HttpClientUtil.put(URL, "{\"key1\":\"value1\"}");
        assertTrue(response != null);
    }

}
