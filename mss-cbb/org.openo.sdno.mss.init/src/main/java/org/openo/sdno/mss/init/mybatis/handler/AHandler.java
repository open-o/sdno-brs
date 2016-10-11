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

package org.openo.sdno.mss.init.mybatis.handler;

import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.init.mybatis.MybatisManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Father class of all the resource handle class,define the basic operation a handler do, including
 * getting SQL map, get,commit,close SQL session. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AHandler.class);

    private SqlSession sqlSession = null;

    protected <T> T getMapper(Class<T> clazz) {
        this.sqlSession = getSqlSession();
        if(null == sqlSession) {
            LOGGER.error("sqlSession is null");
            throw new IllegalArgumentException("sqlSession is null");
        }

        return sqlSession.getMapper(clazz);
    }

    protected void commit() {
        if(null != this.sqlSession) {
            this.sqlSession.commit();
        }
    }

    protected void close() {
        if(null != this.sqlSession) {
            this.sqlSession.close();
            this.sqlSession = null;
        }
    }

    protected SqlSession getSqlSession() {
        return MybatisManagement.getInstance().getSqlSession();
    }
}
