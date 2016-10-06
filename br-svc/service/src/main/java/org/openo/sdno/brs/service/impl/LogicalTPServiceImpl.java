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

package org.openo.sdno.brs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.LogicalTerminationPointMO;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.service.inf.LogicalTPService;
import org.openo.sdno.brs.service.inf.RelationService;
import org.openo.sdno.brs.service.inf.ResourceService;

/**
 * LogicalTP service implement class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class LogicalTPServiceImpl implements LogicalTPService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    private String resourceTypeName;

    private ResourceService tpOperService;

    private RelationService tpRelationService;

    @Override
    public LogicalTerminationPointMO getLogicalTPByID(String objectID) throws ServiceException {
        return tpOperService.getResource(objectID, LogicalTerminationPointMO.class);
    }

    @Override
    public Object getLogicalTPs(String queryString, String key) throws ServiceException {
        return tpOperService.getResourceList(queryString, key, LogicalTerminationPointMO.class);
    }

    @Override
    public String getObjectId(LogicalTerminationPointMO logicalTP) throws ServiceException {
        return tpOperService.genID(logicalTP);
    }

    @Override
    public LogicalTerminationPointMO updateTPByID(String objectID, LogicalTerminationPointMO logicalTP)
            throws ServiceException {
        return tpOperService.updateResource(objectID, logicalTP, LogicalTerminationPointMO.class);
    }

    @Override
    public LogicalTerminationPointMO addLogicalTP(LogicalTerminationPointMO logicalTP) throws ServiceException {

        LogicalTerminationPointMO tpDB = tpOperService.addResource(logicalTP, LogicalTerminationPointMO.class);

        addComposition(logicalTP);
        return tpDB;
    }

    @Override
    public Boolean delTpByID(String objectID) throws ServiceException {
        return tpOperService.deleteResource(objectID, LogicalTerminationPointMO.class);
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public void setTpOperService(ResourceService tpOperService) {
        this.tpOperService = tpOperService;
    }

    public void setTpRelationService(RelationService tpRelationService) {
        this.tpRelationService = tpRelationService;
    }

    private void addComposition(LogicalTerminationPointMO logicalTP) throws ServiceException {
        LOGGER.info("addComposition: begin to add.");

        List<Relation> lstRelation = new ArrayList<Relation>();
        Relation relation = new Relation();
        relation.setSrcId(logicalTP.getMeID());
        relation.setDstId(logicalTP.getId());
        relation.setDstType(resourceTypeName);
        relation.setRelation(Constant.REALTION_COMPOSITION);
        lstRelation.add(relation);
        tpRelationService.createRelation(lstRelation);
    }
}
