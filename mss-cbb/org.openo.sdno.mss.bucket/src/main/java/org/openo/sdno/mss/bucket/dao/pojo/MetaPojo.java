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

package org.openo.sdno.mss.bucket.dao.pojo;

import java.util.List;

/**
 * Meta POJO class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MetaPojo {

    private String bktName = "";

    private RelationPojo relation = null;

    private List<ResourcePojo> resource = null;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param bktName Bucket name
     * @param relation Relation POJO
     * @param resource Collection of resource POJO
     */
    public MetaPojo(String bktName, RelationPojo relation, List<ResourcePojo> resource) {
        super();
        this.bktName = bktName;
        this.relation = relation;
        this.resource = resource;
    }

    public MetaPojo() {
        // Constructor.
    }

    public String getBktName() {
        return bktName;
    }

    public void setBktName(String bktName) {
        this.bktName = bktName;
    }

    public RelationPojo getRelation() {
        return relation;
    }

    public void setRelation(RelationPojo relation) {
        this.relation = relation;
    }

    public List<ResourcePojo> getResource() {
        return resource;
    }

    public void setResource(List<ResourcePojo> resource) {
        this.resource = resource;
    }

}
