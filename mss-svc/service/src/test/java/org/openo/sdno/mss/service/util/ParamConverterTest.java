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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.service.constant.Constant;

/**
 * ParamConverter test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ParamConverterTest {

    @Test
    public void testReplaceEntitysUUID2IDEmpty() {
        InvRespEntity<List<Map<String, Object>>> invRespEntity = new InvRespEntity<List<Map<String, Object>>>();
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        invRespEntity.buildData(uuidMapList);
        ParamConverter.replaceEntitysUUID2ID(invRespEntity);
    }

    @Test
    public void testReplaceEntitysUUID2IDNull() {
        ParamConverter.replaceEntitysUUID2ID(null);
    }

    @Test
    public void testReplaceEntitysUUID2ID() {
        InvRespEntity<List<Map<String, Object>>> invRespEntity = new InvRespEntity<List<Map<String, Object>>>();
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        uuidMapList.add(uuidMap);
        invRespEntity.buildData(uuidMapList);
        InvRespEntity<List<Map<String, Object>>> invRespEntityResult = invRespEntity;
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        idMapList.add(idMap);
        invRespEntityResult.buildData(idMapList);
        ParamConverter.replaceEntitysUUID2ID(invRespEntity);
        assertTrue(invRespEntityResult.equals(invRespEntity));
    }

    @Test
    public void testReplaceEntitysUUID2IDNotUUID() {
        InvRespEntity<List<Map<String, Object>>> invRespEntity = new InvRespEntity<List<Map<String, Object>>>();
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap("not-uuid");
        uuidMapList.add(uuidMap);
        invRespEntity.buildData(uuidMapList);
        InvRespEntity<List<Map<String, Object>>> invRespEntityResult = invRespEntity;
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap("not-uuid");
        idMapList.add(idMap);
        invRespEntityResult.buildData(idMapList);
        ParamConverter.replaceEntitysUUID2ID(invRespEntity);
        assertTrue(invRespEntityResult.equals(invRespEntity));
    }

    @Test
    public void testReplaceEntityUUID2IDEmpty() {
        InvRespEntity<Map<String, Object>> invRespEntity = new InvRespEntity<Map<String, Object>>();
        Map<String, Object> uuidMap = new HashMap<String, Object>();
        invRespEntity.buildData(uuidMap);
        ParamConverter.replaceEntityUUID2ID(invRespEntity);
    }

    @Test
    public void testReplaceEntityUUID2IDNull() {
        ParamConverter.replaceEntityUUID2ID(null);
    }

    @Test
    public void testReplaceEntityUUID2ID() {
        InvRespEntity<Map<String, Object>> invRespEntity = new InvRespEntity<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        ;
        invRespEntity.buildData(uuidMap);
        InvRespEntity<Map<String, Object>> invRespEntityResult = invRespEntity;
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        invRespEntityResult.buildData(idMap);
        ParamConverter.replaceEntityUUID2ID(invRespEntity);
        assertTrue(invRespEntityResult.equals(invRespEntity));
    }

    @Test
    public void testReplaceEntityUUID2IDNotUUID() {
        InvRespEntity<Map<String, Object>> invRespEntity = new InvRespEntity<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap("not-uuid");
        ;
        invRespEntity.buildData(uuidMap);
        InvRespEntity<Map<String, Object>> invRespEntityResult = invRespEntity;
        Map<String, Object> idMap = creatIDMap("not-uuid");
        invRespEntityResult.buildData(idMap);
        ParamConverter.replaceEntityUUID2ID(invRespEntityResult);
        assertTrue(invRespEntityResult.equals(invRespEntity));
    }

    @Test
    public void testReplaceMapsUUID2IDEmpty() {
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        ParamConverter.replaceMapsUUID2ID(uuidMapList);
    }

    @Test
    public void testReplaceMapsUUID2IDNull() {
        ParamConverter.replaceMapsUUID2ID(null);
    }

    @Test
    public void testReplaceMapsUUID2ID() {
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        ;
        uuidMapList.add(uuidMap);
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        idMapList.add(idMap);
        ParamConverter.replaceMapsUUID2ID(uuidMapList);
        assertTrue(idMapList.equals(uuidMapList));
    }

    @Test
    public void testReplaceMapsUUID2IDNotUUID() {
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap("not-uuid");
        ;
        uuidMapList.add(uuidMap);
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap("not-uuid");
        idMapList.add(idMap);
        ParamConverter.replaceMapsUUID2ID(uuidMapList);
        assertTrue(idMapList.equals(uuidMapList));
    }

    @Test
    public void testReplaceMapUUID2IDEmpty() {
        Map<String, Object> uuidMap = new HashMap<String, Object>();
        ParamConverter.replaceMapUUID2ID(uuidMap);
    }

    @Test
    public void testReplaceMapUUID2IDNull() {
        ParamConverter.replaceMapUUID2ID(null);
    }

    @Test
    public void testReplaceMapUUID2ID() {
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        ;
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        ParamConverter.replaceMapUUID2ID(uuidMap);
        assertTrue(idMap.equals(uuidMap));
    }

    @Test
    public void testReplaceMapUUID2IDNotUUID() {
        Map<String, Object> uuidMap = creatUUIDMap("not-uuid");
        ;
        Map<String, Object> idMap = creatIDMap("not-uuid");
        ParamConverter.replaceMapUUID2ID(uuidMap);
        assertTrue(idMap.equals(uuidMap));
    }

    @Test
    public void testReplaceMapsID2UUIDEmpty() {
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        ParamConverter.replaceMapsID2UUID(idMapList);
    }

    @Test
    public void testReplaceMapsID2UUIDNull() {
        ParamConverter.replaceMapsID2UUID(null);
    }

    @Test
    public void testReplaceMapsID2UUID() {
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        idMapList.add(idMap);
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        ;
        uuidMapList.add(uuidMap);
        ParamConverter.replaceMapsID2UUID(idMapList);
        assertTrue(uuidMapList.equals(idMapList));
    }

    @Test
    public void testReplaceMapsID2UUIDNotID() {
        List<Map<String, Object>> idMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> idMap = creatIDMap("not-id");
        idMapList.add(idMap);
        List<Map<String, Object>> uuidMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> uuidMap = creatUUIDMap("not-id");
        ;
        uuidMapList.add(uuidMap);
        ParamConverter.replaceMapsID2UUID(idMapList);
        assertTrue(uuidMapList.equals(idMapList));
    }

    @Test
    public void testReplaceMapID2UUIDEmpty() {
        Map<String, Object> idMap = new HashMap<String, Object>();
        ParamConverter.replaceMapID2UUID(idMap);
    }

    @Test
    public void testReplaceMapID2UUIDNull() {
        ParamConverter.replaceMapID2UUID(null);
    }

    @Test
    public void testReplaceMapID2UUID() {
        Map<String, Object> idMap = creatIDMap(Constant.IDS[2]);
        Map<String, Object> uuidMap = creatUUIDMap(Constant.UUIDS[2]);
        ParamConverter.replaceMapID2UUID(idMap);
        assertTrue(uuidMap.equals(idMap));
    }

    @Test
    public void testReplaceMapID2UUIDNotID() {
        Map<String, Object> idMap = creatIDMap("not-id");
        Map<String, Object> uuidMap = creatUUIDMap("not-id");
        ;
        ParamConverter.replaceMapID2UUID(idMap);
        assertTrue(uuidMap.equals(idMap));
    }

    @Test
    public void testReplaceListID2UUIDEmpty() {
        List<String> idList = new ArrayList<String>();
        ParamConverter.replaceListID2UUID(idList);
    }

    @Test
    public void testReplaceListID2UUIDNull() {
        ParamConverter.replaceListID2UUID(null);
    }

    @Test
    public void testReplaceListID2UUID() {
        List<String> idList = new ArrayList<String>();
        idList.add(Constant.IDS[3]);
        idList.add("not-id");
        ParamConverter.replaceListID2UUID(idList);
        List<String> uuidList = new ArrayList<String>();
        uuidList.add(Constant.UUIDS[3]);
        uuidList.add("not-id");
        assertTrue(uuidList.equals(idList));
    }

    @Test
    public void testReplaceID2UUIDNotID() {
        String resultTemp = ParamConverter.replaceID2UUID("not-id");
        assertTrue("not-id".equals(resultTemp));
    }

    @Test
    public void testReplaceID2UUID() {
        String resultTemp = ParamConverter.replaceID2UUID(Constant.IDS[1]);
        assertTrue(Constant.UUIDS[1].equals(resultTemp));
    }

    private Map<String, Object> creatUUIDMap(String key) {
        Map<String, Object> uuidMap = new HashMap<String, Object>();
        uuidMap.put(key, "mapTemp");
        return uuidMap;
    }

    private Map<String, Object> creatIDMap(String key) {
        Map<String, Object> idMap = new HashMap<String, Object>();
        idMap.put(key, "mapTemp");
        return idMap;
    }

}
