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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File handle class, used to get file path and the file list. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {

    }

    /**
     * Get all the files in the given directory. <br>
     * 
     * @param dirPath the directory path you want to search.
     * @return List of all files in the given directory.
     * @since SDNO 0.5
     */
    public static List<File> getDirFileList(String dirPath) {
        if(StringUtils.isEmpty(dirPath)) {
            LOGGER.error("input path is Empty");
            throw new IllegalArgumentException("input path is Empty");
        }

        List<File> files = new ArrayList<File>();

        File rootDir = new File(dirPath);

        if(rootDir.exists()) {
            for(File file : rootDir.listFiles()) {
                files.add(file);
            }

        }

        return files;
    }
}
