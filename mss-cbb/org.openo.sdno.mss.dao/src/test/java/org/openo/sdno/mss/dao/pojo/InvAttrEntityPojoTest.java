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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Property;

/**
 * InvAttrEntityPojoTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 28, 2016
 */
public class InvAttrEntityPojoTest {

    @Test
    public void testInvAttrEntityPojo() {
        Property pro = new Property();
        pro.setName("Pro");
        pro.setType(Datatype.STRING);
        pro.setLength(new BigInteger("10"));

        InvAttrEntityPojo inv = new InvAttrEntityPojo(pro);
        assertEquals("varchar(10)", inv.getAttrType());

        pro.setType(Datatype.DECIMAL);
        InvAttrEntityPojo inv2 = new InvAttrEntityPojo(pro);
        assertTrue(inv2.getAttrType().contains("decimal"));

        pro.setType(Datatype.DATETIME);
        InvAttrEntityPojo inv3 = new InvAttrEntityPojo(pro);
        assertEquals("date", inv3.getAttrType());

        pro.setType(Datatype.FLOAT);
        InvAttrEntityPojo inv4 = new InvAttrEntityPojo(pro);
        assertTrue(inv4.getAttrType().contains("float"));

        pro.setType(Datatype.DOUBLE);
        InvAttrEntityPojo inv5 = new InvAttrEntityPojo(pro);
        assertEquals("double", inv5.getAttrType());
    }

    @Test
    public void testSetterGetter() {
        Property pro = new Property();
        pro.setName("Pro");
        pro.setType(Datatype.STRING);
        pro.setLength(new BigInteger("10"));
        InvAttrEntityPojo inv = new InvAttrEntityPojo(pro);
        inv.buildValue("attrValue");
        inv.setAttrName("attrName");

        assertEquals("attrValue", inv.getAttrValue());
        assertEquals("attrName", inv.getAttrName());
        assertEquals(Datatype.STRING, inv.getDataType());
    }

    @Test
    public void testEqualsTypeNotSame() {
        Property pro = new Property();
        pro.setName("Pro");
        pro.setType(Datatype.STRING);
        pro.setLength(new BigInteger("10"));
        InvAttrEntityPojo inv = new InvAttrEntityPojo(pro);

        assertFalse(inv.equals("test"));
    }
}
