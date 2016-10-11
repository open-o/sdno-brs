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

package org.openo.sdno.mss.dao.model;

/**
 * The query parameter model.<br>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class QueryParamModel {

    private String fields;

    private String joinAttr;

    private String filter;

    private String sort;

    private String pageSize;

    private String pageNum;

    /**
     * Constructor<br>
     * 
     * @param fields The query fields
     * @param joinAttr The query filter attributes
     * @param filter The filter value
     * @param sort The sort value
     * @param pageSize The size for every page
     * @param pageNum The query page number
     * @since SDNO 0.5
     */
    public QueryParamModel(String fields, String joinAttr, String filter, String sort, String pageSize,
            String pageNum) {
        this.fields = fields;
        this.joinAttr = joinAttr;
        this.filter = filter;
        this.sort = sort;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getJoinAttr() {
        return joinAttr;
    }

    public void setJoinAttr(String joinAttr) {
        this.joinAttr = joinAttr;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
