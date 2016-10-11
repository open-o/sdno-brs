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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.brs.constant.Constant;

/**
 * ResourcePagePara test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class ResourcePageParaTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        List<String> value = new ArrayList<>();
        value.add("1");
        value.add("2");

        Map<String, List<String>> relationInfoMap = new HashMap<String, List<String>>();
        relationInfoMap.put(Constant.FILEDS_EXT, value);
        ResourcePagePara resourcePagePara = new ResourcePagePara(2, 10, Constant.FILEDS_ALL, null, relationInfoMap);

        new ResourcePagePara(2, 10, Constant.FILEDS_EXT + "," + Constant.FILEDS_ALL, null, relationInfoMap);

        new ResourcePagePara(2, 10, Constant.FILEDS_ALL, new HashMap<String, String>(), relationInfoMap);

        List<String> resFieldList = new ArrayList<String>();
        resFieldList.add("1");
        resFieldList.add("2");
        resourcePagePara.setResFieldList(resFieldList);

        List<String> newResFieldList = resourcePagePara.getResFieldList();

        assertTrue(newResFieldList.size() == resFieldList.size());

        boolean bMatch = true;
        for(String temp : newResFieldList) {
            if(!resFieldList.contains(temp)) {
                bMatch = false;
                break;
            }
        }
        assertTrue(bMatch);

        HashMap<String, String> relationFilters = new HashMap<String, String>();
        relationFilters.put("1", "a");
        relationFilters.put("2", "b");
        resourcePagePara.setRelationFields(relationFilters);
        Map<String, String> newRelationFilters = resourcePagePara.getRelationFields();
        assertTrue(newRelationFilters.size() == relationFilters.size());

        bMatch = true;
        Set<String> keySet = newRelationFilters.keySet();
        Iterator<String> iterator = keySet.iterator();
        while(iterator.hasNext()) {
            if(!relationFilters.containsKey(iterator.next())) {
                bMatch = false;
                break;
            }
        }
        assertTrue(bMatch);

        resourcePagePara.setBaseFilters(null);
        assertTrue(resourcePagePara.getBaseFilters() == null);

        resourcePagePara.setPagesize(5);
        assertTrue(5 == resourcePagePara.getPagesize());

        resourcePagePara.setPagenum(20);
        assertTrue(20 == resourcePagePara.getPagenum());

        resourcePagePara.setRelationFilters(null);
        assertTrue(resourcePagePara.getRelationFilters() == null);

    }

}
