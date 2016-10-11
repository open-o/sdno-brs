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

package org.openo.sdno.brs.util.http;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.exception.ErrorCode;
import org.openo.sdno.brs.exception.HttpCode;
import org.openo.sdno.brs.util.json.JsonUtil;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP client utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public final class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    // Only temporary, later need to change
    private HttpClientUtil() {
    }

    /**
     * Execute GET request.<br>
     * 
     * @param url URL
     * @param httpHeaders HTTP headers
     * @return Restful response
     * @throws ServiceException when operate failed
     * @since SDNO 0.5
     */
    public static RestfulResponse get(final String url, final Map<String, String> httpHeaders) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();
        for(Entry<String, String> entry : httpHeaders.entrySet()) {
            restfulParametes.put(entry.getKey(), entry.getValue());
        }
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.putHttpContextHeader(Constant.REQUEST_OWNER, Constant.BUCKET_OWNER);
        return RestfulProxy.get(url, restfulParametes);
    }

    /**
     * Execute POST request.<br>
     * 
     * @param url URL
     * @param sendObj object
     * @return Restful response
     * @throws ServiceException when operate failed
     * @since SDNO 0.5
     */
    public static RestfulResponse post(final String url, Object sendObj) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.putHttpContextHeader(Constant.REQUEST_OWNER, Constant.BUCKET_OWNER);
        if(sendObj != null) {
            String strJsonReq = transferRequest(sendObj);
            restfulParametes.setRawData(strJsonReq);
        }
        return RestfulProxy.post(url, restfulParametes);
    }

    /**
     * Execute PUT request.<br>
     * 
     * @param url URL
     * @param sendObj object
     * @return Restful response
     * @throws ServiceException when operate failed
     * @since SDNO 0.5
     */
    public static RestfulResponse put(final String url, Object sendObj) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.putHttpContextHeader(Constant.REQUEST_OWNER, Constant.BUCKET_OWNER);
        if(sendObj != null) {
            String strJsonReq = transferRequest(sendObj);
            restfulParametes.setRawData(strJsonReq);
        }
        return RestfulProxy.put(url, restfulParametes);
    }

    /**
     * Execute DELETE request.<br>
     * 
     * @param url URL
     * @return Restful response
     * @throws ServiceException when operate failed
     * @since SDNO 0.5
     */
    public static RestfulResponse delete(final String url) throws ServiceException {
        final RestfulParametes restfulParametes = new RestfulParametes();
        restfulParametes.putHttpContextHeader("Content-Type", "application/json;charset=UTF-8");
        restfulParametes.putHttpContextHeader(Constant.REQUEST_OWNER, Constant.BUCKET_OWNER);
        return RestfulProxy.delete(url, restfulParametes);
    }

    /**
     * Transform request to JSON string.<br>
     * 
     * @param obj object
     * @return JSON string
     * @throws ServiceException when transform failed
     * @since SDNO 0.5
     */
    public static String transferRequest(Object obj) throws ServiceException {
        String strJsonReq = "";
        try {
            strJsonReq = JsonUtil.marshal(obj);
        } catch(IOException e) {
            LOGGER.debug("transferRequest fail ", e);
            throw new ServiceException(ErrorCode.BRS_BAD_PARAM, HttpCode.BAD_REQUEST);
        }
        return strJsonReq;
    }
}
