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

package org.openo.sdno.mss.dao.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.dao.model.ModelMgrUtil;
import org.openo.sdno.mss.dao.pojo.InvBasicTablePojo;
import org.openo.sdno.mss.dao.pojo.InvCrossTablePojo;
import org.openo.sdno.mss.dao.pojo.InvRelationCombinePojo;
import org.openo.sdno.mss.model.util.PropertiesUtil;
import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.openo.sdno.mss.schema.relationmodel.Relationtype;

import mockit.Mock;
import mockit.MockUp;

/**
 * ValidUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 July 26, 2016
 */
public class ValidUtilTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCheckResTypeNotSupport() {
        MockUp<ModelMgrUtil> mock = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getInfoModelMap() {
                Map<String, Infomodel> result = new HashMap<>();
                Infomodel model = new Infomodel();
                result.put("Model", model);
                return result;

            }
        };
        String type = "Model2";
        ValidUtil.checkResType(type);
        mock.tearDown();
    }

    @Test
    public void testCheckResTypeSuccess() {
        MockUp<ModelMgrUtil> mock = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, Infomodel> getInfoModelMap() {
                Map<String, Infomodel> result = new HashMap<>();
                Infomodel model = new Infomodel();
                result.put("Model", model);
                return result;

            }
        };
        String type = "Model";
        ValidUtil.checkResType(type);
        mock.tearDown();
    }

    @Test
    public void testCheckAttributesAsterisk() {
        ValidUtil.checkAttributes("Test", "*", false);
    }

    @Test
    public void testCheckAttributesEmpty() {
        ValidUtil.checkAttributes("Test", "", false, false);
    }

    @Test
    public void testCheckAttributesAll() {
        ValidUtil.checkAttributes("Test", "all", false, false);
    }

    @Test
    public void testCheckAttributesExt() {
        ValidUtil.checkAttributes("Test", "ext", false, false);
    }

    @Test
    public void testCheckAttributesSuccess() {

        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("string", Datatype.STRING);
                return attrMap;

            }
        };
        ValidUtil.checkAttributes("string", "string", false, false);
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAttributesNotSupport() {

        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };
        Collection<String> attrArray = new ArrayList<>();
        attrArray.add("string");
        ValidUtil.checkAttributes("test", attrArray, false);
        mock.tearDown();
    }

    @Test()
    public void testCheckAttributesCheckhidden() {

        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("string", Datatype.STRING);
                return attrMap;

            }
        };
        Collection<String> attrArray = new ArrayList<>();
        attrArray.add("string");
        ValidUtil.checkAttributes("test", attrArray, true);
        mock.tearDown();
    }

    @Test()
    public void testCheckAttributesCheckRelation() {

        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };

        MockUp<InvRelationCombinePojo> mock2 = new MockUp<InvRelationCombinePojo>() {

            @Mock
            public Map<String, Datatype> getRelationModel() {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("string", Datatype.STRING);
                return attrMap;

            }
        };
        ValidUtil.checkAttributes("string", "string", false, true);
        mock.tearDown();
        mock2.tearDown();
    }

    @Test
    public void testCheckFilterEmpty() {
        ValidUtil.checkFilter("resType", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFilterNotSupport() {
        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };

        HashMap<String, Object> map = new HashMap<>();
        String str = "value01";
        map.put("01", str);
        String filter = JsonUtil.toJson(map);
        ValidUtil.checkFilter("resType", filter);
        mock.tearDown();
    }

    @Test
    public void testCheckFilterSuccess() {
        MockUp<InvCrossTablePojo> mock = new MockUp<InvCrossTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllAttributes(String resType, boolean hasHiddenAttr) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };

        HashMap<String, Object> map = new HashMap<>();
        String str = "value01";
        map.put("str", str);
        String filter = JsonUtil.toJson(map);
        ValidUtil.checkFilter("resType", filter);
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSortEmpty() {

        ValidUtil.checkSort("resType", "attr", "", "refValue");
    }

    @Test()
    public void testCheckSortNull() {

        ValidUtil.checkSort("resType", "attr", "", null);
    }

    @Test()
    public void testCheckSortSuccess() {
        MockUp<InvBasicTablePojo> mock = new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };
        ValidUtil.checkSort("resType", "attr", "str", "refValue");
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSortNotSupport() {
        MockUp<InvBasicTablePojo> mock = new MockUp<InvBasicTablePojo>() {

            @Mock
            public Map<String, Datatype> getAllBasicAttributes(String resType) {
                Map<String, Datatype> attrMap = new HashMap<String, Datatype>();
                attrMap.put("str", Datatype.STRING);
                return attrMap;

            }
        };
        ValidUtil.checkSort("resType", "attr", "strErr", "refValue");
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationTypeNull() {
        ValidUtil.checkRelationType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationTypeFormatErr() {
        ValidUtil.checkRelationType("123456");
    }

    @Test
    public void testCheckRelationTypeSuccess() {
        MockUp<ValidUtil> mock = new MockUp<ValidUtil>() {

            @Mock
            public void checkResType(String resType) {

                return;

            }
        };
        String[] resTypes = {"1", "2"};
        String[] result = ValidUtil.checkRelationType("1-2");
        assertEquals(resTypes.length, result.length);
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationTypeDefinedException() {
        MockUp<ModelMgrUtil> mock = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap() {
                Map<String, RelationModelRelation> result = new HashMap<>();
                RelationModelRelation tmp = new RelationModelRelation();
                result.put("string", tmp);
                return result;

            }
        };
        ValidUtil.checkRelationTypeDefined("");
        mock.tearDown();
    }

    @Test()
    public void testCheckRelationTypeDefinedSuccess() {
        MockUp<ModelMgrUtil> mock = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap() {
                Map<String, RelationModelRelation> result = new HashMap<>();
                RelationModelRelation tmp = new RelationModelRelation();
                result.put("string", tmp);
                return result;

            }
        };
        ValidUtil.checkRelationTypeDefined("string");
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationEnumValueException() {
        MockUp<PropertiesUtil> mock = new MockUp<PropertiesUtil>() {

            @Mock
            public HashMap<String, Integer> getRELATIONTYPEVALUES() {
                HashMap<String, Integer> result = new HashMap<>();

                result.put("01", 1);
                return result;

            }
        };
        ValidUtil.checkRelationEnumValue(0);
        mock.tearDown();
    }

    @Test()
    public void testCheckRelationEnumValueSuccess() {
        MockUp<PropertiesUtil> mock = new MockUp<PropertiesUtil>() {

            @Mock
            public HashMap<String, Integer> getRELATIONTYPEVALUES() {
                HashMap<String, Integer> result = new HashMap<>();

                result.put("01", 1);
                return result;

            }
        };
        ValidUtil.checkRelationEnumValue(1);
        mock.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationException() {
        MockUp<ValidUtil> mock1 = new MockUp<ValidUtil>() {

            @Mock
            public String[] checkRelationType(String resType) {

                return null;

            }
        };
        MockUp<ValidUtil> mock2 = new MockUp<ValidUtil>() {

            @Mock
            public void checkRelationTypeDefined(String resType) {

                return;

            }
        };
        new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap() {
                Map<String, RelationModelRelation> result = new HashMap<>();
                RelationModelRelation tmp = new RelationModelRelation();
                result.put("string", tmp);
                return result;

            }
        };
        ValidUtil.checkRelation("relationType", "composition");
        mock1.tearDown();
        mock2.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckRelationNumException() {
        MockUp<ValidUtil> mock1 = new MockUp<ValidUtil>() {

            @Mock
            public String[] checkRelationType(String resType) {

                return null;

            }
        };
        MockUp<ValidUtil> mock2 = new MockUp<ValidUtil>() {

            @Mock
            public void checkRelationTypeDefined(String resType) {

                return;

            }
        };
        MockUp<ValidUtil> mock3 = new MockUp<ValidUtil>() {

            @Mock
            public void checkRelationEnumValue(int relationTypeIntValue) {

                return;

            }
        };
        MockUp<ModelMgrUtil> mock4 = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap() {
                Map<String, RelationModelRelation> result = new HashMap<>();
                RelationModelRelation tmp = new RelationModelRelation();
                tmp.setType(Relationtype.COMPOSITION);
                result.put("string", tmp);
                return result;

            }
        };

        MockUp<PropertiesUtil> mock5 = new MockUp<PropertiesUtil>() {

            @Mock
            public HashMap<String, Integer> getRELATIONTYPEVALUES() {
                HashMap<String, Integer> result = new HashMap<>();
                result.put("0", 0);
                result.put("1", 1);
                return result;

            }
        };

        ValidUtil.checkRelation("string", "1");
        mock1.tearDown();
        mock2.tearDown();
        mock3.tearDown();
        mock4.tearDown();
        mock5.tearDown();
    }

    @Test()
    public void testCheckRelationSuccess() {
        MockUp<ValidUtil> mock1 = new MockUp<ValidUtil>() {

            @Mock
            public String[] checkRelationType(String resType) {

                return null;

            }
        };
        MockUp<ValidUtil> mock2 = new MockUp<ValidUtil>() {

            @Mock
            public void checkRelationTypeDefined(String resType) {

                return;

            }
        };
        MockUp<ValidUtil> mock3 = new MockUp<ValidUtil>() {

            @Mock
            public void checkRelationEnumValue(int relationTypeIntValue) {

                return;

            }
        };
        MockUp<ModelMgrUtil> mock4 = new MockUp<ModelMgrUtil>() {

            @Mock
            public Map<String, RelationModelRelation> getRelaModelMap() {
                Map<String, RelationModelRelation> result = new HashMap<>();
                RelationModelRelation tmp = new RelationModelRelation();
                tmp.setType(Relationtype.COMPOSITION);
                result.put("string", tmp);
                return result;

            }
        };

        ValidUtil.checkRelation("string", "composition");
        mock1.tearDown();
        mock2.tearDown();
        mock3.tearDown();
        mock4.tearDown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckUuidException() {
        String uuid = "a*b";
        ValidUtil.checkUuid(uuid);
    }

    @Test
    public void testCheckUuid() {
        String uuid = "a-b";
        ValidUtil.checkUuid(uuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckUuidListException() {
        List<String> uuidList = new ArrayList<String>();
        uuidList.add("a*b");
        ValidUtil.checkUuidList(uuidList);
    }

    @Test
    public void testCheckUuidList() {
        List<String> uuidList = new ArrayList<String>();
        uuidList.add("a-b");
        ValidUtil.checkUuidList(uuidList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSplitPageException() {
        ValidUtil.checkSplitPage("a", "b", null);
    }

    @Test
    public void testCheckSplitPage() {
        ValidUtil.checkSplitPage("a", null, null);
    }

    @Test
    public void testCheckSplitPageEmpty() {
        ValidUtil.checkSplitPage("", null, null);
    }

    @Test
    public void testCheckPropertyPrecisionTrue1() {
        boolean result = ValidUtil.checkPropertyPrecision(5, 5);
        assertTrue(result);
    }

    @Test
    public void testCheckPropertyPrecisionTrue2() {
        boolean result = ValidUtil.checkPropertyPrecision(5, 4);
        assertTrue(result);
    }

    @Test
    public void testCheckPropertyPrecisionfalse1() {
        boolean result = ValidUtil.checkPropertyPrecision(0, -1);
        assertFalse(result);
    }

    @Test
    public void testCheckPropertyPrecisionfalse2() {
        boolean result = ValidUtil.checkPropertyPrecision(0, 4);
        assertFalse(result);
    }

    @Test
    public void testCheckPropertyPrecisionfalse3() {
        boolean result = ValidUtil.checkPropertyPrecision(1, 4);
        assertFalse(result);
    }

}
