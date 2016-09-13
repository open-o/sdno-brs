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

import org.apache.ibatis.session.SqlSession;

/**
 * Inventory count PoJo. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvStatPojo extends InvBasicTablePojo {

    /**
     * Count attribute name.
     */
    private String countAttrName = "*";

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvStatPojo(String resType) {
        super(resType);
    }

    /**
     * Set count attribute name. <br>
     * 
     * @param countAttrName
     * @since SDNO 0.5
     */
    public void setCountAttrName(String countAttrName) {
        this.countAttrName = countAttrName;
    }

    /**
     * Get count attribute name. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public String getCountAttrName() {
        return countAttrName;
    }

    /**
     * Count. <br>
     * 
     * @param session SQL session.
     * @return count
     * @since SDNO 0.5
     */
    public int getCount(SqlSession session) {
        try {
            // Create temporary table.
            createTempAttrTable(session);
            return (Integer)session.selectOne("getStatCnt", this);
        } finally {
            removeTempAttrTable(session);
        }
    }
}
