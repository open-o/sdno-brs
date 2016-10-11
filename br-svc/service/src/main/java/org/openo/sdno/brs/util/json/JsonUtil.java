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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSON;

/**
 * JSON utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
        // Private constructor to prohibit instantiation.
    }

    static {
        // When do anti - serialization, ignore the undefined property.
        // Allows the end to add attributes, and won't not affect the analysis.
        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig()
                .without(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));

        // When do serialization, do not set empty fields to NULL, avoid too many NULL fields.
        MAPPER.setSerializationInclusion(Inclusion.NON_NULL);
    }

    /**
     * Parsing JSON string. Use for the service interface to parse request data.<br>
     * 
     * @param jsonstr request data
     * @param type target type
     * @return Request target type's data
     * @throws IOException when operate failed.
     * @since SDNO 0.5
     */
    public static <T> T unMarshal(String jsonstr, Class<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Parsing JSON string(For generic object types). Use for the service interface to parse request
     * data.<br>
     * 
     * @param jsonstr request data
     * @param type target type
     * @return Request target type's data
     * @throws IOException when operate failed.
     * @since SDNO 0.5
     */
    public static <T> T unMarshal(String jsonstr, TypeReference<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Converts an object to a JSON format string.<br>
     * 
     * @param srcObj object to be converted
     * @return JSON string
     * @throws IOException when operate failed
     * @since SDNO 0.5
     */
    public static String marshal(Object srcObj) throws IOException {
        if(srcObj instanceof JSON) {
            return srcObj.toString();
        }
        return MAPPER.writeValueAsString(srcObj);
    }

    /**
     * Get the MAPPER.<br>
     * 
     * @return MAPPER
     * @since SDNO 0.5
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * Check two JSON objects lists are equal or not.<br>
     * 
     * @param jsonObj list of JSON objects
     * @param jsonObjAnother list of JSON objects
     * @return boolean, return true if they are equal, else false.
     * @since SDNO 0.5
     */
    public static boolean isJsonEquals(List<Object> jsonObj, List<Object> jsonObjAnother) {
        if(null == jsonObj || null == jsonObjAnother || jsonObj.size() != jsonObjAnother.size()) {
            return false;
        }

        Iterator<Object> iter = jsonObjAnother.iterator();
        for(Object obj : jsonObj) {
            Object anotherObj = iter.next();
            if(!isJsonObjEquals(obj, anotherObj)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check two JSON objects are equal or not.<br>
     * 
     * @param obj JSON object
     * @param anotherObj JSON object
     * @return boolean, return true if they are equal, else false.
     * @since SDNO 0.5
     */
    @SuppressWarnings({"unchecked"})
    private static boolean isJsonObjEquals(Object obj, Object anotherObj) {
        if(null == obj && null == anotherObj) {
            return true;
        }

        if((obj instanceof List) && (anotherObj instanceof List)) {
            List<Object> objList = (List<Object>)obj;
            List<Object> anotherObjList = (List<Object>)anotherObj;
            return isJsonEquals(objList, anotherObjList);
        }

        if((obj instanceof Map) && (anotherObj instanceof Map)) {
            Map<String, Object> valueMap = (Map<String, Object>)obj;
            Map<String, Object> anotherValueMap = (Map<String, Object>)anotherObj;
            return isJsonEquals(valueMap, anotherValueMap);
        }

        return null != obj && obj.equals(anotherObj);
    }

    /**
     * Check two JSON objects are equal or not.<br>
     * 
     * @param jsonObj JSON object
     * @param jsonObjAnother JSON object
     * @return boolean, return true if they are equal, else false.
     * @since SDNO 0.5
     */
    public static boolean isJsonEquals(Map<String, Object> jsonObj, Map<String, Object> jsonObjAnother) {
        if(null == jsonObj || null == jsonObjAnother || jsonObj.size() != jsonObjAnother.size()) {
            return false;
        }

        Set<Map.Entry<String, Object>> entrySet = jsonObj.entrySet();
        for(Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if(!jsonObjAnother.containsKey(key)) {
                return false;
            }

            Object anotherValue = jsonObjAnother.get(key);
            if(!isJsonObjEquals(value, anotherValue)) {
                return false;
            }
        }

        return true;
    }

}
