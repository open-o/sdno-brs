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

package org.openo.sdno.mss.dao.util;

import java.io.File;

import org.junit.Test;

/**
 * ValidateUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class ValidateUtilTest {

    @Test
    public void testCheckSize() {
        ValidateUtil.checkSize(5, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSizeMax() {
        ValidateUtil.checkSize(15, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSizeMin() {
        ValidateUtil.checkSize(0, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFileExistNotExist() {
        File file = new File("src/tests");
        ValidateUtil.checkFileExist(file);
    }

    @Test
    public void testCheckFileExist() {
        File file = new File("src/test/java/org/openo/sdno/mss/dao/util/testfile/TestFile.json");
        ValidateUtil.checkFileExist(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFileContainsNotContains() {
        File file = new File("src/test/java/org/openo/sdno/mss/dao/util/testfile/TestFile.json");
        String subDir = "src/test/java/org/openo/sdno/mss/dao/util/io";
        ValidateUtil.checkFileContains(subDir, file);
    }

    @Test
    public void testCheckFileContains() {
        File file = new File("src/test/java/org/openo/sdno/mss/dao/util/testfile/TestFile.json");
        String subDir = "src/test/java/org/openo/sdno/mss/dao/util/testfile";
        ValidateUtil.checkFileContains(subDir, file);
    }
}
