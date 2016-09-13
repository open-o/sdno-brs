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

import org.openo.sdno.mss.init.mybatis.mappers.BucketMapper;
import org.openo.sdno.mss.init.mybatis.pojo.BucketPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bucket handler class, used to get, add, update, delete a Bucket object. <br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class BucketHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketHandler.class);

    /**
     * Add a new bucket. <br>
     * 
     * @param bucket bucket need to add.
     * @return primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    public int addBucket(BucketPojo bucket) {
        if(null == bucket) {
            LOGGER.error("bucekt cannt be null");
            return 0;
        }

        int row = 0;
        try {
            row = getMapper().insert(bucket);

            super.commit();
        } finally {
            super.close();
        }

        return row;

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

    private BucketMapper getMapper() {
        return super.getMapper(BucketMapper.class);
    }
}
