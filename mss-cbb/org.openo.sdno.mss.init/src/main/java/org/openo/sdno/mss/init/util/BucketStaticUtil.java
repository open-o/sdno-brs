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

package org.openo.sdno.mss.init.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bucket static information management class, maintain paths of its configure files and the DB
 * name.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public final class BucketStaticUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketStaticUtil.class);

    private static String appRootPath = "";

    private static final String BUCKET_ABS_DIR = "bucket";

    private static final String BUCKET_ELEMENT_DIR_NAME = "elements";

    private static final String CHANGE_SET_DIR = "changesets";

    private static final String BUCKET_RELATION_DIR_NAME = "relations";

    private static final String CHANGE_LOG_NAME = "changelog.xml";

    private static final String PROPERTIES_FILE_NAME = "bucket.properties";

    private static final String BUCKET_DB_NAME = "bucketsys";

    private BucketStaticUtil() {

    }

    /**
     * @return Returns the bucketAbsDir.
     */
    public static String getBucketAbsDir() {
        return BUCKET_ABS_DIR;
    }

    /**
     * @return Returns the bucketElementDirName.
     */
    public static String getBucketElementDirName() {
        return BUCKET_ELEMENT_DIR_NAME;
    }

    /**
     * @return Returns the changeSetDir.
     */
    public static String getChangeSetDir() {
        return CHANGE_SET_DIR;
    }

    /**
     * @return Returns the bucketRelationDirName.
     */
    public static String getBucketRelationDirName() {
        return BUCKET_RELATION_DIR_NAME;
    }

    /**
     * @return Returns the changeLogName.
     */
    public static String getChangeLogName() {
        return CHANGE_LOG_NAME;
    }

    /**
     * @return Returns the propertiesFileName.
     */
    public static String getPropertiesFileName() {
        return PROPERTIES_FILE_NAME;
    }

    /**
     * @return Returns the bucketDbName.
     */
    public static String getBucketDbName() {
        return BUCKET_DB_NAME;
    }

    /**
     * @return Bucket PropFile Path.
     * @since SDNO 0.5
     */
    public static String getBucketPropFilePath() {
        return getBucketRootPath() + File.separator + PROPERTIES_FILE_NAME;
    }

    /**
     * @return Bucket Change Log DirPath.
     * @since SDNO 0.5
     */
    public static String getBucketChangeLogDirPath() {
        return getBucketRootPath() + File.separator + CHANGE_SET_DIR;
    }

    /**
     * @return Bucket Root Path.
     * @since SDNO 0.5
     */
    public static String getBucketRootPath() {
        return getAppRootPath() + File.separator + BUCKET_ABS_DIR;
    }

    private static String getAppRootPath() {
        return appRootPath;
    }

    /**
     * @param rootPath the root path of bucket file.
     * @since SDNO 0.5
     */
    public static void setAppRootPath(String rootPath) {
        File file = new File(rootPath);

        try {
            appRootPath = file.getCanonicalPath();
        } catch(IOException e) {
            LOGGER.error("get the appRootPath failed", e);
        }

    }

}
