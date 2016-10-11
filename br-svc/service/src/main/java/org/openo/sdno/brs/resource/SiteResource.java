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
import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.service.inf.SiteService;
import org.openo.sdno.brs.util.PagingQueryCheckUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.service.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restful interface class of Site, provide CRUD service of Site resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/sites")
public class SiteResource extends IResource<SiteService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteResource.class);

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/sites";
    }

    /**
     * Get site by UUID.<br>
     * 
     * @param objectID UUID of the site.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object getSite(@PathParam("object_id") String objectID, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs get Site Resource by objectID {}.", objectID);

        ValidateUtil.assertNotEmpty(objectID, "objectID");
        SiteMO data = service.getSiteByID(objectID);
        Map<String, Object> dataResult = new HashMap<String, Object>();
        dataResult.put(Constant.SITE_KEY, data);

        return dataResult;
    }

    /**
     * paged query site list.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public Object getSiteList(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("brs get SiteList Resource.");
        String queryString = request.getQueryString();
        PagingQueryPara param = PagingQueryCheckUtil.analysicQueryString(queryString);
        PagingQueryCheckUtil.checkPagedParameters(param, SiteMO.class);
        return service.getSiteMOs(param.getFields(), param.getFiltersMap(), param.getPageSize(), param.getPageNum());
    }

    /**
     * Add new site.<br>
     * 
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Object addSite(@Context HttpServletRequest request) throws ServiceException {
        LOGGER.info("brs add Site Resource.");
        String objectId = null;
        SiteMO site = new SiteMO();
        try {
            String requestStr = RestUtils.getRequestBody(request);
            site = HttpResponseUtil.getDataModelFromReqStr(requestStr, Constant.SITE_KEY, SiteMO.class);

            objectId = service.getObjectId(site);
            site.setId(objectId);

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            SiteMO data = service.addSite(site);
            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_CREATETIME, data.getCreatetime());
                reponseBody.put(Constant.SITE_KEY, result);
            }

            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * Update site by UUID.<br>
     * 
     * @param objectId UUID of the site need to update.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object updateSite(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs update Site Resource by objectID {}.", objectId);

        ValidateUtil.assertNotEmpty(objectId, "objectID");
        org.openo.sdno.brs.util.validate.ValidateUtil.checkUuid(objectId);

        SiteMO siteForLog = new SiteMO();
        siteForLog.setUser(request);
        siteForLog.setId(objectId);

        try {

            SiteMO site = service.getSiteByID(objectId);
            if(null != site) {
                siteForLog.setName(site.getName());
            }

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            SiteMO data = service.updateSiteByID(objectId, HttpResponseUtil
                    .getDataModelFromReqStr(RestUtils.getRequestBody(request), Constant.SITE_KEY, SiteMO.class));
            if(null != data) {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Constant.RESOURCE_ID, data.getId());
                result.put(Constant.RESOUCRCE_UPDATETIME, data.getUpdatetime());
                reponseBody.put(Constant.SITE_KEY, result);
            }
            return reponseBody;
        } catch(ServiceException e) {
            throw e;
        }
    }

    /**
     * delete site by UUID.<br>
     * 
     * @param objectId UUID of the site need to delete.
     * @param request context of HTTP request.
     * @return object get from data base.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @DELETE
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{object_id}")
    public Object deleteSite(@PathParam("object_id") String objectId, @Context HttpServletRequest request)
            throws ServiceException {
        LOGGER.info("brs delete Site Resource by objectID {}.", objectId);

        ValidateUtil.assertNotEmpty(objectId, "objectID");
        org.openo.sdno.brs.util.validate.ValidateUtil.checkUuid(objectId);

        try {
            SiteMO siteForLog = new SiteMO();
            siteForLog.setUser(request);
            siteForLog.setId(objectId);
            SiteMO site = service.getSiteByID(objectId);
            if(null != site) {
                siteForLog.setName(site.getName());
            }

            Map<String, Object> reponseBody = new HashMap<String, Object>();
            Boolean bResult = service.delSiteByID(objectId);
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
