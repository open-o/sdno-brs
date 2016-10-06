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

import org.openo.sdno.mss.combine.intf.InvStatisticsService;
import org.openo.sdno.mss.dao.intf.InvStatisticsHandler;
import org.openo.sdno.mss.dao.multi.DataSourceCtrler;

/**
 * Inventory statistics service class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class InvStatisticsServiceImpl implements InvStatisticsService {

    private InvStatisticsHandler statHandler;

    public void setStatHandler(InvStatisticsHandler statHandler) {
        this.statHandler = statHandler;
    }

    @Override
    public long total(String bktName, String resType, String attr, String filter) {
        DataSourceCtrler.add(bktName);
        try {
            return statHandler.total(resType, attr, filter);
        } finally {
            DataSourceCtrler.remove();
        }
    }
}
