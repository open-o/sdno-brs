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

package org.openo.sdno.mss.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.openo.sdno.mss.dao.multi.CustomerContextHolder;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;

/**
 * The abstract class extend from SqlSessionDaoSupport. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public abstract class AbstractHandlerImpl extends SqlSessionDaoSupport {

    /**
     * It's used to switch data source dynamically when get SQL session. <br>
     * 
     * @return The object of SqlSession.
     * @since SDNO 0.5
     */
    @Override
    public SqlSession getSqlSession() {
        SqlSession sqlSession = null;

        String ds = DataSourceCtrler.get();

        CustomerContextHolder.setCustomerType(ds);

        sqlSession = super.getSqlSession();

        return sqlSession;
    }
}
