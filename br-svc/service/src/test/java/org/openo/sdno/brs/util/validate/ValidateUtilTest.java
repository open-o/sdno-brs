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

package org.openo.sdno.brs.util.validate;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * ValidateUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ValidateUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = ServiceException.class)
    public void testAssertNotNull1() throws ServiceException {
        ValidateUtil.assertNotNull(null, "test");
    }

    @Test
    public void testAssertNotNull2() {
        try {
            ValidateUtil.assertNotNull("111", "test");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testAssertNotEmpty1() {
        try {
            ValidateUtil.assertNotEmpty(null, "test");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAssertNotEmpty2() {
        try {
            ValidateUtil.assertNotEmpty("111", "test");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testAssertNotList1() {
        try {
            ValidateUtil.assertNotList("111", "test");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAssertNotList2() {
        try {
            List<String> list = new ArrayList<String>();
            list.add("1");

            ValidateUtil.assertNotList(list, "test");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCheckUuidValid() {
        try {
            ValidateUtil.checkUuid("111");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCheckUuidinvalid1() {
        try {
            ValidateUtil.checkUuid(null);
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckUuidinvalid2() {
        try {
            ValidateUtil.checkUuid("");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckUuidinvalid3() {
        try {
            ValidateUtil.checkUuid("()x1");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckTenantID1() {
        try {
            ValidateUtil.checkTenantID("111");
            assertTrue(true);
        } catch(ServiceException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCheckTenantID2() {
        try {
            ValidateUtil.checkTenantID(null);
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCheckTenantID3() {
        try {
            ValidateUtil.checkTenantID("");
            assertTrue(false);
        } catch(ServiceException e) {
            assertTrue(true);
        }
    }
}
