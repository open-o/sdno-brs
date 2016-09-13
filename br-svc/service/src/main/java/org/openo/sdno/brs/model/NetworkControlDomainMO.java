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

import org.openo.sdno.brs.validator.rules.IntRule;
import org.openo.sdno.brs.validator.rules.IpRule;
import org.openo.sdno.brs.validator.rules.StrEnumRule;
import org.openo.sdno.brs.validator.rules.StrRule;
import org.openo.sdno.brs.validator.rules.SupportFilter;

/**
 * Network controller domain module.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class NetworkControlDomainMO extends RootEntity {

    @StrRule(regex = "[a-zA-Z0-9\\-_]{1,36}", matches = true, paramName = "nativeID")
    @SupportFilter
    private String nativeID;

    @StrRule(regex = "[^\\\\:\\*\\?\"><\\|/]{1,255}", matches = true, nullable = false, paramName = "name")
    @SupportFilter
    private String name;

    @StrRule(regex = "[^\\\\:\\*\\?\"><\\|/]{1,255}", matches = true, paramName = "userLabel")
    @SupportFilter
    private String userLabel;

    @StrRule(regex = "[a-zA-Z0-9\\-_]{1,36}", matches = true, paramName = "parentNcdID")
    @SupportFilter
    private String parentNcdID;

    @StrRule(regex = "[^\\\\:\\*\\?\"><\\|/]{1,255}", matches = true, paramName = "location")
    private String location;

    @StrRule(regex = "[^\\\\:\\*\\?\"><\\|/]{1,255}", matches = true, paramName = "manufacturer")
    @SupportFilter
    private String manufacturer;

    @IpRule(type = "ipv4", paramName = "ipAddress")
    private String ipAddress;

    @IntRule(max = 65535, min = 0, paramName = "port")
    private int port;

    @StrEnumRule(range = "up,down", paramName = "adminStatus")
    @SupportFilter
    private String adminStatus;

    @StrEnumRule(range = "up,down", paramName = "operateStatus")
    @SupportFilter
    private String operateStatus;

    @RelationField(dbName = "managementElementIDs", modelName = "networkcontroldomain", paraName = "managementElementIDs")
    private List<String> managementElementIDs;

    public List<String> getManagementElementIDs() {
        return managementElementIDs;
    }

    public void setManagementElementIDs(List<String> managementElementIDs) {
        this.managementElementIDs = managementElementIDs;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getParentNcdID() {
        return parentNcdID;
    }

    public void setParentNcdID(String parentNcdID) {
        this.parentNcdID = parentNcdID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

}
