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

package org.openo.sdno.brs.restrepository.impl;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.util.http.HttpClientUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * MSSProxyImpl test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class MSSProxyImplTest {

    MSSProxyImpl mssProxyImpl = new MSSProxyImpl();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetRelations1() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.getRelations("", "", "", "111", "");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetRelations2() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.getRelations("", "", "", "", "");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetRelations3() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.getRelations("", "", "", null, "");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetRelations4() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.getRelations("", "", "", "", "111");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetRelations5() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.getRelations("", "", "", "", null);
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteRelation1() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse delete(final String url) throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.deleteRelation("", "", null, "", "");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteRelation2() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse delete(final String url) throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.deleteRelation("", "", "2222", "", "");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testDeleteRelation3() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse delete(final String url) throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.deleteRelation("", "", "", null, "");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteRelation4() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse delete(final String url) throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.deleteRelation("", "", "", "2222", "");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetResourceListInfo1() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.getResourceListInfo("", "", "fields", "filter", 1, 20);
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetResourceListInfo2() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.getResourceListInfo("", "", null, null, 0, 20);
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCommonQueryCount1() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                return new RestfulResponse();
            }
        };

        try {
            mssProxyImpl.commonQueryCount("", "", "joinAttr", "filter");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCommonQueryCount2() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.commonQueryCount("", "", "", "");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCommonQueryCount3() {
        new MockUp<HttpClientUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders)
                    throws ServiceException {
                throw new ServiceException();
            }
        };

        try {
            mssProxyImpl.commonQueryCount("", "", null, null);
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

}
