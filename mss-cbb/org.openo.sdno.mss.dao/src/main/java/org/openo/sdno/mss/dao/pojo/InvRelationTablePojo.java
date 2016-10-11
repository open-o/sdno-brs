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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.entities.InvRelationEntity;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Property;
import org.openo.sdno.mss.schema.relationmodel.Relationtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

/**
 * Inventory relation table. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvRelationTablePojo extends DefaultInvTablePojo implements IInvTableDataCrud {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvRelationTablePojo.class);

    private List<InvAttrEntityPojo> attrEntityList = null;

    public static final String DSTTYPE = "dst_type";

    public static final String SRCUUID = "src_uuid";

    public static final String DSTUUID = "dst_uuid";

    public static final String RELATION = "relation";

    public static final String SERVICE_TYPE = "servicetype";

    private String queryUuidName = null;

    private String refUuidName = SRCUUID;

    /**
     * Maximum length of expression.
     */
    private static final int MAXIMUM_LENGTH = 30;

    private final InvRelationEntity fromRow = new InvRelationEntity();

    private int readCount = DEFAULT_READ_COUN;

    public static final int DEFAULT_READ_COUN = 100000;

    public static final int BATCH_INSERT_LIMIT = 1000;

    private List<InvTableRowPojo> tableRowList = null;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvRelationTablePojo(String resType) {
        super(resType);
        this.tableName = PropertiesUtil.getInstance().getINVTABLEPREFIX() + resType
                + PropertiesUtil.getInstance().getRELATIONTABLEPOSTFIX();
        this.allAttrMap = getRelationAttributes();
        emptyCheckColumn = SRCUUID;

        clearFromRow();
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     * @param queryUuidName Query UUID name.
     * @param refUuidName Reference UUID name.
     */
    public InvRelationTablePojo(String resType, String queryUuidName, String refUuidName) {
        this(resType);
        this.queryUuidName = queryUuidName;
        this.refUuidName = refUuidName;
    }

    public InvRelationEntity getFromRow() {
        return fromRow;
    }

    /**
     * Set beginning row.<br>
     * 
     * @param relEntity Relation entity.
     * @since SDNO 0.5
     */
    public void setFromRow(InvRelationEntity relEntity) {
        if(relEntity == null) {
            return;
        }

        fromRow.setSrcUuid(relEntity.getSrcUuid());
        fromRow.setDstUuid(relEntity.getDstUuid());
        fromRow.setDstType(relEntity.getDstType());
        fromRow.setRelation(relEntity.getRelation());
        fromRow.setServiceType(relEntity.getServiceType());
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    /**
     * Set filter. <br>
     * 
     * @param filter Filter condition.
     * @since SDNO 0.5
     */
    public void setRealFilter(String filter) {
        if(StringUtils.isEmpty(filter)) {
            return;
        }

        this.filter = StringUtils.isEmpty(this.filter) ? filter : (this.filter + " and " + filter);
    }

    /**
     * Get relation table attribute information. <br>
     * 
     * @return Attribute map.
     * @since SDNO 0.5
     */
    private Map<String, Datatype> getRelationAttributes() {
        String[] relationColumns = PropertiesUtil.getInstance().getRELATIONTABLECOLUMN();
        Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
        for(int i = 0; i < relationColumns.length; i++) {
            String[] columnStr = relationColumns[i].split(",");
            attrMap.put(columnStr[0], Datatype.fromValue(columnStr[1]));
        }
        return attrMap;
    }

    /**
     * Create temporary table. <br>
     * 
     * @param srcUuidSet Source UUID set.
     * @param session SQL session.
     * @since SDNO 0.5
     */
    public void createTempAttrTable(Set<String> srcUuidSet, SqlSession session) {
        createTableByFieldName(srcUuidSet, session, SRCUUID);
    }

    /**
     * Create temporary attribute table by field name. <br>
     * 
     * @param uuidSet UUID set.
     * @param session SQL session.
     * @param fieldName Field name.
     * @since SDNO 0.5
     */
    public void createTempAttrTableByFiledName(Set<String> uuidSet, SqlSession session, String fieldName) {
        createTableByFieldName(uuidSet, session, fieldName);
    }

    private void createTableByFieldName(Set<String> srcUuidSet, SqlSession session, String fieldName) {
        if(srcUuidSet != null && !srcUuidSet.isEmpty()) {
            if(srcUuidSet.size() < MAXIMUM_LENGTH) {

                boolean isFirst = true;
                StringBuilder sb = new StringBuilder(fieldName);
                sb.append(" in (");
                for(String uuid : srcUuidSet) {
                    if(!isFirst) {
                        sb.append(',');
                    }
                    isFirst = false;
                    sb.append('\'').append(uuid).append('\'');
                }
                sb.append(')');

                filter = StringUtils.isEmpty(filter) ? sb.toString() : filter + " and " + sb.toString();
            } else {
                Property property = new Property();
                property.setName(fieldName);
                property.setType(Datatype.STRING);
                property.setLength(BigInteger.valueOf(36));

                InvTempAttrFilterPojo pojo = new InvTempAttrFilterPojo(resType, property);

                filterAttrList = new ArrayList<InvTempAttrFilterPojo>();
                filterAttrList.add(pojo);

                pojo.createTmpAttrTable(session);

                List<Object> uuidList = new ArrayList<Object>();
                for(String uuid : srcUuidSet) {
                    uuidList.add(uuid);
                }
                pojo.batchAdd(session, uuidList);
            }
        }
    }

    /**
     * Inventory relation table PoJo. <br>
     * 
     * @param valueMap Value map.
     * @return Table PoJo.
     * @since SDNO 0.5
     */
    public InvRelationTablePojo buildValue(Map<String, Object> valueMap) {
        checkValueMap(valueMap);

        if(attrEntityList == null) {
            attrEntityList = new ArrayList<InvAttrEntityPojo>();
        } else {
            attrEntityList.clear();
        }

        for(Map.Entry<String, Object> entry : valueMap.entrySet()) {
            Datatype type = allAttrMap.get(entry.getKey());
            if(type == null) {
                continue;
            }
            Object value = entry.getValue();

            if(StringUtils.equals(RELATION, entry.getKey()) && !StringUtils.isNumeric((String)value)) {
                value = PropertiesUtil.getInstance().getRELATIONTYPEVALUES().get(value);
            }

            if(StringUtils.equals(SERVICE_TYPE, entry.getKey()) && null == value) {
                value = "";
            }

            Object obj = InvTypeConvertor.getInstance().convert(type, value);
            attrEntityList.add(new InvAttrEntityPojo(entry.getKey(), obj));
        }
        if(!valueMap.containsKey(SERVICE_TYPE)) {
            Datatype dt = allAttrMap.get(SERVICE_TYPE);
            Object obj = InvTypeConvertor.getInstance().convert(dt, "");
            attrEntityList.add(new InvAttrEntityPojo(SERVICE_TYPE, obj));
        }

        if(attrEntityList.size() != allAttrMap.size()) {
            LOGGER.warn("relation data is incomplete when buildValue:" + attrEntityList.toString());
            attrEntityList.clear();
        }

        return this;
    }

    /**
     * Validation. <br>
     * 
     * @param valueMap Value map.
     * @since SDNO 0.5
     */
    private void checkValueMap(Map<String, Object> valueMap) {
        Validate.notNull(valueMap);
        Validate.notEmpty(valueMap.values());

        if(allAttrMap.containsKey(SRCUUID)) {
            String srcUuid = (String)valueMap.get(SRCUUID);
            ValidUtil.checkUuid(srcUuid);
        }
        if(allAttrMap.containsKey(DSTUUID)) {
            String dstUuid = (String)valueMap.get(DSTUUID);
            ValidUtil.checkUuid(dstUuid);
        }
        if(allAttrMap.containsKey(RELATION)) {
            String relation = (String)valueMap.get(RELATION);
            if(StringUtils.isNumeric(relation)) {
                ValidUtil.checkRelationEnumValue(Integer.parseInt(relation));
            } else {
                Relationtype.fromValue(relation);
            }
        }
    }

    @Override
    public int addData(SqlSession session) {
        if(attrEntityList.isEmpty()) {
            return -1;
        }
        return session.insert("addDataToRelation", this);
    }

    @Override
    public int removeData(SqlSession session) {
        return session.delete("removeData", this);
    }

    @Override
    public int batchDelete(SqlSession session) {
        return session.delete("batchDeleteRelationData", this);
    }

    @Override
    public int updateData(SqlSession session) {
        return session.update("updateRelationData", this);
    }

    @Override
    public List<Map<String, Object>> getData(SqlSession session) {
        return session.selectList("getUuidbyRefUuid", this);
    }

    /**
     * Batch get source UUID. <br>
     * 
     * @param session SQL session.
     * @return Relations.
     * @since SDNO 0.5
     */
    public List<Map<String, Object>> batchGetRelations(SqlSession session) {
        return session.selectList("getRelationsbyBatchSrcUuid", this);
    }

    /**
     * Get relation records by source UUID. <br>
     * 
     * @param session SQL session.
     * @return Relation.
     * @since SDNO 0.5
     */
    public List<Map<String, Object>> getRelations(SqlSession session) {
        return session.selectList("getRelationsbyRefUuid", this);
    }

    @Override
    public String toString() {
        String str = attrEntityList == null ? "null" : Arrays.toString(attrEntityList.toArray());
        return super.toString() + ", attrEntityList = " + str;
    }

    /**
     * Build interface of UUID condition deletion. <br>
     * <br>
     * 
     * @param srcUuid Source UUID.
     * @param dstUuid Destination UUID.
     * @param dstType Destination type.
     * @param relation Relation.
     * @since SDNO 0.5
     */
    public void buildUuidCondition(String srcUuid, String dstUuid, String dstType, String relation) {
        StringBuilder sb = new StringBuilder();
        appendFilter(sb, SRCUUID, srcUuid);
        appendFilter(sb, DSTUUID, dstUuid);
        appendFilter(sb, DSTTYPE, dstType);

        // Convert relation from String to int.
        if(!StringUtils.isEmpty(relation)) {
            if(sb.length() != 0) {
                sb.append(" and ");
            }
            Integer value = PropertiesUtil.getInstance().getRELATIONTYPEVALUES().get(relation);
            sb.append(tableName).append('.').append(RELATION).append(" = ").append(value);
        }

        this.filter = sb.toString();
    }

    private void appendFilter(StringBuilder sb, String key, String value) {
        if(!StringUtils.isEmpty(value)) {
            if(sb.length() != 0) {
                sb.append(" and ");
            }
            sb.append(tableName).append('.').append(key).append(" = '").append(value).append('\'');
        }
    }

    /**
     * Create type for filter.<br>
     * 
     * @param typeName Type name.
     * @return this
     * @since SDNO 0.5
     */
    public InvRelationTablePojo buildDstTypeFilter(String typeName) {
        String str = tableName + '.' + DSTTYPE + " = '" + typeName + '\'';
        this.filter = StringUtils.isEmpty(filter) ? str : filter + " and " + str;
        return this;
    }

    /**
     * Create Range condition for select. <br>
     * 
     * @param dstTypeList Destination type list.
     * @return this
     * @since SDNO 0.5
     */
    public InvRelationTablePojo buildDstTypeSetFilter(List<String> dstTypeList) {
        if(null == dstTypeList || dstTypeList.isEmpty()) {
            return this;
        }

        String str = "(";
        for(String dstType : dstTypeList) {
            str = str + '\'' + dstType + "',";
        }

        str = str.substring(0, str.length() - 1) + ')';

        str = tableName + '.' + DSTTYPE + " in " + str;

        this.filter = StringUtils.isEmpty(filter) ? str : filter + " and " + str;
        return this;
    }

    /**
     * Build service type for filter.<br>
     * 
     * @param serviceType
     * @return
     * @since SDNO 0.5
     */
    public InvRelationTablePojo buildServiceTypeFilter(String serviceType) {
        if(null != serviceType) {
            String str = tableName + '.' + SERVICE_TYPE + " = '" + serviceType + '\'';
            this.filter = StringUtils.isEmpty(filter) ? str : filter + " and " + str;
        }
        return this;
    }

    /**
     * Build UUID for filter. <br>
     * 
     * @param uuids
     * @return
     * @since SDNO 0.5
     */
    public InvRelationTablePojo buildUuidFilter(List<String> uuids) {
        String str = refUuidName + " in (" + buildUuidFilterStr(uuids) + ')';
        this.filter = StringUtils.isEmpty(filter) ? str : filter + " and " + str;
        return this;
    }

    /**
     * Batch add. <br>
     * 
     * @param session SQL session.
     * @param records Records.
     * @return
     * @since SDNO 0.5
     */
    public int bulkInsertData(SqlSession session, List<InvTableRowPojo> records) {
        int ret = 0;
        if(records == null || records.isEmpty()) {
            return ret;
        }

        List<InvTableRowPojo> relationRows = new ArrayList<InvTableRowPojo>();
        for(int i = 0; i < records.size(); ++i) {
            relationRows.add(records.get(i));

            if(relationRows.size() / BATCH_INSERT_LIMIT > 0) {
                ret += batchInsertData(session, relationRows);
                relationRows.clear();
            }
        }

        if(!relationRows.isEmpty()) {
            ret += batchInsertData(session, relationRows);
            relationRows.clear();
        }
        return ret;
    }

    private String buildUuidFilterStr(List<String> uuidList) {
        StringBuilder refUuidStringBuf = new StringBuilder();

        for(int i = 0, size = uuidList.size(); i < size; i++) {
            refUuidStringBuf.append('\'').append(uuidList.get(i)).append('\'');
            if(i != (size - 1)) {
                refUuidStringBuf.append(',');
            }
        }

        return refUuidStringBuf.toString();
    }

    public String getQueryUuidName() {
        return queryUuidName;
    }

    /**
     * Batch insert. <br>
     * 
     * @param session
     * @param relationRecords
     * @return
     * @since SDNO 0.5
     */
    public int batchInsertData(SqlSession session, List<InvTableRowPojo> relationRecords) {
        int relationRet = 0;
        if(relationRecords == null || relationRecords.isEmpty()) {
            return relationRet;
        }

        if(tableRowList == null) {
            tableRowList = new ArrayList<InvTableRowPojo>();
        } else {
            tableRowList.clear();
        }

        tableRowList.addAll(relationRecords);
        try {
            relationRet = session.insert("batchInsertRelation", this);
        } catch(DataAccessException e) {
            LOGGER.error("Failed to insert relation in batch", e);
        } finally {
            tableRowList.clear();
        }
        return relationRet;
    }

    private void clearFromRow() {
        fromRow.setSrcUuid("");
        fromRow.setDstUuid("");
        fromRow.setDstType("");
        fromRow.setRelation("");
        fromRow.setServiceType("");
    }

    /**
     * Analysis one row of data. <br>
     * 
     * @param valueMap Value map.
     * @return Row PoJo.
     * @since SDNO 0.5
     */
    public InvTableRowPojo parseTableRow(Map<String, Object> valueMap) {
        buildValue(valueMap);
        InvTableRowPojo rowPojo = new InvTableRowPojo();

        for(InvAttrEntityPojo attrEntity : attrEntityList) {
            rowPojo.buildValue(attrEntity.getAttrValue());
        }

        return rowPojo;
    }

}
