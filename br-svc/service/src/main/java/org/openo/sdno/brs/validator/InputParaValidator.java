/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.brs.validator;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Validator class for input parameter.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public final class InputParaValidator {

    /**
     * Class of input parameter check.<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-20
     */
    public static class InputParaCheck {

        private InputParaCheck() {
        }

        /**
         * validate the given attribute.<br/>
         * 
         * @param attrName name of the attribute.
         * @param attrValue value to check.
         * @param clazz class
         * @throws ServiceException if error parameter is null.
         * @since SDNO 0.5
         */
        public static void inputAttrCheck(String attrName, Object attrValue, Class<?> clazz) throws ServiceException {

        }
    }
}
