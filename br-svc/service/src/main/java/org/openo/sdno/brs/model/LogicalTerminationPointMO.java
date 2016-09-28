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

import org.openo.sdno.brs.validator.rules.StrEnumRule;
import org.openo.sdno.brs.validator.rules.StrRule;
import org.openo.sdno.brs.validator.rules.SupportFilter;

/**
 * Logical Termination Point module.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public final class LogicalTerminationPointMO extends RootEntity {

    @StrRule(regex = "[A-Za-z0-9_/]{1,255}", matches = true, nullable = false, paramName = "name")
    @SupportFilter
    private String name;

    @StrRule(range = "1-36", nullable = false, paramName = "meID")
    @SupportFilter
    private String meID;

    @StrEnumRule(range = "ETH,POS,Trunk,Loopback", nullable = false, paramName = "logicalType")
    @SupportFilter
    private String logicalType;

    @StrRule(range = "0-32", paramName = "layerRate")
    private String layerRate;

    @StrEnumRule(range = "true,false", paramName = "isEdgePoint")
    @SupportFilter
    private String isEdgePoint;

    @StrRule(range = "0-255", paramName = "portIndex")
    private String portIndex;

    @StrEnumRule(range = "network_me,os,network_ems,user", paramName = "source")
    @SupportFilter
    private String source;

    @StrRule(range = "0-36", paramName = "owner")
    @SupportFilter
    private String owner;

    @StrRule(range = "0-128", paramName = "ipAddress")
    @SupportFilter
    private String ipAddress;

    @StrEnumRule(range = "active,inactive", paramName = "adminState")
    @SupportFilter
    private String adminState;

    @StrEnumRule(range = "up,down,unknown", paramName = "operState")
    private String operState;

    @StrEnumRule(range = "D_NA,D_BIDIRECTIONAL,D_SOURCE,D_SINK", paramName = "direction")
    private String direction;

    @StrRule(range = "0-64", paramName = "phyBW")
    private String phyBW;

    @StrRule(range = "0-64", paramName = "ipMask")
    private String ipMask;

    @StrRule(range = "0-64", paramName = "nativeID")
    @SupportFilter
    private String nativeID;

    @StrRule(range = "0-24", paramName = "macAddress")
    private String macAddress;

    @StrRule(range = "0-36", paramName = "tenantID")
    @SupportFilter
    private String tenantID;

    @StrEnumRule(range = "unused,used", paramName = "usageState")
    @SupportFilter
    private String usageState;

    @StrRule(range = "0-1023", paramName = "containedLayers")
    private String containedLayers;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public LogicalTerminationPointMO() {
        super();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMeID(String meID) {
        this.meID = meID;
    }

    public String getMeID() {
        return meID;
    }

    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    public String getLogicalType() {
        return logicalType;
    }

    public void setLayerRate(String layerRate) {
        this.layerRate = layerRate;
    }

    public String getLayerRate() {
        return layerRate;
    }

    public void setIsEdgePoint(String isEdgePoint) {
        this.isEdgePoint = isEdgePoint;
    }

    public String getIsEdgePoint() {
        return isEdgePoint;
    }

    public void setPortIndex(String portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortIndex() {
        return portIndex;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }

    public String getAdminState() {
        return adminState;
    }

    public void setOperState(String operState) {
        this.operState = operState;
    }

    public String getOperState() {
        return operState;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setPhyBW(String phyBW) {
        this.phyBW = phyBW;
    }

    public String getPhyBW() {
        return phyBW;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setUsageState(String usageState) {
        this.usageState = usageState;
    }

    public String getUsageState() {
        return usageState;
    }

    public void setContainedLayers(String containedLayers) {
        this.containedLayers = containedLayers;
    }

    public String getContainedLayers() {
        return containedLayers;
    }

    /**
     * Enumeration class,define the type of link.<br>
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
     * Enumeration class, define the rate type of link.<br>
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
     * Enumeration class, define the source of NE.<br>
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
     * Enumeration class, define the state of the link(active, inactive).<br>
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
     * Enumeration class, define the operation state of the link(up, down, unkown).<br>
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
     * Enumeration class,define the direction of the link.<br>
     * 
     * @author
     * @version SDNO 0.5 2016-5-19
     */
    public static class DirectionNum {

        // Unknown
        public static final String UNKNOWN = "D_NA";

        // Carries Traffic Bi-directionally(transmit and receive)
        public static final String BIDIRECTIONAL = "D_BIDIRECTIONAL";

        // Carries Sources Traffic(transmit)
        public static final String TRANSMIT = "D_SOURCE";

        // Carries Sinks Traffic(receive)
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
