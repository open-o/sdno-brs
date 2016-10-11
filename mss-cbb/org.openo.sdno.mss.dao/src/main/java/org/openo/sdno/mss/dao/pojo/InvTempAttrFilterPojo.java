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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;

/**
 * Inventory temporary filter PoJo. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvTempAttrFilterPojo {

    /**
     * Table name.
     */
    private final String tableName;

    /**
     * Temporary table property.
     */
    private InvAttrEntityPojo attrProperty;

    /**
     * Temporary value list.
     */
    private final List<Object> valueListCommit = new ArrayList<Object>();

    public static final int BATCH_COMMIT_CNT = 10000;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param attrName Attribute name.
     * @param resType Resource type.
     */
    public InvTempAttrFilterPojo(String attrName, String resType) {
        tableName = "tmp_tbl_" + StringUtils.remove(UuidUtils.createUuid(), "-");

        Infomodel infoModel = ModelMgrUtil.getInstance().getWholeInfoModelMap().get(resType);
        List<Property> basicProp = infoModel.getBasic().getProperty();
        for(Property property : basicProp) {
            if(property.getName().equals(attrName)) {
                attrProperty = new InvAttrEntityPojo(property);
                break;
            }
        }
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     * @param property property
     */
    public InvTempAttrFilterPojo(String resType, Property property) {
        tableName = "tmp_tbl_" + StringUtils.remove(UuidUtils.createUuid(), "-");

        attrProperty = new InvAttrEntityPojo(property);
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * Build value. <br>
     * 
     * @param value Value
     * @return this
     * @since SDNO 0.5
     */
    public InvTempAttrFilterPojo buildValue(Object value) {
        this.attrProperty = attrProperty.buildValue(value);
        return this;
    }

    /**
     * Create temporary table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    public void createTmpAttrTable(SqlSession session) {
        removeTable(session);
        session.update("creatAttrTempTable", this);
    }

    /**
     * Add data. <br>
     * 
     * @param session SQL session.
     * @return Operation result.
     * @since SDNO 0.5
     */
    public int addData(SqlSession session) {
        return session.insert("addValueList", this);
    }

    /**
     * Batch insert. <br>
     * 
     * @param session SQL session.
     * @param attrValueList Attribute value list.
     * @since SDNO 0.5
     */
    public void batchAdd(SqlSession session, List<Object> attrValueList) {
        int total = 0;
        for(Object value : attrValueList) {
            total += 1;
            valueListCommit.add(value);

            if(total % BATCH_COMMIT_CNT == 0) {
                addData(session);
                valueListCommit.clear();
            }
        }

        if(!valueListCommit.isEmpty()) {
            addData(session);
        }
    }

    /**
     * Delete temporary table. <br>
     * 
     * @param session SQL session.
     * @since SDNO 0.5
     */
    public void removeTable(SqlSession session) {
        session.delete("removeTable", tableName);
    }

    public InvAttrEntityPojo getAttrProperty() {
        return attrProperty;
    }

    @Override
    public String toString() {
        return "tableName = " + tableName + ", property = " + attrProperty;
    }
}
