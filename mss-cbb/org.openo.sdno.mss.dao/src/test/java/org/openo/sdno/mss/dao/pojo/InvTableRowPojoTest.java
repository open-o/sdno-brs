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

package org.openo.sdno.mss.dao.pojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * InvTableRowPojoTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 27, 2016
 */
public class InvTableRowPojoTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInvTableRowPojoDefault() {
        InvTableRowPojo inv = new InvTableRowPojo();
        assertTrue(inv.getAttrValueList().isEmpty());
    }

    @Test
    public void testInvTableRowPojoInputNull() {
        InvTableRowPojo inv = new InvTableRowPojo(null);
        assertTrue(inv.getAttrValueList().isEmpty());
    }

    @Test
    public void testInvTableRowPojoNormalInput() {
        List<Object> attrValueList = new ArrayList<>();
        attrValueList.add("test1");
        InvTableRowPojo inv = new InvTableRowPojo(attrValueList);
        assertEquals(1, inv.getAttrValueList().size());
    }

    @Test
    public void testBuildValue() {
        List<Object> attrValueList = new ArrayList<>();
        attrValueList.add("test1");
        InvTableRowPojo inv = new InvTableRowPojo(attrValueList);
        inv.buildValue("test2");
        assertEquals(2, inv.getAttrValueList().size());
    }

}
