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

package org.openo.sdno.mss.dao.multi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data source controller. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class DataSourceCtrler {

    /**
     * Map of thread to bucket, key - thread name + thread ID, value - bucket name.
     */
    private static Map<String, String> thread2Bucket = new ConcurrentHashMap<String, String>();

    private DataSourceCtrler() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Add a bucket name to buffer. <br>
     * 
     * @param bktName Bucket name.
     * @since SDNO 0.5
     */
    public static synchronized void add(String bktName) {
        thread2Bucket.put(Thread.currentThread().getName() + "-" + Thread.currentThread().getId(), bktName);
    }

    /**
     * Get a bucket from buffer. <br>
     * 
     * @return
     * @since SDNO 0.5
     */
    public static synchronized String get() {
        return thread2Bucket.get(Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
    }

    /**
     * Delete a bucket from buffer. <br>
     * 
     * @since SDNO 0.5
     */
    public static synchronized void remove() {
        thread2Bucket.remove(Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
    }
}
