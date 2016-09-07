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

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json utility class, have some regular method about json. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    /**
     * Turn a object into a json style string. <br/>
     * 
     * @param obj the object need to change.
     * @return json string changed from object.
     * @since SDNO 0.5
     */
    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch(IOException ex) {
            LOGGER.error("Parser to json error.", ex);
            throw new IllegalArgumentException("Parser obj to json error, obj = " + obj, ex);
        }
    }

    /**
     * Turn a json string in to a java object. <br/>
     * 
     * @param jsonStr the json string need to change.
     * @param objClass the java class json string represent.
     * @return the java object parsed from json string.
     * @since SDNO 0.5
     */
    public static <T> T fromJson(String jsonStr, Class<T> objClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonStr, objClass);
        } catch(IOException ex) {
            LOGGER.error("Parser to object error.", ex);
            throw new IllegalArgumentException("Parser json to object error, json = " + jsonStr + ", expect class = "
                    + objClass, ex);
        }
    }

    /**
     * Turn a json string in to a java object. <br/>
     * 
     * @param jsonStr the json string need to change.
     * @param typeRef mapping json string to the java class it represent.
     * @return the java object parsed from json string.
     * @since SDNO 0.5
     */
    public static <T> T fromJson(String jsonStr, TypeReference<T> typeRef) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonStr, typeRef);
        } catch(IOException ex) {
            LOGGER.error("Parser to object error.", ex);
            throw new IllegalArgumentException("Parser json to object error, json = " + jsonStr + ", expect type = "
                    + typeRef.getType(), ex);
        }
    }

    /**
     * Turn a json file in to a java object. <br/>
     * 
     * @param file the json file need to change.
     * @param objClass the java class json string represent.
     * @return the java object parsed from json string.
     * @since SDNO 0.5
     */
    public static <T> T fromJson(File file, Class<T> objClass) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(file, objClass);
        } catch(IOException ex) {
            LOGGER.error("Parser to object error.", ex);
            throw new IllegalArgumentException("Parser json to object error, file = " + file.getName()
                    + ", expect class = " + objClass, ex);
        }
    }
}
