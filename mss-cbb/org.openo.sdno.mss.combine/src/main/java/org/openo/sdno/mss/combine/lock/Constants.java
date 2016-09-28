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

package org.openo.sdno.mss.combine.lock;

/**
 * Constants class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class Constants {

    /**
     * Get lock success.
     */
    public static final int LOCK_SUCCESS = 0;

    /**
     * Can't get the lock, mutex.
     */
    public static final int LOCK_MUTEX = 1;

    /**
     * Can't get the lock, exception.
     */
    public static final int LOCK_EXCEPTION = 2;

    /**
     * HUAWEI's custom service error code.
     */
    public static final int CUSTOM_SERVICE_ERROR = 599;

    /**
     * Lock cannot be re entered.
     */
    public static final String NOT_REENTRANT_LOCK = "1000053";

    private Constants() {
        // Private constructor to prohibit instantiation.
    }
}
