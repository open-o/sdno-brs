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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.openo.sdno.mss.dao.constant.InvSqlState;
import org.openo.sdno.mss.dao.exception.InvSqlException;
import org.openo.sdno.mss.dao.util.intf.ParaCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL Session Proxy class for batch Commit.<br>
 * 
 * @author
 */
public class SqlSessionProxy implements SqlSession {

    /**
     * Logger handler
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionProxy.class);

    /**
     * Global lock
     */
    private static final Lock lock = new ReentrantLock();

    /**
     * retry count when deadlock occurs
     */
    private int deadLockRetryCnt = 0;

    /**
     * retry count when error(except deadlock) occurs
     */
    private int duplicateRetryCnt = 0;

    /**
     * max retry count
     */
    private static final int MAX_RETRY_CNT = 3;

    /**
     * session
     */
    private final SqlSession session;

    /**
     * entity list of commit
     */
    private final List<SqlSessionEntity> recordList;

    /**
     * sql exception,just catch the first exception
     */
    private InvSqlException exception;

    /**
     * Constructor<br>
     * 
     * @param size number of commit records
     * @param session SqlSession Object
     */
    public SqlSessionProxy(int size, SqlSession session) {
        this.session = session;
        recordList = new ArrayList<SqlSessionEntity>(size);
    }

    /**
     * add Point.<br>
     */
    public void addPoint() {
        if(recordList.isEmpty()) {
            return;
        }
        recordList.get(recordList.size() - 1).addPoint();
    }

    /**
     * Get size of recordList.<br>
     * 
     * @return size of recordList
     */
    public int getSqlCnt() {
        return recordList.size();
    }

