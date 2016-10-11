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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.sdno.mss.dao.util.SpringContextUtil;
import org.openo.sdno.mss.schema.datamodel.DataModelFilter;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;

import mockit.Mock;
import mockit.MockUp;

/**
 * ModelMgrUtil Test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class ModelMgrUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetFilterDescException() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };

        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, Datamodel> getDataModelMap(String bktName) {
                Map<String, Datamodel> tmp = new HashMap<>();
                Datamodel model = new Datamodel();
                DataModelFilter filter = new DataModelFilter();
                filter.setName("filter1");
                model.getFilter().add(filter);
                tmp.put("m1", model);
                return tmp;
            }
        };

        ModelMgrUtil.getInstance().getFilterDesc("m1", "filterName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFilterDescExceptionWithEmptyInput() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };

        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, Datamodel> getDataModelMap(String bktName) {
                Map<String, Datamodel> tmp = new HashMap<>();
                Datamodel model = new Datamodel();
                DataModelFilter filter = new DataModelFilter();
                filter.setName("filter1");
                model.getFilter().add(filter);
                tmp.put("m1", model);
                return tmp;
            }
        };

        ModelMgrUtil.getInstance().getFilterDesc("m1", "");
    }

    @Test()
    public void testGetFilterDesc() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };

        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, Datamodel> getDataModelMap(String bktName) {
                Map<String, Datamodel> tmp = new HashMap<>();
                Datamodel model = new Datamodel();
                DataModelFilter filter = new DataModelFilter();
                filter.setName("filter1");
                model.getFilter().add(filter);
                tmp.put("m1", model);
                return tmp;
            }
        };

        ModelMgrUtil.getInstance().getFilterDesc("m1", "filter1");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetUniqueAttrException() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };

        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, String> getResUniqueIndexMap(String bktName) {
                Map<String, String> tmp = new HashMap<>();
                tmp.put("01", "str1");
                return tmp;
            }
        };

        ModelMgrUtil.getInstance().getUniqueAttr("resType");
    }

    @Test()
    public void testGetUniqueAttr() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };

        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, String> getResUniqueIndexMap(String bktName) {
                Map<String, String> tmp = new HashMap<>();
                tmp.put("01", "str1");
                return tmp;
            }
        };

        assertEquals("str1", ModelMgrUtil.getInstance().getUniqueAttr("01"));

    }

    @Test()
    public void testGetRelaModelMap() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };
        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap(String bktName) {

                return null;
            }
        };
        assertNull(ModelMgrUtil.getInstance().getRelaModelMap());
    }

    @Test()
    public void testGetDataName2InfoNames() {
        new MockUp<SpringContextUtil>() {

            @Mock
            public <T> T getBean(String name, Class<T> requiredType) {

                return (T)new ModelManagement();

            }
        };
        new MockUp<ModelManagement>() {

            @Mock
            public Map<String, String> getDataName2InfoNames(String bktName) {

                return null;
            }
        };

        assertNull(ModelMgrUtil.getInstance().getDataName2InfoNames());
    }

}
