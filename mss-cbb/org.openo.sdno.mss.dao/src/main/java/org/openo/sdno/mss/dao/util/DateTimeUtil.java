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

package org.openo.sdno.mss.dao.util;

/**
 * Date time utility class. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * Get time from 1970 : second. <br>
     * 
     * @return Second from 1970.
     * @since SDNO 0.5
     */
    public static int getCurTime() {
        return (int)(System.currentTimeMillis() / 1000);
    }
}
