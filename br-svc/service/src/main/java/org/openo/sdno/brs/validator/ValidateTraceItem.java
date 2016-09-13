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

import java.util.List;

/**
 * Trace item class of validating.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public class ValidateTraceItem {

    /**
     * Key value of attribute.
     */
    private final String attrNameKey;

    /**
     * The object to be validated.
     */
    private final Object validData;

    /**
     * Collection of attributes to be validated.
     */
    private List<AttrValidateInfo> lstAttrValidInfo;

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     * @param attrNameKey Key value of attribute
     * @param validData The object to be validated
     */
    public ValidateTraceItem(String attrNameKey, Object validData) {
        super();
        this.attrNameKey = attrNameKey;
        this.validData = validData;
    }

    public String getAttrNameKey() {
        return attrNameKey;
    }

    public Object getValidData() {
        return validData;
    }

    public List<AttrValidateInfo> getLstAttrValidInfo() {
        return lstAttrValidInfo;
    }

    public void setLstAttrValidInfo(List<AttrValidateInfo> lstAttrValidInfo) {
        this.lstAttrValidInfo = lstAttrValidInfo;
    }

}
