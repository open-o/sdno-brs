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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.check.inf.BrsChecker;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.model.ManagedElementMO;
import org.openo.sdno.brs.model.Relation;
import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.model.roamo.PageResponseData;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.service.inf.MEQueryServiceEx;
import org.openo.sdno.brs.service.inf.ManagedElementService;
import org.openo.sdno.brs.service.inf.RelationService;
import org.openo.sdno.brs.service.inf.ResWithRelationQueryService;
import org.openo.sdno.brs.util.RelationUtil;
import org.openo.sdno.brs.util.http.HttpResponseUtil;
import org.openo.sdno.brs.util.validate.ValidateUtil;
import org.openo.sdno.brs.validator.InputParaValidator.InputParaCheck;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.framework.container.util.UuidUtils;
import org.openo.sdno.rest.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of managed element service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public final class ManagedElementServiceImpl implements ManagedElementService {

    private String bucketName;

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedElementServiceImpl.class);

    private IMSSProxy mssProxy;

    private String resourceTypeName;

    private String mdResTypeName;

    private String siteResTypeName;

    private String controllerResTypeName;

    private BrsChecker brsCheckerService;

    private RelationService relationService;

    private MEQueryServiceEx meQueryServiceEx;

    private ResWithRelationQueryService meQueryService;

    private static String RELATION_MDID = "managementDomainID";

    private static String RELATION_CONTROLLERID = "controllerID";

    private static String RELATION_SITEID = "siteID";

    private static String RELATION_ID = "id";

    @SuppressWarnings("unchecked")
    @Override
    public Object addManagedElement(ManagedElementMO managedElement) throws ServiceException {
        InputParaCheck.inputParamsCheck(managedElement);

        checkIDIsUsed(managedElement);

        Map<String, List<Relation>> mapRelation = (Map<String, List<Relation>>)getRelationFromMO(managedElement);

        managedElement.setManagementDomainID(null);
        managedElement.setControllerID(null);
        managedElement.setSiteID(null);
        managedElement.setNetworkControlDomainID(null);
        List<ManagedElementMO> lstManagedElement = new ArrayList<ManagedElementMO>();
        lstManagedElement.add(managedElement);
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put(Constant.OBJECTS_KEY, lstManagedElement);

        RestfulResponse restfulResponse = mssProxy.addResources(bucketName, resourceTypeName, requestMap);
        ResponseUtils.checkResonseAndThrowException(restfulResponse);

        List<ManagedElementMO> lstME = parseDataFromResponse(restfulResponse.getResponseContent());
        ManagedElementMO meRsp = (!lstME.isEmpty()) ? lstME.get(0) : null;
        if(null == meRsp) {
            LOGGER.error("addManagedElement: The object is null returned by parseDataFromResponse!");
            return null;
        }

        addRelation(mapRelation);

        return meRsp;
    }

    @Override
    public int delManagedElementByID(String objectID) throws ServiceException {
        // Check whether there is a link under the ne
        brsCheckerService.checkResourceIsUsed(objectID, ManagedElementMO.class);

        // delete the resource
        RestfulResponse response = mssProxy.deleteResource(bucketName, resourceTypeName, objectID);
        ResponseUtils.checkResonseAndThrowException(response);

        // return the status code
        return response.getStatus();
    }

    @Override
    public ManagedElementMO getManagedElementByID(String objectID) throws ServiceException {
        return meQueryService.getResourceByID(objectID, ManagedElementMO.class);
    }

    @Override
    public String getObjectId(ManagedElementMO managedElement) throws ServiceException {
        String strResID = managedElement.getId();
        if(!StringUtils.isEmpty(strResID)) {
            return strResID;
        }

        return UuidUtils.createUuid();
    }

    @Override
    public Map<String, Object> getManagedElementMOs(String fields, Map<String, String> filterMap, int pagesize,
            int pagenum) throws ServiceException {
        return meQueryService.getResources(fields, "managedElements", filterMap, pagesize, pagenum,
                ManagedElementMO.class);
    }

    /**
     * @param bucketName The bucketName to set.
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * @param mssProxy The mssProxy to set.
     */
    public void setMssProxy(IMSSProxy mssProxy) {
        this.mssProxy = mssProxy;
    }

    /**
     * @param resourceTypeName The resourceTypeName to set.
     */
    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    /**
     * @param mdResTypeName The mdResTypeName to set.
     */
    public void setMdResTypeName(String mdResTypeName) {
        this.mdResTypeName = mdResTypeName;
    }

    /**
     * @param siteResTypeName The siteResTypeName to set.
     */
    public void setSiteResTypeName(String siteResTypeName) {
        this.siteResTypeName = siteResTypeName;
    }

    /**
     * @param controllerResTypeName The controllerResTypeName to set.
     */
    public void setControllerResTypeName(String controllerResTypeName) {
        this.controllerResTypeName = controllerResTypeName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object updateManagedElementByID(String objectID, ManagedElementMO managedElement) throws ServiceException {
        // check the input parameters
        InputParaCheck.inputParamsCheck(managedElement, true);
        ManagedElementMO meOldDB = getManagedElementByID(objectID);
        if(StringUtils.isEmpty(meOldDB.getId())) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_NOT_EXIST, HttpCode.BAD_REQUEST);
        }

        // deal with the related data
        Map<String, List<Relation>> mapRelation = null;
        boolean bUpdateRelation = needUpdateRelation(managedElement);
        if(bUpdateRelation) {
            managedElement.setId(objectID);
            mapRelation = (Map<String, List<Relation>>)getRelationFromMO(managedElement);
            updateRelation(objectID, mapRelation);
        }

        // call the mss interface , update the base and extend data
        ManagedElementMO objME = updateBaseAndExtData(managedElement, objectID);
        if(null == objME) {
            objME = new ManagedElementMO();
            objME.setId(objectID);
            objME.setUpdatetime(System.currentTimeMillis());
        }

        return objME;
    }

    private boolean needUpdateRelation(ManagedElementMO managedElement) {
        return (null != managedElement.getSiteID()) || (null != managedElement.getControllerID())
                || ((null != managedElement.getManagementDomainID())
                        || (null != managedElement.getNetworkControlDomainID()));
    }

    @SuppressWarnings("rawtypes")
    private List<ManagedElementMO> parseDataFromResponse(String responseContent) throws ServiceException {
        Object rspObj = HttpResponseUtil.assembleRspData(responseContent, ManagedElementMO.class);
        ValidateUtil.assertNotList(rspObj, "resultData");

        List<ManagedElementMO> meList = new ArrayList<ManagedElementMO>();
        List listObj = (List)rspObj;
        for(Object objME : listObj) {
            ManagedElementMO managedElement = JsonUtil.fromJson(JsonUtil.toJson(objME), ManagedElementMO.class);
            meList.add(managedElement);
        }

        return meList;
    }

    private Object getRelationFromMO(ManagedElementMO managedElement) {
        Map<String, List<Relation>> mapRelation = new HashMap<>();
        String strResourceID = managedElement.getId();

        String strMD = managedElement.getManagementDomainID();
        if(null != strMD) {
            List<String> lstMD = new ArrayList<String>();
            lstMD.add(strMD);
            mapRelation.put(mdResTypeName, RelationUtil.convertRelation(lstMD, strResourceID, mdResTypeName));
        }

        mapRelation.put(controllerResTypeName,
                RelationUtil.convertRelation(managedElement.getControllerID(), strResourceID, controllerResTypeName));

        mapRelation.put(siteResTypeName,
                RelationUtil.convertRelation(managedElement.getSiteID(), strResourceID, siteResTypeName));

        mapRelation.put(Constant.RESTYPE_NCD, RelationUtil.convertRelation(managedElement.getNetworkControlDomainID(),
                strResourceID, Constant.RESTYPE_NCD));

        return mapRelation;
    }

    public void setBrsCheckerService(BrsChecker brsCheckerService) {
        this.brsCheckerService = brsCheckerService;
    }

    /**
     * @param meQueryService The meQueryService to set.
     */
    public void setMeQueryService(ResWithRelationQueryService meQueryService) {
        this.meQueryService = meQueryService;
    }

    public void setMeQueryServiceEx(MEQueryServiceEx meQueryServiceEx) {
        this.meQueryServiceEx = meQueryServiceEx;
    }

    public void setRelationService(RelationService relationService) {
        this.relationService = relationService;
    }

    private void updateRelation(String strObjID, Map<String, List<Relation>> mapRelation) throws ServiceException {
        if(null == mapRelation) {
            LOGGER.info("There is no relation data that need update.");
            return;
        }

        updateRelationByRefKey(mapRelation, mdResTypeName, strObjID);
        updateRelationByRefKey(mapRelation, siteResTypeName, strObjID);
        updateRelationByRefKey(mapRelation, controllerResTypeName, strObjID);
        updateRelationByRefKey(mapRelation, Constant.RESTYPE_NCD, strObjID);
    }

    private void updateRelationByRefKey(Map<String, List<Relation>> mapRelation, String strReferenceKey,
            String strObjID) throws ServiceException {
        if(null == mapRelation.get(strReferenceKey)) {
            LOGGER.error("updateRelationByRefKey: No relation need to modify! " + strReferenceKey);
            return;
        }

        List<Relation> lstReq = mapRelation.get(strReferenceKey);

        List<Relation> lstDB = relationService.getRelations(strReferenceKey, strObjID, null);

        relationService.delRelation(strObjID, lstReq, lstDB);

        List<Relation> lstAdd = new ArrayList<Relation>();
        for(Relation relation : lstReq) {
            if(lstDB.contains(relation)) {
                continue;
            }

            if(!StringUtils.isEmpty(relation.getDstId())) {
                lstAdd.add(relation);
            }
        }
        relationService.createRelation(lstAdd);
    }

    /**
     * Get base data by Id.<br>
     * 
     * @param strObjID String object Id
     * @return ManagedElement
     * @since SDNO 0.5
     */
    @Override
    public ManagedElementMO getBaseDataByID(String strObjID) throws ServiceException {
        RestfulResponse response = mssProxy.getResource(bucketName, resourceTypeName, strObjID);
        ResponseUtils.checkResonseAndThrowException(response);

        return (ManagedElementMO)HttpResponseUtil.assembleRspData(response.getResponseContent(),
                ManagedElementMO.class);
    }

    @SuppressWarnings("unchecked")
    private boolean bmodifyBaseAndExtAtrr(ManagedElementMO me) throws ServiceException {
        String strKey = null;
        boolean bReference = false;
        Map<String, Object> mapAtrr = JsonUtil.fromJson(JsonUtil.toJson(me), Map.class);
        for(Map.Entry<String, Object> entry : mapAtrr.entrySet()) {
            strKey = entry.getKey();
            bReference = (strKey.equals(RELATION_MDID)) || (strKey.equals(RELATION_CONTROLLERID))
                    || (strKey.equals(RELATION_SITEID) || (strKey.equals(RELATION_ID)));

            if(bReference) {
                continue;
            }

            if(null != entry.getValue()) {
                return true;
            }
        }

        return false;
    }

    private void addRelation(Map<String, List<Relation>> mapRelation) throws ServiceException {
        if(mapRelation == null || CollectionUtils.isEmpty(mapRelation.values())) {
            return;
        }

        List<Relation> lst;
        List<Relation> lstRelation = new ArrayList<>();
        for(Map.Entry<String, List<Relation>> resEntry : mapRelation.entrySet()) {
            lst = resEntry.getValue();
            if(CollectionUtils.isEmpty(lst)) {
                continue;
            }
            lstRelation.addAll(lst);
        }

        removeInvalidDst(lstRelation);
        relationService.createRelation(lstRelation);
    }

    private ManagedElementMO updateBaseAndExtData(ManagedElementMO me, String strObjID) throws ServiceException {
        ManagedElementMO meRsp = null;
        if(bmodifyBaseAndExtAtrr(me)) {
            me.setManagementDomainID(null);
            me.setControllerID(null);
            me.setSiteID(null);
            me.setId(null);

            RestfulResponse response = mssProxy.updateResource(bucketName, resourceTypeName, strObjID, me);
            ResponseUtils.checkResonseAndThrowException(response);

            String strResult = response.getResponseContent();
            ValidateUtil.assertNotEmpty(strResult, "responseContent");
            meRsp = (ManagedElementMO)HttpResponseUtil.assembleRspData(response.getResponseContent(),
                    ManagedElementMO.class);
        }

        return meRsp;
    }

    private void removeInvalidDst(List<Relation> lstRelation) {
        Iterator<Relation> it = lstRelation.iterator();
        while(it.hasNext()) {
            if(!StringUtils.isEmpty((it.next()).getDstId())) {
                continue;
            }

            it.remove();
        }
    }

    private void checkIDIsUsed(ManagedElementMO me) throws ServiceException {
        Map<String, String> checkObj = new HashMap<>();
        checkObj.put(Constant.RESOURCE_ID, me.getId());
        RestfulResponse response = mssProxy.checkObjExsit(bucketName, resourceTypeName, checkObj);
        HttpResponseUtil.checkResonseAndThrowException(response);
        if("true".equals(response.getResponseContent())) {
            throw new ServiceException(ErrorCode.BRS_RESOURCE_ID_USED, HttpCode.BAD_REQUEST);
        }
    }

    @Override
    public List<ManagedElementMO> getManagedElementByTenantId(String tenantID) throws ServiceException {
        List<ManagedElementMO> meinfo = new ArrayList<ManagedElementMO>();

        String filter = getSiteFilterByTenantID(tenantID);
        String fields = "id,tenantID";
        RestfulResponse restfulResponse = mssProxy.getResourceListInfo(bucketName, siteResTypeName, fields, filter,
                Constant.PAGE_SIZE_KEY, Constant.PAGE_NUM_KEY);
        ResponseUtils.checkResonseAndThrowException(restfulResponse);

        @SuppressWarnings({"rawtypes"})
        PageResponseData pageRsp = new PageResponseData();
        List<SiteMO> data =
                HttpResponseUtil.assembleListRspData(restfulResponse.getResponseContent(), pageRsp, SiteMO.class);
        List<String> siteIdlist = getUuidByTenantId(data);

        if(null != siteIdlist && !siteIdlist.isEmpty()) {
            List<String> meIdList = meQueryServiceEx.getManagedElementIdListBySiteId(siteResTypeName, siteIdlist);

            if(null != meIdList && !meIdList.isEmpty()) {
                for(String id : meIdList) {
                    ManagedElementMO mo = getManagedElementByID(id);
                    meinfo.add(mo);
                }
            }
        }

        return meinfo;
    }

    private String getSiteFilterByTenantID(String tenantID) {
        Map<String, String> filterData = new HashMap<String, String>();
        Map<String, Object> filter = new HashMap<String, Object>();
        String filterDsc = "tenantID =':teid'";
        filterData.put("teid", tenantID);
        filter.put("filterDsc", filterDsc);
        filter.put("filterData", JsonUtil.toJson(filterData));

        return JsonUtil.toJson(filter);
    }

    private List<String> getUuidByTenantId(List<SiteMO> data) {
        List<String> uuidList = new ArrayList<String>();
        if(null == data || data.isEmpty()) {
            LOGGER.error("the tenantId data in site is null, data: {}", data);
            return uuidList;
        }
        for(SiteMO mo : data) {
            uuidList.add(mo.getId());
        }

        return uuidList;
    }

}
