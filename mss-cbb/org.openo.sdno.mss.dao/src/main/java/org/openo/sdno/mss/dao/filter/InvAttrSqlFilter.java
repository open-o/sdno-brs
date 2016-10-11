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

package org.openo.sdno.mss.dao.filter;

import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.mss.dao.pojo.InvTempAttrFilterPojo;

/**
 * Inventory Attribute SQL Filter Class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class InvAttrSqlFilter {

    /**
     * whereFilter SQL
     */
    private String whereFilter;

    /**
     * AttrFilter list
     */
    private List<InvTempAttrFilterPojo> pojoList = new ArrayList<InvTempAttrFilterPojo>();

    /**
     * Get whereFilter attribute.<br>
     * 
     * @return whereFilter attribute
     * @since SDNO 0.5
     */
    public String getWhereFilter() {
        return whereFilter;
    }

    /**
     * Set whereFilter attribute.<br>
     * 
     * @param whereFilter String Object
     * @since SDNO 0.5
     */
    public void setWhereFilter(String whereFilter) {
        this.whereFilter = whereFilter;
    }

    /**
     * Get pojoList attribute.<br>
     * 
     * @return pojoList attribute
     * @since SDNO 0.5
     */
    public List<InvTempAttrFilterPojo> getPojo() {
        return pojoList;
    }

    /**
     * Set pojoList attribute.<br>
     * 
     * @param pojoList AttrFilter list
     * @since SDNO 0.5
     */
    public void setPojo(List<InvTempAttrFilterPojo> pojoList) {
        this.pojoList = pojoList;
    }

    /**
     * Override toString Function.<br>
     * 
     * @return toString content
     * @since SDNO 0.5
     */
    @Override
    public String toString() {
        return "filter = " + whereFilter + ", InvTempAttrFilterPojo = " + pojoList;
    }
}
