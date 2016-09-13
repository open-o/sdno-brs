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

package org.openo.sdno.mss.dao.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import org.openo.sdno.mss.dao.model.ModelManagement;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.openo.sdno.mss.schema.relationmodel.Relationtype;

/**
 * Relation Graph Redius Entity Class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class RelaGraphRedisEntity {

    private final DirectedGraph<String, DefaultEdge> relationGraph = new DefaultDirectedGraph<String, DefaultEdge>(
            DefaultEdge.class);

    private ModelManagement modelMgr = null;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param modelMgr ModelManagement instance
     */
    public RelaGraphRedisEntity(ModelManagement modelMgr) {
        super();
        this.modelMgr = modelMgr;
    }

    /**
     * Get the path between src_res and dst_res.<br>
     * 
     * @param srcRes source resource type
     * @param dtsRes destination resource type
     * @return the path between src_res and dst_res
     * @since SDNO 0.5
     */
    public List<String> findPathBetweenRes(String srcRes, String dtsRes) {
        if(srcRes.equals(dtsRes)) {
            List<String> res = new ArrayList<String>();
            res.add(srcRes + "-" + dtsRes);
            return res;
        }

        List<DefaultEdge> paths = DijkstraShortestPath.findPathBetween(relationGraph, srcRes, dtsRes);
        List<String> relationPath = new ArrayList<String>();
        if(null != paths) {
            for(DefaultEdge edge : paths) {
                String source = relationGraph.getEdgeSource(edge);
                String target = relationGraph.getEdgeTarget(edge);
                relationPath.add(source + "-" + target);
            }
        }
        return relationPath;
    }

    /**
     * Get resource types which have COMPOSITION relation with res.<br>
     * 
     * @param bktName bucket name
     * @param res resource type
     * @return resource types which have COMPOSITION relation with res
     * @since SDNO 0.5
     */
    public List<String> findCompositonResFromRes(String bktName, String res) {
        List<String> relationResList = new ArrayList<String>();
        Collection<RelationModelRelation> relations = this.modelMgr.getRelaModelMap(bktName).values();

        for(RelationModelRelation relation : relations) {
            if(relation.getType() == Relationtype.COMPOSITION && res.equals(relation.getSrc())) {
                relationResList.add(relation.getDst());
            }
        }

        return relationResList;
    }

    /**
     * Get all resource types which are source resource type of given resource type.<br>
     * 
     * @param bktName bucket name
     * @param res resource type
     * @return all resource types which are source resource type of given resource type.
     * @since SDNO 0.5
     */
    public List<String> findSrcRelationResByDstRes(String bktName, String res) {
        List<String> srcRelationResList = new ArrayList<String>();
        Collection<RelationModelRelation> relations = this.modelMgr.getRelaModelMap(bktName).values();
        for(RelationModelRelation relation : relations) {
            if(res.equals(relation.getDst())) {
                srcRelationResList.add(relation.getSrc());
            }
        }
        return srcRelationResList;
    }

    /**
     * Get all resource types which have non-COMPOSITION relation with given res.<br>
     * 
     * @param bktName bucket name
     * @param res resource type
     * @return resource types which have non-COMPOSITION relation with res
     * @since SDNO 0.5
     */
    public List<String> findNotCompositonResFromRes(String bktName, String res) {
        List<String> relationResList = new ArrayList<String>();
        Collection<RelationModelRelation> relations = this.modelMgr.getRelaModelMap(bktName).values();

        for(RelationModelRelation relation : relations) {
            if(relation.getType() != Relationtype.COMPOSITION && res.equals(relation.getSrc())) {
                relationResList.add(relation.getDst());
            }
        }

        return relationResList;
    }

    /**
     * Build Relation Graph of one Bucket.<br>
     * 
     * @param bktName Bucket name
     * @since SDNO 0.5
     */
    public void buildRelationGraph(String bktName) {
        Collection<RelationModelRelation> relations = this.modelMgr.getRelaModelMap(bktName).values();

        // remove duplicated vertex
        Set<String> vertexs = new HashSet<String>();
        for(RelationModelRelation relation : relations) {
            vertexs.add(relation.getSrc());
            vertexs.add(relation.getDst());
        }

        // first add vertex, then add edge
        for(String vertex : vertexs) {
            relationGraph.addVertex(vertex);
        }
        for(RelationModelRelation relation : relations) {
            relationGraph.addEdge(relation.getSrc(), relation.getDst());
        }
    }

    /**
     * Check whether this resource is end of the graph.<br>
     * 
     * @param res resource type
     * @return true if this resource is end of the graph,false otherwise
     * @since SDNO 0.5
     */
    public boolean isEnd(String res) {
        return relationGraph.outDegreeOf(res) == 0;
    }
}
