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

package org.openo.sdno.brs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.RelationService;
import org.openo.sdno.brs.service.inf.ResWithRelationQueryService;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.service.inf.SiteService;
import org.openo.sdno.brs.util.RelationUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * site service implementation class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    private String bucketName;

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    private ResWithRelationQueryService siteQueryService;

    private ResourceService siteOperService;

    private RelationService relationService;

    @Override
    public SiteMO getSiteByID(String objectID) throws ServiceException {
        return siteQueryService.getResourceByID(objectID, SiteMO.class);
    }

    @Override
    public Map<String, Object> getSiteMOs(String fields, Map<String, String> filterMap, int pagesize, int pagenum)
            throws ServiceException {
        return siteQueryService.getResources(fields, "sites", filterMap, pagesize, pagenum, SiteMO.class);
    }

    @Override
    public String getObjectId(SiteMO site) throws ServiceException {
        return siteOperService.genID(site);
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public void setSiteQueryService(ResWithRelationQueryService siteQueryService) {
        this.siteQueryService = siteQueryService;
    }

    public void setSiteOperService(ResourceService siteOperService) {
        this.siteOperService = siteOperService;
    }

    public void setRelationService(RelationService relationService) {
        this.relationService = relationService;
    }

    @Override
    public SiteMO updateSiteByID(String objectID, SiteMO site) throws ServiceException {

        updateRelation(objectID, site.getTenantSiteIDs());

        SiteMO siteDB = updateBaseAndExtAttr(objectID, site);
        if(null == siteDB) {
            siteDB = new SiteMO();
            siteDB.setId(objectID);
            siteDB.setUpdatetime(System.currentTimeMillis());
        }

        return siteDB;
    }

    @Override
    public SiteMO addSite(SiteMO site) throws ServiceException {

        checkNameIsExist(site);

        List<String> tenantSiteLst = new LinkedList<>();
        if(!CollectionUtils.isEmpty(site.getTenantSiteIDs())) {
            tenantSiteLst.addAll(site.getTenantSiteIDs());
        }

        site.setTenantSiteIDs(null);
        SiteMO siteDB = siteOperService.addResource(site, SiteMO.class);

        addTenantSites(tenantSiteLst, siteDB);
        return siteDB;
    }

    @Override
    public Boolean delSiteByID(String objectID) throws ServiceException {
        return siteOperService.deleteResource(objectID, SiteMO.class);
    }

    private void checkNameIsExist(SiteMO site) throws ServiceException {
        Map<String, String> checkObj = new HashMap<>();
        checkObj.put("name", site.getName());
        checkObjExsit(checkObj);
    }

    /**
     * Check whether is the given object exist.<br>
     * 
     * @param key Object to check
     * @throws ServiceException if data base service encounter some problem
     * @since SDNO 0.5
     */
    public void checkObjExsit(Object key) throws ServiceException {
        RestfulResponse response = mssProxy.checkObjExsit(bucketName, resourceTypeName, key);
        HttpResponseUtil.checkResonseAndThrowException(response);
        if("true".equals(response.getResponseContent())) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_NAME_EXIST, HttpCode.BAD_REQUEST);
        }
    }

    private void addTenantSites(List<String> tenantSiteLst, SiteMO site) throws ServiceException {
        if(CollectionUtils.isEmpty(tenantSiteLst)) {
            return;
        }

        LOGGER.info("addTenantSites: begin to add.");

        String strSrcID = site.getId();
        List<Relation> lstRelation = new ArrayList<Relation>();
        for(String strDstID : tenantSiteLst) {
            Relation relation = new Relation();
            relation.setSrcId(strSrcID);
            relation.setDstId(strDstID);
            relation.setDstType(Constant.SITE_KEY);
            relation.setRelation(Constant.REALTION_ASSOCIATION);
            lstRelation.add(relation);
        }

        relationService.createRelation(lstRelation);

        site.setTenantSiteIDs(tenantSiteLst);
    }

    private void updateRelation(String objectID, List<String> relations) throws ServiceException {
        if(null == relations) {
            return;
        }

        List<Relation> relationsDB = relationService.getRelations("site", objectID, null);
        List<Relation> relationReq = RelationUtil.convertRelation(relations, objectID, Constant.SITE_KEY);

        relationService.delRelation(objectID, relationReq, relationsDB);

        List<Relation> lstAdd = new ArrayList<Relation>();
        for(Relation relation : relationReq) {
            if(relationsDB.contains(relation)) {
                continue;
            }

            if(!StringUtils.isEmpty(relation.getDstId())) {
                lstAdd.add(relation);
            }
        }
        relationService.createRelation(lstAdd);
    }

    private SiteMO updateBaseAndExtAttr(String siteID, SiteMO site) throws ServiceException {
        SiteMO siteDB = null;
        if(bUpdateBaseAndExtAttr(site)) {
            site.setTenantSiteIDs(null);
            siteDB = siteOperService.updateResource(siteID, site, SiteMO.class);
        }

        return siteDB;
    }

    private boolean bUpdateBaseAndExtAttr(SiteMO site) {
        return (null != site.getDescription()) || (null != site.getLocation());
    }
}
