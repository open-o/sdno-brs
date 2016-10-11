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
 * This class defines some basic inventory attributes.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public enum InvAttrDefine {

    /**
     * UUID
     */
    UUID("uuid"),

    /**
     * create time
     */
    CREATE_TIME("createtime"),

    /**
     * update time
     */
    UPDATE_TIME("updatetime"),

    /**
     * device UUID
     */
    EMS_UUID("emsuuid");

    private String value;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param value
     */
    InvAttrDefine(String value) {
        this.value = value;
    }

    /**
     * Get value attribute.<br>
     * 
     * @return value attribute
     * @since SDNO 0.5
     */
    public String getValue() {
        return value;
    }
}
