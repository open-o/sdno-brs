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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openo.sdno.framework.container.util.DefaultEnvUtil;
import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog;
import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog.ChangeSet;
import org.openo.sdno.model.liquibasemodel.ObjectFactory;
import org.openo.sdno.mss.init.util.BucketStaticUtil;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelIndex;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Extension;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

/**
 * Generate the database log when changing the set. <br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public abstract class ChangeLogBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLogBuilder.class);

    protected ObjectFactory factory = new ObjectFactory();

    protected DatabaseChangeLog dbChangeLog = factory.createDatabaseChangeLog();

    protected long curTime = System.currentTimeMillis();

    protected volatile Connection conn = null;

    protected int changeSetIndex = 0;

    /**
     * Add the name space of the DB change set.
     * 
     * @since SDNO 0.5
     */
    protected void addDbChangeSetNameSpace() {
        QName nsxsi = new QName("xmlns:xsi");
        QName nsext = new QName("xmlns:ext");
        QName xsischem = new QName("xsi:schemaLocation");

        dbChangeLog.getOtherAttributes().put(nsxsi, "http://www.w3.org/2001/XMLSchema-instance");
        dbChangeLog.getOtherAttributes().put(nsext, "http://www.liquibase.org/xml/ns/dbchangelog-ext");
        dbChangeLog.getOtherAttributes().put(xsischem,
                "http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.0.xsd"
                        + " http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd");
    }

    /**
     * Generate the change set of the information model.<br>
     * 
     * @param keyStr Type of the resource.
     * @param infoModel information model of the type.
     * @since SDNO 0.5
     */
    protected void generateInfoModelChangeSet(String keyStr, Infomodel infoModel) {
        if(null == infoModel) {
            LOGGER.error("infoModel is null");
            return;
        }

        generateBasicChangeSet(keyStr, infoModel);

        generateExtendChangeSet(keyStr, infoModel);

        generateExtendIndex(keyStr);

        generateRelationChangeSet(keyStr);

        generateRelationIndexs(keyStr);

    }

    private void generateBasicChangeSet(String keyStr, Infomodel infoModel) {
        DatabaseChangeLog.ChangeSet changeSet = createChangeSetInfo();

        changeSet.setPreConditions(InfoModelProcess.getPreCondChangeSet(factory, getBasicTableName(keyStr)));

        changeSet.getChangeSetChildren()
                .add(InfoModelProcess.getBasicTable(factory, getBasicTableName(keyStr), infoModel));

        changeSet.getChangeSetChildren().add(InfoModelProcess.getRollBack(factory, getBasicTableName(keyStr)));

        dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(changeSet);
    }

    private String getBasicTableName(String keyStr) {
        return PropertiesUtil.getInstance().getINVTABLEPREFIX() + keyStr;
    }

    private void generateExtendChangeSet(String keyStr, Infomodel infoModel) {
        Extension infoExtension = infoModel.getExtension();
        if(null != infoExtension) {

            String tableNameEx = getExtendTableName(keyStr);

            DatabaseChangeLog.ChangeSet changeSetEx = createChangeSetInfo();

            changeSetEx.setPreConditions(InfoModelProcess.getPreCondChangeSet(factory, tableNameEx));

            changeSetEx.getChangeSetChildren().add(InfoModelProcess.getExtensionTable(factory, tableNameEx));

            changeSetEx.getChangeSetChildren().add(InfoModelProcess.getRollBack(factory, tableNameEx));

            dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(changeSetEx);
        }
    }

    private String getExtendTableName(String keyStr) {
        return PropertiesUtil.getInstance().getINVTABLEPREFIX() + keyStr
                + PropertiesUtil.getInstance().getEXTENSIONTABLEPOSTFIX();
    }

    private void generateExtendIndex(String keyStr) {
        String[] extendIndexs = PropertiesUtil.getInstance().getEXTENDINDEXS();
        for(int i = 0; i < extendIndexs.length; i++) {
            String[] indexInfo = extendIndexs[i].split(",");
            DatabaseChangeLog.ChangeSet indexChangeSet = createChangeSetInfo();

            String tableName = getExtendTableName(keyStr);
            String indexName = indexInfo[0] + keyStr;

            indexChangeSet.setPreConditions(DataModelProcess.getPrecondIndex(factory, indexName, tableName));

            indexChangeSet.getChangeSetChildren()
                    .add(DataModelProcess.getCreateCustomizedIndex(factory, indexInfo, tableName, indexName));

            indexChangeSet.getChangeSetChildren().add(DataModelProcess.getRollBackIndex(factory, indexName, tableName));

            dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(indexChangeSet);
        }
    }

    private String getRelationTableName(String keyStr) {
        return PropertiesUtil.getInstance().getINVTABLEPREFIX() + keyStr
                + PropertiesUtil.getInstance().getRELATIONTABLEPOSTFIX();
    }

    private void generateRelationChangeSet(String keyStr) {

        DatabaseChangeLog.ChangeSet changeSet = createChangeSetInfo();

        changeSet.setPreConditions(InfoModelProcess.getPreCondChangeSet(factory, getRelationTableName(keyStr)));

        changeSet.getChangeSetChildren().add(InfoModelProcess.getRelationTable(factory, getRelationTableName(keyStr)));

        changeSet.getChangeSetChildren().add(InfoModelProcess.getRollBack(factory, getRelationTableName(keyStr)));

        dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(changeSet);
    }

    private void generateRelationIndexs(String keyStr) {

        String[] relationIndexs = PropertiesUtil.getInstance().getRELATIONINDEXS();
        for(int i = 0; i < relationIndexs.length; i++) {
            String[] indexInfo = relationIndexs[i].split(",");
            DatabaseChangeLog.ChangeSet indexChangeSet = createChangeSetInfo();

            String indexName = indexInfo[0] + keyStr;

            indexChangeSet.setPreConditions(
                    DataModelProcess.getPrecondIndex(factory, indexName, getRelationTableName(keyStr)));
            indexChangeSet.getChangeSetChildren().add(DataModelProcess.getCreateCustomizedIndex(factory, indexInfo,
                    getRelationTableName(keyStr), indexName));

            indexChangeSet.getChangeSetChildren()
                    .add(DataModelProcess.getRollBackIndex(factory, indexName, getRelationTableName(keyStr)));

            dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(indexChangeSet);
        }
    }

    /**
     * Generate the information of the change set.<br>
     * 
     * @return the detail of the info, including time and index.
     * @since SDNO 0.5
     */
    protected ChangeSet createChangeSetInfo() {
        changeSetIndex += 1;

        DatabaseChangeLog.ChangeSet changeSet = factory.createDatabaseChangeLogChangeSet();
        changeSet.setId(curTime + "_" + changeSetIndex);
        changeSet.setAuthor(PropertiesUtil.getInstance().getCHANGESETAUTHOR());

        return changeSet;
    }

    /**
     * Generate the change set of the data model.<br>
     * 
     * @param keyStr resource type.
     * @param dataModel data model to change.
     * @param dataName2InfoName the mapping from data model to info model.
     * @since SDNO 0.5
     */
    protected void generateDataModelChangeSet(String keyStr, Datamodel dataModel,
            Map<String, String> dataName2InfoName) {
        if(null == dataModel) {
            return;
        }
        List<DataModelIndex> indexs = dataModel.getIndex();
        for(DataModelIndex index : indexs) {
            buildIndexChangeSetForSql(index, getBasicTableName(dataName2InfoName.get(dataModel.getName())));
        }

    }

    private void buildIndexChangeSetForSql(DataModelIndex index, String tableName) {
        DatabaseChangeLog.ChangeSet indexChangeSet = createChangeSetInfo();

        indexChangeSet.setPreConditions(DataModelProcess.getPrecondIndex(factory, index.getName(), tableName));

        indexChangeSet.getChangeSetChildren().add(DataModelProcess.getCreateIndex(factory, index, tableName));

        indexChangeSet.getChangeSetChildren()
                .add(DataModelProcess.getRollBackIndex(factory, index.getName(), tableName));

        dbChangeLog.getChangeSetOrIncludeOrIncludeAll().add(indexChangeSet);
    }

    /**
     * Save the change set to a XML file.<br>
     * 
     * @throws LiquibaseException if getting a Liquibase object is invalid.
     * @throws SQLException if SQL syntax is wrong.
     * @since SDNO 0.5
     */
    protected void saveChangeLog2File() throws LiquibaseException, SQLException {
        OutputStream outputStream = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DatabaseChangeLog.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            File fileDirPath = new File(getBucketChangeLogDirPath());
            if(!fileDirPath.exists() && !fileDirPath.mkdirs()) {
                LOGGER.error("Create direcoty failed, init failed, path = " + fileDirPath);
                return;
            }

            bakCurrChangeLogFile();

            String filePath = getBucketChangeLogDirPath() + File.separator + BucketStaticUtil.getChangeLogName();
            outputStream = new FileOutputStream(new File(filePath));
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(dbChangeLog, outputStream);
            outputStream.flush();

            effectChangeLog2Db(filePath);
        } catch(JAXBException | IOException e) {
            LOGGER.error("Exception while save ChangeSet Log file. e: ", e);
        } finally {
            if(null != outputStream) {
                try {
                    outputStream.close();
                } catch(IOException e) {
                    LOGGER.error("exception while close stream, e: ", e);
                }
            }
        }
    }

    /**
     * Get the directory path of change.<br>
     * 
     * @return the directory path of change.
     * @since SDNO 0.5
     */
    protected String getBucketChangeLogDirPath() {
        String varShareDir = DefaultEnvUtil.getAppShareDir();
        if(StringUtils.isEmpty(varShareDir) || !varShareDir.startsWith("/var/share/oss/")) {
            return BucketStaticUtil.getBucketChangeLogDirPath();
        }

        return varShareDir + File.separator + "changesets" + File.separator;
    }

    private void bakCurrChangeLogFile() {
        String oldFilePath = getBucketChangeLogDirPath() + File.separator + BucketStaticUtil.getChangeLogName();

        String newFilePath = getBucketChangeLogDirPath() + File.separator + "history" + File.separator
                + System.currentTimeMillis() + "_" + BucketStaticUtil.getChangeLogName();

        File oldFile = new File(oldFilePath);

        if(oldFile.exists()) {
            try {
                FileUtils.moveFile(oldFile, new File(newFilePath));
            } catch(IOException e) {
                LOGGER.error("Exception while bak old changeset file. e: ", e);
            }
        }
    }

    private void effectChangeLog2Db(String filePath) throws LiquibaseException, SQLException {
        if(StringUtils.isEmpty(filePath)) {
            LOGGER.error("filePath is Empty");
            return;
        }

        DatabaseConnection dbConn = null;

        try {
            dbConn = getDBConnection();
            if(null != dbConn) {
                Liquibase liquibase = new Liquibase(filePath, new FileSystemResourceAccessor(), dbConn);
                liquibase.update(null);
            }
        } finally {
            closeConnection(dbConn);
        }
    }

    private void closeConnection(DatabaseConnection dbConn) {
        try {
            if(null != dbConn) {
                dbConn.close();
            }
        } catch(DatabaseException e) {
            LOGGER.error("Exception while close dbConnection", e);
        }
    }

    protected abstract DatabaseConnection getDBConnection() throws SQLException;

}
