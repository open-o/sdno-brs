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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.CommParamMo;
import org.openo.sdno.brs.service.inf.CommParamService;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.util.CommParamCheckUtil;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service implementation of common parameter for Controller communication.(Support CRUD)<br>
 * 
 * @author
 * @version SDNO 0.5 07-June-2016
 */
public class CommParamServiceImpl implements CommParamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommParamServiceImpl.class);

    private ResourceService commoperService;

    public ResourceService getCommoperService() {
        return commoperService;
    }

    /**
     * @param commoperService The commoperService to set
     */
    public void setCommoperService(ResourceService commoperService) {
        this.commoperService = commoperService;
    }

    /**
     * Create Common parameters for controller<br>
     * 
     * @param commParams - common parameter JSON string
     * @param objectId - Controller UUID
     * @return UUID of the common parameters
     * @throws ServiceException - when parameters already exists or input is invalid
     * @since SDNO 0.5
     */
    @Override
    public String createCommParams(CommParamMo commParams, String objectId) throws ServiceException {
        LOGGER.info("CommParamService.createCommParams() in , parameter hostname is {}",
                commParams == null ? "" : commParams.getHostName());
        if(commParams == null) {
            LOGGER.error("commParams is empty");
            throw generateSvcException("create CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        if(objectId == null) {
            LOGGER.error("objectId is null");
            return null;
        }

        // Validate common parameters
        CommParamCheckUtil.validateCommParams(commParams);

        commParams.setObjectId(objectId);

        // Generate UUID
        String paramID = commParams.getId();
        if(StringUtils.isEmpty(paramID)) {
            paramID = UuidUtils.createUuid();
            commParams.setId(paramID);
        }

        // Check if the common parameters already exist in database
        String[] filterName = {"objectId", "port", "protocol"};
        Object[] filterValue = {commParams.getObjectId(), commParams.getPort(), commParams.getProtocol()};
        String filter = getFilterValue(filterName, filterValue);

        List<CommParamMo> cParamlist = commoperService.getObjectList("base", filter, Constant.PAGE_SIZE_KEY,
                Constant.PAGE_NUM_KEY, CommParamMo.class);
        if(!cParamlist.isEmpty()) {
            LOGGER.error("pk is exist");
            throw generateSvcException("createCommParams fail, the request pk is exist",
                    ErrorCode.BRS_RESOURCE_NAME_EXIST, HttpCode.BAD_REQUEST);
        }

        // Add resource to database
        commoperService.addResource(commParams, CommParamMo.class);

        LOGGER.info("CommParamService.createCommParams() out,paramId is {}", paramID);

        return paramID;
    }

    /**
     * Query common parameters by parameter ID and Object ID<br>
     * 
     * @param paramId - parameter ID (unique ID)
     * @param objectId - Controller UUID
     * @return Common parameter Information
     * @throws ServiceException - when input is invalid or mss service return error
     * @since SDNO 0.5
     */
    @Override
    public CommParamMo queryCommParamsDetail(String paramId, String objectId) throws ServiceException {
        LOGGER.info("CommParamService.queryCommParamsDetail() in, the paramId is: {}", paramId);

        // Parameter ID is itself unique, no need to use objectId
        if(StringUtils.isBlank(paramId)) {
            LOGGER.error("paramId is empty");

        }

        String[] filterName = {"uuid"};
        Object[] filterValue = {paramId};

        String filter = getFilterValue(filterName, filterValue);
        List<CommParamMo> cmList = commoperService.getObjectList("base", filter, Constant.PAGE_SIZE_KEY,
                Constant.PAGE_NUM_KEY, CommParamMo.class);
        if(cmList == null || cmList.isEmpty()) {
            LOGGER.error("paramId is not existed");
            return null;

        }

        if(cmList.size() > 1) {
            LOGGER.error("Duplicate param exist with same paramId");
            return null;
        }

        // Object ID is mismatch, Input is not empty
        if(!StringUtils.isBlank(objectId) && !objectId.equals(cmList.get(0).getObjectId())) {
            LOGGER.error("Object Id mis-match");
            return null;
        }

        return cmList.get(0);
    }

    /**
     * Query common parameter list by Object ID<br>
     * 
     * @param objectid - Controller UUID
     * @return List of Common parameter information
     * @throws ServiceException - when input is invalid or resource do not exist
     * @since SDNO 0.5
     */
    @Override
    public List<CommParamMo> queryCommParamsList(String objectid) throws ServiceException {
        LOGGER.info("CommParamService.queryCommParamsList() in, the objectid is: {}", objectid);

        if(StringUtils.isBlank(objectid)) {
            LOGGER.error("objectid is empty");
            return null;

        }

        String[] filterName = {"objectId"};
        Object[] filterValue = {objectid};

        String filter = getFilterValue(filterName, filterValue);
        List<CommParamMo> cmList = commoperService.getObjectList("base", filter, Constant.PAGE_SIZE_KEY,
                Constant.PAGE_NUM_KEY, CommParamMo.class);
        if(cmList == null || cmList.isEmpty()) {
            LOGGER.error("objectId is not existed");
            return null;

        }

        LOGGER.info("CommParamService.queryCommParamsList() out, cmList size is: {}", cmList.size());

        return cmList;
    }

    /**
     * Update common parameter by controller UUID<br>
     * 
     * @param commParams - Common parameter
     * @param objectId - Controller UUID
     * @throws ServiceException - when input is invalid or MSS service returns error
     * @since SDNO 0.5
     */
    @Override
    public void updateCommParams(CommParamMo commParams, String objectId, String paramId) throws ServiceException {
        LOGGER.info("CommParamService.updateCommParams() in , parameter commparamId is {}",
                commParams == null ? "" : commParams.getCommParams());

        if(StringUtils.isBlank(objectId)) {
            LOGGER.error("objectId is empty");
        }

        if(commParams == null) {
            LOGGER.error("commParams is empty");
            throw generateSvcException("updateCommParams fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        if(StringUtils.isBlank(commParams.getHostName()) || StringUtils.isBlank(commParams.getProtocol())) {
            LOGGER.error("some request params");
            throw generateSvcException("create or update CommParams  fail, some request params is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
        CommParamMo cmparam = queryCommParamsDetail(paramId, objectId);
        if(null == cmparam) {
            LOGGER.error("objectId or commParamId is not existed");
            return;
        }

        if(!cmparam.getObjectId().equals(objectId)) {
            LOGGER.error("objectId is not matching");
            return;
        }

        validateUpdateCommParams(commParams);
        commParams.setObjectId(objectId);

        commoperService.updateResource(paramId, commParams, CommParamMo.class);
    }

    /**
     * Delete common parameters by parameter ID<br>
     * 
     * @param paramId - parameter UUID
     * @throws ServiceException - when input is invalid
     * @since SDNO 0.5
     */
    @Override
    public void deleteCommParams(String paramId) throws ServiceException {
        LOGGER.info("CommParamService.deleteCommParams() in");

        if(StringUtils.isBlank(paramId)) {
            LOGGER.error("paramId is empty");
            throw generateSvcException("delete CommParms  fail, the request paramId is empty", ErrorCode.BRS_BAD_PARAM,
                    HttpCode.BAD_REQUEST);
        }

        String objectId = "";
        CommParamMo delParam = queryCommParamsDetail(paramId, "");
        if(null != delParam) {
            commoperService.deleteResource(paramId, CommParamMo.class);
            objectId = delParam.getObjectId();
        }

        LOGGER.info("CommParamService.deleteCommParams() out, objectID is {}, param Id is {}", objectId, paramId);
    }

    /**
     * Delete common parameter by object ID (controller UUID)<br>
     * 
     * @param objId - Controller UUID
     * @throws ServiceException - when input is invalid
     * @since SDNO 0.5
     */
    @Override
    public void deleteCommParamsByObjId(String objId) throws ServiceException {
        LOGGER.info("CommParamService.deleteCommParamsByObjId() in");

        if(StringUtils.isBlank(objId)) {
            LOGGER.error("paramId is empty");
            throw generateSvcException("delete CommParms  fail, the request objId is empty", ErrorCode.BRS_BAD_PARAM,
                    HttpCode.BAD_REQUEST);
        }

        List<CommParamMo> lstParam = queryCommParamsList(objId);
        for(CommParamMo commMo : lstParam) {
            commoperService.deleteResource(commMo.getObjectId(), CommParamMo.class);
        }

        LOGGER.info("CommParamService.deleteCommParamsByObjId() , objectId is {}", objId);

    }

    private void validateUpdateCommParams(CommParamMo commParams) throws ServiceException {
        if(commParams == null) {
            LOGGER.error("commParams is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
        if(commParams.getHostName() != null && StringUtils.isBlank(commParams.getHostName())) {
            LOGGER.error("Hostname is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        if(commParams.getObjectId() != null && StringUtils.isBlank(commParams.getObjectId())) {
            LOGGER.error("id is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        if(commParams.getProtocol() != null && StringUtils.isBlank(commParams.getProtocol())) {
            LOGGER.error("protocol is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

    }

    private String getFilterValue(String[] filterName, Object[] filterValue) {

        Map<String, String> filter = new HashMap<String, String>();
        Map<String, Object> filterdata = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        String filterVlan = null;
        boolean bFirst = true;
        for(int i = 0; i < filterName.length; ++i) {
            if(!bFirst) {
                sb.append(" and ");
            } else {
                bFirst = false;
            }
            sb.append(filterName[i]).append(" = ':").append(filterName[i]).append('\'');
            filterdata.put(filterName[i], filterValue[i]);
        }
        filter.put("filterDsc", sb.toString());

        try {
            filter.put("filterData", JsonUtil.marshal(filterdata));
            filterVlan = JsonUtil.marshal(filter);
        } catch(IOException e) {
            LOGGER.error("exception in json of filter, filter :{}", filter, e);
        }
        return filterVlan;
    }

    private static ServiceException generateSvcException(String msg, String errorCode, int httpCode) {
        ServiceException exception = new ServiceException(msg);
        exception.setId(errorCode);
        exception.setHttpCode(httpCode);
        return exception;
    }
}
