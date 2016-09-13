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

package org.openo.sdno.mss.bucket.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.sdno.mss.bucket.dao.mappers.RelationMapper;
import org.openo.sdno.mss.bucket.dao.pojo.RelationPojo;

/**
 * Relation handler class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class RelationHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationHandler.class);

    private volatile RelationMapper mapper = null;

    private RelationMapper getMapper() {
        if(null == this.mapper) {
            synchronized(this) {
                if(null == this.mapper) {
                    this.mapper = super.getMapper(RelationMapper.class);
                }
            }
        }

        return this.mapper;
    }
    
    /**
     * Init the mapper.<br>
     * 
     * @since  SDNO 0.5
     */
    public void init() {
        this.mapper = getMapper();
    }
    /**
     * Get relation data by bucket name.<br>
     * 
     * @param bktName Bucket name
     * @return Relation POJO
     * @since SDNO 0.5
     */
    public RelationPojo getRelation(String bktName) {
        if(StringUtils.isEmpty(bktName)) {
            LOGGER.error("bktName cannt be empty");
            return null;
        }

        return getMapper().selectByPrimaryKey(bktName);
    }
}
