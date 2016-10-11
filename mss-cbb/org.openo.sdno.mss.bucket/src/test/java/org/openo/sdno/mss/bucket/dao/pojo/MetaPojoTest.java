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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * MetaPojo test class.<br>
 * 
 * @author
 * @version SDNO 0.5 October 11, 2016
 */
public class MetaPojoTest {

    private String BUCKET = "bucketName";

    RelationPojo relation = new RelationPojo();

    List<ResourcePojo> resList = new ArrayList<ResourcePojo>();

    @Test
    public void testConstructor() {
        MetaPojo metaPojo = new MetaPojo(BUCKET, relation, resList);

        assertEquals(BUCKET, metaPojo.getBktName());
        assertEquals(relation, metaPojo.getRelation());
        assertEquals(resList, metaPojo.getResource());
    }

    @Test
    public void test() {
        MetaPojo metaPojo = new MetaPojo();
        metaPojo.setBktName(BUCKET);
        metaPojo.setRelation(relation);
        metaPojo.setResource(resList);

        assertEquals(BUCKET, metaPojo.getBktName());
        assertEquals(relation, metaPojo.getRelation());
        assertEquals(resList, metaPojo.getResource());
    }
}
