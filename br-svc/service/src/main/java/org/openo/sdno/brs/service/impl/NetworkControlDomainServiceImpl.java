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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.NetworkControlDomainMO;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.RelationField;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.NetworkControlDomainService;
import org.openo.sdno.brs.service.inf.RelationService;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class of network control domain service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class NetworkControlDomainServiceImpl implements NetworkControlDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    private String bucketName;

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    private Map<String, List<String>> relationInfoMap;

    private ResourceService ncdOpreateService;

    private RelationService meRelationService;

    public RelationService getMeRelationService() {
        return meRelationService;
    }

    public void setMeRelationService(RelationService meRelationService) {
        this.meRelationService = meRelationService;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public IMSSProxy getMssProxy() {
        return mssProxy;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public ResourceService getNcdOpreateService() {
        return ncdOpreateService;
    }

    public void setNcdOpreateService(ResourceService ncdOpreateService) {
        this.ncdOpreateService = ncdOpreateService;
    }

    @Override
    public String getObjectId(NetworkControlDomainMO ncdMO) throws ServiceException {
        return ncdOpreateService.genID(ncdMO);
    }

    @Override
    public NetworkControlDomainMO addNCD(NetworkControlDomainMO ncdMO) throws ServiceException {

        checkNameIsExist(ncdMO);

        List<String> meIDList = new ArrayList<String>();
        if(!CollectionUtils.isEmpty(ncdMO.getManagementElementIDs())) {
            meIDList.addAll(ncdMO.getManagementElementIDs());
        }

        ncdMO.setManagementElementIDs(null);
        NetworkControlDomainMO ncdDB = ncdOpreateService.addResource(ncdMO, NetworkControlDomainMO.class);

        addMERelation(meIDList, ncdDB);
        return ncdMO;

    }

    private void addMERelation(List<String> meIDList, NetworkControlDomainMO ncd) throws ServiceException {
        if(CollectionUtils.isEmpty(meIDList)) {
            return;
        }

        LOGGER.info("addTenantSites: begin to add.");

        String dstID = ncd.getId();
        List<Relation> lstRelation = convertRelation(meIDList, dstID, resourceTypeName);
        meRelationService.createRelation(lstRelation);

        ncd.setManagementElementIDs(meIDList);
    }

    private void checkNameIsExist(NetworkControlDomainMO ncd) throws ServiceException {
        Map<String, String> checkObj = new HashMap<>();
        checkObj.put("name", ncd.getName());
        checkObjExsit(checkObj);
    }

    /**
     * Check object exsit.<br>
     * 
     * @param key Key object
     * @since SDNO 0.5
     */
    public void checkObjExsit(Object key) throws ServiceException {
        RestfulResponse response = mssProxy.checkObjExsit(bucketName, resourceTypeName, key);
        ResponseUtils.checkResonseAndThrowException(response);
        if("true".equals(response.getResponseContent())) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_ID_USED, HttpCode.BAD_REQUEST);
        }
    }

    @Override
    public NetworkControlDomainMO getNCDById(String objectId) throws ServiceException {
        NetworkControlDomainMO ncdDB = ncdOpreateService.getResource(objectId, NetworkControlDomainMO.class);

        setRelation(ncdDB);
        return ncdDB;
    }

    private <T extends RootEntity> void setRelation(T resource) throws ServiceException {
        if(resource == null || StringUtils.isEmpty(resource.getId())) {
            return;
        }

        initRelationInfoMap(resource.getClass());
        for(String relationField : relationInfoMap.keySet()) {
            String modelName = relationInfoMap.get(relationField).get(0);
            String paraName = relationInfoMap.get(relationField).get(1);

            List<String> data = new ArrayList<String>();
            List<Relation> relationsDB = meRelationService.getRelations(modelName, null, resource.getId());
            for(Relation relation : relationsDB) {
                data.add(relation.getSrcId());
            }
            try {
                Field field = resource.getClass().getDeclaredField(paraName);
                if(!CollectionUtils.isEmpty(data)) {
                    field.setAccessible(true);
                    if(field.getType().equals(String.class)) {
                        field.set(resource, data.get(0));
                        continue;
                    }

                    field.set(resource, data);
                }
            } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                LOGGER.info("setRelationInfo failed,ex={}", e);
            }
        }
    }

    private void initRelationInfoMap(Class<?> classType) {
        if(null != relationInfoMap) {
            return;
        }
        relationInfoMap = new HashMap<String, List<String>>();
        Field[] fields = classType.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(RelationField.class)) {
                List<String> infoList = new ArrayList<String>();
                RelationField relationField = field.getAnnotation(RelationField.class);
                String dbName = relationField.dbName();
                infoList.add(relationField.modelName());
                infoList.add(relationField.paraName());
                relationInfoMap.put(dbName, infoList);
            }
        }

    }

    @Override
    public NetworkControlDomainMO updateNCDByID(String objectId, NetworkControlDomainMO ncdReq) throws ServiceException {

        updateRelation(objectId, ncdReq.getManagementElementIDs());

        ncdReq.setManagementElementIDs(null);
        return ncdOpreateService.updateResource(objectId, ncdReq, NetworkControlDomainMO.class);

    }

    private void updateRelation(String objectID, List<String> relations) throws ServiceException {
        if(null == relations) {
            return;
        }

        List<Relation> relationsDB = meRelationService.getRelations(resourceTypeName, null, objectID);
        List<Relation> relationReq = convertRelation(relations, objectID, resourceTypeName);

        meRelationService.delRelation(objectID, relationReq, relationsDB);

        List<Relation> lstAdd = new ArrayList<Relation>();
        for(Relation relation : relationReq) {
            if(relationsDB.contains(relation)) {
                continue;
            }

            if(!StringUtils.isEmpty(relation.getDstId())) {
                lstAdd.add(relation);
            }
        }
        meRelationService.createRelation(lstAdd);
    }

    private List<Relation> convertRelation(List<String> relations, String objectID, String dstType) {
        if(null == relations) {
            return null;
        }

        List<Relation> lstRelation = new ArrayList<Relation>();
        for(String srcID : relations) {
            Relation relation = new Relation();
            relation.setSrcId(srcID);
            relation.setDstId(objectID);
            relation.setDstType(dstType);
            relation.setRelation(Constant.REALTION_ASSOCIATION);
            lstRelation.add(relation);
        }

        return lstRelation;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getNCDs(String queryString) throws ServiceException {
        Map<String, Object> result =
                (Map<String, Object>)ncdOpreateService.getResourceList(queryString, Constant.NETWORKCONTROLDOMIAN_KEY,
                        NetworkControlDomainMO.class);
        Object ncdData = result.get(Constant.NETWORKCONTROLDOMIAN_KEY);
        try {
            List<NetworkControlDomainMO> ncdList =
                    JsonUtil.unMarshal(JsonUtil.marshal(ncdData), new TypeReference<List<NetworkControlDomainMO>>() {});

            if(!CollectionUtils.isEmpty(ncdList)) {
                for(NetworkControlDomainMO ncd : ncdList) {
                    setRelation(ncd);
                }
            }

            result.put(Constant.NETWORKCONTROLDOMIAN_KEY, ncdList);
        } catch(IOException e) {
            LOGGER.error("parse ncd rsp data error.", e);
        }

        return result;
    }

    @Override
    public Boolean delNCDByID(String objectId) throws ServiceException {

        List<Relation> relationsDB = meRelationService.getRelations(resourceTypeName, null, objectId);
        meRelationService.delRelation(null, new ArrayList<Relation>(), relationsDB);

        return ncdOpreateService.deleteResource(objectId, NetworkControlDomainMO.class);
    }
}
