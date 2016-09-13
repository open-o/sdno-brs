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

import java.util.concurrent.atomic.AtomicReference;

import org.openo.sdno.mss.dao.multi.DBInfo4OSSPlatForm;

/**
 * This class defines two types of database.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public enum InvDbType {
    SQLSERVER, SYBASE;

    /**
     * database type
     */
    private static AtomicReference<InvDbType> dbTypeRef = new AtomicReference<InvDbType>();

    /**
     * Get Database type of current connection.<br>
     * 
     * @return Database type of current Connection
     * @since SDNO 0.5
     */
    public static InvDbType get() {
        InvDbType type = dbTypeRef.get();
        if(type != null) {
            return type;
        }

        type = DBInfo4OSSPlatForm.getType();
        dbTypeRef.compareAndSet(null, type);

        return dbTypeRef.get();
    }
}
