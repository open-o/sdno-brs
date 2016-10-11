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

import java.util.List;

import org.openo.sdno.mss.bucket.dao.mappers.ResourceMapper;
import org.openo.sdno.mss.bucket.dao.pojo.ResourcePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource handler class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ResourceHandlerImpl extends AHandlerImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHandlerImpl.class);

    private volatile ResourceMapper mapper = null;

    private ResourceMapper getMapper() {
        if(null == this.mapper) {
            synchronized(this) {
                if(null == this.mapper) {
                    this.mapper = super.getMapper(ResourceMapper.class);
                }
            }
        }

        return this.mapper;
    }

    /**
     * Initialize the mapper.<br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        this.mapper = getMapper();
    }

    /**
     * Get resource models of specified bucket by bucket name.<br>
     * 
     * @param bktName Bucket name
     * @return Collection of resource models
     * @since SDNO 0.5
     */
    public List<ResourcePojo> getBucketResources(String bktName) {
        return getMapper().selectByBktName(bktName);
    }
}
