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

import java.util.List;

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
import org.openo.sdno.brs.model.CommParamMo;
import org.openo.sdno.brs.service.inf.CommParamService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.framework.container.service.IResource;

/**
 * Restful service for common parameter for Controller communication<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 07-June-2016
 */
@Path("/sdnobrs/v1/commparammgmt")
public class CommonParamResource extends IResource<CommParamService> {

    /**
     * Get resource URI<br>
     * 
     * @return URI string
     * @since SDNO 0.5
     */
    @Override
    public String getResUri() {
        return "/sdnobrs/v1/commparammgmt";
    }

    /**
     * Create common parameter information<br>
     * 
     * @param objectId - Controller UUID
     * @param request - Common Parameter information
     * @return unique parameter ID
     * @throws ServiceException
     * @since SDNO 0.5
     */
    @POST
    @Path("access-objects/{objectId}/commparams")
    @Consumes("application/json")
    @Produces("application/json")
    public Object createCommParams(@PathParam("objectId") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        String requestStr = RestUtils.getRequestBody(request);
        CommParamMo commonparam =
                HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.COMMONPARAMETER, CommParamMo.class);
        return service.createCommParams(commonparam, objectId);
    }

    /**
     * Query common parameters in detail<br>
     * 
     * @param accessObjId - Controller UUID
     * @param paramId - Parameter ID
     * @return Common parameter information
     * @throws ServiceException -when service throws exception
     * @since SDNO 0.5
     */
    @GET
    @Path("access-objects/{accessObjectId}/commparams/{paramID}")
    @Consumes("application/json")
    @Produces("application/json")
    public CommParamMo queryCommParamsDetail(@PathParam("accessObjectId") String accessObjId,
            @PathParam("paramID") String paramId) throws ServiceException {
        return service.queryCommParamsDetail(paramId, accessObjId);
    }

    /**
     * Query common parameter list by object ID<br>
     * 
     * @param accessObjId - Controller UUID
     * @return List of common parameter information
     * @throws ServiceException -when service throws exception
     * @since SDNO 0.5
     */
    @GET
    @Path("access-objects/{accessObjectId}/commparams")
    @Consumes("application/json")
    @Produces("application/json")
    public List<CommParamMo> queryCommParamsList(@PathParam("accessObjectId") String accessObjId)
            throws ServiceException {
        return service.queryCommParamsList(accessObjId);
    }

    /**
     * Update common parameters by Object ID and common parameters<br>
     * 
     * @param objectId - Controller UUID
     * @param paramId - Parameter id
     * @param request - Common parameters
     * @throws ServiceException -when service throws exception
     * @since SDNO 0.5
     */
    @PUT
    @Path("access-objects/{objectId}/commparams/{paramId}")
    @Consumes("application/json")
    @Produces("application/json")
    public void updateCommParams(@PathParam("objectId") String objectId, @PathParam("paramId") String paramId,
            @Context HttpServletRequest request) throws ServiceException {
        String requestStr = RestUtils.getRequestBody(request);
        CommParamMo commonparam =
                HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.COMMONPARAMETER, CommParamMo.class);
        service.updateCommParams(commonparam, objectId, paramId);
    }

    /**
     * Delete common parameter by parameter ID<br>
     * 
     * @param paramId - Parameter ID
     * @throws ServiceException -when service throws exception
     * @since SDNO 0.5
     */
    @DELETE
    @Path("access-objects/{objectId}/commparams/{param_id}")
    @Consumes("application/json")
    @Produces("application/json")
    public void deleteCommParams(@PathParam("param_id") String paramId) throws ServiceException {
        service.deleteCommParams(paramId);
    }

    /**
     * Delete common parameter by object ID<br>
     * 
     * @param objId - Object ID
     * @throws ServiceException -when service throws exception
     * @since SDNO 0.5
     */
    @DELETE
    @Path("access-objects/{objectId}/commparams")
    @Consumes("application/json")
    @Produces("application/json")
    public void deleteCommParamsByObject(@PathParam("objectId") String objId) throws ServiceException {
        service.deleteCommParamsByObjId(objId);
    }

}
