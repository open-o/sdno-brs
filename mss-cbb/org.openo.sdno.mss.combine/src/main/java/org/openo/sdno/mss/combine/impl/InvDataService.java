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

package org.openo.sdno.mss.combine.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.combine.entities.ErrorCode;
import org.openo.sdno.mss.combine.intf.IInvDataService;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.intf.IInvDataHandler;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;
import org.openo.sdno.mss.dao.pojo.InvTypeConvertor;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.dao.util.io.FileUtil;

/**
 * Abstract inventory data services, including the add, delete, modify operation of resource
 * data.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-26
 */
public class InvDataService implements IInvDataService {

    private static final String QUERY_RELATION_DATA = "queryRelationData";

    private static final String QUERY_RELATION_DATA_COUNT = "queryRelationDataCount";

    /**
     * Inventory DAO service, spring injection.
     */
    private IInvDataHandler dataHandler;

    public InvDataService() {
        // Constructor.
    }

    public void setDataHandler(IInvDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(String bktName, final String resType, final String uuid,
            final String attr) throws ServiceException {
        DataSourceCtrler.add(bktName);

        try {
            InvRespEntity<List<Map<String, Object>>> res = dataHandler.get(resType, uuid, attr);

            if(res.getData().isEmpty()) {
                throw new ServiceException(ErrorCode.INTERNAL_ERROR, 404,
                        new ExceptionArgs(new String[] {"UUID not Exist"}, null, null, null));
            }

            return res;
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public Object batchGet(String bktName, final String acceptHeader, final String queryType, final String resType,
            final String attr, final String filter, final String filterEx, final String sortAttrName,
            final String sortType, final String refValue, final String refUnique) throws ServiceException {
        DataSourceCtrler.add(bktName);

        try {
            if(StringUtils.contains(acceptHeader, "text/csv")) {
                File zipFile = downloadData(resType, attr, filter);
                deleteOverdueFile();
                return zipFile;
            }

            // Query all, including basic and extension attributes, non paging.
            if("all".equalsIgnoreCase(queryType)) {
                return dataHandler.batchGet(resType, attr, filter, filterEx);
            }

            checkInputSortFieldStr(sortAttrName, refValue, refUnique);
            ValidUtil.checkResType(resType);
            ValidUtil.checkSort(resType, attr, sortAttrName, refValue);
            ValidUtil.checkSplitPage(sortAttrName, refValue, refUnique);

            // refValue to be converted to a real type.
            Object sortAttrValue = StringUtils.isEmpty(sortAttrName) ? null
                    : InvTypeConvertor.getInstance().convert(resType, sortAttrName, refValue);

            // Paging query, if there is extension attributes, filter it out.
            boolean isAsc = !"desc".equalsIgnoreCase(sortType);

            return dataHandler.getSplitPage(resType, attr, filter, filterEx, sortAttrName, isAsc, sortAttrValue,
                    refUnique);

        } finally {
            DataSourceCtrler.remove();
        }
    }

    private void checkInputSortFieldStr(String sortAttrName, String refValue, String refUnique) {
        // Sort field is empty, does not sort.
        if(StringUtils.isEmpty(sortAttrName)) {
            if(!StringUtils.isEmpty(refValue)) {
                throw new IllegalArgumentException(
                        "Sort attribute is null but sort value is not null, please check input logic.");
            }
        } else {
            boolean refFlag = StringUtils.isEmpty(refValue);
            boolean uniqueFlag = StringUtils.isEmpty(refUnique);

            // If the sort field is different from the unique index field, at least miss one field,
            // which led to can't turn the page normally.
            // If refValue and uniqueFlag are null, it means to query first page.
            // If refValue and uniqueFlag are not null, it means to turn the page.
            if(refFlag ^ uniqueFlag) {
                throw new IllegalArgumentException(
                        "Sort value is xor with uniqueValue: refValue = " + refValue + ", refUnique = " + refUnique);
            }
        }
    }

    /**
     * Delete expired files.<br/>
     * 
     * @since SDNO 0.5
     */
    private void deleteOverdueFile() {
        File fileDir = FileUtil.getUserDirFile("export");
        FileUtil.deleteOverdueFile(fileDir);
    }

    /**
     * Download data to the file.<br/>
     */
    private File downloadData(String resType, String attr, String filter) throws ServiceException {
        ValidUtil.checkResType(resType);
        ValidUtil.checkAttributes(resType, attr, true);
        ValidUtil.checkFilter(resType, filter);

        final String filePath = FileUtil.createTmpFilePath("export");
        return FileUtil.getUserDirFile(filePath);

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
            checkNonEmptyFields(bktName, resType, infoBody);
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
            checkNonEmptyFields(bktName, resType, values);
            checkAddResourceFields(bktName, resType, values);
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
            checkNonEmptyFields(bktName, resType, values);
            return dataHandler.batchUpdate(resType, values);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public Object commQueryGet(String bktName, String resType, String attr, String joinAttr, String filterDsc,
            String filterData, String sort, String pageNumber, String pageCapacity) {
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

            return dataHandler.commQueryGet(resType, attrsList, joinAttrList, filterDsc, filterData, sortList,
                    pageNumber, pageCapacity);
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
        return queryRelationData(bktName, resType, attr, filter, sort, pageNum, pageSize, QUERY_RELATION_DATA_COUNT);
    }

    @Override
    public Object queryRelationData(String bktName, String resType, String attr, String filter, String sort,
            String pageNum, String pageSize) {
        List<Object> objectList =
                queryRelationData(bktName, resType, attr, filter, sort, pageNum, pageSize, QUERY_RELATION_DATA);
        return objectList.get(0);
    }

    private List<Object> queryRelationData(String bktName, String resType, String attr, String filter, String sort,
            String pageNum, String pageSize, String type) {
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

    /**
     * Check non empty fields.<br/>
     * 
     * @param bktname Bucket name
     * @param restype resource type
     * @param info map of info
     * @since SDNO 0.5
     */
    public void checkNonEmptyFields(String bktname, String restype, List<Map<String, Object>> info) {
        // Get non empty field information.
        Map<String, String> dataFields = ModelMgrUtil.getInstance().getNonEmptyValuesInfoModelMap();

        if(dataFields != null && !dataFields.isEmpty()) {

            // When update or add, if non empty field's value is empty, throw exception.
            for(Map<String, Object> infobody : info) {
                for(Map.Entry<String, Object> updateInfo : infobody.entrySet()) {
                    // In the dictionary, find out the field is defined to be not empty, check
                    // whether the input value is not empty.
                    String emptyFlag = dataFields.get(bktname + "-" + restype + "-" + updateInfo.getKey());
                    Object value = updateInfo.getValue();
                    if((emptyFlag != null && !emptyFlag.isEmpty()) && (null == value || "".equals(value))) {
                        throw new IllegalArgumentException(updateInfo.getKey() + " value connot be empty");
                    }
                }
            }
        }
    }

    /**
     * Check resource fields. When add new data, if the part of it is inputed, but non empty fields
     * have no input value, throw exception.<br/>
     * 
     * @param bktname Bucket name
     * @param restype resource type
     * @param info info
     * @since SDNO 0.5
     */
    public void checkAddResourceFields(String bktname, String restype, List<Map<String, Object>> info) {
        Map<String, List<String>> dataFields = ModelMgrUtil.getInstance().getNonEmptyPropertyInfoModelMap();
        List<String> nameList = dataFields.get(bktname + "-" + restype);
        if(nameList != null && !nameList.isEmpty()) {
            for(String name : nameList) {
                for(Map<String, Object> infoBody : info) {
                    Object keyValue = infoBody.get(name);
                    if(null == keyValue || "".equals(keyValue)) {
                        throw new IllegalArgumentException("the field " + name + " connot be empty");
                    }
                }
            }
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
