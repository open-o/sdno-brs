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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.LogicalTerminationPointMO;
import org.openo.sdno.brs.service.inf.LogicalTPService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface class of logical TP, provide CRUD service of LTP resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/logical-termination-points")
public class LogicalTPResource extends IResource<LogicalTPService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogicalTPResource.class);

    private static final String LOGICAL_TPS = "logicalTerminationPoints";

    private static final String ME_ID = "meID";

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/logical-termination-points";
    }

    /**
     * Get LTP information by UUID.<br>
     * 
     * @param objectID UUID of object need to query.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object getLogicalTerminationPoint(@PathParam("object_id") String objectID,
            @Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("BRS get LogicalTerminationPoint by objectID {}.", objectID);
        ValidateUtil.assertNotEmpty(objectID, "object_id");

        Map<String, Object> logicalTPResult = new HashMap<String, Object>();
        LogicalTerminationPointMO data = service.getLogicalTPByID(objectID);
        logicalTPResult.put(Constant.LOGICALTP_KEY, data);

        return logicalTPResult;
    }

    /**
     * Paged query LTP resource list.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Object getLogicalTerminationPointList(@Context HttpServletRequest request) throws ServiceException {
        String queryString = request.getQueryString();
        return service.getLogicalTPs(queryString, LOGICAL_TPS);
    }

    /**
     * Add new termination point.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Object addLogicalTerminationPoint(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("BRS add LogicalTerminationPoint start.");
        String objectId = null;
        LogicalTerminationPointMO logicalTP = new LogicalTerminationPointMO();
        try {
            String requestStr = RestUtils.getRequestBody(request);
            logicalTP = HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.LOGICALTP_KEY,
                    LogicalTerminationPointMO.class);

            // get object id
            objectId = service.getObjectId(logicalTP);
            logicalTP.setId(objectId);

            checkInterfaceExist(logicalTP);

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            LogicalTerminationPointMO data = service.addLogicalTP(logicalTP);

            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_CREATETIME, data.getCreatetime());
                reponseBody.put(Constant.LOGICALTP_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Update termination point.<br>
     * 
     * @param objectId UUID of the object need to update.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object updateLogicalTerminationPoint(@PathParam("object_id") String objectId,
            @Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("BRS update LogicalTerminationPoint by objectID {}.", objectId);
        ValidateUtil.assertNotEmpty(objectId, "object_id");

        LogicalTerminationPointMO logicalTPForLog = new LogicalTerminationPointMO();
        logicalTPForLog.setUser(request);
        logicalTPForLog.setId(objectId);

        try {
            // get TP information by id.
            LogicalTerminationPointMO logicalTP = service.getLogicalTPByID(objectId);
            if(null != logicalTP) {
                logicalTPForLog.setName(logicalTP.getName());
            }

            String requestStr = RestUtils.getRequestBody(request);
            Map<String, Object> reponseBody = new HashMap<String, Object>();
            LogicalTerminationPointMO logicalTPInfo = service.updateTPByID(objectId, HttpResponseUtil
                    .getDataModelFromReqStr(requestStr, Constant.LOGICALTP_KEY, LogicalTerminationPointMO.class));
            if(null != logicalTPInfo) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, logicalTPInfo.getId());
                result.put(Constant.RESOUCRCE_UPDATETIME, logicalTPInfo.getUpdatetime());

                reponseBody.put(Constant.LOGICALTP_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Delete TP information.<br>
     * 
     * @param objectId UUID of the TP need to delete.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object delLogicalTerminationPoint(@PathParam("object_id") String objectId,
            @Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("BRS delete LogicalTerminationPoint by objectID {}.", objectId);
        ValidateUtil.assertNotEmpty(objectId, "object_id");

        LogicalTerminationPointMO logicalTPForLog = new LogicalTerminationPointMO();
        logicalTPForLog.setUser(request);
        logicalTPForLog.setId(objectId);

        try {

            LogicalTerminationPointMO logicalTP = service.getLogicalTPByID(objectId);
            if(null != logicalTP) {
                logicalTPForLog.setName(logicalTP.getName());
            }

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            Boolean isDelSuccess = service.delTpByID(objectId);
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

    @SuppressWarnings("unchecked")
    private void checkInterfaceExist(LogicalTerminationPointMO tpNew) throws ServiceException {
        String queryString = Constant.RESOURCE_NAME + Constant.EQUIVALENT + tpNew.getName();
        Object result = service.getLogicalTPs(queryString, LOGICAL_TPS);
        if(null != result) {
            Object tps = ((Map<String, Object>)result).get(LOGICAL_TPS);
            if(null != tps) {
                List<Map<String, Object>> tplst = (List<Map<String, Object>>)tps;
                if(tplst.isEmpty()) {
                    return;
                }

                for(Map<String, Object> tpDB : tplst) {
                    String mdID = tpNew.getMeID();
                    String meName = tpNew.getName();
                    boolean isIdEqual = (mdID != null) && mdID.equals(tpDB.get(ME_ID));
                    boolean isMeNameEqual = (meName != null) && meName.equals(tpDB.get(Constant.RESOURCE_NAME));
                    if(isIdEqual && isMeNameEqual) {
                        LOGGER.error("LogicalTPResource::checkInterfaceExist. Interface has exist.");
                        throw new ServiceException(ErrorCode.BRS_RESOURCE_NAME_EXIST, HttpCode.BAD_REQUEST);
                    }
                }
            }
        }
    }
}
