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

package org.openo.sdno.brs.model.roamo;

import java.util.Map;

/**
 * Paging query information.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class PagingQueryPara {

    /**
     * page number to query,default 0.
     */
    private int pageNum = 0;

    /**
     * page size maximum 1000.
     */
    private int pageSize = 1000;

    /**
     * default is base, support all,base,or name/id/location, MSS support ext, BRS not for now.
     */
    private String fields = "base";

    /**
     * filter map.
     */
    private Map<String, String> filtersMap;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        if(null == fields || fields.isEmpty()) {
            return;
        }
        this.fields = fields;
    }

    public Map<String, String> getFiltersMap() {
        return filtersMap;
    }

    public void setFiltersMap(Map<String, String> filtersMap) {
        this.filtersMap = filtersMap;
    }
}
