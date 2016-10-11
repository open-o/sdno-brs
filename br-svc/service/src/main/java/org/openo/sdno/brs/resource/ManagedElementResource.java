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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.ManagedElementMO;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.service.inf.ManagedElementService;
import org.openo.sdno.brs.util.PagingQueryCheckUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface class of ManagedElement, provide CRUD service of ManagedElement resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/managed-elements")
public class ManagedElementResource extends IResource<ManagedElementService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedElementResource.class);

    /**
     * Get managed element from DB.<br>
     * 
     * @param objectId UUID of object need to query.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object getManagedElement(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("getManagedElement object_id={}", objectId);

        Map<String, Object> managedEleResult = new HashMap<String, Object>();
        ManagedElementMO managedElement = service.getManagedElementByID(objectId);
        managedEleResult.put(Constant.MANAGEDELEMENT_KEY, managedElement);

        return managedEleResult;

    }

    /**
     * Paged query ME list.<br>
     * 
     * @param tenantID UUID of object need to query.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Object getManagedElementList(@QueryParam("tenantID") final String tenantID,
            @Context HttpServletRequest request) throws ServiceException {
        /**
         * if tenantID is not null,query by tenant id.
         */
        if(!StringUtils.isEmpty(tenantID)) {
            LOGGER.info("getManagedElement tenantId={}", tenantID);
            ValidateUtil.checkTenantID(tenantID);
            return service.getManagedElementByTenantId(tenantID);

        } else {
            // if tenantIDis null,paged query NE.
            String queryString = request.getQueryString();
            PagingQueryPara param = PagingQueryCheckUtil.analysicQueryString(queryString);
            PagingQueryCheckUtil.checkPagedParameters(param, ManagedElementMO.class);
            return service.getManagedElementMOs(param.getFields(), param.getFiltersMap(), param.getPageSize(),
                    param.getPageNum());
        }

    }

    /**
     * Add ME to DB.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Object addManagedElement(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.error("Begin to add ManagedElement!");
        String strContent = RestUtils.getRequestBody(request);
        ManagedElementMO managedElement = null;
        String objectId = null;
        try {

            managedElement = getDataFromReq(strContent, Constant.MANAGEDELEMENT_KEY);
            if(null == managedElement) {
                LOGGER.error("addManagedElement: ManagedElement that get from getDataFromReq is null!");
                throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
            }

            // get object id
            objectId = service.getObjectId(managedElement);
            ValidateUtil.checkUuid(objectId);
            managedElement.setId(objectId);

            checkMeData(managedElement);

            Object objME = service.addManagedElement(managedElement);

            return assembleRspData(objME, Constant.ACTION_ADD);

        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Update ME by uuid.<br>
     * 
     * @param objectId UUID of the ME need to update.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object updateManagedElement(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.error("Begin to modify ManagedElement!");

        ValidateUtil.checkUuid(objectId);
        String strContent = RestUtils.getRequestBody(request);
        ManagedElementMO managedElementForLog = new ManagedElementMO();
        managedElementForLog.setUser(request);
        managedElementForLog.setId(objectId);

        try {

            ManagedElementMO managedElement = getDataFromReq(strContent, Constant.MANAGEDELEMENT_KEY);
            if(null == managedElement) {
                LOGGER.error("updateManagedElement: ManagedElement that get from getDataFromReq is null!");
                throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
            }

            ManagedElementMO managedElementExist = service.getManagedElementByID(objectId);
            if(null != managedElementExist) {
                managedElementForLog.setName(managedElementExist.getName());
            }

            Object objME = service.updateManagedElementByID(objectId, managedElement);
            ((ManagedElementMO)objME).setId(objectId);

            return assembleRspData(objME, Constant.ACTION_UPDATE);
        } catch(ServiceException e) {

            throw e;
        }
    }

    /**
     * Delete object by UUID.<br>
     * 
     * @param objectId UUID of the object need to delete.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object delManagedElement(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.error("Begin to delete ManagedElement!");
        ValidateUtil.checkUuid(objectId);

        ManagedElementMO managedElementForLog = new ManagedElementMO();
        managedElementForLog.setUser(request);
        managedElementForLog.setId(objectId);

        try {
            ManagedElementMO managedElement = service.getManagedElementByID(objectId);
            if(null != managedElement) {
                managedElementForLog.setName(managedElement.getName());
            }

            return service.delManagedElementByID(objectId);

        } catch(ServiceException e) {

            throw e;
        }
    }

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/managed-elements";
    }

    /**
     * Get data from request.<br>
     * 
     * @param strRequest request string.
     * @param strKey key of the value.
     * @return ManagedElementMO get from the request body.
     * @throws ServiceException if request body is invalid.
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    private ManagedElementMO getDataFromReq(String strRequest, String strKey) throws ServiceException {

        ValidateUtil.assertNotEmpty(strRequest, strKey);
        Map<String, Object> managedElementMap = JsonUtil.fromJson(strRequest, Map.class);
        if((null == managedElementMap) || (!managedElementMap.containsKey(strKey))) {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        Object objME = managedElementMap.get(strKey);
        String strContent = JsonUtil.toJson(objME);
        ValidateUtil.assertNotEmpty(strContent, strKey);

        return JsonUtil.fromJson(strContent, ManagedElementMO.class);
    }

    private Object assembleRspData(Object objME, String strAction) {
        Map<String, Object> responseData = new HashMap<String, Object>();
        if(null != objME) {
            ManagedElementMO managedElement = (ManagedElementMO)objME;
            Map<String, Object> result = new HashMap<String, Object>();
            result.put(Constant.RESOURCE_ID, managedElement.getId());
            if(strAction.endsWith(Constant.ACTION_ADD)) {
                result.put(Constant.RESOUCRCE_CREATETIME, managedElement.getCreatetime());
            } else {
                result.put(Constant.RESOUCRCE_UPDATETIME, managedElement.getUpdatetime());
            }
            responseData.put(Constant.MANAGEDELEMENT_KEY, result);
        } else {
            LOGGER.error("assembleRspData: objME is null!");
        }

        return responseData;
    }

    @SuppressWarnings("unchecked")
    private void checkMeData(ManagedElementMO meNew) throws ServiceException {
        if(StringUtils.isEmpty(meNew.getIpAddress())) {
            return;
        }

        String queryString = getFilterString(meNew);
        PagingQueryPara param = PagingQueryCheckUtil.analysicQueryString(queryString);
        PagingQueryCheckUtil.checkPagedParameters(param, ManagedElementMO.class);
        Object result = service.getManagedElementMOs(param.getFields(), param.getFiltersMap(), param.getPageSize(),
                param.getPageNum());
        if(null != result) {
            Object mes = ((Map<String, Object>)result).get("managedElements");
            if(null != mes) {
                List<Map<String, Object>> meLst = (List<Map<String, Object>>)mes;
                if(meLst.isEmpty()) {
                    return;
                }

                LOGGER.error("ManagedElementResource::checkMeData. IP conflict.");
                throw new ServiceException(ErrorCode.BRS_RESOURCE_IP_CONFLICT, HttpCode.BAD_REQUEST);
            }
        }
    }

    private String getFilterString(ManagedElementMO me) {
        StringBuilder filter = (new StringBuilder("ipAddress=")).append(me.getIpAddress());

        if(null != me.getManagementDomainID()) {
            filter.append(Constant.AND).append(Constant.RESOURCE_MD).append(Constant.EQUIVALENT)
                    .append(me.getManagementDomainID());
        }

        return filter.toString();
    }
}
