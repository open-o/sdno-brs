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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.NetworkControlDomainMO;
import org.openo.sdno.brs.service.inf.NetworkControlDomainService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface class of Network Control Domain, provide CRUD service of Network Control Domain
 * resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/network-control-domains")
public class NetworkControlDomainResource extends IResource<NetworkControlDomainService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkControlDomainResource.class);

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/network-control-domains";
    }

    /**
     * get NCD by UUID.<br>
     * 
     * @param objectID UUID of NCD.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Path("{object_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Object getNCDById(@PathParam("object_id") String objectID, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs get NCD Resource by objectID {}.", objectID);

        ValidateUtil.checkUuid(objectID);
        NetworkControlDomainMO data = service.getNCDById(objectID);
        Map<String, Object> dataResult = new HashMap<String, Object>();
        dataResult.put(Constant.NETWORKCONTROLDOMIAN_KEY, data);

        return dataResult;
    }

    /**
     * Get NDC list.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Object getNCDs(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("brs get NCDList Resource.");

        String queryString = request.getQueryString();

        return service.getNCDs(queryString);
    }

    /**
     * Add new NDC.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Object addNCD(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("brs add NCD Resource.");
        String objectId = null;
        NetworkControlDomainMO ncdMO = new NetworkControlDomainMO();
        try {
            String requestStr = RestUtils.getRequestBody(request);
            ncdMO = HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.NETWORKCONTROLDOMIAN_KEY,
                    NetworkControlDomainMO.class);

            objectId = service.getObjectId(ncdMO);
            ncdMO.setId(objectId);

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            NetworkControlDomainMO data = service.addNCD(ncdMO);
            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_CREATETIME, data.getCreatetime());
                reponseBody.put(Constant.NETWORKCONTROLDOMIAN_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Update NDC by UUID.<br>
     * 
     * @param objectId UUID of the NDC need to update.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object updateNCD(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs update NCD Resource by objectID {}.", objectId);

        ValidateUtil.assertNotEmpty(objectId, "objectID");
        ValidateUtil.checkUuid(objectId);

        NetworkControlDomainMO ncdForLog = new NetworkControlDomainMO();
        ncdForLog.setUser(request);
        ncdForLog.setId(objectId);

        try {

            NetworkControlDomainMO ncd = service.getNCDById(objectId);
            if(null != ncd) {
                ncdForLog.setName(ncd.getName());
            }
            Map<String, Object> reponseBody = new HashMap<String, Object>();
            NetworkControlDomainMO data = service.updateNCDByID(objectId,
                    HttpResponseUtil.getDataModelFromReqStr(RestUtils.getRequestBody(request),
                            Constant.NETWORKCONTROLDOMIAN_KEY, NetworkControlDomainMO.class));
            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_UPDATETIME, data.getUpdatetime());
                reponseBody.put(Constant.NETWORKCONTROLDOMIAN_KEY, result);
            }
            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Delete NDC by UUID.<br>
     * 
     * @param objectId UUID of the NDC need to delete.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object delNCD(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs delete NCD Resource by objectID {}.", objectId);

        ValidateUtil.assertNotEmpty(objectId, "objectID");
        ValidateUtil.checkUuid(objectId);

        try {
            NetworkControlDomainMO ncdForLog = new NetworkControlDomainMO();
            ncdForLog.setUser(request);
            ncdForLog.setId(objectId);
            NetworkControlDomainMO ncd = service.getNCDById(objectId);
            if(null != ncd) {
                ncdForLog.setName(ncd.getName());
            }
            Map<String, Object> reponseBody = new HashMap<String, Object>();
            Boolean bResult = service.delNCDByID(objectId);
            if(bResult) {
                reponseBody.put("result", bResult);
            } else {
                reponseBody.put("result", false);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }
}
