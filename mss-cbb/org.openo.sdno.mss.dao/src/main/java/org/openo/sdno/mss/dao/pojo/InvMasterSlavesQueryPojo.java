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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

/**
 * Generic data query. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvMasterSlavesQueryPojo {

    protected String tableName;

    protected String resType;

    protected String tableAlias;

    protected List<AttrEntity> attrsList = new ArrayList<AttrEntity>();

    protected List<String> attrsExtList = new ArrayList<String>();

    protected List<JoinEntity> joinResList = null;

    protected String filterSql;

    protected List<AttrEntity> orderFields = null;

    protected long fromIndex;

    protected long count;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType
     */
    public InvMasterSlavesQueryPojo(String resType) {
        this.tableName = "tbl_inv_" + resType;
    }

    /**
     * Get generic query data. <br>
     * 
     * @param session SQL session.
     * @return Query result.
     * @since SDNO 0.5
     */
    public List<Map<String, Object>> getCommQueryData(SqlSession session) {
        return session.selectList("msDataQuery", this);

    }

    /**
     * Get data quantity.<br>
     * 
     * @param session
     * @return
     * @since SDNO 0.5
     */
    public List<Object> getCommQueryDataCount(SqlSession session) {
        return session.selectList("msDataQueryCount", this);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getFilterSql() {
        return filterSql;
    }

    public void setFilterSql(String filterSql) {
        this.filterSql = filterSql;
    }

    public long getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(long fromIndex) {
        this.fromIndex = fromIndex;
    }

    public List<AttrEntity> getAttrsList() {
        return attrsList;
    }

    public void setAttrsList(List<AttrEntity> attrsList) {
        this.attrsList = attrsList;
    }

    public List<String> getAttrsExtList() {
        return attrsExtList;
    }

    public void setAttrsExtList(List<String> attrsExtList) {
        this.attrsExtList = attrsExtList;
    }

    public List<JoinEntity> getJoinResList() {
        return joinResList;
    }

    public void setJoinResList(List<JoinEntity> joinResList) {
        this.joinResList = joinResList;
    }

    public List<AttrEntity> getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(List<AttrEntity> orderFields) {
        this.orderFields = orderFields;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    /**
     * Attribute entity. <br>
     * 
     * @author
     * @version SDNO 0.5 2016-5-23
     */
    public class AttrEntity {

        private String tableAlias;

        private String attrName;

        private String attrAlias;

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getTableAlias() {
            return tableAlias;
        }

        public void setTableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
        }

        public String getAttrAlias() {
            return attrAlias;
        }

        public void setAttrAlias(String attrAlias) {
            this.attrAlias = attrAlias;
        }

    }

    /**
     * Join entity. <br>
     * 
     * @author
     * @version SDNO 0.5 2016-5-23
     */
    public class JoinEntity {

        private String joinType;

        private String tableName;

        private String tableAlias;

        private String key;

        private String masterKey;

        public String getJoinType() {
            return joinType;
        }

        public void setJoinType(String joinType) {
            this.joinType = joinType;
        }

        public String getTableAlias() {
            return tableAlias;
        }

        public void setTableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String resType) {
            this.tableName = "tbl_inv_" + resType;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMasterKey() {
            return masterKey;
        }

        public void setMasterKey(String masterKey) {
            this.masterKey = masterKey;
        }

    }
}
