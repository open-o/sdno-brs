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

package org.openo.sdno.mss.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.openo.sdno.mss.combine.intf.InvDataService;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.QueryParamModel;
import org.openo.sdno.mss.service.constant.Constant;
import org.openo.sdno.mss.service.entities.BatchQueryFileterEntity;
import org.openo.sdno.mss.service.intf.MssResourceService;
import org.openo.sdno.mss.service.util.BatchQueryUtil;
import org.openo.sdno.mss.service.util.ExceptionArgsUtil;
import org.openo.sdno.mss.service.util.ParamConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class of MSS service to deal with resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MssResourceServiceImpl implements MssResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MssResourceServiceImpl.class);

    public static final int HTTP_S_CODE_BAD_REQUEST = 400;

    public static final String INVALID_INPUT_PARAM = "1000006";

    private InvDataService invDataService;

    public InvDataService getInvDataService() {
        return invDataService;
    }

    public void setInvDataService(InvDataService invDataService) {
        this.invDataService = invDataService;
    }

    @Override
    public Map<String, Object> getResource(String bktName, String resType, String objectId, String fields)
            throws ServiceException {
        String fieldsTemp = fields;
        if(!StringUtils.isEmpty(fields)) {
            String[] attrArray = StringUtils.split(fields.trim(), ", ");
            int len = attrArray.length;
            if(len == 1) {
                fieldsTemp = ParamConverter.replaceID2UUID(fields);
            } else {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < len; i++) {
                    sb.append(ParamConverter.replaceID2UUID(attrArray[i])).append(Constant.COMMA);
                }
                fieldsTemp = sb.substring(0, sb.length() - 1);
            }
        }
        InvRespEntity<List<Map<String, Object>>> invRespEntity;

        try {
            invRespEntity = this.invDataService.get(bktName, resType, objectId, fieldsTemp);
            ParamConverter.replaceEntitysUUID2ID(invRespEntity);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Get resource error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));
        }

        List<Map<String, Object>> resourceMap = invRespEntity.getData();
        Map<String, Object> ret = new HashMap<String, Object>();
        if(resourceMap != null && !resourceMap.isEmpty()) {
            // The single query method only query a single data, to obtain the first data of the
            // list and return
            ret.put(Constant.OBJECT_KEY, resourceMap.get(0));
        } else {
            ret.put(Constant.OBJECT_KEY, new HashMap<String, Object>());
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getResources(String bktName, String resType, QueryParamModel queryParam) {
        String fields = queryParam.getFields();
        String joinAttr = queryParam.getJoinAttr();
        String filter = queryParam.getFilter();
        String sort = queryParam.getSort();
        String pageSize = queryParam.getPageSize();
        String pageNum = queryParam.getPageNum();

        List<String> attrList = new ArrayList<String>();
        if(!StringUtils.isEmpty(fields)) {
            String[] attrArray = StringUtils.split(fields.trim(), ", ");
            int len = attrArray.length;
            for(int i = 0; i < len; i++) {
                attrList.add(ParamConverter.replaceID2UUID(attrArray[i]));
            }
        } else {
            attrList.add("base");
        }
        String fieldsTemp = JsonUtil.toJson(attrList);

        String joinAttrTemp = joinAttr;
        if(!StringUtils.isEmpty(joinAttr)) {
            List<Map<String, Object>> joinLsit =
                    JsonUtil.fromJson(joinAttr, new TypeReference<List<Map<String, Object>>>() {});
            ParamConverter.replaceMapsID2UUID(joinLsit);
            joinAttrTemp = JsonUtil.toJson(joinLsit);
        }

        String sortTemp = sort;
        if(!StringUtils.isEmpty(sort)) {
            List<String> sortList = JsonUtil.fromJson(sort, new TypeReference<List<String>>() {});
            ParamConverter.replaceListID2UUID(sortList);
            sortTemp = JsonUtil.toJson(sortList);
        }

        BatchQueryFileterEntity filterEntity = null;

        if(StringUtils.isEmpty(filter)) {
            filterEntity = new BatchQueryFileterEntity();
        } else {
            filterEntity = JsonUtil.fromJson(filter, BatchQueryFileterEntity.class);
        }

        List<Object> countList = this.invDataService.commQueryGetCount(bktName, resType, joinAttrTemp,
                filterEntity.getFilterDsc(), filterEntity.getFilterData());

        QueryParamModel newQueryParam = new QueryParamModel(fieldsTemp, joinAttrTemp, filterEntity.getFilterData(),
                sortTemp, pageSize, pageNum);

        Object obj = this.invDataService.commQueryGet(bktName, resType, filterEntity.getFilterDsc(), newQueryParam);
        List<Map<String, Object>> data = ((InvRespEntity<List<Map<String, Object>>>)obj).getData();
        ParamConverter.replaceMapsUUID2ID(data);

        PageQueryResult<Object> resEntity = BatchQueryUtil.getBatchQueryResEntity(pageNum, pageSize, countList);
        resEntity.setObjects(data);

        return JsonUtil.toJson(resEntity);
    }

    @Override
    public Map<String, Object> batchUpdateResource(String bktName, String resType, @Context HttpServletRequest request)
            throws ServiceException {

        String requestStr = RestUtils.getRequestBody(request);

        if(StringUtils.isEmpty(requestStr) || StringUtils.isEmpty(bktName) || StringUtils.isEmpty(resType)) {
            String errorMsg = "Batch update resource request parameter connot be null! ";
            LOGGER.error(errorMsg);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(errorMsg));

        }

        InvRespEntity<List<Map<String, Object>>> invRespEntity;

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> requestMap = JsonUtil.fromJson(requestStr, Map.class);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> listResource = (List<Map<String, Object>>)requestMap.get(Constant.OBJECTS_KEY);

            ParamConverter.replaceMapsID2UUID(listResource);
            invRespEntity = this.invDataService.batchUpdate(bktName, resType, listResource);
            ParamConverter.replaceEntitysUUID2ID(invRespEntity);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Batch update resource error! ";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));

        }

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put(Constant.OBJECTS_KEY, invRespEntity.getData());
        return ret;
    }

    @Override
    public void batchDeleteResources(String bktName, String resType, String uuids) throws ServiceException {
        if(StringUtils.isEmpty(uuids)) {
            LOGGER.error("the ids should not be empty or null");
            throw new ServiceException("mss.emptyids", Constant.BAD_PARAM);
        }

        String[] ids = uuids.split(",");
        this.invDataService.batchDelete(bktName, resType, Arrays.asList(ids));
    }

    @Override
    public Map<String, Object> updateResouce(String bktName, String resType, String objectId,
            @Context HttpServletRequest request) throws ServiceException {

        String requestStr = RestUtils.getRequestBody(request);

        if(StringUtils.isEmpty(requestStr)) {
            String errorMsg = "Update resouce request parameter connot be null!";
            LOGGER.error(errorMsg);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(errorMsg));

        }

        InvRespEntity<Map<String, Object>> invRespEntity;

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> valueObject = JsonUtil.fromJson(requestStr, Map.class);
            ParamConverter.replaceMapUUID2ID(valueObject);
            invRespEntity = this.invDataService.update(bktName, resType, objectId, valueObject);
            ParamConverter.replaceEntityUUID2ID(invRespEntity);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Update resource error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));

        }

        Map<String, Object> ret = new HashMap<String, Object>();
        Object data = invRespEntity.getData();
        ret.put(Constant.OBJECT_KEY, data);

        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> batchAddResource(String bktName, String resType, @Context HttpServletRequest request)
            throws ServiceException {

        String urlBody = RestUtils.getRequestBody(request);

        if(StringUtils.isEmpty(urlBody) || StringUtils.isEmpty(bktName) || StringUtils.isEmpty(resType)) {
            String errorMsg = "Batch add resource request parameter connot be null! ";
            LOGGER.error(errorMsg);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(errorMsg));
        }
        try {
            Map<String, Object> values = JsonUtil.fromJson(urlBody, Map.class);
            List<Map<String, Object>> listValues = (List<Map<String, Object>>)values.get(Constant.OBJECTS_KEY);
            ParamConverter.replaceMapsID2UUID(listValues);
            InvRespEntity<List<Map<String, Object>>> invRespEntity =
                    this.invDataService.add(bktName, resType, listValues);
            ParamConverter.replaceEntitysUUID2ID(invRespEntity);
            Map<String, Object> resourcesMap = new HashMap<String, Object>();
            resourcesMap.put(Constant.OBJECTS_KEY, invRespEntity.getData());
            return resourcesMap;
        } catch(IllegalArgumentException e) {
            String errorMsg = "Batch add resource error! ";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));
        }

    }

    @Override
    public void deleteResouce(String bktName, String resType, String uuid) throws ServiceException {
        try {
            this.invDataService.delete(bktName, resType, uuid);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Delete resource error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageQueryResult<Object> getRelationData(String bktName, String resType, String fields, String filter,
            String sort, String pageSize, String pageNum) throws ServiceException {
        Object obj = null;
        List<String> attrList = new ArrayList<String>();
        if(!StringUtils.isEmpty(fields)) {
            String[] attrArray = StringUtils.split(fields.trim(), ", ");
            int len = attrArray.length;
            for(int i = 0; i < len; i++) {
                attrList.add(ParamConverter.replaceID2UUID(attrArray[i]));
            }
        } else {
            attrList.add("base");
        }

        String sortTemp = sort;
        if(!StringUtils.isEmpty(sort)) {
            List<String> sortList = JsonUtil.fromJson(sort, new TypeReference<List<String>>() {});
            ParamConverter.replaceListID2UUID(sortList);
            sortTemp = JsonUtil.toJson(sortList);
        }

        String fieldsTemp = JsonUtil.toJson(attrList);
        List<Object> countList =
                this.invDataService.queryRelationDataCount(bktName, resType, fieldsTemp, filter, "", pageNum, pageSize);

        PageQueryResult<Object> resEntity = BatchQueryUtil.getBatchQueryResEntity(pageNum, pageSize, countList);

        // When the total number is greater than 0, then check
        if(resEntity.getTotal() > 0) {
            obj = this.invDataService.queryRelationData(bktName, resType, fieldsTemp, filter, sortTemp, pageNum,
                    pageSize);
            ParamConverter.replaceMapsUUID2ID((List<Map<String, Object>>)obj);
        }

        resEntity.setObjects(obj);

        return resEntity;
    }

    @Override
    public Boolean exist(String bktName, String resType, @Context HttpServletRequest request) throws ServiceException {
        String urlBody = RestUtils.getRequestBody(request);
        @SuppressWarnings("unchecked")
        Map<String, Object> values = JsonUtil.fromJson(urlBody, Map.class);

        if(values.size() != 1) {
            throw new ServiceException(INVALID_INPUT_PARAM, HTTP_S_CODE_BAD_REQUEST);
        }

        ParamConverter.replaceMapID2UUID(values);

        // Only leaflet property value, key- attribute name, value- check value, here to get the
        // value of key and value using false traversal method, the actual only one cycle
        String attrName = "";
        Object attrVal = "";
        for(Map.Entry<String, Object> entry : values.entrySet()) {
            attrName = entry.getKey();
            attrVal = entry.getValue();
        }

        return this.invDataService.exist(bktName, resType, attrName, attrVal);
    }

    @Override
    public int commQueryStaticsCount(String bktName, String resType, String joinAttr, String filter)
            throws ServiceException {
        try {
            BatchQueryFileterEntity filterEntity = null;
            if(StringUtils.isEmpty(filter)) {
                filterEntity = new BatchQueryFileterEntity();
            } else {
                filterEntity = JsonUtil.fromJson(filter, BatchQueryFileterEntity.class);
            }
            List<Object> staticsCount = this.invDataService.commQueryGetCount(bktName, resType, joinAttr,
                    filterEntity.getFilterDsc(), filterEntity.getFilterData());
            if(staticsCount == null || staticsCount.isEmpty()) {
                return 0;
            }

            return (int)staticsCount.get(0);
        } catch(IllegalArgumentException e) {
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));
        }
    }
}
