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

package org.openo.sdno.brs.check.inf;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * BRS checker interface.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public interface BrsChecker {

    /**
     * Check if the resource is being used.<br>
     * 
     * @param objectID UUID of the object.
     * @param classType Resource type.
     * @throws ServiceException if exception happens in the DB service.
     * @since SDNO 0.5
     */
    void checkResourceIsUsed(String objectID, Class<?> classType) throws ServiceException;
}
