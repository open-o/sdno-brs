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

package org.openo.sdno.brs.exception;

/**
 * Error definition class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class ErrorCode {

    public static final String BRS_BAD_PARAM = "brs.bad_param";

    public static final String PARSE_RESPONSCONTENT_FAILED = "parse responsContent failed";

    public static final String BRS_MANAGEDELEMENT_ISUSED = "brs.managedElement.isused";

    public static final String BRS_LOGICALTP_ISUSED = "brs.logicalTerminationPoint.isused";

    public static final String BRS_PAGINGQUERY_PARAMETERS_ERROR = "brs.bad_paging_query_param";

    public static final String BRS_RESOURCE_EXIST = "brs.Resource.Exsit";

    public static final String BRS_RESOURCE_NOT_EXIST = "brs.Resource.Not.Exsit";

    public static final String BRS_RESOURCE_NAME_EXIST = "brs.Resource.Name.Exsit";

    public static final String BRS_RESOURCE_ID_USED = "brs.Resource.Id.Used";

    public static final String BRS_RESOURCE_IP_CONFLICT = "brs.Resource.Ip.Conflict";

    public static final String BRS_RESOURCE_IF_SAME = "brs.Resource.If.Same";

    public static final String BRS_RESOURCE_LINK_EXIST = "brs.Resource.Link.Exist";

    private ErrorCode() {
        // private constructor
    }
}
