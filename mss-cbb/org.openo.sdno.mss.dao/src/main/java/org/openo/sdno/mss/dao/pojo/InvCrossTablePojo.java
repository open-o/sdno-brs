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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.exception.MethodNotSupportException;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;

/**
 * POJO for Basic and Extension table, usually use for combine table query. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-22
 */
public class InvCrossTablePojo implements IInvTableDataCrud {

    /**
     * Basic information table.
     */
    private final InvBasicTablePojo basic;

    /**
     * Extension information table, using array to implements one to multiple table.
     */
    private List<InvExtTablePojo> extensionList;

    /**
     * Table for network elements.
     */
    private InvTempDevUuidTablePojo device;

    /**
     * Method getData will return all the data, if the quantity is huge, using fromRow and readCount
     * to avoid memory overflow. Maximum quantity is readCount from fromUuid.
     */
    private String fromUuid = FIRST_UUID;

    private int readCount = DEFAULT_READ_COUN;

    public static final String FIRST_UUID = "";

    public static final int DEFAULT_READ_COUN = 100000;

    /**
     * Constructor <br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     * @param attributes Attributes.
     */
    public InvCrossTablePojo(String resType, String attributes) {
        basic = new InvBasicTablePojo(resType).buildAttributes(attributes);
        InvExtTablePojo pojo = new InvExtTablePojo(resType);
        extensionList = pojo.buildAttributes(attributes);

        if(extensionList.isEmpty()) {
            extensionList = null;
        }
    }

    /**
     * Get all the attributes. <br>
     * 
     * @param resType Resource type.
     * @param hasHiddenAttr True if any attribute is hidden.
     * @return
     * @since SDNO 0.5
     */
    public static Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
        Infomodel model = hasHiddenAttr ? ModelMgrUtil.getInstance().getWholeInfoModelMap().get(resType)
                : ModelMgrUtil.getInstance().getInfoModelMap().get(resType);

        if(model == null) {
            throw new IllegalArgumentException("Cannot find info model, resType = " + resType);
        }

        List<Property> basicList = model.getBasic().getProperty();
        List<org.openo.sdno.mss.schema.infomodel.Extension.Property> extensionList = model.getExtension().getProperty();
        int size = basicList.size() + extensionList.size();
        Map<String, Datatype> attrMap = new HashMap<String, Datatype>(size * 2);

        for(Property basic : basicList) {
            attrMap.put(basic.getName(), basic.getType());
        }

        for(org.openo.sdno.mss.schema.infomodel.Extension.Property extension : extensionList) {
            attrMap.put(extension.getName(), extension.getType());
        }

        return attrMap;
    }

    /**
     * Build filter string.<br>
     * 
     * @param field Field.
     * @param dt Data type.
     * @param propSet Proper set.
     * @return this.
     * @since SDNO 0.5
     */
    public String buildFilterString(String field, Datatype dt, Set<String> propSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"").append(field).append("\":[");

        boolean isFirst = true;
        for(String prop : propSet) {
            if(!isFirst) {
                sb.append(',');
            }
            isFirst = false;
            if(dt.equals(Datatype.STRING)) {
                sb.append('\"').append(prop).append('\"');
            } else {
                sb.append(prop);
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Create filter. <br>
     * 
     * @param filter Filter
     * @return this.
     * @since SDNO 0.5
     */
    public InvCrossTablePojo buildFilter(String filter) {
        basic.setFilter(filter);
        return this;
    }

    /**
     * Build filter extension. <br>
     * 
     * @param filterEx Filter extension.
     * @return this.
     * @since SDNO 0.5
     */
    public InvCrossTablePojo buildFilterEx(String filterEx) {
        basic.setFilterEx(filterEx);
        return this;
    }

    /**
     * Build filter description. <br>
     * 
     * @param filterDesc Filter description.
     * @return this.
     * @since SDNO 0.5
     */
    public InvCrossTablePojo buildFilterDesc(String filterDesc) {
        basic.setFilterDesc(filterDesc);
        return this;
    }

    /**
     * Build UUIDfilter. <br>
     * 
     * @param uuid UUID.
     * @return this.
     * @since SDNO 0.5
     */
    public InvCrossTablePojo buildUuidFilter(String uuid) {
        basic.buildUuidFilter(uuid);
        return this;
    }

    /**
     * build device UUID. <br>
     * 
     * @param device Device.
     * @return this.
     * @since SDNO 0.5
     */
    public InvCrossTablePojo buildDevUuid(InvTempDevUuidTablePojo device) {
        this.device = device;
        return this;
    }

    @Override
    public int addData(SqlSession session) {
        // unimplemented.
        throw new MethodNotSupportException("addData");
    }

    @Override
    public int removeData(SqlSession session) {
        // unimplemented.
        throw new MethodNotSupportException("removeData");
    }

    @Override
    public int updateData(SqlSession session) {
        // unimplemented.
        throw new MethodNotSupportException("updateData");
    }

    @Override
    public int batchDelete(SqlSession session) {
        // unimplemented.
        throw new MethodNotSupportException("batchDeleteData");
    }

    @Override
    public List<Map<String, Object>> getData(SqlSession session) {
        try {
            basic.createTempAttrTable(session);
            return session.selectList("getData", this);
        } finally {
            basic.removeTempAttrTable(session);
        }
    }

    @Override
    public String toString() {
        String str = extensionList == null ? "null" : Arrays.toString(extensionList.toArray());
        return "basic = [" + ObjectUtils.toString(basic) + "], extensionList = " + str;
    }

    public InvBasicTablePojo getBasic() {
        return basic;
    }

    public List<InvExtTablePojo> getExtensionList() {
        return extensionList;
    }

    public InvTempDevUuidTablePojo getDevice() {
        return device;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
