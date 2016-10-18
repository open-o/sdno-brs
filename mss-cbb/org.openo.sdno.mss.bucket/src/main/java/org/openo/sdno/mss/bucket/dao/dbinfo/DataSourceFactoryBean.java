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

package org.openo.sdno.mss.bucket.dao.dbinfo;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory class for data source.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class DataSourceFactoryBean implements FactoryBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFactoryBean.class);

    private IDBInfo dbInfo = new DBInfo4OSSPl();

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Get the data source after setting the basic attributes.<br>
     * 
     * @return Data source
     * @throws Exception if set driver class failed.
     * @since SDNO 0.5
     */
    @Override
    public javax.sql.DataSource getObject() throws Exception {
        if(null == this.dataSource) {
            LOGGER.error("dataSource is null");
            return this.dataSource;
        }

        if(null == this.dbInfo) {
            LOGGER.error("dbInfo is null");
            return this.dataSource;
        }

        dbInfo.init();

        String str = this.dbInfo.getDriver();
        if(StringUtils.isEmpty(str)) {
            LOGGER.error("Driver is empty");
            return this.dataSource;
        }
        this.dataSource.setDriverClassName(str);

        str = this.dbInfo.getUrl();
        if(StringUtils.isEmpty(str)) {
            LOGGER.error("Url is empty");
            return this.dataSource;
        }
        this.dataSource.setUrl(str);

        str = this.dbInfo.getUser();
        if(StringUtils.isEmpty(str)) {
            LOGGER.error("User is empty");
            return this.dataSource;
        }
        this.dataSource.setUsername(str);

        String pwd = dbInfo.getPassword();
        if(null == pwd) {
            LOGGER.error("pwd is null");
            return this.dataSource;
        }
        this.dataSource.setPassword(pwd);
        pwd = null;

        this.dbInfo.destroyPassword();

        return this.dataSource;
    }

    public void setDbInfo(IDBInfo dbinfo) {
        this.dbInfo = dbinfo;
    }

    /**
     * @return Returns the dbInfo.
     */
    public IDBInfo getDbInfo() {
        return dbInfo;
    }

    /**
     * Get data source type.<br>
     * 
     * @return Data source type
     * @since SDNO 0.5
     */
    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    /**
     * Check the object is singleton or not.<br>
     * 
     * @return True if is singleton, otherwise return false.
     * @since SDNO 0.5
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

}
