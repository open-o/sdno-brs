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

package org.openo.sdno.mss.dao.intf;

import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.dao.entities.InvRelationEntity;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.BaseModel;

/**
 * Inventory Relation Data handler.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public interface InvRelationDataHandler {

    /**
     * Query Data by Relation.<br>
     * 
     * @param relationType relation type
     * @param refUnique unique index
     * @param baseModel The base attributes
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> get(final String relationType, final String refUnique,
            BaseModel baseModel);

    /**
     * Get Relation Data.<br>
     * 
     * @param srcResType source res type
     * @param dstResType destination res type
     * @param srcUuids source key id
     * @param dstUuids destination key id
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> getRelationData(final String srcResType, final String dstResType,
            final String srcUuids, final String dstUuids);

    /**
     * Add Relation Data.<br>
     * 
     * @param relationType relation type
     * @param values relation data
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> add(final String relationType, List<Map<String, Object>> values);

    /**
     * Delete Data by relation.<br>
     * 
     * @param relationType relation type
     * @param srcUuid source key id
     * @param dstUuid destination key id
     * @param reltype
     * @return InvRespEntity Object
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> delete(final String relationType, final String srcUuid, final String dstUuid,
            final String reltype);

    /**
     * Query SubNodes by src_uuid.<br>
     * 
     * @param resType resource type
     * @param uuidList key id list
     * @return SubNodes
     * @since SDNO 0.5
     */
    List<InvRelationEntity> querySubNode(String resType, List<String> uuidList);

    /**
     * Query Parent Nodes by src_uuid.<br>
     * 
     * @param resType resource type
     * @param uuidList Key id list
     * @return Parent Nodes
     * @since SDNO 0.5
     */
    List<InvRelationEntity> queryParentNode(String resType, List<String> uuidList);

}
