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

package org.openo.sdno.mss.dao.model.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.sdno.mss.model.util.ModelUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelIndex;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redius model Entity class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public class ModelRedisEntity {

    /**
     * Logger handler
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelRedisEntity.class);

    /**
     * Initialization flag
     */
    private boolean inited = false;

    /**
     * Info model
     */
    private volatile Map<String, Infomodel> infoModelMap = new ConcurrentHashMap<String, Infomodel>();

    /**
     * Whole Info model
     */
    private volatile Map<String, Infomodel> wholeInfoModelMap = new ConcurrentHashMap<String, Infomodel>();

    /**
     * Data model map
     */
    private Map<String, Datamodel> dataModelMap = new ConcurrentHashMap<String, Datamodel>();

    /**
     * Relation model map
     */
    private Map<String, RelationModelRelation> relaModelMap = new ConcurrentHashMap<String, RelationModelRelation>();

    /**
     * Map of data model and info model name, key=data model name value=info model name
     */
    private Map<String, String> dataName2InfoNames = new ConcurrentHashMap<String, String>();

    /**
     * Map of resource type and unique index, key=resource type value= unique index
     */
    private Map<String, String> resUniqueIndexMap = new ConcurrentHashMap<String, String>();

    /**
     * Map of bucket name and resource type name
     */
    private Map<String, String> dataFields = new ConcurrentHashMap<String, String>();

    /**
     * Map of Bucket and resource types. key=Bucket name value=List of Resource types
     */
    private Map<String, List<String>> listBktValues = new ConcurrentHashMap<String, List<String>>();

    /**
     * Get dataFields attribute.<br>
     * 
     * @return dataFields attribute
     * @since SDNO 0.5
     */
    public Map<String, String> getDataFields() {
        return dataFields;
    }

    /**
     * Set dataFields attribute.<br>
     * 
     * @param dataFields Map Object
     * @since SDNO 0.5
     */
    public void setDataFields(Map<String, String> dataFields) {
        this.dataFields = dataFields;
    }

    /**
     * Get listBktValues attribute.<br>
     * 
     * @return listBktValues attribute
     * @since SDNO 0.5
     */
    public Map<String, List<String>> getListBktValues() {
        return listBktValues;
    }

    /**
     * Set listBktValues attribute.<br>
     * 
     * @param listBktValues Map Object
     * @since SDNO 0.5
     */
    public void setListBktValues(Map<String, List<String>> listBktValues) {
        this.listBktValues = listBktValues;
    }

    /**
     * Get initialized attribute.<br>
     * 
     * @return initialized attribute
     * @since SDNO 0.5
     */
    public boolean isInited() {
        return inited;
    }

    /**
     * Set initialized attribute<br>
     * 
     * @param inited
     * @since SDNO 0.5
     */
    public void setInited(boolean inited) {
        this.inited = inited;
    }

    /**
     * Get infoModelMap attribute.<br>
     * 
     * @return infoModelMap attribute
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getInfoModelMap() {
        return infoModelMap;
    }

    /**
     * Set infoModelMap attribute.<br>
     * 
     * @param infoModelMap Map Object
     * @since SDNO 0.5
     */
    public void setInfoModelMap(Map<String, Infomodel> infoModelMap) {
        this.infoModelMap = infoModelMap;
    }

    /**
     * Get wholeInfoModelMap attribute.<br>
     * 
     * @return wholeInfoModelMap attribute
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getWholeInfoModelMap() {
        return wholeInfoModelMap;
    }

    /**
     * Set wholeInfoModelMap attribute.<br>
     * 
     * @param wholeInfoModelMap Map Object
     * @since SDNO 0.5
     */
    public void setWholeInfoModelMap(Map<String, Infomodel> wholeInfoModelMap) {
        this.wholeInfoModelMap = wholeInfoModelMap;
    }

    /**
     * Get dataModelMap attribute.<br>
     * 
     * @return dataModelMap attribute
     * @since SDNO 0.5
     */
    public Map<String, Datamodel> getDataModelMap() {
        return dataModelMap;
    }

    /**
     * Set dataModelMap attribute.<br>
     * 
     * @param dataModelMap Map Object
     * @since SDNO 0.5
     */
    public void setDataModelMap(Map<String, Datamodel> dataModelMap) {
        this.dataModelMap = dataModelMap;
    }

    /**
     * Get relaModelMap attribute.<br>
     * 
     * @return relaModelMap attribute
     * @since SDNO 0.5
     */
    public Map<String, RelationModelRelation> getRelaModelMap() {
        return relaModelMap;
    }

    /**
     * Set relaModelMap attribute.<br>
     * 
     * @param relaModelMap Map object
     * @since SDNO 0.5
     */
    public void setRelaModelMap(Map<String, RelationModelRelation> relaModelMap) {
        this.relaModelMap = relaModelMap;
    }

    /**
     * Get dataName2InfoNames attribute.<br>
     * 
     * @return dataName2InfoNames attribute
     * @since SDNO 0.5
     */
    public Map<String, String> getDataName2InfoNames() {
        return dataName2InfoNames;
    }

    /**
     * Set dataName2InfoNames attribute.<br>
     * 
     * @param dataName2InfoNames Map Object
     * @since SDNO 0.5
     */
    public void setDataName2InfoNames(Map<String, String> dataName2InfoNames) {
        this.dataName2InfoNames = dataName2InfoNames;
    }

    /**
     * Get resUniqueIndexMap attribute.<br>
     * 
     * @return resUniqueIndexMap attribute
     * @since SDNO 0.5
     */
    public Map<String, String> getResUniqueIndexMap() {
        return resUniqueIndexMap;
    }

    /**
     * Set resUniqueIndexMap attribute.<br>
     * 
     * @param resUniqueIndexMap Map Object
     * @since SDNO 0.5
     */
    public void setResUniqueIndexMap(Map<String, String> resUniqueIndexMap) {
        this.resUniqueIndexMap = resUniqueIndexMap;
    }

    /**
     * Initialize redis model.<br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        try {
            initWholeInfoModel();
            initResUniqueIndex();
            this.inited = true;
        } catch(Exception e) {
            LOGGER.error("exception while init models, e: ", e);
            this.inited = false;
        }
    }

    /**
     * Initialize dataModelMap and resUniqueIndexMap.<br>
     * 
     * @since SDNO 0.5
     */
    private void initResUniqueIndex() {
        Object[] dataKeySet = this.dataModelMap.keySet().toArray();
        for(Object keyStr : dataKeySet) {
            String infoName = dataName2InfoNames.get(keyStr);
            if(infoName == null) {
                LOGGER.error("Failed to get info model name of data model {}", keyStr);
            } else {
                this.dataModelMap.put(infoName, dataModelMap.get(keyStr));
            }
            this.dataModelMap.remove(keyStr);
        }

        // generate resUniqueIndexMap
        for(Map.Entry<String, Datamodel> entry : this.dataModelMap.entrySet()) {
            String resType = entry.getKey();
            inner: for(DataModelIndex index : entry.getValue().getIndex()) {
                if(index.isIsunique()) {
                    this.resUniqueIndexMap.put(resType, index.getValue());
                    break inner;
                }
            }
        }
    }

    /**
     * Initialize whole info model.<br>
     * 
     * @since SDNO 0.5
     */
    private void initWholeInfoModel() {
        if(null == this.dataName2InfoNames) {
            this.dataName2InfoNames = new ConcurrentHashMap<String, String>();
        }

        for(Infomodel model : this.infoModelMap.values()) {
            this.wholeInfoModelMap.put(model.getName(), ModelUtil.getWholeInfoModel(model));
            this.dataName2InfoNames.put(model.getDatamodel(), model.getName());
        }
    }
}
