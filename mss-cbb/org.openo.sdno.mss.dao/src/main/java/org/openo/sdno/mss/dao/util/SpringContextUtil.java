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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring context utility class. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public final class SpringContextUtil implements ApplicationContextAware {

    private static final List<ApplicationContext> contexts = new ArrayList<ApplicationContext>();

    private SpringContextUtil() {
        // Private constructor to prohibit instantiation.
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contexts.add(applicationContext);
    }

    /**
     * Get bean. <br>
     * 
     * @param name Name.
     * @param requiredType Required type.
     * @return (T)Object
     * @since SDNO 0.5
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        for(ApplicationContext context : contexts) {
            if(context.containsBean(name)) {
                return (T)context.getBean(name, requiredType);
            }
        }

        throw new IllegalArgumentException("Context not found, name = " + name + ", requiredType = " + requiredType);
    }
}
