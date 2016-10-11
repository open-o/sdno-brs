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
import org.openo.sdno.brs.model.ControllerMo;
import org.openo.sdno.brs.service.inf.ControllerService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.framework.container.service.IResource;

/**
 * controller service resource class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-6-7
 */

@Path("/sdnobrs/v1/controller")
public class ControllerResource extends IResource<ControllerService> {

    /**
     * add controller.<br>
     * 
     * @param request HTTP request.
     * @return UUID of the added controller.
     * @throws ServiceException if add controller failed.
     * @since SDNO 0.5
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public String addAccessObject(@Context HttpServletRequest request) throws ServiceException {
        String requestStr = RestUtils.getRequestBody(request);
        ControllerMo controller =
                HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.CONTROLLER, ControllerMo.class);
        return service.addController(controller);
    }

    /**
     * query controller by UUID.<br>
     * 
     * @param objectId UUID of controller.
     * @return controller of the given UUID.
     * @throws ServiceException if exception happens in data base.
     * @since SDNO 0.5
     */
    @GET
    @Path("/{objectId}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public ControllerMo queryControllerMO(@PathParam("objectId") String objectId) throws ServiceException {
        return service.queryController(objectId);
    }

    /**
     * update controller by UUID.<br>
     * 
     * @param objectId UUID of controller.
     * @param request HTTP request.
     * @throws ServiceException if exception happens in data base.
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{objectId}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public void modifyController(@PathParam("objectId") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        String requestStr = RestUtils.getRequestBody(request);
        ControllerMo controller =
                HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.CONTROLLER, ControllerMo.class);
        service.modifyController(objectId, controller);
    }

    /**
     * delete controller by UUID.<br>
     * 
     * @param objectId UUID of the controller.
     * @throws ServiceException if exception happens in data base.
     * @since SDNO 0.5
     */
    @DELETE
    @Path("/{objectId}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public void deleteController(@PathParam("objectId") String objectId) throws ServiceException {
        service.deleteController(objectId);
    }

    /**
     * query the URL.<br>
     * 
     * @return uri of this service.
     * @since SDNO 0.5
     */
    @Override
    public String getResUri() {

        return "/sdnobrs/v1/controller";
    }
}
