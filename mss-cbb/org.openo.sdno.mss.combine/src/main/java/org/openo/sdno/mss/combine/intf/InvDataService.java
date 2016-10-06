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

package org.openo.sdno.mss.combine.intf;

import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.exception.ServerInnerException;
import org.openo.sdno.mss.dao.model.QueryParamModel;

/**
 * Inventory data service interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-25
 */
public interface InvDataService {

    /**
     * Query a single resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param uuid UUID
     * @param attr Resource attribute
     * @return query results.
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> get(String bktName, final String resType, final String uuid,
            final String attr) throws ServiceException;

    /**
     * Delete a resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param uuid UUID of resource
     * @return Boolean, true when success, else false
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> delete(String bktName, String resType, String uuid);

    /**
     * Batch delete resource and resource relation.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param uuidList List of UUID
     * @return Boolean, true when success, else false
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> batchDelete(String bktName, String resType, List<String> uuidList);

    /**
     * Update a resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param uuid UUID of resource
     * @param value Collection of resource value
     * @return Collection of updated data
     * @throws ServerInnerException when update failed
     * @since SDNO 0.5
     */
    InvRespEntity<Map<String, Object>> update(String bktName, String resType, String uuid, Map<String, Object> value)
            throws ServerInnerException;

    /**
     * Batch update resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param values Collection of resource value
     * @return Collection of updated data
     * @throws ServerInnerException when update failed
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> batchUpdate(String bktName, String resType,
            List<Map<String, Object>> values) throws ServerInnerException;

    /**
     * Batch add resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param values Collection of resource value
     * @return Collection of added data
     * @throws ServerInnerException when add failed
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> add(String bktName, String resType, List<Map<String, Object>> values)
            throws ServerInnerException;

    /**
     * Common query, support the query mode of one main table and Multiple subordinate tables.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param filterDsc Filter strin
     * @param queryParam Query parameters
     * @param pageCapacity Page capacity
     * @return Collection of query results
     * @since SDNO 0.5
     */
    Object commQueryGet(String bktName, String resType, String filterDsc, QueryParamModel queryParam);

    /**
     * Common query, get statistics count of records.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param joinAttr Collection of join attributes
     * @param filterDsc Filter string
     * @param filterData Filter string
     * @return Collection of query results
     * @since SDNO 0.5
     */
    List<Object> commQueryGetCount(String bktName, String resType, String joinAttr, String filterDsc,
            String filterData);

    /**
     * Query the total amount of relation data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param fields fields
     * @param filter filter
     * @param sort sort string
     * @param pageNum Page number
     * @param pageSize Page size
     * @return query result
     * @since SDNO 0.5
     */
    List<Object> queryRelationDataCount(String bktName, String resType, String fields, String filter, String sort,
            String pageNum, String pageSize);

    /**
     * Query relation data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param attr attributes
     * @param filter filter
     * @param sort sort string
     * @param pageNum Page number
     * @param pageSize Page size
     * @return query result
     * @since SDNO 0.5
     */
    Object queryRelationData(String bktName, String resType, String attr, String filter, String sort, String pageNum,
            String pageSize);

    /**
     * Check whether the single attribute exists.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type
     * @param attrName attribute name
     * @param attrVal attribute value
     * @return Boolean, true when it exist, else false.
     * @throws ServiceException
     * @since SDNO 0.5
     */
    Boolean exist(String bktName, final String resType, final String attrName, final Object attrVal)
            throws ServiceException;
}
