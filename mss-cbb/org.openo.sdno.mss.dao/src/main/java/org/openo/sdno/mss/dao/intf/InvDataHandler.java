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

package org.openo.sdno.mss.dao.intf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.QueryParamModel;

/**
 * Inventory Data Handler Interface.<br>
 * <p>
 * These Interfaces include following interfaces: add data,delete data,update data and query data.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public interface InvDataHandler {

    /**
     * Data Query Interface.<br>
     * 
     * @param resType resource type.
     * @param uuid UUID of object.
     * @param attr attribute name.
     * @return list of the entity.
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> get(final String resType, final String uuid, final String attr);

    /**
     * Data batch query Interface, contains basic and extensive attributes.<br>
     * 
     * @param resType resource name
     * @param attr attribute name
     * @param filter filter condition
     * @param filterEx filter condition
     * @return
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> batchGet(final String resType, final String attr, final String filter,
            final String filterEx);

    /**
     * Query Data by pages.<br>
     * 
     * @param resType resource type
     * @param attr attribute name
     * @param filter filter parameter
     * @param filterEx filter parameter
     * @param refValue attribute value
     * @param refUniqueValue unique index,just for splitting pages
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> getSplitPage(String resType, String attr, String filter, String filterEx,
            Object refValue, String refUniqueValue);

    /**
     * Data Query by sort pages.<br>
     * 
     * @param resType resource type
     * @param attr attribute name
     * @param filter filter parameter
     * @param filterEx filter parameter
     * @param sortAttrName sort attribute name
     * @param isAsc is Ascend
     * @param refValue attribute value
     * @param refUnique unique index
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> getSplitPage(String resType, String attr, String filter, String filterEx,
            String sortAttrName, Object refValue, String refUnique);

    /**
     * Data Delete Interface.<br>
     * 
     * @param resType resource type
     * @param uuid key id
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> delete(final String resType, final String uuid);

    /**
     * Data Batch Delete Interface.<br>
     * 
     * @param resType resource type
     * @param uuidList key id list
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> batchDelete(final String resType, List<String> uuidList);

    /**
     * Data Update Interface.<br>
     * 
     * @param resType resource type
     * @param uuid key id
     * @param value data value
     * @return InvRespEntity
     * @since SDNO 0.5
     */
    InvRespEntity<Map<String, Object>> update(String resType, String uuid, Map<String, Object> value);

    /**
     * Data batch Update Interface.<br>
     * 
     * @param resType resource type
     * @param values new values
     * @return InvRespEntity object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> batchUpdate(String resType, List<Map<String, Object>> values);

    /**
     * Data Add Interface.<br>
     * 
     * @param resType resource type
     * @param values data need to be added
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<Map<String, Object>> add(String resType, Map<String, Object> values);

    /**
     * Data Batch Add Interface.<br>
     * 
     * @param resType resource type
     * @param values data list need to be added
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> batchAdd(String resType, List<Map<String, Object>> values);

    /**
     * Count Extensive Data Interface.<br>
     * 
     * @param filter filter condition
     * @param resType resource type
     * @return count of extensive data
     * @since SDNO 0.5
     */
    int countExtData(String filter, String resType);

    /**
     * Data Common Query Interface.<br>
     * 
     * @param resType resource type
     * @param attrsList attribute list
     * @param joinAttrList join attribute list
     * @param filterDsc filter Description
     * @param sortList sort list
     * @param queryParam parameter class for query
     * @return InvRespEntity<List<Map<String, Object>>>
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> commQueryGet(String resType, List<String> attrsList,
            List<HashMap<String, Object>> joinAttrList, String filterDsc, List<String> sortList,
            QueryParamModel queryParam);

    /**
     * Data Common Query Interface.<br>
     * 
     * @param resType resource type
     * @param joinAttrList join attribute list
     * @param filterDsc filter Description
     * @param filterData filter Data
     * @return List<Object>
     * @since SDNO 0.5
     */
    List<Object> commQueryGetCount(String resType, List<HashMap<String, Object>> joinAttrList, String filterDsc,
            String filterData);

    /**
     * Query Relation Data Interface.<br>
     * 
     * @param resType resource type
     * @param attrsList attribute list
     * @param filter filter
     * @param sortList sort list
     * @param pageNum page number
     * @param pageSize page size
     * @return Relation Data
     * @since SDNO 0.5
     */
    Object queryRelationData(String resType, List<String> attrsList, String filter, List<String> sortList,
            String pageNum, String pageSize);

    /**
     * Query Relation Data count Interface.<br>
     * 
     * @param resType resource type
     * @param attrsList attribute list
     * @param filter filter
     * @param sortList sort list
     * @param pageNum page number
     * @param pageSize page size
     * @return Relation Data count
     * @since SDNO 0.5
     */
    List<Object> queryRelationDataCount(String resType, List<String> attrsList, String filter, List<String> sortList,
            String pageNum, String pageSize);

    /**
     * Check whether resource attribute exist.<br>
     * 
     * @param resType resource type
     * @param attrName attribute name
     * @param attrVal attribute value
     * @return
     * @since SDNO 0.5
     */
    Boolean exist(String resType, String attrName, Object attrVal);
}
