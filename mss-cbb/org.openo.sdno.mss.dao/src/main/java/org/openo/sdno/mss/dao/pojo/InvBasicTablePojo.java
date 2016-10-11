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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.constant.InvAttrDefine;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

/**
 * Basic table of inventory. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class InvBasicTablePojo extends DefaultInvTablePojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvBasicTablePojo.class);

    /**
     * Attribute entities.
     */
    private List<InvAttrEntityPojo> attrEntityList;

    private boolean isAbsent;

    public static final String UUID = "uuid";

    public static final int MAXIMUM_INSERT_COUNT = 10000;

    private List<InvTableRowPojo> tableRowList = null;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvBasicTablePojo(String resType) {
        super(resType);
        this.tableName = "tbl_inv_" + resType;
        this.allAttrMap = getAllBasicAttributes(resType);
    }

    private InvBasicTablePojo() {
        super();
    }

    /**
     * Copy basic attributes. <br>
     * 
     * @return Copy
     * @since SDNO 0.5
     */
    public InvBasicTablePojo copy() {
        InvBasicTablePojo pojo = new InvBasicTablePojo();
        pojo.resType = this.resType;
        pojo.tableName = this.tableName;
        pojo.allAttrMap = this.allAttrMap;
        pojo.querySession = this.querySession;
        pojo.isAbsent = this.isAbsent;

        return pojo;
    }

    /**
     * Get all include hidden attributes in basic table. <br>
     * 
     * @param resType Resource type.
     * @return Attribute map.
     * @since SDNO 0.5
     */
    public static Map<String, Datatype> getAllBasicAttributes(String resType) {
        Infomodel infoModel = ModelMgrUtil.getInstance().getWholeInfoModelMap().get(resType);

        List<Property> basicProp = infoModel.getBasic().getProperty();
        Map<String, Datatype> attrMap = new HashMap<String, Datatype>(basicProp.size() * 2);

        for(Property property : basicProp) {
            attrMap.put(property.getName(), property.getType());
        }

        return attrMap;
    }

    /**
     * Initialize attribute. <br>
     * 
     * @param attributes Attributes.
     * @return this
     * @since SDNO 0.5
     */
    public InvBasicTablePojo buildAttributes(String attributes) {
        attrEntityList = new ArrayList<InvAttrEntityPojo>();

        if("ext".equals(attributes)) {
            InvAttrEntityPojo uuidPojo = new InvAttrEntityPojo(InvAttrDefine.UUID.getValue());
            attrEntityList.add(uuidPojo);
            return this;
        }

        if(StringUtils.isEmpty(attributes) || "*".equals(attributes) || "all".equals(attributes)) {
            for(String attr : allAttrMap.keySet()) {
                attrEntityList.add(new InvAttrEntityPojo(attr));
            }
            return this;
        }

        String[] attrArray = StringUtils.split(attributes.trim(), ", ");
        for(String attr : attrArray) {
            if(allAttrMap.containsKey(attr)) {
                attrEntityList.add(new InvAttrEntityPojo(attr));
            }
        }

        InvAttrEntityPojo uuidPojo = new InvAttrEntityPojo(InvAttrDefine.UUID.getValue());
        if(!attrEntityList.contains(uuidPojo)) {
            attrEntityList.add(uuidPojo);
        }

        return this;
    }

    /**
     * Build attribute value. <br>
     * 
     * @param valueMap Value map.
     * @return this.
     * @since SDNO 0.5
     */
    public InvBasicTablePojo buildValue(Map<String, Object> valueMap) {
        Validate.notNull(valueMap);
        Validate.notEmpty(valueMap.values());
        if(valueMap.containsKey(uuid)) {
            uuid = (String)valueMap.get(InvAttrDefine.UUID.getValue());
        }

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

            Object obj = InvTypeConvertor.getInstance().convert(type, entry.getValue());
            attrEntityList.add(new InvAttrEntityPojo(entry.getKey(), obj));
        }

        return this;
    }

    /**
     * Build filter condition based on UUID. <br>
     * 
     * @param uuid UUID.
     * @return this.
     * @since SDNO 0.5
     */
    public InvBasicTablePojo buildUuidFilter(String uuid) {
        setUuidFilter(uuid);
        return this;
    }

    @Override
    public int addData(SqlSession session) {
        return session.insert("addDataInBasic", this);
    }

    @Override
    public int updateData(SqlSession session) {

        if(isAbsent && getRowCnt(session) == 0) {
            for(InvAttrEntityPojo pojo : attrEntityList) {
                if(InvAttrDefine.UPDATE_TIME.getValue().equals(pojo.getAttrName())) {
                    pojo.setAttrName(InvAttrDefine.CREATE_TIME.getValue());
                    break;
                }
            }

            InvAttrEntityPojo uuidAttr = new InvAttrEntityPojo(InvAttrDefine.UUID.getValue(), uuid);
            if(!attrEntityList.contains(uuidAttr)) {
                attrEntityList.add(uuidAttr);
            }

            return addData(session);
        }

        return session.update("updateDataInBasic", this);
    }

    public List<InvAttrEntityPojo> getAttrEntityList() {
        return attrEntityList;
    }

    @Override
    public String toString() {
        String str = attrEntityList == null ? "null" : Arrays.toString(attrEntityList.toArray());
        return super.toString() + ", attrEntityList = " + str;
    }

    /**
     * Batch update. <br>
     * 
     * @param session SQL session.
     * @param basicRecords Basic records.
     * @return result.
     * @since SDNO 0.5
     */
    public int batchUpdateData(SqlSession session, List<InvTableRowPojo> basicRecords) {
        int basicRet = 0;
        if(basicRecords == null || basicRecords.isEmpty()) {
            return basicRet;
        }

        if(tableRowList == null) {
            tableRowList = new ArrayList<InvTableRowPojo>();
        } else {
            tableRowList.clear();
        }

        tableRowList.addAll(basicRecords);
        try {
            basicRet = session.insert("batchUpdateDataInBasic", this);
        } catch(DataAccessException exp) {
            LOGGER.error("Failed to insert basic data in batch", exp);
        } finally {
            tableRowList.clear();
        }
        return basicRet;
    }

    /**
     * Analysis data. <br>
     * 
     * @param valueMap Value map.
     * @return Row data
     * @since SDNO 0.5
     */
    public InvTableRowPojo parseSpecAttrTableRow(Map<String, Object> valueMap) {
        if(null == attrEntityList || attrEntityList.isEmpty()) {
            buildBasicAttrList(valueMap.keySet());
        }

        InvTableRowPojo rowPojo = new InvTableRowPojo();

        for(Entry<String, Object> entry : valueMap.entrySet()) {
            if(allAttrMap.containsKey(entry.getKey())) {
                rowPojo.buildValue(valueMap.get(entry.getKey()));
            }
        }

        return rowPojo;
    }

    private void buildBasicAttrList(Set<String> keys) {
        if(null == attrEntityList) {
            attrEntityList = new ArrayList<InvAttrEntityPojo>();
        }

        for(String key : keys) {
            if(allAttrMap.containsKey(key)) {
                InvAttrEntityPojo attrEntityPojo = new InvAttrEntityPojo(key);
                attrEntityList.add(attrEntityPojo);
            }
        }
    }

    /**
     * @param isAbsent The isAbsent to set.
     */
    public void setAbsent(boolean isAbsent) {
        this.isAbsent = isAbsent;
    }
}
