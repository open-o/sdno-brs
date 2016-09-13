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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * Inventory check. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public abstract class AInvCheckerPojo {

    private String resType;

    private String tblName;

    private String attrName;

    private Object attrVal;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param resType Database type.
     * @param attrName Attribute name.
     * @param attrVal Attribute.
     */
    public AInvCheckerPojo(String resType, String attrName, Object attrVal) {
        super();
        this.resType = !StringUtils.isEmpty(resType) ? resType : null;
        this.attrName = !StringUtils.isEmpty(attrName) ? attrName : null;
        this.attrVal = attrVal;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Object getAttrVal() {
        return attrVal;
    }

    public void setAttrVal(Object attrVal) {
        this.attrVal = attrVal;
    }

    /**
     * Check if the attribute is exist. <br>
     * 
     * @param sqlSession
     * @return
     * @since SDNO 0.5
     */
    public abstract int exist(SqlSession sqlSession);

}
