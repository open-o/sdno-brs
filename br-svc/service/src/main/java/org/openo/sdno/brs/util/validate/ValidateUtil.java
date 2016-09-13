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

package org.openo.sdno.brs.util.validate;

import java.util.List;

import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;

/**
 * Validate tool class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ValidateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    private ValidateUtil() {

    }

    /**
     * Check if the object is null.<br>
     * 
     * @param object The object to be checked
     * @param name Object name
     * @throws ServiceException if the object is null
     * @since SDNO 0.5
     */
    public static void assertNotNull(Object object, String name) throws ServiceException {
        if(object == null) {
            LOGGER.error("{} can't be null", name);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    /**
     * Check if the parameter is empty.<br>
     * 
     * @param param The parameter to be checked
     * @param name Parameter name
     * @throws ServiceException if the Parameter is null
     * @since SDNO 0.5
     */
    public static void assertNotEmpty(String param, String name) throws ServiceException {
        if(!StringUtils.hasLength(param)) {
            LOGGER.error("{} can't be null or empty", name);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
    }

    /**
     * Check if the object is a list.<br>
     * 
     * @param obj The object to be checked
     * @param name Object name
     * @throws ServiceException if the object is null
     * @since SDNO 0.5
     */
    public static void assertNotList(Object obj, String name) throws ServiceException {
        if(!(obj instanceof List)) {
            LOGGER.error("{} is not list", name);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

    }

    /**
     * Check if the UUID is valid or not.<br>
     * 
     * @param uuid UUID
     * @throws ServiceException if the UUID is not valid
     * @since SDNO 0.5
     */
    public static void checkUuid(String uuid) throws ServiceException {
        if((null != uuid) && (uuid.length() > 0) && uuid.matches("[a-zA-Z0-9\\-\\_]{1,36}")) {
            return;
        }

        LOGGER.error("{} is not right UUID.", uuid);
        String[] detailArgs = {uuid};
        ExceptionArgs args = new ExceptionArgs(null, null, detailArgs, null);
        throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST, args);
    }

    /**
     * Check if the tenant id is null or empty.<br>
     * 
     * @param tenantId Tenant id
     * @throws ServiceException if the tenant id is null or empty
     * @since SDNO 0.5
     */
    public static void checkTenantID(String tenantId) throws ServiceException {
        if((null != tenantId) && (tenantId.length() > 0)) {
            return;
        }
        LOGGER.error("{} is not right tenantId.", tenantId);
        throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
    }
}
