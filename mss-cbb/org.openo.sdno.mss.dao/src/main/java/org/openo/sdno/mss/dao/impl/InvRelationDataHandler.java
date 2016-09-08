/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.mss.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.openo.sdno.mss.dao.entities.InvRelationEntity;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.intf.IInvDataHandler;
import org.openo.sdno.mss.dao.intf.IInvRelationDataHandler;
import org.openo.sdno.mss.dao.model.RelationGraphMgrUtil;
import org.openo.sdno.mss.dao.pojo.InvRelationTablePojo;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.init.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * The class of relative data handler. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class InvRelationDataHandler extends AbstractHandler implements IInvRelationDataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvRelationDataHandler.class);

    protected IInvDataHandler invDataHandler = null;

    private static final int QUERYNUMLIMIT = 340;

    public void setInvDataHandler(IInvDataHandler invDataHandler) {
        this.invDataHandler = invDataHandler;
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> getRelationData(final String srcResType, final String dstResType,
            final String srcUuids, final String dstUuids) {
        // define the returned map
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        // must input srcUuids or dstUuids
        if(StringUtils.isEmpty(srcUuids) && StringUtils.isEmpty(dstUuids)) {
            return InvRespEntity.valueOfSuccess(result, result.size());
        }

        // check resource type
        ValidUtil.checkResType(srcResType);

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(srcResType);

        if(!StringUtils.isEmpty(dstResType)) {
            relationPojo.buildDstTypeFilter(dstResType);
        }

        String uuids = "";
        String queryType = "";
        if(!StringUtils.isEmpty(srcUuids)) {
            uuids = srcUuids;
            queryType = InvRelationTablePojo.SRCUUID;
        } else if(!StringUtils.isEmpty(dstUuids)) {
            uuids = dstUuids;
            queryType = InvRelationTablePojo.DSTUUID;
        }

        String[] uuidArrays = uuids.split(",");

        if(null != uuidArrays && uuidArrays.length > 0) {
            int size = uuidArrays.length;

            HashSet<String> uuidSet = new HashSet<String>(size);

            for(String srcUuid : uuidArrays) {
                ValidUtil.checkUuid(srcUuid);
                uuidSet.add(srcUuid);
            }

            SqlSession sqlSession = getSqlSession();

            try {
                relationPojo.createTempAttrTableByFiledName(uuidSet, sqlSession, queryType);

                LOGGER.info("Get resType [{}] relation create temp success. The uuid size is:[{}]", srcResType, size);

                List<Map<String, Object>> batchGetRelations = relationPojo.batchGetRelations(sqlSession);

                if(batchGetRelations != null) {
                    result = batchGetRelations;
                }

                LOGGER.info("Get resType [{}] relation success. The result size is:[{}]", srcResType, result.size());
            } finally {
                relationPojo.removeTempAttrTable(sqlSession);

                LOGGER.info("Get resType [{}] relation remove temp success.", srcResType);
            }
        }

        return InvRespEntity.valueOfSuccess(result, result.size());
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(final String relationType, final String queryType, // NOPMD
            final String srcUuid, final String dstUuid, final String srcAttribute, final String dstAttribute,
            final String serviceType, final String refUnique) {
        String[] resTypes = ValidUtil.checkRelationType(relationType);

        if(StringUtils.isEmpty(srcUuid) && StringUtils.isEmpty(dstUuid)) {
            throw new IllegalArgumentException("Url parameter error: src_uuid and dst_uuid are both empty.");
        }
        if(!StringUtils.isEmpty(srcUuid) && !StringUtils.isEmpty(dstUuid)) {
            throw new IllegalArgumentException("Url parameter error: src_uuid and dst_uuid both have values.");
        }

        // get the relative path
        List<String> paths = RelationGraphMgrUtil.getInstance().findPathBetweenRes(resTypes[0], resTypes[1]);

        if(CollectionUtils.isEmpty(paths)) {
            LOGGER.info("Through the relationship" + relationType
                    + " between resource query, relationship path not found!");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            return InvRespEntity.valueOfSuccess(result, result.size());
        }

        BaseModel baseModel = new BaseModel(srcUuid, dstUuid, srcAttribute, dstAttribute, queryType, serviceType);

        return getDetail(baseModel, resTypes, paths, refUnique);
    }

    private InvRespEntity<List<Map<String, Object>>> getDetail(BaseModel baseModel, String[] resTypes,
            List<String> paths, final String refUnique) {
        String queryUuidName = null;
        String refUuidName = null;
        String queryResType = null;
        String queryAttrs = null;
        String refUuidValue = null;

        String queryType = baseModel.getQueryType();
        String srcUuid = baseModel.getSrcUuid();
        String dstUuid = baseModel.getDstUuid();
        String srcAttribute = baseModel.getSrcAttribute();
        String dstAttribute = baseModel.getDstAttribute();
        String serviceType = baseModel.getServiceType();

        if(!StringUtils.isEmpty(srcUuid)) {
            ValidUtil.checkUuid(srcUuid);
            ValidUtil.checkAttributes(resTypes[1], dstAttribute, true);

            queryUuidName = InvRelationTablePojo.DSTUUID;
            refUuidName = InvRelationTablePojo.SRCUUID;
            queryResType = resTypes[1];
            queryAttrs = dstAttribute;
            refUuidValue = srcUuid;
        }
        if(!StringUtils.isEmpty(dstUuid)) {
            ValidUtil.checkUuid(dstUuid);
            ValidUtil.checkAttributes(resTypes[0], srcAttribute, true);

            queryUuidName = InvRelationTablePojo.SRCUUID;
            refUuidName = InvRelationTablePojo.DSTUUID;
            queryResType = resTypes[0];
            queryAttrs = srcAttribute;
            refUuidValue = dstUuid;
            // reverse the query path
            Collections.reverse(paths);
        }

        ValidUtil.checkAttributes(queryResType, queryAttrs, true);

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(resTypes[0], queryUuidName, refUuidName);
        List<String> refUuidList = new ArrayList<String>();
        refUuidList.add(refUuidValue);
        Iterator<String> it = paths.iterator();
        List<String> queryUuidList = getQueryUuidList(it, refUuidList, relationPojo, serviceType);
        if(CollectionUtils.isEmpty(queryUuidList)) {
            LOGGER.info("Target uuid list is empty!");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            return InvRespEntity.valueOfSuccess(result, result.size());
        }

        Map<String, Object> uuidCondition = new HashMap<String, Object>();
        uuidCondition.put("uuid", queryUuidList);
        if("all".equalsIgnoreCase(queryType)) {
            return invDataHandler.batchGet(queryResType, queryAttrs, JsonUtil.toJson(uuidCondition), null);
        }

        return this.invDataHandler.getSplitPage(queryResType, queryAttrs, JsonUtil.toJson(uuidCondition), null, null,
                refUnique);
    }

    private List<String> getQueryUuidList(Iterator<String> it, List<String> refUuidList,
            InvRelationTablePojo relationPojo, String serviceType) {
        List<String> result = new ArrayList<String>();
        if(it.hasNext()) {
            // avert the sql is too long
            List<List<String>> partList = splitList(refUuidList, QUERYNUMLIMIT);
            String typeName = it.next();
            String[] resTypes = ValidUtil.checkRelationType(typeName);
            List<Map<String, Object>> queryResults = new ArrayList<Map<String, Object>>();
            for(List<String> part : partList) {
                relationPojo.buildServiceTypeFilter(serviceType);
                relationPojo.buildDstTypeFilter(resTypes[1]).buildUuidFilter(part);
                queryResults.addAll(relationPojo.getData(getSqlSession()));
            }
            result = getUuidListFromQueryResults(queryResults, relationPojo.getQueryUuidName());
        }

        if(CollectionUtils.isEmpty(result)) {
            return null;
        }

        if(it.hasNext()) {
            result = getQueryUuidList(it, result, relationPojo, serviceType);
        }
        return result;
    }

    private <T> List<List<T>> splitList(List<T> list, final int length) {
        List<List<T>> partList = new ArrayList<List<T>>();
        final int size = list.size();
        for(int i = 0; i < size; i += length) {
            partList.add(new ArrayList<T>(list.subList(i, Math.min(size, i + length))));
        }
        return partList;
    }

    private List<String> getUuidListFromQueryResults(List<Map<String, Object>> queryResults, String uuidName) {
        List<String> result = new ArrayList<String>();
        for(int i = 0, size = queryResults.size(); i < size; i++) {
            Map<String, Object> valueMap = queryResults.get(i);
            if(null != valueMap && !valueMap.isEmpty()) {
                String uuid = (String)valueMap.get(uuidName);
                if(!StringUtils.isEmpty(uuid)) {
                    result.add(uuid);
                }
            }
        }
        return result;
    }

    /**
     * Update data. <br/>
     * 
     * @param session The session for update
     * @param relationPojo The relative table
     * @param valueMap The value
     * @since SDNO 0.5
     */
    public void doUpdate(SqlSession session, InvRelationTablePojo relationPojo, Map<String, Object> valueMap) {
        if(valueMap == null || valueMap.isEmpty()) {
            return;
        }

        String srcUuid = (String)valueMap.get(InvRelationTablePojo.SRCUUID);
        String dstUuid = (String)valueMap.get(InvRelationTablePojo.DSTUUID);
        String dstType = (String)valueMap.get(InvRelationTablePojo.DSTTYPE);

        relationPojo.buildValue(valueMap).buildUuidCondition(srcUuid, dstUuid, dstType, null);
        relationPojo.updateData(session);
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> add(final String relationType, List<Map<String, Object>> values) {

        String[] resTypes = ValidUtil.checkRelationType(relationType);

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(resTypes[0]);
        LOGGER.info("Beginning batch add data to relational tables, pojo = " + relationPojo);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        for(Map<String, Object> valueMap : values) {
            if(addOneData(getSqlSession(), relationPojo, valueMap)) {
                result.add(valueMap);
            }
        }

        LOGGER.info("Completed batch add data to relational tables, pojo = " + relationPojo);

        return InvRespEntity.valueOfSuccess(result, result.size());
    }

    /**
     * Add data. <br/>
     * 
     * @param session The session for add
     * @param relationPojo The relative table
     * @param valueMap The value
     * @return true if success and otherwise failure.
     * @since SDNO 0.5
     */
    boolean addOneData(SqlSession session, InvRelationTablePojo relationPojo, Map<String, Object> valueMap) {
        int result = doAdd(session, relationPojo, valueMap);
        return result == 1;
    }

    private int doAdd(SqlSession session, InvRelationTablePojo relationPojo, Map<String, Object> valueMap) {
        return relationPojo.buildValue(valueMap).addData(session);

    }

    @Override
    public InvRespEntity<Boolean> delete(final String relationType, final String srcUuid, final String dstUuid,
            final String reltype) {

        String[] resTypes = ValidUtil.checkRelationType(relationType);

        if(StringUtils.isEmpty(srcUuid) && StringUtils.isEmpty(dstUuid)) {
            throw new IllegalArgumentException("Url parameter error: src_uuid and dst_uuid are both empty.");
        }

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(resTypes[0]);

        LOGGER.info("Beginning delete data from relational tables, pojo = " + relationPojo);

        int result = doDelete(getSqlSession(), relationPojo, srcUuid, dstUuid, resTypes[1], reltype);

        LOGGER.info("Completed delete data from relational tables, pojo = " + relationPojo);

        return InvRespEntity.valueOfSuccess(true, result);
    }

    private int doDelete(SqlSession session, InvRelationTablePojo relationPojo, final String srcUuid,
            final String dstUuid, // NOPMD
            final String dstType, final String relativeType) {
        relationPojo.buildUuidCondition(srcUuid, dstUuid, dstType, relativeType);

        // get all data before delete
        List<Map<String, Object>> relations = relationPojo.getRelations(session);

        LOGGER.debug("Before do delete filter is:[{}]", relationPojo.getFilter());
        LOGGER.debug("Before do delete get relationList is:[{}]", relations);

        int result = 0;

        if(relations != null && !relations.isEmpty()) {
            result = relationPojo.removeData(session);
        }

        return result;
    }

    /**
     * Get the batch handle session and you must call close after use. <br/>
     * 
     * @return
     * @since SDNO 0.5
     */
    public SqlSession getBatchSession() {
        return ((SqlSessionTemplate)getSqlSession()).getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    }

    @Override
    public List<InvRelationEntity> querySubNode(String resType, List<String> uuidList) {
        return queryNodeByUuidType(resType, uuidList, InvRelationTablePojo.SRCUUID);
    }

    @Override
    public List<InvRelationEntity> queryParentNode(String resType, List<String> uuidList) {
        return queryNodeByUuidType(resType, uuidList, InvRelationTablePojo.DSTUUID);
    }

    private List<InvRelationEntity> queryNodeByUuidType(String resType, List<String> uuidList, String uuidType) {
        SqlSession sqlSession = getSqlSession();

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(resType);

        HashSet<String> uuidSet = new HashSet<String>(uuidList);

        try {
            relationPojo.createTempAttrTableByFiledName(uuidSet, sqlSession, uuidType);
            List<Map<String, Object>> relationDatalist = relationPojo.batchGetRelations(sqlSession);

            if(relationDatalist.isEmpty()) {
                return new ArrayList<InvRelationEntity>();
            }

            List<InvRelationEntity> nodeList = new ArrayList<InvRelationEntity>(relationDatalist.size());

            for(Map<String, Object> resultMap : relationDatalist) {
                InvRelationEntity invRelationEntity = new InvRelationEntity();

                invRelationEntity.setSrcUuid(resultMap.get(InvRelationTablePojo.SRCUUID).toString());
                invRelationEntity.setDstUuid(resultMap.get(InvRelationTablePojo.DSTUUID).toString());
                invRelationEntity.setDstType(resultMap.get(InvRelationTablePojo.DSTTYPE).toString());
                invRelationEntity.setRelation(resultMap.get(InvRelationTablePojo.RELATION).toString());
                invRelationEntity.setServiceType(resultMap.get(InvRelationTablePojo.SERVICE_TYPE).toString());

                nodeList.add(invRelationEntity);
            }

            LOGGER.debug("queryNodeByUuidType the uuidType is: {}, the nodeList is: [{}]", uuidType, nodeList);

            return nodeList;
        } finally {
            relationPojo.removeTempAttrTable(sqlSession);
        }
    }

    /**
     * The class of basic model. <br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-21
     */
    private class BaseModel {

        private final String srcUuid;

        private final String dstUuid;

        private final String srcAttribute;

        private final String dstAttribute;

        private final String queryType;

        private final String serviceType;

        /**
         * Constructor<br/>
         * 
         * @since SDNO 0.5
         * @param srcUuid The src uuid
         * @param dstUuid The dst uuid
         * @param srcAttribute The scr attribute
         * @param dstAttribute the dst attribute
         * @param queryType The query type
         * @param serviceType The service type
         */
        public BaseModel(String srcUuid, String dstUuid, String srcAttribute, String dstAttribute, String queryType,
                String serviceType) {
            super();
            this.srcUuid = srcUuid;
            this.dstUuid = dstUuid;
            this.srcAttribute = srcAttribute;
            this.dstAttribute = dstAttribute;
            this.queryType = queryType;
            this.serviceType = serviceType;
        }

        /**
         * @return Returns the srcUuid.
         */
        public String getSrcUuid() {
            return srcUuid;
        }

        /**
         * @return Returns the dstUuid.
         */
        public String getDstUuid() {
            return dstUuid;
        }

        /**
         * @return Returns the srcAttribute.
         */
        public String getSrcAttribute() {
            return srcAttribute;
        }

        /**
         * @return Returns the dstAttribute.
         */
        public String getDstAttribute() {
            return dstAttribute;
        }

        /**
         * @return Returns the queryType.
         */
        public String getQueryType() {
            return queryType;
        }

        /**
         * @return Returns the serviceType.
         */
        public String getServiceType() {
            return serviceType;
        }
    }
}
