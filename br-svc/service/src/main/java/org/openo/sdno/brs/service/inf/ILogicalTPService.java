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

import org.openo.sdno.brs.model.LogicalTerminationPointMO;

/**
 * LogicalTP service interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface ILogicalTPService extends IService {

    /**
     * Get logical Tp by Id.<br>
     * 
     * @param objectID Object Id
     * @return The logical Tp
     * @since SDNO 0.5
     */
    LogicalTerminationPointMO getLogicalTPByID(String objectID) throws ServiceException;

    /**
     * Get TP list.<br>
     * 
     * @param queryString Query string object
     * @param key The query key
     * @return The logical TPs
     * @since SDNO 0.5
     */
    Object getLogicalTPs(String queryString, String key) throws ServiceException;

    /**
     * Get Id of Tp.<br>
     * 
     * @param logicalTP The logical Tp
     * @return The Id of Tp
     * @since SDNO 0.5
     */
    String getObjectId(LogicalTerminationPointMO logicalTP) throws ServiceException;

    /**
     * Update the Tp information though the Id.<br>
     * 
     * @param objectID The object Id
     * @param logicalTP The logical Tp
     * @return Logical Tp object
     * @since SDNO 0.5
     */
    LogicalTerminationPointMO updateTPByID(String objectID, LogicalTerminationPointMO logicalTP)
            throws ServiceException;

    /**
     * Add logical Tp.<br>
     * 
     * @param logicalTP The logical Tp object
     * @return Logical Tp object
     * @since SDNO 0.5
     */
    LogicalTerminationPointMO addLogicalTP(LogicalTerminationPointMO logicalTP) throws ServiceException;

    /**
     * Delete logical Tp though Id.<br>
     * 
     * @param objectID The object Id
     * @return true when delete success
     *         false when delete failed
     * @since SDNO 0.5
     */
    Boolean delTpByID(String objectID) throws ServiceException;
}
