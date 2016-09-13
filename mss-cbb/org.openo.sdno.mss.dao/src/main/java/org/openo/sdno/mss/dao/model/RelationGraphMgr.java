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

package org.openo.sdno.mss.dao.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.sdno.mss.dao.model.entity.RelaGraphRedisEntity;

/**
 * Relation Graph Manage class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class RelationGraphMgr {

    /**
     * RelaGraphRedisEntity cache
     */
    private Map<String, RelaGraphRedisEntity> relaGraphEntities = new ConcurrentHashMap<String, RelaGraphRedisEntity>();

    /**
     * ModelManagement instance
     */
    private ModelManagement modelMgr = null;

    /**
     * Set ModelManagement attribute.<br>
     * 
     * @param modelMgr ModelManagement attribute
     * @since SDNO 0.5
     */
    public void setModelMgr(ModelManagement modelMgr) {
        this.modelMgr = modelMgr;
    }

    /**
     * Get RelaGraphRedisEntity instance.<br>
     * 
     * @param bktName bucket name
     * @return RelaGraphRedisEntity instance
     * @since SDNO 0.5
     */
    private RelaGraphRedisEntity getRelaGraphEntity(String bktName) {
        if(null == this.relaGraphEntities.get(bktName)) {
            synchronized(relaGraphEntities) {
                if(null == this.relaGraphEntities.get(bktName)) {
                    RelaGraphRedisEntity entity = new RelaGraphRedisEntity(modelMgr);

                    entity.buildRelationGraph(bktName);
                    this.relaGraphEntities.put(bktName, entity);
                }
            }
        }

        return this.relaGraphEntities.get(bktName);

    }

    /**
     * Find Path between src_res and dst_res.<br>
     * 
     * @param bktName bucket name
     * @param srcRes source resource name
     * @param dstRes destination resource name
     * @return path between src_res and dst_res
     * @since SDNO 0.5
     */
    public List<String> findPathBetweenRes(String bktName, String srcRes, String dstRes) {
        return getRelaGraphEntity(bktName).findPathBetweenRes(srcRes, dstRes);
    }

    /**
     * Get resource types which have COMPOSITION relation with res.<br>
     * 
     * @param bktName bucket name
     * @param res resource name
     * @return resource types which have COMPOSITION relation with res
     * @since SDNO 0.5
     */
    public List<String> findCompositonResFromRes(String bktName, String res) {
        return getRelaGraphEntity(bktName).findCompositonResFromRes(bktName, res);
    }

    /**
     * Get all resource types which are source resource type of given resource type.<br>
     * 
     * @param bktName bucket name
     * @param res resource name
     * @return all resource types which are source resource type of given resource type
     * @since SDNO 0.5
     */
    public List<String> findSrcRelationResByDstRes(String bktName, String res) {
        return getRelaGraphEntity(bktName).findSrcRelationResByDstRes(bktName, res);
    }

    /**
     * Get all resource types which have non-COMPOSITION relation with given res.<br>
     * 
     * @param bktName bucket name
     * @param res resource name
     * @return all resource types which have non-COMPOSITION relation with given res
     * @since SDNO 0.5
     */
    public List<String> findNotCompositonResFromRes(String bktName, String res) {
        return getRelaGraphEntity(bktName).findNotCompositonResFromRes(bktName, res);
    }

    /**
     * Check whether res is end of the graph Entity.<br>
     * 
     * @param bktName bucket name
     * @param res resource name
     * @return true if res is end of the graph,false otherwise
     * @since SDNO 0.5
     */
    public boolean isEnd(String bktName, String res) {
        return getRelaGraphEntity(bktName).isEnd(res);
    }

}
