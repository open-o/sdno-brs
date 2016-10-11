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

package org.openo.sdno.brs.util.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.brs.model.SiteMO;

import net.sf.json.JSONArray;

/**
 * JSON utility test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-25
 */
public class JsonUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUnMarshal() throws IOException {
        SiteMO site = new SiteMO();
        site.setId("1");
        String jsonstr = "{\"id\":\"1\"}";

        SiteMO result = JsonUtil.unMarshal(jsonstr, SiteMO.class);
        assertEquals(result.toString(), site.toString());
    }

    @Test
    public void testUnMarshal2() throws IOException {
        SiteMO site = new SiteMO();
        site.setId("1");
        String jsonstr = "{\"id\":\"1\"}";

        SiteMO result = JsonUtil.unMarshal(jsonstr, new TypeReference<SiteMO>() {});
        assertEquals(result.toString(), site.toString());
    }

    @Test
    public void testMarshal() throws IOException {
        SiteMO site = new SiteMO();
        site.setId("1");
        String jsonstr = "{\"id\":\"1\"}";
        String result = JsonUtil.marshal(site);

        assertEquals(jsonstr, result);
    }

    @Test
    public void testMarsha2() throws IOException {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "Luise");
        map1.put("age", "20");
        JSONArray ja = JSONArray.fromObject(map1);

        String jsonstr = "[{\"name\":\"Luise\",\"age\":\"20\"}]";
        String result = JsonUtil.marshal(ja);
        // assertEquals(jsonstr, result);
    }

    @Test
    public void testGetMapper() {
        assertTrue(JsonUtil.getMapper() instanceof ObjectMapper);
    }

    @Test
    public void testIsJsonEquals1() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, "obj0");
        anotherObjList.add(0, "obj0");

        assertTrue(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals2() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, "obj0");
        anotherObjList.add(0, "obj1");

        assertFalse(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals3() {
        List<Object> objList = new ArrayList<>();

        objList.add(0, "obj0");

        assertFalse(JsonUtil.isJsonEquals(objList, null));
    }

    @Test
    public void testIsJsonEquals4() {
        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        String tmpStr = "obj";
        objMap1.put("0", tmpStr);
        objMap2.put("0", tmpStr);

        assertTrue(JsonUtil.isJsonEquals(objMap1, objMap2));
    }

    @Test
    public void testIsJsonEquals5() {
        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        String tmpStr1 = "obj1";
        String tmpStr2 = "obj2";
        objMap1.put("0", tmpStr1);
        objMap1.put("0", tmpStr2);
        objMap2.put("0", tmpStr1);

        assertFalse(JsonUtil.isJsonEquals(objMap1, objMap2));
    }

    @Test
    public void testIsJsonEquals6() {
        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        String tmpStr1 = "obj1";

        objMap1.put("1", tmpStr1);
        objMap2.put("0", tmpStr1);

        assertFalse(JsonUtil.isJsonEquals(objMap1, objMap2));
    }

    @Test
    public void testIsJsonEquals7() {
        Map<String, Object> objMap1 = new HashMap<>();
        String tmpStr1 = "obj1";
        objMap1.put("1", tmpStr1);

        assertFalse(JsonUtil.isJsonEquals(objMap1, null));
    }

    @Test
    public void testIsJsonEquals8() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, "obj0");
        anotherObjList.add(0, "obj1");

        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        objMap1.put("0", objList);
        objMap2.put("0", anotherObjList);

        assertFalse(JsonUtil.isJsonEquals(objMap1, objMap2));
    }

    @Test
    public void testIsJsonEquals9() {
        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        String tmpStr1 = "obj1";

        objMap1.put("1", tmpStr1);
        objMap2.put("0", tmpStr1);

        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, objMap1);
        anotherObjList.add(0, objMap2);

        assertFalse(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals10() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, "obj0");
        anotherObjList.add(0, "obj1");

        Map<String, Object> objMap1 = new HashMap<>();
        Map<String, Object> objMap2 = new HashMap<>();
        objMap1.put("0", objList);
        objMap1.put("1", objList);
        objMap2.put("0", anotherObjList);

        assertFalse(JsonUtil.isJsonEquals(null, objMap2));
        assertFalse(JsonUtil.isJsonEquals(objMap2, null));
        assertFalse(JsonUtil.isJsonEquals(objMap1, objMap2));
    }

    @Test
    public void testIsJsonEquals11() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, "obj0");
        objList.add(1, "obj1");
        anotherObjList.add(0, "obj1");

        assertFalse(JsonUtil.isJsonEquals(null, anotherObjList));
        assertFalse(JsonUtil.isJsonEquals(objList, null));
        assertFalse(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals12() {
        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, null);
        anotherObjList.add(0, null);

        assertTrue(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals13() {
        List<String> valueList1 = new ArrayList<>();
        valueList1.add("list1");

        List<String> valueList2 = new ArrayList<>();
        valueList2.add("list1");

        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, valueList1);
        anotherObjList.add(0, valueList2);

        assertTrue(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals14() {
        List<String> valueList1 = new ArrayList<>();
        valueList1.add("list1");

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("0", null);

        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, valueList1);
        anotherObjList.add(0, objMap);

        assertFalse(JsonUtil.isJsonEquals(objList, anotherObjList));
    }

    @Test
    public void testIsJsonEquals15() {
        List<String> valueList1 = new ArrayList<>();
        valueList1.add("list1");

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("0", null);

        List<Object> objList = new ArrayList<>();
        List<Object> anotherObjList = new ArrayList<>();
        objList.add(0, valueList1);
        anotherObjList.add(0, null);

        assertFalse(JsonUtil.isJsonEquals(objList, anotherObjList));
        assertFalse(JsonUtil.isJsonEquals(anotherObjList, objList));
    }

}
