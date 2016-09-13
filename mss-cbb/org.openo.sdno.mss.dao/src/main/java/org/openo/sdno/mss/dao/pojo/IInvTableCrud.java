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

import org.apache.ibatis.session.SqlSession;

/**
 * Functions for database table Operations. <br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface IInvTableCrud {

    /**
     * Create a table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    void createTable(SqlSession session);

    /**
     * Delete table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    void removeTable(SqlSession session);
}
