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

import org.openo.sdno.brs.model.Relation;

/**
 * Service interface for query reference relations.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface RelationService extends IService {

    /**
     * Get related data.<br>
     * 
     * @param dstType Destination type
     * @param srcIds Source IDs
     * @param dstIds Destination IDs
     * @return The list of related data
     * @since SDNO 0.5
     */
    List<Relation> getRelations(String dstType, String srcIds, String dstIds) throws ServiceException;

    /**
     * Delete the relation of resource.<br>
     * 
     * @param strObjID The resource Id
     * @param lstReq The list of request data
     * @param lstDB The list of database
     * @since SDNO 0.5
     */
    void delRelation(String strObjID, List<Relation> lstReq, List<Relation> lstDB) throws ServiceException;

    /**
     * Create the relation of resource.<br>
     * 
     * @param lstRelation The list of relation data
     * @since SDNO 0.5
     */
    void createRelation(List<Relation> lstRelation) throws ServiceException;
}
