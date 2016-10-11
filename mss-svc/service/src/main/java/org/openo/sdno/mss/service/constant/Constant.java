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

package org.openo.sdno.mss.service.constant;

/**
 * MSS service constant.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class Constant {

    public static final String OPERATE_SUCCESS = "200:OK";

    public static final String OBJECTS_KEY = "objects";

    public static final String DST_TYPE = "dst_type";

    public static final int SERVICE_ERROR_CODE = 599;

    public static final String KEY_UUID = "uuid";

    public static final String KEY_ID = "id";

    public static final String[] UUIDS = {"uuid", "src_uuid", "dst_uuid", "-uuid", "-src_uuid", "-dst_uuid"};

    public static final String[] IDS = {"id", "src_id", "dst_id", "-id", "-src_id", "-dst_id"};

    public static final CharSequence COMMA = ",";

    public static final int DEFAULT_PAGESIZE = 1000;

    public static final String OBJECT_KEY = "object";

    public static final String RELATIONS_KEY = "relationships";

    public static final int BAD_PARAM = 400;

    private Constant() {

    }
}
