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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.openo.sdno.mss.init.mybatis.handler.HandlerManagement;
import org.openo.sdno.mss.init.mybatis.pojo.RelationPojo;
import org.openo.sdno.mss.init.mybatis.pojo.ResourcePojo;
import org.openo.sdno.mss.init.util.UUIDUtils;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

/**
 * Tool class to initialize model, it can also update the change of model. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class ModelIniter extends ChangeLogBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelIniter.class);

    protected ModelData modelData = null;

    protected DBParam dbParam = null;

    private static final Object lock = new Object();

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param modelData
     * @param dbParam
     */
    public ModelIniter(ModelData modelData, DBParam dbParam) {
        super();
        this.modelData = modelData;
        this.dbParam = dbParam;
    }

    /**
     * Initialize the model. <br>
     * 
     * @since SDNO 0.5
     */
    public void init() throws LiquibaseException, SQLException {
        try {

            addDbChangeSetNameSpace();

            initInfoModelChangeSet();

            initDataModelChangeSet();
            LOGGER.warn("init tablels for " + this.modelData.getBktName() + " do not interruption it, waiting please.");

            saveChangeLog2File();
            LOGGER.warn("saving models for " + this.modelData.getBktName()
                    + " do not interruption it, waiting please......");
            saveModels2Db();
            LOGGER.warn("model inited, exit the process now. ");
        } finally {
            dbParam.destroyPassword();
        }
    }

    /**
     * @return Returns the modelData.
     */
    public ModelData getModelData() {
        return modelData;
    }

    /**
     * @param modelData The modelData to set.
     */
    public void setModelData(ModelData modelData) {
        this.modelData = modelData;
    }

    private void saveModels2Db() {
        saveResource2Db();

        saveRelation2Db();
    }

    private void saveResource2Db() {
        Map<String, Infomodel> infoModels = this.modelData.getInfoModelMap();
        List<ResourcePojo> resources = new ArrayList<ResourcePojo>();

        for(Map.Entry<String, Infomodel> entry : infoModels.entrySet()) {
            ResourcePojo pojo = new ResourcePojo();

            pojo.setBktName(this.modelData.getBktName());
            pojo.setUuid(UUIDUtils.createUuid());
            pojo.setDmspec(JsonUtil.toJson(this.modelData.getDataModels().get(entry.getKey())));
            pojo.setImspec(JsonUtil.toJson(entry.getValue()));
            pojo.setRestype(entry.getKey());
            resources.add(pojo);
        }

        HandlerManagement.getInstance().getResourceHandler().addResources(resources);
    }

    private void saveRelation2Db() {
        RelationPojo relation =
                HandlerManagement.getInstance().getRelationHandler().getRelation(this.modelData.getBktName());
        LOGGER.warn("relation is null, now insert the latest relations");
        relation = new RelationPojo();
        relation.setBktName(this.modelData.getBktName());
        relation.setRmspec(JsonUtil.toJson(this.modelData.getRelationModels()));
        HandlerManagement.getInstance().getRelationHandler().addRelation(relation);
    }

    private void initInfoModelChangeSet() {
        Map<String, Infomodel> infoModels = this.modelData.getWholeInfoModels();

        for(Map.Entry<String, Infomodel> entry : infoModels.entrySet()) {
            Infomodel infoModel = infoModels.get(entry.getKey());

            generateInfoModelChangeSet(entry.getKey(), infoModel);
        }
    }

    private void initDataModelChangeSet() {
        Map<String, Datamodel> dataModels = this.modelData.getDataModels();

        for(Map.Entry<String, Datamodel> entry : dataModels.entrySet()) {
            Datamodel dataModel = dataModels.get(entry.getKey());
            generateDataModelChangeSet(entry.getKey(), dataModel, this.modelData.getDataName2InfoNames());
        }
    }

    /**
     * @return The DB connection.
     * @throws SQLException if the SQL syntax is wrong.
     * @since SDNO 0.5
     */
    @Override
    protected DatabaseConnection getDBConnection() throws SQLException {

        if("mysql".equalsIgnoreCase(dbParam.getDbType())) {
            return new JdbcConnection(getConnection());
        }
        LOGGER.error("Unknown databse type: " + dbParam.getDbType());
        return null;
    }

    /**
     * @return The DB connection built from DbPara.
     * @throws SQLException if the SQL syntax is wrong.
     * @since SDNO 0.5
     */
    protected Connection getConnection() throws SQLException {
        if(null == this.conn) {
            synchronized(lock) {
                if(null == this.conn) {
                    this.dbParam.setDbName(modelData.getBktName());
                    this.conn = DriverManager.getConnection(dbParam.getUrl(), dbParam.getDbUser(),
                            String.valueOf(dbParam.getDbPwd()));
                }
            }
        }
        return this.conn;
    }
}
