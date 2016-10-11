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

package org.openo.sdno.mss.init.mybatis.mappers;

import java.util.List;

import org.openo.sdno.mss.init.mybatis.pojo.BucketPojo;

/**
 * Mapper of bucket,used to create, insert, select and update a bucket PoJo in the database. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public interface BucketMapper {

    /**
     * Create a new table. <br>
     * 
     * @return effected line number.
     * @since SDNO 0.5
     */
    int createTable();

    /**
     * Delete a bucket by its primary key. <br>
     * 
     * @param name name of bucket.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int deleteByPrimaryKey(String name);

    /**
     * Add a new bucket into the DB. <br>
     * 
     * @param record the new bucket.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insert(BucketPojo record);

    /**
     * Insert a new record in the specific row. <br>
     * 
     * @param record the new PoJo.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insertSelective(BucketPojo record);

    /**
     * Get a bucket by its name. <br>
     * 
     * @param name name of the bucket.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    BucketPojo selectByPrimaryKey(String name);

    /**
     * Query all the bucket. <br>
     * 
     * @return List of the buckets.
     * @since SDNO 0.5
     */
    List<BucketPojo> selectAll();

    /**
     * Update the specific field by its primary key. <br>
     * 
     * @param record the new PoJo.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKeySelective(BucketPojo record);

    /**
     * Update the bucket by its primary key.<br>
     * 
     * @param record the new PoJo
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKey(BucketPojo record);
}
