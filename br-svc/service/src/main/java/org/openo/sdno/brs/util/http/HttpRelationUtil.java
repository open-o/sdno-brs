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

package org.openo.sdno.brs.util.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.RelationField;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class of HTTP Relation.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class HttpRelationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRelationUtil.class);

    private HttpRelationUtil() {
        // Private Construction Function
    }

    /**
     * Transfer map data to inventory model data.<br>
     * 
     * @param dataModelObj map data object
     * @param type type
     * @return list of data model
     * @throws ServiceException when process failed
     * @since SDNO 0.5
     */
    @SuppressWarnings("rawtypes")
    public static <T> T transferDataModel(Map dataModelObj, Class<T> type) throws ServiceException {
        T data;
        try {
            data = type.newInstance();
        } catch(InstantiationException | IllegalAccessException e1) {
            LOGGER.debug("transferDataModel failed ", e1);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        try {
            Map<String, List<String>> relationMap = null;
            Object relationDatas = dataModelObj.get(Constant.RELATION);
            if(null != relationDatas) {
                // Initialize relation data
                relationMap = transferRelationDatas(relationDatas);
                dataModelObj.remove(Constant.RELATION);
            }

            data = JsonUtil.unMarshal(JsonUtil.marshal(dataModelObj), type);

            setRelationDatas(data, relationMap);

        } catch(SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
            LOGGER.debug("transferDataModel failed ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        return data;
    }

    /**
     * Process response data and relation data. <br>
     * 
     * @param responseContent response content
     * @param pageRsp page response data
     * @param type type
     * @return list of data model
     * @throws ServiceException when process failed
     * @since SDNO 0.5
     */
    @SuppressWarnings({"rawtypes"})
    public static <T> List<T> assembleListRspWithRelationData(String responseContent, PageResponseData pageRsp,
            Class<T> type) throws ServiceException {
        ValidateUtil.assertNotEmpty(responseContent, "responseContent");

        List<T> dataModelList = new ArrayList<T>();

        PageQueryResult pageResult = new PageQueryResult();
        pageResult = org.openo.sdno.framework.container.util.JsonUtil.fromJson(responseContent, PageQueryResult.class);

        Object data = pageResult.getObjects();

        if((null != data) && (data instanceof List)) {
            // Transfer data to inventory model
            for(Object dataModelObj : (List)data) {
                if(!(dataModelObj instanceof Map)) {
                    throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
                }

                dataModelList.add(transferDataModel((Map)dataModelObj, type));
            }
        }

        pageRsp.setTotalNum(pageResult.getTotal());
        pageRsp.setPageSize(pageResult.getPageSize());
        pageRsp.setTotalPageNum(pageResult.getTotalPageNum());
        pageRsp.setCurrentPageNum(pageResult.getCurrentPage());

        return dataModelList;
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, List<String>> transferRelationDatas(Object relationDatas) {
        Map<String, List<String>> relationMap = null;
        if((null != relationDatas) && (relationDatas instanceof List)) {
            relationMap = new HashMap<String, List<String>>();
            for(Object relationData : (List)relationDatas) {
                if(relationData instanceof Map) {
                    String dstID = (String)((Map)relationData).get(Constant.RELATION_ID);
                    String dstType = (String)((Map)relationData).get(Constant.RELATION_DSTTYPE);
                    List<String> dstIdList = null;
                    if(null == relationMap.get(dstType)) {
                        dstIdList = new ArrayList<String>();
                        dstIdList.add(dstID);
                        relationMap.put(dstType, dstIdList);
                    } else {
                        dstIdList = relationMap.get(dstType);
                        dstIdList.add(dstID);
                    }
                }

            }
        }
        return relationMap;
    }

    private static <T> void setRelationDatas(T data, Map<String, List<String>> relationMap)
            throws IllegalAccessException {
        if(null != relationMap) {
            Field[] fields = data.getClass().getDeclaredFields();
            for(Field field : fields) {

                if(field.isAnnotationPresent(RelationField.class)) {
                    RelationField relationField = field.getAnnotation(RelationField.class);
                    String modelName = relationField.modelName();
                    if(null != relationMap.get(modelName)) {
                        field.setAccessible(true);
                        if(field.getType().equals(String.class)) {
                            field.set(data, relationMap.get(modelName).get(0));
                            continue;
                        } else if(field.getType().equals(List.class)) {
                            field.set(data, relationMap.get(modelName));
                        }
                    }
                }
            }

        }
    }
}
