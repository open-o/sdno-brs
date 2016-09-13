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

import org.apache.commons.lang.StringUtils;

/**
 * SQL special character handle. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class SQLUtil {

    private SQLUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Change \\ to \\\\ <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeBackSlash(String str) {
        return str == null ? null : StringUtils.replace(str, "\\", "\\\\");
    }

    /**
     * Change ' to \\' <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeSingleQuotes(String str) {
        return str == null ? null : StringUtils.replace(str, "'", "\\'");
    }

    /**
     * Change \" to \\\" <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeDoubleQuotes(String str) {
        return str == null ? null : StringUtils.replace(str, "\"", "\\\"");
    }

    /**
     * Change % to \\% <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapePercent(String str) {
        return str == null ? null : StringUtils.replace(str, "%", "\\%");
    }

    /**
     * Change _ to \\_ <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeUnderLine(String str) {
        return str == null ? null : StringUtils.replace(str, "_", "\\_");
    }

    /**
     * Convert SQL special character, single quote mark then double quote mark. <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeSqlQuotes(String str) {
        if(null == str) {
            return str;
        }

        return escapeDoubleQuotes(escapeSingleQuotes(str));
    }

    /**
     * Convert SQL special character, single quote mark then double quote mark. <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeSqlSlashAndQuotes(String str) {
        if(null == str) {
            return str;
        }

        // Backslash should be replace first, or will be replace twice.
        return escapeSqlQuotes(escapeBackSlash(str));
    }

    /**
     * Replace special character in SQL, Order to convert : / ' " % _ <br>
     * 
     * @param str String for convert.
     * @return String after convert.
     * @since SDNO 0.5
     */
    public static String escapeSqlSpecialAllChars(String str) {
        if(str == null) {
            return str;
        }

        // Backslash should be replace first, or will be replace twice.
        return escapeUnderLine(escapePercent(escapeSqlSlashAndQuotes(str)));
    }
}
