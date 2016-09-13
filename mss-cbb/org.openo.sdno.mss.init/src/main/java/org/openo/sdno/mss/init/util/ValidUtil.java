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

package org.openo.sdno.mss.init.util;

/**
 * Check the property's precision, the attribute's length can't be less than it's scale. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ValidUtil {

    private static int DEFAULT_STRING_LEN = 255;

    private static int DEFAULT_DECIMAL_LEN = 10;

    private static int DEFAULT_SCALE_LEN = 0;

    private ValidUtil() {

    }

    /**
     * Check if the attribute's length can't be less than it's scale <br>
     * 
     * @param length attribute's length.
     * @param scale attribute's scale.
     * @return true if it's ok, false if it's invalid.
     * @since SDNO 0.5
     */
    public static boolean checkPropertyPrecision(int length, int scale) {
        return length > 0 && length >= scale;
    }
}
