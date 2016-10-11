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
 * Resource PoJo class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class ResourcePojo {

    private String uuid;

    private String bktName;

    private String restype;

    private String imspec;

    private String dmspec;

    /**
     * @return Returns the UUID.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid The UUID to set.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid.trim();
    }

    /**
     * @return Returns the bktName.
     */
    public String getBktName() {
        return bktName;
    }

    /**
     * @param bktName The bktName to set.
     */
    public void setBktName(String bktName) {
        this.bktName = bktName.trim();
    }

    /**
     * @return Returns the res type.
     */
    public String getRestype() {
        return restype;
    }

    /**
     * @param restype The res type to set.
     */
    public void setRestype(String restype) {
        this.restype = restype.trim();
    }

    /**
     * @return Returns the imspec.
     */
    public String getImspec() {
        return imspec;
    }

    /**
     * @param imspec The imspec to set.
     */
    public void setImspec(String imspec) {
        this.imspec = imspec.trim();
    }

    /**
     * @return Returns the dmspec.
     */
    public String getDmspec() {
        return dmspec;
    }

    /**
     * @param dmspec The dmspec to set.
     */
    public void setDmspec(String dmspec) {
        this.dmspec = dmspec.trim();
    }

}
