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

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.model.liquibasemodel.Column;
import org.openo.sdno.model.liquibasemodel.Constraints;
import org.openo.sdno.model.liquibasemodel.CreateTable;
import org.openo.sdno.model.liquibasemodel.DatabaseChangeLog.ChangeSet.PreConditions;
import org.openo.sdno.model.liquibasemodel.DropTable;
import org.openo.sdno.model.liquibasemodel.Not;
import org.openo.sdno.model.liquibasemodel.ObjectFactory;
import org.openo.sdno.model.liquibasemodel.OnChangeSetPreconditionErrorOrFail;
import org.openo.sdno.model.liquibasemodel.Rollback;
import org.openo.sdno.model.liquibasemodel.TableExists;
import org.openo.sdno.mss.init.util.ValidUtil;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Basic;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle the infomodel. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class InfoModelProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoModelProcess.class);

    private InfoModelProcess() {

    }

    /**
     * Generate the precondition of information model. <br>
     * 
     * @param factory object factory
     * @param tableName name of table
     * @return precondition object
     * @since SDNO 0.5
     */
    public static PreConditions getPreCondChangeSet(ObjectFactory factory, String tableName) {

        PreConditions preCondChangeSet = factory.createDatabaseChangeLogChangeSetPreConditions();
        preCondChangeSet.setOnFail(OnChangeSetPreconditionErrorOrFail.fromValue("MARK_RAN"));

        TableExists tableExists = factory.createTableExists();
        tableExists.setTableName(tableName);
        tableExists.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());

        Not preCondNot = factory.createNot();
        preCondNot.getPreConditionChildren().add(tableExists);

        preCondChangeSet.getPreConditionChildren().add(preCondNot);
        return preCondChangeSet;
    }

    /**
     * Create basic information table. <br>
     * 
     * @param factory object factory
     * @param tableName name of table
     * @param infoModel infoModel object
     * @return basic info table class
     * @since SDNO 0.5
     */
    public static Object getBasicTable(ObjectFactory factory, String tableName, Infomodel infoModel) {
        Basic infoBasic = infoModel.getBasic();
        List<Property> properties = infoBasic.getProperty();
        CreateTable basicTable = factory.createCreateTable();
        basicTable.setTableName(tableName);
        basicTable.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());
        for(Property property : properties) {
            Column propertyColumn = factory.createColumn();
            propertyColumn.setName(property.getName());
            propertyColumn.setType(getColunmType(property.getType(), property.getLength(), property.getScale()));

            if("uuid".equalsIgnoreCase(property.getName())) {
                Constraints primarykey = factory.createConstraints();
                primarykey.setPrimaryKey("true");
                propertyColumn.getContent().add(primarykey);
            }
            basicTable.getColumn().add(propertyColumn);
        }

        return basicTable;
    }

    /**
     * get the data type string from DB. <br>
     * 
     * @param type data type
     * @param length length of type string
     * @param scale scale of BigInteger object
     * @return string of data type
     * @since SDNO 0.5
     */
    private static String getColunmType(Datatype type, BigInteger length, BigInteger scale) {
        if(type.value().equalsIgnoreCase(Datatype.INTEGER.value())) {
            return "int";
        } else if(type.value().equalsIgnoreCase(Datatype.STRING.value())) {
            return "varchar(" + length + ")";
        } else if(type.value().equalsIgnoreCase(Datatype.DECIMAL.value())) {
            StringBuilder sb = new StringBuilder();
            sb.append("decimal");

            buildPrecision(sb, length, scale);
            return sb.toString();
        } else if(type.value().equalsIgnoreCase(Datatype.DATETIME.value())) {
            return "int";
        } else if(type.value().equalsIgnoreCase(Datatype.FLOAT.value())) {
            StringBuilder sb = new StringBuilder();
            sb.append("float");
            buildPrecision(sb, length, scale);

            return sb.toString();
        } else if(type.value().equalsIgnoreCase(Datatype.DOUBLE.value())) {
            return "double";
        }
        LOGGER.warn("Not support Datatype " + type.value());
        return null;
    }

    private static void buildPrecision(StringBuilder sb, BigInteger length, BigInteger scale) {
        BigInteger len = length;
        BigInteger scales = scale;
        if(null == length) {
            len = BigInteger.TEN;
        }
        if(null == scale) {
            scales = BigInteger.ZERO;
        }

        if(ValidUtil.checkPropertyPrecision(len.intValue(), scales.intValue())) {
            sb.append('(').append(len).append(',').append(scales).append(')');
        }
    }

    /**
     * generate the roll back object. <br>
     * 
     * @param factory factory class
     * @param tableName name of table
     * @return RollBack object
     * @since SDNO 0.5
     */
    public static Object getRollBack(ObjectFactory factory, String tableName) {

        Rollback rollBack = factory.createRollback();
        DropTable dropTable = factory.createDropTable();
        dropTable.setTableName(tableName);
        dropTable.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());
        rollBack.getContent().add(dropTable);
        return rollBack;
    }

    /**
     * Generate the extend table. <br>
     * 
     * @param factory object factory
     * @param tableNameEx name of extend table
     * @return extend table name
     * @since SDNO 0.5
     */
    public static Object getExtensionTable(ObjectFactory factory, String tableNameEx) {

        CreateTable extensionTable = factory.createCreateTable();
        extensionTable.setTableName(tableNameEx);
        extensionTable.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());

        String[] exColumns = PropertiesUtil.getInstance().getExtensionTableColumn();

        for(int i = 0; i < exColumns.length; i++) {
            Column columnEx = factory.createColumn();
            String[] columnStr = exColumns[i].split(",");
            columnEx.setName(columnStr[0]);

            BigInteger length = null;
            if(columnStr.length > 2 && !StringUtils.isEmpty(columnStr[2])) {
                length = new BigInteger(columnStr[2]);
            }
            columnEx.setType(getColunmType(Datatype.fromValue(columnStr[1]), length, BigInteger.ZERO));
            extensionTable.getColumn().add(columnEx);
        }
        return extensionTable;
    }

    /**
     * Create relation table <br>
     * 
     * @param factory object factory
     * @param tableName name of table
     * @return relation table
     * @since SDNO 0.5
     */
    public static Object getRelationTable(ObjectFactory factory, String tableName) {
        CreateTable relationTable = factory.createCreateTable();
        relationTable.setTableName(tableName);
        relationTable.setSchemaName(PropertiesUtil.getInstance().getDEFAULTSCHEMA());

        String[] relationTableCols = PropertiesUtil.getInstance().getRELATIONTABLECOLUMN();
        for(int i = 0; i < relationTableCols.length; i++) {
            Column relationColumn = factory.createColumn();
            String[] columnStr = relationTableCols[i].split(",");
            relationColumn.setName(columnStr[0]);

            BigInteger length = null;
            if(columnStr.length > 2 && !StringUtils.isEmpty(columnStr[2])) {
                length = new BigInteger(columnStr[2]);
            }
            relationColumn.setType(getColunmType(Datatype.fromValue(columnStr[1]), length, BigInteger.ZERO));
            Constraints notNullkey = factory.createConstraints();
            notNullkey.setNullable("false");
            relationColumn.getContent().add(notNullkey);
            relationTable.getColumn().add(relationColumn);
        }
        return relationTable;
    }
}
