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

import java.util.Set;

import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * Filter condition handler . <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class FilterUtil {

    private FilterUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Build range filter condition. <br>
     * 
     * @param field Field
     * @param dt data type
     * @param propSet Prop set
     * @return Range filter condition string.
     * @since SDNO 0.5
     */
    public static <T> String buildInFilterString(String field, Datatype dt, Set<T> propSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(field).append("\":[");

        boolean isFirst = true;
        for(T prop : propSet) {
            if(!isFirst) {
                sb.append(',');
            }
            isFirst = false;
            if(dt.equals(Datatype.STRING)) {
                sb.append('\"').append(prop).append('\"');
            } else {
                sb.append(prop);
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}
