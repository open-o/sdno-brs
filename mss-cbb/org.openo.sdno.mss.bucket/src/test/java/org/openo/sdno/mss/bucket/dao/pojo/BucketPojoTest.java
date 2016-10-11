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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * BucketPojo test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class BucketPojoTest {

    private BucketPojo bucket = new BucketPojo();

    @Test
    public void testSetNull() {
        bucket.setName(null);
        bucket.setOwner(null);
        bucket.setAllowread(null);
        bucket.setAuthorization(null);

        assertNull(bucket.getName());
        assertNull(bucket.getOwner());
        assertNull(bucket.getAllowread());
        assertNull(bucket.getAuthorization());
    }

    @Test
    public void test() {
        bucket.setAllowread("Allowread11");
        bucket.setAuthorization("Authorization22");
        bucket.setName("Name33");
        bucket.setOwner("Owner44");

        assertEquals("Allowread11", bucket.getAllowread());
        assertEquals("Authorization22", bucket.getAuthorization());
        assertEquals("Name33", bucket.getName());
        assertEquals("Owner44", bucket.getOwner());
    }

}
