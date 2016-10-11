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

/**
 * Filter Replace Class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-08-23
 */
public class FilterReplaceReturn {

    /**
     * where filter SQL
     */
    private String allWhereFilter;

    /**
     * next begin index
     */
    private int nextBeginIndex;

    FilterReplaceReturn(String filter, int index) {
        setAllWhereFilter(filter);
        setNextBeginIndex(index);
    }

    /**
     * @return Returns the allWhereFilter.
     */
    public String getAllWhereFilter() {
        return allWhereFilter;
    }

    /**
     * @param allWhereFilter The allWhereFilter to set.
     */
    public void setAllWhereFilter(String allWhereFilter) {
        this.allWhereFilter = allWhereFilter;
    }

    /**
     * @return Returns the nextBeginIndex.
     */
    public int getNextBeginIndex() {
        return nextBeginIndex;
    }

    /**
     * @param nextBeginIndex The nextBeginIndex to set.
     */
    public void setNextBeginIndex(int nextBeginIndex) {
        this.nextBeginIndex = nextBeginIndex;
    }
}
