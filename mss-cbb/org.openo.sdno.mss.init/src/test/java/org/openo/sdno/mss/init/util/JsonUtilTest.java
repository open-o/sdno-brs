/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.mss.init.util;

import static org.junit.Assert.assertTrue;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mockit.Mock;
import mockit.MockUp;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.sdno.mss.init.util.JsonUtil;

public class JsonUtilTest {

    static Map<String, String> strMap = new HashMap<String, String>();

    static String jsonStr;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        strMap.put("key1", "value1");
        strMap.put("key2", "value2");
        strMap.put("key3", "value3");

        jsonStr = "{\"key3\":\"value3\",\"key2\":\"value2\",\"key1\":\"value1\"}";
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        strMap.clear();
        jsonStr = "";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToJsonNegtive() {
        new MockUp<ObjectMapper>() {

            @Mock
            String writeValueAsString(Object value) throws IOException, JsonGenerationException, JsonMappingException {
                throw new IOException();
            }
        };
        JsonUtil.toJson(strMap);
    }

    @Test
    public <T> void testFromJsonClass() {
        Map<String, String> tempMap = JsonUtil.fromJson(jsonStr, Map.class);
        assertTrue(tempMap.equals(strMap));
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testFromJsonClassNegtive() {
        new MockUp<ObjectMapper>() {

            @Mock
            <T> T readValue(String content, Class<T> valueType) throws IOException, JsonParseException,
                    JsonMappingException {
                throw new IOException();
            }
        };
        JsonUtil.fromJson(jsonStr, Map.class);
    }

    @Test
    public void testFromJsonTypeReference() {
        Map<String, String> tempMap = JsonUtil.fromJson(jsonStr, new TypeReference<Map<String, String>>() {});
        assertTrue(tempMap.equals(strMap));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsonTeypeException() {
        new MockUp<ObjectMapper>() {

            @Mock
            <T> T readValue(String content, TypeReference valueTypeRef) throws IOException, JsonParseException,
                    JsonMappingException {
                throw new IOException();
            }
        };
        JsonUtil.fromJson(jsonStr, new TypeReference<Map<String, String>>() {});
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFromJsonFileClass() {
        File inputFile = new File("src/test/java/org/openo/sdno/mss/init/util/testfile/JsonUtilTestFile.json");
        Map<String, String> tempMap = JsonUtil.fromJson(inputFile, Map.class);
        assertTrue(tempMap.equals(strMap));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsonFileClassException() {
        new MockUp<ObjectMapper>() {

            @Mock
            <T> T readValue(File inputFile, Class<T> valueType) throws IOException, JsonParseException,
                    JsonMappingException {
                throw new IOException();
            }
        };
        File inputFile = new File("src/test/java/org/openo/sdno/mss/init/util/testfile/JsonUtilTestFile.json");
        JsonUtil.fromJson(inputFile, Map.class);
    }
}
