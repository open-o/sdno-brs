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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.model.ModelParser;
import org.openo.sdno.mss.schema.infomodel.Infomodel;

import mockit.Mock;
import mockit.MockUp;

/**
 * ModelUtil test class. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ModelUtilTest {

    private ModelParser modelParser;

    private List<File> fileList = new ArrayList<File>();

    @Before
    public void setUp() throws Exception {
        fileList.clear();

        String appRootPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + "bucket" + File.separator + "elements";

        File baseDir = new File(appRootPath);

        for(File file : baseDir.listFiles()) {
            if(file.isFile()) {
                fileList.add(file);
            }
        }

        modelParser = new ModelParser(fileList);
        modelParser.parseModel();
    }

    @Test
    public void testGetWholeInfoModel() {
        Collection<Infomodel> infoModels = modelParser.getInfoModel().values();
        Infomodel infomodel1 = infoModels.iterator().next();
        Infomodel infomodel2 = ModelUtil.getWholeInfoModel(infomodel1);
        assertTrue(infomodel1.getName().equals(infomodel2.getName())
                && infomodel2.getBasic().getProperty().size() >= infomodel1.getBasic().getProperty().size());
    }

    @Test
    public void testDeepCloneIoException() {
        new MockUp<ObjectOutputStream>() {

            @Mock
            public final void writeObject(Object obj) throws IOException {
                throw new IOException("");
            }
        };

        Collection<Infomodel> infoModels = modelParser.getInfoModel().values();

        try {
            ModelUtil.getWholeInfoModel(infoModels.iterator().next());
            assertTrue(false);
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeepCloneClassNotFoundException() {
        new MockUp<ObjectInputStream>() {

            @Mock
            public final Object readObject() throws IOException, ClassNotFoundException {
                throw new ClassNotFoundException("");
            }
        };

        Collection<Infomodel> infoModels = modelParser.getInfoModel().values();

        try {
            ModelUtil.getWholeInfoModel(infoModels.iterator().next());
            assertTrue(false);
        } catch(Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDeepCloseException() {
        new MockUp<ObjectOutputStream>() {

            @Mock
            public void close() throws IOException {
                throw new IOException("");
            }
        };

        Collection<Infomodel> infoModels = modelParser.getInfoModel().values();
        ModelUtil.getWholeInfoModel(infoModels.iterator().next());
    }

}
