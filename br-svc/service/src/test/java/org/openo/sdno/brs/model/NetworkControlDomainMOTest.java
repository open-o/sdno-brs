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

package org.openo.sdno.brs.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * NetworkControlDomainMO test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class NetworkControlDomainMOTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        NetworkControlDomainMO networkControlDomainMO = new NetworkControlDomainMO();

        List<String> managementElementIDs = new ArrayList<>();
        managementElementIDs.add("1");
        managementElementIDs.add("2");

        networkControlDomainMO.setManagementElementIDs(managementElementIDs);
        List<String> newManagementElementIDs = networkControlDomainMO.getManagementElementIDs();

        boolean bMatch = true;
        assertTrue(newManagementElementIDs.size() == managementElementIDs.size());
        for(String temp : newManagementElementIDs) {
            if(!managementElementIDs.contains(temp)) {
                bMatch = false;
                break;
            }
        }

        assertTrue(bMatch);

        networkControlDomainMO.setNativeID("111");
        assertTrue("111".equals(networkControlDomainMO.getNativeID()));

        networkControlDomainMO.setName("name");
        assertTrue("name".equals(networkControlDomainMO.getName()));

        networkControlDomainMO.setUserLabel("label");
        assertTrue("label".equals(networkControlDomainMO.getUserLabel()));

        networkControlDomainMO.setParentNcdID("123");
        assertTrue("123".equals(networkControlDomainMO.getParentNcdID()));

        networkControlDomainMO.setLocation("1");
        assertTrue("1".equals(networkControlDomainMO.getLocation()));

        networkControlDomainMO.setManufacturer("test");
        assertTrue("test".equals(networkControlDomainMO.getManufacturer()));

        networkControlDomainMO.setIpAddress("127.0.0.1");
        assertTrue("127.0.0.1".equals(networkControlDomainMO.getIpAddress()));

        networkControlDomainMO.setPort(80);
        assertTrue(80 == networkControlDomainMO.getPort());

        networkControlDomainMO.setAdminStatus("up");
        assertTrue("up".equals(networkControlDomainMO.getAdminStatus()));

        networkControlDomainMO.setOperateStatus("up");
        assertTrue("up".equals(networkControlDomainMO.getOperateStatus()));

    }

}
