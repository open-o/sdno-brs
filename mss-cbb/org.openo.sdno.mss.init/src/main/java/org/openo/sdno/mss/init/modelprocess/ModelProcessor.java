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

import java.sql.SQLException;

import org.openo.sdno.mss.init.buckets.BucketModel;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.exception.LiquibaseException;

/**
 * Class to modify or add a new model. <br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class ModelProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProcessor.class);

    ModelData modelData = null;

    private DBParam dbParam = null;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param dbParam database parameter.
     */
    public ModelProcessor(DBParam dbParam) {
        super();
        this.dbParam = dbParam;
    }

    /**
     * Load model from file or DB. <br>
     * 
     * @param bktName model file name
     * @param modelBasicPath path to the file
     * @since SDNO 0.5
     */
    public void loadModel(String bktName, String modelBasicPath) throws LiquibaseException, SQLException {
        try {
            LOGGER.info("Start to init the models.......");
            BucketModel bktModel = new BucketModel(modelBasicPath);
            bktModel.loadBucketModelFiles();
            this.modelData = new ModelData(bktModel, bktName);
            this.modelData.init();
            ModelIniter initer = new ModelIniter(this.modelData, this.dbParam);
            initer.init();
        } finally {

            this.dbParam.destroyPassword();
        }
    }
}
