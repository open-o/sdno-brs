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

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.PopMO;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.PopService;
import org.openo.sdno.brs.service.inf.ResWithRelationQueryService;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pop service implement class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-11-30
 */
public class PopServiceImpl implements PopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopServiceImpl.class);

    private String bucketName;

    private String resourceTypeName;

    private IMSSProxy mssProxy;

    private ResWithRelationQueryService popQueryService;

    private ResourceService popOperService;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public IMSSProxy getMssProxy() {
        return mssProxy;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    public ResWithRelationQueryService getPopQueryService() {
        return popQueryService;
    }

    public void setPopQueryService(ResWithRelationQueryService popQueryService) {
        this.popQueryService = popQueryService;
    }

    public ResourceService getPopOperService() {
        return popOperService;
    }

    public void setPopOperService(ResourceService popOperService) {
        this.popOperService = popOperService;
    }

    @Override
    public PopMO query(String objectId) throws ServiceException {
        return popQueryService.getResourceByID(objectId, PopMO.class);
    }

    @Override
    public Map<String, Object> batchQuery(String fields, Map<String, String> filterMap, int pagesize, int pagenum)
            throws ServiceException {
        return popQueryService.getResources(fields, "pops", filterMap, pagesize, pagenum, PopMO.class);
    }

    @Override
    public String getObjectId(PopMO popMO) throws ServiceException {
        return popOperService.genID(popMO);
    }

    @Override
    public PopMO updatePop(String objectId, PopMO popMO) throws ServiceException {
        if(!checkIdExist(objectId)) {
            LOGGER.error("Pop Not Exist");
            throw new ServiceException("Pop Not Exist");
        }
        popMO.setId(null);
        return popOperService.updateResource(objectId, popMO, PopMO.class);
    }

    @Override
    public PopMO addPop(PopMO popMO) throws ServiceException {
        if(checkNameExist(popMO.getName()) || checkIdExist(popMO.getId())) {
            LOGGER.error("Pop Name or Id already exist");
            throw new ServiceException("Pop Name or Id already exist");
        }
        return popOperService.addResource(popMO, PopMO.class);
    }

    @Override
    public Boolean deletePop(String objectId) throws ServiceException {
        if(!checkIdExist(objectId)) {
            LOGGER.info("This pop does not exist, no need to delete");
            return true;
        }
        return popOperService.deleteResource(objectId, PopMO.class);
    }

    private boolean checkNameExist(String name) throws ServiceException {
        Map<String, String> nameKey = new HashMap<String, String>();
        nameKey.put("name", name);
        return checkObjectExist(nameKey);
    }

    private boolean checkIdExist(String objectId) throws ServiceException {
        Map<String, String> idKey = new HashMap<String, String>();
        idKey.put("uuid", objectId);
        return checkObjectExist(idKey);
    }

    private boolean checkObjectExist(Map<String, String> dataKey) throws ServiceException {

        RestfulResponse response = mssProxy.checkObjExsit(bucketName, resourceTypeName, dataKey);
        if(!HttpCode.isSucess(response.getStatus())) {
            LOGGER.error("Check Resource Exist failed");
            throw new ServiceException("Check Resource Exist failed");
        }

        return "true".equalsIgnoreCase(response.getResponseContent());
    }

}
