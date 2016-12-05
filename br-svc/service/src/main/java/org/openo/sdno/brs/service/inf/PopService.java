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

import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.model.PopMO;
import org.openo.sdno.framework.container.service.IService;

/**
 * Interface of pop service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-11-30
 */
public interface PopService extends IService {

    /**
     * Query PopMO by Uuid.<br>
     * 
     * @param objectId Pop Uuid
     * @return The site object
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    PopMO query(String objectId) throws ServiceException;

    /**
     * Batch query PopMOs.<br>
     * 
     * @param fields Fields to be queried
     * @param filterMap The filter map
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @return The map of site
     * @throws ServiceException when batch query failed
     * @since SDNO 0.5
     */
    Map<String, Object> batchQuery(final String fields, Map<String, String> filterMap, int pagesize, int pagenum)
            throws ServiceException;

    /**
     * Get Id of PopMO.<br>
     * 
     * @param popMO PopMO Object
     * @return The Id of PopMO
     * @throws ServiceException when get failed
     * @since SDNO 0.5
     */
    String getObjectId(PopMO popMO) throws ServiceException;

    /**
     * Update Pop Object.<br>
     * 
     * @param popMO PopMO need to update
     * @return objectId PopMO Uuid
     * @return Pop object updated
     * @throws ServiceException when update failed
     * @since SDNO 0.5
     */
    PopMO updatePop(String objectId, PopMO popMO) throws ServiceException;

    /**
     * Add Pop object.<br>
     * 
     * @param popMO Pop object
     * @return Pop Object added
     * @throws ServiceException when add failed
     * @since SDNO 0.5
     */
    PopMO addPop(PopMO popMO) throws ServiceException;

    /**
     * Delete pop object.<br>
     * 
     * @param objectId pop Uuid
     * @return true if delete success, otherwise false
     * @throws ServiceException when delete failed
     * @since SDNO 0.5
     */
    Boolean deletePop(String objectId) throws ServiceException;
}
