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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Basic;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvTempAttrFilterPojoTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 27, 2016
 */
public class InvTempAttrFilterPojoTest {

    @Test
    public void testGetTableName() {
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getWholeInfoModelMap() {
                Map<String, Infomodel> result = new HashMap<>();
                Infomodel model = new Infomodel();
                Basic basic = new Basic();
                model.setBasic(basic);
                result.put("Model", model);
                return result;

            }
        };

        InvTempAttrFilterPojo inv = new InvTempAttrFilterPojo("attrName", "Model");

        assertTrue(inv.getTableName().contains("tmp_tbl_"));
    }

    @Test
    public void testGetAttrProperty() {
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getWholeInfoModelMap() {
                Map<String, Infomodel> result = new HashMap<>();
                Infomodel model = new Infomodel();
                Basic basic = new Basic();
                model.setBasic(basic);
                result.put("Model", model);
                return result;

            }
        };

        Property pro = new Property();
        pro.setName("porperty1");
        pro.setType(Datatype.INTEGER);
        InvTempAttrFilterPojo inv = new InvTempAttrFilterPojo("attrName", pro);

        assertEquals("porperty1", inv.getAttrProperty().getAttrName());
        assertEquals("int", inv.getAttrProperty().getAttrType());
    }

    @Test
    public void testToString() {
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getWholeInfoModelMap() {
                Map<String, Infomodel> result = new HashMap<>();
                Infomodel model = new Infomodel();
                Basic basic = new Basic();
                model.setBasic(basic);
                result.put("Model", model);
                return result;

            }
        };

        InvTempAttrFilterPojo inv = new InvTempAttrFilterPojo("attrName", "Model");

        assertTrue(inv.toString().contains("tableName"));
    }

    @Test
    public void addData() {
        new MockUp<SqlSession>() {

            @Mock
            public int insert(String statement, Object parameter) {

                return 0;
            }
        };

        Property pro = new Property();
        pro.setName("porperty1");
        pro.setType(Datatype.INTEGER);
        InvTempAttrFilterPojo inv = new InvTempAttrFilterPojo("attrName", pro);
        PriSqlSession sql = new PriSqlSession();
        assertEquals(0, inv.addData(sql));
    }

    @Test
    public void batchAddInputEmpty() {
        new MockUp<SqlSession>() {

            @Mock
            public int insert(String statement, Object parameter) {

                return 0;
            }
        };

        Property pro = new Property();
        pro.setName("porperty1");
        pro.setType(Datatype.INTEGER);
        InvTempAttrFilterPojo inv = new InvTempAttrFilterPojo("attrName", pro);
        PriSqlSession sql = new PriSqlSession();
        List<Object> attrValueList = new ArrayList<>();
        inv.batchAdd(sql, attrValueList);
    }

    private class PriSqlSession implements SqlSession {

        /**
         * <br>
         * 
         * @param statement
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <T> T selectOne(String statement) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <T> T selectOne(String statement, Object parameter) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <E> List<E> selectList(String statement) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <E> List<E> selectList(String statement, Object parameter) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @param rowBounds
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param mapKey
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <K, V> Map<K, V> selectMap(String statement, String mapKey) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @param mapKey
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @param mapKey
         * @param rowBounds
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {

            return null;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @param handler
         * @since SDNO 0.5
         */
        @Override
        public void select(String statement, Object parameter, ResultHandler handler) {

        }

        /**
         * <br>
         * 
         * @param statement
         * @param handler
         * @since SDNO 0.5
         */
        @Override
        public void select(String statement, ResultHandler handler) {

        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @param rowBounds
         * @param handler
         * @since SDNO 0.5
         */
        @Override
        public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {

        }

        /**
         * <br>
         * 
         * @param statement
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int insert(String statement) {

            return 0;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int insert(String statement, Object parameter) {

            return 0;
        }

        /**
         * <br>
         * 
         * @param statement
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int update(String statement) {

            return 0;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int update(String statement, Object parameter) {

            return 0;
        }

        /**
         * <br>
         * 
         * @param statement
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int delete(String statement) {

            return 0;
        }

        /**
         * <br>
         * 
         * @param statement
         * @param parameter
         * @return
         * @since SDNO 0.5
         */
        @Override
        public int delete(String statement, Object parameter) {

            return 0;
        }

        /**
         * <br>
         * 
         * @since SDNO 0.5
         */
        @Override
        public void commit() {

        }

        /**
         * <br>
         * 
         * @param force
         * @since SDNO 0.5
         */
        @Override
        public void commit(boolean force) {

        }

        /**
         * <br>
         * 
         * @since SDNO 0.5
         */
        @Override
        public void rollback() {

        }

        /**
         * <br>
         * 
         * @param force
         * @since SDNO 0.5
         */
        @Override
        public void rollback(boolean force) {

        }

        /**
         * <br>
         * 
         * @return
         * @since SDNO 0.5
         */
        @Override
        public List<BatchResult> flushStatements() {

            return null;
        }

        /**
         * <br>
         * 
         * @since SDNO 0.5
         */
        @Override
        public void close() {

        }

        /**
         * <br>
         * 
         * @since SDNO 0.5
         */
        @Override
        public void clearCache() {

        }

        /**
         * <br>
         * 
         * @return
         * @since SDNO 0.5
         */
        @Override
        public Configuration getConfiguration() {

            return null;
        }

        /**
         * <br>
         * 
         * @param type
         * @return
         * @since SDNO 0.5
         */
        @Override
        public <T> T getMapper(Class<T> type) {

            return null;
        }

        /**
         * <br>
         * 
         * @return
         * @since SDNO 0.5
         */
        @Override
        public Connection getConnection() {

            return null;
        }

    }
}
