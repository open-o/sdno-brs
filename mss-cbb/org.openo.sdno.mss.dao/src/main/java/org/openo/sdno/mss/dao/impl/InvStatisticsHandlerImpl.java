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

package org.openo.sdno.mss.dao.impl;

import org.apache.commons.lang.StringUtils;

import org.openo.sdno.mss.dao.intf.InvStatisticsHandler;
import org.openo.sdno.mss.dao.pojo.InvStatPojo;
import org.openo.sdno.mss.dao.util.ValidUtil;

/**
 * The class of statistics handler about inventory. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class InvStatisticsHandlerImpl extends AbstractHandlerImpl implements InvStatisticsHandler {

    @Override
    public long total(String resType, String attr, String filter) {
        ValidUtil.checkResType(resType);

        ValidUtil.checkAttributes(resType, attr, true);

        ValidUtil.checkFilter(resType, filter);

        InvStatPojo pojo = new InvStatPojo(resType);
        if(!StringUtils.isEmpty(attr)) {
            pojo.setCountAttrName(attr);
        }

        if(!StringUtils.isEmpty(filter)) {
            pojo.setFilter(filter);
        }

        return pojo.getCount(getSqlSession());
    }

}
