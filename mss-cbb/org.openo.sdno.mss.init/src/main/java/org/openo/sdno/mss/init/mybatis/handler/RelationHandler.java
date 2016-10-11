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

package org.openo.sdno.mss.init.mybatis.handler;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.mss.init.mybatis.mappers.RelationMapper;
import org.openo.sdno.mss.init.mybatis.pojo.RelationPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Relation handler class, used to get, add, delete, and update the relation map. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class RelationHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationHandler.class);

    private RelationMapper getMapper() {
        return super.getMapper(RelationMapper.class);
    }

    /**
     * Add a new relation model. <br>
     * 
     * @param relation new relation PoJo need to add.
     * @return primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    public int addRelation(RelationPojo relation) {
        if(null == relation) {
            LOGGER.error("relation cannt be null");
            return 0;
        }

        int row = 0;

        try {
            row = getMapper().insert(relation);
            super.commit();
        } finally {
            super.close();
        }
        return row;
    }

    /**
     * Get the model by Bucket name. <br>
     * 
     * @param bktName name of the Bucket object.
     * @return RelationPojo object.
     * @since SDNO 0.5
     */
    public RelationPojo getRelation(String bktName) {
        if(StringUtils.isEmpty(bktName)) {
            LOGGER.error("bktName cannt be empty");
            return null;
        }

        RelationPojo pojo = null;

        try {
            pojo = getMapper().selectByPrimaryKey(bktName);

        } finally {
            super.close();
        }

        return pojo;
    }

    /**
     * Create a new table. <br>
     * 
     * @return primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    public int createTable() {
        int row = 0;
        try {
            row = getMapper().createTable();
            super.commit();
        } finally {
            super.close();
        }

        return row;
    }
}
