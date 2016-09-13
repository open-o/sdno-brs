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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * Inventory master slaver query PoJo. <br>>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class InvRelationCombinePojo extends InvMasterSlavesQueryPojo {

    /**
     * Relation table name.
     */
    private String relationTableName;

    /**
     * Relation table name alias.
     */
    private String relationTableNameAlias;

    /**
     * Relation filter condition.
     */
    private String relationFilter;

    /**
     * Create temporary table based on relation table.
     */
    private String relationTmpTableSql;

    /**
     * Relation table temporary table alias.
     */
    private String relationTempTableAlias;

    private static final String MASTER_KEY = "uuid";

    private static final String RELATION_KEY = "src_uuid";

    private List<String> attrsLinkList = new ArrayList<String>();

    private static Map<String, Datatype> relationModel;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvRelationCombinePojo(String resType) {
        super(resType);
        this.resType = resType;
        this.relationTableName = this.tableName + PropertiesUtil.getInstance().getRELATIONTABLEPOSTFIX();
    }

    @Override
    public List<Map<String, Object>> getCommQueryData(SqlSession session) {
        return session.selectList("msRelationDataQuery", this);

    }

    @Override
    public List<Object> getCommQueryDataCount(SqlSession session) {
        return session.selectList("msRelationDataQueryCount", this);
    }

    public String getRelationTableName() {
        return relationTableName;
    }

    public void setRelationTableName(String relationTableName) {
        this.relationTableName = relationTableName;
    }

    public String getRelationTableNameAlias() {
        return relationTableNameAlias;
    }

    public void setRelationTableNameAlias(String relationTableNameAlias) {
        this.relationTableNameAlias = relationTableNameAlias;
        setRelationTempTableAlias(relationTableNameAlias + "_tmp");
    }

    public String getRelationTempTableAlias() {
        return relationTempTableAlias;
    }

    public void setRelationTempTableAlias(String relationTempTableAlias) {
        this.relationTempTableAlias = relationTempTableAlias;
    }

    public String getRelationFilter() {
        return relationFilter;
    }

    /**
     * Set relation table filter. <br>
     * 
     * @param relationFilter
     * @since SDNO 0.5
     */
    public void setRelationFilter(String relationFilter) {
        if(StringUtils.isEmpty(relationFilter)) {
            return;
        }

        this.relationFilter = relationFilter;

        this.relationTmpTableSql =
                new StringBuffer().append("(select distinct ").append(getRelationKey()).append(" from ")
                        .append(relationTableName).append(" ").append(relationTableNameAlias).append(" where ")
                        .append(relationFilter).append(")").toString();
    }

    public String getRelationTmpTableSql() {
        return relationTmpTableSql;
    }

    public void setRelationTmpTableSql(String relationTmpTableSql) {
        this.relationTmpTableSql = relationTmpTableSql;
    }

    public List<String> getAttrsLinkList() {
        return attrsLinkList;
    }

    public void setAttrsLinkList(List<String> attrsLinkList) {
        this.attrsLinkList = attrsLinkList;
    }

    public String getMasterKey() {
        return MASTER_KEY;
    }

    public String getRelationKey() {
        return RELATION_KEY;
    }

    public static Map<String, Datatype> getRelationModel() {
        if(relationModel == null) {
            initRelation();
        }

        return relationModel;
    }

    private static void initRelation() {
        Map<String, Datatype> model = new HashMap<String, Datatype>();
        String[] columnObjs = PropertiesUtil.getInstance().getRELATIONTABLECOLUMN();
        for(String columnObj : columnObjs) {
            String[] column = StringUtils.split(columnObj, ", ");
            model.put(column[0], Datatype.fromValue(column[1]));
        }

        relationModel = model;
    }
}
