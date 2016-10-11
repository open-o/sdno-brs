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

package org.openo.sdno.mss.init.buckets;

import java.io.File;
import java.sql.SQLException;

import org.openo.sdno.framework.container.util.Bucket;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.init.dbinfo.DBParam;
import org.openo.sdno.mss.init.modelprocess.ModelProcessor;
import org.openo.sdno.mss.init.mybatis.MybatisManagement;
import org.openo.sdno.mss.init.mybatis.handler.BucketHandler;
import org.openo.sdno.mss.init.mybatis.handler.HandlerManagement;
import org.openo.sdno.mss.init.mybatis.handler.RelationHandler;
import org.openo.sdno.mss.init.mybatis.handler.ResourceHandler;
import org.openo.sdno.mss.init.mybatis.pojo.BucketPojo;
import org.openo.sdno.mss.init.util.AuthorityUtil;
import org.openo.sdno.mss.init.util.BucketStaticUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liquibase.exception.LiquibaseException;

/**
 * Bucket management class,run as singleton model.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class BucketsMgrt {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketsMgrt.class);

    private static final BucketsMgrt INSTANCE = new BucketsMgrt();

    private Bucket bucket = null;

    private DBParam dbParam = null;

    private BucketsMgrt() {
    }

    /**
     * Get singleton instance of bucket.<br>
     * 
     * @since SDNO 0.5
     */
    public static BucketsMgrt getInstance() {
        return INSTANCE;
    }

    public void setDbParam(DBParam dbParam) {
        this.dbParam = dbParam;
    }

    /**
     * To check whether the current application support Bucket management.<br>
     * 
     * @since SDNO 0.5
     */
    public boolean isBucketSupport() {
        File file = new File(getBucketInfoPath());

        if(file.exists() && file.isFile()) {
            return true;
        }

        return false;
    }

    /**
     * Initialization method of Bucket.<br>
     * 
     * @throws SQLException
     * @throws LiquibaseException
     * @throws CloneNotSupportedException
     * @since SDNO 0.5
     */
    public void init() throws LiquibaseException, SQLException, CloneNotSupportedException {
        try {
            LOGGER.info("init bucket.");

            initBucket();

            ModelProcessor modelProcessor = new ModelProcessor(this.dbParam.clone());

            MybatisManagement.getInstance().setDbParam(this.dbParam);

            if(!createTables()) {
                LOGGER.error("create bucketsys tables failed");
                throw new SQLException("create bucketsys tables failed");
            }

            doInit();
            modelProcessor.loadModel(this.bucket.getName(), BucketStaticUtil.getBucketRootPath());

        } finally {
            this.dbParam.destroyPassword();
        }
    }

    private BucketPojo getBucketPojo(Bucket bucket) {
        BucketPojo pojo = new BucketPojo();

        pojo.setName(bucket.getName());
        pojo.setOwner(bucket.getOwner());
        pojo.setAuthorization(bucket.getAuthorization());
        pojo.setAllowread(JsonUtil.toJson(bucket.getAllowread()));

        return pojo;
    }

    private void doInit() {
        BucketHandler bucketHandler = HandlerManagement.getInstance().getBucketHandler();

        bucketHandler.addBucket(getBucketPojo(this.bucket));
    }

    private void initBucket() {
        this.bucket = JsonUtil.fromJson(new File(getBucketInfoPath()), Bucket.class);
        if(!AuthorityUtil.isValidAuth(this.bucket.getAuthorization())) {
            LOGGER.error("authorization:{} of bucket:{} is invalid, now normalize it", this.bucket.getName(),
                    this.bucket.getAuthorization());
            this.bucket.setAuthorization(AuthorityUtil.normalizeAuth(this.bucket.getAuthorization()));
        }
    }

    private boolean createTables() {
        BucketHandler bucketHandler = HandlerManagement.getInstance().getBucketHandler();
        bucketHandler.createTable();

        RelationHandler relationHandler = HandlerManagement.getInstance().getRelationHandler();
        relationHandler.createTable();

        ResourceHandler resourceHandler = HandlerManagement.getInstance().getResourceHandler();
        resourceHandler.createTable();

        return true;
    }

    private String getBucketInfoPath() {
        return BucketStaticUtil.getBucketRootPath() + File.separator + "mss-bucket-define.json";
    }
}
