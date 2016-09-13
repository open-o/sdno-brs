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

package org.openo.sdno.mss.dao.model;

import org.openo.sdno.framework.container.util.DefaultEnvUtil;
import org.openo.sdno.mss.dao.exception.GeneralException;
import org.openo.sdno.mss.dao.model.entity.ModelRedisEntity;

/**
 * This is class of model Jedis Cache.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class ModelJedisCache {

    private static Object lock = new Object();

    /**
     * ModelJedisCache singleton instance
     */
    private static volatile ModelJedisCache INSTANCE = null;

    /**
     * redis cache
     */

    private ModelJedisCache() {

    }

    /**
     * Get ModelJedisCache singleton instance.<br>
     * 
     * @return ModelJedisCache singleton instance
     * @since SDNO 0.5
     */
    public static ModelJedisCache getInstance() {
        if(null == INSTANCE) {
            synchronized(lock) {
                if(null == INSTANCE) {
                    ModelJedisCache jedisCache = new ModelJedisCache();
                    if(!jedisCache.getCahce()) {
                        INSTANCE = null;
                        throw new GeneralException("Init Model Jedis Cache instance failed");
                    } else {
                        INSTANCE = jedisCache;
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Get ModelRedisEntity instance.<br>
     * 
     * @param bktName bucket name
     * @return ModelRedisEntity instance
     * @since SDNO 0.5
     */
    public ModelRedisEntity getCacheEntity(String bktName) {
        return null;
    }

    /**
     * Put ModelRedisEntity instance.<br>
     * 
     * @param bktName bucket name
     * @param entity ModelRedisEntity object
     * @since SDNO 0.5
     */
    public void putCacheEntity(String bktName, ModelRedisEntity entity) {
        // To put ModelRedisEntity instance
    }

    /**
     * Get RedisKey of Bucket.<br>
     * 
     * @param bktName bucket name
     * @return RedisKey of Bucket
     * @since SDNO 0.5
     */
    private String getRedisKey(String bktName) {

        return DefaultEnvUtil.getAppName() + "_model_" + bktName;
    }

    private boolean getCahce() {
        return true;
    }
}
