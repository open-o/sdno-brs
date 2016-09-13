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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.mss.init.mybatis.mappers.ResourceMapper;
import org.openo.sdno.mss.init.mybatis.pojo.ResourcePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mybatis resource handler class, use Bucket class to get all the resource(key-value pair) from
 * configure file. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ResourceHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHandler.class);

    private ResourceMapper getMapper() {
        return super.getMapper(ResourceMapper.class);
    }

    /**
     * Create resource table.<br>
     * 
     * @return the effect row of data base.
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

    /**
     * Batch add new resources.<br>
     * 
     * @param resources resource list need to added.
     * @return the effect row of data base.
     * @since SDNO 0.5
     */
    public int addResources(List<ResourcePojo> resources) {
        if(null == resources) {
            LOGGER.error("resources cannt be null");
            return 0;
        }
        int row = 0;

        for(ResourcePojo record : resources) {
            row += addResources(record);
        }

        return row;
    }

    /**
     * Add new resources.<br>
     * 
     * @param resource resource need to added.
     * @return the effect row of data base.
     * @since SDNO 0.5
     */
    public int addResources(ResourcePojo resource) {
        if(null == resource) {
            LOGGER.error("resource cannt be null");
            return 0;
        }
        int row = 0;
        try {
            if(StringUtils.isEmpty(resource.getImspec()) || StringUtils.isEmpty(resource.getDmspec())) {
                LOGGER.error("info model or data model for " + resource.getRestype()
                        + " is empty, cannt insert it to db. im:" + resource.getImspec() + " dm: "
                        + resource.getDmspec());
            } else {
                row = getMapper().insert(resource);
                super.commit();
            }
        } finally {
            super.close();
        }

        return row;
    }

}
