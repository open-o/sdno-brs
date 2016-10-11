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

package org.openo.sdno.mss.init.mybatis.pojo;

/**
 * bucket PoJo class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class BucketPojo {

    private String name;

    private String owner;

    private String authorization;

    private String allowread;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name.trim();
    }

    /**
     * @return Returns the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner.trim();
    }

    /**
     * @return Returns the authorization.
     */
    public String getAuthorization() {
        return authorization;
    }

    /**
     * @param authorization The authorization to set.
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization.trim();
    }

    /**
     * @return Returns the allow read.
     */
    public String getAllowread() {
        return allowread;
    }

    /**
     * @param allowread The allow read to set.
     */
    public void setAllowread(String allowread) {
        this.allowread = allowread.trim();
    }

}
