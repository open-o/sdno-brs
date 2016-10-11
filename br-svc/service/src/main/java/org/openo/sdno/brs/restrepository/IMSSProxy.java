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

package org.openo.sdno.brs.restrepository;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

/**
 * Interface of MSS service proxy.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public interface IMSSProxy {

    /**
     * Get resource data.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param objectID UUID of object.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getResource(String bucketName, String resourceTypeName, String objectID) throws ServiceException;

    /**
     * Get relation data.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param dstType destination resource type.
     * @param srcIds UUID of the source resource.
     * @param dstIds UUID of the destination resource.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getRelations(String bucketName, String resourceTypeName, String dstType, String srcIds,
            String dstIds) throws ServiceException;

    /**
     * Batch get relation data.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param fields fields to query.
     * @param filter filter field.
     * @param pagesize size of the page.
     * @param pagenum number of page.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getRelationResourceList(String bucketName, String resourceTypeName, String fields, String filter,
            int pagesize, int pagenum) throws ServiceException;

    /**
     * Batch get resource datas.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param fields fields to query.
     * @param filter filter field.
     * @param pagesize size of the page.
     * @param pagenum number of page.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getResourceList(String bucketName, String resourceTypeName, String fields, String filter,
            int pagesize, int pagenum) throws ServiceException;

    /**
     * Batch add resources.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param obj object to add.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse addResources(String bucketName, String resourceTypeName, Object obj) throws ServiceException;

    /**
     * Update the resource.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param objectId UUID of the object need update.
     * @param obj object to update.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse updateResource(String bucketName, String resourceTypeName, String objectId, Object obj)
            throws ServiceException;

    /**
     * Delete resource from DB.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param objectId UUID of the object need update.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse deleteResource(String bucketName, String resourceTypeName, String objectId) throws ServiceException;

    /**
     * Delete relation from DB.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param strSrcID UUID of the source resource.
     * @param strDstID UUID of the destination resource.
     * @param strDstType type of the destination resource.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse deleteRelation(String bucketName, String resourceTypeName, String strSrcID, String strDstID,
            String strDstType) throws ServiceException;

    /**
     * Create relation.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param obj relation object to create.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse createRelation(String bucketName, String resourceTypeName, Object obj) throws ServiceException;

    /**
     * Check if the given object exist.<br>
     * 
     * @param bucketName name of the bucket.
     * @param resourceTypeName name of resource type.
     * @param obj object to check.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse checkObjExsit(String bucketName, String resourceTypeName, Object obj) throws ServiceException;

    /**
     * Get ME by relation.<br>
     * 
     * @param bucketName name of bucket.
     * @param siteResTypeName name of resource type of site.
     * @param resourceTypeName name of resource type.
     * @param dstIds destination object UUID.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getManageElementByRelation(String bucketName, String siteResTypeName, String resourceTypeName,
            String dstIds) throws ServiceException;

    /**
     * Count common query.<br>
     * 
     * @param bucketName name of bucket.
     * @param resourceTypeName name of resource type.
     * @param joinAttr joint attribute.
     * @param filter filter field.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse commonQueryCount(String bucketName, String resourceTypeName, String joinAttr, String filter)
            throws ServiceException;

    /**
     * Get resource list which match the filter condition.<br>
     * 
     * @param bucketName name of bucket.
     * @param resourceTypeName name of resource type.
     * @param fields fields to query.
     * @param filter filter field.
     * @param pagesize size of page.
     * @param pagenum number of page.
     * @return RestfulResponse contain data and error code.
     * @throws ServiceException if data base service encounter some problem.
     * @since SDNO 0.5
     */
    RestfulResponse getResourceListInfo(String bucketName, String resourceTypeName, String fields, String filter,
            int pagesize, int pagenum) throws ServiceException;
}
