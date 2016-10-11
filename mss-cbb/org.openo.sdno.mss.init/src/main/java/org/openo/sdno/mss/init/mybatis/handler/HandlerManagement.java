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

package org.openo.sdno.mss.init.mybatis.handler;

/**
 * Handler management class, you can use it to get all kinds of handler instance. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class HandlerManagement {

    private static final HandlerManagement INSTANCE = new HandlerManagement();

    /**
     * Get the HandlerManagement class singleton instance. <br>
     * 
     * @since SDNO 0.5
     */
    public static HandlerManagement getInstance() {
        return INSTANCE;
    }

    /**
     * Get BucketHandler class. <br>
     * 
     * @since SDNO 0.5
     */
    public BucketHandler getBucketHandler() {
        return new BucketHandler();
    }

    /**
     * Get ResourceHandler class. <br>
     * 
     * @since SDNO 0.5
     */
    public ResourceHandler getResourceHandler() {
        return new ResourceHandler();
    }

    /**
     * Get RelationHandler class. <br>
     * 
     * @since SDNO 0.5
     */
    public RelationHandler getRelationHandler() {
        return new RelationHandler();
    }

}
