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

import org.apache.ibatis.annotations.Param;

import org.openo.sdno.mss.bucket.dao.pojo.ResourcePojo;

/**
 * Operation mapper for resource table.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-18
 */
public interface ResourceMapper {

    /**
     * Delete one record by primary key.<br>
     * 
     * @param uuid Primary key
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int deleteByPrimaryKey(String uuid);

    /**
     * Add one record including all fields.<br>
     * 
     * @param record New record
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int insert(ResourcePojo record);

    /**
     * Add specified field, only for non empty fields.<br>
     * 
     * @param record New record
     * @return The row number just inserted into the database
     * @since SDNO 0.5
     */
    int insertSelective(ResourcePojo record);

    /**
     * Query resource data by primary key.<br>
     * 
     * @param bktName Bucket name
     * @return Resource data
     * @since SDNO 0.5
     */
    List<ResourcePojo> selectByBktName(String bktName);

    /**
     * Update specified field of resource data by primary key, only for non empty fields.<br>
     * 
     * @param record Resource data record
     * @return The row number just updated in the database
     * @since SDNO 0.5
     */
    int updateByPrimaryKeySelective(ResourcePojo record);

    /**
     * Update one record by primary key.<br>
     * 
     * @param record Resource data record
     * @return The row number just updated in the database
     * @since SDNO 0.5
     */
    int updateByPrimaryKey(ResourcePojo record);

    /**
     * Query resource collection by bucket name and model name.<br>
     * 
     * @param bktName Bucket name
     * @param modelName Model name
     * @return Resource collection
     * @since SDNO 0.5
     */
    List<ResourcePojo> selectByBktAndModelName(@Param("bktName") String bktName, @Param("modelName") String modelName);
}
