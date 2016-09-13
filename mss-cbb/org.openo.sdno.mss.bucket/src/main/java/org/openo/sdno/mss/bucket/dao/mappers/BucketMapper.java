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

package org.openo.sdno.mss.bucket.dao.mappers;

import java.util.List;

import org.openo.sdno.mss.bucket.dao.pojo.BucketPojo;

/**
 * Operation mapper for bucket table.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-18
 */
public interface BucketMapper {

    /**
     * Delete bucket records by primary key.<br>
     * 
     * @param name Bucket name
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int deleteByPrimaryKey(String name);

    /**
     * Add bucket record.<br>
     * 
     * @param record New record
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int insert(BucketPojo record);

    /**
     * Add specified field of bucket record.<br>
     * 
     * @param record Bucket record
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int insertSelective(BucketPojo record);

    /**
     * Query bucket record by primary key.<br>
     * 
     * @param name Bucket name
     * @return Bucket record
     * @since SDNO 0.5
     */
    BucketPojo selectByPrimaryKey(String name);

    /**
     * Query all bucket records.<br>
     * 
     * @return Collection of bucket records
     * @since SDNO 0.5
     */
    List<BucketPojo> selectAll();

    /**
     * Update specified field of bucket record by primary key.<br>
     * 
     * @param record Bucket record
     * @return The row number just updated in the database
     * @since SDNO 0.5
     */
    int updateByPrimaryKeySelective(BucketPojo record);

    /**
     * Update bucket record by primary key.<br>
     * 
     * @param record Bucket record
     * @return The row number just updated in the database
     * @since SDNO 0.5
     */
    int updateByPrimaryKey(BucketPojo record);
}
