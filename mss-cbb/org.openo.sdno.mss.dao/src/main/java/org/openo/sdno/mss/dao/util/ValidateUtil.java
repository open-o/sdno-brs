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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;

import org.openo.sdno.mss.dao.util.io.FileUtil;

/**
 * Validate Utility class. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public final class ValidateUtil {

    private ValidateUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Check size. <br>
     * 
     * @param size size
     * @param min minimum
     * @param max maximum
     * @since SDNO 0.5
     */
    public static void checkSize(int size, int min, int max) {
        if(size < min || size > max) {
            throw new IllegalArgumentException("Size is not correct, size = " + size + ", except size between " + min
                    + " and " + max);
        }
    }

    /**
     * Check if file is existed. <br>
     * 
     * @param file File
     * @since SDNO 0.5
     */
    public static void checkFileExist(File file) {
        Validate.notNull(file);
        if(!file.exists()) {
            throw new IllegalArgumentException("Input parameter error, file is not exist: " + file.getPath());
        }
    }

    /**
     * Check if file in the directory for safe. <br>
     * 
     * @param subDir Sub directory.
     * @param file file
     * @since SDNO 0.5
     */
    public static void checkFileContains(String subDir, File file) {
        File tempDir = FileUtil.getAbsoluteDirFile(subDir);
        try {
            if(!FileUtils.directoryContains(tempDir, file)) {
                throw new IllegalArgumentException("File path not legal, filePath = " + file.getPath());
            }
        } catch(IOException ex) {
            throw new IllegalArgumentException("Open whith IOException, filePath = " + file.getPath(), ex);
        }
    }

}
