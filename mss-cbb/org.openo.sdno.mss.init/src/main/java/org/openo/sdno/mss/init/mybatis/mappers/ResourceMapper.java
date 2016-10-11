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

import org.openo.sdno.mss.init.mybatis.pojo.ResourcePojo;

/**
 * Resource mapper class, used to create, insert, update, delete a resource PoJo in DB. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public interface ResourceMapper {

    /**
     * Create a new table. <br>
     * 
     * @return effected line number.
     * @since SDNO 0.5
     */
    int createTable();

    /**
     * Delete a row by its UUID. <br>
     * 
     * @param uuid UUID of the resource.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int deleteByPrimaryKey(String uuid);

    /**
     * Insert a new record. <br>
     * 
     * @param record the new record.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insert(ResourcePojo record);

    /**
     * Insert a new field, and it can not be empty. <br>
     * 
     * @param record the new recored.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insertSelective(ResourcePojo record);

    /**
     * Get all the resource by the bucket name. <br>
     * 
     * @param bktName name of the bucket.
     * @return List of resource PoJo.
     * @since SDNO 0.5
     */
    List<ResourcePojo> selectByBktName(String bktName);

    /**
     * Update a field by its primary key. <br>
     * 
     * @param record the new record.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKeySelective(ResourcePojo record);

    /**
     * Update a record by its primary key. <br>
     * 
     * @param record the new record.
     * @return Primary key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKey(ResourcePojo record);
}
