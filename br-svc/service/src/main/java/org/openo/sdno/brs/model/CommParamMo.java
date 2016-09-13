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
 * common parameter definition class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-6-3
 */
public class CommParamMo extends RootEntity {

    /**
     * id of entity which parameter belong to.
     */
    private String objectId;

    private String hostName;

    private String protocol;

    private String port;

    /**
     * user and password information.
     */
    private String commParams;

    /**
     * @return Returns the objectId.
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * @param objectId The objectId to set.
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Returns the protocol.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol The protocol to set.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return Returns the port.
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return Returns the commParams.
     */
    public String getCommParams() {
        return commParams;
    }

    /**
     * @param commParams The commParams to set.
     */
    public void setCommParams(String commParams) {
        this.commParams = commParams;
    }
}
