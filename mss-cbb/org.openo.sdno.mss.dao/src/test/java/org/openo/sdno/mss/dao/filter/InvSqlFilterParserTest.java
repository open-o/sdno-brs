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

package org.openo.sdno.mss.dao.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.dao.pojo.InvBasicTablePojo;
import org.openo.sdno.mss.schema.infomodel.Datatype;

import mockit.Mock;
import mockit.MockUp;

/**
 * InvSqlFilterParserTest class.<br>
 * 
 * @author
 * @version SDNO 0.5 Jul 26, 2016
 */
public class InvSqlFilterParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInvSqlFilterParserSuccess() {
        InvSqlFilterParser sfp = new InvSqlFilterParser(null, "resType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckDataTypeExceptionInteger() {
        String test = "test";
        InvSqlFilterParserUtil.checkDataType(Datatype.INTEGER, test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckDataTypeIntegerExceptionDouble() {
        String test = "test";
        InvSqlFilterParserUtil.checkDataType(Datatype.DOUBLE, test);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildSqlFilterFieldNamesEmpty() {
        Map<String, Object> map = new HashMap<>();
        String tmp = "tmp";
        map.put("1", tmp);
        String jsonStr = JsonUtil.toJson(map);

        InvSqlFilterParser sfp = new InvSqlFilterParser(null, "resType");
        sfp.buildSqlFilter(" and ", jsonStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildSqlFilterJsonValidFieldsEmpty() {
        Map<String, Object> map = new HashMap<>();
        String tmp = "tmp";
        map.put("1", tmp);
        String jsonStr = JsonUtil.toJson(map);

        InvSqlFilterParser sfp = new InvSqlFilterParser(null, "resType");
        sfp.buildSqlFilter("test", jsonStr);
    }

}
