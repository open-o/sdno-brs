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

import org.openo.sdno.mss.dao.multi.DataSourceCtrler;
import org.openo.sdno.mss.dao.util.SpringContextUtil;

/**
 * Relation Graph Manager Utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public final class RelationGraphMgrUtil {

    /**
     * RelationGraphMgrUtil Singleton instance
     */
    private static final RelationGraphMgrUtil INSTANCE = new RelationGraphMgrUtil();

    /**
     * RelationGraphMgr instance
     */
    private RelationGraphMgr relaGraphMgr = null;

    /**
     * Get Singleton instance of RelationGraphMgrUtil.<br>
     * 
     * @return Singleton instance of RelationGraphMgrUtil
     * @since SDNO 0.5
     */
    public static RelationGraphMgrUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Get RelationGraphMgr instance.<br>
     * 
     * @return RelationGraphMgr instance
     * @since SDNO 0.5
     */
    private RelationGraphMgr getRelaGraphMgr() {
        if(null == this.relaGraphMgr) {
            synchronized(INSTANCE) {
                if(null == this.relaGraphMgr) {
                    this.relaGraphMgr = SpringContextUtil.getBean("mssRelaGraphManagement", RelationGraphMgr.class);
                }
            }
        }

        return this.relaGraphMgr;
    }

    /**
     * Get bucket name.<br>
     * 
     * @return bucket name
     * @since SDNO 0.5
     */
    private String getBktName() {
        return DataSourceCtrler.get();
    }

    /**
     * Find Path between src_res and dst_res.<br>
     * 
     * @param srcRes source res name
     * @param dstRes destination res name
     * @return path between src_res and dst_res
     * @since SDNO 0.5
     */
    public List<String> findPathBetweenRes(String srcRes, String dstRes) {
        return getRelaGraphMgr().findPathBetweenRes(getBktName(), srcRes, dstRes);
    }

    /**
     * Get resource types which have COMPOSITION relation with given res.<br>
     * 
     * @param res resource type
     * @return resource types which have COMPOSITION relation with given res
     * @since SDNO 0.5
     */
    public List<String> findCompositonResFromRes(String res) {
        return getRelaGraphMgr().findCompositonResFromRes(getBktName(), res);
    }

    /**
     * Get all resource types which are source resource type of given resource type.<br>
     * 
     * @param res resource type
     * @return all resource types which are source resource type of given resource type
     * @since SDNO 0.5
     */
    public List<String> findSrcRelationResByDstRes(String res) {
        return getRelaGraphMgr().findSrcRelationResByDstRes(getBktName(), res);
    }

    /**
     * Get all resource types which have non-COMPOSITION relation with given res.<br>
     * 
     * @param res resource type
     * @return all resource types which have non-COMPOSITION relation with given res
     * @since SDNO 0.5
     */
    public List<String> findNotCompositonResFromRes(String res) {
        return getRelaGraphMgr().findNotCompositonResFromRes(getBktName(), res);
    }

    /**
     * Check whether res is end of the graph Entity.<br>
     * 
     * @param res resource type
     * @return true if res is end of the graph,false otherwise
     * @since SDNO 0.5
     */
    public boolean isEnd(String res) {
        return getRelaGraphMgr().isEnd(getBktName(), res);
    }

}
