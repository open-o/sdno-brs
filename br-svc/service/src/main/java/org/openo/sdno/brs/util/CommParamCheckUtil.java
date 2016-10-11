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

package org.openo.sdno.brs.util;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.CommParamMo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommParam check utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-22
 */
public class CommParamCheckUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommParamCheckUtil.class);

    private CommParamCheckUtil() {

    }

    /**
     * Validate Communicate Parameters.<br>
     * 
     * @param commParams CommParamMo object
     * @throws ServiceException when CommParamMo is invalid
     * @since SDNO 0.5
     */
    public static void validateCommParams(CommParamMo commParams) throws ServiceException {
        // Check Object ID
        if(StringUtils.isBlank(commParams.getObjectId())) {
            LOGGER.error("objectId is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        checkPort(commParams.getPort());
        checkHostName(commParams.getHostName());
        checkProtocol(commParams.getProtocol());
    }

    private static void checkHostName(String hostName) throws ServiceException {
        // Check host Name
        if(StringUtils.isBlank(hostName)) {
            LOGGER.error("hostname is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        // Check hostName
        if(hostName.length() > 128) {
            LOGGER.error("hostname is too long");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    private static void checkProtocol(String protocol) throws ServiceException {
        // Check protocol
        if(StringUtils.isBlank(protocol)) {
            LOGGER.error("protocal is empty");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        // Validate protocol length
        if(protocol.length() > 32) {
            LOGGER.error("protocal is too long");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    private static void checkPort(String port) throws ServiceException {
        // Check port
        if(StringUtils.isEmpty(port)) {
            LOGGER.error("port is error,port is error");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        // Check Port
        if(Integer.parseInt(port) > 65535) {
            LOGGER.error("port is error,port is too big");
            throw generateSvcException("create or update CommParams  fail, the request commParams is empty",
                    ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    private static ServiceException generateSvcException(String msg, String errorCode, int httpCode) {
        ServiceException e = new ServiceException(msg);
        e.setId(errorCode);
        e.setHttpCode(httpCode);
        return e;
    }
}
