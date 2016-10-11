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

package org.openo.sdno.mss.service.intf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * The interface of MSS service to deal with relation.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public interface MssRelationService {

    /**
     * Query resource relationship.<br>
     * 
     * @param bktName Bucket name
     * @param srcResType Source resource type
     * @param dstResType Destination resource type
     * @param srcUuids Source UUID
     * @param dstUuids Destination UUID
     * @return The map of relationship
     * @since SDNO 0.5
     */
    Map<String, Object> getRelation(String bktName, final String srcResType, final String dstResType,
            final String srcUuids, final String dstUuids) throws ServiceException;

    /**
     * Create resource relationship.<br>
     * 
     * @param bktName Bucket name
     * @param relationType Resource type name
     * @param request Request body
     * @since SDNO 0.5
     */
    void addRelation(String bktName, final String relationType, @Context HttpServletRequest request)
            throws ServiceException;

    /**
     * Delete resource relationship.<br>
     * 
     * @param bktName Bucket name
     * @param relationType Resource type name
     * @param srcUuid Source UUID
     * @param dstUuid Destination UUID
     * @param dstType Destination type
     * @param reltype Relationship name
     * @since SDNO 0.5
     */
    void deleteRelation(String bktName, String relationType, String srcUuid, String dstUuid, String dstType,
            String reltype) throws ServiceException;

}
