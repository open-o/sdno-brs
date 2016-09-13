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

/**
 * controller definition class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-6-3
 */
public class ControllerMo extends RootEntity {

    /**
     * controller uuid.
     */
    private String objectId;

    private String name;

    /**
     * controller type: SNC, AC.
     */
    private String productName;

    private String version;

    private String slaveHostName;

    /**
     * controller ip.
     */
    private String hostName;

    private String vendor;

    private String description;

    /**
     * @param objectId The objectId to set.
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return Returns the objectId.
     */
    public String getObjectId() {
        return objectId;
    }

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
        this.name = name;
    }

    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param productName The productName to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return Returns the slaveHostName.
     */
    public String getSlaveHostName() {
        return slaveHostName;
    }

    /**
     * @return Returns the productName.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param slaveHostName The slaveHostName to set.
     */
    public void setSlaveHostName(String slaveHostName) {
        this.slaveHostName = slaveHostName;
    }

    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Returns the vendor.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor The vendor to set.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
