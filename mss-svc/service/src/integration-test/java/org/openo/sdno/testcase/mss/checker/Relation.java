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

package org.openo.sdno.testcase.mss.checker;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Relation definition class, describe the relation between two object.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class Relation {

    @JsonProperty("src_id")
    private String srcId;

    @JsonProperty("dst_id")
    private String dstId;

    private String relation;

    private String serviceType;

    @JsonProperty("dst_type")
    private String dstType;

    /**
     * @param srcId The srcId to set.
     */
    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    /**
     * @return Returns the srcId.
     */
    public String getSrcId() {
        return srcId;
    }

    /**
     * @return Returns the dstId.
     */
    public String getDstId() {
        return dstId;
    }

    /**
     * @param dstId The dstId to set.
     */
    public void setDstId(String dstId) {
        this.dstId = dstId;
    }

    /**
     * @param dstType The dstType to set.
     */
    public void setDstType(String dstType) {
        this.dstType = dstType;
    }

    /**
     * @return Returns the dstType.
     */
    public String getDstType() {
        return dstType;
    }

    /**
     * @param serviceType The serviceType to set.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @return Returns the relation.
     */
    public String getRelation() {
        return relation;
    }

    /**
     * @param relation The relation to set.
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        Relation other = (Relation)obj;

        if(srcId == null) {
            if(other.srcId != null) {
                return false;
            }
        } else if(!srcId.equals(other.srcId)) {
            return false;
        }
        if(dstId == null) {
            if(other.dstId != null) {
                return false;
            }
        } else if(!dstId.equals(other.dstId)) {
            return false;
        }
        if(dstType == null) {
            if(other.dstType != null) {
                return false;
            }
        } else if(!dstType.equals(other.dstType)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((srcId == null) ? 0 : srcId.hashCode());
        result = prime * result + ((dstId == null) ? 0 : dstId.hashCode());
        result = prime * result + ((dstType == null) ? 0 : dstType.hashCode());
        return result;
    }
}
