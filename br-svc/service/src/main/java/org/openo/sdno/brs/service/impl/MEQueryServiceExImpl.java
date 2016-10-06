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
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.MEQueryServiceEx;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.rest.ResponseUtils;

/**
 * Query service for ME.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MEQueryServiceExImpl implements MEQueryServiceEx {

    private String bucketName;

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<String> getManagedElementIdListBySiteId(String siteResTypeName, List<String> uuidList)
            throws ServiceException {
        StringBuilder idStr = new StringBuilder();
        List<String> meidList = new ArrayList<String>();
        for(String id : uuidList) {
            idStr = idStr.append(id).append(" ");
        }
        String dstIds = idStr.toString().trim().replaceAll(" ", ",");

        RestfulResponse response =
                mssProxy.getManageElementByRelation(bucketName, siteResTypeName, resourceTypeName, dstIds);
        ResponseUtils.checkResonseAndThrowException(response);

        List<Relation> relationList =
                (List)HttpResponseUtil.assembleRspData(response.getResponseContent(), Relation.class);

        if(null != relationList) {
            for(Relation relation : relationList) {
                meidList.add(relation.getSrcId());
            }
        }

        return meidList;
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
