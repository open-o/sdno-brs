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

package org.openo.sdno.mss.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.model.util.ModelParserUtil;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;

/**
 * Model parser class, 1. call construct function, 2. call parser to parse the XML file, 3. get the
 * parsed result. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ModelParser {

    private List<File> models = new ArrayList<File>();

    /**
     * Construction function, use default file path.
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param models model file.
     */
    public ModelParser(List<File> models) {
        super();
        this.models = models;
    }

    public List<File> getModels() {
        return models;
    }

    public void setModels(List<File> models) {
        this.models = models;
    }

    /**
     * Specify the model. <br>
     * 
     * @since SDNO 0.5
     */
    public void parseModel() {
        ModelParserUtil.getInstance().parseModel(this.models);
    }

    /**
     * Get information model parsing result. <br>
     * 
     * @return result contain information model.
     * @since SDNO 0.5
     */
    public Map<String, Infomodel> getInfoModel() {
        return ModelParserUtil.getInstance().getInfoModels();
    }

    /**
     * Get data model parsing result. <br>
     * 
     * @return result contain data model.
     * @since SDNO 0.5
     */
    public Map<String, Datamodel> getDataModel() {
        return ModelParserUtil.getInstance().getDataModels();
    }

    /**
     * Get relation model parsing result. <br>
     * 
     * @return result contain relation model.
     * @since SDNO 0.5
     */
    public Map<String, RelationModelRelation> getRelationModel() {
        return ModelParserUtil.getInstance().getRelationModels();
    }
}
