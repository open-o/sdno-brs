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
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.model.roamo.ResTypeEnum;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.StatisticsService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Statistics Service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private IMSSProxy mssProxy;

    private String bucketName;

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    @Override
    public int count(String resType, String tenantId) throws ServiceException {
        try {
            ResTypeEnum resource = ResTypeEnum.getValue(resType);
            switch(resource) {
                case SITE:
                    return doCountSite(resType, tenantId);

                case ME:
                    return doCommonCount(resType);

                default:
                    return doCommonCount(resType);
            }
        } catch(IllegalArgumentException e) {
            LOGGER.error("invalid restype:{}", resType, e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    private int doCommonCount(String resType) throws ServiceException {
        LOGGER.info("count {}", resType);
        RestfulResponse rsp = mssProxy.commonQueryCount(bucketName, resType, null, null);
        ResponseUtils.checkResonseAndThrowException(rsp);

        return Integer.parseInt(rsp.getResponseContent());
    }

    private int doCountSite(String resType, String tenantId) throws ServiceException {
        LOGGER.info("count {} by tenant id:{}.", resType, tenantId);
        checkTenantId(tenantId);
        String filter = null;

        if(tenantId != null && !tenantId.isEmpty()) {
            try {
                Map<String, String> filterMap = new HashMap<String, String>();
                filterMap.put("tenantID", tenantId);
                filter = HttpResponseUtil.getFilterValue(filterMap, SiteMO.class);
            } catch(IOException e) {
                LOGGER.error("get filter failed.", e);
                throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
            }
        }

        RestfulResponse rsp = mssProxy.commonQueryCount(bucketName, resType, null, filter);
        ResponseUtils.checkResonseAndThrowException(rsp);

        return Integer.parseInt(rsp.getResponseContent());
    }

    private void checkTenantId(String tenantId) throws ServiceException {
        if(tenantId != null && tenantId.length() > 36) {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }
}
