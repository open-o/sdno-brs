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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * SQLUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class SQLUtilTest {

    @Test
    public void testEscapeBackSlashNull() {
        String result = SQLUtil.escapeBackSlash(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeBackSlash() {
        String result = SQLUtil.escapeBackSlash("Escape\\Back\\Slash");
        String resultTemp = "Escape\\\\Back\\\\Slash";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeSingleQuotesNull() {
        String result = SQLUtil.escapeSingleQuotes(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeSingleQuotes() {
        String result = SQLUtil.escapeSingleQuotes("Escape'Single'Quotes");
        String resultTemp = "Escape\\'Single\\'Quotes";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeDoubleQuotesNull() {
        String result = SQLUtil.escapeDoubleQuotes(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeDoubleQuotes() {
        String result = SQLUtil.escapeDoubleQuotes("Escape\"Double\"Quotes");
        String resultTemp = "Escape\\\"Double\\\"Quotes";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapePercentNull() {
        String result = SQLUtil.escapePercent(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapePercent() {
        String result = SQLUtil.escapePercent("Escape%Percent");
        String resultTemp = "Escape\\%Percent";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeUnderLineNull() {
        String result = SQLUtil.escapeUnderLine(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeUnderLine() {
        String result = SQLUtil.escapeUnderLine("Escape_Under_Line");
        String resultTemp = "Escape\\_Under\\_Line";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeSqlQuotesNull() {
        String result = SQLUtil.escapeSqlQuotes(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeSqlQuotes() {
        String result = SQLUtil.escapeSqlQuotes("Escape'Sql\"Quotes");
        String resultTemp = "Escape\\'Sql\\\"Quotes";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeSqlSlashAndQuotesNull() {
        String result = SQLUtil.escapeSqlSlashAndQuotes(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeSqlSlashAndQuotes() {
        String result = SQLUtil.escapeSqlSlashAndQuotes("Escape'Sql\"Slash\"And\\Quotes");
        String resultTemp = "Escape\\'Sql\\\"Slash\\\"And\\\\Quotes";
        assertTrue(resultTemp.equals(result));
    }

    @Test
    public void testEscapeSqlSpecialAllCharsNull() {
        String result = SQLUtil.escapeSqlSpecialAllChars(null);
        assertTrue(result == null);
    }

    @Test
    public void testEscapeSqlSpecialAllChars() {
        String result = SQLUtil.escapeSqlSpecialAllChars("test\\Escape'Sql\"Special%All_Chars");
        String resultTemp = "test\\\\Escape\\'Sql\\\"Special\\%All\\_Chars";
        assertTrue(resultTemp.equals(result));
    }
}
