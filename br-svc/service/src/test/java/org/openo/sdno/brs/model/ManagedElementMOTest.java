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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * ManagedElementMO test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ManagedElementMOTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        ManagedElementMO managedElementMO = new ManagedElementMO();

        managedElementMO.hashCode();

        assertTrue(managedElementMO.equals(managedElementMO));
        assertFalse(managedElementMO.equals(null));
        assertFalse(managedElementMO.equals(new String()));

        ManagedElementMO managedElementMO2 = new ManagedElementMO();
        assertTrue(managedElementMO.equals(managedElementMO2));

        managedElementMO2.setVersion("1");
        assertFalse(managedElementMO.equals(managedElementMO2));
        assertFalse(managedElementMO2.equals(managedElementMO));

        managedElementMO2.hashCode();
        managedElementMO2.toString();

        List<String> networkControlDomainID = new ArrayList<>();
        networkControlDomainID.add("1");
        networkControlDomainID.add("2");
        managedElementMO.setNetworkControlDomainID(networkControlDomainID);
        List<String> newNetworkControlDomainID = managedElementMO.getNetworkControlDomainID();

        assertTrue(newNetworkControlDomainID.size() == networkControlDomainID.size());

        boolean bMatch = true;
        for(String temp : newNetworkControlDomainID) {
            if(!networkControlDomainID.contains(temp)) {
                bMatch = false;
                break;
            }
        }

        assertTrue(bMatch);

        managedElementMO.setName("name");
        assertTrue("name".equals(managedElementMO.getName()));

        managedElementMO.setLogicID("123");
        assertTrue("123".equals(managedElementMO.getLogicID()));

        managedElementMO.setPhyNeID("456");
        assertTrue("456".equals(managedElementMO.getPhyNeID()));

        managedElementMO.setProductName("productName");
        assertTrue("productName".equals(managedElementMO.getProductName()));

        managedElementMO.setIsVirtual("true");
        assertTrue("true".equals(managedElementMO.getIsVirtual()));

        managedElementMO.setIpAddress("127.0.0.1");
        assertTrue("127.0.0.1".equals(managedElementMO.getIpAddress()));

        managedElementMO.setSource("user");
        assertTrue("user".equals(managedElementMO.getSource()));

        managedElementMO.setOwner("owner");
        assertTrue("owner".equals(managedElementMO.getOwner()));

        managedElementMO.setAdminState("active");
        assertTrue("active".equals(managedElementMO.getAdminState()));

        managedElementMO.setOperState("down");
        assertTrue("down".equals(managedElementMO.getOperState()));

        managedElementMO.setSerialNumber("000");
        assertTrue("000".equals(managedElementMO.getSerialNumber()));

        managedElementMO.setManufacturer("test");
        assertTrue("test".equals(managedElementMO.getManufacturer()));

        managedElementMO.setManufactureDate("2016.7.1");
        assertTrue("2016.7.1".equals(managedElementMO.getManufactureDate()));

        managedElementMO.setLocation("1");
        assertTrue("1".equals(managedElementMO.getLocation()));

        managedElementMO.setManagementDomainID("111");
        assertTrue("111".equals(managedElementMO.getManagementDomainID()));

        List<String> controllerID = new ArrayList<>();
        controllerID.add("1");
        controllerID.add("2");
        managedElementMO.setControllerID(controllerID);
        List<String> newControllerID = managedElementMO.getControllerID();

        assertTrue(controllerID.size() == newControllerID.size());

        bMatch = true;
        for(String temp : newControllerID) {
            if(!controllerID.contains(temp)) {
                bMatch = false;
                break;
            }
        }

        assertTrue(bMatch);

        List<String> siteID = new ArrayList<>();
        siteID.add("1");
        siteID.add("2");
        managedElementMO.setSiteID(siteID);
        List<String> newSiteID = managedElementMO.getSiteID();

        assertTrue(siteID.size() == newSiteID.size());

        bMatch = true;
        for(String temp : newSiteID) {
            if(!siteID.contains(temp)) {
                bMatch = false;
                break;
            }
        }

        assertTrue(bMatch);

        managedElementMO.setNativeID("111");
        assertTrue("111".equals(managedElementMO.getNativeID()));

        managedElementMO.setNeRole("role");
        assertTrue("role".equals(managedElementMO.getNeRole()));
    }

}
