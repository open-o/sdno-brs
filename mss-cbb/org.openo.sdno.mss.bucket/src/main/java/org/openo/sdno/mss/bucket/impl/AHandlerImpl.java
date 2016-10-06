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

package org.openo.sdno.mss.bucket.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for mapper handler.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public abstract class AHandlerImpl extends SqlSessionDaoSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AHandlerImpl.class);

    /**
     * Get mapper of specified class.<br>
     * 
     * @param clazz Class
     * @return Mapper
     * @since SDNO 0.5
     */
    protected <T> T getMapper(Class<T> clazz) {
        SqlSession sqlSession = getSqlSession();
        if(null == sqlSession) {
            LOGGER.error("sqlSession is null");
            throw new IllegalArgumentException("sqlSession is null");
        }

        return sqlSession.getMapper(clazz);
    }
}
