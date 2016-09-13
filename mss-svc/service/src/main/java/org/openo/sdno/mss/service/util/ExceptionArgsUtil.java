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

package org.openo.sdno.mss.service.util;

import org.openo.baseservice.remoteservice.exception.ExceptionArgs;

/**
 * Tool class for operating exception arguments.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ExceptionArgsUtil {

    private ExceptionArgsUtil() {

    }

    /**
     * Get exception arguments.<br>
     * 
     * @param ex Throwable object
     * @return Exception arguments
     * @since SDNO 0.5
     */
    public static ExceptionArgs getExceptionArgs(Throwable ex) {
        ExceptionArgs excArg = new ExceptionArgs();
        String[] detailMsg = {ex.getMessage()};
        excArg.setDetailArgs(detailMsg);
        return excArg;
    }

    /**
     * Get exception arguments.<br>
     * 
     * @param msg Detail message
     * @return Exception arguments
     * @since SDNO 0.5
     */
    public static ExceptionArgs getExceptionArgs(String msg) {
        ExceptionArgs excArg = new ExceptionArgs();
        String[] detailMsg = {msg};
        excArg.setDetailArgs(detailMsg);
        return excArg;
    }
}
