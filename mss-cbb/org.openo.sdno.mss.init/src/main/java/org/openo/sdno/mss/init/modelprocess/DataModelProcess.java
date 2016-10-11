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

import java.io.Serializable;

import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog.ChangeSet.PreConditions;
import org.openo.sdno.model.liquibasemodel.DropIndex;
import org.openo.sdno.model.liquibasemodel.ObjectFactory;
import org.openo.sdno.model.liquibasemodel.OnChangeSetPreconditionErrorOrFail;
import org.openo.sdno.model.liquibasemodel.Rollback;
import org.openo.sdno.model.liquibasemodel.Sql;
import org.openo.sdno.model.liquibasemodel.SqlCheck;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelIndex;

/**
 * Data model processing class, it can create index of data model. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class DataModelProcess {

    private DataModelProcess() {

    }

    /**
     * Generate the precondition of index. <br>
     * 
     * @param factory factory class of object.
     * @param indexName name of the index.
     * @param tableName name of the table.
     * @return precondition of the index.
     * @since SDNO 0.5
     */
    public static PreConditions getPrecondIndex(ObjectFactory factory, String indexName, String tableName) {
        PreConditions preCondIndex = factory.createDatabaseChangeLogChangeSetPreConditions();
        preCondIndex.setOnFail(OnChangeSetPreconditionErrorOrFail.fromValue("MARK_RAN"));

        SqlCheck sqlcheck = factory.createSqlCheck();
        String sqlStr =
                "SELECT COUNT(*) FROM information_schema.statistics WHERE TABLE_SCHEMA = DATABASE() and table_name = "
                        + "'" + tableName + "'" + " AND index_name = " + "'" + indexName + "'>0";

        sqlcheck.setContent(sqlStr);
        sqlcheck.setExpectedResult("0");

        preCondIndex.getPreConditionChildren().add(sqlcheck);
        return preCondIndex;
    }

    /**
     * Get the created index.<br>
     * 
     * @param factory factory class of object.
     * @param index the index class
     * @param tableName name of the table.
     * @return sqlInstance instance of SQL.
     * @since SDNO 0.5
     */
    public static Object getCreateIndex(ObjectFactory factory, DataModelIndex index, String tableName) {
        Sql sqlInstance = getSqlInstance(factory);
        sqlInstance.getContent().add(getCreateIndexSql(index, tableName));
        return sqlInstance;
    }

    private static Serializable getCreateIndexSql(DataModelIndex index, String tableName) {
        return "create " + (index.isIsunique() ? "unique" : "") + " index " + index.getName() + " on " + tableName + "("
                + index.getValue() + ")";
    }

    /**
     * Construct the log of changing index.<br>
     * 
     * @param factory factory class of object.
     * @param indexInfo information of the index.
     * @param tableName name of the table.
     * @param indexName name of the index.
     * @return sqlInstance instance of SQL.
     * @since SDNO 0.5
     */
    public static Object getCreateCustomizedIndex(ObjectFactory factory, String[] indexInfo, String tableName,
            String indexName) {
        Sql sqlInstance = getSqlInstance(factory);

        String uniqueSqlStr = "";

        if(indexInfo.length == 3) {
            String uniqueStr = indexInfo[2];
            if("true".equalsIgnoreCase(uniqueStr)) {
                uniqueSqlStr = "unique";
            }
        }

        String columns = indexInfo[1].replaceAll("-", ",");

        String sqlStr = "create " + uniqueSqlStr + " index " + indexName + " on " + tableName + "(" + columns + ")";
        sqlInstance.getContent().add(sqlStr);

        return sqlInstance;
    }

    private static Sql getSqlInstance(ObjectFactory factory) {
        Sql sqlInstance = factory.createSql();
        sqlInstance.setDbms("mssql,sybase,mysql,oracle");
        sqlInstance.setSplitStatements("true");
        sqlInstance.setStripComments("true");
        return sqlInstance;
    }

    /**
     * Generating log of the rollback. <br>
     * 
     * @param factory factory class of object.
     * @param name name of the object.
     * @param tableName name of the table.
     * @return roolBack object of rollback.
     * @since SDNO 0.5
     */
    public static Object getRollBackIndex(ObjectFactory factory, String name, String tableName) {
        DropIndex dropIndex = factory.createDropIndex();
        dropIndex.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());
        dropIndex.setTableName(tableName);
        dropIndex.setIndexName(name);
        Rollback rollBack = factory.createRollback();
        rollBack.getContent().add(dropIndex);
        return rollBack;
    }

}
