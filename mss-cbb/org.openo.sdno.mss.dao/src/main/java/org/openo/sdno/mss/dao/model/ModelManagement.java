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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.openo.sdno.framework.container.util.Bucket;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.bucket.dao.pojo.MetaPojo;
import org.openo.sdno.mss.bucket.dao.pojo.RelationPojo;
import org.openo.sdno.mss.bucket.dao.pojo.ResourcePojo;
import org.openo.sdno.mss.bucket.intf.BucketHandler;
import org.openo.sdno.mss.bucket.intf.MetaHandler;
import org.openo.sdno.mss.dao.model.entity.ModelRedisEntity;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model Memory Data Management.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class ModelManagement {

    private static volatile Object lock = new Object();

    /**
     * Logger handler
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelManagement.class);

    /**
     * Spring injected, read the model
     */
    private MetaHandler metaHanlder = null;

    /**
     * Spring injected, read the bucket
     */
    private BucketHandler bucketHandler = null;

    /**
     * Set metaHanlder attribute.<br>
     * 
     * @param metaHanlder IMetaHandler Object
     * @since SDNO 0.5
     */
    public void setMetaHanlder(MetaHandler metaHanlder) {
        this.metaHanlder = metaHanlder;
    }

    /**
     * Set bucketHandler attribute.<br>
     * 
     * @param bucketHandler IBucketHandler Object
     * @since SDNO 0.5
     */
    public void setBucketHandler(BucketHandler bucketHandler) {
        this.bucketHandler = bucketHandler;
    }

    /**
     * Get ModelRedisEntity of bucket.<br>
     * 
     * @param bktName bucket name
     * @return ModelRedisEntity of bucket
     * @since SDNO 0.5
     */
    private ModelRedisEntity getRamModelEntity(String bktName) {
        if(null == ModelJedisCache.getInstance().getCacheEntity(bktName)
                || !ModelJedisCache.getInstance().getCacheEntity(bktName).isInited()) {
            synchronized(lock) {
                if(null == ModelJedisCache.getInstance().getCacheEntity(bktName)
                        || !ModelJedisCache.getInstance().getCacheEntity(bktName).isInited()) {
                    ModelRedisEntity entity = initModel(bktName);
                    if(null != entity) {
                        ModelJedisCache.getInstance().putCacheEntity(bktName, entity);
                        return entity;
                    }
                }
            }
        }
        return ModelJedisCache.getInstance().getCacheEntity(bktName);

    }

    /**
     * Initialize model entity and returned.<br>
     * 
     * @param bktName bucket name
     * @return model entity after initialization
     * @since SDNO 0.5
     */
    private ModelRedisEntity initModel(String bktName) {
        if(StringUtils.isEmpty(bktName)) {
            LOGGER.error("bktName is empty");
            return null;
        }

        MetaPojo metaPojo = metaHanlder.getMeta(bktName);
        if(null == metaPojo) {
            LOGGER.error("cannt get the metaData for " + bktName);
            return null;
        }

        RelationPojo relationPojo = metaPojo.getRelation();
        List<ResourcePojo> resourcePojoList = metaPojo.getResource();

        return initModel(relationPojo, resourcePojoList);

    }

    /**
     * Initialize model entity and returned.<br>
     * 
     * @param relationPojo RelationPojo Object
     * @param resourcePojoList ResourcePojo List
     * @return model entity after initialization
     * @since SDNO 0.5
     */
    private ModelRedisEntity initModel(RelationPojo relationPojo, List<ResourcePojo> resourcePojoList) {
        ModelRedisEntity entity = new ModelRedisEntity();

        for(ResourcePojo resourcePojo : resourcePojoList) {
            Infomodel infoModel = JsonUtil.fromJson(resourcePojo.getImspec(), Infomodel.class);
            Datamodel dataModel = JsonUtil.fromJson(resourcePojo.getDmspec(), Datamodel.class);
            if(null == dataModel || null == infoModel) {
                LOGGER.error("incomplete model info for " + resourcePojo.getBktName() + "." + resourcePojo.getRestype()
                        + " im:" + infoModel + " dm:" + dataModel + " MSS will ignore this model");
                continue;
            }
            entity.getInfoModelMap().put(infoModel.getName(), infoModel);
            infoModelFilelds(entity, resourcePojo.getBktName(), infoModel);
            entity.getDataModelMap().put(dataModel.getName(), dataModel);
        }

        Map<String, RelationModelRelation> relations =
                JsonUtil.fromJson(relationPojo.getRmspec(), new TypeReference<Map<String, RelationModelRelation>>() {});

        entity.getRelaModelMap().putAll(relations);

        entity.init();

        return entity;
    }

    /**
     * Get info model Map of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return info model Map of the bucket
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getInfoModelMap(String bktName) {
        return getRamModelEntity(bktName).getInfoModelMap();
    }

    /**
     * Get dataFields of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return dataFields of the bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getDefaultDataModelMap(String bktName) {
        return getRamModelEntity(bktName).getDataFields();
    }

    /**
     * Get listBktValues of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return listBktValues of the bucket
     * @since SDNO 0.5
     */
    public Map<String, List<String>> getBucketInfoModelMap(String bktName) {
        return getRamModelEntity(bktName).getListBktValues();
    }

    /**
     * Get wholeInfoModelMap of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return wholeInfoModelMap of the bucket
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getWholeInfoModelMap(String bktName) {
        return getRamModelEntity(bktName).getWholeInfoModelMap();
    }

    /**
     * Get dataModelMap of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return dataModelMap of the bucket
     * @since SDNO 0.5
     */
    public Map<String, Datamodel> getDataModelMap(String bktName) {
        return getRamModelEntity(bktName).getDataModelMap();
    }

    /**
     * Get relaModelMap of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return relaModelMap of the bucket
     * @since SDNO 0.5
     */
    public Map<String, RelationModelRelation> getRelaModelMap(String bktName) {
        return getRamModelEntity(bktName).getRelaModelMap();
    }

    /**
     * Get dataName2InfoNames Map of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return dataName2InfoNames Map of the bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getDataName2InfoNames(String bktName) {
        return getRamModelEntity(bktName).getDataName2InfoNames();
    }

    /**
     * Get resUniqueIndexMap of of the bucket.<br>
     * 
     * @param bktName bucket name
     * @return resUniqueIndexMap of of the bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getResUniqueIndexMap(String bktName) {
        return getRamModelEntity(bktName).getResUniqueIndexMap();
    }

    /**
     * Initialize models of all buckets<br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        List<Bucket> buckets = bucketHandler.getBucket();

        if(null == buckets || buckets.isEmpty()) {
            LOGGER.error("Bucket list is null or is empty");
            return;
        }

        for(Bucket bucket : buckets) {
            getRamModelEntity(bucket.getName());
        }
    }

    private void infoModelFilelds(ModelRedisEntity entity, String bktname, Infomodel model) {
        List<Property> inforProperties = model.getBasic().getProperty();
        // same resource type in the same bucket will be added in one list
        List<String> restypeValue = new ArrayList<String>();
        String restype = model.getName();
        for(Property pro : inforProperties) {
            if("not null".equals(pro.getDefault())) {
                entity.getDataFields().put(bktname + "-" + restype + "-" + pro.getName(), "not null");
                restypeValue.add(pro.getName());
            }
        }

        entity.getListBktValues().put(bktname + "-" + restype, restypeValue);
    }
}
