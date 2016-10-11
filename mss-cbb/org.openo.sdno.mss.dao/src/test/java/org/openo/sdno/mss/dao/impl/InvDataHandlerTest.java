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

package org.openo.sdno.mss.dao.impl;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.sdno.mss.dao.entities.InvRelationEntity;
import org.openo.sdno.mss.dao.intf.InvRelationDataHandler;
import org.openo.sdno.mss.dao.util.ValidUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvDataHandler test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class InvDataHandlerTest {

    InvRelationDataHandler relationDataHandler;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        new MockUp<ValidUtil>() {

            @Mock
            public void checkResType(String resType) {
                return;
            }

            @Mock
            public void checkAttributes(String resType, String attributes, boolean checkHiddenAttr) {
                return;
            }

            @Mock
            public void checkFilter(String resType, String filter) {
                return;
            }

            @Mock
            public void checkSort(String resType, String attributes, String sortAttrName, Object refValue) {
                return;
            }

            @Mock
            public void checkSplitPage(String sortAttrName, Object refValue, Object uniqueValue) {
                return;
            }
        };

        new MockUp<InvRelationDataHandlerImpl>() {

            @Mock
            public List<InvRelationEntity> querySubNode(String resType, List<String> uuidList) {
                List<InvRelationEntity> subNodeList = new ArrayList<InvRelationEntity>();
                subNodeList.add(new InvRelationEntity());
                return subNodeList;
            }

        };

    }

    InvDataHandlerImpl demo = new InvDataHandlerImpl();

    @Test
    public void testGetSplitPageNull() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        Class clazz = demo.getClass();
        Class[] args = new Class[] {String.class, List.class, Map.class, Map.class};
        Method method = clazz.getDeclaredMethod("prepareSubNodesRecursively", args);
        method.setAccessible(true);
        List<String> uuidList = new ArrayList<String>();
        Map<String, List<String>> preDelDataMap = new HashMap<String, List<String>>();
        Map<String, List<InvRelationEntity>> preDelRelationMap = new HashMap<String, List<InvRelationEntity>>();
        preDelDataMap.put("key", uuidList);
        List<InvRelationEntity> relist = new ArrayList<InvRelationEntity>();
        preDelRelationMap.put("key", relist);
        method.invoke(demo, "key", uuidList, preDelDataMap, preDelRelationMap);
        assertTrue(uuidList.isEmpty());
    }

}
