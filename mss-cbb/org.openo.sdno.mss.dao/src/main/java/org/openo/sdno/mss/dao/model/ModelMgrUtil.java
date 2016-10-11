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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;
import org.openo.sdno.mss.dao.util.SpringContextUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelFilter;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;

/**
 * Model Manage Utility class.<br>
 * <p>
 * manage a instance of ModelManagement
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 20, 2016
 */
public final class ModelMgrUtil {

    /**
     * ModelMgrUtil Singleton instance
     */
    private static final ModelMgrUtil INSTANCE = new ModelMgrUtil();

    private ModelManagement modelMgr = null;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     */
    private ModelMgrUtil() {
    }

    /**
     * Get Singleton instance of ModelMgrUtil.<br>
     * 
     * @return singleton instance
     * @since SDNO 0.5
     */
    public static ModelMgrUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Get bucket name.<br>
     * 
     * @return bucket name
     * @since SDNO 0.5
     */
    private String getBktName() {
        return DataSourceCtrler.get();

    }

    /**
     * Get ModelManagement instance.<br>
     * 
     * @return ModelManagement instance
     * @since SDNO 0.5
     */
    private ModelManagement getModelMgr() {
        if(null == this.modelMgr) {
            synchronized(INSTANCE) {
                if(null == this.modelMgr) {
                    modelMgr = SpringContextUtil.getBean("mssModelManagement", ModelManagement.class);
                }
            }
        }
        return this.modelMgr;
    }

    /**
     * Get infoModelMap of this bucket.<br>
     * 
     * @return infoModelMap of this bucket
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getInfoModelMap() {
        return getModelMgr().getInfoModelMap(getBktName());
    }

    /**
     * Get dataFields of this bucket.<br>
     * 
     * @return dataFields of this bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getNonEmptyValuesInfoModelMap() {
        return getModelMgr().getDefaultDataModelMap(getBktName());
    }

    /**
     * Get listBktValues of this bucket.<br>
     * 
     * @return listBktValues of this bucket
     * @since SDNO 0.5
     */
    public Map<String, List<String>> getNonEmptyPropertyInfoModelMap() {
        return getModelMgr().getBucketInfoModelMap(getBktName());
    }

    /**
     * Get wholeInfoModelMap of this bucket,info contains uuid/createtime/updatetime.<br>
     * 
     * @return wholeInfoModelMap of this bucket
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getWholeInfoModelMap() {
        return getModelMgr().getWholeInfoModelMap(getBktName());
    }

    /**
     * Get dataModelMap of this bucket.<br>
     * 
     * @return dataModelMap of this bucket
     * @since SDNO 0.5
     */
    public Map<String, Datamodel> getDataModelMap() {
        return getModelMgr().getDataModelMap(getBktName());
    }

    /**
     * Get relaModelMap of this bucket.<br>
     * 
     * @return relaModelMap of this bucket
     * @since SDNO 0.5
     */
    public Map<String, RelationModelRelation> getRelaModelMap() {
        return getModelMgr().getRelaModelMap(getBktName());
    }

    /**
     * Get dataName2InfoNames of this bucket.<br>
     * 
     * @return dataName2InfoNames of this bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getDataName2InfoNames() {
        return getModelMgr().getDataName2InfoNames(getBktName());
    }

    /**
     * Get resUniqueIndexMap of this bucket.<br>
     * 
     * @return resUniqueIndexMap of this bucket
     * @since SDNO 0.5
     */
    public Map<String, String> getResUniqueIndexMap() {
        return getModelMgr().getResUniqueIndexMap(getBktName());
    }

    /**
     * Get filter description of res.<br>
     * 
     * @param resType resource type
     * @param filterName filter name
     * @return filter description of res
     * @since SDNO 0.5
     */
    public String getFilterDesc(String resType, String filterName) {
        String realFilterName = StringUtils.isEmpty(filterName) ? "default" : filterName;

        // construct unique index attribute
        Datamodel dataModel = getDataModelMap().get(resType);
        for(DataModelFilter filter : dataModel.getFilter()) {
            if(realFilterName.equals(filter.getName())) {
                return filter.getValue();
            }
        }

        throw new IllegalArgumentException(
                "Cannot find filter value, resType = " + resType + ", filterName = " + realFilterName);
    }

    /**
     * Get Unique attribute of res.<br>
     * 
     * @param resType resource type
     * @return unique attribute of res
     * @since SDNO 0.5
     */
    public String getUniqueAttr(String resType) {
        String attrName = getResUniqueIndexMap().get(resType);
        if(StringUtils.isEmpty(attrName)) {
            throw new IllegalStateException("The resource without unique index: " + resType);
        }

        return attrName;
    }
}
