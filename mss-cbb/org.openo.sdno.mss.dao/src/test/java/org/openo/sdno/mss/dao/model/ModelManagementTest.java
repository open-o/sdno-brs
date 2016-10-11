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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.framework.container.util.Bucket;
import org.openo.sdno.mss.bucket.intf.BucketHandler;
import org.openo.sdno.mss.dao.model.entity.ModelRedisEntity;
import org.openo.sdno.mss.schema.datamodel.Datamodel;

import mockit.Mock;
import mockit.MockUp;

/**
 * ModelManagementTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class ModelManagementTest {

    @Before
    public void setUp() throws Exception {
        new MockUp<ModelManagement>() {

            @Mock
            private ModelRedisEntity getRamModelEntity(String bktName) {

                return new ModelRedisEntity();
            }
        };
    }

    @Test
    public void testGetDataModelMap() {
        MockUp<ModelRedisEntity> mock = new MockUp<ModelRedisEntity>() {

            @Mock
            public Map<String, Datamodel> getDataModelMap() {

                return null;
            }
        };
        ModelManagement mm = new ModelManagement();
        assertNull(mm.getDataModelMap("test"));
        mock.tearDown();
    }

    @Test
    public void testGetDataName2InfoNames() {
        MockUp<ModelRedisEntity> mock = new MockUp<ModelRedisEntity>() {

            @Mock
            Map<String, String> getDataName2InfoNames() {

                return null;
            }
        };
        ModelManagement mm = new ModelManagement();
        assertNull(mm.getDataName2InfoNames("test"));
        mock.tearDown();
    }

    @Test
    public void testGetResUniqueIndexMap() {
        MockUp<ModelRedisEntity> mock = new MockUp<ModelRedisEntity>() {

            @Mock
            Map<String, String> getResUniqueIndexMap() {

                return null;
            }
        };
        ModelManagement mm = new ModelManagement();
        assertNull(mm.getResUniqueIndexMap("test"));
        mock.tearDown();
    }

    @Test
    public void testInitNull() {
        ModelManagement mm = new ModelManagement();
        PriBucketHandler bh = new PriBucketHandler();
        mm.setBucketHandler(bh);
        mm.init();
    }

    @Test
    public void testInitEmpty() {
        MockUp<PriBucketHandler> mock = new MockUp<PriBucketHandler>() {

            @Mock
            public List<Bucket> getBucket() {
                List<Bucket> list = new ArrayList<>();
                return list;
            }
        };

        ModelManagement mm = new ModelManagement();
        PriBucketHandler bh = new PriBucketHandler();
        mm.setBucketHandler(bh);
        mm.init();
        mock.tearDown();
    }

    @Test
    public void testInit() {
        MockUp<PriBucketHandler> mock = new MockUp<PriBucketHandler>() {

            @Mock
            public List<Bucket> getBucket() {
                Bucket bk = new Bucket();
                List<Bucket> list = new ArrayList<>();
                list.add(0, bk);
                return list;
            }
        };

        ModelManagement mm = new ModelManagement();
        PriBucketHandler bh = new PriBucketHandler();
        mm.setBucketHandler(bh);
        mm.init();
        mock.tearDown();
    }

    private class PriBucketHandler implements BucketHandler {

        @Override
        public List<Bucket> getBucket() {

            return null;
        }

    }

}
