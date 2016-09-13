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

package org.openo.sdno.mss.init.buckets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.sdno.framework.container.util.JsonUtil;
import org.openo.sdno.mss.init.util.BucketStaticUtil;
import org.openo.sdno.mss.init.util.FileUtil;
import org.openo.sdno.mss.model.ModelParser;
import org.openo.sdno.mss.schema.datamodel.Datamodel;
import org.openo.sdno.mss.schema.infomodel.Infomodel;
import org.openo.sdno.mss.schema.relationmodel.RelationModelRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to process BucketModel, it scan and parse the model definition file then create the model
 * object for database. A bucket model contain all the information about a Java object and it's
 * relation map in the database.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-25
 */
public class BucketModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketModel.class);

    private final List<File> models = new ArrayList<File>();

    private Map<String, Infomodel> infoModels = new ConcurrentHashMap<String, Infomodel>();

    private Map<String, Datamodel> dataModels = new ConcurrentHashMap<String, Datamodel>();

    private Map<String, RelationModelRelation> relationModels = new ConcurrentHashMap<String, RelationModelRelation>();

    private static final String DM_PREFIX = "dm_";

    private static final String IM_PREFIX = "im_";

    private static final String RM_PREFIX = "rm_";

    private static final String MODEL_POSTFIX = ".xml";

    private ModelParser modelParser = null;

    private String modelBasicPath = "";

    /**
     * Constructor<br>
     * 
     * @since SDNO 0.5
     * @param modelBasicPath
     */
    public BucketModel(String modelBasicPath) {
        super();
        this.modelBasicPath = modelBasicPath;
    }

    /**
     * @return Returns the infoModels.
     */
    public Map<String, Infomodel> getInfoModels() {
        return infoModels;
    }

    /**
     * @return Returns the dataModels.
     */
    public Map<String, Datamodel> getDataModels() {
        return dataModels;
    }

    /**
     * @return Returns the relationModels.
     */
    public Map<String, RelationModelRelation> getRelationModels() {
        return relationModels;
    }

    /**
     * initialize the bucket model. <br>
     * 
     * @since SDNO 0.5
     */
    public void loadBucketModelFiles() {
        LOGGER.debug("Begin to deal the bucket models.");

        // scan model files.
        scanModelFiles();

        LOGGER.warn("file list: " + JsonUtil.toJson(this.models));

        this.modelParser = new ModelParser(this.models);

        this.modelParser.parseModel();

        // cache the model data parsed from original file.
        this.infoModels = modelParser.getInfoModel();
        this.dataModels = modelParser.getDataModel();
        this.relationModels = modelParser.getRelationModel();

        LOGGER.info("Model file parse successed...");
    }

    private void scanModelFiles() {
        scanElementModelFiles();
        scanRelationModelFiles();
    }

    private void scanElementModelFiles() {
        String filePath = this.modelBasicPath + File.separator + BucketStaticUtil.getBucketElementDirName();

        File baseDir = new File(filePath);

        if(!baseDir.exists()) {
            LOGGER.info("baseDir is not exist, nothing to do.");
            return;
        }

        for(File file : baseDir.listFiles()) {
            if(file.isFile() && isModelFile(file.getName())) {
                this.models.add(file);
            }
        }

        recursiveScanElementModelFiles(baseDir);
    }

    private boolean isModelFile(String fileName) {
        if((fileName.startsWith(DM_PREFIX) || fileName.startsWith(IM_PREFIX) || fileName.startsWith(RM_PREFIX))
                && fileName.endsWith(MODEL_POSTFIX)) {
            return true;
        }
        return false;
    }

    private void recursiveScanElementModelFiles(File dir) {
        for(File file : dir.listFiles()) {
            if(file.isDirectory()) {
                scanInfoModelFiles(file.getName(), file);
                recursiveScanElementModelFiles(file.getAbsoluteFile());
            }
        }
    }

    private void scanInfoModelFiles(String resource, File dir) {
        File dmFile = null;
        File imFile = null;

        for(File file : dir.listFiles()) {
            if(file.isFile() && isValidModelFile(dir.getName(), file.getName(), DM_PREFIX)) {
                dmFile = file;
            } else if(file.isFile() && isValidModelFile(dir.getName(), file.getName(), IM_PREFIX)) {
                imFile = file;
            }
        }

        if(null != dmFile && null != imFile) {
            this.models.add(dmFile);
            this.models.add(imFile);
        }

    }

    private boolean isValidModelFile(String resource, String fileName, String pattern) {
        if(fileName.startsWith(pattern) && fileName.endsWith(MODEL_POSTFIX)
                && fileName.replace(pattern, "").replace(MODEL_POSTFIX, "").equals(resource)) {
            return true;
        }

        return false;
    }

    private void scanRelationModelFiles() {
        String filePath = this.modelBasicPath + File.separator + BucketStaticUtil.getBucketRelationDirName();
        models.addAll(FileUtil.getDirFileList(filePath));
    }
}
