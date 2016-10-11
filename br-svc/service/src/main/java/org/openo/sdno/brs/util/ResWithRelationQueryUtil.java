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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.RelationField;
import org.openo.sdno.brs.model.RootEntity;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class of ResWithRelationQuery.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-22
 */
public class ResWithRelationQueryUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResWithRelationQueryUtil.class);

    private ResWithRelationQueryUtil() {

    }

    /**
     * Convert filed to filed query format.<br>
     * 
     * @param fieldList list of filed
     * @return filed query string
     * @since SDNO 0.5
     */
    public static String convertToFieldString(List<String> fieldList) {
        StringBuffer buffer = new StringBuffer();
        for(String field : fieldList) {
            buffer.append(field);
            buffer.append(Constant.COMMA);
        }
        String fields = buffer.toString();
        if(fields.length() > 0) {
            fields = fields.substring(0, fields.length() - 1);
        }

        return fields;
    }

    /**
     * Convert filter map to filter String.<br>
     * 
     * @param filterMap filter map
     * @param classType class type
     * @return filter String
     * @throws ServiceException when convert failed
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> convertToFilterString(Map<String, String> filterMap, Class<T> classType)
            throws ServiceException {
        String filters = null;
        try {
            filters = HttpResponseUtil.getFilterValue(filterMap, classType);
        } catch(IOException e) {
            LOGGER.error("brs has bad param.", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
        return JsonUtil.fromJson(filters, Map.class);
    }

    /**
     * Get resource UUid.<br>
     * 
     * @param resourceList list of resource
     * @return resource UUID string
     * @since SDNO 0.5
     */
    public static <T extends RootEntity> String getResourcesIds(List<T> resourceList) {

        StringBuffer buffer = new StringBuffer();
        for(T resource : resourceList) {
            if(null != resource.getId()) {
                buffer.append(resource.getId());
                buffer.append(Constant.COMMA);
            }
        }
        String srcFields = buffer.toString();
        if(srcFields.length() > 0) {
            srcFields = srcFields.substring(0, srcFields.length() - 1);
        }

        return srcFields;
    }

    /**
     * Filter Base And Extensive Attribute.<br>
     * 
     * @param resourceLst List of resource
     * @param classType Class Type
     * @since SDNO 0.5
     */
    public static <T> void filterBaseAndExtAttr(List<T> resourceLst, Class<T> classType) {
        Class<?> currentClass = classType;

        List<Field> lstField = new ArrayList<Field>();
        lstField.addAll(Arrays.asList(currentClass.getDeclaredFields()));
        Class<?> superClassType = currentClass.getSuperclass();
        while(null != superClassType) {
            lstField.addAll(Arrays.asList(superClassType.getDeclaredFields()));
            superClassType = superClassType.getSuperclass();
        }

        for(T resource : resourceLst) {
            for(Field field : lstField) {
                if((field.isAnnotationPresent(RelationField.class)) || field.getName().equals(Constant.RESOURCE_ID)) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    field.set(resource, null);
                } catch(SecurityException | IllegalArgumentException | IllegalAccessException exception) {
                    LOGGER.error("filterBaseAndExtAttr: fail to set null", exception);
                }
            }
        }
    }

    /**
     * Get Map of Relation.<br>
     * 
     * @param relations List of Relation
     * @return Map of Relation
     * @since SDNO 0.5
     */
    public static Map<String, List<String>> getRelationMap(List<Relation> relations) {
        Map<String, List<String>> relationMap = new HashMap<String, List<String>>();
        for(Relation relation : relations) {
            List<String> relationList = relationMap.get(relation.getSrcId());
            if(null == relationList) {
                relationList = new ArrayList<String>();
            }

            relationList.add(relation.getDstId());
            relationMap.put(relation.getSrcId(), relationList);
        }
        return relationMap;
    }

}
