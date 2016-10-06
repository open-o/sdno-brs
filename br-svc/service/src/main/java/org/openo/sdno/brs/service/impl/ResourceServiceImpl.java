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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.check.inf.BrsChecker;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.util.PagingQueryCheckUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.brs.validator.InputParaValidator.InputParaCheck;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic CRUD service for resources.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private IMSSProxy mssProxy;

    private String bucketName;

    private String resourceTypeName;

    private BrsChecker brsCheckerService;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResource(String objectID, Class<T> type) throws ServiceException {
        RestfulResponse response = mssProxy.getResource(bucketName, resourceTypeName, objectID);
        ResponseUtils.checkResonseAndThrowException(response);

        return (T)HttpResponseUtil.assembleRspData(response.getResponseContent(), type);
    }

    @Override
    public <T extends RootEntity> Object getResourceList(String queryString, String key, Class<T> classType)
            throws ServiceException {
        PagingQueryPara param = PagingQueryCheckUtil.analysicQueryString(queryString);
        return getResourceList(param, key, classType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T extends RootEntity> Object getResourceList(PagingQueryPara param, String key, Class<T> classType)
            throws ServiceException {
        if(param == null) {
            throw new ServiceException();
        }

        PagingQueryCheckUtil.checkPagedParameters(param, classType);

        String fields = null;
        String filters = null;

        try {
            fields = param.getFields();
            filters = HttpResponseUtil.getFilterValue(param.getFiltersMap(), classType);
        } catch(IOException e) {
            LOGGER.error("filters data is error! please check.", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, "filters data is error! please check.");
        }

        int pageSize = param.getPageSize();
        int pageNum = param.getPageNum();
        RestfulResponse response =
                mssProxy.getResourceList(bucketName, resourceTypeName, fields, filters, pageSize, pageNum);
        ResponseUtils.checkResonseAndThrowException(response);

        PageResponseData pageRsp = new PageResponseData();
        Map<String, Object> resourceMap = new HashMap<String, Object>();

        resourceMap.put(key, HttpResponseUtil.assembleListRspData(response.getResponseContent(), pageRsp, classType));
        resourceMap.put(Constant.RESPONSE_TOTALNUM, pageRsp.getTotalNum());
        resourceMap.put(Constant.RESPONSE_PAGESIZE, pageRsp.getPageSize());
        resourceMap.put(Constant.RESPONSE_TOPAGENUM, pageRsp.getTotalPageNum());
        resourceMap.put(Constant.RESPONSE_CURPAGENUM, pageRsp.getCurrentPageNum());

        Map<String, Object> resourceRspMap = new HashMap<String, Object>();
        try {
            resourceRspMap = JsonUtil.unMarshal(JsonUtil.marshal(resourceMap), Map.class);
        } catch(IOException e) {
            LOGGER.error("mss responseContent form is error!", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, "mss responseContent form is error!");
        }

        return resourceRspMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RootEntity> T addResource(T data, Class<T> type) throws ServiceException {
        InputParaCheck.inputParamsCheck(data);

        checkIDIsUsed(data);

        List<Object> list = new ArrayList<Object>();
        Map<String, Object> sendBody = new HashMap<String, Object>();
        list.add(data);
        sendBody.put(Constant.OBJECTS_KEY, list);

        RestfulResponse response = mssProxy.addResources(bucketName, resourceTypeName, sendBody);
        ResponseUtils.checkResonseAndThrowException(response);

        List<T> responseList = (List<T>)HttpResponseUtil.assembleRspData(response.getResponseContent(), type);

        if(CollectionUtils.isEmpty(responseList)) {
            return null;
        }

        return responseList.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T updateResource(String objectID, T data, Class<T> type) throws ServiceException {
        InputParaCheck.inputParamsCheck(data, true);
        RestfulResponse response = mssProxy.updateResource(bucketName, resourceTypeName, objectID, data);
        ResponseUtils.checkResonseAndThrowException(response);

        return (T)HttpResponseUtil.assembleRspData(response.getResponseContent(), type);
    }

    @Override
    public <T> Boolean deleteResource(String objectID, Class<T> classType) throws ServiceException {
        brsCheckerService.checkResourceIsUsed(objectID, classType);
        RestfulResponse response = mssProxy.deleteResource(bucketName, resourceTypeName, objectID);
        ResponseUtils.checkResonseAndThrowException(response);
        return true;
    }

    @Override
    public <T extends RootEntity> String genID(T data) throws ServiceException {
        if(!StringUtils.isEmpty(data.getId())) {
            return data.getId();
        }
        return UuidUtils.createUuid();
    }

    /**
     * @param mssProxy The mssProxy to set.
     */
    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    /**
     * @param bucketName The bucketName to set.
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * @param resourceTypeName The resourceTypeName to set.
     */
    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    /**
     * @param brsCheckerService The brsCheckerService to set.
     */
    public void setBrsCheckerService(BrsChecker brsCheckerService) {
        this.brsCheckerService = brsCheckerService;
    }

    @Override
    public String checkObjExsit(Object key) throws ServiceException {
        RestfulResponse response = mssProxy.checkObjExsit(bucketName, resourceTypeName, key);
        HttpResponseUtil.checkResonseAndThrowException(response);
        return response.getResponseContent();
    }

    private <T extends RootEntity> void checkIDIsUsed(T data) throws ServiceException {
        Map<String, String> checkObj = new HashMap<>();
        checkObj.put(Constant.RESOURCE_ID, data.getId());

        String result = checkObjExsit(checkObj);
        if("true".equals(result)) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_ID_USED, HttpCode.BAD_REQUEST);
        }
    }

    /**
     * get object list<br>
     * 
     * @param fields Fields as string
     * @param filter Filter used to filter the objects we don't need
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @param classType The type of class
     * @return The list of object
     * @since SDNO 0.5
     */
    @Override
    public <T> List<T> getObjectList(String fields, String filter, int pagesize, int pagenum, Class<T> classType)
            throws ServiceException {

        RestfulResponse restfulResponse =
                mssProxy.getResourceListInfo(bucketName, resourceTypeName, fields, filter, pagesize, pagenum);
        ResponseUtils.checkResonseAndThrowException(restfulResponse);

        @SuppressWarnings({"rawtypes"})
        PageResponseData pageRsp = new PageResponseData();
        return HttpResponseUtil.assembleListRspData(restfulResponse.getResponseContent(), pageRsp, classType);
    }
}
