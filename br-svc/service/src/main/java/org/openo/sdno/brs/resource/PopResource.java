/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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
import org.openo.sdno.brs.model.PopMO;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.service.inf.PopService;
import org.openo.sdno.brs.util.PagingQueryCheckUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface class of Pop, provide CRUD service of Pop resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-11-30
 */
@Path("/sdnobrs/v1/pops")
public class PopResource extends IResource<PopService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopResource.class);

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/pops";
    }

    /**
     * Query Pop by Uuid.<br>
     * 
     * @param objectId Pop Uuid
     * @return Pop queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Map<String, Object> getPop(@PathParam("object_id") String objectId) throws ServiceException {

        ValidateUtil.checkUuid(objectId);

        Map<String, Object> resultMap = new HashMap<>();
        PopMO popMO = service.query(objectId);
        resultMap.put(Constant.POP_KEY, (null != popMO) ? popMO : new HashMap<String, String>());

        return resultMap;
    }

    /**
     * Batch Query Pops.<br>
     * 
     * @param request HttpServletRequest Object
     * @return List of Pops queried out
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> getPopList(@Context HttpServletRequest request) throws ServiceException {
        String queryString = request.getQueryString();
        PagingQueryPara param = PagingQueryCheckUtil.analysicQueryString(queryString);
        PagingQueryCheckUtil.checkPagedParameters(param, PopMO.class);
        return service.batchQuery(param.getFields(), param.getFiltersMap(), param.getPageSize(), param.getPageNum());
    }

    /**
     * Create Pop Object.<br>
     * 
     * @param request HttpServletRequest Object
     * @return create result
     * @throws ServiceException when add failed
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Map<String, Object> addPop(@Context HttpServletRequest request) throws ServiceException {

        String requestBody = RestUtils.getRequestBody(request);
        PopMO popMO = HttpResponseUtil.getDataModelFromReqStr(requestBody, Constant.POP_KEY, PopMO.class);
        if(null == popMO) {
            LOGGER.error("addPop: PopMO that get from getDataFromReq is null!!");
            throw new ServiceException("PopMO that get from getDataFromReq is null");
        }

        popMO.setId(service.getObjectId(popMO));
        PopMO resultPopMO = service.addPop(popMO);

        Map<String, Object> resultObject = new HashMap<>();
        resultObject.put(Constant.RESOURCE_ID, resultPopMO.getId());
        resultObject.put(Constant.RESOUCRCE_CREATETIME, resultPopMO.getCreatetime());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constant.POP_KEY, resultObject);

        return resultMap;
    }

    /**
     * Delete Pop Object.<br>
     * 
     * @param objectId Pop Object Uuid
     * @return delete result
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Map<String, Object> deletePop(@PathParam("object_id") String objectId) throws ServiceException {
        ValidateUtil.checkUuid(objectId);

        Boolean result = service.deletePop(objectId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result ? true : false);

        return resultMap;
    }

    /**
     * Update Pop Object.<br>
     * 
     * @param objectId Pop Object Uuid
     * @param request HttpServletRequest Object
     * @return update result
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Map<String, Object> updatePop(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {

        ValidateUtil.checkUuid(objectId);

        PopMO popMO = HttpResponseUtil.getDataModelFromReqStr(RestUtils.getRequestBody(request), Constant.POP_KEY,
                PopMO.class);
        if(null == popMO) {
            LOGGER.error("UpdatePop: PopMO that get from getDataFromReq is null!!");
            throw new ServiceException("PopMO that get from getDataFromReq is null");
        }

        PopMO resultPopMO = service.updatePop(objectId, popMO);

        Map<String, Object> resultObject = new HashMap<>();
        resultObject.put(Constant.RESOURCE_ID, resultPopMO.getId());
        resultObject.put(Constant.RESOUCRCE_UPDATETIME, resultPopMO.getUpdatetime());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constant.POP_KEY, resultObject);

        return resultMap;
    }

}
