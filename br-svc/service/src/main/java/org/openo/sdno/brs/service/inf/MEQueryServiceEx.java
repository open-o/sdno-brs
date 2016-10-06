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

package org.openo.sdno.brs.service.inf;

import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.framework.container.service.IService;

/**
 * Interface of the ME query extend service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface MEQueryServiceEx extends IService {

    /**
     * According to the related table, through the ID in the Site query its corresponding
     * ManagedElement ID.<br>
     * 
     * @param siteResTypeName Site resource type name
     * @param uuidList The list of UUID
     * @return The list of ManagedElement Id
     * @since SDNO 0.5
     */
    List<String> getManagedElementIdListBySiteId(String siteResTypeName, List<String> uuidList) throws ServiceException;
}
