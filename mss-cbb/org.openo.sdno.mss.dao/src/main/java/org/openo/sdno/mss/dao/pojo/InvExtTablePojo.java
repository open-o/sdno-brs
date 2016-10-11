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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.constant.InvAttrDefine;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Extension.Property;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inventory extension table. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-22
 */
public class InvExtTablePojo extends DefaultInvTablePojo implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvExtTablePojo.class);

    private static final long serialVersionUID = 159753L;

    /**
     * Attribute name.
     */
    private String attrName;

    /**
     * Attribute value.
     */
    private String attrValue;

    /**
     * Batch insert maximum quantity.
     */
    public static final int BATCH_INSERT_LIMIT = 500;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    private InvExtTablePojo() {
        super();
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvExtTablePojo(String resType) {
        super(resType);
        this.tableName = "tbl_inv_" + resType + "_ex";
        this.allAttrMap = getAllExtAttributes(resType);
    }

    /**
     * Copy basic attribute. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public InvExtTablePojo copy() {
        InvExtTablePojo pojo = new InvExtTablePojo();
        pojo.resType = this.resType;
        pojo.tableName = this.tableName;
        pojo.uuid = this.uuid;
        pojo.allAttrMap = this.allAttrMap;
        pojo.querySession = this.querySession;

        return pojo;
    }

    /**
     * Copy all the attributes, return multiple instance. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public List<InvExtTablePojo> copyAllAttr() {
        List<InvExtTablePojo> result = new ArrayList<InvExtTablePojo>();
        for(String attr : allAttrMap.keySet()) {
            result.add(copy().buildAttribute(attr));
        }

        return result;
    }

    /**
     * Pre handle analysis. <br>
     * 
     * @param resType
     * @return
     * @since SDNO 0.5
     */
    public static Map<String, Datatype> getAllExtAttributes(String resType) {
        Infomodel infoModel = ModelMgrUtil.getInstance().getWholeInfoModelMap().get(resType);

        List<Property> extProp = infoModel.getExtension().getProperty();
        Map<String, Datatype> attrMap = new HashMap<String, Datatype>(extProp.size() * 2);
        for(Property property : extProp) {
            attrMap.put(property.getName(), property.getType());
        }

        return attrMap;
    }

    /**
     * Build attribute. <br>
     * 
     * @param attrName
     * @return
     * @since SDNO 0.5
     */
    public InvExtTablePojo buildAttribute(String attrName) {
        this.attrName = attrName;
        return this;
    }

    /**
     * Attributes batch build, return multiple instance. <br>
     * 
     * @param attributes Attributes.
     * @return
     * @since SDNO 0.5
     */
    public List<InvExtTablePojo> buildAttributes(String attributes) {
        // As default, '*' or ' '(whitespace) needn't return extension attribute.
        if(StringUtils.isEmpty(attributes) || "*".equals(attributes)) {
            return Collections.emptyList();
        }

        if("all".equals(attributes) || "ext".equals(attributes)) {
            return copyAllAttr();
        }

        List<InvExtTablePojo> resultList = new ArrayList<InvExtTablePojo>();
        String[] attrArray = StringUtils.split(attributes.trim(), ", ");
        for(String str : attrArray) {
            if(allAttrMap.containsKey(str)) {
                resultList.add(copy().buildAttribute(str));
            }
        }

        return resultList;
    }

    /**
     * Build value. <br>
     * 
     * @param value Value.
     * @return
     * @since SDNO 0.5
     */
    public InvExtTablePojo buildValue(String value) {
        this.attrValue = value;
        return this;
    }

    /**
     * Build multiple values, return value list. <br>
     * 
     * @param valueMap Values
     * @return
     * @since SDNO 0.5
     */
    public List<InvExtTablePojo> buildValues(Map<String, Object> valueMap) {
        Validate.notNull(valueMap);
        Validate.notEmpty(valueMap.values());

        if(valueMap.containsKey(InvAttrDefine.UUID.getValue())) {
            uuid = (String)valueMap.get(InvAttrDefine.UUID.getValue());
        }

        List<InvExtTablePojo> resultList = new ArrayList<InvExtTablePojo>();

        for(Map.Entry<String, Object> entry : valueMap.entrySet()) {
            Datatype type = allAttrMap.get(entry.getKey());
            if(type == null) {
                continue;
            }

            String value = entry.getValue() == null ? null : entry.getValue().toString();
            InvExtTablePojo pojo = copy().buildAttribute(entry.getKey()).buildValue(value);
            resultList.add(pojo);
        }

        return resultList;
    }

    /**
     * Build UUID filter condition. <br>
     * 
     * @param uuid UUID
     * @return
     * @since SDNO 0.5
     */
    public InvExtTablePojo buildUuidFilter(String uuid) {
        setUuidFilter(uuid);
        return this;
    }

    /**
     * Build all conditions of filter. <br>
     * 
     * @return Filter condition.
     * @since SDNO 0.5
     */
    public InvExtTablePojo buildAllFilter() {
        setUuidFilter(uuid);
        if(attrName != null) {
            filter = filter + " and attrname = '" + attrName + "'";
        }
        return this;
    }

    @Override
    public int addData(SqlSession session) {

        if(attrValue == null) {
            return 0;
        }

        return session.insert("addDataInExtension", this);
    }

    @Override
    public int updateData(SqlSession session) {
        return session.update("updateDataInExtension", this);
    }

    /**
     * Get extension data. <br>
     * 
     * @param session SQL session.
     * @return Query result.
     * @since SDNO 0.5
     */
    public List<InvExtTablePojo> getExtData(SqlSession session) {
        @SuppressWarnings("resource")
        SqlSession sqlSession = querySession == null ? session : querySession;
        return sqlSession.selectList("getExtData", this);
    }

    /**
     * Batch query extension data. <br>
     * 
     * @param session SQL session.
     * @param batchSession SQL session.
     * @param uuids UUID
     * @return Query result.
     * @since SDNO 0.5
     */
    public List<InvExtTablePojo> getExtDatas(SqlSession session, SqlSession batchSession, List<String> uuids) {
        if(uuids.size() == 1) {
            buildUuidFilter(uuids.get(0));
            return getExtData(session);
        }

        InvTempUuidTablePojo pojo = new InvTempUuidTablePojo(resType);
        pojo.removeTable(session);
        try {
            pojo.createTable(session);
            pojo.addData(batchSession, uuids);

            return session.selectList("getExtDatas", pojo);
        } finally {
            pojo.removeTable(session);
        }
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    /**
     * Query quantity of extension data. <br>
     * 
     * @param session SQL session.
     * @return quantity.
     * @since SDNO 0.5
     */
    public int countExtData(SqlSession session) {
        @SuppressWarnings("resource")
        SqlSession sqlSession = querySession == null ? session : querySession;
        return (Integer)sqlSession.selectOne("countExtData", this);
    }

    /**
     * Build attribute filter condition. <br>
     * 
     * @return Query result.
     * @since SDNO 0.5
     */
    public InvExtTablePojo buildAttrNameFilter() {
        if(attrName != null) {
            filter = tableName + ".attrname = '" + attrName + "'";
        }
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + ", attrName = " + attrName;
    }
}
