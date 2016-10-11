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

package org.openo.sdno.mss.dao.model.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;

/**
 * RelaGraphRedisEntityTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 September 29, 2016
 */
public class RelaGraphRedisEntityTest {

    @Test
    public void testFindPathBetweenRes() {
        // prepare a list of relations to be used: N1->N2, N2->N3, and N1->N3
        Map<String, RelationModelRelation> map = new HashMap<String, RelationModelRelation>();
        RelationModelRelation r1 = new RelationModelRelation();
        RelationModelRelation r2 = new RelationModelRelation();
        RelationModelRelation r3 = new RelationModelRelation();
        r1.setSrc("N1");
        r1.setDst("N2");
        r2.setSrc("N2");
        r2.setDst("N3");
        r3.setSrc("N1");
        r3.setDst("N3");
        map.put("edge1", r1);
        map.put("edge2", r2);
        map.put("edge3", r3);

        // find the shortest path between N1 and N3
        RelaGraphRedisEntity entity = new RelaGraphRedisEntity(null);
        entity.buildRelationGraph(map.values());
        List<String> list1 = entity.findPathBetweenRes("N1", "N3");

        // assertion
        assertEquals("there should be only one string.", list1.size(), 1);
        assertEquals("The shortest path should be N1-N3", list1.toArray()[0], "N1-N3");
    }
}
