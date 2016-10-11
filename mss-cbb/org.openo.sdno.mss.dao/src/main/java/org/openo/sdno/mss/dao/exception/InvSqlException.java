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

package org.openo.sdno.mss.dao.exception;

import org.apache.ibatis.exceptions.PersistenceException;
import org.openo.sdno.mss.dao.constant.InvSqlState;

/**
 * Inventory SQL Exception.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
@SuppressWarnings("serial")
public class InvSqlException extends RuntimeException {

    /**
     * sqlState
     */
    private InvSqlState sqlState;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param ex Persistence Exception
     * @param sqlState sqlState
     */
    public InvSqlException(PersistenceException ex, InvSqlState sqlState) {
        super(ex);
        this.sqlState = sqlState;
    }

    /**
     * Get sqlState attribute.<br>
     * 
     * @return sqlState attribute
     * @since SDNO 0.5
     */
    public InvSqlState getSqlState() {
        return sqlState;
    }
}
