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

package org.openo.sdno.brs.restrepository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.apache.cxf.common.util.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.restrepository.IMSSProxy;
import org.openo.sdno.brs.util.http.HttpClientUtil;


/**
 * Proxy of MSS service.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MSSProxyImpl implements IMSSProxy {

    /**
     * MSS service REST URL
     */
    private static final String MSS_SERVICE_REST_URL_PREFIX = "/openoapi/sdnomss/v1/buckets/";

    private static final String MSS_REST_URL_RESOURCES = "resources";

    private static final String MSS_REST_URL_OBJECT = "objects";

    private static final String MSS_REST_URL_RELATIONOBJECTS = "relation-objects";

    private static final String FORWARD_SLASH = "/";

    private static final String MSS_REST_URL_RELATIONSHIPS = "relationships";

    private static final String MSS_REST_URL_DST_TYPE = "dst_type";

    private static final String MSS_REST_URL_SRC_IDS = "src_ids";

    private static final String MSS_REST_URL_DST_IDS = "dst_ids";

    private static final String MSS_SRC_ID = "src_id";

    private static final String MSS_DST_ID = "dst_id";

    private static final String MSS_REST_URL_CHECKEXIST = "checkexist";

    private static final Object MSS_REST_URL_STATISTICS = "statistics";

    @Override
    public RestfulResponse getResource(final String bucketName, String resourceTypeName, String objectID)
            throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_OBJECT).append(FORWARD_SLASH).append(objectID).append(Constant.PARAM_SPLIT_LABLE)
                .append(Constant.RESOUCRCE_FILEDS).append(Constant.EQUIVALENT).append(Constant.FILEDS_ALL)
                .toString();

        return HttpClientUtil.get(url, new HashMap<String, String>());
    }

    @Override
    public RestfulResponse getResourceList(final String bucketName, String resourceTypeName, String fields,
            String filter, int pagesize, int pagenum) throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_OBJECT)
                .append(buildQuery(Constant.RESOUCRCE_FILEDS, fields, "filter", filter,
                    "pagesize", String.valueOf(pagesize), "pagenum", String.valueOf(pagenum)))
                .toString();

        return HttpClientUtil.get(url, new HashMap<String, String>());
    }

    @Override
    public RestfulResponse getRelations(String bucketName, String resourceTypeName, String dstType, String srcIds,
            String dstIds) throws ServiceException {
        StringBuilder urlbuilder = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_RELATIONSHIPS).append(Constant.PARAM_SPLIT_LABLE)
                .append(MSS_REST_URL_DST_TYPE).append(Constant.EQUIVALENT).append(dstType);

        if((null != srcIds) && (!srcIds.isEmpty())) {
            urlbuilder.append(Constant.AND).append(MSS_REST_URL_SRC_IDS).append(Constant.EQUIVALENT).append(srcIds);
        } else if((null != dstIds) && (!dstIds.isEmpty())) {
            urlbuilder.append(Constant.AND).append(MSS_REST_URL_DST_IDS).append(Constant.EQUIVALENT).append(dstIds);
        }

        return HttpClientUtil.get(urlbuilder.toString(), new HashMap<String, String>());
    }

    @Override
    public RestfulResponse addResources(final String bucketName, String resourceTypeName, Object obj)
            throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_OBJECT).toString();

        return HttpClientUtil.post(url, obj);
    }

    @Override
    public RestfulResponse updateResource(final String bucketName, String resourceTypeName, String objectId, Object obj)
            throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_OBJECT).append(FORWARD_SLASH).append(objectId).toString();

        return HttpClientUtil.put(url, obj);
    }

    @Override
    public RestfulResponse deleteResource(final String bucketName, String resourceTypeName, String objectId)
            throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_OBJECT).append(FORWARD_SLASH).append(objectId).toString();

        return HttpClientUtil.delete(url);
    }

    @Override
    public RestfulResponse deleteRelation(String bucketName, String resourceTypeName, String strSrcID, String strDstID,
            String strDstType) throws ServiceException {
        StringBuilder strBuilder = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName)
                .append(FORWARD_SLASH).append(MSS_REST_URL_RELATIONSHIPS).append(Constant.PARAM_SPLIT_LABLE)
                .append(MSS_REST_URL_DST_TYPE).append(Constant.EQUIVALENT).append(strDstType);

        // Source
        if(!StringUtils.isEmpty(strSrcID)) {
            strBuilder.append(Constant.AND).append(MSS_SRC_ID).append(Constant.EQUIVALENT).append(strSrcID);
        }

        // Destination
        if(!StringUtils.isEmpty(strDstID)) {
            strBuilder.append(Constant.AND).append(MSS_DST_ID).append(Constant.EQUIVALENT).append(strDstID);
        }

        return HttpClientUtil.delete(strBuilder.toString());
    }

    @Override
    public RestfulResponse createRelation(String bucketName, String resourceTypeName, Object obj)
            throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RELATIONSHIPS).toString();

        return HttpClientUtil.post(url, obj);
    }

    @Override
    public RestfulResponse getRelationResourceList(String bucketName, String resourceTypeName, String fields,
            String filter, int pagesize, int pagenum) throws ServiceException {
        // GET
        // /openoapi/sdnomss/v1/{bucket-name}/resources/{resource-type-name}/relation-objects?fileds=attr1,attr2******&filter=******=****&pagenum=***&pagesize=****

        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RELATIONOBJECTS)
                .append(buildQuery(Constant.RESOUCRCE_FILEDS, fields, "filter", filter,
                    "pagesize", String.valueOf(pagesize), "pagenum", String.valueOf(pagenum)))
                .toString();

        return HttpClientUtil.get(url, new HashMap<String, String>());
    }

    @Override
    public RestfulResponse checkObjExsit(String bucketName, String resourceTypeName, Object obj)
            throws ServiceException {
        // /openoapi/sdnomss/v1/buckets/{bucket-name}/resources/{resource-type-name}/checkexist
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_CHECKEXIST).toString();
        return HttpClientUtil.post(url, obj);
    }

    @Override
    public RestfulResponse getResourceListInfo(final String bucketName, String resourceTypeName, String fields,
            String filter, int pagesize, int pagenum) throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_OBJECT).toString();
        Map<String, String> param = new HashMap<String, String>();

        if(null != filter) {
            param.put("filter", filter);
        }
        if(null != fields) {
            param.put("fields", fields);
        }

        int finalPageSize = (pagesize == 0) ? 1000 : pagesize;
        param.put("pagesize", String.valueOf(finalPageSize));
        param.put("pagenum", String.valueOf(pagenum));

        return HttpClientUtil.get(url, param);
    }

    @Override
    public RestfulResponse getManageElementByRelation(String bucketName, String siteResTypeName,
            String resourceTypeName, String dstIds) throws ServiceException {
        String url = new StringBuilder(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RELATIONSHIPS)
                .append(buildQuery(MSS_REST_URL_DST_TYPE, siteResTypeName, MSS_REST_URL_DST_IDS, dstIds))
                .toString();

        return HttpClientUtil.get(url, new HashMap<String, String>());
    }

    @Override
    public RestfulResponse commonQueryCount(String bucketName, String resourceTypeName, String joinAttr, String filter)
            throws ServiceException {
        String url = new StringBuffer(MSS_SERVICE_REST_URL_PREFIX).append(bucketName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_RESOURCES).append(FORWARD_SLASH).append(resourceTypeName).append(FORWARD_SLASH)
                .append(MSS_REST_URL_STATISTICS).append(buildQuery("joinAttr", joinAttr, "filter", filter))
                .toString();

        return HttpClientUtil.get(url, new HashMap<String, String>());
    }

    private String buildQuery(String... nameOrValues) throws ServiceException
    {
        // TODO URLEncodedUtils not follows rfc2369, char in [ !'()~] is not encoded as expected.
        // So a perfect soltion need to be found in future
        if (0 != nameOrValues.length % 2) {
            throw new ServiceException("buildQuery need name-value pairs, but input is " + nameOrValues.toString());
        }

        String[] copiedNameOrValues = nameOrValues;

        List<NameValuePair> nvs = new ArrayList<>();
        for(int i = 0; i < copiedNameOrValues.length; i += 2) {
            String name = copiedNameOrValues[i];
            String value = copiedNameOrValues[i + 1];
            if (StringUtils.isEmpty(value)) {
                // TODO I am not sure is XXX=null or XXX= also a valid condition for service logic.
                continue;
            } else {
                nvs.add(new BasicNameValuePair(name, value));
            }
        }

        if (nvs.isEmpty()) {
            return new String();
        } else {
            return "?" + URLEncodedUtils.format(nvs, "UTF-8");
        }

    }
}
