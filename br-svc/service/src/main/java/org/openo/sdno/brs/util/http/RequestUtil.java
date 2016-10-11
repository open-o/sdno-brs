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

package org.openo.sdno.brs.util.http;

import java.util.HashMap;
import java.util.Map;

import org.openo.sdno.brs.constant.Constant;

/**
 * Request utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class RequestUtil {

    private RequestUtil() {
    }

    /**
     * Convert query conditions into map structure.<br>
     * 
     * @param queryString query conditions
     * @return map query condition
     * @since SDNO 0.5
     */
    public static Map<String, String> getQueryStringMap(String queryString) {

        Map<String, String> queryMap = new HashMap<String, String>();
        String[] queryStrArray = queryString.split(Constant.AND);
        for(int i = 0; i < queryStrArray.length; i++) {
            String[] propertyStrArray = queryStrArray[i].split(Constant.EQUIVALENT);

            String propertyName = propertyStrArray[0];
            String propertyValue = null;
            if(propertyStrArray.length == 2) {
                propertyValue = propertyStrArray[1];
            }
            queryMap.put(propertyName, propertyValue);
        }
        return queryMap;
    }

}
