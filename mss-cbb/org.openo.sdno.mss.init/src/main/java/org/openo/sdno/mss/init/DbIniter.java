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

package org.openo.sdno.mss.init;

import java.io.IOException;
import java.sql.SQLException;

import org.openo.sdno.mss.init.buckets.BucketsMgrt;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.openo.sdno.mss.init.util.BucketStaticUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.exception.LiquibaseException;

/**
 * Entrance for DB Initialize Jar's call.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class DbIniter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbIniter.class);

    /**
     * Initialization of database.<br>
     * 
     * @param dbParam parameter for database.
     * @throws IOException when database init failed
     * @throws CloneNotSupportedException when database init failed
     * @since SDNO 0.5
     */
    public void init(DBParam dbParam) throws LiquibaseException, SQLException, IOException, CloneNotSupportedException {
        try {
            LOGGER.warn("DBiniter is running.....");

            BucketStaticUtil.setAppRootPath("buckets/" + dbParam.getDbName());

            if(!BucketsMgrt.getInstance().isBucketSupport()) {
                LOGGER.warn("App is Not Bucket Management support, nothing to do.");
                return;
            }

            dbParam.setDbName(BucketStaticUtil.getBucketDbName());
            BucketsMgrt.getInstance().setDbParam(dbParam);
            BucketsMgrt.getInstance().init();

            LOGGER.warn("bucketsMgrt init finished");
        } finally {
            dbParam.destroyPassword();
        }
    }
}
