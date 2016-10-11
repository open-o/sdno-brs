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

package org.openo.sdno.mss.model.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import org.openo.sdno.mss.schema.infomodel.Datatype;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.infomodel.ObjectFactory;
import org.openo.sdno.mss.schema.infomodel.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model static utility class to clone and turn it to whole info model. <br>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ModelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelUtil.class);

    private static String infoModelPrefix = "im";

    private static String dataModelPrefix = "dm";

    private static String relationModelPrefix = "rm";

    private ModelUtil() {

    }

    public static String getInfoModelPrefix() {
        return infoModelPrefix;
    }

    public static String getDataModelPrefix() {
        return dataModelPrefix;
    }

    public static String getRelationModelPrefix() {
        return relationModelPrefix;
    }

    /**
     * Turn information model to whole info model. <br>
     * 
     * @param infoModel model which contain the data field of PoJo.
     * @return WholeInfoModel model which contain the data field of PoJo.
     * @since SDNO 0.5
     */
    public static Infomodel getWholeInfoModel(Infomodel infoModel) {
        ObjectFactory factory = new ObjectFactory();
        Infomodel wholeInfoModel = deepClone(infoModel);

        String[] basicFixedCols = PropertiesUtil.getInstance().getBasicTableFixedColumn();
        for(int j = 0; j < basicFixedCols.length; j++) {
            String[] columnStr = basicFixedCols[j].split(",");
            Property fixedProperty = factory.createProperty();
            fixedProperty.setName(columnStr[0]);
            fixedProperty.setType(Datatype.fromValue(columnStr[1]));
            if(columnStr.length > 2 && null != columnStr[2] && !"".equals(columnStr[2])) {
                fixedProperty.setLength(new BigInteger(columnStr[2]));
            }
            wholeInfoModel.getBasic().getProperty().add(fixedProperty);
        }
        return wholeInfoModel;
    }

    /**
     * Use serialization to deep clone a info model. <br>
     * 
     * @param infoModel info model need to clone.
     * @return the cloned info model.
     * @since SDNO 0.5
     */
    private static Infomodel deepClone(Infomodel infoModel) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Infomodel cloneModel = new Infomodel();
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(infoModel);

            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            cloneModel = (Infomodel)ois.readObject();
        } catch(IOException e) {
            LOGGER.error("clone infomodel failed!", e);
            LOGGER.error("Reason: " + e.getLocalizedMessage());
        } catch(ClassNotFoundException e) {
            LOGGER.error("clone infomodel failed!", e);
            LOGGER.error("Reason: " + e.getLocalizedMessage());
        } finally {
            close(bos);
            close(oos);
            close(bis);
            close(ois);
        }

        return cloneModel;
    }

    /**
     * Close the stream. <br>
     * 
     * @param stream stream need to close.
     * @since SDNO 0.5
     */
    private static void close(Closeable stream) {
        if(stream == null) {
            return;
        }

        try {
            stream.close();
        } catch(IOException ex) {
            LOGGER.info(ex.getMessage(), ex);
        }
    }

}
