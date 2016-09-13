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

package org.openo.sdno.mss.dao.intf;

/**
 * Inventory Statistics handler Interface.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 21, 2016
 */
public interface IInvStatisticsHandler {

    /**
     * Get record counts Interface.<br>
     * 
     * @param resType resource type
     * @param attr attribute
     * @param filter filter condition
     * @return record counts
     * @since SDNO 0.5
     */
    long total(String resType, String attr, String filter);
}
