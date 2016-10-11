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

package org.openo.sdno.mss.combine.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.util.ValidUtil;
import org.openo.sdno.mss.dao.util.io.FileUtil;

/**
 * Utility class of InvDataService.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class InvDataServiceUtil {

    private InvDataServiceUtil() {

    }

    /**
     * Delete expired files.<br>
     * 
     * @since SDNO 0.5
     */
    public static void deleteOverdueFile() {
        File fileDir = FileUtil.getUserDirFile("export");
        FileUtil.deleteOverdueFile(fileDir);
    }

    /**
     * Download Data.<br>
     * 
     * @param resType resource type
     * @param attr resource attribute
     * @param filter filter
     * @return File download
     * @throws ServiceException when download failed
     * @since SDNO 0.5
     */
    public static File downloadData(String resType, String attr, String filter) throws ServiceException {
        ValidUtil.checkResType(resType);
        ValidUtil.checkAttributes(resType, attr, true);
        ValidUtil.checkFilter(resType, filter);

        final String filePath = FileUtil.createTmpFilePath("export");
        return FileUtil.getUserDirFile(filePath);
    }

    /**
     * Check Input Sort Field String.<br>
     * 
     * @param sortAttrName sort Attribute name
     * @param refValue reference value
     * @param refUnique reference unique
     * @since SDNO 0.5
     */
    public static void checkInputSortFieldStr(String sortAttrName, String refValue, String refUnique) {
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
     * Check resource fields. When add new data, if the part of it is inputed, but non empty fields
     * have no input value, throw exception.<br>
     * 
     * @param bktname Bucket name
     * @param restype resource type
     * @param info info
     * @since SDNO 0.5
     */
    public static void checkAddResourceFields(String bktname, String restype, List<Map<String, Object>> info) {
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

    /**
     * Check non empty fields.<br>
     * 
     * @param bktname Bucket name
     * @param restype resource type
     * @param info map of info
     * @since SDNO 0.5
     */
    public static void checkNonEmptyFields(String bktname, String restype, List<Map<String, Object>> info) {
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
}
