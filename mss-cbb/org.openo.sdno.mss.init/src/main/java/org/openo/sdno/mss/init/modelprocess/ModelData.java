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

package org.openo.sdno.mss.init.modelprocess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.sdno.mss.init.buckets.BucketModel;
import org.openo.sdno.mss.model.util.ModelUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelIndex;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model data processing class to initialize and update the model. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class ModelData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelData.class);

    private Map<String, Infomodel> wholeInfoModels = new ConcurrentHashMap<String, Infomodel>();

    private Map<String, String> dataName2InfoNames = new ConcurrentHashMap<String, String>();

    private Map<String, String> modelUniqueIndexs = new ConcurrentHashMap<String, String>();

    private BucketModel bktModel = null;

    private String bktName = "";

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param bktModel
     * @param bktName
     */
    public ModelData(BucketModel bktModel, String bktName) {
        super();
        this.bktModel = bktModel;
        this.bktName = bktName;
    }

    /**
     * @return Returns the wholeInfoModels.
     */
    public Map<String, Infomodel> getWholeInfoModels() {
        return wholeInfoModels;
    }

    /**
     * @return Returns the dataName2InfoNames.
     */
    public Map<String, String> getDataName2InfoNames() {
        return dataName2InfoNames;
    }

    /**
     * @return Returns the bktName.
     */
    public String getBktName() {
        return bktName;
    }

    /**
     * @return The data models.
     * @since SDNO 0.5
     */
    public Map<String, Datamodel> getDataModels() {
        return this.bktModel.getDataModels();
    }

    /**
     * @return The relation models.
     * @since SDNO 0.5
     */
    public Map<String, RelationModelRelation> getRelationModels() {
        return this.bktModel.getRelationModels();

    }

    /**
     * Initialize the model data. <br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        LOGGER.info("Load model data into Mem.....");
        generateInfoModel();
        postProcess(this.bktModel.getDataModels(), this.dataName2InfoNames, this.modelUniqueIndexs);
    }

    /**
     * Replace the key in dataModelMap to the name of info name. <br>
     * 
     * @param dataModelMap data model set
     * @param dataName2InfoName mapping from data model to information model
     * @param resUniqueIndexMap unique index of map
     * @return true if success, false if failed
     * @since SDNO 0.5
     */
    private boolean postProcess(Map<String, Datamodel> dataModelMap, Map<String, String> dataName2InfoName,
            Map<String, String> resUniqueIndexMap) {
        boolean allSuc = true;
        Object[] dataKeySet = dataModelMap.keySet().toArray();
        for(Object keyStr : dataKeySet) {
            String infoName = dataName2InfoName.get(keyStr);
            if(infoName == null) {
                allSuc = false;
                LOGGER.error("Failed to get info model name of data model {}", keyStr);
            } else {
                dataModelMap.put(infoName, dataModelMap.get(keyStr));
            }
            dataModelMap.remove(keyStr);
        }

        for(Map.Entry<String, Datamodel> entry : dataModelMap.entrySet()) {
            String resType = entry.getKey();
            inner: for(DataModelIndex index : entry.getValue().getIndex()) {
                if(index.isIsunique()) {
                    resUniqueIndexMap.put(resType, index.getValue());
                    break inner;
                }
            }
        }
        return allSuc;
    }

    private void generateInfoModel() {
        Map<String, Infomodel> infoModels = this.bktModel.getInfoModels();

        for(Infomodel model : infoModels.values()) {
            this.wholeInfoModels.put(model.getName(), ModelUtil.getWholeInfoModel(model));
            this.dataName2InfoNames.put(model.getDatamodel(), model.getName());
        }
    }

    /**
     * Read the info model from file. <br>
     * 
     * @return map of the info model
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getInfoModelMap() {
        return this.bktModel.getInfoModels();
    }
}
