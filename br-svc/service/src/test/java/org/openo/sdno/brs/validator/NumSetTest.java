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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

/**
 * NumSet test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class NumSetTest {

    private NumSet numSet;

    @Before
    public void setUp() throws Exception {
        numSet = NumSet.parse("1,2,3,9,8,4,7,11-17,6,21,18-19,25-23");
    }

    @Test
    public void testGetMax() {
        int num = numSet.getMax();
        assertTrue(25 == num);
    }

    @Test
    public void testGetMaxNotExisted() {
        numSet = NumSet.parse("");
        int num = numSet.getMax();
        assertTrue(0 == num);
    }

    @Test
    public void testGetMin() {
        int num = numSet.getMin();
        assertTrue(1 == num);
    }

    @Test
    public void testGetMinNotExisted() {
        numSet = NumSet.parse("");
        int num = numSet.getMin();
        assertTrue(0 == num);
    }

    @Test
    public void testAdjust() {
        numSet = NumSet.parse("");
        assertTrue(numSet.isEmpty());
    }

    @Test
    public void testContains() {
        assertTrue(numSet.contains(2));
        assertFalse(numSet.contains(20));
        assertFalse(numSet.contains(26));

        numSet = NumSet.parse("");
        assertFalse(numSet.contains(2));
    }

    @Test
    public void testToString() {
        String a = numSet.toString();
        assertTrue("1-4,6-9,11-19,21,23-25".equals(a));

        numSet = NumSet.parse("");
        String b = numSet.toString();
        assertTrue("".equals(b));
    }

    @Test
    public void testSetSpliter() {
        numSet.setSpliter("|");
        assertTrue("|".equals(numSet.getSpliter()));

        numSet.setSpliter(null);
        assertTrue(NumSet.DEFAULTSPLITER.equals(numSet.getSpliter()));
    }

    @Test
    public void testSetRangeSpliter() {
        numSet.setRangeSpliter("&");
        assertTrue("&".equals(numSet.getRangeSpliter()));
        assertTrue("1&4,6&9,11&19,21,23&25".equals(numSet.toString()));

        numSet.setRangeSpliter("");
        assertTrue(SingleRange.DEFAULTSPLITER.equals(numSet.getRangeSpliter()));

        numSet.setRangeSpliter(null);
        assertTrue(SingleRange.DEFAULTSPLITER.equals(numSet.getRangeSpliter()));

        numSet = NumSet.parse("");
        numSet.setRangeSpliter(",");
        assertTrue(",".equals(numSet.getRangeSpliter()));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        NumSet newNumSet = (NumSet)numSet.clone();
        assertTrue(numSet.getSpliter().equals(newNumSet.getSpliter()));
        assertTrue(numSet.getRangeSpliter().equals(newNumSet.getRangeSpliter()));
        assertTrue(numSet.getRanges().size() == newNumSet.getRanges().size());

        numSet = NumSet.parse("");
        newNumSet = (NumSet)numSet.clone();
        assertTrue(numSet.getRanges().size() == newNumSet.getRanges().size());
    }

    @Test
    public void testParse() {
        new MockUp<String>() {

            @Mock
            public String[] split(String regex) {
                String[] ret = {};
                return ret;
            }
        };

        NumSet.parse("", null, null);

        try {
            NumSet.parse("", "#", "#");
            assertTrue(false);
        } catch(Exception e) {
        }

        NumSet.parse("", ",", "-");
        numSet = NumSet.parse("1,2,3,8-9,9-12", ",", "-");
        assertTrue(numSet.getRanges().size() == 0);
    }

}
