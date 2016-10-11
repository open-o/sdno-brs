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

package org.openo.sdno.mss.service.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openo.sdno.framework.container.util.PageQueryResult;
import org.openo.sdno.mss.service.constant.Constant;

/**
 * Utility class of BatchQuery.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-8-23
 */
public class BatchQueryUtil {

    private BatchQueryUtil() {

    }

    /**
     * Get Query Result from Response Entity.<br>
     * 
     * @param pageNum page number
     * @param pageSize page size
     * @param countList list of count
     * @return page query result
     * @since SDNO 0.5
     */
    public static PageQueryResult<Object> getBatchQueryResEntity(String pageNum, String pageSize,
            List<Object> countList) {
        PageQueryResult<Object> resEntity = new PageQueryResult<Object>();

        int page = 0;
        if(!StringUtils.isEmpty(pageNum)) {
            page = Integer.parseInt(pageNum);
        }

        // The default method of general query shows 1000 data in a page
        int size = Constant.DEFAULT_PAGESIZE;
        if(!StringUtils.isEmpty(pageSize)) {
            size = Integer.parseInt(pageSize);
        }

        int total = 0;
        if(null != countList && !countList.isEmpty()) {
            total = (int)countList.get(0);
        }

        int totalPageNum = total % size > 0 ? ((total / size) + 1) : (total / size);

        resEntity.setTotalPageNum(totalPageNum);
        resEntity.setCurrentPage(page);
        resEntity.setPageSize(size);
        resEntity.setTotal(total);

        return resEntity;
    }
}
