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

package org.openo.sdno.mss.bucket.dao.pojo;

/**
 * Resource POJO class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ResourcePojo {

    private String uuid;

    private String bktName;

    private String restype;

    private String imspec;

    private String dmspec;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getBktName() {
        return bktName;
    }

    public void setBktName(String bktName) {
        this.bktName = bktName == null ? null : bktName.trim();
    }

    public String getRestype() {
        return restype;
    }

    public void setRestype(String restype) {
        this.restype = restype == null ? null : restype.trim();
    }

    public String getImspec() {
        return imspec;
    }

    public void setImspec(String imspec) {
        this.imspec = imspec == null ? null : imspec.trim();
    }

    public String getDmspec() {
        return dmspec;
    }

    public void setDmspec(String dmspec) {
        this.dmspec = dmspec == null ? null : dmspec.trim();
    }
}
