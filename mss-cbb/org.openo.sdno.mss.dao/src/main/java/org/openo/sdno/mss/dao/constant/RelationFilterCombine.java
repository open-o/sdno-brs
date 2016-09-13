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

package org.openo.sdno.mss.dao.constant;

/**
 * This class defines several RelationFilterCombine types.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public enum RelationFilterCombine {
    /**
     * intersection
     */
    INTERSECT("intersect"),

    /**
     * union
     */
    UNION("union"),

    /**
     * minus
     */
    MINUS("minus"),

    /**
     * default
     */
    DEFAULT("");

    private String type;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param type RelationFilterCombine type
     */
    RelationFilterCombine(String type) {
        this.type = type;
    }

    /**
     * Get RelationFilterCombine instance by type.<br>
     * 
     * @param type RelationFilterCombine type
     * @return RelationFilterCombine instance returned, and DEFAULT if not matched
     * @since SDNO 0.5
     */
    public static RelationFilterCombine getType(String type) {
        for(RelationFilterCombine r : RelationFilterCombine.values()) {
            if(r.type.equalsIgnoreCase(type)) {
                return r;
            }
        }

        return DEFAULT;
    }
}
