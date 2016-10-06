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

package org.openo.sdno.mss.combine.impl;

import java.util.List;
import java.util.Map;

import org.openo.sdno.mss.combine.intf.InvRelationDataService;
import org.openo.sdno.mss.dao.entities.InvRespEntity;
import org.openo.sdno.mss.dao.intf.InvRelationDataHandler;
import org.openo.sdno.mss.dao.model.BaseModel;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inventory relation data service class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public class InvRelationDataServiceImpl implements InvRelationDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvRelationDataServiceImpl.class);

    protected InvRelationDataHandler relationDataHandler = null;

    public InvRelationDataServiceImpl() {
        // constructor.
    }

    public void setRelationDataHandler(InvRelationDataHandler relationDataHandler) {
        this.relationDataHandler = relationDataHandler;
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(String bktName, final String relationType,
            final String refUnique, BaseModel baseModel) {
        DataSourceCtrler.add(bktName);
        try {
            return relationDataHandler.get(relationType, refUnique, baseModel);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> get(String bktName, final String srcResType,
            final String dstResType, final String srcUuids, final String dstUuids) {
        DataSourceCtrler.add(bktName);
        try {
            return relationDataHandler.getRelationData(srcResType, dstResType, srcUuids, dstUuids);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<List<Map<String, Object>>> add(String bktName, final String relationType,
            List<Map<String, Object>> values) {
        DataSourceCtrler.add(bktName);
        try {
            return relationDataHandler.add(relationType, values);
        } finally {
            DataSourceCtrler.remove();
        }
    }

    @Override
    public InvRespEntity<Boolean> delete(String bktName, final String relationType, final String srcUuid,
            final String dstUuid, final String reltype) {
        DataSourceCtrler.add(bktName);
        try {
            return relationDataHandler.delete(relationType, srcUuid, dstUuid, reltype);
        } finally {
            DataSourceCtrler.remove();
        }
    }

}
