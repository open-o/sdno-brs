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

package org.openo.sdno.mss.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.service.constant.Constant;

/**
 * The class to do a parameter transformation.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ParamConverter {

    private ParamConverter() {

    }

    /**
     * Replaces the UUID field in the returned data list into the Id field.<br>
     * 
     * @param invRespEntity The returned data list
     * @since SDNO 0.5
     */
    public static void replaceEntitysUUID2ID(InvRespEntity<List<Map<String, Object>>> invRespEntity) {
        if(invRespEntity == null || invRespEntity.getData().isEmpty()) {
            return;
        }

        replaceMapsUUID2ID(invRespEntity.getData());
    }

    /**
     * Replaces the UUID field in the returned data into the Id field.<br>
     * 
     * @param invRespEntity The returned data
     * @since SDNO 0.5
     */
    public static void replaceEntityUUID2ID(InvRespEntity<Map<String, Object>> invRespEntity) {
        if(invRespEntity == null || invRespEntity.getData().isEmpty()) {
            return;
        }

        replaceMapUUID2ID(invRespEntity.getData());
    }

    /**
     * Replaces the UUID in the returned data list as Id.<br>
     * 
     * @param respData The returned data list
     * @since SDNO 0.5
     */
    public static void replaceMapsUUID2ID(List<Map<String, Object>> respData) {
        if(respData == null || respData.isEmpty()) {
            return;
        }

        for(Map<String, Object> respOne : respData) {
            replaceMapUUID2ID(respOne);
        }
    }

    /**
     * Replaces the UUID field in the property to the Id field.<br>
     * 
     * @param props The property
     * @since SDNO 0.5
     */
    public static void replaceMapUUID2ID(Map<String, Object> props) {
        if(props == null || props.isEmpty()) {
            return;
        }

        for(int i = 0, len = Constant.UUIDS.length; i < len; i++) {
            String uuid = Constant.UUIDS[i];
            Object value = props.get(uuid);
            if(value != null) {
                props.remove(uuid);
                props.put(Constant.IDS[i], value);
            }
        }
    }

    /**
     * Replace the Id in the request parameter list to UUID.<br>
     * 
     * @param reqData The request parameter list
     * @since SDNO 0.5
     */
    public static void replaceMapsID2UUID(List<Map<String, Object>> reqData) {
        if(reqData == null || reqData.isEmpty()) {
            return;
        }

        for(Map<String, Object> reqOne : reqData) {
            replaceMapID2UUID(reqOne);
        }
    }

    /**
     * Replace the Id in the request parameter to UUID.<br>
     * 
     * @param props The request parameter
     * @since SDNO 0.5
     */
    public static void replaceMapID2UUID(Map<String, Object> props) {
        if(props == null || props.isEmpty()) {
            return;
        }

        for(int i = 0, len = Constant.IDS.length; i < len; i++) {
            String id = Constant.IDS[i];
            Object value = props.get(id);
            if(value != null) {
                props.remove(id);
                props.put(Constant.UUIDS[i], value);
            }
        }
    }

    /**
     * Replace the Id in the data list to UUID.<br>
     * 
     * @param attrList The data list
     * @since SDNO 0.5
     */
    public static void replaceListID2UUID(List<String> attrList) {
        if(attrList == null || attrList.isEmpty()) {
            return;
        }

        List<String> l = new ArrayList<>();
        for(String attr : attrList) {
            int idx = findAttrByID(attr);
            if(idx > -1) {
                l.add(Constant.UUIDS[idx]);
            } else {
                l.add(attr);
            }
        }

        attrList.clear();
        attrList.addAll(l);
    }

    /**
     * Replace the Id in the data to UUID.<br>
     * 
     * @param attr The data as string
     * @return The converted data as string
     * @since SDNO 0.5
     */
    public static String replaceID2UUID(String attr) {
        int idx = findAttrByID(attr);
        if(idx > -1) {
            return Constant.UUIDS[idx];
        }

        return attr;
    }

    private static int findAttrByID(String attr) {
        for(int i = 0, len = Constant.IDS.length; i < len; i++) {
            if(Constant.IDS[i].equals(attr)) {
                return i;
            }
        }
        return -1;
    }

}
