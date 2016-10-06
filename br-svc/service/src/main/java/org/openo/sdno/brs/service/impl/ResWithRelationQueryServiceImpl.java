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

package org.openo.sdno.brs.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.RelationField;
import org.openo.sdno.brs.model.ResourcePagePara;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.ResWithRelationQueryService;
import org.openo.sdno.brs.util.ResWithRelationQueryUtil;
import org.openo.sdno.brs.util.http.HttpRelationUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation class of relation query.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ResWithRelationQueryServiceImpl implements ResWithRelationQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResWithRelationQueryServiceImpl.class);

    private String bucketName;

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    private Map<String, List<String>> relationInfoMap;

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RootEntity> T getResourceByID(String objectID, Class<T> classType) throws ServiceException {

        RestfulResponse response = mssProxy.getResource(bucketName, resourceTypeName, objectID);
        ResponseUtils.checkResonseAndThrowException(response);

        T result = (T)HttpResponseUtil.assembleRspData(response.getResponseContent(), classType);

        if(!StringUtils.isEmpty(result.getId())) {
            List<T> resourceLst = new ArrayList<T>();
            resourceLst.add(result);
            initRelationInfoMap(classType);

            queryResLstRuleRelations(resourceLst, relationInfoMap.keySet());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getResources(String fields, String key, Map<String, String> filterMap, int pagesize,
            int pagenum, Class<?> classType) throws ServiceException {
        initRelationInfoMap(classType);
        ResourcePagePara pagePara = new ResourcePagePara(pagesize, pagenum, fields, filterMap, relationInfoMap);
        Map<String, Object> resourceRspMap = queryResourceList(key, pagePara, classType);

        try {
            resourceRspMap = org.openo.sdno.brs.util.json.JsonUtil
                    .unMarshal(org.openo.sdno.brs.util.json.JsonUtil.marshal(resourceRspMap), Map.class);
        } catch(IOException e) {
            LOGGER.error("mss responseContent form is error!", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, "mss responseContent form is error!");
        }
        return resourceRspMap;
    }

    /**
     * Get relations.<br>
     * 
     * @param dstType Destination Type
     * @param srcIds source Ids
     * @param dstIds Destination Ids
     * @return The relation list
     * @since SDNO 0.5
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Relation> getRelations(String dstType, String srcIds, String dstIds) throws ServiceException {
        String[] paras = {dstType, srcIds, dstIds};
        LOGGER.info("getRelations dstType={}, srcids={}, dstids={}", paras);
        RestfulResponse response = mssProxy.getRelations(bucketName, resourceTypeName, dstType, srcIds, dstIds);
        ResponseUtils.checkResonseAndThrowException(response);

        return (List)HttpResponseUtil.assembleRspData(response.getResponseContent(), Relation.class);
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    @SuppressWarnings("rawtypes")
    private <T> Map<String, Object> queryResourceList(String key, ResourcePagePara pagePara, Class<T> classType)
            throws ServiceException {

        String fields = ResWithRelationQueryUtil.convertToFieldString(pagePara.getResFieldList());
        Map<String, Object> basefilters =
                ResWithRelationQueryUtil.convertToFilterString(pagePara.getBaseFilters(), classType);
        Map<String, Object> relationfilters = convertToRelationFilterString(pagePara.getRelationFilters());

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put(Constant.BASIC, basefilters);
        filterMap.put(Constant.RELATION, relationfilters);

        String filters = org.openo.sdno.framework.container.util.JsonUtil.toJson(filterMap);
        RestfulResponse response = mssProxy.getRelationResourceList(bucketName, resourceTypeName, fields, filters,
                pagePara.getPagesize(), pagePara.getPagenum());
        ResponseUtils.checkResonseAndThrowException(response);

        Map<String, Object> resourceMap = new HashMap<String, Object>();
        PageResponseData pageRsp = new PageResponseData();
        List<T> resourceLst =
                HttpRelationUtil.assembleListRspWithRelationData(response.getResponseContent(), pageRsp, classType);
        filterResList(resourceLst, pagePara, classType);
        resourceMap.put(key, resourceLst);

        resourceMap.put(Constant.RESPONSE_TOTALNUM, pageRsp.getTotalNum());
        resourceMap.put(Constant.RESPONSE_PAGESIZE, pageRsp.getPageSize());
        resourceMap.put(Constant.RESPONSE_TOPAGENUM, pageRsp.getTotalPageNum());
        resourceMap.put(Constant.RESPONSE_CURPAGENUM, pageRsp.getCurrentPageNum());

        return resourceMap;
    }

    private <T> void filterResList(List<T> resourceLst, ResourcePagePara pagePara, Class<T> classType) {
        if(CollectionUtils.isEmpty(resourceLst)) {
            return;
        }

        List<String> lstRes = pagePara.getResFieldList();
        if(CollectionUtils.isEmpty(lstRes)) {
            ResWithRelationQueryUtil.filterBaseAndExtAttr(resourceLst, classType);
        }

        filterRelation(resourceLst, pagePara.getRelationFields());
    }

    private <T> void filterRelation(List<T> resourceLst, Map<String, String> relationFields) {
        Set<String> keys = relationInfoMap.keySet();
        List<String> cleanParaNames = new ArrayList<String>();
        for(String dbName : keys) {
            if(!relationFields.containsKey(dbName)) {
                List<String> values = relationInfoMap.get(dbName);
                String paraName = values.get(1);
                cleanParaNames.add(paraName);
            }
        }

        for(T resource : resourceLst) {
            try {
                if(!CollectionUtils.isEmpty(cleanParaNames)) {
                    for(String paraName : cleanParaNames) {
                        Field field = resource.getClass().getDeclaredField(paraName);
                        field.setAccessible(true);
                        field.set(resource, null);
                    }

                }
            } catch(NoSuchFieldException | SecurityException | IllegalAccessException ex) {
                LOGGER.error("filterRelation failed,ex={}", ex);
            }
        }
    }

    private Map<String, Object> convertToRelationFilterString(Map<String, String> relationQueryMap)
            throws ServiceException {
        Map<String, Object> filtersMap = new HashMap<String, Object>();

        try {

            if(relationQueryMap == null || CollectionUtils.isEmpty(relationQueryMap.values())) {
                return filtersMap;
            }

            String desc = "";
            List<Object> lstData = new ArrayList<Object>();
            List<Object> lstDsc = new ArrayList<Object>();
            for(Map.Entry<String, String> entry : relationQueryMap.entrySet()) {

                String queryProperty = entry.getKey();

                // dst_type:controler,site
                List<String> relationInfos = relationInfoMap.get(queryProperty);
                String queryPtyIDValue = "";
                String queryPtyTypeValue = "";

                Map<String, Object> filtersData = new HashMap<String, Object>();
                if(!CollectionUtils.isEmpty(relationInfos)) {
                    // "(dst_id = ':dst_id1\' and dst_type = ':dst_type1')\",
                    queryPtyIDValue = relationInfos.get(0) + "IDValue";
                    queryPtyTypeValue = relationInfos.get(0) + "TypeValue";
                    desc = new StringBuilder(Constant.RELATION_ID).append(Constant.FILTER_EQUAL)
                            .append(Constant.FILTER_QUOTE).append(Constant.COLON).append(queryPtyIDValue)
                            .append(Constant.FILTER_QUOTE).append(Constant.FILTER_AND).append(Constant.RELATION_DSTTYPE)
                            .append(Constant.FILTER_EQUAL).append(Constant.FILTER_QUOTE).append(Constant.COLON)
                            .append(queryPtyTypeValue).append(Constant.FILTER_QUOTE).toString();

                    filtersData.put(queryPtyIDValue, entry.getValue());
                    filtersData.put(queryPtyTypeValue, relationInfos.get(0));
                    lstData.add(filtersData);
                    lstDsc.add(desc);
                }

            }

            filtersMap.put(Constant.FILTER_DSC, JsonUtil.marshal(lstDsc));
            filtersMap.put(Constant.FILTER_DATA, JsonUtil.marshal(lstData));

            return filtersMap;
        } catch(IOException ex) {
            LOGGER.error("brs has bad param.", ex);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

    }

    private <T extends RootEntity> void queryResLstRuleRelations(List<T> resourceLst,
            Collection<String> relationFieldList) throws ServiceException {
        if(CollectionUtils.isEmpty(resourceLst) || CollectionUtils.isEmpty(relationFieldList)) {
            return;
        }

        String srcFields = ResWithRelationQueryUtil.getResourcesIds(resourceLst);

        List<Relation> relationList = null;
        Map<String, Map<String, List<String>>> allRelationMap = new HashMap<String, Map<String, List<String>>>();
        if(!StringUtils.isEmpty(srcFields)) {
            for(String relationField : relationFieldList) {
                if(relationInfoMap.containsKey(relationField)) {
                    String modelName = relationInfoMap.get(relationField).get(0);
                    String paraName = relationInfoMap.get(relationField).get(1);
                    relationList = getRelations(modelName, srcFields, null);
                    allRelationMap.put(paraName, ResWithRelationQueryUtil.getRelationMap(relationList));
                }
            }

            setRelationInfo(resourceLst, allRelationMap);
        }

    }

    private <T extends RootEntity> void setRelationInfo(List<T> resourceLst,
            Map<String, Map<String, List<String>>> allRelationMap) {

        Map<String, List<String>> relationDataMap = null;
        for(T resource : resourceLst) {
            Set<String> keys = relationInfoMap.keySet();
            for(String key : keys) {
                try {
                    List<String> values = relationInfoMap.get(key);
                    String paraName = values.get(1);
                    Field field = resource.getClass().getDeclaredField(paraName);
                    relationDataMap = allRelationMap.get(paraName);

                    if((null != field) && (null != relationDataMap)) {
                        List<String> datas = relationDataMap.get(resource.getId());
                        if(!CollectionUtils.isEmpty(datas)) {
                            field.setAccessible(true);
                            if(field.getType().equals(String.class)) {
                                field.set(resource, datas.get(0));
                                continue;
                            }

                            field.set(resource, datas);
                        }
                    }
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException
                        | IllegalAccessException ex) {
                    LOGGER.info("setRelationInfo failed,ex={}", ex);
                }
            }
        }
    }

    private void initRelationInfoMap(Class<?> classType) {
        if(null != relationInfoMap) {
            return;
        }

        relationInfoMap = new HashMap<String, List<String>>();
        Field[] fields = classType.getDeclaredFields();

        for(Field field : fields) {
            if(field.isAnnotationPresent(RelationField.class)) {
                List<String> infoList = new ArrayList<String>();
                RelationField relationField = field.getAnnotation(RelationField.class);
                infoList.add(relationField.modelName());
                infoList.add(relationField.paraName());
                String dbName = relationField.dbName();
                relationInfoMap.put(dbName, infoList);
            }
        }

    }
}
