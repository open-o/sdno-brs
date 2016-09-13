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

package org.openo.sdno.mss.dao.exception;

import javax.ws.rs.WebApplicationException;

import org.openo.sdno.mss.dao.constant.InvErrorCodeDefine;
import org.openo.sdno.mss.dao.entities.InvRespEntity;

/**
 * Server Inner Exception class.<br>
 * <p>
 * Server internal error,may be caused by bug,system context or environment
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
@SuppressWarnings("serial")
public class ServerInnerException extends WebApplicationException implements IRespEntityException {

    /**
     * entity
     */
    private InvRespEntity<Object> entity;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param cause cause of exception
     * @param error error code
     * @param message error message
     */
    public ServerInnerException(final Throwable cause, InvErrorCodeDefine error, String message) {
        super(cause);
        this.entity = new InvRespEntity<Object>().buildRetCode(error.getErrorCode()).buildMessage(message);
    }

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param error error code
     * @param message error message
     */
    public ServerInnerException(InvErrorCodeDefine error, String message) {
        super();
        this.entity = new InvRespEntity<Object>().buildRetCode(error.getErrorCode()).buildMessage(message);
    }

    /**
     * Get entity attribute.<br>
     * 
     * @return entity attribute
     * @since SDNO 0.5
     */
    @Override
    public InvRespEntity<Object> getEntity() {
        return entity;
    }
}
