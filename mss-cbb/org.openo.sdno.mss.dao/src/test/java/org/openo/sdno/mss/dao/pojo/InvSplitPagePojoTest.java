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
import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Basic;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvSplitPagePojo test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class InvSplitPagePojoTest {

    InvBasicTablePojo invBasicTablePojo;

    InvSplitPagePojo invSplitPagePojo;

    List<Property> basicProp;

    @Before
    public void setUp() throws Exception {
        basicProp = new ArrayList<Property>();
        for(int i = 0; i < 10; i++) {
            Property property = new Property();
            property.setType(Datatype.STRING);
            property.setName("name" + i);
            basicProp.add(property);
        }

        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getWholeInfoModelMap() {
                HashMap<String, Infomodel> map = new HashMap<String, Infomodel>();
                Infomodel infomodel = new Infomodel();
                infomodel.setBasic(new Basic());
                map.put("test", infomodel);
                return map;
            }

            @Mock
            public String getUniqueAttr(String resType) {

                return "UniqueAttr";
            }
        };

        new MockUp<Basic>() {

            @Mock
            public List<Property> getProperty() {

                return basicProp;
            }
        };
    }

    @Test
    public void testGetDataWithEmptyRst() {

        new MockUp<PriSqlSession>() {

            @Mock
            public <E> List<E> selectList(String statement, Object parameter) {
                List<Map<String, Object>> list = new ArrayList<>();
                return (List<E>)list;
            }
        };
        new MockUp<InvTypeConvertor>() {

            @Mock
            public Object convert(String resType, String attrName, Object attrValue) {

                return "value";
            }
        };
        invBasicTablePojo = new InvBasicTablePojo("test");
        invSplitPagePojo = new InvSplitPagePojo(invBasicTablePojo);
        invSplitPagePojo.buildSortAttr("sortAttrName", "refValue", true);
        invSplitPagePojo.buildUniqueAttr("uniqueValue");
        assertEquals(0, invSplitPagePojo.getData(new PriSqlSession()).size());
    }

    @Test
    public void testGetDataWithNullSortAttr() {

        new MockUp<PriSqlSession>() {

            @Mock
            public <E> List<E> selectList(String statement, Object parameter) {
                List<Map<String, Object>> list = new ArrayList<>();
                return (List<E>)list;
            }
        };
        new MockUp<InvTypeConvertor>() {

            @Mock
            public Object convert(String resType, String attrName, Object attrValue) {

                return "value";
            }
        };
        invBasicTablePojo = new InvBasicTablePojo("test");
        invSplitPagePojo = new InvSplitPagePojo(invBasicTablePojo);
        invSplitPagePojo.buildSortAttr("sortAttrName", "refValue", true);

        assertEquals(0, invSplitPagePojo.getData(new PriSqlSession()).size());
    }

    @Test
    public void testGetDataWithNullUniqueAttr() {

        new MockUp<PriSqlSession>() {

            @Mock
            public <E> List<E> selectList(String statement, Object parameter) {
                List<Map<String, Object>> list = new ArrayList<>();
                return (List<E>)list;
            }
        };
        new MockUp<InvTypeConvertor>() {

            @Mock
            public Object convert(String resType, String attrName, Object attrValue) {

                return "value";
            }
        };
        invBasicTablePojo = new InvBasicTablePojo("test");
        invSplitPagePojo = new InvSplitPagePojo(invBasicTablePojo);

        invSplitPagePojo.buildUniqueAttr("uniqueValue");
        assertEquals(0, invSplitPagePojo.getData(new PriSqlSession()).size());
    }

    @Test
    public void testGetDataWithNullSortAttrValue() {

        new MockUp<PriSqlSession>() {

            @Mock
            public <E> List<E> selectList(String statement, Object parameter) {
                List<Map<String, Object>> list = new ArrayList<>();
                list.add(new HashMap<String, Object>());
                return (List<E>)list;
            }
        };
        new MockUp<InvTypeConvertor>() {

            @Mock
            public Object convert(String resType, String attrName, Object attrValue) {

                return "value";
            }
        };
        invBasicTablePojo = new InvBasicTablePojo("test");
        invSplitPagePojo = new InvSplitPagePojo(invBasicTablePojo);
        invSplitPagePojo.buildSortAttr("sortAttrName", null, true);
        invSplitPagePojo.buildUniqueAttr("uniqueValue");
        assertEquals(2, invSplitPagePojo.getData(new PriSqlSession()).size());
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
