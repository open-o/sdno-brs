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

package org.openo.sdno.brs.model.roamo;

/**
 * Enumeration class of result.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public enum EnumResult {
    SUCCESS("success", 0), FAIL("fail", 1);

    /**
     * result: success|fail|
     */
    private String name;

    /**
     * index start from 0.
     */
    private int index;

    /**
     * @param name name of result.
     * @param index value of result.
     */
    EnumResult(final String name, final int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Gets index.
     * 
     * @return Value of index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets name.
     * 
     * @return Value of name.
     */
    public String getName() {
        return name;
    }

}
