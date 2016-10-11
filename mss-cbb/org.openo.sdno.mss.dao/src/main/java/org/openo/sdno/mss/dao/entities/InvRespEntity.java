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

package org.openo.sdno.mss.dao.entities;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Inventory Response Entity class.<br>
 * <p>
 * 1.if you don't care HTTP body,just return this response 2.if you care HTTP body,you can return
 * the HTTP response and this response. 3.The business can extend this message body.
 * </p>
 * 
 * @param <T> T is class of user defined data
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public class InvRespEntity<T> {

    /**
     * TODO more error code will be defined
     */
    private static final int ERROR_UNKNOWN = -2;

    /**
     * business error code
     * 1.400 client request error
     * 2.500 server internal error
     * 3.0 the requester no need to parse this field
     */
    @JsonProperty
    protected int retcode;

    /**
     * error message
     */
    @JsonProperty
    protected String message;

    /**
     * row number which is affected
     */
    @JsonProperty
    protected int row;

    /**
     * user defined data
     */
    @JsonProperty
    protected T data;

    /**
     * Constructor<br>
     * <p>
     * Call by JSON.
     * </p>
     * 
     * @since SDNO 0.5
     */
    public InvRespEntity() {
        // do nothing,called by JSON
    }

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since SDNO 0.5
     * @param retcode return code
     * @param data data value
     * @param row row number
     */
    public InvRespEntity(int retcode, T data, int row) {
        this.retcode = retcode;
        this.row = row;
        this.message = retcode == 0 ? "success" : "failed";
        this.data = data;
    }

    /**
     * Set retCode and return.<br>
     * 
     * @param retcode retCode value
     * @return this object
     * @since SDNO 0.5
     */
    public InvRespEntity<T> buildRetCode(int retcode) {
        this.retcode = retcode;
        return this;
    }

    /**
     * Set message and return.<br>
     * 
     * @param message error message
     * @return this object
     * @since SDNO 0.5
     */
    public InvRespEntity<T> buildMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set data and return.<br>
     * 
     * @param data data value
     * @return this object
     * @since SDNO 0.5
     */
    public InvRespEntity<T> buildData(T data) {
        this.data = data;
        return this;
    }

    /**
     * Check whether this is success response.<br>
     * 
     * @return true if this is success response, false otherwise
     * @since SDNO 0.5
     */
    @JsonIgnore
    public boolean isSuccess() {
        return retcode == 0;
    }

    /**
     * Get return code attribute.<br>
     * 
     * @return return code attribute
     * @since SDNO 0.5
     */
    @JsonIgnore
    public int getRetCode() {
        return retcode;
    }

    /**
     * Get message attribute.<br>
     * 
     * @return message attribute
     * @since SDNO 0.5
     */
    @JsonIgnore
    public String getMessage() {
        return message;
    }

    /**
     * Get data attribute.<br>
     * 
     * @return data attribute
     * @since SDNO 0.5
     */
    @JsonIgnore
    public T getData() {
        return data;
    }

    /**
     * Set message attribute.<br>
     * 
     * @param message String Object
     * @since SDNO 0.5
     */
    @JsonIgnore
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get row attribute.<br>
     * 
     * @return row attribute
     * @since SDNO 0.5
     */
    @JsonIgnore
    public int getRow() {
        return row;
    }

    /**
     * Override toString Function.<br>
     * 
     * @return toString Content
     * @since SDNO 0.5
     */
    @Override
    public String toString() {
        return "retcode = " + retcode + ", message = " + message + ", row = " + row;
    }

    /**
     * Get a simple success response.<br>
     * 
     * @param data data value
     * @return response instance
     * @since SDNO 0.5
     */
    public static <T> InvRespEntity<T> valueOfSuccess(T data) {
        return new InvRespEntity<T>(0, data, -1);
    }

    /**
     * Get a simple success response.<br>
     * 
     * @param data data value
     * @param row row number
     * @return response instance
     * @since SDNO 0.5
     */
    public static <T> InvRespEntity<T> valueOfSuccess(T data, int row) {
        return new InvRespEntity<T>(0, data, row);
    }

    /**
     * Get a simple fail response.<br>
     * 
     * @param data data value
     * @param row row number
     * @return response instance
     * @since SDNO 0.5
     */
    public static <T> InvRespEntity<T> valueOfError(T data, int row) {
        return new InvRespEntity<T>(ERROR_UNKNOWN, data, row);
    }

    /**
     * Get a simple fail response.<br>
     * 
     * @param message error message
     * @return response instance
     * @since SDNO 0.5
     */
    public static <T> InvRespEntity<T> valueOfError(String message) {
        InvRespEntity<T> entity = new InvRespEntity<T>(ERROR_UNKNOWN, null, -1);
        entity.message = message;
        return entity;
    }

    /**
     * Get a simple fail response.<br>
     * 
     * @param retcode error code
     * @return response instance
     * @since SDNO 0.5
     */
    public static <T> InvRespEntity<T> valueOfError(int retcode) {
        return new InvRespEntity<T>(retcode, null, -1);
    }
}
