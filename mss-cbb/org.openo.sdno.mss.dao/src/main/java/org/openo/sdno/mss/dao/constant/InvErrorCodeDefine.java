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

/**
 * This class defines several error codes.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public enum InvErrorCodeDefine {

    /**
     * URL not found
     */
    INV_NOT_FOUND_URL_ERROR(1107316829),

    /**
     * resource not found
     */
    INV_RES_NOT_FOUND_ERROR(1107316830),

    /**
     * network element not found
     */
    INV_NOT_FOUND_NE(1107316831),

    /**
     * session timeout
     */
    INV_SESSION_TIMEOUT(1107316836),

    /**
     * parameter error
     */
    INV_INVALID_INPUT_ERROR(1107316837),

    /**
     * network element sync error, is synchronizing
     */
    INV_NE_SYNCING_ERROR(1107316838),

    /**
     * request header error
     */
    INV_INVALID_REQ_HEAD_ERROR(1107316839),

    /**
     * unknown error
     */
    INV_UNKNOWN_ERROR(1107316840),

    /**
     * ABS not connected
     */
    INV_ABS_NOT_CONNECT_ERROR(1107316841),

    /**
     * file read write failed
     */
    INV_FILE_RW_FAILED(1107316842),

    /**
     * get network element for ABS error
     */
    INV_GET_NE_FROM_ABS_ERROR(1107316843),

    /**
     * get system from ABS error
     */
    INV_GET_SYSTYPE_FROM_ABS_ERROR(1107316844),

    /**
     * database commit error
     */
    INV_DB_COMMIT_ERROR(1107316845),

    /**
     * server connect failed
     */
    INV_CONNECT_SERVER_ERROR(1107316846),

    /**
     * server not found
     */
    INV_NOT_FOUND_SERVER(1107316847),

    /**
     * server is not available
     */
    INV_SERVER_INVALID(1107316848),

    /**
     * data not exist
     */
    INV_DATA_NOTEXIST(1107316849);

    /**
     * error code
     */
    private int error;

    InvErrorCodeDefine(int error) {
        this.error = error;
    }

    /**
     * Get error attribute.<br>
     * 
     * @return error attribute
     * @since SDNO 0.5
     */
    public int getErrorCode() {
        return error;
    }
}
