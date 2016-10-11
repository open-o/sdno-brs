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

package org.openo.sdno.mss.dao.pojo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.openo.sdno.mss.dao.util.intf.ParaCallable;
import org.openo.sdno.mss.schema.infomodel.Datatype;

/**
 * Inventory type convert. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-23
 */
public final class InvTypeConvertor {

    /**
     * Map for data type.
     */
    private Map<Datatype, ParaCallable<Object, ?>> map;

    /**
     * Instance
     */
    private static final InvTypeConvertor INSTANCE = new InvTypeConvertor();

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     */
    private InvTypeConvertor() {
        map = new HashMap<Datatype, ParaCallable<Object, ?>>();
        map.put(Datatype.DATETIME, new ParaCallable<Object, Date>() {

            @Override
            public Date call(Object value) {
                return new Date(Long.parseLong(value.toString()));
            }
        });

        map.put(Datatype.DECIMAL, new ParaCallable<Object, BigDecimal>() {

            @Override
            public BigDecimal call(Object value) {
                return new BigDecimal(value.toString());
            }
        });

        map.put(Datatype.INTEGER, new ParaCallable<Object, Integer>() {

            @Override
            public Integer call(Object value) {
                return Integer.parseInt(value.toString());
            }
        });

        map.put(Datatype.STRING, new ParaCallable<Object, String>() {

            @Override
            public String call(Object value) {
                return value.toString();
            }
        });

        map.put(Datatype.FLOAT, new ParaCallable<Object, Float>() {

            @Override
            public Float call(Object value) {
                return Float.parseFloat(value.toString());
            }
        });

        map.put(Datatype.DOUBLE, new ParaCallable<Object, Double>() {

            @Override
            public Double call(Object value) {
                return Double.parseDouble(value.toString());
            }
        });
    }

    /**
     * Get instance. <br>
     * 
     * @return INSTANCE
     * @since SDNO 0.5
     */
    public static InvTypeConvertor getInstance() {
        return INSTANCE;
    }

    /**
     * Data conversion. <br>
     * 
     * @param type Type.
     * @param value Value.
     * @return Converted data.
     * @since SDNO 0.5
     */
    public Object convert(Datatype type, Object value) {
        if(value == null) {
            return null;
        }

        return map.get(type).call(value);
    }

    /**
     * data convert. <br>
     * 
     * @param resType Resource type.
     * @param attrName Attribute name.
     * @param attrValue Attribute value.
     * @return converted data.
     * @since SDNO 0.5
     */
    public Object convert(String resType, String attrName, Object attrValue) {
        Datatype type = InvCrossTablePojo.getAllAttributes(resType, true).get(attrName);
        return convert(type, attrValue);
    }
}
