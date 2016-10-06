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

import org.openo.sdno.brs.model.RootEntity;

/**
 * The interface of service to query resource and relation.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface ResWithRelationQueryService extends IService {

    /**
     * Get resource according Id.<br>
     * 
     * @param objectID The object Id
     * @param type The class type
     * @return The resource data
     * @since SDNO 0.5
     */
    <T extends RootEntity> T getResourceByID(String objectID, Class<T> type) throws ServiceException;

    /**
     * Paging query resource list information.<br>
     * 
     * @param fields Fields to be queried
     * @param key The key of resource object
     * @param filterMap The filter conditions
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @param classType The class type
     * @return The map of resource
     * @since SDNO 0.5
     */
    Map<String, Object> getResources(final String fields, String key, Map<String, String> filterMap, int pagesize,
            int pagenum, Class<?> classType) throws ServiceException;
}
