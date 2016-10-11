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

package org.openo.sdno.mss.init.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mybatis management class, it call the mybatis'API to initialize the basic parameter of DB from
 * configure file and get SQL session from session factory. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class MybatisManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisManagement.class);

    private static final String MYBATIS_CONFIG_PATH = "META-INF/mybatis/config.xml";

    private static final String MYBAITS_PROPERTIES_PATH = "META-INF/mybatis/config.properties";

    private static final MybatisManagement INSTANCE = new MybatisManagement();

    private DBParam dbParam = null;

    private volatile SqlSessionFactory sqlSessionFac = null;

    private MybatisManagement() {

    }

    /**
     * Get a singleton of class. <br>
     * 
     * @since SDNO 0.5
     */
    public static MybatisManagement getInstance() {
        return INSTANCE;
    }

    /**
     * @param dbParam the DB parameter to set.
     * @since SDNO 0.5
     */
    public synchronized void setDbParam(DBParam dbParam) {
        this.dbParam = dbParam;
        this.getSqlSessionFac();
    }

    private Properties getDatasourceProperties(DBParam dbParam) throws IOException {
        Resources.setDefaultClassLoader(getClass().getClassLoader());

        Properties properties = Resources.getResourceAsProperties(MYBAITS_PROPERTIES_PATH);

        properties.setProperty("driver", dbParam.getDriver());
        properties.setProperty("url", dbParam.getUrl());
        properties.setProperty("name", dbParam.getDbUser());
        properties.setProperty("pwd", String.valueOf(dbParam.getDbPwd()));

        return properties;
    }

    /**
     * Get session for SQL. <br>
     * 
     * @since SDNO 0.5
     */
    public SqlSession getSqlSession() {
        SqlSession session = this.sqlSessionFac.openSession(true);
        LOGGER.info("session: {}", session);
        return session;
    }

    private SqlSessionFactory getSqlSessionFac() {
        if(null == this.sqlSessionFac) {
            synchronized(INSTANCE) {
                this.sqlSessionFac = getSqlSessionFac(this.dbParam);
            }
        }
        return this.sqlSessionFac;
    }

    private SqlSessionFactory getSqlSessionFac(DBParam dbParam) {
        SqlSessionFactory sqlSessionFactory = null;
        InputStream mybatisConfig = null;
        Properties mybatisProperties = null;
        try {
            Resources.setDefaultClassLoader(getClass().getClassLoader());

            mybatisConfig = Resources.getResourceAsStream(MYBATIS_CONFIG_PATH);

            mybatisProperties = getDatasourceProperties(this.dbParam);

            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            sqlSessionFactory = builder.build(mybatisConfig, mybatisProperties);

            destoryDBPwd();
        } catch(IOException e) {
            LOGGER.error("exception catched, e:", e);
            sqlSessionFactory = null;
        } finally {
            if(null != mybatisConfig) {
                try {
                    mybatisConfig.close();
                } catch(IOException e) {
                    LOGGER.error("IOException catched while close myabtisCfg", e);
                }
            }
        }

        return sqlSessionFactory;
    }

    /**
     * Clear the key in the memory. <br>
     * 
     * @since SDNO 0.5
     */
    public void destoryDBPwd() {
        this.dbParam.destroyPassword();
    }

}
