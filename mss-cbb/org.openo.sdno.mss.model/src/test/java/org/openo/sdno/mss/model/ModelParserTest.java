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

package org.openo.sdno.mss.model;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.mss.model.util.ModelParserUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * ModelParser test class. <br/>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public class ModelParserTest {

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
    }

    @Test
    public void testGetModels() {
        boolean bMatch = true;

        List<File> fileList2 = modelParser.getModels();
        assertTrue(fileList.size() == fileList2.size());

        for(File file1 : fileList) {
            if(!fileList2.contains(file1)) {
                bMatch = false;
            }
        }

        assertTrue(bMatch);
    }

    @Test
    public void testSetModels() {
        modelParser.setModels(null);
        assertTrue(modelParser.getModels() == null);
    }

    @Test
    public void testParseModel() {
        modelParser.parseModel();

        assertTrue(modelParser.getInfoModel().size() != 0);
        assertTrue(modelParser.getDataModel().size() != 0);
        assertTrue(modelParser.getRelationModel().size() != 0);
    }

    @Test
    public void testParseModelNull() {
        modelParser.setModels(null);
        modelParser.parseModel();
    }

    @Test
    public void testParseModelJaxbException() {
        new MockUp<ModelParserUtil>() {

            @Mock
            private void parseInfoModel(File file) throws JAXBException {
                throw new JAXBException("");
            }
        };

        modelParser.parseModel();
    }

    @SuppressWarnings("restriction")
    @Test
    public void testParseModelParserConfigurationException() {
        new MockUp<com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl>() {

            @Mock
            public SAXParser newSAXParser() throws ParserConfigurationException {
                throw new ParserConfigurationException();
            }
        };

        modelParser.parseModel();
    }

}
