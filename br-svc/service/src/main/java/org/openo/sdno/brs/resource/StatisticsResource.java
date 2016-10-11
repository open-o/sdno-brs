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

package org.openo.sdno.brs.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.service.inf.StatisticsService;
import org.openo.sdno.framework.container.service.IResource;

/**
 * Get total count of the resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
@Path("/sdnobrs/v1/statistics")
public class StatisticsResource extends IResource<StatisticsService> {

    @Override
    public String getResUri() {
        return "/sdnobrs/v1/statistics";
    }

    /**
     * get total count of the given type of resource.<br>
     * 
     * @param resType resource type.
     * @param tenantId UUID of the tenant.
     * @return total count of the given resource.
     * @throws ServiceException if data base service have encounter some problem.
     * @since SDNO 0.5
     */
    @GET
    @Path("/count")
    public int count(@QueryParam("res-type-name") String resType, @QueryParam("tenant-id") String tenantId)
            throws ServiceException {
        return service.count(resType, tenantId);
    }

}
