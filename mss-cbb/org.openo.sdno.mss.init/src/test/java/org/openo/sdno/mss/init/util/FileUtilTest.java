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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * File utility test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-27
 */
public class FileUtilTest {

    private static final String FILE_PATH = "src/test/java/org/openo/sdno/mss/init/util";

    @Test(expected = IllegalArgumentException.class)
    public void testGetDirFileListEmpty() {
        FileUtil.getDirFileList("");
    }

    @Test
    public void testGetDirFileListNotExists() {
        List<File> fileListResult = FileUtil.getDirFileList("src/tests");
        List<File> fileList = new ArrayList<File>();
        assertTrue(fileList.equals(fileListResult));
    }

    @Test
    public void testGetDirFileList() {
        List<File> fileListResult = FileUtil.getDirFileList(FILE_PATH + "/testfile");
        List<File> fileList = new ArrayList<File>();
        File file = new File(FILE_PATH + "/testfile/JsonUtilTestFile.json");
        fileList.add(file);
        Set<File> s = new HashSet<File>();
        s.addAll(fileList);
        Set<File> d = new HashSet<File>();
        d.addAll(fileListResult);

        assertTrue(s.equals(d));
    }

}
