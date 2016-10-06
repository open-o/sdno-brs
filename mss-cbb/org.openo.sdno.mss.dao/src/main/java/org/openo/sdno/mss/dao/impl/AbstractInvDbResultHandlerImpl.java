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

package org.openo.sdno.mss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract inventory SQL query processor class. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-24
 */
public abstract class AbstractInvDbResultHandlerImpl implements ResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInvDbResultHandlerImpl.class);

    private Exception exception;

    /**
     * It's used to handle result. <br>
     * 
     * @param context The object of ResultContext
     * @since SDNO 0.5
     */
    @Override
    public void handleResult(ResultContext context) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> rowDataMap = (HashMap<String, Object>)context.getResultObject();
            process(context.getResultCount(), rowDataMap);
        } catch(Exception ex) {
            LOGGER.error("Handle result failed.", ex);
            exception = ex;
            context.stop();
        }
    }

    protected abstract void process(int count, Map<String, Object> result) throws Exception;

    public Exception getException() {
        return exception;
    }
}
