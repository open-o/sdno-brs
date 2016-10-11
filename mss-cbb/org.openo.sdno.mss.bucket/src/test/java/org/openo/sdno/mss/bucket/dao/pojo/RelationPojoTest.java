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

package org.openo.sdno.mss.bucket.dao.pojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * RelationPojo test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class RelationPojoTest {

    RelationPojo relation = new RelationPojo();

    @Test
    public void testNull() {
        relation.setBktName(null);
        relation.setRmspec(null);

        assertNull(relation.getBktName());
        assertNull(relation.getRmspec());
    }

    @Test
    public void test() {
        relation.setBktName("BktName11");
        relation.setRmspec("Rmspec22");

        assertEquals("BktName11", relation.getBktName());
        assertEquals("Rmspec22", relation.getRmspec());
    }
}
