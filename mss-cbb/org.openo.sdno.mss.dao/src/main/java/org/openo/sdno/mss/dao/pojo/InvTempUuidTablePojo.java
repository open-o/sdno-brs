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

package org.openo.sdno.mss.dao.pojo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.mss.dao.impl.InvDataHandlerImpl;

/**
 * Temporary table for UUID. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvTempUuidTablePojo implements IInvTableCrud {

    /**
     * Table name.
     */
    private String tableName;

    /**
     * Resource type.
     */
    private String resType;

    /**
     * Table column UUID.
     */
    private String uuid;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    public InvTempUuidTablePojo() {
        // Default constructor.
    }

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType Resource type.
     */
    public InvTempUuidTablePojo(String resType) {
        Validate.notEmpty(resType);
        this.resType = resType;
        tableName = "tmp_tbl_" + StringUtils.remove(UuidUtils.createUuid(), "-");
    }

    private InvTempUuidTablePojo copy(String uuid) {
        InvTempUuidTablePojo pojo = new InvTempUuidTablePojo();
        pojo.uuid = uuid;
        pojo.resType = resType;
        pojo.tableName = tableName;
        return pojo;
    }

    public String getTableName() {
        return tableName;
    }

    public String getResType() {
        return resType;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public void createTable(SqlSession session) {
        removeTable(session);
        session.update("createUuidTempTable", this);
    }

    /**
     * Insert data to network element UUID table. <br>
     * 
     * @param session SQL session.
     * @param uuidList UUID list.
     * @since SDNO 0.5
     */
    public void addData(SqlSession session, List<String> uuidList) {
        int i = 0;
        for(String tempUuid : uuidList) {
            session.insert("addDataInUuidTempTable", copy(tempUuid));
            if(++i % InvDataHandlerImpl.BATCH_COMMIT_CNT == 0) {
                session.commit();
                session.clearCache();
            }
        }

        session.commit();
        session.clearCache();
    }

    @Override
    public void removeTable(SqlSession session) {
        session.update("removeTable", tableName);
    }
}
