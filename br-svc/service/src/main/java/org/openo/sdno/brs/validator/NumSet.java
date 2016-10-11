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

package org.openo.sdno.brs.validator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Number set class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class NumSet implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumSet.class);

    public static final String DEFAULTSPLITER = ",";

    private String mSpliter = DEFAULTSPLITER;

    private String rangeSpliter = SingleRange.DEFAULTSPLITER;

    private final List<SingleRange> singleRanges = new ArrayList<SingleRange>();

    /**
     * Check the single range list is empty or not.<br>
     * 
     * @return True if empty, false if not.
     * @since SDNO 0.5
     */
    public boolean isEmpty() {
        return singleRanges.isEmpty();
    }

    /**
     * Get max value of the single range list. Return 0 if the list if empty.<br>
     * 
     * @return Max value of the single range list
     * @since SDNO 0.5
     */
    public int getMax() {
        int max = 0;
        if(!isEmpty()) {
            max = singleRanges.get(singleRanges.size() - 1).getEnd();
        }
        return max;
    }

    /**
     * Get minimum value of the single range list. Return 0 if the list if empty.<br>
     * 
     * @return Minimum value of the single range list
     * @since SDNO 0.5
     */
    public int getMin() {
        int max = 0;
        if(!isEmpty()) {
            max = singleRanges.get(0).getStart();
        }
        return max;
    }

    /**
     * Sort the ranges in the collection and merge.<br>
     * 
     * @since SDNO 0.5
     */
    private void adjust() {
        sort();

        int curIndex = 0;
        while(curIndex < singleRanges.size() - 1) {
            SingleRange curRange = singleRanges.get(curIndex);
            SingleRange nextRange = singleRanges.get(curIndex + 1);

            if(curRange.getEnd() + 1 >= nextRange.getStart()) {
                // Merge two ranges.
                curRange.setEnd(Math.max(nextRange.getEnd(), curRange.getEnd()));
                singleRanges.remove(curIndex + 1);
            } else {
                curIndex++;
            }
        }
    }

    private void sort() {
        int len = singleRanges.size();

        for(int i = 0; i < len; i++) {
            int smallIndex = i;
            SingleRange sr = singleRanges.get(smallIndex);

            for(int j = i + 1; j < len; j++) {
                SingleRange tempSR = singleRanges.get(j);
                if(tempSR.getStart() < sr.getStart()) {
                    sr = tempSR;
                    smallIndex = j;
                }
            }

            if(smallIndex != i) {
                singleRanges.set(smallIndex, singleRanges.get(i));
                singleRanges.set(i, sr);
            }
        }
    }

    /**
     * Check if the number is contained in the range.<br>
     * 
     * @param value The value to be checked
     * @return True if contains, false if not.
     * @since SDNO 0.5
     */
    public boolean contains(final int value) {
        int index = -1;

        for(index = singleRanges.size() - 1; index >= 0; index--) {
            SingleRange sr = singleRanges.get(index);
            if(sr.contains(value)) {
                break;
            }
        }
        return index >= 0;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        for(int i = 0, n = singleRanges.size(); i < n; i++) {
            buf.append(singleRanges.get(i).toString());
            buf.append(mSpliter);
        }
        if(buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }
        return buf.toString();
    }

    public String getSpliter() {
        return mSpliter;
    }

    public void setSpliter(final String spliter) {
        mSpliter = spliter;
        if(StringUtils.isEmpty(mSpliter)) {
            mSpliter = DEFAULTSPLITER;
        }
    }

    public List<SingleRange> getRanges() {
        return singleRanges;
    }

    public String getRangeSpliter() {
        return rangeSpliter;
    }

    /**
     * Set splitter of single range.<br>
     * 
     * @param spliter Splitter
     * @since SDNO 0.5
     */
    public void setRangeSpliter(final String spliter) {
        rangeSpliter = spliter;
        if((rangeSpliter == null) || (rangeSpliter.length() <= 0)) {
            rangeSpliter = SingleRange.DEFAULTSPLITER;
        }

        for(int i = 0, n = singleRanges.size(); i < n; i++) {
            SingleRange sr = singleRanges.get(i);
            sr.setSpliter(rangeSpliter);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        NumSet set = null;
        try {
            set = (NumSet)super.clone();
            set.mSpliter = mSpliter;
            set.rangeSpliter = rangeSpliter;

            for(int i = 0, n = singleRanges.size(); i < n; i++) {
                SingleRange sr = singleRanges.get(i);
                set.singleRanges.add((SingleRange)sr.clone());
            }
        } catch(CloneNotSupportedException exp) {
            set = null;
            LOGGER.error("clone failed", exp);
        }
        return set;
    }

    /**
     * Parse set string and build a NumSet object.<br>
     * 
     * @param setString Set string
     * @param setSpliter Set splitter
     * @param rangeSpliter Range splitter
     * @return NumSet object
     * @since SDNO 0.5
     */
    public static NumSet parse(final String setString, final String setSpliter, final String rangeSpliter) {
        String useSetSpliter = setSpliter;
        String useRangeSpliter = rangeSpliter;
        if(StringUtils.isEmpty(useSetSpliter)) {
            useSetSpliter = NumSet.DEFAULTSPLITER;
        }
        if(StringUtils.isEmpty(useRangeSpliter)) {
            useRangeSpliter = SingleRange.DEFAULTSPLITER;
        }

        // Set splitter can not equal with range splitter.
        if(useSetSpliter.equals(useRangeSpliter)) {
            throw new IllegalArgumentException();
        }
        NumSet numSet = new NumSet();
        numSet.setSpliter(setSpliter);

        String setStr = setString;
        if(!StringUtils.isEmpty(setStr)) {
            setStr = setStr.trim();

            // Change user set splitter to default splitter.
            if(useRangeSpliter.contains(useSetSpliter)) {
                setStr = setStr.replaceAll(useRangeSpliter, SingleRange.DEFAULTSPLITER).replaceAll(useSetSpliter,
                        NumSet.DEFAULTSPLITER);
                useSetSpliter = NumSet.DEFAULTSPLITER;
                useRangeSpliter = SingleRange.DEFAULTSPLITER;
            }

            // Parse set splitter.
            String[] rangeStr = Normalizer.normalize(setStr, Normalizer.Form.NFC).split(useSetSpliter);
            for(int i = 0; i < rangeStr.length; i++) {
                SingleRange sr = SingleRange.parse(rangeStr[i], useRangeSpliter);
                sr.setSpliter(rangeSpliter);
                numSet.singleRanges.add(sr);
            }
            numSet.adjust();
        }
        return numSet;
    }

    /**
     * Parse set string and build a NumSet object, using default splitter.<br>
     * 
     * @param setString Set string
     * @return NumSet object
     * @since SDNO 0.5
     */
    public static NumSet parse(final String setString) {
        return parse(setString, NumSet.DEFAULTSPLITER, SingleRange.DEFAULTSPLITER);
    }
}
