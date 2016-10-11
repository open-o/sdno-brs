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

package org.openo.sdno.brs.exception;

/**
 * HTTP code definition.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class HttpCode {

    /**
     * Exception in business service.<br>
     * Server have encounter a unexpected situation,so it can not finish the request.Such problem
     * often due to bug in server source code.
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * response ok, the expected response head or data will return with this response.
     */
    public static final int RESPOND_OK = 200;

    /**
     * The request is accepted but not finished.
     */
    public static final int RESPOND_ACCEPTED = 202;

    /**
     * Request failed, server can not find the requested resource.
     */
    public static final int NOT_FOUND = 404;

    /**
     * The current request can not be understood by server or parameter is invalid.
     */
    public static final int BAD_REQUEST = 400;

    /**
     * The requested resource can not match the type in request header.
     */
    public static final int NOT_ACCEPTABLE = 406;

    private HttpCode() {
        // private constructor
    }

    /**
     * Return if the response is success.<br>
     * 
     * @param httpCode the response HTTP code.
     * @return if the result is ok or not.
     * @since SDNO 0.5
     */
    public static boolean isSucess(int httpCode) {
        return httpCode / 100 == 2;
    }
}
