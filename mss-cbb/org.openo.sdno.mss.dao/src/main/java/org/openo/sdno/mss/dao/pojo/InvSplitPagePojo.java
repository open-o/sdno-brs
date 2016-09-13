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

import org.apache.ibatis.session.SqlSession;

import org.openo.sdno.mss.dao.exception.MethodNotSupportException;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;

/**
 * PoJo use for disperse page select. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvSplitPagePojo implements IInvTableDataCrud {

    /**
     * Maximum data count for one page.
     */
    private static final int PAGE_SIZE = 1000;

    /**
     * Basic table information.
     */
    private final InvBasicTablePojo basic;

    /**
     * Name of Attribute which will use for sorting.
     */
    private InvAttrEntityPojo sortAttr;

    /**
     * Unique index attribute.
     */
    private final InvAttrEntityPojo uniqueAttr;

    /**
     * Ascending or Descending.
     */
    private String sort;

    private int topCnt;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param basic Basic table PoJO.
     */
    public InvSplitPagePojo(InvBasicTablePojo basic) {
        this.basic = basic;
        String uniqueName = ModelMgrUtil.getInstance().getUniqueAttr(basic.getResType());
        this.uniqueAttr = new InvAttrEntityPojo(uniqueName);
    }

    /**
     * Build unique index. <br>
     * 
     * @param uniqueValue
     * @return
     * @since SDNO 0.5
     */
    public InvSplitPagePojo buildUniqueAttr(String uniqueValue) {
        Object value =
                InvTypeConvertor.getInstance().convert(basic.getResType(), uniqueAttr.getAttrName(), uniqueValue);
        this.uniqueAttr.buildValue(value);
        return this;
    }

    /**
     * Sort attribute build. <br>
     * 
     * @param sortAttrName Sort attribute name.
     * @param refValue Reference value.
     * @param isAsc Is ascending sort.
     * @return this.
     * @since SDNO 0.5
     */
    public InvSplitPagePojo buildSortAttr(String sortAttrName, Object refValue, boolean isAsc) {
        this.sortAttr = new InvAttrEntityPojo(sortAttrName, refValue);
        sort = isAsc ? "asc" : "desc";
        return this;
    }

    public InvAttrEntityPojo getUniqueAttr() {
        return uniqueAttr;
    }

    public InvBasicTablePojo getBasic() {
        return basic;
    }

    public InvAttrEntityPojo getSortAttr() {
        return sortAttr;
    }

    public String getSort() {
        return sort;
    }

    public int getTopCnt() {
        return topCnt;
    }

    @Override
    public int addData(SqlSession session) {
        // unimplemented
        throw new MethodNotSupportException("addData");
    }

    @Override
    public int removeData(SqlSession session) {
        // unimplemented
        throw new MethodNotSupportException("removeData");
    }

    @Override
    public int batchDelete(SqlSession session) {
        // unimplemented
        throw new MethodNotSupportException("batchDeleteData");
    }

    @Override
    public int updateData(SqlSession session) {
        // unimplemented
        throw new MethodNotSupportException("updateData");
    }

    @Override
    public List<Map<String, Object>> getData(SqlSession session) {
        try {
            basic.createTempAttrTable(session);
            return doQueryData(session);
        } finally {
            basic.removeTempAttrTable(session);
        }
    }

    private List<Map<String, Object>> doQueryData(SqlSession session) {

        List<Map<String, Object>> result = session.selectList("getDataWithSplitPage1", this);

        if(result.size() < PAGE_SIZE && sortAttr != null && uniqueAttr != null && uniqueAttr.getAttrValue() != null) {

            if(!result.isEmpty() && (null == sortAttr.getAttrValue())) {
                uniqueAttr.buildValue(result.get(result.size() - 1).get(uniqueAttr.getAttrName()));
            }

            this.topCnt = PAGE_SIZE - result.size();
            List<Map<String, Object>> tmpResult = session.selectList("getDataWithSplitPage2", this);
            result.addAll(tmpResult);
        }

        return result;
    }

}
