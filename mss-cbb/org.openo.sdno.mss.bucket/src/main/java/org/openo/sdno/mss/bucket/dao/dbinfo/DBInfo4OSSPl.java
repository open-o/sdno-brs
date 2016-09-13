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
 * Class of data source information acquisition.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-18
 */
public class DBInfo4OSSPl implements IDBInfo {

    public static final String SERVER_NAME = "serverName";

    public static final String PWD = "passwd";

    public static final String USER = "user";

    public static final String PORT = "port";

    public static final String DB_NAME = "bucketsys";

    @Override
    public String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://localhost:3306/bucketsys?useUnicode=true&characterEncoding=UTF-8";
    }

    @Override
    public String getUser() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "Test_12345";
    }

    @Override
    public void destroyPassword() {
        // The password is assigned as constant, and no need to destroy.
    }

    @Override
    public void init() {
        // The member variables are assigned as constant, and no initialization is required.
    }

    /**
     * Destroy password.<br>
     * 
     * @since SDNO 0.5
     */
    public void destroy() {
        this.destroyPassword();
    }
}
