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
import static org.junit.Assert.assertNull;
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
import org.openo.sdno.mss.dao.entities.InvRelationEntity;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvRelationTablePojoTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 28, 2016
 */
public class InvRelationTablePojoTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSetRealFilter() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        inv.setRealFilter("filter");
        assertEquals("filter", inv.getFilter());

        inv.setRealFilter("");
        assertEquals("filter", inv.getFilter());

        inv.setRealFilter("filter2");
        assertEquals("filter and filter2", inv.getFilter());
    }

    @Test
    public void testSetFromRow() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        InvRelationEntity ire = new InvRelationEntity();
        ire.setDstType("dstType");
        ire.setDstUuid("dstUuid");
        ire.setSrcUuid("srcUuid");
        ire.setRelation("relation");
        ire.setServiceType("serviceType");

        inv.setFromRow(ire);
        assertEquals("dstType", inv.getFromRow().getDstType());
        assertEquals("dstUuid", inv.getFromRow().getDstUuid());
        assertEquals("srcUuid", inv.getFromRow().getSrcUuid());
        assertEquals("relation", inv.getFromRow().getRelation());
        assertEquals("serviceType", inv.getFromRow().getServiceType());

        inv.setFromRow(null);
        assertEquals("dstType", inv.getFromRow().getDstType());
        assertEquals("dstUuid", inv.getFromRow().getDstUuid());
        assertEquals("srcUuid", inv.getFromRow().getSrcUuid());
        assertEquals("relation", inv.getFromRow().getRelation());
        assertEquals("serviceType", inv.getFromRow().getServiceType());
    }

    @Test
    public void testBuildUuidFilter() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<String> uuids = new ArrayList<>();
        inv.buildUuidFilter(uuids);
        inv.getFilter();
        assertEquals("src_uuid in ()", inv.getFilter());

        uuids.add("123456");
        inv.buildUuidFilter(uuids);
        inv.getFilter();
        assertEquals("src_uuid in () and src_uuid in ('123456')", inv.getFilter());

        inv.setRealFilter("filter");
        inv.buildUuidFilter(uuids);
        inv.getFilter();
        assertEquals("src_uuid in () and src_uuid in ('123456') and filter and src_uuid in ('123456')",
                inv.getFilter());
    }

    @Test
    public void testBuildServiceTypeFilter() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");

        inv.buildServiceTypeFilter("serviceType");
        assertEquals("tbl_inv_resType_links.servicetype = 'serviceType'", inv.getFilter());

        inv.buildServiceTypeFilter(null);
        assertEquals("tbl_inv_resType_links.servicetype = 'serviceType'", inv.getFilter());

        inv.buildServiceTypeFilter("");
        assertEquals("tbl_inv_resType_links.servicetype = 'serviceType' and tbl_inv_resType_links.servicetype = ''",
                inv.getFilter());
    }

    @Test
    public void testBuildDstTypeSetFilter() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<String> dstTypeList = new ArrayList<>();
        dstTypeList.add("type1");
        inv.buildDstTypeSetFilter(dstTypeList);
        assertEquals("tbl_inv_resType_links.dst_type in ('type1')", inv.getFilter());

        inv.buildDstTypeSetFilter(null);
        assertEquals("tbl_inv_resType_links.dst_type in ('type1')", inv.getFilter());

    }

    @Test
    public void testBuildDstTypeSetFilterWithEmptyInput() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");

        List<String> emptyList = new ArrayList<>();
        inv.buildDstTypeSetFilter(emptyList);
        assertNull(inv.getFilter());
    }

    @Test
    public void testBuildDstTypeSetFilterWithOtherFilter() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<String> uuids = new ArrayList<>();
        uuids.add("123456");
        inv.buildUuidFilter(uuids);

        List<String> dstTypeList1 = new ArrayList<>();
        dstTypeList1.add("type1");

        inv.buildDstTypeSetFilter(dstTypeList1);

        assertEquals("src_uuid in ('123456') and tbl_inv_resType_links.dst_type in ('type1')", inv.getFilter());

    }

    @Test
    public void testParseTableRow() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("src_uuid", "123");
        valueMap.put("dst_uuid", "456");
        valueMap.put("relation", "composition");

        InvTableRowPojo result = inv.parseTableRow(valueMap);
        assertTrue(result.getAttrValueList().isEmpty());
    }

    @Test
    public void testBulkInsertDataWithEmptyInput() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<InvTableRowPojo> records = new ArrayList<>();
        assertEquals(0, inv.bulkInsertData(new PriSqlSession(), records));
    }

    @Test
    public void testBulkInsertDataWithNullInput() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<InvTableRowPojo> records = new ArrayList<>();
        assertEquals(0, inv.bulkInsertData(new PriSqlSession(), null));
    }

    @Test
    public void testBulkInsertData() {
        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        List<InvTableRowPojo> records = new ArrayList<>();
        records.add(new InvTableRowPojo());
        assertEquals(1, inv.bulkInsertData(new PriSqlSession(), records));
    }

    @Test
    public void testAddDataWithEmptyAttrEntityList() {
        new MockUp<InvRelationTablePojo>() {

            @Mock
            public void checkValueMap(Map<String, Object> valueMap) {

                return;
            }
        };

        InvRelationTablePojo inv = new InvRelationTablePojo("resType");
        Map<String, Object> valueMap = new HashMap<>();
        inv.buildValue(valueMap);
        assertEquals(-1, inv.addData(new PriSqlSession()));
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

            return 1;
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
