/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.mss.service.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IResource;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.openo.sdno.mss.service.intf.IMssRelationService;
import org.openo.sdno.mss.service.intf.IMssResourceService;
import org.openo.sdno.mss.service.intf.IMssSvcService;

/**
 * MSS module implements interface layer based on CloudSop platform.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnomss/v1/buckets")
public class MssSvcResource extends IResource<IMssSvcService> {

    /**
     * Resource operation service.
     */
    private IMssResourceService mssResourceService;

    /**
     * Relation operation service.
     */
    private IMssRelationService mssRelationService;

    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Override
    public String getResUri() {
        return "/sdnomss/v1/buckets";
    }

    public void setMssResourceService(IMssResourceService mssResourceService) {
        this.mssResourceService = mssResourceService;
    }

    public void setMssRelationService(IMssRelationService mssRelationService) {
        this.mssRelationService = mssRelationService;
    }

    /**
     * Check whether there is data in the single resource and single attribute.<br/>
     * 
     * @param bktName bucket name
     * @param resType resource type name
     * @param request Request input stream
     * @return true when a single resource attribute has a single data
     *         false when doesn't have a single data
     * @since SDNO 0.5
     */
    @POST
    @Path("/{bucket-name}/resources/{resource-type-name}/checkexist")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Boolean exist(@PathParam("bucket-name") String bktName, @PathParam("resource-type-name") String resType,
            @Context HttpServletRequest request) throws ServiceException {
        return this.mssResourceService.exist(bktName, resType, request);
    }

    /**
     * Query a single resource data.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param objectId Object id
     * @param fields Resource property list
     * @return The map of resource
     * @since SDNO 0.5
     */
    @GET
    @Path("/{bucket-name}/resources/{resource-type-name}/objects/{object_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> getResouce(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @PathParam("object_id") String objectId,
            @QueryParam("fields") String fields) throws ServiceException {
        return this.mssResourceService.getResource(bktName, resType, objectId, fields);
    }

    /**
     * Update single resource.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param objectId Object id
     * @param request Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{bucket-name}/resources/{resource-type-name}/objects/{object_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> updateResouce(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @PathParam("object_id") String objectId,
            @Context HttpServletRequest request) throws ServiceException {
        return this.mssResourceService.updateResouce(bktName, resType, objectId, request);
    }

    /**
     * Delete resource.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param uuid Uuid
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{bucket-name}/resources/{resource-type-name}/objects/{object_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public void deleteResouce(@PathParam("bucket-name") String bktName, @PathParam("resource-type-name") String resType,
            @PathParam("object_id") String uuid) throws ServiceException {
        this.mssResourceService.deleteResouce(bktName, resType, uuid);
    }

    /**
     * Batch update resource.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param request Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    @PUT
    @Path("{bucket-name}/resources/{resource-type-name}/objects")
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> batchUpdateResources(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @Context HttpServletRequest request)
            throws ServiceException {

        return this.mssResourceService.batchUpdateResource(bktName, resType, request);
    }

    /**
     * Batch delete resources.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param uuids Ids
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{bucket-name}/resources/{resource-type-name}/objects")
    @Produces("application/json")
    @Consumes("application/json")
    public void batchDeleteResources(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @QueryParam("ids") String uuids) throws ServiceException {
        this.mssResourceService.batchDeleteResources(bktName, resType, uuids);
    }

    /**
     * Create resource relationship.<br/>
     * 
     * @param bktName Bucket name
     * @param relationType Resource type name
     * @param request Request body
     * @since SDNO 0.5
     */
    @POST
    @Path("/{bucket-name}/resources/{resource-type-name}/relationships")
    @Produces("application/json")
    @Consumes("application/json")
    public void addRelation(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String relationType, @Context HttpServletRequest request)
            throws ServiceException {
        this.mssRelationService.addRelation(bktName, relationType, request);

    }

    /**
     * Delete resource relationship.<br/>
     * 
     * @param bktName Bucket name
     * @param relationType Resource type name
     * @param srcUuid Source uuid
     * @param dstUuid Destination uuid
     * @param dstType Destination type
     * @param reltype Relationship name
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{bucket-name}/resources/{resource-type-name}/relationships")
    @Produces("application/json")
    @Consumes("application/json")
    public void deleteRelation(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String relationType, @QueryParam("src_id") String srcUuid,
            @QueryParam("dst_id") String dstUuid, @QueryParam("dst_type") String dstType,
            @QueryParam("relationship_name") String reltype) throws ServiceException {
        this.mssRelationService.deleteRelation(bktName, relationType, srcUuid, dstUuid, dstType, reltype);
    }

    /**
     * Query resource relationship.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param dstType Destination resource type
     * @param srcUuids Source uuid
     * @param dstUuids Destination uuid
     * @return The map of relationship
     * @since SDNO 0.5
     */
    @GET
    @Path("/{bucket-name}/resources/{resource-type-name}/relationships")
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> getRelation(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @QueryParam("dst_type") String dstType,
            @QueryParam("src_ids") String srcUuids, @QueryParam("dst_ids") String dstUuids) throws ServiceException {
        return this.mssRelationService.getRelation(bktName, resType, dstType, srcUuids, dstUuids);

    }

    /**
     * Batch add resource.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param request Request input stream body
     * @return The map of resource
     * @since SDNO 0.5
     */
    @POST
    @Path("/{bucket-name}/resources/{resource-type-name}/objects")
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> batchAddResource(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @Context HttpServletRequest request)
            throws ServiceException {
        return this.mssResourceService.batchAddResource(bktName, resType, request);
    }

    /**
     * Batch query data.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param fields Resource property list
     * @param joinAttr JoinAttr
     * @param filter Filter
     * @param sort Sort
     * @param pageSize The size of page
     * @param pageNum The number of page
     * @return The batch query data as string
     * @since SDNO 0.5
     */
    @GET
    @Path("/{bucket-name}/resources/{resource-type-name}/objects")
    @Produces("application/json")
    @Consumes("application/json")
    public String batchGetResouce(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @QueryParam("fields") String fields,
            @QueryParam("joinAttr") String joinAttr, @QueryParam("filter") String filter,
            @QueryParam("sort") String sort, @QueryParam("pagesize") String pageSize,
            @QueryParam("pagenum") String pageNum) {
        return this.mssResourceService.getResources(bktName, resType, fields, joinAttr, filter, sort, pageSize,
                pageNum);
    }

    /**
     * Query relation data.<br/>
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
    @GET
    @Path("/{bucket-name}/resources/{resource-type-name}/relation-objects")
    @Produces("application/json")
    @Consumes("application/json")
    public PageQueryResult<Object> getRelationData(@PathParam("bucket-name") String bktName,
            @PathParam("resource-type-name") String resType, @QueryParam("fields") String fields,
            @QueryParam("filter") String filter, @QueryParam("sort") String sort,
            @QueryParam("pagesize") String pageSize, @QueryParam("pagenum") String pageNum) throws ServiceException {
        return this.mssResourceService.getRelationData(bktName, resType, fields, filter, sort, pageSize, pageNum);
    }

    /**
     * Total number of statistical resources.<br/>
     * 
     * @param bktName Bucket name
     * @param resType Resource type name
     * @param joinAttr JoinAttr
     * @param filter Filter
     * @return The number of statistical resources
     * @since SDNO 0.5
     */
    @GET
    @Path("/{bucket-name}/resources/{resource-type-name}/statistics")
    public int count(@PathParam("bucket-name") final String bktName,
            @PathParam("resource-type-name") final String resType, @QueryParam("joinAttr") final String joinAttr,
            @QueryParam("filter") final String filter) throws ServiceException {
        return this.mssResourceService.commQueryStaticsCount(bktName, resType, joinAttr, filter);
    }

}
