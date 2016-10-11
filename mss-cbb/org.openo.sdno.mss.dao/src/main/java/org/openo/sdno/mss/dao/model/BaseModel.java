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

package org.openo.sdno.mss.dao.model;

/**
 * The class of basic model.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class BaseModel {

    private final String srcUuid;

    private final String dstUuid;

    private final String srcAttribute;

    private final String dstAttribute;

    private final String queryType;

    private final String serviceType;

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param srcUuid The source UUID
     * @param dstUuid The destination UUID
     * @param srcAttribute The source attribute
     * @param dstAttribute the destination attribute
     * @param queryType The query type
     * @param serviceType The service type
     */
    public BaseModel(String srcUuid, String dstUuid, String srcAttribute, String dstAttribute, String queryType,
            String serviceType) {
        super();
        this.srcUuid = srcUuid;
        this.dstUuid = dstUuid;
        this.srcAttribute = srcAttribute;
        this.dstAttribute = dstAttribute;
        this.queryType = queryType;
        this.serviceType = serviceType;
    }

    /**
     * @return Returns the srcUuid.
     */
    public String getSrcUuid() {
        return srcUuid;
    }

    /**
     * @return Returns the dstUuid.
     */
    public String getDstUuid() {
        return dstUuid;
    }

    /**
     * @return Returns the srcAttribute.
     */
    public String getSrcAttribute() {
        return srcAttribute;
    }

    /**
     * @return Returns the dstAttribute.
     */
    public String getDstAttribute() {
        return dstAttribute;
    }

    /**
     * @return Returns the queryType.
     */
    public String getQueryType() {
        return queryType;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

}
