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

/**
 * Class of single range, such as from 1 to 4, or just one digit.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class SingleRange implements Cloneable {

    public static final String DEFAULTSPLITER = "-";

    private String mSpliter = DEFAULTSPLITER;

    private int mStart;

    private int mEnd;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public SingleRange() {
        super();
    }

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param start Start value
     * @param end End value
     */
    public SingleRange(final int start, final int end) {
        mStart = Math.min(start, end);
        mEnd = Math.max(start, end);
    }

    /**
     * Get intersection with other single range.<br>
     * 
     * @param range The intersection to get intersection with
     * @return The intersection range
     * @since SDNO 0.5
     */
    public SingleRange intersection(final SingleRange range) {
        if((range == null) || (mStart > range.mEnd) || (mEnd < range.mStart)) {
            return null;
        }

        SingleRange result = new SingleRange();
        result.mStart = Math.max(mStart, range.mStart);
        result.mEnd = Math.min(mEnd, range.mEnd);

        return result;
    }

    @Override
    public String toString() {
        if(mStart == mEnd) {
            return Integer.toString(mStart);
        }
        return mStart + mSpliter + mEnd;
    }

    public int getEnd() {
        return mEnd;
    }

    public void setEnd(final int end) {
        this.mEnd = end;
    }

    public String getSpliter() {
        return mSpliter;
    }

    public void setSpliter(final String spliter) {
        mSpliter = spliter;
        if((mSpliter == null) || (mSpliter.length() <= 0)) {
            mSpliter = DEFAULTSPLITER;
        }
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(final int start) {
        this.mStart = start;
    }

    /**
     * Check if the value is contained in the range or not.<br>
     * 
     * @param value The value to be checked
     * @return True if contains, false if not.
     * @since SDNO 0.5
     */
    public boolean contains(final int value) {
        return (mStart <= value) && (mEnd >= value);
    }

    @Override
    public boolean equals(final Object obj) {
        boolean isEqual = false;
        if(obj instanceof SingleRange) {
            SingleRange range = (SingleRange)obj;
            isEqual = (range.mStart == mStart) && (range.mEnd == mEnd) && range.mSpliter.equals(mSpliter);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return mStart << 8 | mEnd;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SingleRange range = null;
        range = (SingleRange)super.clone();
        range.mSpliter = mSpliter;
        range.mStart = mStart;
        range.mEnd = mEnd;
        return range;
    }

    /**
     * Parse range string and build a SingleRange object.<br>
     * 
     * @param rangeStr Range string
     * @param spliter Splitter
     * @return SingleRange object
     * @since SDNO 0.5
     */
    public static SingleRange parse(final String rangeStr, final String spliter) {
        SingleRange range = new SingleRange();
        range.mSpliter = spliter;
        String[] ranges = rangeStr.split(spliter);

        if(ranges.length == 1) {
            range.mStart = Integer.parseInt(ranges[0]);
            range.mEnd = range.mStart;
        } else if(ranges.length == 2) {
            range.mStart = Integer.parseInt(ranges[0]);
            range.mEnd = Integer.parseInt(ranges[1]);

            // If the range is not logical, then the exchange start and end.
            if(range.mEnd < range.mStart) {
                int t = range.mEnd;
                range.mEnd = range.mStart;
                range.mStart = t;
            }
        } else {
            throw new IllegalArgumentException("SingleRange.parse can't accept mutli separators");
        }
        return range;
    }

    /**
     * Parse range string and build a SingleRange object.<br>
     * 
     * @param rangeStr Range string
     * @return SingleRange object
     * @since SDNO 0.5
     */
    public static SingleRange parse(final String rangeStr) {
        return parse(rangeStr, DEFAULTSPLITER);
    }
}
