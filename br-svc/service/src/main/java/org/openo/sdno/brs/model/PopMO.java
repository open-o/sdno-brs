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

import org.openo.sdno.brs.validator.rules.StrRule;
import org.openo.sdno.brs.validator.rules.SupportFilter;

/**
 * Model Class of Pop Resource.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-11-30
 */
public class PopMO extends RootEntity {

    /**
     * Name of Pop
     */
    @StrRule(range = "1-255", nullable = false, paramName = "name")
    @SupportFilter
    private String name;

    /**
     * Location of Pop
     */
    @StrRule(range = "0-255", paramName = "location")
    @SupportFilter
    private String location;

    /**
     * Area of Pop
     */
    @StrRule(range = "0-255", paramName = "area")
    @SupportFilter
    private String area;

    /**
     * Source
     */
    @StrRule(range = "1-16", paramName = "source")
    @SupportFilter
    private String source;

    /**
     * Controller Id
     */
    @StrRule(range = "1-36", nullable = false, paramName = "source")
    @SupportFilter
    private String controllerID;

    /**
     * DC Gateway
     */
    @StrRule(range = "0-128", paramName = "dcGW")
    @SupportFilter
    private String dcGW;

    /**
     * Internet Gateway
     */
    @StrRule(range = "0-128", paramName = "internetGW")
    @SupportFilter
    private String internetGW;

    /**
     * Site Gateway
     */
    @StrRule(range = "0-128", paramName = "siteGW")
    @SupportFilter
    private String siteGW;

    /**
     * Managed Gateway
     */
    @StrRule(range = "0-128", paramName = "managedGW")
    @SupportFilter
    private String managedGW;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getControllerID() {
        return controllerID;
    }

    public void setControllerID(String controllerID) {
        this.controllerID = controllerID;
    }

    public String getDcGW() {
        return dcGW;
    }

    public void setDcGW(String dcGW) {
        this.dcGW = dcGW;
    }

    public String getInternetGW() {
        return internetGW;
    }

    public void setInternetGW(String internetGW) {
        this.internetGW = internetGW;
    }

    public String getSiteGW() {
        return siteGW;
    }

    public void setSiteGW(String siteGW) {
        this.siteGW = siteGW;
    }

    public String getManagedGW() {
        return managedGW;
    }

    public void setManagedGW(String managedGW) {
        this.managedGW = managedGW;
    }
}
