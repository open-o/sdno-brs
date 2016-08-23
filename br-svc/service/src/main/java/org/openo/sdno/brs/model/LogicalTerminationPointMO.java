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

/**
 * Logical Termination Point module.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public final class LogicalTerminationPointMO extends RootEntity {

    private String name;

    private String meID;

    private String logicalType;

    private String layerRate;

    private String isEdgePoint;

    private String portIndex;

    private String source;

    private String owner;

    private String ipAddress;

    private String adminState;

    private String operState;

    private String direction;

    private String phyBW;

    private String ipMask;

    private String nativeID;

    private String macAddress;

    private String tenantID;

    private String usageState;

    private String containedLayers;

    /**
     * Constructor.<br/>
     * 
     * @since SDNO 0.5
     */
    public LogicalTerminationPointMO() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMeID() {
        return meID;
    }

    public void setMeID(String meID) {
        this.meID = meID;
    }

    public String getLogicalType() {
        return logicalType;
    }

    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    public String getLayerRate() {
        return layerRate;
    }

    public void setLayerRate(String layerRate) {
        this.layerRate = layerRate;
    }

    public String getIsEdgePoint() {
        return isEdgePoint;
    }

    public void setIsEdgePoint(String isEdgePoint) {
        this.isEdgePoint = isEdgePoint;
    }

    public String getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(String portIndex) {
        this.portIndex = portIndex;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhyBW() {
        return phyBW;
    }

    public void setPhyBW(String phyBW) {
        this.phyBW = phyBW;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getUsageState() {
        return usageState;
    }

    public void setUsageState(String usageState) {
        this.usageState = usageState;
    }

    public String getContainedLayers() {
        return containedLayers;
    }

    public void setContainedLayers(String containedLayers) {
        this.containedLayers = containedLayers;
    }

    /**
     * Enumeration class,define the type of link.<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class LogicalTypeNum {

        public static final String ETH = "ETH";

        public static final String POS = "POS";

        public static final String TRUNK = "Trunk";

        private LogicalTypeNum() {

        }
    }

    /**
     * Enumeration class, define the rate type of link.<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class LayerRateNum {

        public static final String ETHERNET = "LR_Ethernet";

        public static final String FAST_ETHERNET = "LR_Fast_Ethernet";

        public static final String GIGABIT_ETHERNET = "LR_Gigabit_Ethernet";

        public static final String PHYSICAL_ELECTRICAL = "LR_PHYSICAL_ELECTRICAL";

        public static final String PHYSICAL_OPTICAL = "LR_PHYSICAL_OPTICAL";

        private LayerRateNum() {

        }
    }

    /**
     * Enumeration class, define the source of NE.<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class SourceNum {

        public static final String NETWORK_ME = "network_me";

        public static final String OS = "os";

        public static final String NETWORK_EMS = "network_ems";

        public static final String USER = "user";

        private SourceNum() {

        }
    }

    /**
     * Enumeration class, define the state of the link(active, inactive).<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class AdminStateNum {

        public static final String ACTIVE = "active";

        public static final String INACTIVE = "inactive";

        private AdminStateNum() {

        }
    }

    /**
     * Enumeration class, define the operation state of the link(up, down, unkown).<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class OperStateNum {

        public static final String UP = "up";

        public static final String DOWN = "down";

        public static final String UNKNOWN = "unknown";

        private OperStateNum() {

        }
    }

    /**
     * Enumeration class,define the direction of the link.<br/>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class DirectionNum {

        // Unknown
        public static final String UNKNOWN = "D_NA";

        // Carries Traffic Bi-directionally（transmit and receive）
        public static final String BIDIRECTIONAL = "D_BIDIRECTIONAL";

        // Carries Sources Traffic(transmit)
        public static final String TRANSMIT = "D_SOURCE";

        // Carries Sinks Traffic（receive）
        public static final String RECEIVE = "D_SINK";

        private DirectionNum() {

        }
    }

    @Override
    public String toString() {
        return "LogicalTerminationPointMO [name=" + name + ", meID=" + meID + ", logicalType=" + logicalType
                + ", layerRate=" + layerRate + ", isEdgePoint=" + isEdgePoint + ", portIndex=" + portIndex + ", source="
                + source + ", owner=" + owner + ", ipAddress=" + ipAddress + ", adminState=" + adminState
                + ", operState=" + operState + ", direction=" + direction + ", phyBW=" + phyBW + ", ipMask=" + ipMask
                + ", nativeID=" + nativeID + ", macAddress=" + macAddress + ", tenantID=" + tenantID + ", usageState="
                + usageState + ", containedLayers=" + containedLayers + "]";
    }

}
