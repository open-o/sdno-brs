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
 * Temporary table for network element. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvTempDevUuidTablePojo implements IInvTableCrud {

    /**
     * Table name.
     */
    private final String tableName;

    /**
     * Device UUID
     */
    private String devUuid;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param resType resource type.
     */
    public InvTempDevUuidTablePojo(String resType) {
        Validate.notEmpty(resType);
        tableName = "tmp_tbl_" + StringUtils.remove(UuidUtils.createUuid(), "-");
    }

    /**
     * Build value. <br>
     * 
     * @param devUuid Device UUID.
     * @return this.
     * @since SDNO 0.5
     */
    public InvTempDevUuidTablePojo buildValue(String devUuid) {
        this.devUuid = devUuid;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * Get device UUID. <br>
     * 
     * @return device UUID.
     * @since SDNO 0.5
     */
    public String getDevId() {
        return devUuid;
    }

    @Override
    public void createTable(SqlSession session) {
        removeTable(session);
        session.update("createNeIdTempTable", this);
    }

    /**
     * Insert data to network element ID temporary table. <br>
     * 
     * @param session SQL session.
     * @param devUuidList Device UUID list.
     * @since SDNO 0.5
     */
    public void addData(SqlSession session, List<String> devUuidList) {
        int i = 0;
        for(String tempDevUuid : devUuidList) {
            session.insert("addDataInNeIdTemp", buildValue(tempDevUuid));
            if(++i % InvDataHandlerImpl.BATCH_COMMIT_CNT == 0) {
                session.commit();
                session.clearCache();
            }
        }
    }

    @Override
    public void removeTable(SqlSession session) {
        session.update("removeTable", tableName);
    }
}
