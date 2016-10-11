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

package org.openo.sdno.mss.dao.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.dao.pojo.InvBasicTablePojo;
import org.openo.sdno.mss.dao.pojo.InvTempAttrFilterPojo;
import org.openo.sdno.mss.dao.util.SQLUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inventory SQL Filter Parser Class.<br>
 * <p>
 * This class provide functions to build InvAttrSqlFilter.
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class InvSqlFilterParser {

    /**
     * Logger handler
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InvSqlFilterParser.class);

    /**
     * and Relation operator
     */
    private static final String AND_RELATION = " and ";

    /**
     * or Relation operator
     */
    private static final String OR_RELATION = " or ";

    /**
     * in Relation operator
     */
    private static final String IN_RELATION = " in ";

    /**
     * like Relation operator
     */
    private static final String LIKE_RELATION = " like ";

    /**
     * equal Relation operator
     */
    private static final String EQAUL_SPERATOR = "=";

    /**
     * session
     */
    private final SqlSession session;

    /**
     * AttrFilter list
     */
    private final List<InvTempAttrFilterPojo> pojoList = new ArrayList<InvTempAttrFilterPojo>();

    /**
     * max number of Fields
     */
    private static final int LONG_VALUE_NUM = 30;

    /**
     * Resource type
     */
    private final String resType;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param session SQL Session
     * @param resType resource type
     */
    public InvSqlFilterParser(SqlSession session, String resType) {
        this.resType = resType;
        this.session = session;
    }

    /**
     * Build SQL Filter.<br>
     * 
     * @param allWhereFilter where filter pattern
     * @param jsonStr Filter Object
     * @return InvAttrSqlFilter Object
     * @since SDNO 0.5
     */
    public InvAttrSqlFilter buildSqlFilter(String allWhereFilter, String jsonStr) {
        Validate.notEmpty(allWhereFilter);
        Validate.notEmpty(jsonStr);
        LOGGER.info("Start to process sql filter. All filter:" + allWhereFilter + " sizeOf Json value: "
                + jsonStr.length());
        Map<String, Object> jsonFieldValueMap = JsonUtil.fromJson(jsonStr, new TypeReference<Map<String, Object>>() {});
        List<String> fieldNames = parseAllField(allWhereFilter);
        if(fieldNames.isEmpty()) {
            throw new IllegalArgumentException("Empty field. All filter:" + allWhereFilter);
        }

        List<String> jsonValidFields = InvSqlFilterParserUtil.validJsonField(jsonFieldValueMap, fieldNames);
        if(jsonValidFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "Json value is invalid.Json value:" + jsonStr + ", All filter:" + allWhereFilter);
        }

        StringBuilder sb = new StringBuilder(allWhereFilter);
        String newAllWhereFilter = sb.toString();
        for(String field : fieldNames) {
            if(!jsonValidFields.contains(field)) {

                String fieldRemoveStr = findRemoveField(newAllWhereFilter, field);
                newAllWhereFilter = StringUtils.replace(newAllWhereFilter, fieldRemoveStr, "");
            }
        }

        int currentIndex = 0;
        for(String field : fieldNames) {
            if(jsonValidFields.contains(field)) {

                Object jsonFieldValue = jsonFieldValueMap.get(field);
                FilterReplaceReturn filterReplaceRet =
                        replaceFieldValue(newAllWhereFilter, field, jsonFieldValue, currentIndex);
                newAllWhereFilter = filterReplaceRet.getAllWhereFilter();
                currentIndex = filterReplaceRet.getNextBeginIndex();
            }
        }

        newAllWhereFilter = InvSqlFilterParserUtil.joinTableNameField(newAllWhereFilter, resType);
        InvAttrSqlFilter attrSqlFilter = new InvAttrSqlFilter();
        attrSqlFilter.setWhereFilter(newAllWhereFilter);
        attrSqlFilter.setPojo(pojoList);
        return attrSqlFilter;
    }

    private FilterReplaceReturn replaceFieldValue(String inputFilter, String field, Object jsonFieldValue,
            int startIndex) {
        String outputFilter = inputFilter;
        String subStr = outputFilter.substring(startIndex);
        int endIndex = startIndex;
        if(subStr.contains(AND_RELATION)) {
            endIndex = startIndex + subStr.indexOf(AND_RELATION);
        } else {
            endIndex = startIndex + subStr.length();
        }

        String fieldPairStr = outputFilter.substring(startIndex, endIndex);
        String fieldValueStyle;
        Datatype dataType = InvBasicTablePojo.getAllBasicAttributes(resType).get(field);

        if(fieldPairStr.contains(OR_RELATION)) {
            outputFilter = processOrExpression(outputFilter, field, fieldPairStr, jsonFieldValue, dataType);
        } else if(fieldPairStr.contains(IN_RELATION)) {
            outputFilter = processInExpression(outputFilter, field, fieldPairStr, jsonFieldValue, dataType);
        } else if(fieldPairStr.contains(LIKE_RELATION)) {

            if(!(jsonFieldValue instanceof String)) {
                throw new IllegalArgumentException("Json value type is not string in like expresionn.Json value type:"
                        + jsonFieldValue.getClass());
            }
            InvSqlFilterParserUtil.checkDataType(dataType, jsonFieldValue);
            outputFilter = processLikeExpression(outputFilter, field, fieldPairStr, jsonFieldValue);
        } else if(fieldPairStr.contains(EQAUL_SPERATOR)) {
            InvSqlFilterParserUtil.checkDataType(dataType, jsonFieldValue);
            fieldValueStyle = fieldPairStr.split(EQAUL_SPERATOR)[1].trim();

            String jsonValue = jsonFieldValue.toString();

            jsonValue = SQLUtil.escapeSqlSlashAndQuotes(jsonValue);
            if(dataType == Datatype.STRING) {
                jsonValue = "'" + jsonValue + "'";
            }
            outputFilter = StringUtils.replace(outputFilter, fieldValueStyle, jsonValue);
        }

        return new FilterReplaceReturn(outputFilter, getNextBeginIndexFromReplacedFilter(outputFilter, startIndex));
    }

    private int getNextBeginIndexFromReplacedFilter(String filter, int startIndex) {
        int nextBeginIndex = startIndex;
        String leftStr = filter.substring(startIndex);
        if(leftStr.contains(AND_RELATION)) {
            nextBeginIndex = startIndex + leftStr.indexOf(AND_RELATION) + AND_RELATION.length();
        } else {
            nextBeginIndex = startIndex + leftStr.length();
        }

        return nextBeginIndex;
    }

    private String processLikeExpression(String sqlFilter, String field, String inputLikeExp, Object jsonFieldValue) {
        String fieldValueStyle = inputLikeExp.split(LIKE_RELATION)[1].trim();
        String likeField = ":" + field;

        String jsonFieldValueStr = jsonFieldValue.toString();

        jsonFieldValueStr = SQLUtil.escapeBackSlash(jsonFieldValueStr);
        jsonFieldValueStr = SQLUtil.escapeSqlSpecialAllChars(jsonFieldValueStr);
        String replaceValue = StringUtils.replace(fieldValueStyle, likeField, jsonFieldValueStr);
        return StringUtils.replace(sqlFilter, fieldValueStyle, replaceValue);
    }

    private String processOrExpression(String sqlFilter, String field, String inputOrExp, Object jsonFieldValue,
            Datatype dataType) {
        InvSqlFilterParserUtil.checkJsonValue(field, jsonFieldValue, dataType);

        @SuppressWarnings("unchecked")
        List<Object> fieldValueList = (List<Object>)jsonFieldValue;
        String replaceOrExp = inputOrExp;
        String[] fieldArry = replaceOrExp.split(OR_RELATION);
        int fieldNum = fieldArry.length;

        if(fieldValueList.size() > fieldArry.length) {
            throw new IllegalArgumentException(
                    "Json value size is out of bounds. Json value:" + jsonFieldValue + ", Filter:" + inputOrExp);
        }

        if(fieldValueList.size() < fieldArry.length) {
            int difNum = fieldArry.length - fieldValueList.size();
            for(int i = 0; i < difNum; i++) {
                int end = replaceOrExp.indexOf(OR_RELATION) + OR_RELATION.length();
                String removeFieldStr = replaceOrExp.substring(0, end);
                replaceOrExp = StringUtils.replace(replaceOrExp, removeFieldStr, "");
            }
            fieldNum = fieldValueList.size();
        }

        for(int i = 0; i < fieldNum; i++) {
            int start = replaceOrExp.indexOf(":" + field);
            String subInStr = replaceOrExp.substring(start);
            int end = subInStr.indexOf(OR_RELATION) + start;
            if(!subInStr.contains(OR_RELATION)) {
                end = subInStr.length() + start - 1;
            }
            String replaceStr = replaceOrExp.substring(start, end);

            String prepareSql = fieldValueList.get(i).toString();
            prepareSql = SQLUtil.escapeSqlSlashAndQuotes(prepareSql);

            if(dataType == Datatype.STRING) {
                prepareSql = "'" + prepareSql + "'";
            }
            replaceOrExp = StringUtils.replace(replaceOrExp, replaceStr, prepareSql);
        }

        return StringUtils.replace(sqlFilter, inputOrExp, replaceOrExp);
    }

    private String processInExpression(String sqlFilter, String field, String fieldPairStr, Object jsonFieldValue,
            Datatype dataType) {
        InvSqlFilterParserUtil.checkJsonValue(field, jsonFieldValue, dataType);

        @SuppressWarnings("unchecked")
        List<Object> fieldValueList = (List<Object>)jsonFieldValue;
        String outputFilter = sqlFilter;

        if(fieldValueList.size() > LONG_VALUE_NUM) {

            int start = outputFilter.indexOf(field);
            String subStr = outputFilter.substring(start);

            int end = start + subStr.indexOf(AND_RELATION) + AND_RELATION.length();
            if(!subStr.contains(AND_RELATION)) {
                end = subStr.length() + start;
            }

            String removeStr = outputFilter.substring(start, end);
            outputFilter = StringUtils.replace(outputFilter, removeStr, "");

            InvTempAttrFilterPojo tmpAttrFilterPojo = new InvTempAttrFilterPojo(field, resType);
            LOGGER.info("Create temporary table. Table name:" + tmpAttrFilterPojo.getTableName());
            tmpAttrFilterPojo.createTmpAttrTable(session);

            try {
                tmpAttrFilterPojo.batchAdd(session, fieldValueList);
                pojoList.add(tmpAttrFilterPojo);
            } catch(RuntimeException exc) {
                tmpAttrFilterPojo.removeTable(session);
                LOGGER.error("Fail to insert value into temp table. Table:" + tmpAttrFilterPojo.getTableName());
                throw exc;
            }
            LOGGER.info("Create temporary table end. Table name:" + tmpAttrFilterPojo.getTableName());
        } else {
            String jsonListValueStr = jsonFieldValue.toString();
            jsonListValueStr = jsonListValueStr.substring(1, jsonListValueStr.length() - 1);
            jsonListValueStr = "(" + jsonListValueStr + ")";

            String inExpession = fieldPairStr.split(IN_RELATION)[1].trim();

            String prepareStr = InvSqlFilterParserUtil.replaceInValue(fieldValueList, dataType);
            outputFilter = StringUtils.replace(outputFilter, inExpession, prepareStr);
        }

        return outputFilter;
    }

    private String findRemoveField(String sqlFilter, String field) {
        String removeFieldStr = "";
        int startIndex = sqlFilter.indexOf(field);
        String subStr = sqlFilter.substring(startIndex);
        int endIndex = startIndex;

        if(subStr.contains(AND_RELATION)) {
            endIndex = subStr.indexOf(AND_RELATION) + AND_RELATION.length() + startIndex;

            removeFieldStr = sqlFilter.substring(startIndex, endIndex);
            if(removeFieldStr.contains(OR_RELATION)) {
                startIndex = startIndex - 1;
            }
        } else {

            endIndex = subStr.length() + startIndex;
            if(startIndex > AND_RELATION.length()) {
                startIndex = startIndex - AND_RELATION.length();
            } else {
                startIndex = 0;
            }
        }

        removeFieldStr = sqlFilter.substring(startIndex, endIndex);
        return removeFieldStr;
    }

    private List<String> parseAllField(String allFieldFilter) {
        List<String> fieldNameList = new ArrayList<String>();

        String[] fieldPairArrs = allFieldFilter.split(AND_RELATION);
        for(String fieldPair : fieldPairArrs) {
            String fieldName = "";
            if(fieldPair.contains(IN_RELATION)) {
                fieldName = fieldPair.split(IN_RELATION)[0].trim();
            } else if(fieldPair.contains(OR_RELATION)) {

                fieldPair = fieldPair.substring(1, fieldPair.length() - 1);
                fieldName = fieldPair.split(EQAUL_SPERATOR)[0].trim();
            } else if(fieldPair.contains(LIKE_RELATION)) {
                fieldName = fieldPair.split(LIKE_RELATION)[0].trim();
            } else if(fieldPair.contains(EQAUL_SPERATOR)) {
                fieldName = fieldPair.split(EQAUL_SPERATOR)[0].trim();
            }

            fieldNameList.add(fieldName);
        }

        return fieldNameList;
    }
}
