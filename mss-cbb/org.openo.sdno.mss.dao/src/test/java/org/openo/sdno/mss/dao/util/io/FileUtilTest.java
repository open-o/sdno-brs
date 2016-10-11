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

package org.openo.sdno.mss.dao.util.io;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openo.sdno.framework.container.util.DefaultEnvUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * FileUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class FileUtilTest {

    private static final String PATH1 = "src/test/java/org/openo/sdno/mss/dao/util/testfile/TestFile.json";

    private static final String PATH2 = "src/test/java/org/openo/sdno/mss/dao/util/io/testfile";

    @Test
    public void testGetUserDirFile() {
        File fileResult = FileUtil.getUserDirFile(PATH1);
        File file = new File(DefaultEnvUtil.getAppShareDir() + File.separator + PATH1);
        assertTrue(file.equals(fileResult));
    }

    @Test
    public void testGetAbsoluteDirFile() {
        File fileResult = FileUtil.getAbsoluteDirFile(PATH1);
        File file = new File(PATH1);
        assertTrue(file.equals(fileResult));
    }

    @Test
    public void testDeleteOverdueFile() throws IOException {
        new MockUp<FileUtil>() {

            @Mock
            boolean isOverdue(long fileLastModified, long overdueMills, long curTimeMills) {
                return true;
            }
        };
        new File(PATH2).mkdir();
        File fileTemp = new File(PATH2 + "/TestIOFile.json");
        fileTemp.createNewFile();
        FileUtil.deleteOverdueFile(fileTemp);
        assertTrue(!fileTemp.exists());
        File fileTemp1 = new File(PATH2);
        FileUtils.forceDelete(fileTemp1);
    }

    @Test
    public void testDeleteOverdueFileNotExist() throws IOException {
        new MockUp<FileUtil>() {

            @Mock
            boolean isOverdue(long fileLastModified, long overdueMills, long curTimeMills) {
                return true;
            }
        };
        new File(PATH2).mkdir();
        File fileTemp = new File(PATH2 + "/TestIOFile.json");
        FileUtil.deleteOverdueFile(fileTemp);
        assertTrue(!fileTemp.exists());
        File fileTemp1 = new File(PATH2);
        FileUtils.forceDelete(fileTemp1);
    }

    @Test
    public void testDeleteOverdueFileDirectory() throws IOException {
        new MockUp<FileUtil>() {

            @Mock
            boolean isOverdue(long fileLastModified, long overdueMills, long curTimeMills) {
                return true;
            }
        };
        new File(PATH2).mkdir();
        File fileTemp = new File(PATH2 + "/TestIOFile.json");
        fileTemp.createNewFile();
        File fileTemp1 = new File(PATH2);
        FileUtil.deleteOverdueFile(fileTemp1);
        assertTrue(!fileTemp.exists());
    }

    @Test
    public void testGetFileServiceFullPathFile() {
        File fileResult = FileUtil.getFileServiceFullPathFile(PATH1);
        File file = new File(PATH1);
        assertTrue(file.equals(fileResult));

    }

    @Test
    public void testCreateTmpFilePath() {
        FileUtil.createTmpFilePath(PATH2);
    }

}
