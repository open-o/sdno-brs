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
 * This class defines several import modes.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public enum InvImportMode {

    /**
     * merge operation
     */
    MERGE(1),

    /**
     * insert operation
     */
    INSERT(2),

    /**
     * update operation
     */
    UPDATE(3),

    /**
     * delete operation
     */
    DELETE(4);

    /**
     * operation mode
     */
    private int importMode;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param importMode import mode
     */
    InvImportMode(int importMode) {
        this.importMode = importMode;
    }

    /**
     * Get importMode attribute.<br>
     * 
     * @return importMode attribute
     * @since SDNO 0.5
     */
    public int getImpotMode() {
        return importMode;
    }

    /**
     * Get InvImportMode instance by importMode.<br>
     * 
     * @param importMode
     * @return InvImportMode instance returned
     * @since SDNO 0.5
     */
    public static InvImportMode valueOf(int importMode) {
        InvImportMode[] values = InvImportMode.values();
        for(InvImportMode value : values) {
            if(value.getImpotMode() == importMode) {
                return value;
            }
        }

        throw new IllegalArgumentException("No such enum define of InvImportMode: " + importMode);
    }
}
