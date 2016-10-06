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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.RelationService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Relation service implementation class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class RelationServiceImpl implements RelationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationServiceImpl.class);

    private String bucketName;

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Relation> getRelations(String dstType, String srcIds, String dstIds) throws ServiceException {
        String[] paras = {dstType, srcIds, dstIds};
        LOGGER.info("getRelations dstType={}, srcids={}, dstids={}", paras);
        RestfulResponse response = mssProxy.getRelations(bucketName, resourceTypeName, dstType, srcIds, dstIds);
        ResponseUtils.checkResonseAndThrowException(response);

        return (List)HttpResponseUtil.assembleRspData(response.getResponseContent(), Relation.class);
    }

    @Override
    public void delRelation(String strObjID, List<Relation> lstReq, List<Relation> lstDB) throws ServiceException {
        LOGGER.error("delRelation: begin to delete relation.");

        RestfulResponse restfulRsp = null;
        for(Relation relation : lstDB) {

            if(lstReq.contains(relation)) {
                continue;
            }
            String srcID = StringUtils.isEmpty(relation.getSrcId()) ? strObjID : relation.getSrcId();
            restfulRsp = mssProxy.deleteRelation(bucketName, resourceTypeName, srcID, relation.getDstId(),
                    relation.getDstType());
            ResponseUtils.checkResonseAndThrowException(restfulRsp);
        }
    }

    @Override
    public void createRelation(List<Relation> lstRelation) throws ServiceException {
        LOGGER.error("createRelation: begin to create relation.");

        if(!CollectionUtils.isEmpty(lstRelation)) {
            Map<String, Object> requestMap = new HashMap<String, Object>();
            requestMap.put(Constant.RELATIONS_KEY, lstRelation);
            RestfulResponse restfulRsp = mssProxy.createRelation(bucketName, resourceTypeName, requestMap);
            ResponseUtils.checkResonseAndThrowException(restfulRsp);
        } else {
            LOGGER.error("No relation need to create.");
        }
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }
}
