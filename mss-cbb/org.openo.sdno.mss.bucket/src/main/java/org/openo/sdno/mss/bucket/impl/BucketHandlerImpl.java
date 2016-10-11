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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.Bucket;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.bucket.dao.mappers.BucketMapper;
import org.openo.sdno.mss.bucket.dao.pojo.BucketPojo;
import org.openo.sdno.mss.bucket.intf.BucketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bucket handler class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class BucketHandlerImpl extends AHandlerImpl implements BucketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketHandlerImpl.class);

    private volatile BucketMapper mapper = null;

    /**
     * Initialize bucket mapper.<br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        this.mapper = getMapper();
    }

    private BucketMapper getMapper() {
        if(null == this.mapper) {
            synchronized(this) {
                if(null == this.mapper) {
                    this.mapper = super.getMapper(BucketMapper.class);
                }
            }
        }

        return this.mapper;
    }

    @Override
    public List<Bucket> getBucket() {
        List<Bucket> buckets = new ArrayList<Bucket>();
        List<BucketPojo> pojos = getMapper().selectAll();

        if(null != pojos) {
            for(BucketPojo pojo : pojos) {
                buckets.add(convertBucketPojo2Bucket(pojo));
            }
        }

        return buckets;
    }

    private BucketPojo convertBucket2BucketPojo(Bucket bucket) {
        if(null == bucket) {
            return null;
        }

        BucketPojo pojo = new BucketPojo();

        pojo.setAllowread(JsonUtil.toJson(bucket.getAllowread()));
        pojo.setAuthorization(bucket.getAuthorization());
        pojo.setName(bucket.getName());
        pojo.setOwner(bucket.getOwner());

        return pojo;
    }

    private Bucket convertBucketPojo2Bucket(BucketPojo pojo) {
        if(null == pojo) {
            return null;
        }

        Bucket bucket = new Bucket(pojo.getName(), pojo.getOwner(), pojo.getAuthorization());
        bucket.setAllowread(JsonUtil.fromJson(pojo.getAllowread(), new TypeReference<List<String>>() {}));

        return bucket;

    }
}
