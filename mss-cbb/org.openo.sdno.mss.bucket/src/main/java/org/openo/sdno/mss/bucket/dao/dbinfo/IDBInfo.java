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

package org.openo.sdno.mss.bucket.dao.dbinfo;

/**
 * Interface of DB info.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public interface IDBInfo {

    /**
     * @return Returns the driver.
     */
    String getDriver();

    /**
     * @return Returns the URL.
     */
    String getUrl();

    /**
     * @return Returns the user.
     */
    String getUser();

    /**
     * Initialize the basic DB info.<br>
     * 
     * @since SDNO 0.5
     */
    void init();

    /**
     * Get the encrypted password.<br>
     * 
     * @return Encrypted password
     * @since SDNO 0.5
     */
    String getPassword();

    /**
     * Destroy the clear text password in the memory.<br>
     * 
     * @since SDNO 0.5
     */
    void destroyPassword();

}
