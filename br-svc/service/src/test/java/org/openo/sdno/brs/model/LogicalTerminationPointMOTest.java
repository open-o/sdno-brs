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

import org.junit.Before;
import org.junit.Test;

/**
 * LogicalTerminationPointMO test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class LogicalTerminationPointMOTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {

        LogicalTerminationPointMO logicalTerminationPointMO = new LogicalTerminationPointMO();

        logicalTerminationPointMO.setName("name");
        assertTrue("name".equals(logicalTerminationPointMO.getName()));

        logicalTerminationPointMO.setMacAddress("abc123");
        assertTrue("abc123".equals(logicalTerminationPointMO.getMacAddress()));

        logicalTerminationPointMO.setMeID("111");
        assertTrue("111".equals(logicalTerminationPointMO.getMeID()));

        logicalTerminationPointMO.setLogicalType("ETH");
        assertTrue("ETH".equals(logicalTerminationPointMO.getLogicalType()));

        logicalTerminationPointMO.setLayerRate("11");
        assertTrue("11".equals(logicalTerminationPointMO.getLayerRate()));

        logicalTerminationPointMO.setIsEdgePoint("true");
        assertTrue("true".equals(logicalTerminationPointMO.getIsEdgePoint()));

        logicalTerminationPointMO.setPortIndex("1");
        assertTrue("1".equals(logicalTerminationPointMO.getPortIndex()));

        logicalTerminationPointMO.setSource("user");
        assertTrue("user".equals(logicalTerminationPointMO.getSource()));

        logicalTerminationPointMO.setOwner("owner");
        assertTrue("owner".equals(logicalTerminationPointMO.getOwner()));

        logicalTerminationPointMO.setIpAddress("127.0.0.1");
        assertTrue("127.0.0.1".equals(logicalTerminationPointMO.getIpAddress()));

        logicalTerminationPointMO.setAdminState("active");
        assertTrue("active".equals(logicalTerminationPointMO.getAdminState()));

        logicalTerminationPointMO.setOperState("up");
        assertTrue("up".equals(logicalTerminationPointMO.getOperState()));

        logicalTerminationPointMO.setDirection("D_BIDIRECTIONAL");
        assertTrue("D_BIDIRECTIONAL".equals(logicalTerminationPointMO.getDirection()));

        logicalTerminationPointMO.setPhyBW("1");
        assertTrue("1".equals(logicalTerminationPointMO.getPhyBW()));

        logicalTerminationPointMO.setIpMask("24");
        assertTrue("24".equals(logicalTerminationPointMO.getIpMask()));

        logicalTerminationPointMO.setNativeID("123");
        assertTrue("123".equals(logicalTerminationPointMO.getNativeID()));

        logicalTerminationPointMO.setTenantID("456");
        assertTrue("456".equals(logicalTerminationPointMO.getTenantID()));

        logicalTerminationPointMO.setUsageState("used");
        assertTrue("used".equals(logicalTerminationPointMO.getUsageState()));

        logicalTerminationPointMO.setContainedLayers("1111");
        assertTrue("1111".equals(logicalTerminationPointMO.getContainedLayers()));

        logicalTerminationPointMO.toString();
    }

}
