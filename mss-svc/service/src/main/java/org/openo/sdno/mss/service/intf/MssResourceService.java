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

package org.openo.sdno.mss.service.intf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.openo.sdno.mss.dao.model.QueryParamModel;

/**
 * The interface of MSS service to deal with resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public interface MssResourceService {

    /**
     * Query a single resource data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param objectId Object id
     * @param fields Resource property list
     * @return The map of resource
     * @since SDNO 0.5
     */
    Map<String, Object> getResource(String bktName, String resType, String objectId, String fields)
            throws ServiceException;

    /**
     * Batch query data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param queryParam The query parameter list
     * @return The batch query data as string
     * @since SDNO 0.5
     */
    String getResources(String bktName, String resType, QueryParamModel queryParam);

    /**
     * Batch update resource.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param requestBody Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    Map<String, Object> batchUpdateResource(String bktName, String resType, @Context HttpServletRequest request)
            throws ServiceException;

    /**
     * Batch delete resources.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param uuids Ids
     * @since SDNO 0.5
     */
    void batchDeleteResources(String bktName, String resType, String uuids) throws ServiceException;

    /**
     * Update single resource.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param objectId Object id
     * @param request Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    Map<String, Object> updateResouce(String bktName, String resType, String objectId,
            @Context HttpServletRequest request) throws ServiceException;

    /**
     * Batch add resource.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param request Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    Map<String, Object> batchAddResource(String bktName, String resType, @Context HttpServletRequest request)
            throws ServiceException;

    /**
     * Delete resource.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param uuid UUID
     * @since SDNO 0.5
     */
    void deleteResouce(String bktName, String resType, String uuid) throws ServiceException;

    /**
     * Query relation data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param fields Resource property list
     * @param filter Filter
     * @param sort Sort
     * @param pageSize The size of page
     * @param pageNum The number of page
     * @return The relation data
     * @since SDNO 0.5
     */
    PageQueryResult<Object> getRelationData(String bktName, String resType, String fields, String filter, String sort,
            String pageSize, String pageNum) throws ServiceException;

    /**
     * Check whether a single resource attribute has a single data.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param request Request input stream body
     * @return true when a single resource attribute has a single data
     *         false when doesn't have a single data
     * @since SDNO 0.5
     */
    Boolean exist(String bktName, String resType, @Context HttpServletRequest request) throws ServiceException;

    /**
     * Total number of statistical resources.<br>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param joinAttr JoinAttr
     * @param filter Filter
     * @return The number of statistical resources
     * @since SDNO 0.5
     */
    int commQueryStaticsCount(String bktName, String resType, String joinAttr, String filter) throws ServiceException;
}
