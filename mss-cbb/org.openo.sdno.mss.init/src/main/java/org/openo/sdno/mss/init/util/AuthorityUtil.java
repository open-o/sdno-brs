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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authority utility class, used to normalize the authority and check the authority is valid. <br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class AuthorityUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityUtil.class);

    private static final String DEFAULT = "private";

    private static final String[] AUTH_LIST = {"private", "public-read"};

    private AuthorityUtil() {

    }

    /**
     * Normalize the authority of bucket, if its authorization is not in the list, give it a default
     * one.
     * <br>
     * 
     * @param auth the bucket object's authority.
     * @return bucket object's authorization if it is in the authorization list, default
     *         authorization if it's not.
     * @since SDNO 0.5
     */
    public static String normalizeAuth(String auth) {
        if(StringUtils.isEmpty(auth)) {
            return DEFAULT;
        }

        if(isAuthInList(auth)) {
            return auth;
        }

        return DEFAULT;
    }

    /**
     * Check if it's a valid authorization <br>
     * 
     * @param auth the bucket's authorization.
     * @return true if it's valid, false if not.
     * @since SDNO 0.5
     */
    public static boolean isValidAuth(String auth) {
        if(StringUtils.isEmpty(auth)) {
            LOGGER.error("Invalid authoriy");
            return false;
        }

        return isAuthInList(auth);

    }

    /**
     * Check whether a authorization is in the authorization list. <br>
     * 
     * @param auth authorization need to check.
     * @return true if the authorization is in the list, false if not.
     * @since SDNO 0.5
     */
    private static boolean isAuthInList(String auth) {
        for(String str : AUTH_LIST) {
            if(auth.equals(str)) {
                return true;
            }
        }
        return false;
    }

}
