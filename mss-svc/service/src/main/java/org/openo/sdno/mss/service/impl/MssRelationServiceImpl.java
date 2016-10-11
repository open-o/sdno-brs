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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.combine.intf.InvRelationDataService;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.service.constant.Constant;
import org.openo.sdno.mss.service.intf.MssRelationService;
import org.openo.sdno.mss.service.util.ExceptionArgsUtil;
import org.openo.sdno.mss.service.util.ParamConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class of MSS service to deal with relation.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MssRelationServiceImpl implements MssRelationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MssRelationServiceImpl.class);

    private InvRelationDataService invRelationDataService;

    public void setInvRelationDataService(InvRelationDataService invRelationDataService) {
        this.invRelationDataService = invRelationDataService;
    }

    @Override
    public Map<String, Object> getRelation(String bktName, String srcResType, String dstResType, String srcUuids,
            String dstUuids) throws ServiceException {
        InvRespEntity<List<Map<String, Object>>> invRespEntity;

        try {
            invRespEntity = this.invRelationDataService.get(bktName, srcResType, dstResType, srcUuids, dstUuids);
            ParamConverter.replaceEntitysUUID2ID(invRespEntity);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Get relation error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));
        }

        Map<String, Object> relationMap = new HashMap<String, Object>();
        relationMap.put(Constant.RELATIONS_KEY, invRespEntity.getData());

        return relationMap;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void addRelation(String bktName, String relationType, @Context HttpServletRequest request)
            throws ServiceException {
        String requestStr = RestUtils.getRequestBody(request);

        if(StringUtils.isEmpty(requestStr) || StringUtils.isEmpty(relationType)) {
            String errorMsg = "Add relation request parameter connot be null!";
            LOGGER.error(errorMsg);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(errorMsg));
        }

        Map<String, Object> requestMap = JsonUtil.fromJson(requestStr, Map.class);

        Object objectsValue = requestMap.get(Constant.RELATIONS_KEY);
        List<Map<String, Object>> relationValues = (List<Map<String, Object>>)objectsValue;
        ParamConverter.replaceMapsID2UUID(relationValues);
        try {
            for(Map<String, Object> relation : relationValues) {
                StringBuilder srcDstTpye = new StringBuilder();
                srcDstTpye.append(relationType).append('-').append(relation.get(Constant.DST_TYPE));
                List<Map<String, Object>> values = Collections.singletonList(relation);
                this.invRelationDataService.add(bktName, srcDstTpye.toString(), values);
            }
        } catch(IllegalArgumentException e) {
            String errorMsg = "Add relation error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));

        }

    }

    @Override
    public void deleteRelation(String bktName, String relationType, String srcUuid, String dstUuid, String dstType,
            String reltype) throws ServiceException {
        if(StringUtils.isEmpty(relationType) || StringUtils.isEmpty(dstType)) {
            String errorMsg = "Delete relation request parameter connot be null! ";
            LOGGER.error(errorMsg);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(errorMsg));

        }

        StringBuilder srcDstType = new StringBuilder();
        srcDstType.append(relationType).append('-').append(dstType);

        try {
            this.invRelationDataService.delete(bktName, srcDstType.toString(), srcUuid, dstUuid, reltype);
        } catch(IllegalArgumentException e) {
            String errorMsg = "Delete relation error!";
            LOGGER.error(errorMsg, e);
            throw new ServiceException(null, Constant.SERVICE_ERROR_CODE, ExceptionArgsUtil.getExceptionArgs(e));

        }
    }

}
