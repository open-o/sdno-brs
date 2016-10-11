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

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring context utility class. Provide utility of ApplicationContext and Object.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext arg0) {
        applicationContext = arg0;
    }

    /**
     * Get bean object by ID.<br>
     * 
     * @param id bean ID
     * @return bean object
     * @since SDNO 0.5
     */
    public static Object getBeanById(String id) {
        return applicationContext.getBean(id);
    }
}
