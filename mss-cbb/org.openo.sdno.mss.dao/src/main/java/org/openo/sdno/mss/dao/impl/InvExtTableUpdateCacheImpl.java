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

package org.openo.sdno.mss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.constant.InvAttrDefine;
import org.openo.sdno.mss.dao.pojo.InvExtTablePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Update cache of extended table class. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class InvExtTableUpdateCacheImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvExtTableUpdateCacheImpl.class);

    private static class ExtEntity {

        private final String resType;

        private final String uuid;

        private final String attrName;

        /**
         * Constructor<br>
         * 
         * @since SDNO 0.5
         * @param resType The resource type
         * @param uuid The UUID
         * @param attrName The attribute name
         */
        public ExtEntity(String resType, String uuid, String attrName) {
            this.resType = resType;
            this.uuid = uuid;
            this.attrName = attrName;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            }

            ExtEntity other = (ExtEntity)obj;
            return ObjectUtils.equals(resType, other.resType) && ObjectUtils.equals(uuid, other.uuid)
                    && ObjectUtils.equals(attrName, other.attrName);
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(resType);
            builder.append(uuid);
            builder.append(attrName);

            return builder.toHashCode();
        }
    }

    private Map<ExtEntity, String> cache;

    private final SqlSession session;

    private SqlSession batchSession;

    private final Callable<SqlSession> callable;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param session The session
     * @param callable2 The callable of SqlSession
     */
    public InvExtTableUpdateCacheImpl(SqlSession session, Callable<SqlSession> callable2) {
        this.session = session;
        this.callable = callable2;
    }

    /**
     * Query and store in cache. <br>
     * 
     * @param resType Resource type
     * @param datas Data
     * @since SDNO 0.5
     */
    public void query(String resType, List<Map<String, Object>> datas) {
        cache = new HashMap<ExtEntity, String>(datas.size() * 2);
        List<String> uuids = new ArrayList<String>(datas.size());
        for(Map<String, Object> data : datas) {
            String uuid = data.get(InvAttrDefine.UUID.getValue()).toString();
            uuids.add(uuid);
        }

        try {
            doQuery(resType, uuids);
        } finally {
            if(batchSession != null) {
                batchSession.close();
            }
        }
    }

    private void doQuery(String resType, List<String> uuids) {
        if(uuids.isEmpty()) {
            LOGGER.warn("Should not empty uuids: {}", resType);
            return;
        }

        InvExtTablePojo tablePojo = new InvExtTablePojo(resType);
        List<InvExtTablePojo> result;
        if(uuids.size() == 1) {
            tablePojo.buildUuidFilter(uuids.get(0));
            result = tablePojo.getExtData(session);
        } else {
            if(batchSession == null) {
                try {
                    batchSession = callable.call();
                } catch(Exception ex) {
                    // Generally should not fail
                    throw new IllegalStateException(ex);
                }
            }

            result = tablePojo.getExtDatas(session, batchSession, uuids);
        }

        for(InvExtTablePojo pojo : result) {
            ExtEntity entity = new ExtEntity(resType, pojo.getUuid(), pojo.getAttrName());
            cache.put(entity, pojo.getAttrValue());
        }
    }

    /**
     * Get value from cache. <br>
     * 
     * @param resType Resource type
     * @param uuid UUID
     * @param attrName Attribute name
     * @return Value which is got from cache
     * @since SDNO 0.5
     */
    public String getValue(String resType, String uuid, String attrName) {
        ExtEntity entity = new ExtEntity(resType, uuid, attrName);
        return cache.get(entity);
    }
}
