/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.mss.init.mybatis.mappers;

import org.openo.sdno.mss.init.mybatis.pojo.RelationPojo;

/**
 * Relation map class, used to create, insert, select, update a relation pojo. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public interface RelationMapper {

    /**
     * Create a new table. <br/>
     * 
     * @return effected line number.
     * @since SDNO 0.5
     */
    int createTable();

    /**
     * Delete a relation pojo by it's primary key got by the bucket name. <br/>
     * 
     * @param bktName name of the bucket.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int deleteByPrimaryKey(String bktName);

    /**
     * Add a new relation pojo. <br/>
     * 
     * @param record the new relation pojo.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insert(RelationPojo record);

    /**
     * Add a new row in the specific table. <br/>
     * 
     * @param record the new pojo.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int insertSelective(RelationPojo record);

    /**
     * Get the relation pojo by it's primary key. <br/>
     * 
     * @param bktName bucket name, used to get the primary key.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    RelationPojo selectByPrimaryKey(String bktName);

    /**
     * Update the specific row in the pojo by it's primary key. <br/>
     * 
     * @param record the new pojo.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKeySelective(RelationPojo record);

    /**
     * Update all the rows in the pojo by it's primary key. <br/>
     * 
     * @param record the new pojo.
     * @return Primaty key if success, 0 if failed.
     * @since SDNO 0.5
     */
    int updateByPrimaryKey(RelationPojo record);
}
