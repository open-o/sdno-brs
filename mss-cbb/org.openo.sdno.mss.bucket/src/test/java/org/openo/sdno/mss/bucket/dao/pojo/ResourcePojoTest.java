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
 * ResourcePojo test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ResourcePojoTest {

    ResourcePojo resource = new ResourcePojo();

    @Test
    public void testNull() {
        resource.setBktName(null);
        resource.setDmspec(null);
        resource.setImspec(null);
        resource.setRestype(null);
        resource.setUuid(null);

        assertNull(resource.getBktName());
        assertNull(resource.getDmspec());
        assertNull(resource.getImspec());
        assertNull(resource.getRestype());
        assertNull(resource.getUuid());
    }

    @Test
    public void test() {
        resource.setBktName("BktName11");
        resource.setDmspec("Dmspec22");
        resource.setImspec("Imspec33");
        resource.setRestype("Restype44");
        resource.setUuid("Uuid55");

        assertEquals("BktName11", resource.getBktName());
        assertEquals("Dmspec22", resource.getDmspec());
        assertEquals("Imspec33", resource.getImspec());
        assertEquals("Restype44", resource.getRestype());
        assertEquals("Uuid55", resource.getUuid());
    }

}
