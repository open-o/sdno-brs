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

package org.openo.sdno.mss.combine.intf;

import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.model.BaseModel;

/**
 * Inventory relation data service interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public interface InvRelationDataService {

    /**
     * Query another resource's info through relation model by resource UUID, such as querying all
     * ports of NE.<br>
     * 
     * @param bktName Bucket name
     * @param relationType relation type
     * @param refUnique The unique index of the last (or first) data, used for paging
     * @param baseModel The base attributes
     * @return query result
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> get(String bktName, final String relationType, final String refUnique,
            BaseModel baseModel);

    /**
     * Query inventory relation.<br>
     * 
     * @param bktName Bucket name
     * @param srcResType source resource type
     * @param dstResType destination resource type
     * @param srcUuids source resource UUIDs
     * @param dstUuids destination resource UUIDs
     * @return query result
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> get(String bktName, final String srcResType, final String dstResType,
            final String srcUuids, final String dstUuids);

    /**
     * Add relation data.<br>
     * 
     * @param bktName Bucket name
     * @param relationType relation type
     * @param values relation data
     * @return operation result
     * @since SDNO 0.5
     */
    InvRespEntity<List<Map<String, Object>>> add(String bktName, final String relationType,
            List<Map<String, Object>> values);

    /**
     * Delete relation data.<br>
     * 
     * @param bktName Bucket name
     * @param relationType relation type
     * @param srcUuid source resource UUID
     * @param dstUuid destination resource UUID
     * @param reltype relative type
     * @return operation result
     * @since SDNO 0.5
     */
    InvRespEntity<Boolean> delete(String bktName, final String relationType, final String srcUuid, final String dstUuid,
            final String reltype);

}
