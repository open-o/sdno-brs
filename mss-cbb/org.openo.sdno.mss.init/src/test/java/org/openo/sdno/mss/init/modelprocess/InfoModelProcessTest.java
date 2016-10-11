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

package org.openo.sdno.mss.init.modelprocess;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.Test;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Property;

/**
 * InfoModelProcess test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-27
 */
public class InfoModelProcessTest {

    @Test
    public void testGetColunmTypeFloat() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Property property = new Property();
        property.setLength(BigInteger.valueOf(10));
        property.setScale(BigInteger.valueOf(10));
        property.setType(Datatype.FLOAT);
        Class clazz = Class.forName("org.openo.sdno.mss.init.modelprocess.InfoModelProcess");
        Class[] args = new Class[] {Datatype.class, BigInteger.class, BigInteger.class};
        Method method = clazz.getDeclaredMethod("getColunmType", args);
        method.setAccessible(true);
        assertTrue("float(10,10)".equals(
                method.invoke(InfoModelProcess.class, property.getType(), property.getLength(), property.getScale())));

    }

    @Test
    public void testGetColunmTypeInt() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Property property = new Property();
        property.setLength(BigInteger.valueOf(10));
        property.setScale(BigInteger.valueOf(10));
        property.setType(Datatype.DATETIME);
        Class clazz = Class.forName("org.openo.sdno.mss.init.modelprocess.InfoModelProcess");
        Class[] args = new Class[] {Datatype.class, BigInteger.class, BigInteger.class};
        Method method = clazz.getDeclaredMethod("getColunmType", args);
        method.setAccessible(true);
        assertTrue("int".equals(
                method.invoke(InfoModelProcess.class, property.getType(), property.getLength(), property.getScale())));

    }

    @Test
    public void testGetColunmTypeDouble() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Property property = new Property();
        property.setLength(BigInteger.valueOf(10));
        property.setScale(BigInteger.valueOf(10));
        property.setType(Datatype.DOUBLE);
        Class clazz = Class.forName("org.openo.sdno.mss.init.modelprocess.InfoModelProcess");
        Class[] args = new Class[] {Datatype.class, BigInteger.class, BigInteger.class};
        Method method = clazz.getDeclaredMethod("getColunmType", args);
        method.setAccessible(true);
        assertTrue("double".equals(
                method.invoke(InfoModelProcess.class, property.getType(), property.getLength(), property.getScale())));
    }

    @Test
    public void testBuildPrecisionNull() throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class clazz = Class.forName("org.openo.sdno.mss.init.modelprocess.InfoModelProcess");
        Class[] args = new Class[] {StringBuilder.class, BigInteger.class, BigInteger.class};
        Method method = clazz.getDeclaredMethod("buildPrecision", args);
        method.setAccessible(true);
        assertTrue(null == method.invoke(InfoModelProcess.class, new StringBuilder(), null, null));
    }

}
