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

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.brs.validator.rules.StrEnumRule;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.openo.sdno.rest.RoaExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class HttpResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseUtil.class);

    private HttpResponseUtil() {
    }

    /**
     * Transfer restful request to inventory model.<br>
     * 
     * @param requestStr restful request
     * @param key key
     * @param type type
     * @return inventory model data
     * @throws ServiceException when transfer failed
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDataModelFromReqStr(String requestStr, String key, Class<T> type) throws ServiceException {
        ValidateUtil.assertNotEmpty(requestStr, "requestBodyStr");

        Map<String, Object> requestMap = new HashMap<String, Object>();
        try {
            requestMap = JsonUtil.unMarshal(requestStr, Map.class);
        } catch(IOException e) {
            LOGGER.debug("getDataModelFromReqStr failed ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        Object dataModelObj = requestMap.get(key);
        ValidateUtil.assertNotEmpty(String.valueOf(dataModelObj), key);
        if(!(dataModelObj instanceof Map)) {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        T data;
        try {
            data = type.newInstance();
        } catch(InstantiationException e1) {
            LOGGER.debug("getDataModelFromReqStr failed ", e1);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        } catch(IllegalAccessException e1) {
            LOGGER.debug("getDataModelFromReqStr failed ", e1);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        try {
            data = JsonUtil.unMarshal(JsonUtil.marshal(dataModelObj), type);
        } catch(IOException e) {
            LOGGER.debug("getDataModelFromReqStr failed ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        return data;
    }

    /**
     * Process data returned by batch query.<br>
     * 
     * @param responseContent response content
     * @param pageRsp page response data
     * @param type type
     * @return list of data model
     * @throws ServiceException when process failed
     * @since SDNO 0.5
     */
    @SuppressWarnings({"rawtypes"})
    public static <T> List<T> assembleListRspData(String responseContent, PageResponseData pageRsp, Class<T> type)
            throws ServiceException {
        ValidateUtil.assertNotEmpty(responseContent, "responseContent");

        List<T> dataModelList = new ArrayList<T>();

        PageQueryResult pageResult = new PageQueryResult();
        try {
            pageResult = JsonUtil.unMarshal(responseContent, PageQueryResult.class);
        } catch(IOException e) {
            LOGGER.debug("assembleListRspData failed ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        Object data = pageResult.getObjects();
        if(null == data) {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
        if(data instanceof List) {
            // Transfer data to inventory model
            for(Object dataModelObj : (List)data) {
                if(!(dataModelObj instanceof Map)) {
                    throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
                }

                dataModelList.add(HttpRelationUtil.transferDataModel((Map)dataModelObj, type));
            }
        }

        pageRsp.setTotalNum(pageResult.getTotal());
        pageRsp.setPageSize(pageResult.getPageSize());
        pageRsp.setTotalPageNum(pageResult.getTotalPageNum());
        pageRsp.setCurrentPageNum(pageResult.getCurrentPage());

        return dataModelList;
    }

    /**
     * Process restful response data.<br>
     * 
     * @param responseContent response content
     * @param type type
     * @return list of data model
     * @throws ServiceException when process failed
     * @since SDNO 0.5
     */
    @SuppressWarnings({"rawtypes"})
    public static <T> Object assembleRspData(String responseContent, Class<T> type) throws ServiceException {
        ValidateUtil.assertNotEmpty(responseContent, "responseContent");

        List<T> dataModelList = new ArrayList<T>();

        Map map = null;
        try {
            map = JsonUtil.unMarshal(responseContent, Map.class);
        } catch(IOException e) {
            LOGGER.debug("assembleRspData failed ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        if(null == map) {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        // If there is object field, it is a single modify or query operation.
        // Structure is {"object":{}}.
        if(map.containsKey(Constant.OBJECT_KEY)) {
            return HttpRelationUtil.transferDataModel((Map)map.get(Constant.OBJECT_KEY), type);
        }

        Object obj = null;

        if(map.containsKey(Constant.RELATIONS_KEY)) {
            obj = map.get(Constant.RELATIONS_KEY);
        }

        if(map.containsKey(Constant.OBJECTS_KEY)) {
            obj = map.get(Constant.OBJECTS_KEY);
        }

        if(obj instanceof List) {
            // Transfer data to inventory model
            for(Object dataModelObj : (List)obj) {
                if(!(dataModelObj instanceof Map)) {
                    throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
                }

                dataModelList.add(HttpRelationUtil.transferDataModel((Map)dataModelObj, type));
            }
        } else {
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }

        return dataModelList;
    }

    /**
     * Get filter's filter attribute information.<br>
     * 
     * @param queryMap map of query info
     * @param classType class type
     * @return filter attribute information
     * @throws IOException when operate failed
     * @since SDNO 0.5
     */
    public static <T> String getFilterValue(Map<String, String> queryMap, Class<T> classType) throws IOException {
        Map<String, Object> filtersMap = new HashMap<String, Object>();
        if(queryMap == null || CollectionUtils.isEmpty(queryMap.values())) {
            return JsonUtil.marshal(filtersMap);
        }

        Map<String, Object> filtersData = new HashMap<String, Object>();
        String filtersDsc = "";
        List<String> enumList = getEnumParaNames(classType);
        for(Map.Entry<String, String> entry : queryMap.entrySet()) {
            // Assemble query attribute filter info
            String queryProperty = entry.getKey();
            String queryPtyValue = queryProperty + "Value";
            if(enumList.contains(queryProperty)) {
                // Exact match of enumeration values.
                filtersDsc += new StringBuilder(queryProperty).append(Constant.FILTER_EQUAL)
                        .append(Constant.FILTER_QUOTE).append(Constant.COLON).append(queryPtyValue)
                        .append(Constant.FILTER_QUOTE).append(Constant.FILTER_AND).toString();
            } else {
                filtersDsc += new StringBuilder(queryProperty).append(Constant.FILTER_LIKE)
                        .append(Constant.FILTER_QUOTE).append(Constant.FILTER_PER).append(Constant.COLON)
                        .append(queryPtyValue).append(Constant.FILTER_PER).append(Constant.FILTER_QUOTE)
                        .append(Constant.FILTER_AND).toString();
            }

            filtersData.put(queryPtyValue, entry.getValue());
        }

        // Delete last AND
        filtersDsc = filtersDsc.substring(0, filtersDsc.length() - Constant.FILTER_AND.length());
        filtersMap.put(Constant.FILTER_DSC, filtersDsc);
        filtersMap.put(Constant.FILTER_DATA, JsonUtil.marshal(filtersData));

        return JsonUtil.marshal(filtersMap);
    }

    /**
     * Check the response of restful request, if status is not OK then throw a exception, only for
     * internal interface use. <br>
     * 
     * @param response restful response
     * @return null
     * @throws ServiceException if status is not OK
     * @since SDNO 0.5
     */
    public static String checkResonseAndThrowException(RestfulResponse response) throws ServiceException {
        if(!HttpCode.isSucess(response.getStatus())) {
            RoaExceptionInfo roaExceptionInfo = null;
            try {
                roaExceptionInfo = JsonUtil.unMarshal(response.getResponseContent(), RoaExceptionInfo.class);
            } catch(IOException e) {
                LOGGER.error("parse failed: ", e);
                return null;
            }
            if(null == roaExceptionInfo) {
                return null;
            }
            ServiceException serviceException = new ServiceException();
            serviceException.setHttpCode(response.getStatus());
            serviceException.setId(roaExceptionInfo.getExceptionId());
            serviceException.setExceptionArgs(
                    new ExceptionArgs(roaExceptionInfo.getDescArgs(), roaExceptionInfo.getReasonArgs(),
                            roaExceptionInfo.getDetailArgs(), roaExceptionInfo.getAdviceArgs()));
            throw serviceException;
        }
        return null;
    }

    private static <T> List<String> getEnumParaNames(Class<T> classType) {
        Class<?> currentClass = classType;
        List<String> enumList = new ArrayList<String>();

        while(!currentClass.equals(Object.class)) {
            Field[] fields = currentClass.getDeclaredFields();
            for(Field field : fields) {
                if(field.isAnnotationPresent(StrEnumRule.class)) {
                    StrEnumRule enumRule = field.getAnnotation(StrEnumRule.class);
                    enumList.add(enumRule.paramName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return enumList;
    }
}
