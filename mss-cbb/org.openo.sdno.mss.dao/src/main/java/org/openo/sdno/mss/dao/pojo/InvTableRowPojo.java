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

/**
 * Data row PoJO. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvTableRowPojo {

    /**
     * Attribute value list.
     */
    private final List<Object> attrValueList = new ArrayList<Object>();

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    public InvTableRowPojo() {
        // Default constructor.
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param attrValueList Attribute value list.
     */
    public InvTableRowPojo(List<Object> attrValueList) {
        if(attrValueList == null) {
            return;
        }
        for(Object value : attrValueList) {
            this.attrValueList.add(value);
        }
    }

    /**
     * Build value.<br>
     * 
     * @param attrValue Attribute value.
     * @return this
     * @since SDNO 0.5
     */
    public InvTableRowPojo buildValue(Object attrValue) {
        attrValueList.add(attrValue);
        return this;
    }

    public List<Object> getAttrValueList() {
        return attrValueList;
    }
}
