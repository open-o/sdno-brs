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

package org.openo.sdno.mss.dao.io;

import org.apache.ibatis.session.SqlSession;

import org.openo.sdno.mss.dao.util.intf.ParaCallable;

/**
 * Entity Class used to record commit content.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 23, 2016
 */
public class SqlSessionEntity {

    private ParaCallable<SqlSession, Integer> callable;

    private boolean isPoint = false;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param callable callback function
     */
    public SqlSessionEntity(ParaCallable<SqlSession, Integer> callable) {
        this.callable = callable;
    }

    /**
     * Set isPoint to be true.<br>
     * 
     * @since SDNO 0.5
     */
    public void addPoint() {
        this.isPoint = true;
    }

    /**
     * Get isPoint attribute.<br>
     * 
     * @return isPoint attribute
     * @since SDNO 0.5
     */
    public boolean isPoint() {
        return isPoint;
    }

    /**
     * Get callable attribute.<br>
     * 
     * @return callable attribute
     * @since SDNO 0.5
     */
    public ParaCallable<SqlSession, Integer> getCallable() {
        return callable;
    }

}
