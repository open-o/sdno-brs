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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * DataSourceFactoryBean test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class DataSourceFactoryBeanTest {

    private DataSourceFactoryBean dataSourceBean = new DataSourceFactoryBean();

    private DataSource dataSource = new DataSource();

    private IDBInfo dbInfo = new DBInfo4OSSPl();

    @Test
    public void testDataSourceNull() throws Exception {
        dataSourceBean.setDataSource(null);
        assertNull(dataSourceBean.getObject());
    }

    @Test
    public void testDBInfoNull() throws Exception {
        dataSourceBean.setDataSource(dataSource);
        dataSourceBean.setDbInfo(null);
        assertEquals(dataSource, dataSourceBean.getObject());
    }

    @Test
    public void testGetObject() throws Exception {
        dataSourceBean.setDataSource(dataSource);
        dataSourceBean.setDbInfo(dbInfo);
        assertEquals(dataSource, dataSourceBean.getObject());
    }

}