    /**
     * Retrieve a single row mapped from the statement key.<br>
     * 
     * @param statement Unique identifier matching the statement to use
     * @return Mapped object
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement) {
        return (T)session.selectOne(statement);
    }

    /**
     * Retrieve a single row mapped from the statement key and parameter.<br>
     * 
     * @param statement Unique identifier matching the statement to use.
     * @param parameter parameter A parameter object to pass to the statement.
     * @return Mapped object
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T)session.selectOne(statement, parameter);
    }

    /**
     * Retrieve a list of mapped objects from the statement key.<br>
     * 
     * @param statement Unique identifier matching the statement to use.
     * @return List of mapped object
     */
    @Override
    public <E> List<E> selectList(String statement) {
        return session.selectList(statement);
    }

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.<br>
     * 
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return List of mapped object
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return session.selectList(statement, parameter);
    }

    /**
     * Retrieve a list of mapped objects from the statement key and parameter,
     * within the specified row bounds.<br>
     * 
     * @param <E> the returned list element type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @param rowBounds Bounds to limit object retrieval
     * @return List of mapped object
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return session.selectList(statement, parameter, rowBounds);
    }

    /**
     * The selectMap is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.<br>
     * Eg. Return a of Map[Integer,Author] for selectMap("selectAuthors","id")
     * 
     * @param <K> the returned Map keys type
     * @param <V> the returned Map values type
     * @param statement Unique identifier matching the statement to use.
     * @param mapKey The property to use as key for each value in the list.
     * @return Map containing key pair data.
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return session.selectMap(statement, mapKey);
    }

    /**
     * The selectMap is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.<br>
     * 
     * @param <K> the returned Map keys type
     * @param <V> the returned Map values type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @param mapKey The property to use as key for each value in the list.
     * @return Map containing key pair data.
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return session.selectMap(statement, parameter, mapKey);
    }

    /**
     * The selectMap is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.<br>
     * 
     * @param <K> the returned Map keys type
     * @param <V> the returned Map values type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @param mapKey The property to use as key for each value in the list.
     * @param rowBounds Bounds to limit object retrieval
     * @return Map containing key pair data.
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return session.selectMap(statement, parameter, mapKey, rowBounds);
    }

    /**
     * Retrieve a single row mapped from the statement key and parameter.<br>
     * using a {@code ResultHandler}.
     * 
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @param handler ResultHandler that will handle each retrieved row
     * @return Mapped object
     */
    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        session.select(statement, parameter, handler);
    }

    /**
     * Retrieve a single row mapped from the statement.<br>
     * using a {@code ResultHandler}.
     * 
     * @param statement Unique identifier matching the statement to use.
     * @param handler ResultHandler that will handle each retrieved row
     * @return Mapped object
     */
    @Override
    public void select(String statement, ResultHandler handler) {
        session.select(statement, handler);
    }

    /**
     * Retrieve a single row mapped from the statement key and parameter.<br>
     * using a {@code ResultHandler} and {@code RowBounds}
     * 
     * @param statement Unique identifier matching the statement to use.
     * @param rowBounds RowBound instance to limit the query results
     * @param handler ResultHandler that will handle each retrieved row
     * @return Mapped object
     */
    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        session.select(statement, parameter, rowBounds, handler);
    }

    /**
     * Execute an insert statement.
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @return integer The number of rows affected by the insert.
     */
    @Override
    public int insert(final String statement) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.insert(statement);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.insert(statement);
    }

    /**
     * Execute an insert statement with the given parameter object. Any generated
     * auto increment values or selectKey entries will modify the given parameter
     * object properties. Only the number of rows affected will be returned.<br>
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return integer The number of rows affected by the insert.
     */
    @Override
    public int insert(final String statement, final Object parameter) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.insert(statement, parameter);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.insert(statement, parameter);
    }

    /**
     * Execute an update statement. The number of rows affected will be returned.<br>
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @return integer The number of rows affected by the update.
     */
    @Override
    public int update(final String statement) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.update(statement);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.update(statement);
    }

    /**
     * Execute an update statement. The number of rows affected will be returned.<br>
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return integer The number of rows affected by the update.
     */
    @Override
    public int update(final String statement, final Object parameter) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.update(statement, parameter);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.update(statement, parameter);
    }

    /**
     * Execute a delete statement. The number of rows affected will be returned.<br>
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @return integer The number of rows affected by the delete.
     */
    @Override
    public int delete(final String statement) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.delete(statement);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.delete(statement);
    }

    /**
     * Execute a delete statement. The number of rows affected will be returned.<br>
     * 
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return integer The number of rows affected by the delete.
     */
    @Override
    public int delete(final String statement, final Object parameter) {
        ParaCallable<SqlSession, Integer> callable = new ParaCallable<SqlSession, Integer>() {

            @Override
            public Integer call(SqlSession session) {
                return session.delete(statement, parameter);
            }
        };
        recordList.add(new SqlSessionEntity(callable));

        return session.delete(statement, parameter);
    }

    /**
     * Flushes batch statements and commits database connection.
     * Note that database connection will not be committed if no updates/deletes/inserts were
     * called.<br>
     * To force the commit call {@link SqlSession#commit(boolean)}
     */
    @Override
    public void commit() {
        session.commit();
    }

    /**
     * Try the best to commit.<br>
     */
    public void tryTheBestCommit() {
        if(recordList.isEmpty()) {
            return;
        }

        doCommit(recordList);
        clearCache();
    }

    /**
     * Do commit.<br>
     * 
     * @param records records need to commit
     */
    private void doCommit(List<SqlSessionEntity> records) {
        lock.lock();

        try {
            session.commit();
        } catch(PersistenceException ex) {
            int failedSqlIndex = getFailedSqlIndex(ex);
            session.clearCache();

            int sqlState = getSqlState(ex);
            LOGGER.warn("INV-Import: PersistenceException", ex);
            LOGGER.warn("INV-Import: Failed to commit sql: index = {}, sqlState = {}", failedSqlIndex, sqlState);

            // if deadlock,retry
            if(InvSqlState.DEAD_LOCK.matches(sqlState) && (++deadLockRetryCnt <= MAX_RETRY_CNT)) {
                LOGGER.warn("INV-Import: DeadLock retry", ex);
                recommitCurrent(failedSqlIndex, records);
            } else {
                if(++duplicateRetryCnt > MAX_RETRY_CNT) {
                    LOGGER.warn("INV-Import: Duplicate retry count > " + MAX_RETRY_CNT);
                } else {
                    LOGGER.warn("INV-Import: Duplicate retry", ex);
                    setException(ex, sqlState);
                    // TODO if have duplicated insert index, then uppdate
                    recommit(failedSqlIndex, records);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get SQL State from PersistenceException.<br>
     * 
     * @param ex PersistenceException
     * @return SQL State of PersistenceException
     */
    private int getSqlState(PersistenceException ex) {
        Throwable curException = ex.getCause();
        if(!(curException instanceof BatchExecutorException)) {
            return -1;
        }

        return ((BatchExecutorException)curException).getBatchUpdateException().getErrorCode();
    }

    /**
     * Get SQL State to PersistenceException. <br>
     * 
     * @param ex PersistenceException
     * @param sqlState SQL State
     */
    private void setException(PersistenceException ex, int sqlState) {
        if(exception == null) {
            InvSqlState state = InvSqlState.INSERT_DUPLICATE_KEY.matches(sqlState) ? InvSqlState.INSERT_DUPLICATE_KEY
                    : InvSqlState.UNKNOWN;
            exception = new InvSqlException(ex, state);
        }
    }

    /**
     * Get exception attribute.<br>
     * 
     * @return exception attribute
     */
    public InvSqlException getException() {
        return exception;
    }

    /**
     * ReCommit from current.<br>
     * 
     * @param failedSqlIndex failedSqlIndex
     * @param records records needed to commit
     */
    private void recommitCurrent(int failedSqlIndex, List<SqlSessionEntity> records) {
        if(failedSqlIndex < 0) {
            return;
        }

        List<SqlSessionEntity> subRecords = records.subList(failedSqlIndex, records.size());
        for(SqlSessionEntity record : subRecords) {
            record.getCallable().call(session);
        }

        doCommit(subRecords);
    }

    /**
     * ReCommit all records and ignore failed commit.<br>
     * 
     * @param failedSqlIndex failedSqlIndex
     * @param records records needed to commit
     */
    private void recommit(int failedSqlIndex, List<SqlSessionEntity> records) {
        if(records.size() <= 1 || failedSqlIndex < 0 || failedSqlIndex >= records.size() - 1) {
            return;
        }

        int nextIndex = searchNext(failedSqlIndex, records);
        if(nextIndex == -1) {
            return;
        }

        List<SqlSessionEntity> subRecords = records.subList(nextIndex, records.size());
        for(SqlSessionEntity record : subRecords) {
            record.getCallable().call(session);
        }

        doCommit(subRecords);
    }

    /**
     * Get FailedSqlIndex.<br>
     * 
     * @param ex PersistenceException
     * @return FailedSqlIndex and -1 if not found.
     */
    private int getFailedSqlIndex(PersistenceException ex) {
        Throwable curEexception = ex.getCause();
        if(!(curEexception instanceof BatchExecutorException)) {
            String msg = curEexception == null ? "null" : curEexception.getClass().getName();
            LOGGER.info("Not BatchExecutorException: {}", msg);
            return -1;
        }

        BatchExecutorException executorEx = (BatchExecutorException)ex.getCause();
        List<BatchResult> resultList = executorEx.getSuccessfulBatchResults();

        int lastSuccessCnt;
        int[] lastCnts = executorEx.getBatchUpdateException().getUpdateCounts();
        if(lastCnts == null) {
            lastSuccessCnt = 0;
        } else {
            // -3 flags failed
            int index = ArrayUtils.indexOf(lastCnts, -3);
            lastSuccessCnt = index == -1 ? lastCnts.length : index;
        }

        // no record success,return to the first record
        if(resultList == null || resultList.isEmpty()) {
            return lastSuccessCnt;
        }

        int cnt = lastSuccessCnt;
        for(BatchResult result : resultList) {
            cnt += result.getUpdateCounts().length;
        }

        return cnt;
    }

    /**
     * Find Next batch of SQLs.<br>
     * 
     * @param failedSqlIndex failedSqlIndex
     * @param records records needed to commit
     * @return SqlIndex
     */
    private int searchNext(int failedSqlIndex, List<SqlSessionEntity> records) {
        if(records == null || records.isEmpty()) {
            return -1;
        }

        for(int i = failedSqlIndex, n = records.size(); i < n - 1; i++) {
            // next of point
            if(records.get(i).isPoint()) {
                return i + 1;
            }
        }

        // not found
        return -1;
    }

    /**
     * Flushes batch statements and commits database connection.<br>
     * 
     * @param force forces connection commit
     */
    @Override
    public void commit(boolean force) {
        session.commit(force);
    }

    /**
     * Discards pending batch statements and rolls database connection back.
     * Note that database connection will not be rolled back if no updates/deletes/inserts were
     * called.<br>
     * To force the rollback call {@link SqlSession#rollback(boolean)}
     */
    @Override
    public void rollback() {
        session.rollback();
    }

    /**
     * Discards pending batch statements and rolls database connection back.
     * Note that database connection will not be rolled back if no updates/deletes/inserts were
     * called.<br>
     * 
     * @param force forces connection rollback
     */
    @Override
    public void rollback(boolean force) {
        session.rollback(force);
    }

    /**
     * Flushes batch statements.<br>
     * 
     * @return BatchResult list of updated records
     */
    @Override
    public List<BatchResult> flushStatements() {
        return session.flushStatements();
    }

    /**
     * Closes the session.<br>
     */
    @Override
    public void close() {
        session.close();
    }

    /**
     * Clears local session cache.<br>
     */
    @Override
    public void clearCache() {
        // reinitialize
        recordList.clear();
        session.clearCache();
        deadLockRetryCnt = 0;
    }

    /**
     * Retrieves current configuration.<br>
     * 
     * @return Configuration
     */
    @Override
    public Configuration getConfiguration() {
        return session.getConfiguration();
    }

    /**
     * Retrieves a mapper.<br>
     * 
     * @param <T> the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return session.getMapper(type);
    }

    /**
     * Retrieves inner database connection.<br>
     * 
     * @return Connection
     */
    @Override
    public Connection getConnection() {
        return session.getConnection();
    }

}
