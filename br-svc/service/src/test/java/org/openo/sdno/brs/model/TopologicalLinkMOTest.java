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
 * TopologicalLinkMO test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class TopologicalLinkMOTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testTopologicalLinkMO() {
        TopologicalLinkMO topologicalLinkMO = new TopologicalLinkMO();

        topologicalLinkMO.setName("test");
        assertTrue("test".equals(topologicalLinkMO.getName()));

        topologicalLinkMO.setLayerRate("layerRate");
        assertTrue("layerRate".equals(topologicalLinkMO.getLayerRate()));

        topologicalLinkMO.setaEnd("aEnd");
        assertTrue("aEnd".equals(topologicalLinkMO.getaEnd()));

        topologicalLinkMO.setzEnd("zEnd");
        assertTrue("zEnd".equals(topologicalLinkMO.getzEnd()));

        topologicalLinkMO.setaEndME("aEndMe");
        assertTrue("aEndMe".equals(topologicalLinkMO.getaEndME()));

        topologicalLinkMO.setzEndME("zEndMe");
        assertTrue("zEndMe".equals(topologicalLinkMO.getzEndME()));

        topologicalLinkMO.setPhyBW("phyBw");
        assertTrue("phyBw".equals(topologicalLinkMO.getPhyBW()));

        topologicalLinkMO.setOwner("owner");
        assertTrue("owner".equals(topologicalLinkMO.getOwner()));

        topologicalLinkMO.setDirection("uni_direction");
        assertTrue("uni_direction".equals(topologicalLinkMO.getDirection()));

        topologicalLinkMO.setLogicalType("l2Link");
        assertTrue("l2Link".equals(topologicalLinkMO.getLogicalType()));

        topologicalLinkMO.setSource("network_me");
        assertTrue("network_me".equals(topologicalLinkMO.getSource()));

        topologicalLinkMO.setAdminState("active");
        assertTrue("active".equals(topologicalLinkMO.getAdminState()));

        topologicalLinkMO.setOperState("up");
        assertTrue("up".equals(topologicalLinkMO.getOperState()));

        topologicalLinkMO.setNativeID("123");
        assertTrue("123".equals(topologicalLinkMO.getNativeID()));

        topologicalLinkMO.setLatency("latency");
        assertTrue("latency".equals(topologicalLinkMO.getLatency()));

        topologicalLinkMO.toString();
    }

}
