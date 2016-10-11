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

package org.openo.sdno.brs.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.common.util.CollectionUtils;
import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.validator.InputParaValidator.InputParaCheck;
import org.openo.sdno.brs.validator.rules.SupportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Paging query check utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class PagingQueryCheckUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagingQueryCheckUtil.class);

    private PagingQueryCheckUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Analyze parameters in query request.<br>
     * 
     * @param queryString query request
     * @return result of analysis
     * @throws ServiceException
     * @since SDNO 0.5
     */
    public static PagingQueryPara analysicQueryString(String queryString) throws ServiceException {
        PagingQueryPara pagingQueryPara = new PagingQueryPara();
        if(queryString == null || queryString.isEmpty()) {
            return pagingQueryPara;
        }

        Map<String, String> queryMap = new HashMap<String, String>();
        String[] queryStrArray = queryString.split(Constant.AND);
        for(int i = 0; i < queryStrArray.length; i++) {
            String[] propertyStrArray = queryStrArray[i].split(Constant.EQUIVALENT);
            String propertyName = propertyStrArray[0];
            String propertyValue = null;
            if(propertyStrArray.length == 2) {
                propertyValue = propertyStrArray[1];
            }
            queryMap.put(propertyName, propertyValue);
        }

        int pageSizeValue = getPageSize(queryMap);
        int pageNumValue = getPageNum(queryMap);

        String fieldsValue = queryMap.get(Constant.RESOUCRCE_FILEDS);

        /**
         * Remove pageNum, pageSize, fields. The rest of the data belong to filters.
         */
        queryMap.remove(Constant.RESOUCRCE_PAGESIZE);
        queryMap.remove(Constant.RESOUCRCE_PAGENUM);
        queryMap.remove(Constant.RESOUCRCE_FILEDS);

        pagingQueryPara.setPageSize(pageSizeValue);
        pagingQueryPara.setPageNum(pageNumValue);
        pagingQueryPara.setFields(fieldsValue);
        pagingQueryPara.setFiltersMap(queryMap);

        return pagingQueryPara;
    }

    /**
     * Get page size, and do conversion check.<br>
     */
    private static int getPageSize(Map<String, String> queryMap) throws ServiceException {

        // Default value is 1000.
        int pageSize = 1000;

        String pageSizePara = queryMap.get(Constant.RESOUCRCE_PAGESIZE);
        if(pageSizePara != null && !pageSizePara.isEmpty()) {
            try {
                pageSize = Integer.parseInt(pageSizePara);
            } catch(NumberFormatException e) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_PAGESIZE, null, pageSizePara, "1-1000");
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()), e);
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }
        }

        return pageSize;
    }

    /**
     * Get page number, and do conversion check.<br>
     */
    private static int getPageNum(Map<String, String> queryMap) throws ServiceException {
        int pageNum = 0;
        String pageNumPara = queryMap.get(Constant.RESOUCRCE_PAGENUM);

        if(pageNumPara != null && !pageNumPara.isEmpty()) {
            try {
                pageNum = Integer.parseInt(pageNumPara);
            } catch(NumberFormatException e) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_PAGENUM, null, pageNumPara, null);
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()), e);
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }
        }

        return pageNum;
    }

    /**
     * Check pageSize's parameter range. 1-1000.<br>
     */
    private static void checkPageSize(int pageSize) throws ServiceException {
        if(pageSize > 1000 || pageSize < 1) {
            ExceptionArgs eArgs =
                    genExceptionArgs(Constant.RESOUCRCE_PAGESIZE, null, String.valueOf(pageSize), "1-1000");
            LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
            throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
        }
    }

    /**
     * Check pageNum's parameter range.<br>
     */
    private static void checkPageNum(int pageNum) throws ServiceException {
        if(pageNum < 0) {
            ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_PAGENUM, null, String.valueOf(pageNum), null);
            LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
            throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
        }
    }

    /**
     * Check fields.<br>
     */
    private static <T extends RootEntity> void checkFields(String fieldsValue, Map<String, Field> allFieldsMap,
            Class<T> classType) throws ServiceException {
        if(Constant.FILEDS_BASE.equals(fieldsValue) || Constant.FILEDS_ALL.equals(fieldsValue)) {
            initAllFields(allFieldsMap, classType);
            return;
        }

        List<String> fieldsList = Arrays.asList(fieldsValue.split(","));

        if(CollectionUtils.isEmpty(fieldsList)) {
            ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_FILEDS, null, null, null);
            LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
            throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
        }

        initAllFields(allFieldsMap, classType);

        for(String field : fieldsList) {
            if(!allFieldsMap.containsKey(field)) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_FILEDS, null, field, null);
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }
        }
    }

    /**
     * Check filters.<br>
     */
    private static void checkFilters(Map<String, String> filtersMap, Map<String, Field> allFieldsMap,
            Class<?> classType) throws ServiceException {

        if(filtersMap == null || CollectionUtils.isEmpty(filtersMap.values())) {
            return;
        }

        Set<String> filtersKey = filtersMap.keySet();
        for(String key : filtersKey) {
            Field field = allFieldsMap.get(key);
            if(null == field) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_FILTERS, null, key, null);
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }

            Boolean isSupportFilter = false;
            Annotation[] annotation = field.getAnnotations();
            for(Annotation tmpAnn : Arrays.asList(annotation)) {
                if(tmpAnn.annotationType() == (SupportFilter.class)) {
                    isSupportFilter = true;
                    break;
                }
            }

            if(!isSupportFilter) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_FILTERS, null, key, null);
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }

            if(filtersMap.get(key) == null || filtersMap.get(key).isEmpty()) {
                ExceptionArgs eArgs = genExceptionArgs(Constant.RESOUCRCE_FILTERS, null, key, null);
                LOGGER.error(Arrays.toString(eArgs.getReasonArgs()));
                throw new ServiceException(ErrorCode.BRS_PAGINGQUERY_PARAMETERS_ERROR, HttpCode.NOT_ACCEPTABLE, eArgs);
            }

            // Check if it is in accordance with the annotation.
            InputParaCheck.inputAttrCheck(key, filtersMap.get(key), classType);
        }
    }

    /**
     * Check page query parameters.<br>
     * 
     * @param paraMap parameters
     * @param classType class type
     * @throws ServiceException when check failed
     * @since SDNO 0.5
     */
    public static <T extends RootEntity> void checkPagedParameters(PagingQueryPara paraMap, Class<T> classType)
            throws ServiceException {
        checkPageNum(paraMap.getPageNum());

        checkPageSize(paraMap.getPageSize());

        Map<String, Field> fieldsMap = new HashMap<String, Field>();
        String fieldsString = paraMap.getFields();
        checkFields(fieldsString, fieldsMap, classType);

        Map<String, String> filtersMap = paraMap.getFiltersMap();
        checkFilters(filtersMap, fieldsMap, classType);
    }

    /**
     * Initialize fieldsMap.<br>
     */
    private static <T extends RootEntity> void initAllFields(Map<String, Field> fieldsMap, Class<T> classType) {
        Class<?> currentClass = classType;
        while(!(currentClass == Object.class)) {
            Field[] fields = currentClass.getDeclaredFields();
            for(Field index : fields) {
                if(!fieldsMap.containsKey(index.getName())) {
                    fieldsMap.put(index.getName(), index);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    /**
     * Convert error message to error message list displayed on the foreground. <br>
     */
    private static ExceptionArgs genExceptionArgs(String desc, String reason, String detail, String advice) {
        ExceptionArgs eArgs = new ExceptionArgs();

        if(advice != null) {
            eArgs.setAdviceArgs(new String[] {advice});
        }

        eArgs.setDescArgs(new String[] {desc});

        if(detail != null) {
            eArgs.setDetailArgs(new String[] {detail});
        }

        return eArgs;
    }
}
