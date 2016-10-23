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

package org.openo.sdno.mss.combine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.combine.intf.InvDataService;
import org.openo.sdno.mss.combine.util.InvDataServiceUtil;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.intf.InvDataHandler;
import org.openo.sdno.mss.dao.model.QueryParamModel;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;
import org.openo.sdno.mss.dao.util.ValidUtil;

/**
 * Abstract inventory data services, including the add, delete, modify operation of resource
 * data.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-26
 */
public class InvDataServiceImpl implements InvDataService {

    private static final String QUERY_RELATION_DATA = "queryRelationData";

    private static final String QUERY_RELATION_DATA_COUNT = "queryRelationDataCount";

    /**
     * Inventory DAO service, spring injection.
     */
    private InvDataHandler dataHandler;

    public InvDataServiceImpl() {
        // Constructor.
    }

    public void setDataHandler(InvDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(String bktName, final String resType, final String uuid,
            final String attr) throws ServiceException {
        DataSourceCtrler.add(bktName);

        try {
            InvRespEntity<List<Map<String, Object>>> res = dataHandler.get(resType, uuid, attr);

            return res;
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<Boolean> delete(String bktName, String resType, String uuid) {
        DataSourceCtrler.add(bktName);
        try {
            return dataHandler.delete(resType, uuid);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<Boolean> batchDelete(String bktName, String resType, List<String> uuidList) {
        DataSourceCtrler.add(bktName);
        try {
            return dataHandler.batchDelete(resType, uuidList);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<Map<String, Object>> update(String bktName, String resType, String uuid,
            Map<String, Object> value) {
        DataSourceCtrler.add(bktName);
        try {
            List<Map<String, Object>> infoBody = new ArrayList<Map<String, Object>>();

            // Non empty field check, match batch update interface parameters.
            infoBody.add(value);
            InvDataServiceUtil.checkNonEmptyFields(bktName, resType, infoBody);
            return dataHandler.update(resType, uuid, value);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> add(String bktName, String resType,
            List<Map<String, Object>> values) {
        DataSourceCtrler.add(bktName);
        try {
            InvDataServiceUtil.checkNonEmptyFields(bktName, resType, values);
            InvDataServiceUtil.checkAddResourceFields(bktName, resType, values);
            return dataHandler.batchAdd(resType, values);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> batchUpdate(String bktName, String resType,
            List<Map<String, Object>> values) {
        DataSourceCtrler.add(bktName);
        try {
            InvDataServiceUtil.checkNonEmptyFields(bktName, resType, values);
            return dataHandler.batchUpdate(resType, values);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public Object commQueryGet(String bktName, String resType, String filterDsc, QueryParamModel queryParam) {
        String attr = queryParam.getFields();
        String joinAttr = queryParam.getJoinAttr();
        String sort = queryParam.getSort();

        DataSourceCtrler.add(bktName);
        try {
            if(StringUtils.isEmpty(attr)) {
                throw new IllegalArgumentException("attr can not be empty");
            }

            ValidUtil.checkResType(resType);

            List<String> attrsList = JsonUtil.fromJson(attr, new TypeReference<List<String>>() {});

            // Parse joinAttr
            List<HashMap<String, Object>> joinAttrList = null;
            if(!StringUtils.isEmpty(joinAttr)) {
                joinAttrList = JsonUtil.fromJson(joinAttr, new TypeReference<List<HashMap<String, Object>>>() {});
            }
            // Parse sort field
            List<String> sortList = null;
            if(!StringUtils.isEmpty(sort)) {
                sortList = JsonUtil.fromJson(sort, new TypeReference<List<String>>() {});
            }

            return dataHandler.commQueryGet(resType, attrsList, joinAttrList, filterDsc, sortList, queryParam);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public List<Object> commQueryGetCount(String bktName, String resType, String joinAttr, String filterDsc,
            String filterData) {
        DataSourceCtrler.add(bktName);
        try {
            ValidUtil.checkResType(resType);

            // Parse joinAttr
            List<HashMap<String, Object>> joinAttrList = null;
            if(!StringUtils.isEmpty(joinAttr)) {
                joinAttrList = JsonUtil.fromJson(joinAttr, new TypeReference<List<HashMap<String, Object>>>() {});
            }

            return dataHandler.commQueryGetCount(resType, joinAttrList, filterDsc, filterData);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public List<Object> queryRelationDataCount(String bktName, String resType, String attr, String filter, String sort,
            String pageNum, String pageSize) {
        QueryParamModel queryParam = new QueryParamModel("", attr, filter, sort, pageSize, pageNum);
        return queryRelationData(bktName, resType, QUERY_RELATION_DATA_COUNT, queryParam);
    }

    @Override
    public Object queryRelationData(String bktName, String resType, String attr, String filter, String sort,
            String pageNum, String pageSize) {
        QueryParamModel queryParam = new QueryParamModel("", attr, filter, sort, pageSize, pageNum);

        List<Object> objectList = queryRelationData(bktName, resType, QUERY_RELATION_DATA, queryParam);
        return objectList.get(0);
    }

    private List<Object> queryRelationData(String bktName, String resType, String type, QueryParamModel queryParam) {
        String attr = queryParam.getJoinAttr();
        String filter = queryParam.getFilter();
        String sort = queryParam.getSort();
        String pageNum = queryParam.getPageNum();
        String pageSize = queryParam.getPageSize();

        DataSourceCtrler.add(bktName);
        try {
            if(StringUtils.isEmpty(attr)) {
                throw new IllegalArgumentException("attr can not be empty");
            }

            ValidUtil.checkResType(resType);

            List<String> attrsList = JsonUtil.fromJson(attr, new TypeReference<List<String>>() {});

            // Parse sort field
            List<String> sortList = null;
            if(!StringUtils.isEmpty(sort)) {
                sortList = JsonUtil.fromJson(sort, new TypeReference<List<String>>() {});
            }

            if(QUERY_RELATION_DATA_COUNT.equals(type)) {
                return dataHandler.queryRelationDataCount(resType, attrsList, filter, sortList, pageNum, pageSize);
            } else {
                Object object = dataHandler.queryRelationData(resType, attrsList, filter, sortList, pageNum, pageSize);
                List<Object> objectList = new ArrayList<Object>();
                objectList.add(object);
                return objectList;
            }
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public Boolean exist(String bktName, String resType, String attrName, Object attrVal) throws ServiceException {
        DataSourceCtrler.add(bktName);

        try {
            ValidUtil.checkResType(resType);
            return dataHandler.exist(resType, attrName, attrVal);
        } finally {
            DataSourceCtrler.remove();
        }
    }
}
