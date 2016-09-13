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

package org.openo.sdno.brs.model;

import java.util.List;

import org.openo.sdno.brs.validator.rules.StrEnumRule;
import org.openo.sdno.brs.validator.rules.StrRule;
import org.openo.sdno.brs.validator.rules.SupportFilter;

/**
 * Site module.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class SiteMO extends RootEntity {

    @StrEnumRule(range = "network_site,tenant_site", nullable = false, paramName = "type")
    @SupportFilter
    private String type;

    @StrRule(range = "0-36", paramName = "tenantID")
    @SupportFilter
    private String tenantID;

    @StrRule(range = "0-255", paramName = "location")
    private String location;

    @StrRule(range = "1-255", nullable = false, paramName = "name")
    @SupportFilter
    protected String name;

    /**
     * the corresponding tenant site of the site on the web side.(1:N)
     */
    @RelationField(dbName = "tenantSiteIDs", modelName = "site", paraName = "tenantSiteIDs")
    @SupportFilter
    private List<String> tenantSiteIDs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getTenantSiteIDs() {
        return tenantSiteIDs;
    }

    public void setTenantSiteIDs(List<String> tenantSiteIDs) {
        this.tenantSiteIDs = tenantSiteIDs;
    }

    @Override
    public String toString() {
        return "SiteMO [type=" + type + ", tenantID=" + tenantID + ", location=" + location + ", id=" + id + ", name="
                + name + ", description=" + description + ", createtime=" + createtime + ", updatetime=" + updatetime
                + "]";
    }

}
