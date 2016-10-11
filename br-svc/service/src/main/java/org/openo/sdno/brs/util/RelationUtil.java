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

import java.util.ArrayList;
import java.util.List;

import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.model.Relation;

/**
 * Relation utility class.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-24
 */
public class RelationUtil {

    private RelationUtil() {
        // Private constructor to prohibit instantiation.
    }

    /**
     * Convert relation.<br>
     * 
     * @param lstID list of IDs
     * @param strSrcID source ID
     * @param strDstType destination type
     * @return list of relations
     * @since SDNO 0.5
     */
    public static List<Relation> convertRelation(List<String> lstID, String strSrcID, String strDstType) {
        if(null == lstID) {
            return null;
        }

        List<Relation> lstRelation = new ArrayList<Relation>();
        for(String strDstID : lstID) {
            Relation relation = new Relation();
            relation.setSrcId(strSrcID);
            relation.setDstId(strDstID);
            relation.setDstType(strDstType);
            relation.setRelation(Constant.REALTION_ASSOCIATION);
            lstRelation.add(relation);
        }

        return lstRelation;
    }
}
