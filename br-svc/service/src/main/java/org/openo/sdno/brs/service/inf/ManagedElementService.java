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

import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.model.ManagedElementMO;
import org.openo.sdno.framework.container.service.IService;

/**
 * Interface of managed element service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface ManagedElementService extends IService {

    /**
     * Query data according to the network ID.<br>
     * 
     * @param objectID The object Id
     * @return The managedElement object
     * @since SDNO 0.5
     */
    ManagedElementMO getManagedElementByID(String objectID) throws ServiceException;

    /**
     * Get the Id of the object.<br>
     * 
     * @param managedElement The managedElement object
     * @return The Id of the object
     * @since SDNO 0.5
     */
    String getObjectId(ManagedElementMO managedElement) throws ServiceException;

    /**
     * Paging query element list information.<br>
     * 
     * @param fields The fields to be queried
     * @param filterMap The filter map
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @return The querying result
     * @since SDNO 0.5
     */
    Map<String, Object> getManagedElementMOs(final String fields, Map<String, String> filterMap, int pagesize,
            int pagenum) throws ServiceException;

    /**
     * Add managedElement.<br>
     * 
     * @param managedElement The managedElement object to be added
     * @return The managedElement object
     * @since SDNO 0.5
     */
    Object addManagedElement(ManagedElementMO managedElement) throws ServiceException;

    /**
     * Update the managedElement information according the Id.<br>
     * 
     * @param objectID The object Id
     * @param managedElement The managedElement object
     * @return The managedElement object
     * @since SDNO 0.5
     */
    Object updateManagedElementByID(String objectID, ManagedElementMO managedElement) throws ServiceException;

    /**
     * Delete the managedElement object according the Id.<br>
     * 
     * @param objectID The object Id
     * @return The operation status code
     * @since SDNO 0.5
     */
    int delManagedElementByID(String objectID) throws ServiceException;

    /**
     * Get the resource data according the resource Id.<br>
     * 
     * @param strObjID The object Id
     * @return The managedElement object
     * @since SDNO 0.5
     */
    ManagedElementMO getBaseDataByID(String strObjID) throws ServiceException;

    /**
     * Get the ME information according the tenant Id.<br>
     * 
     * @param tenantId The tenant Id
     * @return The list of managedElement object
     * @since SDNO 0.5
     */
    List<ManagedElementMO> getManagedElementByTenantId(String tenantId) throws ServiceException;
}
