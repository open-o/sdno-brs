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

package org.openo.sdno.mss.dao.io;

import static org.junit.Assert.assertEquals;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

/**
 * SqlSessionProxy test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class SqlSessionProxyTest {

    private PersistenceException ex = new PersistenceException();

    private Throwable th = new Throwable();

    private BatchExecutorException batchException =
            new BatchExecutorException("message", new BatchUpdateException(), null, null);

    @Test
    public void testInsert() {

        new MockUp<DefaultSqlSession>() {

            @Mock
            public int insert(String statement) {
                return 99;
            }

            @Mock
            public void close() {

            }
        };

        SqlSessionProxy sqlProxy = new SqlSessionProxy(5, new DefaultSqlSession(null, null));
        sqlProxy.tryTheBestCommit();
        assertEquals(99, sqlProxy.insert("statement"));
        sqlProxy.close();
    }

    @Test
    public void testCommitException() {

        new MockUp<DefaultSqlSession>() {

            @Mock
            public int insert(String statement) {
                return 99;
            }

            @Mock
            public void close() {
            }

            @Mock
            public void clearCache() {
            }

            @Mock
            public void commit() {
                ex.initCause(th);
                throw ex;
            }
        };

        DefaultSqlSession session = new DefaultSqlSession(null, null);
        SqlSessionProxy sqlProxy = new SqlSessionProxy(5, session);
        sqlProxy.insert("statement");
        sqlProxy.tryTheBestCommit();
        sqlProxy.close();
        assertEquals(th, ex.getCause());
    }

    @Test
    public void testCommitExceptionListNull() {

        new MockUp<DefaultSqlSession>() {

            @Mock
            public int insert(String statement) {
                return 99;
            }

            @Mock
            public void close() {
            }

            @Mock
            public void clearCache() {
            }

            @Mock
            public void commit() {
                ex.initCause(batchException);
                throw ex;
            }
        };

        DefaultSqlSession session = new DefaultSqlSession(null, null);
        SqlSessionProxy sqlProxy = new SqlSessionProxy(5, session);
        sqlProxy.insert("statement");
        sqlProxy.tryTheBestCommit();
        sqlProxy.close();
        assertEquals(batchException, ex.getCause());
    }

    @Test
    public void testCommitExceptionListNotNull() {

        BatchResult batchResult = new BatchResult(null, "sql");
        int updateCounts[] = new int[1];
        batchResult.setUpdateCounts(updateCounts);
        List<BatchResult> resultList = new ArrayList<BatchResult>();
        resultList.add(batchResult);
        BatchUpdateException updateException = new BatchUpdateException("reason", "SQLState", 1, updateCounts);

        final BatchExecutorException exception =
                new BatchExecutorException("message", updateException, resultList, batchResult);

        new MockUp<DefaultSqlSession>() {

            @Mock
            public int insert(String statement) {
                return 99;
            }

            @Mock
            public void close() {
            }

            @Mock
            public void clearCache() {
            }

            @Mock
            public void commit() {
                ex.initCause(exception);
                throw ex;
            }
        };

        DefaultSqlSession session = new DefaultSqlSession(null, null);
        SqlSessionProxy sqlProxy = new SqlSessionProxy(5, session);
        sqlProxy.insert("statement1");
        sqlProxy.insert("statement2");
        sqlProxy.insert("statement3");
        sqlProxy.insert("statement4");
        sqlProxy.tryTheBestCommit();
        sqlProxy.close();
        assertEquals(exception, ex.getCause());
    }
}
