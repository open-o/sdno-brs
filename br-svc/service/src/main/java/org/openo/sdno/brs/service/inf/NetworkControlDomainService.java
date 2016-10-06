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

package org.openo.sdno.brs.service.inf;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IService;

import org.openo.sdno.brs.model.NetworkControlDomainMO;

/**
 * Interface of network controller domain service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface NetworkControlDomainService extends IService {

    /**
     * Get NCDs.<br>
     * 
     * @param field The field to query
     * @return The NCD object
     * @since SDNO 0.5
     */
    Object getNCDs(String field) throws ServiceException;

    /**
     * Get object Id.<br>
     * 
     * @param ncdMO The NetworkControlDomainMO object
     * @return The object Id
     * @since SDNO 0.5
     */
    String getObjectId(NetworkControlDomainMO ncdMO) throws ServiceException;

    /**
     * Add NCD.<br>
     * 
     * @param ncdMO The NetworkControlDomainMO object
     * @return The NetworkControlDomainMO object
     * @since SDNO 0.5
     */
    NetworkControlDomainMO addNCD(NetworkControlDomainMO ncdMO) throws ServiceException;

    /**
     * Get NCD according Id.<br>
     * 
     * @param objectId The object Id
     * @return The NetworkControlDomainMO object
     * @since SDNO 0.5
     */
    NetworkControlDomainMO getNCDById(String objectId) throws ServiceException;

    /**
     * Update NCD according Id.<br>
     * 
     * @param objectId The object Id
     * @param dataModelFromReqStr Data model from request
     * @return The NetworkControlDomainMO object
     * @since SDNO 0.5
     */
    NetworkControlDomainMO updateNCDByID(String objectId, NetworkControlDomainMO dataModelFromReqStr)
            throws ServiceException;

    /**
     * Delete NCD according Id.<br>
     * 
     * @param objectId The object Id
     * @return true when delete success
     *         false when delete failed
     * @since SDNO 0.5
     */
    Boolean delNCDByID(String objectId) throws ServiceException;

}
