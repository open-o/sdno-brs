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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * BucketStatic utility test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-27
 */
public class BucketStaticUtilTest {

    static String appRootPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        appRootPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
                + "resources";

        BucketStaticUtil.setAppRootPath(appRootPath);

    }

    @Test
    public void testGetBucketAbsDir() {
        assertEquals("bucket", BucketStaticUtil.getBucketAbsDir());
    }

    @Test
    public void testGetBucketElementDirName() {
        assertEquals("elements", BucketStaticUtil.getBucketElementDirName());
    }

    @Test
    public void testGetChangeSetDir() {
        assertEquals("changesets", BucketStaticUtil.getChangeSetDir());
    }

    @Test
    public void testGetBucketRelationDirName() {
        assertEquals("relations", BucketStaticUtil.getBucketRelationDirName());
    }

    @Test
    public void testGetChangeLogName() {
        assertEquals("changelog.xml", BucketStaticUtil.getChangeLogName());
    }

    @Test
    public void testGetPropertiesFileName() {
        assertEquals("bucket.properties", BucketStaticUtil.getPropertiesFileName());
    }

    @Test
    public void testGetBucketDbName() {

        assertEquals("bucketsys", BucketStaticUtil.getBucketDbName());
    }

    @Test
    public void testGetBucketPropFilePath() {
        String Path = appRootPath + File.separator + "bucket" + File.separator + "bucket.properties";
        assertEquals(Path, BucketStaticUtil.getBucketPropFilePath());
    }

    @Test
    public void testGetBucketChangeLogDirPath() {

        String Path = appRootPath + File.separator + "bucket" + File.separator + "changesets";
        assertEquals(Path, BucketStaticUtil.getBucketChangeLogDirPath());
    }

    @Test
    public void testGetBucketRootPath() {

        String bucketRootPath = appRootPath + File.separator + "bucket";
        assertEquals(bucketRootPath, BucketStaticUtil.getBucketRootPath());
    }

    @Test
    public void testSetAppRootPath() {
        String bucketRootPath = appRootPath + File.separator + "bucket";
        assertEquals(bucketRootPath, BucketStaticUtil.getBucketRootPath());
    }

}
