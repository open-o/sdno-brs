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

package org.openo.sdno.mss.dao.entities;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Inventory Relation Entity.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 May 19, 2016
 */
public class InvRelationEntity {

    /**
     * source UUID
     */
    private String srcUuid;

    /**
     * destination UUID
     */
    private String dstUuid;

    /**
     * destination resource type
     */
    private String dstType;

    /**
     * business resource type
     */
    private String serviceType;

    /**
     * relation type, composition, aggregation, association
     */
    private String relation;

    /**
     * Get srcUuid attribute.<br>
     * 
     * @return srcUuid attribute
     * @since SDNO 0.5
     */
    public String getSrcUuid() {
        return srcUuid;
    }

    /**
     * Set srcUuid attribute.<br>
     * 
     * @param srcUuid String Object
     * @since SDNO 0.5
     */
    public void setSrcUuid(String srcUuid) {
        this.srcUuid = srcUuid;
    }

    /**
     * Get dstUuid attribute.<br>
     * 
     * @return dstUuid attribute
     * @since SDNO 0.5
     */
    public String getDstUuid() {
        return dstUuid;
    }

    /**
     * Set dstUuid attribute.<br>
     * 
     * @param dstUuid String Object
     * @since SDNO 0.5
     */
    public void setDstUuid(String dstUuid) {
        this.dstUuid = dstUuid;
    }

    /**
     * Get dstType attribute.<br>
     * 
     * @return dstType attribute
     * @since SDNO 0.5
     */
    public String getDstType() {
        return dstType;
    }

    /**
     * Set dstType attribute.<br>
     * 
     * @param dstType String Object
     * @since SDNO 0.5
     */
    public void setDstType(String dstType) {
        this.dstType = dstType;
    }

    /**
     * Get serviceType attribute.<br>
     * 
     * @return serviceType attribute
     * @since SDNO 0.5
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Set serviceType attribute.<br>
     * 
     * @param serviceType String Object
     * @since SDNO 0.5
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Get relation attribute.<br>
     * 
     * @return relation attribute
     * @since SDNO 0.5
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Set relation attribute.<br>
     * 
     * @param relation String Object
     * @since SDNO 0.5
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * Override toString Function.<br>
     * 
     * @return toString Content
     * @since SDNO 0.5
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
