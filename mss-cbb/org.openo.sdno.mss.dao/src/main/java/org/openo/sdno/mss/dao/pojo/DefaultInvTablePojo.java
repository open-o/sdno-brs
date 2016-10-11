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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.exception.MethodNotSupportException;
import org.openo.sdno.mss.dao.filter.InvAttrSqlFilter;
import org.openo.sdno.mss.dao.filter.InvSqlFilterParser;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * Default inventory table.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class DefaultInvTablePojo implements IInvTableDataCrud {

    /**
     * Resource type
     */
    protected String resType;

    /**
     * Table name
     */
    protected String tableName;

    protected String uuid;

    /**
     * Filter condition
     */
    protected String filter;

    /**
     * Filter condition
     */
    protected String filterEx;

    /**
     * Filter condition JSON
     */
    private String filterJson;

    /**
     * Filter description
     */
    protected String filterDesc;

    /**
     * If the filter condition is more than 100, create a temporary table. filter in the temporary
     * table.
     */
    protected List<InvTempAttrFilterPojo> filterAttrList;

    /**
     * All attributes of this resource.
     */
    protected Map<String, Datatype> allAttrMap;

    /**
     * UUID type of which will be deleted, src_uuid or dst_uuid
     */
    protected String delUuidType;

    /**
     * Source UUIDs of deleting, using for batch operation.
     */
    protected Set<String> preDeleteUuidSet;

    /**
     * This SQL session is a little special , using for single query. Can not be submitted alone in
     * batch operation.
     */
    protected SqlSession querySession;

    protected String emptyCheckColumn = "uuid";

    /**
     * Constructor<br>
     * <p>
     * Protected constructor, only can be instanced in son classes.
     * </p>
     * 
     * @since SDNO 0.5
     */
    protected DefaultInvTablePojo() {
    }

    /**
     * Constructor<br>
     * <p>
     * Protected constructor, only can be instanced in son classes.
     * </p>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    protected DefaultInvTablePojo(String resType) {
        this.resType = resType;
    }

    public String getEmptyCheckColumn() {
        return emptyCheckColumn;
    }

    public Map<String, Datatype> getAllAttrMap() {
        return allAttrMap;
    }

    public String getDelUuidType() {
        return delUuidType;
    }

    public void setDelUuidType(String delUuidType) {
        this.delUuidType = delUuidType;
    }

    public Set<String> getPreDeleteUuidSet() {
        return preDeleteUuidSet;
    }

    public void setPreDeleteUuidSet(Set<String> preDeleteUuidSet) {
        this.preDeleteUuidSet = preDeleteUuidSet;
    }

    /**
     * Set filter conditions. <br>
     * 
     * @param filterJson Filter condition JSON.
     * @since SDNO 0.5
     */
    public void setFilter(String filterJson) {
        this.filterJson = StringUtils.isEmpty(filterJson) ? null : filterJson;
    }

    public void setFilterEx(String filterEx) {
        this.filterEx = StringUtils.isEmpty(filterEx) ? null : filterEx;
    }

    public void setFilterDesc(String filterDesc) {
        this.filterDesc = StringUtils.isEmpty(filterDesc) ? null : filterDesc;
    }

    /**
     * Create temporary table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    public void createTempAttrTable(SqlSession session) {
        if(null == filterEx) {
            if(null == filterJson) {
                return;
            }

            if(filterDesc == null) {
                filterDesc = ModelMgrUtil.getInstance().getFilterDesc(getResType(), null);
            }

            InvSqlFilterParser parser = new InvSqlFilterParser(session, getResType());
            InvAttrSqlFilter sqlFilter = parser.buildSqlFilter(filterDesc, filterJson);
            filter = StringUtils.isEmpty(sqlFilter.getWhereFilter()) ? null : sqlFilter.getWhereFilter();
            if(sqlFilter.getPojo() != null && !sqlFilter.getPojo().isEmpty()) {
                filterAttrList = sqlFilter.getPojo();
            }
        } else {
            filter = filterEx;
        }
    }

    public List<InvTempAttrFilterPojo> getFilterAttrList() {
        return filterAttrList;
    }

    /**
     * Delete temporary table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    public void removeTempAttrTable(SqlSession session) {
        List<InvTempAttrFilterPojo> pojos = getFilterAttrList();
        if(pojos == null || pojos.isEmpty()) {
            return;
        }

        for(InvTempAttrFilterPojo pojo : pojos) {
            pojo.removeTable(session);
        }
    }

    public void setQuerySession(SqlSession querySession) {
        this.querySession = querySession;
    }

    /**
     * Get row count. <br>
     * 
     * @param session SQL session.
     * @return
     * @since SDNO 0.5
     */
    public int getRowCnt(SqlSession session) {
        @SuppressWarnings("resource")
        SqlSession sqlSession = querySession == null ? session : querySession;
        return (Integer)sqlSession.selectOne("getRowCnt", this);
    }

    /**
     * Is it a empty table?<br>
     * 
     * @param session
     * @return true if it is a empty table.
     * @since SDNO 0.5
     */
    public boolean isEmtpyTable(SqlSession session) {

        @SuppressWarnings("resource")
        SqlSession sqlSession = querySession == null ? session : querySession;
        String result = sqlSession.selectOne("isEmptyTable", this);

        return StringUtils.isEmpty(result);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Set filter condition. <br>
     * 
     * @param uuid UUID
     * @since SDNO 0.5
     */
    public void setUuidFilter(String uuid) {
        this.uuid = uuid;
        filter = tableName + ".uuid = '" + uuid + "'";
    }

    public String getTableName() {
        return tableName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFilter() {
        return filter;
    }

    public String getResType() {
        return resType;
    }

    @Override
    public String toString() {
        // If the variable havn't been initialized, print the original value.
        String filterStr = filter == null ? filterJson : filter;
        return "resType = " + resType + ", uuid = " + uuid + ", filter = " + filterStr;
    }

    @Override
    public int addData(SqlSession session) {
        // implement by subclass
        return 0;
    }

    @Override
    public int updateData(SqlSession session) {
        // implement by subclass
        return 0;
    }

    @Override
    public int removeData(SqlSession session) {
        return session.delete("removeData", this);
    }

    @Override
    public int batchDelete(SqlSession session) {
        return session.delete("batchDeleteData", this);
    }

    @Override
    public List<Map<String, Object>> getData(SqlSession session) {
        // unimplemented
        throw new MethodNotSupportException("getData");
    }
}
