/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.brs.model;

import java.util.List;

/**
 * Managed element module, usually used for net element.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public final class ManagedElementMO extends Resource {

    /**
     * user name.
     */
    private String name;

    /**
     * Logical id of NE, used to match the NE collected from controller.
     */
    private String logicID;

    /**
     * Physical NE ID.
     */
    private String phyNeID;

    /**
     * Product name, such like：NE40E、CX600.
     */
    private String productName;

    /**
     * if it is virtual,1:yes 0:no.
     */
    private String isVirtual;

    /**
     * manage IP address.
     */
    private String ipAddress;

    /**
     * source of NE. scope：NETWORK_ME（get from network）、OS（get from controller）、NETWORK_EMS（get from
     * EMS）、USER（user import）.
     */
    private String source;

    /**
     * owner. like：from controller，fill in controller UUID；from NE，fill in NE UUID.
     */
    private String owner;

    /**
     * manage state:active/inactive.
     */
    private String adminState;

    /**
     * running state: up/down/unkonwn.
     */
    private String operState;

    /**
     * serial number.
     */
    private String serialNumber;

    /**
     * NE role
     */
    private String neRole;

    /**
     * manufacturer name.
     */
    private String manufacturer;

    /**
     * manufacture date.
     */
    private String manufactureDate;

    /**
     * location information.
     */
    private String location;

    /**
     * manage domain ID
     */
    private String managementDomainID;

    /**
     * controller ID
     */
    private List<String> controllerID;

    /**
     * site ID
     */
    private List<String> siteID;

    /**
     * native id from outer system,distributed when get resource from outer system. Like the uuid of
     * NE allocated from AC controller.
     */
    private String nativeID;

    /**
     * manage domain ID
     */
    private List<String> networkControlDomainID;

    /**
     * Constructor.<br/>
     * 
     * @since SDNO 0.5
     */
    public ManagedElementMO() {
        super();
    }

    public List<String> getNetworkControlDomainID() {
        return networkControlDomainID;
    }

    public void setNetworkControlDomainID(List<String> networkControlDomainID) {
        this.networkControlDomainID = networkControlDomainID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogicID() {
        return logicID;
    }

    public void setLogicID(String logicID) {
        this.logicID = logicID;
    }

    public String getPhyNeID() {
        return phyNeID;
    }

    public void setPhyNeID(String phyNeID) {
        this.phyNeID = phyNeID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(String isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAdminState() {
        return adminState;
    }

    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }

    public String getOperState() {
        return operState;
    }

    public void setOperState(String operState) {
        this.operState = operState;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Returns the managementDomainID.
     */
    public String getManagementDomainID() {
        return managementDomainID;
    }

    /**
     * @param managementDomainID The managementDomainID to set.
     */
    public void setManagementDomainID(String managementDomainID) {
        this.managementDomainID = managementDomainID;
    }

    /**
     * @return Returns the controllerID.
     */
    public List<String> getControllerID() {
        return controllerID;
    }

    /**
     * @param controllerID The controllerID to set.
     */
    public void setControllerID(List<String> controllerID) {
        this.controllerID = controllerID;
    }

    /**
     * @return Returns the siteID.
     */
    public List<String> getSiteID() {
        return siteID;
    }

    /**
     * @param siteID The siteID to set.
     */
    public void setSiteID(List<String> siteID) {
        this.siteID = siteID;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public String getNeRole() {
        return neRole;
    }

    public void setNeRole(String neRole) {
        this.neRole = neRole;
    }

}
