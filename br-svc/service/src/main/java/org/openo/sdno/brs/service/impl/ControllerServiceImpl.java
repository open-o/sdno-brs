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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.check.inf.BrsChecker;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.ControllerMo;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.ControllerService;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.validator.InputParaValidator.InputParaCheck;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * controller management service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-6-7
 */
public class ControllerServiceImpl implements ControllerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerServiceImpl.class);

    private IMSSProxy mssProxy;

    private String bucketName;

    private String resourceTypeName;

    private BrsChecker brsCheckerService;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public BrsChecker getBrsCheckerService() {
        return brsCheckerService;
    }

    public void setBrsCheckerService(BrsChecker brsCheckerService) {
        this.brsCheckerService = brsCheckerService;
    }

    /**
     * Query Controller by Id.<br>
     * 
     * @param objectId UUID of controller.
     * @return controller of the given UUID.
     * @throws ServiceException when query failed
     * @since SDNO 0.5
     */
    @Override
    public ControllerMo queryController(String objectId) throws ServiceException {
        UuidUtils.checkUuid(objectId);
        RestfulResponse response = getMssProxy().getResource(bucketName, resourceTypeName, objectId);
        ResponseUtils.checkResonseAndThrowException(response);

        return (ControllerMo)HttpResponseUtil.assembleRspData(response.getResponseContent(), ControllerMo.class);
    }

    /**
     * add controller.<br>
     * 
     * @param controller controller to add.
     * @return UUID of the given controller.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    @Override
    public String addController(ControllerMo controller) throws ServiceException {
        InputParaCheck.inputParamsCheck(controller);

        if(StringUtils.isEmpty(controller.getId())) {
            String controllerid = UuidUtils.createUuid();
            controller.setId(controllerid);
            controller.setObjectId(controllerid);
        }

        List<Object> list = new ArrayList<Object>();
        Map<String, Object> sendBody = new HashMap<String, Object>();
        list.add(controller);
        sendBody.put(Constant.OBJECTS_KEY, list);

        RestfulResponse response = getMssProxy().addResources(bucketName, resourceTypeName, sendBody);
        ResponseUtils.checkResonseAndThrowException(response);

        return controller.getId();
    }

    /**
     * delete controller by UUID.<br>
     * 
     * @param objectId UUID of controller.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    @Override
    public void deleteController(String objectId) throws ServiceException {
        UuidUtils.checkUuid(objectId);
        RestfulResponse response = getMssProxy().deleteResource(bucketName, resourceTypeName, objectId);
        ResponseUtils.checkResonseAndThrowException(response);

    }

    /**
     * update controller.<br>
     * 
     * @param objectId UUID of controller.
     * @param controller the controller information to update.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    @Override
    public void modifyController(String objectId, ControllerMo controller) throws ServiceException {
        UuidUtils.checkUuid(objectId);
        RestfulResponse response = getMssProxy().updateResource(bucketName, resourceTypeName, objectId, controller);
        ResponseUtils.checkResonseAndThrowException(response);

    }

    public IMSSProxy getMssProxy() {
        return mssProxy;
    }

    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

}
