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

package org.openo.sdno.mss.dao.pojo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

/**
 * Functions for database operation. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface IInvTableDataCrud {

    /**
     * Insert data to database. <br>
     * 
     * @param session SQL session.
     * @return Status code of operation.
     * @since SDNO 0.5
     */
    int addData(SqlSession session);

    /**
     * Delete data. <br>
     * 
     * @param session SQL session
     * @return Status code of operation.
     * @since SDNO 0.5
     */
    int removeData(SqlSession session);

    /**
     * Batch deleting of the data. <br>
     * 
     * @param session SQL session.
     * @return Status code of operation.
     * @since SDNO 0.5
     */
    int batchDelete(SqlSession session);

    /**
     * Update. <br>
     * 
     * @param session SQL session.
     * @return Status code of operation.
     * @since SDNO 0.5
     */
    int updateData(SqlSession session);

    /**
     * Get data from database. <br>
     * 
     * @param session SQL session.
     * @return
     * @since SDNO 0.5
     */
    List<Map<String, Object>> getData(SqlSession session);
}
