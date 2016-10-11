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

package org.openo.sdno.brs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.common.util.CollectionUtils;
import org.openo.sdno.brs.constant.Constant;

/**
 * Paging parameter for querying resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ResourcePagePara {

    /**
     * basic table field.
     */
    private List<String> resFieldList = new ArrayList<String>();

    /**
     * key:dbName,Value:paraName.
     */
    private Map<String, String> relationFields = new HashMap<String, String>();

    /**
     * basic table filter.
     */
    private Map<String, String> baseFilters = new HashMap<String, String>();

    /**
     * relation table filter.
     */
    private Map<String, String> relationFilters = new HashMap<String, String>();

    /**
     * maximum number in each page.
     */
    private int pagesize;

    /**
     * page number.
     */
    private int pagenum;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param pagesize maximum number in each page.
     * @param pagenum page number.
     * @param fields filter fields.
     * @param filterMap map of relation information.
     * @param relationInfoMap
     */
    public ResourcePagePara(int pagesize, int pagenum, String fields, Map<String, String> filterMap,
            Map<String, List<String>> relationInfoMap) {
        super();
        initParameters(fields, filterMap, relationInfoMap);
        this.pagesize = pagesize;
        this.pagenum = pagenum;
    }

    private void initParameters(String fields, Map<String, String> filterMap,
            Map<String, List<String>> relationInfoMap) {

        initFields(fields, relationInfoMap);

        initFilters(filterMap, relationInfoMap);
    }

    private void initFields(String fields, Map<String, List<String>> relationInfoMap) {
        // attribute field
        Set<String> relationKeySet = relationInfoMap.keySet();

        // query all field,set relation field.
        if(Constant.FILEDS_ALL.equals(fields)) {
            for(String dbName : relationKeySet) {
                String paraName = relationInfoMap.get(dbName).get(1);
                relationFields.put(dbName, paraName);
            }

            resFieldList.add(Constant.FILEDS_ALL);
            return;

        }

        String[] fieldsArray = fields.split(",");
        for(String field : fieldsArray) {
            if(relationKeySet.contains(field)) {
                String paraName = relationInfoMap.get(field).get(1);
                relationFields.put(field, paraName);
                continue;
            }

            resFieldList.add(field);
        }

    }

    private void initFilters(Map<String, String> filterMap, Map<String, List<String>> relationInfoMap) {
        if(filterMap == null || CollectionUtils.isEmpty(filterMap.values())) {
            return;
        }

        Set<String> filterKeys = filterMap.keySet();
        for(String filterKey : filterKeys) {
            if(relationInfoMap.containsKey(filterKey)) {
                relationFilters.put(filterKey, filterMap.get(filterKey));
                continue;
            }
            baseFilters.put(filterKey, filterMap.get(filterKey));
        }
    }

    /**
     * @return Returns the resFieldList.
     */
    public List<String> getResFieldList() {
        return resFieldList;
    }

    /**
     * @param resFieldList The resFieldList to set.
     */
    public void setResFieldList(List<String> resFieldList) {
        this.resFieldList = resFieldList;
    }

    /**
     * @return Returns the relationFilters.
     */
    public Map<String, String> getRelationFilters() {
        return relationFilters;
    }

    /**
     * @param relationFilters The relationFilters to set.
     */
    public void setRelationFilters(Map<String, String> relationFilters) {
        this.relationFilters = relationFilters;
    }

    /**
     * @return Returns the baseFilters.
     */
    public Map<String, String> getBaseFilters() {
        return baseFilters;
    }

    /**
     * @param baseFilters The baseFilters to set.
     */
    public void setBaseFilters(Map<String, String> baseFilters) {
        this.baseFilters = baseFilters;
    }

    /**
     * @return Returns the page size.
     */
    public int getPagesize() {
        return pagesize;
    }

    /**
     * @param pagesize The page size to set.
     */
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    /**
     * @return Returns the page number.
     */
    public int getPagenum() {
        return pagenum;
    }

    /**
     * @param pagenum The page number to set.
     */
    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    /**
     * @return Returns the relationFields.
     */
    public Map<String, String> getRelationFields() {
        return relationFields;
    }

    /**
     * @param relationFields The relationFields to set.
     */
    public void setRelationFields(Map<String, String> relationFields) {
        this.relationFields = relationFields;
    }

}
