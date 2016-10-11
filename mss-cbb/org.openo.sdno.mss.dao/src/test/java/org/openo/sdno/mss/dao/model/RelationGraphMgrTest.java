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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.dao.model.entity.RelaGraphRedisEntity;

import mockit.Mock;
import mockit.MockUp;

/**
 * RelationGraphMgrTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class RelationGraphMgrTest {

    @Before
    public void setUp() throws Exception {
        new MockUp<RelationGraphMgr>() {

            @Mock
            private RelaGraphRedisEntity getRelaGraphEntity(String bktName) {

                return new RelaGraphRedisEntity(null);

            }
        };
    }

    @Test
    public void testFindPathBetweenRes() {
        MockUp<RelaGraphRedisEntity> mock = new MockUp<RelaGraphRedisEntity>() {

            @Mock
            public List<String> findPathBetweenRes(String srcRes, String dstRes) {

                return null;
            }
        };
        RelationGraphMgr mgr = new RelationGraphMgr();
        assertNull(mgr.findPathBetweenRes("bktName", "srcRes", "dstRes"));
        mock.tearDown();
    }

    @Test
    public void testFindCompositonResFromRes() {
        MockUp<RelaGraphRedisEntity> mock = new MockUp<RelaGraphRedisEntity>() {

            @Mock
            public List<String> findCompositonResFromRes(String srcRes, String dstRes) {

                return null;
            }
        };
        RelationGraphMgr mgr = new RelationGraphMgr();
        assertNull(mgr.findCompositonResFromRes("bktName", "res"));
        mock.tearDown();
    }

    @Test
    public void testFindNotCompositonResFromRes() {
        MockUp<RelaGraphRedisEntity> mock = new MockUp<RelaGraphRedisEntity>() {

            @Mock
            public List<String> findNotCompositonResFromRes(String srcRes, String dstRes) {

                return null;
            }
        };
        RelationGraphMgr mgr = new RelationGraphMgr();
        assertNull(mgr.findNotCompositonResFromRes("bktName", "res"));
        mock.tearDown();
    }

    @Test
    public void testIsEnd() {
        MockUp<RelaGraphRedisEntity> mock = new MockUp<RelaGraphRedisEntity>() {

            @Mock
            public boolean isEnd(String res) {

                return true;
            }
        };
        RelationGraphMgr mgr = new RelationGraphMgr();
        assertTrue(mgr.isEnd("bktName", "res"));
        mock.tearDown();
    }
}
