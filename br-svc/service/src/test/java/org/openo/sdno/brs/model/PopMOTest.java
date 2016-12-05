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

import org.junit.Test;

public class PopMOTest {

    @Test
    public void test() {

        PopMO popMO = new PopMO();

        popMO.setId("id");
        assertTrue("id".equals(popMO.getId()));

        popMO.setDescription("description");
        assertTrue("description".equals(popMO.getDescription()));

        popMO.setName("name");
        assertTrue("name".equals(popMO.getName()));

        popMO.setLocation("location");
        assertTrue("location".equals(popMO.getLocation()));

        popMO.setArea("area");
        assertTrue("area".equals(popMO.getArea()));

        popMO.setSource("source");
        assertTrue("source".equals(popMO.getSource()));

        popMO.setControllerID("controllerId");
        assertTrue("controllerId".equals(popMO.getControllerID()));

        popMO.setDcGW("dcGW");
        assertTrue("dcGW".equals(popMO.getDcGW()));

        popMO.setInternetGW("internetGW");
        assertTrue("internetGW".equals(popMO.getInternetGW()));

        popMO.setSiteGW("siteGw");
        assertTrue("siteGw".equals(popMO.getSiteGW()));

        popMO.setManagedGW("managedGw");
        assertTrue("managedGw".equals(popMO.getManagedGW()));
    }

}
