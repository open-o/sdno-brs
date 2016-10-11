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

package org.openo.sdno.mss.model.util;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

/**
 * PropertiesUtil test class. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class PropertiesUtilTest {

    private static final String MYBAITS_PROPERTIES_PATH = "/META-INF/conf/bucket.properties";

    private String appRoot = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "bucket" + File.separator + "cofig" + File.separator;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetInfoModelPrefix() {
        assertTrue("im".equals(PropertiesUtil.getInstance().getINFOMODELPREFIX()));
    }

    @Test
    public void testGetDataModelPrefix() {
        assertTrue("dm".equals(PropertiesUtil.getInstance().getDATAMODELPREFIX()));
    }

    @Test
    public void testGetRelationModelPrefix() {
        assertTrue("rm".equals(PropertiesUtil.getInstance().getRELAMODELPREFIX()));
    }

    @Test
    public void testGetProperties() {

        String cfgFileName = appRoot + "test.txt";

        assertTrue(PropertiesUtil.getProperties(cfgFileName).size() != 0);
    }

    @Test
    public void testGetPropertiesFileNotFoundException() {

        String cfgFileName = appRoot + "test1.txt";

        assertTrue((0 == PropertiesUtil.getProperties(cfgFileName).size()));
    }

    @Test
    public void testGetPropertiesIoException() {
        new MockUp<Properties>() {

            @Mock
            public synchronized void load(InputStream inStream) throws IOException {
                throw new IOException();
            }
        };

        String cfgFileName = appRoot + "test.txt";

        assertTrue((0 == PropertiesUtil.getProperties(cfgFileName).size()));
    }

    @Test
    public void testGetPropertiesCloseException() {
        new MockUp<FileInputStream>() {

            @Mock
            public void close() throws IOException {
                throw new IOException("");
            }
        };

        String cfgFileName = appRoot + "test.txt";

        PropertiesUtil.getProperties(cfgFileName);
    }

    @Test
    public void testGetPropertiesException() {
        URL configUrl = null;

        assertTrue(null == PropertiesUtil.getProperties(configUrl));
    }

    @Test
    public void testGetPropertiesIoException2() {
        new MockUp<URL>() {

            @Mock
            public final InputStream openStream() throws java.io.IOException {
                throw new IOException();
            }
        };

        URL url = getClass().getResource(MYBAITS_PROPERTIES_PATH);

        try {
            PropertiesUtil.getProperties(url);
            assertTrue(false);
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetPropertiesCloseException2() {
        new MockUp<BufferedInputStream>() {

            @Mock
            public void close() throws IOException {
                throw new IOException();
            }
        };

        URL url = getClass().getResource(MYBAITS_PROPERTIES_PATH);

        PropertiesUtil.getProperties(url);
    }

    @Test
    public void testGetUrl() {
        String cfgFileName = appRoot + "test.txt";

        PropertiesUtil.getURL(cfgFileName);
    }

    @Test
    public void testGetUrlException() {
        new MockUp<URI>() {

            @Mock
            public URL toURL() throws MalformedURLException {
                throw new MalformedURLException();
            }
        };

        String cfgFileName = appRoot + "test.txt";

        assertTrue(null == PropertiesUtil.getURL(cfgFileName));
    }
}
