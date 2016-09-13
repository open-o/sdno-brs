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

package org.openo.sdno.brs.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Single range test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-26
 */
public class SingleRangeTest extends SingleRange {

    @Test
    public void testSetGet() {
        SingleRange singelRange = new SingleRange(0, 0);
        assertEquals(singelRange.getStart(), 0);
        assertEquals(singelRange.getEnd(), 0);
        assertEquals(singelRange.getSpliter(), "-");

        singelRange.setStart(1);
        singelRange.setEnd(2);
        singelRange.setSpliter("/");

        assertEquals(singelRange.getStart(), 1);
        assertEquals(singelRange.getEnd(), 2);
        assertEquals(singelRange.getSpliter(), "/");
    }

    @Test
    public void testIntersection() {
        SingleRange singelRange1 = new SingleRange(1, 4);
        SingleRange singelRange2 = new SingleRange(2, 5);

        SingleRange result = singelRange1.intersection(singelRange2);
        assertEquals(result.getStart(), 2);
        assertEquals(result.getEnd(), 4);
    }

    @Test
    public void testIntersectionNull() {
        SingleRange singelRange1 = new SingleRange(1, 2);
        SingleRange singelRange2 = new SingleRange(3, 5);

        assertEquals(singelRange1.intersection(null), null);
        assertEquals(singelRange2.intersection(singelRange1), null);
        assertEquals(singelRange1.intersection(singelRange2), null);
    }

    @Test
    public void testToString() {
        SingleRange singelRange1 = new SingleRange(1, 2);
        SingleRange singelRange2 = new SingleRange(1, 1);
        assertEquals(singelRange1.toString(), "1-2");
        assertEquals(singelRange2.toString(), "1");
    }

    @Test
    public void testContains() {
        SingleRange singelRange1 = new SingleRange(1, 2);

        assertFalse(singelRange1.contains(0));
        assertTrue(singelRange1.contains(1));
        assertFalse(singelRange1.contains(3));
    }

    @Test
    public void testEquals() {
        SingleRange singelRange1 = new SingleRange(1, 2);
        SingleRange singelRange2 = new SingleRange(1, 2);
        SingleRange singelRange3 = new SingleRange(3, 4);
        SingleRange singelRange4 = new SingleRange(1, 4);

        assertTrue(singelRange1.equals(singelRange2));
        assertFalse(singelRange1.equals(singelRange4));
        assertFalse(singelRange1.equals(singelRange3));
        singelRange2.setSpliter("*");
        assertFalse(singelRange1.equals(singelRange2));
    }

    @Test
    public void testHashCode() {
        SingleRange singelRange1 = new SingleRange(1, 2);

        assertEquals(258, singelRange1.hashCode());

    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        SingleRange singelRange1 = new SingleRange(1, 2);
        Object result = singelRange1.clone();
        assertTrue(singelRange1.equals(result));
    }

    @Test
    public void testParse1() {
        assertEquals(SingleRange.parse("1", "~").toString(), "1");
        assertEquals(SingleRange.parse("1~2", "~").toString(), "1~2");
        assertEquals(SingleRange.parse("2~1", "~").toString(), "1~2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse2() {
        SingleRange.parse("1-2-3", "-");
    }

    @Test
    public void testParse3() {
        assertEquals(SingleRange.parse("1").toString(), "1");
        assertEquals(SingleRange.parse("1-2").toString(), "1-2");
        assertEquals(SingleRange.parse("2-1").toString(), "1-2");
    }

    @Test
    public void testSetSpliter() {
        SingleRange singelRange = new SingleRange(2, 5);
        singelRange.setSpliter(null);
        assertEquals(singelRange.getSpliter(), SingleRange.DEFAULTSPLITER);

        singelRange.setSpliter("");
        assertEquals(singelRange.getSpliter(), SingleRange.DEFAULTSPLITER);
    }

}
