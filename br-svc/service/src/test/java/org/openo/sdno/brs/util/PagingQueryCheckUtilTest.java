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

package org.openo.sdno.brs.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;

import org.openo.sdno.brs.model.SiteMO;
import org.openo.sdno.brs.model.roamo.PagingQueryPara;

/**
 * PagingQueryCheckUtil test class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-25
 */
public class PagingQueryCheckUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAnalysicQueryString1() throws ServiceException {

        PagingQueryPara result = PagingQueryCheckUtil.analysicQueryString(null);
        PagingQueryPara result2 = PagingQueryCheckUtil.analysicQueryString("");
        PagingQueryPara emptyPagingQueryPara = new PagingQueryPara();

        assertEquals(result.getFields(), emptyPagingQueryPara.getFields());
        assertEquals(result.getFiltersMap(), emptyPagingQueryPara.getFiltersMap());
        assertEquals(result.getPageNum(), emptyPagingQueryPara.getPageNum());
        assertEquals(result.getPageSize(), emptyPagingQueryPara.getPageSize());
        assertEquals(result2.getFields(), emptyPagingQueryPara.getFields());
        assertEquals(result2.getFiltersMap(), emptyPagingQueryPara.getFiltersMap());
        assertEquals(result2.getPageNum(), emptyPagingQueryPara.getPageNum());
        assertEquals(result2.getPageSize(), emptyPagingQueryPara.getPageSize());

    }

    @Test
    public void testAnalysicQueryString2() throws ServiceException {

        PagingQueryPara result = PagingQueryCheckUtil.analysicQueryString("A=0&B=0");

        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("A", "0");
        queryMap.put("B", "0");

        assertEquals(result.getFields(), "base");
        assertEquals(result.getFiltersMap(), queryMap);
        assertEquals(result.getPageNum(), 0);
        assertEquals(result.getPageSize(), 1000);

    }

    @Test
    public void testAnalysicQueryString3() throws ServiceException {

        PagingQueryPara result = PagingQueryCheckUtil.analysicQueryString("A=0&B=0&pageNum=1&pageSize=500");

        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("A", "0");
        queryMap.put("B", "0");

        assertEquals(result.getFields(), "base");
        assertEquals(result.getFiltersMap(), queryMap);
        assertEquals(result.getPageNum(), 1);
        assertEquals(result.getPageSize(), 500);

    }

    @Test
    public void testCheckPagedParameters1() throws ServiceException {
        PagingQueryPara paraMap = new PagingQueryPara();

        PagingQueryCheckUtil.checkPagedParameters(paraMap, SiteMO.class);
    }

    @Test(expected = ServiceException.class)
    public void testCheckPagedParameters2() throws ServiceException {
        PagingQueryPara paraMap = new PagingQueryPara();
        paraMap.setPageNum(-1);

        PagingQueryCheckUtil.checkPagedParameters(paraMap, SiteMO.class);
    }

    @Test
    public void testCheckPagedParameters3() {
        PagingQueryPara paraMap = new PagingQueryPara();
        paraMap.setPageSize(2000);
        try {
            PagingQueryCheckUtil.checkPagedParameters(paraMap, SiteMO.class);
        } catch(ServiceException e) {
            assertEquals("brs.bad_paging_query_param", e.getId());
        }
    }
}
