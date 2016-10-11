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

package org.openo.sdno.brs.check.impl;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.check.inf.BrsChecker;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.model.LogicalTerminationPointMO;
import org.openo.sdno.brs.model.ManagedElementMO;
import org.openo.sdno.brs.model.TopologicalLinkMO;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;
import org.openo.sdno.brs.service.inf.ResourceService;
import org.openo.sdno.brs.util.validate.ValidateUtil;

/**
 * Checker of BRS service, check if the resources are being used.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class BrsCheckerImpl implements BrsChecker {

    /**
     * ResourceService proxy
     */
    private ResourceService service;

    /**
     * Check if the NE is being used.<br>
     * 
     * @param managedElementId UUID of the manage element object.
     * @throws ServiceException if exception happens in the DB service.
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    private void checkMEIsUsed(String managedElementId) throws ServiceException {
        ValidateUtil.assertNotEmpty(managedElementId, Constant.MANAGEDELEMENT_ID);

        // assembling query filter information.
        Map<String, String> srcFilterMap = new HashMap<String, String>();
        srcFilterMap.put(Constant.AEND_ME, managedElementId);
        Map<String, String> dstFilterMap = new HashMap<String, String>();
        dstFilterMap.put(Constant.ZEND_ME, managedElementId);

        // a side parameter.
        PagingQueryPara srcParamMap = new PagingQueryPara();
        srcParamMap.setPageNum(Constant.PAGE_NUM_KEY);
        srcParamMap.

                setPageSize(Constant.PAGE_SIZE_KEY);
        srcParamMap.setFields(Constant.AEND_ME);
        srcParamMap.setFiltersMap(srcFilterMap);
        // z side parameter.
        PagingQueryPara dstParamMap = new PagingQueryPara();
        dstParamMap.setPageNum(Constant.PAGE_NUM_KEY);
        dstParamMap.setPageSize(Constant.PAGE_SIZE_KEY);
        dstParamMap.setFields(Constant.ZEND_ME);
        dstParamMap.setFiltersMap(dstFilterMap);

        Map<String, Object> srcTopologicalLinkList = (Map<String, Object>)service.getResourceList(srcParamMap,
                Constant.TOPOLOGICALLINK_KEY, TopologicalLinkMO.class);
        Object srcTotalNumObject = srcTopologicalLinkList.get(Constant.TOTAL_NUM_KEY);
        ValidateUtil.assertNotNull(srcTotalNumObject, Constant.TOTAL_NUM_KEY);
        int srcTotalNum = (int)srcTotalNumObject;
        if(srcTotalNum > 0) {
            throw new ServiceException(ErrorCode.BRS_MANAGEDELEMENT_ISUSED,
                    "brs managedElement is used by topologicalLink");
        } else {
            Map<String, Object> dstTopologicalLinkList = (Map<String, Object>)service.getResourceList(dstParamMap,
                    Constant.TOPOLOGICALLINK_KEY, TopologicalLinkMO.class);
            Object dstTotalNumObject = dstTopologicalLinkList.get(Constant.TOTAL_NUM_KEY);
            ValidateUtil.assertNotNull(dstTotalNumObject, Constant.TOTAL_NUM_KEY);
            int dstTotalNum = (int)dstTotalNumObject;
            if(dstTotalNum > 0) {
                throw new ServiceException(ErrorCode.BRS_MANAGEDELEMENT_ISUSED,
                        "brs managedElement is used by topologicalLink");
            }
        }
    }

    /**
     * Check if the TP is being used.<br>
     * 
     * @param logicalTPId UUID of the TP.
     * @throws ServiceException if exception happens in the DB service.
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    private void checkTPIsUsed(String logicalTPId) throws ServiceException {
        ValidateUtil.assertNotEmpty(logicalTPId, Constant.LOGICALTP_ID);
        // assembling query filter information.
        Map<String, String> srcFilterMap = new HashMap<String, String>();
        srcFilterMap.put(Constant.A_END, logicalTPId);
        Map<String, String> dstFilterMap = new HashMap<String, String>();
        dstFilterMap.put(Constant.Z_END, logicalTPId);

        // a side parameter.
        PagingQueryPara srcParamMap = new PagingQueryPara();
        srcParamMap.setPageNum(Constant.PAGE_NUM_KEY);
        srcParamMap.setPageSize(Constant.PAGE_SIZE_KEY);
        srcParamMap.setFields(Constant.A_END);
        srcParamMap.setFiltersMap(srcFilterMap);
        // z side parameter.
        PagingQueryPara dstParamMap = new PagingQueryPara();
        dstParamMap.setPageNum(Constant.PAGE_NUM_KEY);
        dstParamMap.setPageSize(Constant.PAGE_SIZE_KEY);
        dstParamMap.setFields(Constant.Z_END);
        dstParamMap.setFiltersMap(dstFilterMap);

        Map<String, Object> srcTopologicalLinkList = (Map<String, Object>)service.getResourceList(srcParamMap,
                Constant.TOPOLOGICALLINK_KEY, TopologicalLinkMO.class);
        Object srcTotalNumObject = srcTopologicalLinkList.get(Constant.TOTAL_NUM_KEY);
        ValidateUtil.assertNotNull(srcTotalNumObject, Constant.TOTAL_NUM_KEY);
        int srcTotalNum = (int)srcTotalNumObject;
        if(srcTotalNum > 0) {
            throw new ServiceException(ErrorCode.BRS_LOGICALTP_ISUSED,
                    "brs logicalTerminationPoint is used by topologicalLink");
        } else {

            Map<String, Object> dstTopologicalLinkList = (Map<String, Object>)service.getResourceList(dstParamMap,
                    Constant.TOPOLOGICALLINK_KEY, TopologicalLinkMO.class);
            Object dstTotalNumObject = dstTopologicalLinkList.get(Constant.TOTAL_NUM_KEY);
            ValidateUtil.assertNotNull(dstTotalNumObject, Constant.TOTAL_NUM_KEY);
            int dstTotalNum = (int)dstTotalNumObject;
            if(dstTotalNum > 0) {
                throw new ServiceException(ErrorCode.BRS_LOGICALTP_ISUSED,
                        "brs logicalTerminationPoint is used by topologicalLink");
            }
        }
    }

    /**
     * Check if the resource is being used.<br>
     * 
     * @param objectID UUID of the object.
     * @param classType Resource type.
     * @throws ServiceException if exception happens in the DB service.
     * @since SDNO 0.5
     */
    @Override
    public void checkResourceIsUsed(String objectID, Class<?> classType) throws ServiceException {
        if(classType.equals(LogicalTerminationPointMO.class)) {
            // check if TP is being used.
            checkTPIsUsed(objectID);
        }

        if(classType.equals(ManagedElementMO.class)) {
            // check if NE is being used.
            checkMEIsUsed(objectID);
        }
    }

    /**
     * @param service The service to set.
     */
    public void setService(ResourceService service) {
        this.service = service;
    }
}
