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
 * Link resource MO.<br/>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class TopologicalLinkMO extends RootEntity {

    protected String name;

    private String layerRate;

    private String aEnd;

    private String zEnd;

    private String aEndME;

    private String zEndME;

    private String phyBW;

    private String owner;

    private String direction;

    /**
     * link type
     */
    private String logicalType;

    private String source;

    private String adminState;

    private String operState;

    private String nativeID;

    private String latency;

    /**
     * Constructor.<br/>
     * 
     * @since SDNO 0.5
     */
    public TopologicalLinkMO() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the layerRate.
     */
    public String getLayerRate() {
        return layerRate;
    }

    /**
     * @param layerRate The layerRate to set.
     */
    public void setLayerRate(String layerRate) {
        this.layerRate = layerRate;
    }

    /**
     * @return Returns the aEnd.
     */
    public String getaEnd() {
        return aEnd;
    }

    /**
     * @param aEnd The aEnd to set.
     */
    public void setaEnd(String aEnd) {
        this.aEnd = aEnd;
    }

    /**
     * @return Returns the zEnd.
     */
    public String getzEnd() {
        return zEnd;
    }

    /**
     * @param zEnd The zEnd to set.
     */
    public void setzEnd(String zEnd) {
        this.zEnd = zEnd;
    }

    /**
     * @return Returns the aEndME.
     */
    public String getaEndME() {
        return aEndME;
    }

    /**
     * @param aEndME The aEndME to set.
     */
    public void setaEndME(String aEndME) {
        this.aEndME = aEndME;
    }

    /**
     * @return Returns the zEndME.
     */
    public String getzEndME() {
        return zEndME;
    }

    /**
     * @param zEndME The zEndME to set.
     */
    public void setzEndME(String zEndME) {
        this.zEndME = zEndME;
    }

    /**
     * @return Returns the phyBW.
     */
    public String getPhyBW() {
        return phyBW;
    }

    /**
     * @param phyBW The phyBW to set.
     */
    public void setPhyBW(String phyBW) {
        this.phyBW = phyBW;
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
        this.owner = owner;
    }

    /**
     * @return Returns the direction.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction The direction to set.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return Returns the logicalType.
     */
    public String getLogicalType() {
        return logicalType;
    }

    /**
     * @param logicalType The logicalType to set.
     */
    public void setLogicalType(String logicalType) {
        this.logicalType = logicalType;
    }

    /**
     * @return Returns the source.
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return Returns the adminState.
     */
    public String getAdminState() {
        return adminState;
    }

    /**
     * @param adminState The adminState to set.
     */
    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }

    /**
     * @return Returns the operState.
     */
    public String getOperState() {
        return operState;
    }

    /**
     * @param operState The operState to set.
     */
    public void setOperState(String operState) {
        this.operState = operState;
    }

    public String getNativeID() {
        return nativeID;
    }

    public void setNativeID(String nativeID) {
        this.nativeID = nativeID;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return "TopologicalLinkMO [logicalType=" + logicalType + ", layerRate=" + layerRate + ", direction=" + direction
                + ", source=" + source + ", owner=" + owner + ", " + "adminState=" + adminState + ", operState="
                + operState + ", phyBW=" + phyBW + ", aEnd=" + aEnd + ", zEnd=" + zEnd + ", aEndME=" + aEndME
                + ",zEndME=" + zEndME + ", id=" + id + ", name=" + name + ", description=" + description
                + ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
    }

}
