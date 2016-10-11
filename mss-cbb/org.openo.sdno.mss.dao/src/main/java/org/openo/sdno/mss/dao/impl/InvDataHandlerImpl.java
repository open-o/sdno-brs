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

package org.openo.sdno.mss.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.type.TypeReference;
import org.mybatis.spring.SqlSessionTemplate;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.mss.dao.constant.InvAttrDefine;
import org.openo.sdno.mss.dao.constant.InvErrorCodeDefine;
import org.openo.sdno.mss.dao.constant.InvSqlState;
import org.openo.sdno.mss.dao.constant.RelationFilterCombine;
import org.openo.sdno.mss.dao.entities.InvRelationEntity;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.exception.InvSqlException;
import org.openo.sdno.mss.dao.exception.ServerInnerException;
import org.openo.sdno.mss.dao.filter.InvSqlFilterParserUtil;
import org.openo.sdno.mss.dao.intf.InvDataHandler;
import org.openo.sdno.mss.dao.intf.InvRelationDataHandler;
import org.openo.sdno.mss.dao.io.SqlSessionProxy;
import org.openo.sdno.mss.dao.model.QueryParamModel;
import org.openo.sdno.mss.dao.model.RelationGraphMgrUtil;
import org.openo.sdno.mss.dao.pojo.AInvCheckerPojo;
import org.openo.sdno.mss.dao.pojo.InvAttrEntityPojo;
import org.openo.sdno.mss.dao.pojo.InvBasicCheckerPojo;
import org.openo.sdno.mss.dao.pojo.InvBasicTablePojo;
import org.openo.sdno.mss.dao.pojo.InvCrossTablePojo;
import org.openo.sdno.mss.dao.pojo.InvExtCheckerPojo;
import org.openo.sdno.mss.dao.pojo.InvExtTablePojo;
import org.openo.sdno.mss.dao.pojo.InvMasterSlavesQueryPojo;
import org.openo.sdno.mss.dao.pojo.InvMasterSlavesQueryPojo.AttrEntity;
import org.openo.sdno.mss.dao.pojo.InvMasterSlavesQueryPojo.JoinEntity;
import org.openo.sdno.mss.dao.pojo.InvRelationCombinePojo;
import org.openo.sdno.mss.dao.pojo.InvRelationTablePojo;
import org.openo.sdno.mss.dao.pojo.InvSplitPagePojo;
import org.openo.sdno.mss.dao.util.DateTimeUtil;
import org.openo.sdno.mss.dao.util.FilterUtil;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.dao.util.ValidateUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.relationmodel.Relationtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class of handle inventory, such as add, delete, modify, query, export and import. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class InvDataHandlerImpl extends AbstractHandlerImpl implements InvDataHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvDataHandlerImpl.class);

    /**
     * The max count for batch handle when add, update and import.
     */
    public static final int MAX_BATCH_CNT = 1000;

    /**
     * The count for batch commit.
     */
    public static final int BATCH_COMMIT_CNT = 1000;

    /**
     * The max count for batch handle when delete.
     */
    public static final int BATCH_DELETE_UUID_CNT = 1000;

    /**
     * The max count of relation filter.
     */
    public static final int MAX_FILTER_COMBINE = 5;

    /**
     * The matched relation filter.
     */
    private static final String LIST_REG = "^ *\\[.*\\]";

    /**
     * The relative data handler.
     */
    InvRelationDataHandler relationDataHandler;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    public InvDataHandlerImpl() {
        super();
    }

    public void setRelationDataHandler(InvRelationDataHandler relationDataHandler) {
        this.relationDataHandler = relationDataHandler;
    }

    /**
     * Get the batch handle session and you must call close after use. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public SqlSession getBatchSession() {
        return ((SqlSessionTemplate)getSqlSession()).getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(final String resType, final String uuid, final String attr) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkUuid(uuid);
        ValidUtil.checkAttributes(resType, attr, true);

        InvCrossTablePojo pojo = new InvCrossTablePojo(resType, attr).buildUuidFilter(uuid);

        LOGGER.debug("Begin get data, pojo = " + pojo);
        List<Map<String, Object>> data = pojo.getData(getSqlSession());
        LOGGER.debug("End of get data, pojo = " + pojo);
        return InvRespEntity.valueOfSuccess(data, data.size());
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> batchGet(final String resType, final String attr,
            final String filter, final String filterEx) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkAttributes(resType, attr, true);
        ValidUtil.checkFilter(resType, filter);

        InvCrossTablePojo pojo = new InvCrossTablePojo(resType, attr).buildFilter(filter).buildFilterEx(filterEx);
        LOGGER.debug("Begin batch get data, pojo = " + pojo);
        List<Map<String, Object>> data = pojo.getData(getSqlSession());
        LOGGER.debug("End of batch get data, pojo = " + pojo);
        return InvRespEntity.valueOfSuccess(data, data.size());
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> getSplitPage(String resType, String attr, String filter,
            String filterEx, Object refValue, String refUniqueValue) {
        return this.getSplitPage(resType, attr, filter, filterEx, null, refValue, refUniqueValue);
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> getSplitPage(String resType, String attr, String filter,
            String filterEx, String sortAttrName, Object refValue, String refUnique) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkAttributes(resType, attr, true);
        ValidUtil.checkFilter(resType, filter);
        ValidUtil.checkSort(resType, attr, sortAttrName, refValue);
        ValidUtil.checkSplitPage(sortAttrName, refValue, refUnique);

        InvBasicTablePojo basic = new InvBasicTablePojo(resType).buildAttributes(attr);
        basic.setFilter(filter);
        basic.setFilterEx(filterEx);

        InvSplitPagePojo pojo = new InvSplitPagePojo(basic);
        if(!StringUtils.isEmpty(refUnique)) {
            pojo.buildUniqueAttr(refUnique);
        }

        if(!StringUtils.isEmpty(sortAttrName)) {
            pojo.buildSortAttr(sortAttrName, refValue, true);
        }

        List<Map<String, Object>> data = pojo.getData(getSqlSession());

        appendExData(resType, data, attr);

        return InvRespEntity.valueOfSuccess(data, data.size());
    }

    private void appendExData(String resType, List<Map<String, Object>> resultData, String attr) {
        // do nothing when resultData is empty
        if(resultData != null && !resultData.isEmpty()) {
            // get all extended attributes name from attr
            InvExtTablePojo pojo = new InvExtTablePojo(resType);
            List<InvExtTablePojo> exTablePojos = pojo.buildAttributes(attr);

            // do nothing when extended attributes is empty
            if(!exTablePojos.isEmpty()) {
                Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
                for(Map<String, Object> row : resultData) {
                    Object uuid = row.get(InvAttrDefine.UUID.getValue());
                    Validate.notNull(uuid);
                    map.put(uuid.toString(), row);
                }

                // build a new filter with extended attributes
                boolean isFirst = true;
                StringBuilder filterBuilder = new StringBuilder();
                for(InvExtTablePojo exPojo : exTablePojos) {
                    if(!isFirst) {
                        filterBuilder.append(',');
                    }
                    isFirst = false;
                    filterBuilder.append(exPojo.getAttrName());
                }

                // call InvCrossTablePojo to get extended attributes by designated UUID
                InvCrossTablePojo crossPojo = new InvCrossTablePojo(resType, filterBuilder.toString());
                crossPojo.buildFilter(
                        FilterUtil.buildInFilterString(InvAttrDefine.UUID.getValue(), Datatype.STRING, map.keySet()));

                List<Map<String, Object>> exData = crossPojo.getData(getSqlSession());

                // merge the new extended attributes to resultData
                for(Map<String, Object> row : exData) {
                    Map<String, Object> data = map.get(row.get(InvAttrDefine.UUID.getValue()));
                    Validate.notNull(data);

                    data.putAll(row);
                }
            }
        }
    }

    @Override
    public InvRespEntity<Boolean> delete(final String resType, final String uuid) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkUuid(uuid);

        List<String> srcUuidList = new ArrayList<String>();
        srcUuidList.add(uuid);

        return batchDelete(resType, srcUuidList);
    }

    /**
     * Delete data. <br>
     * 
     * @param session The session for delete
     * @param basicPojo The basic table
     * @param extPojo The extended table
     * @param uuid The record key
     * @return The fixed value 3.
     * @since SDNO 0.5
     */
    public int doDelete(SqlSession session, InvBasicTablePojo basicPojo, InvExtTablePojo extPojo, String uuid) {
        ValidUtil.checkUuid(uuid);

        // delete the data in basic table
        basicPojo.buildUuidFilter(uuid);
        basicPojo.removeData(session);

        // delete the data in extended table
        extPojo.buildUuidFilter(uuid);
        extPojo.removeData(session);

        return 3;
    }

    private int doBatchDeleteBasicAndExt(SqlSession session, InvBasicTablePojo basicPojo, InvExtTablePojo extPojo,
            Set<String> uuidSet) {
        basicPojo.setPreDeleteUuidSet(uuidSet);

        int ret = basicPojo.batchDelete(session);

        extPojo.setPreDeleteUuidSet(uuidSet);
        extPojo.batchDelete(session);

        return ret;

    }

    @Override
    public InvRespEntity<Map<String, Object>> update(String resType, String uuid, Map<String, Object> value) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkUuid(uuid);
        Validate.notNull(value);
        ValidUtil.checkAttributes(resType, value.keySet(), true);

        // query the data is exist or not
        InvRespEntity<List<Map<String, Object>>> invRespEntity = this.get(resType, uuid, "*");
        if(invRespEntity.getRow() == 0) {
            throw new ServerInnerException(InvErrorCodeDefine.INV_DATA_NOTEXIST, "Data is not exist when update!");
        }

        // forbid modify UUID
        value.put(InvAttrDefine.UUID.getValue(), uuid);

        InvBasicTablePojo basicPojo = new InvBasicTablePojo(resType);
        InvExtTablePojo extPojo = new InvExtTablePojo(resType);

        basicPojo.setAbsent(false);

        int time = DateTimeUtil.getCurTime();

        LOGGER.debug("Begin update data, pojo = " + basicPojo + " " + extPojo);

        // do update
        doUpdate(getSqlSession(), time, basicPojo, extPojo, uuid, value, null);

        LOGGER.debug("End of update data, pojo = " + basicPojo + " " + extPojo);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(InvAttrDefine.UUID.getValue(), uuid);
        map.put(InvAttrDefine.UPDATE_TIME.getValue(), value.get(InvAttrDefine.UPDATE_TIME.getValue()));
        map.putAll(value);

        return InvRespEntity.valueOfSuccess(map, 1);
    }

    @Override
    public int countExtData(String attrName, String resType) {
        ValidUtil.checkResType(resType);
        Validate.notNull(attrName);

        InvExtTablePojo extPojo = new InvExtTablePojo(resType);
        extPojo.buildAttribute(attrName).buildAttrNameFilter();

        return extPojo.countExtData(getSqlSession());
    }

    /**
     * Update data. <br>
     * 
     * @param session The session for update
     * @param time The begin time
     * @param basicPojo The basic table
     * @param extPojo The extended table
     * @param uuid The record UUID
     * @param valueMap The data that want to update
     * @param extCache The cache data
     * @return 0 if do nothing.
     * @since SDNO 0.5
     */
    public int doUpdate(SqlSession session, int time, InvBasicTablePojo basicPojo, InvExtTablePojo extPojo, String uuid,
            Map<String, Object> valueMap, InvExtTableUpdateCacheImpl extCache) {
        ValidUtil.checkUuid(uuid);

        int ret = 0;
        // update extended table
        List<InvExtTablePojo> extPojos = extPojo.buildValues(valueMap);
        if(!extPojos.isEmpty()) {
            InvExtTableUpdateCacheImpl cache;
            if(extCache == null) {
                cache = new InvExtTableUpdateCacheImpl(getSqlSession(), new Callable<SqlSession>() {

                    @Override
                    public SqlSession call() throws Exception {
                        return getBatchSession();
                    }
                });
                cache.query(basicPojo.getResType(), Arrays.asList(valueMap));
            } else {
                cache = extCache;
            }

            ret = doUpdateExtDatas(session, basicPojo.getResType(), uuid, extPojos, cache);
        }

        // update basic table
        boolean bRequireUpdateTime = ret > 0;
        // upate time
        if(!valueMap.containsKey(InvAttrDefine.UPDATE_TIME.getValue())) {
            valueMap.put(InvAttrDefine.UPDATE_TIME.getValue(), time);
        } else {
            bRequireUpdateTime = true;
        }

        // forbid update uuid
        if(valueMap.containsKey(InvAttrDefine.UUID.getValue())) {
            Object oldUUID = valueMap.remove(InvAttrDefine.UUID.getValue());
            basicPojo.buildUuidFilter(uuid).buildValue(valueMap);
            valueMap.put(InvAttrDefine.UUID.getValue(), oldUUID);
        } else {
            basicPojo.buildUuidFilter(uuid).buildValue(valueMap);
        }

        if(!bRequireUpdateTime) {
            List<InvAttrEntityPojo> entityList = basicPojo.getAttrEntityList();
            // don nothing when just update time or uuid
            if(entityList.size() == 1) {
                // do nothing
                return 0;
            }
        }

        // set a new session
        basicPojo.setQuerySession(getSqlSession());

        basicPojo.updateData(session);

        return ret + 2;
    }

    private int doUpdateExtDatas(SqlSession session, String resourceType, String uuid, List<InvExtTablePojo> extPojos,
            InvExtTableUpdateCacheImpl cache) {
        int ret = 0;

        for(InvExtTablePojo extPojo : extPojos) {
            String oldValue = cache.getValue(resourceType, uuid, extPojo.getAttrName());
            String newValue = extPojo.getAttrValue();

            if(ObjectUtils.equals(newValue, oldValue)) {
                continue;
            }

            if(newValue == null) {
                if(oldValue != null) {
                    extPojo.buildAllFilter();
                    extPojo.removeData(session);
                    ret++;
                }
            } else {
                if(oldValue == null) {
                    extPojo.addData(session);
                } else {
                    extPojo.buildAllFilter();
                    extPojo.updateData(session);
                }

                ret++;
            }
        }

        return ret;
    }

    @Override
    public InvRespEntity<Boolean> batchDelete(final String resType, List<String> uuidList) {
        long startTime = System.currentTimeMillis();

        int count = 0;

        LOGGER.info("BatchDelete {} start, the source uuid list is: [{}]", resType, uuidList);

        // check resource type
        ValidUtil.checkResType(resType);

        // check uuid
        ValidUtil.checkUuidList(uuidList);

        count = batchDeleteSubNodes(resType, uuidList);

        batchDeleteParentNodes(resType, uuidList);

        LOGGER.info("BatchDelete {} finished, Time-consuming: {} milliseconds. delete uuid [{}] success. result = [{}]",
                new Object[] {resType, System.currentTimeMillis() - startTime, uuidList, count});

        return InvRespEntity.valueOfSuccess(true, count);
    }

    private int batchDeleteSubNodes(final String resType, List<String> srcUuidList) {
        // prepare the delete map of basic and extended table
        Map<String, List<String>> preDelDataMap = new HashMap<String, List<String>>(srcUuidList.size());

        // prepare the delete map of relative map
        Map<String, List<InvRelationEntity>> preDelRelationMap =
                new HashMap<String, List<InvRelationEntity>>(srcUuidList.size());

        long startTime = System.currentTimeMillis();

        prepareSubNodesRecursively(resType, srcUuidList, preDelDataMap, preDelRelationMap);

        LOGGER.info("prepareSubNodesRecursively {} finished, Time-consuming: {} milliseconds", resType,
                System.currentTimeMillis() - startTime);

        return deleteSubNodes(resType, preDelDataMap, preDelRelationMap);
    }

    private void prepareSubNodesRecursively(String resType, List<String> uuidList,
            Map<String, List<String>> preDelDataMap, Map<String, List<InvRelationEntity>> preDelRelationMap) {
        if(uuidList.isEmpty() && preDelDataMap.keySet().contains(resType)
                && preDelRelationMap.keySet().contains(resType)) {
            LOGGER.warn("uuidList is Empty, resType: " + resType);
            return;
        }

        List<InvRelationEntity> subNodeList = relationDataHandler.querySubNode(resType, uuidList);

        if(preDelDataMap.get(resType) == null) {
            preDelDataMap.put(resType, uuidList);
        } else {
            List<String> existUuidList = preDelDataMap.get(resType);
            existUuidList.addAll(uuidList);
        }

        if(preDelRelationMap.keySet().contains(resType)) {
            preDelRelationMap.get(resType).addAll(subNodeList);
        } else {
            preDelRelationMap.put(resType, subNodeList);
        }

        Map<String, List<String>> subNodeMap = new HashMap<String, List<String>>(subNodeList.size());

        subNodeListToMap(subNodeList, subNodeMap);

        removeCycleData(preDelDataMap, subNodeMap);

        for(Entry<String, List<String>> entry : subNodeMap.entrySet()) {
            prepareSubNodesRecursively(entry.getKey(), entry.getValue(), preDelDataMap, preDelRelationMap);
        }
    }

    private void removeCycleData(Map<String, List<String>> preDelDataMap, Map<String, List<String>> subNodeMap) {
        for(Map.Entry<String, List<String>> entry : subNodeMap.entrySet()) {
            String resType = entry.getKey();
            List<String> uuidVals = entry.getValue();
            if(preDelDataMap.keySet().contains(resType)) {
                Iterator<String> it = uuidVals.iterator();
                while(it.hasNext()) {
                    String val = it.next();
                    if(preDelDataMap.get(resType).contains(val)) {
                        it.remove();
                    }
                }
            }
        }
    }

    private void subNodeListToMap(List<InvRelationEntity> subNodeList, Map<String, List<String>> subNodeMap) {
        for(InvRelationEntity nodeobject : subNodeList) {
            if(!Relationtype.COMPOSITION.value().equals(nodeobject.getRelation())) {
                continue;
            }

            List<String> compositionDstTypeList = subNodeMap.get(nodeobject.getDstType());
            if(compositionDstTypeList == null) {
                compositionDstTypeList = new ArrayList<String>();
                subNodeMap.put(nodeobject.getDstType(), compositionDstTypeList);
            }
            compositionDstTypeList.add(nodeobject.getDstUuid());
        }
    }

    private int deleteSubNodes(String resType, Map<String, List<String>> preDelDataMap,
            Map<String, List<InvRelationEntity>> preDelRelationMap) {
        long startTime = System.currentTimeMillis();

        int count = 0;

        for(Entry<String, List<String>> entry : preDelDataMap.entrySet()) {
            List<String> uuidList = entry.getValue();

            int ret = batchDelBasicData(entry.getKey(), uuidList);

            if(resType.equals(entry.getKey())) {
                count = count + ret;
            }

        }

        LOGGER.info("do batch delete basic data success, Time-consuming: {} milliseconds. preDelDataMap is: {}",
                System.currentTimeMillis() - startTime, preDelDataMap);

        for(Entry<String, List<InvRelationEntity>> entry : preDelRelationMap.entrySet()) {
            batchDelRelationData(entry.getKey(), entry.getValue(), InvRelationTablePojo.SRCUUID);
        }

        LOGGER.info("do batch delete subNodes success, Time-consuming: {} milliseconds. preDelRelationMap is: {}",
                System.currentTimeMillis() - startTime, preDelRelationMap);

        return count;
    }

    private int batchDelBasicData(String type, List<String> uuidList) {
        InvBasicTablePojo basicPojo = new InvBasicTablePojo(type);
        InvExtTablePojo extPojo = new InvExtTablePojo(type);

        Set<String> uuidListToSet = new HashSet<String>(uuidList);

        // split the set
        List<Set<String>> splitHashSet = splitHashSet(uuidListToSet, BATCH_DELETE_UUID_CNT);

        int count = 0;

        for(Set<String> uuidSet : splitHashSet) {
            int ret = doBatchDeleteBasicAndExt(getSqlSession(), basicPojo, extPojo, uuidSet);
            count = count + ret;
        }

        return count;
    }

    private Set<String> listToSet(List<String> uuidList) {
        Set<String> uuidSet = new HashSet<String>(uuidList.size());
        for(int i = 0; i < uuidList.size(); i++) {
            uuidSet.add(uuidList.get(i));
        }
        return uuidSet;
    }

    private List<Set<String>> splitHashSet(Set<String> uuidSet, int count) {
        List<String> uuidList = new ArrayList<String>(uuidSet.size());

        Iterator<String> iterator = uuidSet.iterator();
        while(iterator.hasNext()) {
            String uuid = iterator.next();
            uuidList.add(uuid);
        }

        List<List<String>> splitArrayList = splitArrayList(uuidList, count);

        List<Set<String>> resultSet = new ArrayList<Set<String>>();
        for(List<String> uuidSubList : splitArrayList) {
            resultSet.add(listToSet(uuidSubList));
        }
        return resultSet;
    }

    private List<List<String>> splitArrayList(List<String> srcList, int count) {
        List<List<String>> resultList = new ArrayList<List<String>>();

        if(srcList.isEmpty()) {
            return resultList;
        }

        int size = srcList.size();

        if(size <= count) {
            resultList.add(srcList);
        } else {
            int pre = size / count;
            int last = size % count;

            for(int i = 0; i < pre; i++) {
                List<String> preItemList = new ArrayList<String>(count);
                for(int j = 0; j < count; j++) {
                    preItemList.add(srcList.get(i * count + j));
                }
                resultList.add(preItemList);
            }

            if(last > 0) {
                List<String> lastItemList = new ArrayList<String>();
                for(int i = 0; i < last; i++) {
                    lastItemList.add(srcList.get(pre * count + i));
                }
                resultList.add(lastItemList);
            }
        }
        return resultList;

    }

    private void batchDelRelationData(String resType, List<InvRelationEntity> relationList, String uuidType) {
        long startTime = System.currentTimeMillis();

        Set<String> uuidSet = new HashSet<String>(relationList.size());

        if(InvRelationTablePojo.SRCUUID.equals(uuidType)) {
            for(int i = 0; i < relationList.size(); i++) {
                uuidSet.add(relationList.get(i).getSrcUuid());
            }
        } else if(InvRelationTablePojo.DSTUUID.equals(uuidType)) {
            for(int i = 0; i < relationList.size(); i++) {
                uuidSet.add(relationList.get(i).getDstUuid());
            }
        } else {
            return;
        }

        if(uuidSet.isEmpty()) {
            return;
        } else if(uuidSet.size() <= BATCH_DELETE_UUID_CNT) {
            doBatchDelRelationByUuidType(getSqlSession(), resType, uuidSet, uuidType);
        } else {
            List<Set<String>> splitHashSet = splitHashSet(uuidSet, BATCH_DELETE_UUID_CNT);
            for(Set<String> uuidSubSet : splitHashSet) {
                doBatchDelRelationByUuidType(getSqlSession(), resType, uuidSubSet, uuidType);
            }
        }

        LOGGER.info("do batch delete relation {} success, Time-consuming: {} milliseconds. uuidList.size() is:{}",
                new Object[] {resType, System.currentTimeMillis() - startTime, relationList.size()});
    }

    private int doBatchDelRelationByUuidType(SqlSession session, String restType, Set<String> uuidSet,
            String uuidType) {
        InvRelationTablePojo relationPojo = new InvRelationTablePojo(restType);
        relationPojo.setPreDeleteUuidSet(uuidSet);
        relationPojo.setDelUuidType(uuidType);

        return relationPojo.batchDelete(session);
    }

    private void batchDeleteParentNodes(final String resType, List<String> srcUuidList) {
        long startTime = System.currentTimeMillis();

        deleteParentNodes(resType, srcUuidList);

        LOGGER.info("Batch delete parent nodes {} success, Time-consuming: {} milliseconds. The uuid list is: {}.",
                new Object[] {resType, System.currentTimeMillis() - startTime, srcUuidList});

    }

    private void deleteParentNodes(final String resType, List<String> srcUuidList) {
        List<String> resTypeList = RelationGraphMgrUtil.getInstance().findSrcRelationResByDstRes(resType);

        LOGGER.info("delete parent Node resList is: [{}]", resTypeList);

        if(!resTypeList.isEmpty()) {
            for(String relationRestType : resTypeList) {
                List<InvRelationEntity> parentNodeList =
                        relationDataHandler.queryParentNode(relationRestType, srcUuidList);

                batchDelRelationData(relationRestType, parentNodeList, InvRelationTablePojo.DSTUUID);
            }
        }
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> batchUpdate(String resType, List<Map<String, Object>> values) {
        ValidUtil.checkResType(resType);
        Validate.notEmpty(values);
        ValidateUtil.checkSize(values.size(), 0, MAX_BATCH_CNT);

        // check data exist or not
        checkBasicDataExist(resType, values);

        InvBasicTablePojo basicPojo = new InvBasicTablePojo(resType);

        basicPojo.setAbsent(false);

        // set query session
        basicPojo.setQuerySession(getSqlSession());

        InvExtTablePojo extPojo = new InvExtTablePojo(resType);
        extPojo.setQuerySession(getSqlSession());
        LOGGER.debug("Begin update data, pojo = " + basicPojo + " " + extPojo);

        int time = DateTimeUtil.getCurTime();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        final SqlSessionProxy sessionProxy = new SqlSessionProxy(values.size(), getBatchSession());

        try {

            for(Map<String, Object> valueMap : values) {
                ValidUtil.checkAttributes(resType, valueMap.keySet(), true);

                String uuid = (String)valueMap.get(InvAttrDefine.UUID.getValue());

                doUpdate(sessionProxy, time, basicPojo.copy(), extPojo, uuid, valueMap, null);

                sessionProxy.addPoint();
                if(sessionProxy.getSqlCnt() / BATCH_COMMIT_CNT > 0) {
                    sessionProxy.tryTheBestCommit();
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(InvAttrDefine.UUID.getValue(), uuid);
                map.put(InvAttrDefine.UPDATE_TIME.getValue(), time);
                map.putAll(valueMap);
                result.add(map);
            }

            sessionProxy.tryTheBestCommit();
            InvSqlException ex = sessionProxy.getException();
            if(ex != null) {
                InvErrorCodeDefine error = ex.getSqlState() == InvSqlState.INSERT_DUPLICATE_KEY
                        ? InvErrorCodeDefine.INV_DB_COMMIT_ERROR : InvErrorCodeDefine.INV_UNKNOWN_ERROR;
                throw new ServerInnerException(ex, error, "Fail to commit sql.");
            }
        } finally {
            sessionProxy.close();
        }

        LOGGER.debug("End of update data, pojo = " + basicPojo + " " + extPojo);
        return InvRespEntity.valueOfSuccess(result, values.size());
    }

    private void checkBasicDataExist(String resType, List<Map<String, Object>> values) {
        InvCrossTablePojo crossPojo = new InvCrossTablePojo(resType, InvAttrDefine.UUID.getValue());

        Set<String> updateUuidSet = new HashSet<String>();
        for(Map<String, Object> valueMap : values) {
            updateUuidSet.add((String)valueMap.get(InvAttrDefine.UUID.getValue()));
        }

        Datatype dt = crossPojo.getBasic().getAllAttrMap().get(InvAttrDefine.UUID.getValue());

        String buildFilterString = crossPojo.buildFilterString(InvAttrDefine.UUID.getValue(), dt, updateUuidSet);

        crossPojo.buildFilterDesc(InvAttrDefine.UUID.getValue() + " in (:" + InvAttrDefine.UUID.getValue() + ")")
                .buildFilter(buildFilterString);

        crossPojo.setReadCount(updateUuidSet.size());
        List<Map<String, Object>> datas = crossPojo.getData(getSqlSession());

        Set<String> dbUuidSet = new HashSet<String>(datas.size());

        for(Map<String, Object> data : datas) {
            dbUuidSet.add(data.get(InvAttrDefine.UUID.getValue()).toString());
        }

        for(String updateUuid : updateUuidSet) {
            if(!dbUuidSet.contains(updateUuid)) {
                LOGGER.error("The dbUuidSet is:[{}], the updateUuid is:[{}]", dbUuidSet, updateUuid);

                throw new ServerInnerException(InvErrorCodeDefine.INV_DATA_NOTEXIST,
                        "uuid [" + updateUuid + "] is not exist when update!");
            }
        }
    }

    @Override
    public InvRespEntity<Map<String, Object>> add(String resType, Map<String, Object> values) {
        ValidUtil.checkResType(resType);
        Validate.notNull(values);
        ValidUtil.checkAttributes(resType, values.keySet(), false);

        InvBasicTablePojo basicPojo = new InvBasicTablePojo(resType);
        InvExtTablePojo extPojo = new InvExtTablePojo(resType);

        LOGGER.debug("Begin add data, pojo = " + basicPojo + " " + extPojo);
        doAdd(getSqlSession(), DateTimeUtil.getCurTime(), basicPojo, extPojo, values);
        LOGGER.debug("End of add data, pojo = " + basicPojo + " " + extPojo);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(InvAttrDefine.UUID.getValue(), values.get(InvAttrDefine.UUID.getValue()));
        map.put(InvAttrDefine.CREATE_TIME.getValue(), values.get(InvAttrDefine.CREATE_TIME.getValue()));

        return InvRespEntity.valueOfSuccess(map, 1);
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> batchAdd(String resType, List<Map<String, Object>> values) {
        ValidUtil.checkResType(resType);
        Validate.notEmpty(values);
        ValidateUtil.checkSize(values.size(), 0, MAX_BATCH_CNT);

        LOGGER.debug("Begin batch add data, resType = {}", resType);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        int curTime = DateTimeUtil.getCurTime();
        doBatchAdd(result, curTime, resType, values);

        LOGGER.debug("End of batch add data, resType = {}", resType);
        return InvRespEntity.valueOfSuccess(result, values.size());
    }

    private void doBatchAdd(List<Map<String, Object>> result, int curTime, String resType,
            List<Map<String, Object>> values) {
        SqlSessionProxy sessionProxy = new SqlSessionProxy(values.size(), getBatchSession());

        InvBasicTablePojo basicPojo = new InvBasicTablePojo(resType);
        InvExtTablePojo extPojo = new InvExtTablePojo(resType);

        try {
            for(Map<String, Object> valueMap : values) {
                ValidUtil.checkAttributes(resType, valueMap.keySet(), true);
                doAdd(sessionProxy, curTime, basicPojo.copy(), extPojo, valueMap);
                sessionProxy.addPoint();

                Map<String, Object> map = new HashMap<String, Object>();
                map.putAll(valueMap);
                map.put(InvAttrDefine.UUID.getValue(), valueMap.get(InvAttrDefine.UUID.getValue()));
                map.put(InvAttrDefine.CREATE_TIME.getValue(), valueMap.get(InvAttrDefine.CREATE_TIME.getValue()));
                result.add(map);

                if(sessionProxy.getSqlCnt() / BATCH_COMMIT_CNT > 0) {
                    sessionProxy.tryTheBestCommit();
                }
            }

            sessionProxy.tryTheBestCommit();
            InvSqlException ex = sessionProxy.getException();
            if(ex != null) {
                InvErrorCodeDefine error = ex.getSqlState() == InvSqlState.INSERT_DUPLICATE_KEY
                        ? InvErrorCodeDefine.INV_DB_COMMIT_ERROR : InvErrorCodeDefine.INV_UNKNOWN_ERROR;
                throw new ServerInnerException(ex, error, "Fail to commit sql.");
            }
        } finally {
            sessionProxy.close();
        }
    }

    /**
     * Add Data. <br>
     * 
     * @param session The session for add
     * @param curTime The begin time
     * @param basicPojo The basic table
     * @param extPojo The extended table
     * @param valueMap The data that prepare to add
     * @return The number more than 2.
     * @since SDNO 0.5
     */
    public int doAdd(SqlSession session, int curTime, InvBasicTablePojo basicPojo, InvExtTablePojo extPojo,
            Map<String, Object> valueMap) {
        // create the UUID
        String uuid = (String)valueMap.get(InvAttrDefine.UUID.getValue());
        if(StringUtils.isEmpty(uuid)) {
            uuid = UuidUtils.createUuid();
            valueMap.put(InvAttrDefine.UUID.getValue(), uuid);
        } else {
            // check UUID
            ValidUtil.checkUuid(uuid);
        }

        // create the time
        if(valueMap.get(InvAttrDefine.CREATE_TIME.getValue()) == null) {
            valueMap.put(InvAttrDefine.CREATE_TIME.getValue(), curTime);
        }

        // insert to basic table
        basicPojo.buildValue(valueMap).addData(session);

        // insert to extended table
        List<InvExtTablePojo> extPojos = extPojo.buildValues(valueMap);
        for(InvExtTablePojo pojo : extPojos) {
            pojo.addData(session);
        }

        return extPojos.size() + 2;
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> commQueryGet(String resType, List<String> attrsList,
            List<HashMap<String, Object>> joinAttrList, String filterDsc, List<String> sortList,
            QueryParamModel queryParam) {
        InvMasterSlavesQueryPojo msPojo = new InvMasterSlavesQueryPojo(resType);
        msPojo.setResType(resType);
        String masterTableAlias = "A";
        // set the alias of basic table, the default is A
        msPojo.setTableAlias(masterTableAlias);
        // check parameters
        checkAndSetMasterResType(resType, attrsList,
                fillFilterData(resType, masterTableAlias, filterDsc, queryParam.getFilter()), sortList, msPojo,
                queryParam.getPageNum(), queryParam.getPageSize());

        // check joinAttrList
        checkAndSetJoinResType(joinAttrList, msPojo);

        // check sortList
        checkSortFields(msPojo, sortList);

        SqlSession session = getSqlSession();

        List<Map<String, Object>> data = msPojo.getCommQueryData(session);

        // add extended attributes to basic attributes
        if(msPojo.getAttrsExtList() != null && !msPojo.getAttrsExtList().isEmpty()) {
            genDataWithEx(session, resType, data, msPojo);
        }

        return InvRespEntity.valueOfSuccess(data, data.size());
    }

    @Override
    public List<Object> commQueryGetCount(String resType, List<HashMap<String, Object>> joinAttrList, String filterDsc,
            String filterData) {
        InvMasterSlavesQueryPojo msPojo = new InvMasterSlavesQueryPojo(resType);
        msPojo.setResType(resType);
        String masterTableAlias = "A";
        // set the alias of basic table, the default is A
        msPojo.setTableAlias(masterTableAlias);
        // check parameters
        checkAndSetMasterResType(resType, new ArrayList<String>(),
                fillFilterData(resType, masterTableAlias, filterDsc, filterData), null, msPojo, "0", "0");

        // check joinAttrList
        checkAndSetJoinResType(joinAttrList, msPojo);
        SqlSession session = getSqlSession();

        return msPojo.getCommQueryDataCount(session);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> queryRelationData(String resType, List<String> attrsList, String filterStr,
            List<String> sortList, String pageNum, String pageSize) {
        Map<String, Object> filter = JsonUtil.fromJson(filterStr, Map.class);
        Map<String, String> basicFilter = (Map<String, String>)filter.get("basic");
        Map<String, String> relationFilter = (Map<String, String>)filter.get("relation");
        InvRelationCombinePojo pojo = new InvRelationCombinePojo(resType);
        fillRelationCombine(pojo, attrsList, basicFilter, relationFilter, sortList, pageNum, pageSize);
        checkSortFields(pojo, sortList);
        SqlSession session = getSqlSession();
        List<Map<String, Object>> data = pojo.getCommQueryData(session);
        // add extended attributes to basic attributes
        if(pojo.getAttrsExtList() != null && !pojo.getAttrsExtList().isEmpty()) {
            genDataWithEx(session, resType, data, pojo);
        }

        if(pojo.getAttrsLinkList() != null && !pojo.getAttrsLinkList().isEmpty()) {
            // add relation
            genDataWithLink(session, resType, data, pojo);
        }

        return data;
    }

    private void genDataWithLink(SqlSession session, String resType, List<Map<String, Object>> data,
            InvRelationCombinePojo pojo) {
        Set<String> uuids = getUUIDs(data);

        InvRelationTablePojo relationPojo = new InvRelationTablePojo(resType);
        try {
            relationPojo.createTempAttrTable(uuids, session);
            appendBaseDataWithLinkData(data, relationPojo.batchGetRelations(session), pojo.getAttrsLinkList());
        } finally {
            relationPojo.removeTempAttrTable(session);
        }

    }

    @SuppressWarnings("unchecked")
    private void appendBaseDataWithLinkData(List<Map<String, Object>> data, List<Map<String, Object>> relationData,
            List<String> attrsLinkList) {
        if(data == null || relationData == null || attrsLinkList == null) {
            return;
        }

        Map<String, Map<String, Object>> rspDataMap = new HashMap<String, Map<String, Object>>();
        for(Map<String, Object> oneRspData : data) {
            rspDataMap.put((String)oneRspData.get("uuid"), oneRspData);
        }

        for(Map<String, Object> relation : relationData) {
            String id = (String)relation.get(InvRelationTablePojo.SRCUUID);
            Map<String, Object> oneRspData = rspDataMap.get(id);
            if(oneRspData == null) {
                LOGGER.error("append link data to base object:{} failed.", id);
                continue;
            }

            List<Map<Object, Object>> relationObjList = (List<Map<Object, Object>>)oneRspData.get("relation");
            if(relationObjList == null) {
                relationObjList = new ArrayList<Map<Object, Object>>();
            }

            Map<Object, Object> relationObj = new HashMap<Object, Object>();
            for(String linkAttr : attrsLinkList) {
                String key = linkAttr;
                if("dst_uuid".equals(key)) {
                    key = "dst_id";
                }
                relationObj.put(key, relation.get(linkAttr));
            }
            relationObjList.add(relationObj);
            oneRspData.put("relation", relationObjList);
        }

        data.clear();
        data.addAll(rspDataMap.values());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> queryRelationDataCount(String resType, List<String> attrsList, String filterStr,
            List<String> sortList, String pageNum, String pageSize) {
        Map<String, Object> filter = JsonUtil.fromJson(filterStr, Map.class);
        Map<String, String> basicFilter = (Map<String, String>)filter.get("basic");
        Map<String, String> relationFilter = (Map<String, String>)filter.get("relation");
        InvRelationCombinePojo pojo = new InvRelationCombinePojo(resType);
        fillRelationCombine(pojo, attrsList, basicFilter, relationFilter, sortList, pageNum, pageSize);
        checkSortFields(pojo, sortList);
        SqlSession session = getSqlSession();
        return pojo.getCommQueryDataCount(session);
    }

    private void fillRelationCombine(InvRelationCombinePojo pojo, List<String> attrsList,
            Map<String, String> basicFilter, Map<String, String> relationFilter, List<String> sortList, String pageNum,
            String pageSize) {
        final long maxPageCount = 1000;
        String basicTableAlias = "A";
        String relationTableAlias = "B";
        // set alias
        pojo.setTableAlias(basicTableAlias);
        pojo.setRelationTableNameAlias(relationTableAlias);
        // set filter
        String basicFilterStr = (basicFilter != null) ? fillFilterData(pojo.getResType(), basicTableAlias,
                basicFilter.get("filterDsc"), basicFilter.get("filterData")) : null;
        setFilterSql(basicFilterStr, pojo);
        String relationFilterStr =
                genRelationFilter(pojo.getResType(), pojo.getRelationTableName(), relationTableAlias, relationFilter);
        pojo.setRelationFilter(relationFilterStr);

        // set attribute
        fillAttrsForRelCombine(pojo, attrsList);
        // set sort
        setSortFields(pojo, sortList);

        try {
            long count = 1000;
            long fromIndex = 0;

            if(!StringUtils.isEmpty(pageSize)) {
                count = Long.valueOf(pageSize);
            }

            if(!StringUtils.isEmpty(pageNum)) {
                fromIndex = Long.valueOf(pageNum);
            }

            pojo.setFromIndex(fromIndex * count);

            if(count > maxPageCount) {
                LOGGER.error("page capacity:" + count + " exceeds the max allowed count:" + maxPageCount);

                throw new IllegalArgumentException(
                        "page capacity:" + count + " exceeds the max allowed count:" + maxPageCount);
            }

            pojo.setCount(count);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("page capacity or page Number format error: page capacity is :"
                    + pageSize + " and pageNumber is :" + pageNum);
        }
    }

    private String genRelationFilter(String resType, String tableName, String tableAlias,
            Map<String, String> relationFilter) {
        if(relationFilter == null) {
            return null;
        }

        String filterDsc = relationFilter.get("filterDsc");
        filterDsc = StringUtils.isEmpty(filterDsc) ? "" : filterDsc.trim();
        String filterData = relationFilter.get("filterData");
        filterData = StringUtils.isEmpty(filterData) ? "" : filterData.trim();

        if(StringUtils.isEmpty(filterDsc) && StringUtils.isEmpty(filterData)) {
            logger.error("filter is empty.");
            return null;
        } else if(StringUtils.isEmpty(filterDsc) || StringUtils.isEmpty(filterData)) {
            LOGGER.error("filterDsc:" + filterDsc + " or filterData:" + filterDsc + " is empty.");
            throw new IllegalArgumentException("filterDsc:" + filterDsc + " or filterData:" + filterDsc + " is empty.");
        }

        // the filter split by "#"
        if(!filterDsc.matches(LIST_REG)) {
            return fillFilterData(resType, tableAlias, filterDsc, filterData, true);
        }

        String filterCombine = relationFilter.get("filterCombine");
        RelationFilterCombine combineType = StringUtils.isEmpty(filterCombine) ? RelationFilterCombine.INTERSECT
                : RelationFilterCombine.getType(filterCombine.trim());
        return genCombineFilter(resType, tableName, tableAlias, filterDsc, filterData, combineType,
                InvRelationTablePojo.SRCUUID);

    }

    private StringBuffer genInsertFilter(String tableName, String tableAlias, String fields, String oneFilterStr,
            int filterSize, int index, StringBuffer result) {
        StringBuffer filter;

        if(index == 0) {
            if(filterSize == 1) {
                filter = new StringBuffer().append(" (").append(fields).append(")").append(" in ").append(" (")
                        .append("select ").append(fields).append(" from ").append(tableName).append(" ")
                        .append(tableAlias).append(" where (").append(oneFilterStr).append(")").append(")");
            } else {
                filter = new StringBuffer().append("select ").append(fields).append(" from ").append(tableName)
                        .append(" ").append(tableAlias).append(" where (").append(oneFilterStr).append(")");
            }
        } else if(index < filterSize - 1) {
            filter = new StringBuffer().append("select ").append(fields).append(" from ").append(tableName).append(" ")
                    .append(tableAlias).append(" where ").append(oneFilterStr).append(" and (").append(fields)
                    .append(")").append(" in ").append(" (").append(result).append(")");
        } else {
            filter = new StringBuffer().append("(").append(oneFilterStr).append(" and (").append(fields).append(")")
                    .append(" in ").append(" (").append(result).append("))");
        }

        return filter;
    }

    private String genCombineFilter(String resType, String tableName, String tableAlias, String filterDsc,
            String filterData, RelationFilterCombine combineType, String fields) {
        if(!filterDsc.matches(LIST_REG) || !filterData.matches(LIST_REG)) {
            LOGGER.error("combine filterDsc: " + filterDsc + " and filterData: " + filterData + " is invalid.");
            throw new IllegalArgumentException(
                    "combine filterDsc: " + filterDsc + " and filterData: " + filterData + " is invalid.");
        }

        StringBuffer result = new StringBuffer();
        List<String> descList = JsonUtil.fromJson(filterDsc, new TypeReference<List<String>>() {});

        List<Map<String, Object>> dataMapList =
                JsonUtil.fromJson(filterData, new TypeReference<List<Map<String, Object>>>() {});

        int len = descList.size();
        if(len > MAX_FILTER_COMBINE) {
            LOGGER.error("combine filter exceeds the max size.");
            throw new IllegalArgumentException("combine filter exceeds the max size.");
        }

        for(int i = 0; i < len; i++) {
            String desc = descList.get(i);
            Map<String, Object> dataMap = dataMapList.get(i);
            String oneFilterStr = fillFilterData(resType, tableAlias, desc, JsonUtil.toJson(dataMap), true);
            if(StringUtils.isEmpty(oneFilterStr)) {
                LOGGER.error("filterDsc:{} is invalid.", filterDsc);
                throw new IllegalArgumentException("filterDsc: " + filterDsc + " is invalid.");
            }

            switch(combineType) {
                case INTERSECT:
                    result = genInsertFilter(tableName, tableAlias, fields, oneFilterStr, len, i, result);
                    break;

                default:
                    // need replenish UNION and MINUS in future
                    throw new IllegalArgumentException(
                            "filterCombine:" + combineType + " is invalid." + " UNION or MINUS not support now");
            }
        }

        return result.toString().trim();
    }

    private void fillAttrsForRelCombine(InvRelationCombinePojo pojo, List<String> attrsList) {
        Set<String> allAttrs = InvBasicTablePojo.getAllBasicAttributes(pojo.getResType()).keySet();

        Map<String, Datatype> extAttrs = InvExtTablePojo.getAllExtAttributes(pojo.getResType());

        Set<String> relAttrs = InvRelationCombinePojo.getRelationModel().keySet();
        Set<String> relationAttrs = new HashSet<String>();
        for(String relAttr : relAttrs) {
            if("src_uuid".equals(relAttr)) {
                continue;
            }
            relationAttrs.add(relAttr);
        }

        checkAndSetAttrs(attrsList, allAttrs, extAttrs.keySet(), relationAttrs);

        for(String attrItem : attrsList) {
            if(!allAttrs.contains(attrItem)) {
                if(extAttrs.containsKey(attrItem)) {
                    pojo.getAttrsExtList().add(attrItem);
                } else if(relationAttrs.contains(attrItem)) {
                    pojo.getAttrsLinkList().add(attrItem);
                } else {
                    LOGGER.error("No such attr error: attr name: " + attrItem + " resType :" + pojo.getResType());

                    throw new IllegalArgumentException(
                            "No such attr error: attr name: " + attrItem + " resType :" + pojo.getResType());
                }
            } else {
                AttrEntity attrEntity = pojo.new AttrEntity();
                attrEntity.setTableAlias(pojo.getTableAlias());
                attrEntity.setAttrName(attrItem);
                pojo.getAttrsList().add(attrEntity);
            }
        }

        if(pojo.getAttrsLinkList().isEmpty()) {
            pojo.getAttrsLinkList().add(InvRelationTablePojo.DSTTYPE);
            pojo.getAttrsLinkList().add(InvRelationTablePojo.DSTUUID);
        }

        if(!attrsList.contains("uuid")) {
            AttrEntity attrEntity = pojo.new AttrEntity();
            attrEntity.setTableAlias(pojo.getTableAlias());
            attrEntity.setAttrName("uuid");
            pojo.getAttrsList().add(attrEntity);
        }
    }

    private void checkAndSetAttrs(List<String> attrsList, Set<String> basicAttrs, Set<String> extAttrs,
            Set<String> relationAttrs) {
        // just handle the attribute are basic, extended or all, and the attribute list only have
        // one attribute
        if(attrsList.size() != 1) {
            return;
        }

        String attr = attrsList.get(0);
        if("base".equals(attr)) {
            attrsList.clear();
            attrsList.addAll(basicAttrs);
            return;
        }

        if("ext".equals(attr)) {
            attrsList.clear();
            attrsList.addAll(extAttrs);
            return;
        }

        if("all".equals(attr)) {
            attrsList.clear();
            attrsList.addAll(basicAttrs);
            attrsList.addAll(extAttrs);

            if(relationAttrs != null) {
                attrsList.addAll(relationAttrs);
            }
            return;
        }
    }

    private void checkSortFields(InvMasterSlavesQueryPojo msPojo, List<String> sortList) {
        if(sortList == null || sortList.isEmpty()) {
            LOGGER.error("Invalid arg [sortList] inputed.");
            return;
        }

        Set<String> allAttrs = InvBasicTablePojo.getAllBasicAttributes(msPojo.getResType()).keySet();
        List<AttrEntity> attrs = msPojo.getAttrsList();

        for(String sortFields : sortList) {
            sortFields = sortFields.replace("-", "");
            if(!allAttrs.contains(sortFields)) {
                boolean isAlias = false;
                for(AttrEntity attr : attrs) {
                    if(!StringUtils.isEmpty(attr.getAttrAlias()) && sortFields.equals(attr.getAttrAlias())) {
                        isAlias = true;
                        break;
                    }
                }

                if(!isAlias) {
                    LOGGER.error("sort fields : " + sortFields + " is invalid!");
                    throw new IllegalArgumentException("sort fields : " + sortFields + " is invalid!");
                }
            }
        }
    }

    private void checkAndSetJoinResType(List<HashMap<String, Object>> joinAttrList, InvMasterSlavesQueryPojo msPojo) {
        if(joinAttrList == null || joinAttrList.isEmpty()) {
            LOGGER.error("joinAttrList, Nothing to do");
            return;
        }

        msPojo.setJoinResList(new ArrayList<JoinEntity>());

        for(int i = 0; i < joinAttrList.size(); i++) {
            HashMap<String, Object> joinItem = joinAttrList.get(i);

            Object masterRefKey = joinItem.get("attr");
            Object joinRefRes = joinItem.get("refRes");
            Object joinRefKey = joinItem.get("refKey");
            Object joinFilterDsc = joinItem.get("filterDsc");
            Object joinFilterData = joinItem.get("filterData");
            Object joinRefAttr = joinItem.get("refAttr");

            if(masterRefKey == null || joinRefRes == null || joinRefKey == null || joinRefAttr == null) {
                LOGGER.error(
                        "JoinAttr parameters can not be empty for masterRefKey, joinRefRes, joinRefKey or joinRefAttr");
                throw new IllegalArgumentException(
                        "JoinAttr parameters can not be empty for masterRefKey, joinRefRes, joinRefKey or joinRefAttr");
            }

            String tableAlias = getTableAlias(i);
            checkAndSetEachJoinResType(masterRefKey.toString(), joinRefRes.toString(), joinRefAttr.toString(),
                    joinRefKey.toString(),
                    fillFilterData(joinRefRes.toString(), tableAlias, joinFilterDsc, joinFilterData), tableAlias,
                    msPojo);
        }
    }

    private void checkAndSetEachJoinResType(String joinKey, String resType, String attr, String joinRefKey,
            String filterSql, String tableAlias, InvMasterSlavesQueryPojo msPojo) {
        ValidUtil.checkResType(resType);
        ValidUtil.checkAttributes(resType, joinRefKey, true);
        ValidUtil.checkAttributes(msPojo.getResType(), joinKey, true);

        if(StringUtils.isEmpty(joinRefKey) || StringUtils.isEmpty(joinKey)) {
            LOGGER.error("attr or refKey is empty for resType: " + resType);

            throw new IllegalArgumentException("attr or refKey is empty for resType: " + resType);
        }

        JoinEntity joinResType = msPojo.new JoinEntity();

        joinResType.setTableAlias(tableAlias);
        joinResType.setTableName(resType);
        joinResType.setKey(joinKey);
        joinResType.setMasterKey(joinRefKey);

        msPojo.getJoinResList().add(joinResType);

        setJoinResTypeAttrfields(resType, attr, joinResType, msPojo);

        setFilterSql(filterSql, msPojo);
    }

    private void setJoinResTypeAttrfields(String resType, String attr, JoinEntity joinResType,
            InvMasterSlavesQueryPojo msPojo) {
        HashMap<String, String> attrsMap = JsonUtil.fromJson(attr, new TypeReference<HashMap<String, String>>() {});

        if(attrsMap.isEmpty()) {
            LOGGER.error("attr can not be empty for resType: " + resType);
            throw new IllegalArgumentException("attr can not be empty for resType: " + resType);
        }

        Set<String> allAttrs = InvBasicTablePojo.getAllBasicAttributes(resType).keySet();

        for(Entry<String, String> attrItem : attrsMap.entrySet()) {
            if(!allAttrs.contains(attrItem.getKey())) {
                LOGGER.error("attr check error attr name is : " + attrItem.getKey() + " resType is: " + resType);

                throw new IllegalArgumentException(
                        "attr check error attr name is : " + attrItem.getKey() + " resType is: " + resType);
            } else {
                AttrEntity attrEntity = msPojo.new AttrEntity();
                attrEntity.setTableAlias(joinResType.getTableAlias());
                attrEntity.setAttrName(attrItem.getKey());
                attrEntity.setAttrAlias(attrItem.getValue());

                msPojo.getAttrsList().add(attrEntity);
            }
        }
    }

    private String getTableAlias(int size) {
        int maxTableJoinNumber = 10;

        final String[] tableAlias = new String[] {"B", "C", "D", "E", "F", "G", "H", "I", "J"};

        if(size > maxTableJoinNumber || size > tableAlias.length) {
            throw new IllegalArgumentException("table number is too much to dispose: table size is: " + size);
        }

        return tableAlias[size];
    }

    private void checkAndSetMasterResType(String resType, List<String> attrsList, String filterSql,
            List<String> sortList, InvMasterSlavesQueryPojo msPojo, String pageNumber, String pageCapacity) {
        final long maxPageCount = 1000;
        setAttrfields(attrsList, msPojo);

        setFilterSql(filterSql, msPojo);

        setSortFields(msPojo, sortList);

        try {
            long count = 1000;
            long fromIndex = 0;

            if(!StringUtils.isEmpty(pageCapacity)) {
                count = Long.valueOf(pageCapacity);
            }

            if(!StringUtils.isEmpty(pageNumber)) {
                fromIndex = Long.valueOf(pageNumber);
            }

            msPojo.setFromIndex(fromIndex * count);

            if(count > maxPageCount) {
                LOGGER.error("page capacity:" + count + " exceeds the max allowed count:" + maxPageCount);

                throw new IllegalArgumentException(
                        "page capacity:" + count + " exceeds the max allowed count:" + maxPageCount);
            }

            msPojo.setCount(count);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("page capacity or page Number format error: page capacity is :"
                    + pageCapacity + " and pageNumber is :" + pageNumber);
        }

    }

    private void setAttrfields(List<String> attrsList, InvMasterSlavesQueryPojo msPojo) {
        Set<String> allAttrs = InvBasicTablePojo.getAllBasicAttributes(msPojo.getResType()).keySet();

        Map<String, Datatype> extAttrs = InvExtTablePojo.getAllExtAttributes(msPojo.getResType());

        checkAndSetAttrs(attrsList, allAttrs, extAttrs.keySet(), null);

        for(String attrItem : attrsList) {
            if(!allAttrs.contains(attrItem)) {
                if(extAttrs.containsKey(attrItem)) {
                    msPojo.getAttrsExtList().add(attrItem);
                } else {
                    LOGGER.error("No such attr error: attr name: " + attrItem + " resType :" + msPojo.getResType());

                    throw new IllegalArgumentException(
                            "No such attr error: attr name: " + attrItem + " resType :" + msPojo.getResType());
                }
            } else {
                AttrEntity attrEntity = msPojo.new AttrEntity();
                attrEntity.setTableAlias(msPojo.getTableAlias());
                attrEntity.setAttrName(attrItem);
                msPojo.getAttrsList().add(attrEntity);
            }
        }

        if(!attrsList.contains("uuid")) {
            AttrEntity attrEntity = msPojo.new AttrEntity();
            attrEntity.setTableAlias(msPojo.getTableAlias());
            attrEntity.setAttrName("uuid");
            msPojo.getAttrsList().add(attrEntity);
        }
    }

    private void setFilterSql(String filterSql, InvMasterSlavesQueryPojo msPojo) {
        if(StringUtils.isEmpty(filterSql)) {
            LOGGER.error("filterSql is null");
            return;
        }

        String newFilterSql = "(" + filterSql + ")";

        if(StringUtils.isEmpty(msPojo.getFilterSql())) {
            msPojo.setFilterSql(newFilterSql);
        } else {
            msPojo.setFilterSql(msPojo.getFilterSql() + " and " + newFilterSql);
        }
    }

    private void setSortFields(InvMasterSlavesQueryPojo msPojo, List<String> sortList) {
        if(sortList == null || sortList.isEmpty()) {
            return;
        }

        msPojo.setOrderFields(new ArrayList<AttrEntity>());

        for(String sortItem : sortList) {
            AttrEntity sortEntity = msPojo.new AttrEntity();

            if(sortItem.indexOf('-') >= 0) {
                sortItem = sortItem.replace("-", "") + " desc";
            } else {
                // default is ascending
                sortItem += " asc";
            }
            sortEntity.setAttrName(sortItem);

            msPojo.getOrderFields().add(sortEntity);
        }
    }

    private void genDataWithEx(SqlSession session, String resType, List<Map<String, Object>> data,
            InvMasterSlavesQueryPojo msPojo) {
        Set<String> uuids = getUUIDs(data);

        InvExtTablePojo extPojo = new InvExtTablePojo(resType);
        SqlSession batchSession = getBatchSession();
        try {
            List<InvExtTablePojo> extDatas = extPojo.getExtDatas(session, batchSession, new ArrayList<String>(uuids));
            appendBaseDataWithExData(data, extDatas, msPojo.getAttrsExtList());
        } finally {
            // you must call close after use
            if(batchSession != null) {
                batchSession.close();
                batchSession = null;
            }
        }
    }

    private void appendBaseDataWithExData(List<Map<String, Object>> data, List<InvExtTablePojo> extDatas,
            List<String> extLists) {
        // TODO this need be modified as efficiency
        for(Map<String, Object> item : data) {
            String uuid = item.get("uuid").toString();
            for(InvExtTablePojo extPojo : extDatas) {
                if(uuid.equals(extPojo.getUuid())) {
                    for(String extName : extLists) {
                        if(extPojo.getAttrName().equals(extName)) {
                            item.put(extName, extPojo.getAttrValue());
                        }
                    }
                }
            }
        }
    }

    private Set<String> getUUIDs(List<Map<String, Object>> data) {
        Set<String> uuids = new HashSet<String>();

        for(Map<String, Object> item : data) {
            uuids.add(item.get("uuid").toString());
        }

        return uuids;
    }

    private String fillFilterData(String resType, String tableAlias, Object filterDscObj, Object filterDataObj) {
        return fillFilterData(resType, tableAlias, filterDscObj, filterDataObj, false);
    }

    private String fillFilterData(String resType, String tableAlias, Object filterDscObj, Object filterDataObj,
            boolean checkRelation) {

        String filterDsc = filterDscObj != null ? filterDscObj.toString().trim() : "";
        String filterData = filterDataObj != null ? filterDataObj.toString().trim() : "";

        if(StringUtils.isEmpty(filterDsc) && StringUtils.isEmpty(filterData)) {
            LOGGER.error("filterDsc:" + filterDsc + " and filterData:" + filterDsc + " is empty.");
            return null;
        } else if(StringUtils.isEmpty(filterDsc) || StringUtils.isEmpty(filterData)) {
            LOGGER.error("filterDsc:" + filterDsc + " or filterData:" + filterDsc + " is empty.");
            throw new IllegalArgumentException("filterDsc:" + filterDsc + " or filterData:" + filterDsc + " is empty.");
        }

        List<String> filterFields = new ArrayList<String>();

        String filterDscResult = parseFields(filterDsc, filterFields, tableAlias);
        HashMap<String, Object> filterDataMap =
                JsonUtil.fromJson(filterData, new TypeReference<HashMap<String, Object>>() {});

        checkFilter(filterFields, filterDataMap, resType, filterDsc, filterData, checkRelation);

        List<FieldPosition> fields = new ArrayList<FieldPosition>();
        for(Entry<String, Object> filterItem : filterDataMap.entrySet()) {
            fields.add(this.new FieldPosition(filterDsc.indexOf(filterItem.getKey()), filterItem.getKey(),
                    filterItem.getValue()));
        }

        Collections.sort(fields, new Comparator<FieldPosition>() {

            @Override
            public int compare(FieldPosition arg0, FieldPosition arg1) {
                return arg0.getPos() > arg1.getPos() ? -1 : 1;
            }

        });

        return fillFilterDsc(fields, filterFields, filterDsc, filterData, resType, checkRelation, filterDscResult);
    }

    private void checkFilter(List<String> filterFields, HashMap<String, Object> filterDataMap, String resType,
            String filterDsc, String filterData, boolean checkRelation) {
        if(filterFields.size() != filterDataMap.size()) {
            throw new IllegalArgumentException(
                    "field count in filterDsc:" + filterDsc + " and  filterData:" + filterData + " is inconsistent.");
        }

        for(String filterField : filterFields) {
            ValidUtil.checkAttributes(resType, filterField, true, checkRelation);
        }
    }

    private String fillFilterDsc(List<FieldPosition> fields, List<String> filterFields, String filterDsc,
            String filterData, String resType, boolean checkRelation, String filterDscResult) {
        String filterResult = filterDscResult;

        for(int pos = 0; pos < fields.size(); pos++) {
            FieldPosition fieldPos = fields.get(pos);

            if(fieldPos.getPos() < 0) {
                throw new IllegalArgumentException("filterDsc:" + filterDsc + " does not include filed:"
                        + fieldPos.getField() + " in filterData:" + filterDsc + " is empty.");
            }

            if(fieldPos.getValue() == null || StringUtils.isEmpty(fieldPos.getValue().toString())) {
                throw new IllegalArgumentException(
                        " field data is empty, field name :" + fieldPos.getField() + " filterData is :" + filterData);
            }

            String fieldName = filterFields.get(filterFields.size() - pos - 1);
            long startTime = System.currentTimeMillis();
            Datatype dataType = InvBasicTablePojo.getAllBasicAttributes(resType).get(fieldName);
            LOGGER.info("cost time:" + (System.currentTimeMillis() - startTime) + " ms");

            Datatype checkDataType = dataType;

            if(checkRelation) {
                Datatype relationDataType = InvRelationCombinePojo.getRelationModel().get(fieldName);
                if(relationDataType != null) {
                    checkDataType = relationDataType;
                }
            }

            checkDataType(fieldName, fieldPos.getValue(), checkDataType);

            filterResult = filterResult.replaceFirst(":" + fieldPos.getField(),
                    replaceFieldValue(fieldName, fieldPos, checkDataType));
        }

        return filterResult;
    }

    private static String replaceID2UUID(String filterField) {
        if(StringUtils.isEmpty(filterField)) {
            return filterField;
        }

        String[] ids = {"id", "src_id", "dst_id"};
        String[] uuids = {"uuid", "src_uuid", "dst_uuid"};
        for(int i = 0, len = ids.length; i < len; i++) {
            if(filterField.equals(ids[i])) {
                return uuids[i];
            }
        }

        return filterField;
    }

    private String replaceFieldValue(String field, FieldPosition fieldValue, Datatype dataType) {
        if(fieldValue.getValue() instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> fieldValueList = (List<Object>)fieldValue.getValue();
            return InvSqlFilterParserUtil.replaceInValue(fieldValueList, dataType).replace("(", "").replace(")", "");
        } else {
            return StringEscapeUtils.escapeSql(fieldValue.getValue().toString());
        }

    }

    private void checkDataType(String field, Object value, Datatype dataType) {
        if(value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> fieldValueList = (List<Object>)value;
            if(fieldValueList.isEmpty()) {
                throw new IllegalArgumentException("Json value is empty." + ", Field:" + field);
            }
            InvSqlFilterParserUtil.checkDataType(dataType, fieldValueList.get(0));
        } else {
            InvSqlFilterParserUtil.checkDataType(dataType, value);
        }
    }

    private static String parseFields(String allFieldFilter, List<String> fieldNameList, String tableAlias) {
        final String orRelation = " or ";
        final String andRelation = " and ";
        final String fieldSplit = " |=|>|<|!";

        String fieldFilter = formatFilterStr(allFieldFilter);
        String[] fieldAndPairArrs = fieldFilter.split(andRelation);
        StringBuffer sb = new StringBuffer();

        for(int andIndex = 0; andIndex < fieldAndPairArrs.length; andIndex++) {
            String fieldAndPair = fieldAndPairArrs[andIndex];
            String originalAndFieldPair = fieldAndPair;

            fieldAndPair = fieldAndPair.replaceAll("\\(", "").replaceAll("\\)", "").trim();

            String[] fieldPairArrs = new String[] {fieldAndPair};
            String[] originalFieldPairArrs = new String[] {originalAndFieldPair};
            if(fieldAndPair.contains(orRelation)) {
                fieldPairArrs = fieldAndPair.split(orRelation);
                originalFieldPairArrs = originalAndFieldPair.split(orRelation);
            }

            for(int orIndex = 0; orIndex < fieldPairArrs.length; orIndex++) {
                String fieldPair = fieldPairArrs[orIndex];
                String[] fieldPairArray = fieldPair.split(fieldSplit);

                if(fieldPairArray.length > 1) {
                    String fieldName = fieldPairArray[0].trim();
                    String replaceField = replaceID2UUID(fieldName);
                    String originalFieldPair =
                            originalFieldPairArrs[orIndex].replaceFirst(fieldName, tableAlias + "." + replaceField);

                    if(fieldPairArrs.length > 1 && orIndex < fieldPairArrs.length - 1) {
                        sb.append(originalFieldPair + orRelation); // NOPMD
                    } else if(fieldPairArrs.length == 1 || orIndex == fieldPairArrs.length - 1) {
                        sb.append(originalFieldPair); // NOPMD
                    }

                    fieldNameList.add(replaceField);
                } else {
                    LOGGER.error("filterDsc invalid at :" + fieldPair + " filterDsc:" + fieldFilter);

                    throw new IllegalArgumentException(
                            "filterDsc invalid at :" + fieldPair + " filterDsc:" + fieldFilter);
                }
            }

            if(fieldAndPairArrs.length > 1 && andIndex < fieldAndPairArrs.length - 1) {
                sb.append(andRelation);
            }

        }

        return sb.toString();
    }

    private static String formatFilterStr(String filterDsc) {
        if(StringUtils.isEmpty(filterDsc)) {
            return filterDsc;
        }

        String andRelaxTwoBrak = "\\)\\s*[A|a][N|n][D|d]\\s*\\(";
        String andRelaxLeftBrak = "\\)\\s*[A|a][N|n][D|d]\\s";
        String andRelaxRightBrak = "\\s[A|a][N|n][D|d]\\s*\\(";
        String andRelaxNoBrak = "\\s[A|a][N|n][D|d]\\s";

        String orRelaxTwoBrak = "\\)\\s*[O|o][R|r]\\s*\\(";
        String orRelaxLeftBrak = "\\)\\s*[O|o][R|r]\\s";
        String orRelaxRightBrak = "\\s[O|o][R|r]\\s*\\(";
        String orRelaxNoBrak = "\\s[O|o][R|r]\\s";

        String formatFilterDsc = filterDsc.replaceAll(andRelaxTwoBrak, ") and (").replaceAll(andRelaxLeftBrak, ") and ")
                .replaceAll(andRelaxRightBrak, " and (").replaceAll(andRelaxNoBrak, " and ")
                .replaceAll(orRelaxTwoBrak, ") or (").replaceAll(orRelaxLeftBrak, ") or ")
                .replaceAll(orRelaxRightBrak, " or (").replaceAll(orRelaxNoBrak, " or ");

        LOGGER.info("filterDsc: " + formatFilterDsc);

        return formatFilterDsc;
    }

    private class FieldPosition {

        private final Integer pos;

        private final String field;

        private final Object value;

        /**
         * Constructor<br>
         * 
         * @since SDNO 0.5
         * @param pos The position
         * @param field The field
         * @param value The value
         */
        public FieldPosition(Integer pos, String field, Object value) {
            this.pos = pos;
            this.field = field;
            this.value = value;
        }

        public Integer getPos() {
            return pos;
        }

        public Object getValue() {
            return value;
        }

        public String getField() {
            return field;
        }
    }

    @Override
    public Boolean exist(String resType, String attrName, Object attrVal) {
        Map<String, Datatype> basicAttrs = InvBasicTablePojo.getAllBasicAttributes(resType);
        Map<String, Datatype> extAttrs = InvExtTablePojo.getAllExtAttributes(resType);

        AInvCheckerPojo pojo = null;

        if(basicAttrs.containsKey(attrName)) {
            pojo = new InvBasicCheckerPojo(resType, attrName, attrVal);
        } else if(extAttrs.containsKey(attrName)) {
            pojo = new InvExtCheckerPojo(resType, attrName, attrVal);
        } else {
            throw new IllegalArgumentException("Resource type " + resType + " is not support: " + attrName);
        }

        int cnt = pojo.exist(getSqlSession());

        return cnt > 0;
    }
}
