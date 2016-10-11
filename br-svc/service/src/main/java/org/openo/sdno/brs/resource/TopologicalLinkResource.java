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

package org.openo.sdno.brs.resource;

import java.util.HashMap;
import java.util.List;
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
import javax.ws.rs.core.Context;

import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.TopologicalLinkMO;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface of topological link.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/topological-links")
public class TopologicalLinkResource extends IResource<ResourceService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologicalLinkResource.class);

    private static final String TP_LINKS = "topologicalLinks";

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/topological-links";
    }

    /**
     * Get link by UUID.<br>
     * 
     * @param objectID UUID of link.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object getTopologicalLinkByobjectID(@PathParam("object_id") String objectID,
            @Context HttpServletRequest request) throws ServiceException {
        ValidateUtil.assertNotEmpty(objectID, "object_id");
        LOGGER.info("brs get TopologicalLink Resource by objectID {}.", objectID);

        Map<String, Object> topologicalLinkResult = new HashMap<String, Object>();
        TopologicalLinkMO data = service.getResource(objectID, TopologicalLinkMO.class);
        topologicalLinkResult.put(Constant.TOPOLOGICALLINK_KEY, data);

        return topologicalLinkResult;
    }

    /**
     * get link list.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Object getTopologicalLinkList(@Context HttpServletRequest request) throws ServiceException {
        String queryString = request.getQueryString();
        return service.getResourceList(queryString, TP_LINKS, TopologicalLinkMO.class);
    }

    /**
     * add link to DB.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Object addTopologicalLink(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("brs add TopologicalLink Resource.");
        String objectId = null;
        TopologicalLinkMO topologicalLink = new TopologicalLinkMO();
        try {
            String requestStr = RestUtils.getRequestBody(request);
            topologicalLink = HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.TOPOLOGICALLINK_KEY,
                    TopologicalLinkMO.class);

            objectId = service.genID(topologicalLink);
            topologicalLink.setId(objectId);

            checkIfRight(topologicalLink);

            checkLinkExist(topologicalLink);

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            TopologicalLinkMO data = service.addResource(topologicalLink, TopologicalLinkMO.class);

            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_CREATETIME, data.getCreatetime());
                reponseBody.put(Constant.TOPOLOGICALLINK_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {

            throw e;
        }
    }

    /**
     * Update link by UUID.<br>
     * 
     * @param objectId UUID of the link need to update.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object updateTopologicalLinkByobjectID(@PathParam("object_id") String objectId,
            @Context HttpServletRequest request) throws ServiceException {
        ValidateUtil.assertNotEmpty(objectId, "object_id");
        LOGGER.info("brs update TopologicalLink Resource by objectID {}.", objectId);

        TopologicalLinkMO topologicalLinkInfoForLog = new TopologicalLinkMO();
        topologicalLinkInfoForLog.setUser(request);
        topologicalLinkInfoForLog.setId(objectId);

        try {

            TopologicalLinkMO topologicalLink = service.getResource(objectId, TopologicalLinkMO.class);
            if(null != topologicalLink) {
                topologicalLinkInfoForLog.setName(topologicalLink.getName());
            }

            String requestStr = RestUtils.getRequestBody(request);
            Map<String, Object> reponseBody = new HashMap<String, Object>();
            TopologicalLinkMO topologicalLinkInfo =
                    service.updateResource(objectId, HttpResponseUtil.getDataModelFromReqStr(requestStr,
                            Constant.TOPOLOGICALLINK_KEY, TopologicalLinkMO.class), TopologicalLinkMO.class);
            if(null != topologicalLinkInfo) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, topologicalLinkInfo.getId());
                result.put(Constant.RESOUCRCE_UPDATETIME, topologicalLinkInfo.getUpdatetime());

                reponseBody.put(Constant.TOPOLOGICALLINK_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * delete link by UUID.<br>
     * 
     * @param objectId UUID of the link need to delete.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object delTopologicalLink(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs delete TopologicalLink Resource by objectID {}.", objectId);

        TopologicalLinkMO topologicalLinkForLog = new TopologicalLinkMO();
        topologicalLinkForLog.setUser(request);
        topologicalLinkForLog.setId(objectId);

        try {

            TopologicalLinkMO topologicalLink = service.getResource(objectId, TopologicalLinkMO.class);
            if(null != topologicalLink) {
                topologicalLinkForLog.setName(topologicalLink.getName());
            }

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            Boolean isDelSuccess = service.deleteResource(objectId, TopologicalLinkMO.class);
            if(isDelSuccess) {
                reponseBody.put("result", true);
            } else {
                reponseBody.put("result", false);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    private void checkIfRight(TopologicalLinkMO link) throws ServiceException {
        // interface in the same NE can not be the same.
        if(link.getaEndME().equals(link.getzEndME()) && link.getaEnd().equals(link.getzEnd())) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_IF_SAME, HttpCode.BAD_REQUEST);
        }
    }

    /**
     * check if the link exist.<br>
     * 
     * @param linkNew the link need to check.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    private void checkLinkExist(TopologicalLinkMO linkNew) throws ServiceException {
        String queryString = new StringBuilder(Constant.RESOURCE_LOGICAL_TYPE).append(Constant.EQUIVALENT)
                .append(linkNew.getLogicalType()).append(Constant.AND).append(Constant.A_END)
                .append(Constant.EQUIVALENT).append(linkNew.getaEnd()).append(Constant.AND).append(Constant.Z_END)
                .append(Constant.EQUIVALENT).append(linkNew.getzEnd()).toString();
        Object result = service.getResourceList(queryString, TP_LINKS, TopologicalLinkMO.class);
        if(null != result) {
            Object links = ((Map<String, Object>)result).get(TP_LINKS);
            if(null != links) {
                List<Map<String, Object>> linkList = (List<Map<String, Object>>)links;
                if(linkList.isEmpty()) {
                    return;
                }

                for(Map<String, Object> linkDB : linkList) {
                    if(extidIsSame(linkDB, linkNew)) {
                        LOGGER.error("TopologicalLinkResource::checkLinkExist. Link exist.");
                        ExceptionArgs eArgs = new ExceptionArgs();
                        eArgs.setReasonArgs(new String[] {(String)linkDB.get(Constant.RESOURCE_NAME)});
                        throw new ServiceException(ErrorCode.BRS_RESOURCE_LINK_EXIST, HttpCode.BAD_REQUEST, eArgs);
                    }
                }
            }
        }
    }

    private boolean extidIsSame(Map<String, Object> linkDB, TopologicalLinkMO linkNew) {
        String logicalType = linkNew.getLogicalType();
        String aEnd = linkNew.getaEnd();
        String zEnd = linkNew.getzEnd();
        boolean isTypeEqual = (logicalType != null) && logicalType.equals(linkDB.get(Constant.RESOURCE_LOGICAL_TYPE));
        boolean isAEdnEqual = (aEnd != null) && aEnd.equals(linkDB.get(Constant.A_END));
        boolean isZEndEqual = (zEnd != null) && zEnd.equals(linkDB.get(Constant.Z_END));
        if(isTypeEqual && isAEdnEqual && isZEndEqual) {

            return true;
        }

        return false;
    }
}
