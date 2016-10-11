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
import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.schema.infomodel.Datatype;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvBasicTablePojoTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 28, 2016
 */
public class InvBasicTablePojoTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testParseSpecAttrTableRowWithNullAttrEntityList() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        Map<String, Object> valueMap = new HashMap<>();
        InvTableRowPojo result = inv.parseSpecAttrTableRow(valueMap);
        assertTrue(result.getAttrValueList().isEmpty());
    }

    @Test
    public void testParseSpecAttrTableRowWithMapContainsKey() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        inv.buildAttributes("ext");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("Model", "model1");
        InvTableRowPojo result = inv.parseSpecAttrTableRow(valueMap);
        assertEquals("model1", result.getAttrValueList().get(0));
    }

    @Test
    public void testParseSpecAttrTableRowWithKeyNotContained() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        inv.buildAttributes("ext");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("test", "model1");
        InvTableRowPojo result = inv.parseSpecAttrTableRow(valueMap);
        assertTrue(result.getAttrValueList().isEmpty());
    }

    @Test
    public void testBatchUpdateDataWithEmptyInput() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        PriSqlSession sql = new PriSqlSession();
        List<InvTableRowPojo> basicRecords = new ArrayList<>();
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");

        assertEquals(0, inv.batchUpdateData(sql, basicRecords));
    }

    @Test
    public void testBatchUpdateDataWithNullInput() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        PriSqlSession sql = new PriSqlSession();
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");

        assertEquals(0, inv.batchUpdateData(sql, null));
    }

    @Test
    public void testBatchUpdateDataWithNullTableRowList() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        new MockUp<PriSqlSession>() {

            @Mock
            public int insert(String statement, Object parameter) {

                return 1;
            }
        };

        PriSqlSession sql = new PriSqlSession();
        List<InvTableRowPojo> basicRecords = new ArrayList<>();
        basicRecords.add(new InvTableRowPojo());
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");

        assertEquals(1, inv.batchUpdateData(sql, basicRecords));
    }

    @Test
    public void testBatchUpdateDataWithTableRowListNotNull() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        new MockUp<PriSqlSession>() {

            @Mock
            public int insert(String statement, Object parameter) {

                return 1;
            }
        };

        PriSqlSession sql = new PriSqlSession();
        List<InvTableRowPojo> basicRecords = new ArrayList<>();
        basicRecords.add(new InvTableRowPojo());
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        inv.batchUpdateData(sql, basicRecords);

        assertEquals(1, inv.batchUpdateData(sql, basicRecords));
    }

    @Test
    public void testUpdateDataWithAbsentFalse() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        new MockUp<InvBasicTablePojo>() {

            @Mock
            public int getRowCnt(SqlSession session) {

                return 0;
            }
        };
        new MockUp<PriSqlSession>() {

            @Mock
            public int update(String statement, Object parameter) {

                return 1;
            }
        };
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        PriSqlSession sql = new PriSqlSession();
        inv.setAbsent(false);
        inv.buildAttributes("ext");
        assertEquals(1, inv.updateData(sql));
    }

    @Test
    public void testUpdateDataWithRowCntNotZero() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        new MockUp<InvBasicTablePojo>() {

            @Mock
            public int getRowCnt(SqlSession session) {

                return 1;
            }
        };
        new MockUp<PriSqlSession>() {

            @Mock
            public int update(String statement, Object parameter) {

                return 1;
            }
        };
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        PriSqlSession sql = new PriSqlSession();
        inv.setAbsent(true);
        inv.buildAttributes("ext");
        assertEquals(1, inv.updateData(sql));
    }

    @Test
    public void testUpdateData() {
        new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> result = new HashMap<>();

                result.put("Model", Datatype.STRING);
                return result;
            }
        };

        new MockUp<InvBasicTablePojo>() {

            @Mock
            public int getRowCnt(SqlSession session) {

                return 0;
            }
        };
        new MockUp<PriSqlSession>() {

            @Mock
            public int insert(String statement, Object parameter) {

                return 1;
            }
        };
        InvBasicTablePojo inv = new InvBasicTablePojo("resType");
        PriSqlSession sql = new PriSqlSession();
        inv.setAbsent(true);
        inv.buildAttributes("ext");
        assertEquals(1, inv.updateData(sql));
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
