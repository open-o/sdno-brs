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
import org.openo.sdno.framework.container.service.IService;

import org.openo.sdno.brs.model.SiteMO;

/**
 * The interface of service to deal the site resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface SiteService extends IService {

    /**
     * Get the site information according the site Id.<br>
     * 
     * @param objectID The object Id
     * @return The site object
     * @since SDNO 0.5
     */
    SiteMO getSiteByID(String objectID) throws ServiceException;

    /**
     * Get the list of site.<br>
     * 
     * @param fields Fields to be queried
     * @param filterMap The filter map
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @return The map of site
     * @since SDNO 0.5
     */
    Map<String, Object> getSiteMOs(final String fields, Map<String, String> filterMap, int pagesize, int pagenum)
            throws ServiceException;

    /**
     * Get site Id.<br>
     * 
     * @param site The site object
     * @return The site Id
     * @since SDNO 0.5
     */
    String getObjectId(SiteMO site) throws ServiceException;

    /**
     * Update the site information.<br>
     * 
     * @param objectID The site Id
     * @param site The site object
     * @return The site object
     * @since SDNO 0.5
     */
    SiteMO updateSiteByID(String objectID, SiteMO site) throws ServiceException;

    /**
     * Add site object.<br>
     * 
     * @param site The site object
     * @return The site object
     * @since SDNO 0.5
     */
    SiteMO addSite(SiteMO site) throws ServiceException;

    /**
     * Delete site object.<br>
     * 
     * @param objectID The site Id
     * @return true when delete success
     *         false when delete failed
     * @since SDNO 0.5
     */
    Boolean delSiteByID(String objectID) throws ServiceException;
}
