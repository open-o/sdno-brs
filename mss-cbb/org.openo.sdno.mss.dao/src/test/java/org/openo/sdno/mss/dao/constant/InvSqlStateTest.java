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

package org.openo.sdno.mss.dao.constant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvSqlStateTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class InvSqlStateTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testMatchesSybaseCode() {
        new MockUp<InvDbType>() {

            @Mock
            public InvDbType get() {

                return InvDbType.SYBASE;

            }
        };

        assertTrue(InvSqlState.DEAD_LOCK.matches(1205));
    }

    @Test
    public void testMatchesSqlserverCode() {
        new MockUp<InvDbType>() {

            @Mock
            public InvDbType get() {

                return InvDbType.SQLSERVER;

            }
        };

        assertTrue(InvSqlState.INSERT_DUPLICATE_KEY.matches(23000));
    }

    @Test
    public void testMatchesFalse() {
        assertFalse(InvSqlState.DEAD_LOCK.matches(999));
    }
}
