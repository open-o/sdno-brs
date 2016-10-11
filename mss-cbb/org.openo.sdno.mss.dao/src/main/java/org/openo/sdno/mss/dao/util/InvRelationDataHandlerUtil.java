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

package org.openo.sdno.mss.dao.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.pojo.InvRelationTablePojo;
import org.springframework.util.CollectionUtils;

/**
 * Utility class of InvRelationDataHandler.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class InvRelationDataHandlerUtil {

    private static final int QUERYNUMLIMIT = 340;

    private InvRelationDataHandlerUtil() {

    }

    /**
     * Get Query UUID List.<br>
     * 
     * @param it String iterator
     * @param refUuidList list of UUID
     * @param relationPojo InvRelationTablePojo Object
     * @param serviceType Service type
     * @param session SqlSession
     * @return list of UUID
     * @since SDNO 0.5
     */
    public static List<String> getQueryUuidList(Iterator<String> it, List<String> refUuidList,
            InvRelationTablePojo relationPojo, String serviceType, SqlSession session) {
        List<String> result = new ArrayList<String>();
        if(it.hasNext()) {
            // avert the sql is too long
            List<List<String>> partList = InvRelationDataHandlerUtil.splitList(refUuidList, QUERYNUMLIMIT);
            String typeName = it.next();
            String[] resTypes = ValidUtil.checkRelationType(typeName);
            List<Map<String, Object>> queryResults = new ArrayList<Map<String, Object>>();
            for(List<String> part : partList) {
                relationPojo.buildServiceTypeFilter(serviceType);
                relationPojo.buildDstTypeFilter(resTypes[1]).buildUuidFilter(part);
                queryResults.addAll(relationPojo.getData(session));
            }
            result = getUuidListFromQueryResults(queryResults, relationPojo.getQueryUuidName());
        }

        if(CollectionUtils.isEmpty(result)) {
            return null;
        }

        if(it.hasNext()) {
            result = getQueryUuidList(it, result, relationPojo, serviceType, session);
        }
        return result;
    }

    /**
     * Get UUID from Query Results.<br>
     * 
     * @param queryResults query result
     * @param uuidName UUID name
     * @return list of UUID
     * @since SDNO 0.5
     */
    private static List<String> getUuidListFromQueryResults(List<Map<String, Object>> queryResults, String uuidName) {
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
     * Split list.<br>
     * 
     * @param list list object
     * @param length length of list
     * @return list split
     * @since SDNO 0.5
     */
    public static <T> List<List<T>> splitList(List<T> list, final int length) {
        List<List<T>> partList = new ArrayList<List<T>>();
        final int size = list.size();
        for(int i = 0; i < size; i += length) {
            partList.add(new ArrayList<T>(list.subList(i, Math.min(size, i + length))));
        }
        return partList;
    }
}
