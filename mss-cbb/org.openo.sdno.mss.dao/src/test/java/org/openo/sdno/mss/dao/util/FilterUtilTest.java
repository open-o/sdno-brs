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

package org.openo.sdno.mss.dao.util;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * FilterUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class FilterUtilTest {

    @Test
    public void testBuildStringListFilter() {
        Set<String> inputSet = new HashSet<String>();
        inputSet.add("a");
        inputSet.add("b");
        String result = FilterUtil.buildInFilterString("field", Datatype.STRING, inputSet);
        String expectResult = "{\"field\":[\"a\",\"b\"]}";
        assertTrue(expectResult.equals(result));
    }

    @Test
    public void testBuildIntegerListFilter() {
        Set<Integer> inputSet = new HashSet<Integer>();
        inputSet.add(1);
        inputSet.add(2);
        String result = FilterUtil.buildInFilterString("field", Datatype.INTEGER, inputSet);
        String expectResult = "{\"field\":[1,2]}";
        assertTrue(expectResult.equals(result));
    }

}
