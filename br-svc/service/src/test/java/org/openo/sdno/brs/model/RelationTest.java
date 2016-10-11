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

import org.junit.Before;
import org.junit.Test;

/**
 * Relation test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class RelationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        Relation relation = new Relation();
        relation.setServiceType("test");
        assertTrue("test".equals(relation.getServiceType()));
        relation.hashCode();

        assertTrue(relation.equals(relation));
        assertFalse(relation.equals(null));
        assertFalse(relation.equals(new String()));

        Relation relation2 = new Relation();
        relation2.setServiceType("test");
        assertTrue(relation.equals(relation2));
        relation2.hashCode();

        relation2.setDstType("dstType");
        assertFalse(relation.equals(relation2));
        assertFalse(relation2.equals(relation));
        relation2.hashCode();

        relation2.setDstType(null);
        relation2.setDstId("dstId");
        assertFalse(relation.equals(relation2));
        assertFalse(relation2.equals(relation));
        relation2.hashCode();

        relation2.setDstId(null);
        relation2.setSrcId("srcId");
        assertFalse(relation.equals(relation2));
        assertFalse(relation2.equals(relation));
        relation2.hashCode();
    }

}
