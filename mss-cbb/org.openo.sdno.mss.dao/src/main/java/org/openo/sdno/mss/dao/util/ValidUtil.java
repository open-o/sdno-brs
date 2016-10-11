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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.pojo.InvCrossTablePojo;
import org.openo.sdno.mss.dao.pojo.InvRelationCombinePojo;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.openo.sdno.mss.schema.relationmodel.Relationtype;

/**
 * Parameter validate utility, <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public final class ValidUtil {

    private ValidUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Check if resource type in range. <br>
     * 
     * @param resType Resource type.
     * @since SDNO 0.5
     */
    public static void checkResType(String resType) {
        Validate.notEmpty(resType);
        Map<String, Infomodel> resTypeMap = ModelMgrUtil.getInstance().getInfoModelMap();
        if(!resTypeMap.containsKey(resType)) {
            throw new IllegalArgumentException("Resource type is not support: " + resType);
        }
    }

    /**
     * Check UUID. <br>
     * 
     * @param uuid UUID
     * @since SDNO 0.5
     */
    public static void checkUuid(String uuid) {
        Validate.notEmpty(uuid);
        if(!uuid.matches("[a-zA-Z0-9\\-\\_]{1,36}")) {
            throw new IllegalArgumentException("UUID is not support: " + uuid);
        }
    }

    /**
     * UUID batch check. <br>
     * 
     * @param uuidList UUID list.
     * @since SDNO 0.5
     */
    public static void checkUuidList(List<String> uuidList) {
        Validate.notNull(uuidList);
        for(String uuid : uuidList) {
            checkUuid(uuid);
        }
    }

    /**
     * Check attributes. <br>
     * 
     * @param resType Resource type.
     * @param attributes Attributes.
     * @param checkHiddenAttr Check hidden attribute.
     * @since SDNO 0.5
     */
    public static void checkAttributes(String resType, String attributes, boolean checkHiddenAttr) {
        checkAttributes(resType, attributes, checkHiddenAttr, false);
    }

    /**
     * Check attribute, non resType check in this method. <br>
     * 
     * @param resType Resource type.
     * @param attributes Attributes.
     * @param checkHiddenAttr True check hidden attribute.
     * @param checkRelation True check relation
     * @since SDNO 0.5
     */
    public static void checkAttributes(String resType, String attributes, boolean checkHiddenAttr,
            boolean checkRelation) {
        // If attribute is null, check all attributes of basic.
        if(StringUtils.isEmpty(attributes)) {
            return;
        }

        /*
         * attribute is *, check all attributes of basic.
         * attribute is all, check all attributes of basic and ext.
         * attribute is ext, check all attributes of ext.
         */
        String attr = attributes.trim();
        if("*".equals(attr) || "all".equals(attr) || "ext".equals(attr)) {
            return;
        }

        String[] attrArray = StringUtils.split(attr, ", ");
        checkAttributes(resType, Arrays.asList(attrArray), checkHiddenAttr, checkRelation);
    }

    /**
     * Check attributes. <br>
     * 
     * @param resType Resource type
     * @param attrArray Attribute array.
     * @param checkHiddenAttr Check hidden attribute.
     * @since SDNO 0.5
     */
    public static void checkAttributes(String resType, Collection<String> attrArray, boolean checkHiddenAttr) {
        checkAttributes(resType, attrArray, checkHiddenAttr, false);
    }

    /**
     * Check attributes, non resType check in this method. <br>
     * 
     * @param resType Resource type.
     * @param attrArray Attribute array.
     * @param checkHiddenAttr Check hidden attribute.
     * @param checkRelation Check relation.
     * @since SDNO 0.5
     */
    private static void checkAttributes(String resType, Collection<String> attrArray, boolean checkHiddenAttr,
            boolean checkRelation) {
        Validate.notEmpty(attrArray);

        Set<String> basicAttrs =
                org.openo.sdno.mss.dao.pojo.InvCrossTablePojo.getAllAttributes(resType, checkHiddenAttr).keySet();
        Set<String> allAttrs = new HashSet<String>();
        allAttrs.addAll(basicAttrs);
        // Check if there any relation table column.
        if(checkRelation) {
            Set<String> relationAttrs = InvRelationCombinePojo.getRelationModel().keySet();
            allAttrs.addAll(relationAttrs);
        }

        // If attribute array is not empty, all the attributes should in resource type range.
        for(String attr : attrArray) {
            if(!allAttrs.contains(attr)) {
                throw new IllegalArgumentException(resType + ":Attributes is not support: " + attr);
            }
        }
    }

    /**
     * Check filter condition, non resType check in this method. <br>
     * <br>
     * 
     * @param resType Resource type.
     * @param filter filter condition.
     * @since SDNO 0.5
     */
    public static void checkFilter(String resType, String filter) {
        // Filter can be empty.
        if(StringUtils.isEmpty(filter)) {
            return;
        }

        // Convert filter condition from JSON to map.
        Map<String, Object> filterMap = JsonUtil.fromJson(filter, new TypeReference<HashMap<String, Object>>() {});
        Set<String> keySet = filterMap.keySet();

        // Get all attributes
        Set<String> attrs = InvCrossTablePojo.getAllAttributes(resType, true).keySet();
        // Make sure all attributes in legal range.
        for(String attr : keySet) {
            if(!attrs.contains(attr)) {
                throw new IllegalArgumentException(resType + ":Attr in filter is not support: " + attr);
            }
        }
    }

    /**
     * Check sort parameter, non resType check in this method. <br>
     * 
     * @param resType Resource type.
     * @param attributes Attributes.
     * @param sortAttrName Sort attribute name.
     * @param refValue Reference value.
     * @since SDNO 0.5
     */
    public static void checkSort(String resType, String attributes, String sortAttrName, Object refValue) {
        // If sort is empty, no need sort.
        if(StringUtils.isEmpty(sortAttrName)) {
            if(refValue != null) {
                throw new IllegalArgumentException(
                        "Sort attribute is null but sort value is not null, please check input logic.");
            }
            return;
        }

        // Sort can only be one of attribute in basic table.
        Map<String, Datatype> map = org.openo.sdno.mss.dao.pojo.InvBasicTablePojo.getAllBasicAttributes(resType);
        if(!map.containsKey(sortAttrName)) {
            throw new IllegalArgumentException(resType + ":Sort attribute is not support: " + sortAttrName);
        }
    }

    /**
     * Check paging parameter. <br>
     * 
     * @param sortAttrName Sort parameter.
     * @param refValue Reference value.
     * @param uniqueValue Unique value.
     * @since SDNO 0.5
     */
    public static void checkSplitPage(String sortAttrName, Object refValue, Object uniqueValue) {
        // If sort required.
        if(!StringUtils.isEmpty(sortAttrName)) {
            boolean refFlag = refValue == null;
            boolean uniqueFlag = uniqueValue == null;

            if(refFlag ^ uniqueFlag) {
                throw new IllegalArgumentException("Sort value is xor with uniqueValue: refValue = " + refValue
                        + ", uniqueValue = " + uniqueValue);
            }
        }
    }

    /**
     * Check relation type. <br>
     * 
     * @param relationType Relation type.
     * @return Resource types.
     * @since SDNO 0.5
     */
    public static String[] checkRelationType(String relationType) {
        Validate.notEmpty(relationType);
        String[] resTypes = relationType.split("-");
        if(null == resTypes || resTypes.length != 2) {
            throw new IllegalArgumentException("RelationType input format is incorrect :" + relationType);
        }
        String src = resTypes[0];
        checkResType(src);
        return resTypes;
    }

    /**
     * Check if relation name defined in relation model. <br>
     * 
     * @param relationType Relation type.
     * @since SDNO 0.5
     */
    public static void checkRelationTypeDefined(String relationType) {
        Set<String> relationKeySet = ModelMgrUtil.getInstance().getRelaModelMap().keySet();
        if(!relationKeySet.contains(relationType)) {
            throw new IllegalArgumentException("RelationType is not defined in the relational model :" + relationType);
        }
    }

    /**
     * Check enumeration. <br>
     * 
     * @param relationTypeIntValue
     * @since SDNO 0.5
     */
    public static void checkRelationEnumValue(int relationTypeIntValue) {
        Collection<Integer> relationValues = PropertiesUtil.getInstance().getRELATIONTYPEVALUES().values();
        if(!relationValues.contains(Integer.valueOf(relationTypeIntValue))) {
            throw new IllegalArgumentException(
                    "Input value " + relationTypeIntValue + " is not a valid enumeration value.");
        }
    }

    /**
     * Check relation. <br>
     * 
     * @param relation Table relation.
     * @since SDNO 0.5
     */
    private static void checkRelationValue(String relation) {
        if(StringUtils.isNumeric(relation)) {
            ValidUtil.checkRelationEnumValue(Integer.parseInt(relation));
        } else {
            Relationtype.fromValue(relation);
        }
    }

    /**
     * Check property precision. <br>
     * 
     * @param length Length
     * @param scale scale
     * @return True in precision
     * @since SDNO 0.5
     */
    public static boolean checkPropertyPrecision(int length, int scale) {
        return length > 0 && length >= scale;
    }

    /**
     * Check relation. <br>
     * 
     * @param relationType Relation type.
     * @param relation relation.
     * @since SDNO 0.5
     */
    private static void checkRelationValid(String relationType, String relation) {
        Map<String, RelationModelRelation> relModelMap = ModelMgrUtil.getInstance().getRelaModelMap();
        if(!relModelMap.containsKey(relationType)) {
            throw new IllegalArgumentException("RelationType is not defined in the relational model :" + relationType);
        }

        String relationStr = "";
        if(StringUtils.isNumeric(relation)) {
            Map<String, Integer> relTypeMap = PropertiesUtil.getInstance().getRELATIONTYPEVALUES();
            int value = Integer.parseInt(relation);
            for(String key : relTypeMap.keySet()) {
                if(relTypeMap.get(key).equals(value)) {
                    relationStr = key;
                    break;
                }
            }
        } else {
            relationStr = relation;
        }

        Relationtype modelType = relModelMap.get(relationType).getType();
        if(!modelType.value().equals(relationStr)) {
            throw new IllegalArgumentException("Relation value is not defined in the relational model, relationType :"
                    + relationType + ", relation :" + relation);
        }
    }

    /**
     * Check all the relation type and value of relation, 1. if type is exist, 2. if value validate,
     * if relation value follow model define. <br>
     * 
     * @param relationType Relation type.
     * @param relation Relation
     * @since SDNO 0.5
     */
    public static void checkRelation(String relationType, String relation) {
        checkRelationType(relationType);

        checkRelationTypeDefined(relationType);

        checkRelationValue(relation);

        checkRelationValid(relationType, relation);
    }
}
