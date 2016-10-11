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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.openo.sdno.framework.container.util.DefaultEnvUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File utility functions.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public final class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Default overdue time: 7days.
     */
    private static final int DEFAULT_OVERDUE_TIME = 7;

    /**
     * Lock for making sure there is only one thread running this paragraph of code.
     */
    private static final Lock lock = new ReentrantLock();

    private FileUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Get file in user's directory. <br>
     * 
     * @param path URL of user.path
     * @return File object of user file
     * @since SDNO 0.5
     */
    public static File getUserDirFile(String path) {
        Validate.notEmpty(path);
        return new File(DefaultEnvUtil.getAppShareDir() + File.separator + path);
    }

    /**
     * Get absolute directory file object. <br>
     * 
     * @param absolutePath Absolute path
     * @return File
     * @since SDNO 0.5
     */
    public static File getAbsoluteDirFile(String absolutePath) {
        Validate.notEmpty(absolutePath);

        return new File(absolutePath);
    }

    /**
     * Delete file which is overdue. <br>
     * 
     * @param file File object of overdue file.
     * @since SDNO 0.5
     */
    public static void deleteOverdueFile(File file) {
        try {
            deleteOverdueFile(file, DEFAULT_OVERDUE_TIME, TimeUnit.DAYS);
        } catch(IOException ex) {

            // Deleting was failed.
            LOGGER.error("Delete overdue file failed, fileDir", ex);
        }
    }

    /**
     * Delete all the sub elements if the file is a folder. <br>
     * 
     * @param file File object.
     * @param overdue Overdue time.
     * @param overdueUnit Unit of overdue time.
     * @throws IOException
     * @since SDNO 0.5
     */
    private static void deleteOverdueFile(File file, long overdue, TimeUnit overdueUnit) throws IOException {
        Validate.notNull(file);
        Validate.notNull(overdueUnit);
        if(!file.exists()) {
            LOGGER.error("File not exist:");
            return;
        }

        long curTime = System.currentTimeMillis();
        long overdueMills = overdueUnit.toMillis(overdue);

        lock.lock();
        try {
            if(file.exists()) {
                deleteOverdueFile(file, overdueMills, curTime);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Delete overdue time file. <br>
     * 
     * @param file File object.
     * @param overdueMills Overdue time(millisecond).
     * @param curTimeMills Current time(millisecond).
     * @throws IOException
     * @since SDNO 0.5
     */
    private static void deleteOverdueFile(File file, long overdueMills, long curTimeMills) throws IOException {
        long latsModified = file.lastModified();

        // Delete all the sub elements if file is a directory.
        if(file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if(subFiles != null) {
                for(File subFile : subFiles) {
                    deleteOverdueFile(subFile, overdueMills, curTimeMills);
                }
            }

            if(ArrayUtils.isEmpty(file.list()) && isOverdue(latsModified, overdueMills, curTimeMills)) {
                FileUtils.forceDelete(file);
            }
        } else {
            if(isOverdue(latsModified, overdueMills, curTimeMills)) {
                FileUtils.forceDelete(file);
                return;
            }
        }
    }

    private static boolean isOverdue(long fileLastModified, long overdueMills, long curTimeMills) {
        return curTimeMills - fileLastModified > overdueMills;
    }

    /**
     * Get full path of file. <br>
     * 
     * @param path Path string
     * @return File path
     * @since SDNO 0.5
     */
    public static File getFileServiceFullPathFile(String path) {
        Validate.notEmpty(path);
        return new File(path);
    }

    /**
     * Create file URL, profile is var/xxx/YY/MM/DD/UUIDs.<br>
     * 
     * @param subDir Sub directory.
     * @return URL of file
     * @since SDNO 0.5
     */
    public static String createTmpFilePath(String subDir) {
        Validate.notEmpty(subDir);

        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH) + 1;
        String monthStr = month < 10 ? String.valueOf("0" + month) : String.valueOf(month);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        String dayStr = day < 10 ? String.valueOf("0" + day) : String.valueOf(day);

        return subDir + "/" + year + '/' + monthStr + '/' + dayStr + '/' + UuidUtils.createUuid();
    }

}
