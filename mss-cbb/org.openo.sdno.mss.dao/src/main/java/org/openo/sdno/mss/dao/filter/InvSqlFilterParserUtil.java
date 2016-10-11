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
import org.openo.sdno.mss.dao.util.SQLUtil;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * Utility class of InvSqlFilterParser.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class InvSqlFilterParserUtil {

    private InvSqlFilterParserUtil() {

    }

    /**
     * Check JSON Value.<br>
     * 
     * @param field field name
     * @param jsonFieldValue field value
     * @param dataType data type
     * @since SDNO 0.5
     */
    public static void checkJsonValue(String field, Object jsonFieldValue, Datatype dataType) {
        if(!(jsonFieldValue instanceof List)) {
            throw new IllegalArgumentException(
                    "Json value is not a list. Json value:" + jsonFieldValue + ", Field:" + field);
        }

        @SuppressWarnings("unchecked")
        List<Object> fieldValueList = (List<Object>)jsonFieldValue;
        if(fieldValueList.isEmpty()) {
            throw new IllegalArgumentException("Json value is empty." + ", Field:" + field);
        }

        checkDataType(dataType, fieldValueList.get(0));
    }

    /**
     * Check JSON Field.<br>
     * 
     * @param jsonFieldPairs Pair of field value and name
     * @param fieldNames list of field name
     * @return field name filtered
     * @since SDNO 0.5
     */
    public static List<String> validJsonField(Map<String, Object> jsonFieldPairs, List<String> fieldNames) {
        List<String> jsonValidFields = new ArrayList<String>();
        for(String field : fieldNames) {
            if(jsonFieldPairs.containsKey(field)) {
                jsonValidFields.add(field);
            }
        }
        return jsonValidFields;
    }

    /**
     * Get SQL by field List.<br>
     * 
     * @param fieldValueList field value list
     * @param dataType Data Type
     * @return SQL returned
     * @since SDNO 0.5
     */
    public static String replaceInValue(List<Object> fieldValueList, Datatype dataType) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0, size = fieldValueList.size(); i < size; i++) {
            Object value = fieldValueList.get(i);

            String escapeValue = value.toString();
            escapeValue = SQLUtil.escapeSqlSlashAndQuotes(escapeValue);

            switch(dataType) {
                case STRING:
                    sb.append('\'' + escapeValue + "',");
                    break;
                case INTEGER:
                case DECIMAL:
                case DATETIME:
                case FLOAT:
                case DOUBLE:
                    sb.append(escapeValue).append(',');
                    break;
                default:
                    break;
            }
        }

        if(sb.length() >= 1) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return "(" + sb.toString() + ")";
    }

    /**
     * Join table name fields.<br>
     * 
     * @param allWhereFilter where filter pattern
     * @param resType resource type
     * @return filter SQL
     * @since SDNO 0.5
     */
    public static String joinTableNameField(String allWhereFilter, String resType) {
        if(StringUtils.isEmpty(allWhereFilter)) {
            return allWhereFilter;
        }

        StringBuilder sb = new StringBuilder(allWhereFilter);
        String newAllWhereFilter = sb.toString();

        String tableName = PropertiesUtil.getInstance().getINVTABLEPREFIX() + resType + ".";
        if(newAllWhereFilter.startsWith("uuid in ")) {
            newAllWhereFilter = tableName + newAllWhereFilter;
        } else if(newAllWhereFilter.indexOf(" uuid in ") != -1) {
            newAllWhereFilter = StringUtils.replaceOnce(newAllWhereFilter, " uuid in ", " " + tableName + "uuid in ");
        }

        return newAllWhereFilter;
    }

    /**
     * Check whether is valid DataType.<br>
     * 
     * @param dataType data type
     * @param value input Object
     * @since SDNO 0.5
     */
    public static void checkDataType(Datatype dataType, Object value) {
        boolean isValid = true;
        switch(dataType) {
            case STRING:
                isValid = value.getClass().equals(String.class);
                break;
            case INTEGER:
                isValid = value.getClass().equals(Integer.class);
                break;
            case DECIMAL:
                break;
            case DATETIME:
                break;
            case FLOAT:
                break;
            case DOUBLE:
                isValid = value.getClass().equals(Double.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown data type: " + value.getClass());
        }

        if(!isValid) {
            throw new IllegalArgumentException(
                    "Json value type doesn't match with field data type." + ", Field data type:" + dataType);
        }
    }
}
