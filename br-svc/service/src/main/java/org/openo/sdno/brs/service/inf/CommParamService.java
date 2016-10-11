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
import org.openo.sdno.brs.model.CommParamMo;
import org.openo.sdno.framework.container.service.IService;

/**
 * interface of common parameter service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-6-3
 */
public interface CommParamService extends IService {

    /**
     * create a common parameter.<br>
     * 
     * @param parameter CommParamMo to add.
     * @param objectId controller id.
     * @return UUID of the parameter.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    String createCommParams(CommParamMo parameter, String objectId) throws ServiceException;

    /**
     * query common parameter by UUID.<br>
     * 
     * @param paramId UUID of parameter.
     * @param objectId controller id.
     * @return CommParamMo with the given UUID.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    CommParamMo queryCommParamsDetail(String paramId, String objectId) throws ServiceException;

    /**
     * batch query parameter by controller UUID.<br>
     * 
     * @param objectid controller UUID.
     * @return list of common parameter.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    List<CommParamMo> queryCommParamsList(String objectid) throws ServiceException;

    /**
     * update parameter by UUID.<br>
     * 
     * @param commparams common parameter object to update.
     * @param objectId UUID of the object to update.
     * @param paramId The id of the parameter
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    void updateCommParams(CommParamMo commparams, String objectId, String paramId) throws ServiceException;

    /**
     * delete parameter by UUID.<br>
     * 
     * @param paramId UUID of the object to delete.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    void deleteCommParams(String paramId) throws ServiceException;

    /**
     * delete parameter object by controller id.<br>
     * 
     * @param objId controller id.
     * @throws ServiceException if exception happens in DB.
     * @since SDNO 0.5
     */
    void deleteCommParamsByObjId(String objId) throws ServiceException;

}
