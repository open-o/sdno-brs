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

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IService;

import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;

/**
 * The interface of service to deal the resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface ResourceService extends IService {

    /**
     * Get the resource data.<br>
     * 
     * @param objectID The object Id
     * @param type The class type
     * @return The resource data
     * @since SDNO 0.5
     */
    <T> T getResource(String objectID, Class<T> type) throws ServiceException;

    /**
     * Get the resource list.<br>
     * 
     * @param queryString Query string object
     * @param key The query key
     * @param type The class type
     * @return The resource object
     * @since SDNO 0.5
     */
    <T extends RootEntity> Object getResourceList(String queryString, String key, Class<T> type)
            throws ServiceException;

    /**
     * Get the resource list.<br>
     * 
     * @param param Paging query parameter
     * @param key The query key
     * @param classType The class type
     * @return The resource object
     * @since SDNO 0.5
     */
    <T extends RootEntity> Object getResourceList(PagingQueryPara param, String key, Class<T> classType)
            throws ServiceException;

    /**
     * Add resource data.<br>
     * 
     * @param data The resource data to be added
     * @param type The class type
     * @return The resource data
     * @since SDNO 0.5
     */
    <T extends RootEntity> T addResource(T data, Class<T> type) throws ServiceException;

    /**
     * Update resource data.<br>
     * 
     * @param objectID The object Id
     * @param data The resource data to be updated
     * @param type The class type
     * @return The resource data
     * @since SDNO 0.5
     */
    <T> T updateResource(String objectID, T data, Class<T> type) throws ServiceException;

    /**
     * Delete resource data.<br>
     * 
     * @param objectID The object Id
     * @param type The class type
     * @return true when delete success
     *         false when delete false
     * @since SDNO 0.5
     */
    <T> Boolean deleteResource(String objectID, Class<T> type) throws ServiceException;

    /**
     * Get resource Id.<br>
     * 
     * @param data The resource data
     * @return The resource Id
     * @since SDNO 0.5
     */
    <T extends RootEntity> String genID(T data) throws ServiceException;

    /**
     * Check whether the object is exist.<br>
     * 
     * @param key The key object
     * @return The result as string
     * @since SDNO 0.5
     */
    String checkObjExsit(Object key) throws ServiceException;

    /**
     * According to the filter conditions to obtain a list of objects.<br>
     * 
     * @param fields Fields to be queried
     * @param filter The filter conditions
     * @param pagesize The size of page
     * @param pagenum The number of page
     * @param classType The class type
     * @return The list of objects
     * @since SDNO 0.5
     */
    <T> List<T> getObjectList(String fields, String filter, int pagesize, int pagenum, Class<T> classType)
            throws ServiceException;
}
