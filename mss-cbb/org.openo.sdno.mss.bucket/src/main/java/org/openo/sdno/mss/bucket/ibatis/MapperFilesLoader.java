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

package org.openo.sdno.mss.bucket.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class of mapper file loader.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class MapperFilesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperFilesLoader.class);

    private SqlSessionFactory sqlSessionFactory;

    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Initialize to load mapper files.<br>
     * 
     * @since SDNO 0.5
     */
    public void init() {
        if(null == this.sqlSessionFactory) {
            LOGGER.error("error :sqlSessionFactory or mapperfiles is null");
            return;
        }

        try {
            Resources.setDefaultClassLoader(getClass().getClassLoader());

            URL bucketMapperURL = Resources.getResourceURL("META-INF/mappers/BucketMapper.xml");
            URL relationMapperURL = Resources.getResourceURL("META-INF/mappers/RelationMapper.xml");
            URL resourceMapperURL = Resources.getResourceURL("META-INF/mappers/ResourceMapper.xml");

            loadMapperFormURL(bucketMapperURL);
            loadMapperFormURL(relationMapperURL);
            loadMapperFormURL(resourceMapperURL);

        } catch(IOException e) {
            LOGGER.error("exception while load mappers. e: ", e);
        }

    }

    private synchronized void loadMapperFileIntoMybatis(InputStream inputStream, Configuration configuration,
            String resource, Map<String, XNode> sqlFragments) {
        LOGGER.warn("load mapper file:{}", resource);
        new XMLMapperBuilder(inputStream, configuration, resource, sqlFragments).parse();
    }

    private void loadMapperFormURL(URL url) {
        Resources.setDefaultClassLoader(getClass().getClassLoader());
        if(this.sqlSessionFactory != null) {
            InputStream fileInput = null;
            try {
                Configuration configuration = this.sqlSessionFactory.getConfiguration();
                fileInput = url.openStream();
                loadMapperFileIntoMybatis(fileInput, configuration, url.getFile(), configuration.getSqlFragments());
            } catch(IOException e) {
                LOGGER.error("load mapper file {} error", url, e);
            } finally {
                closeInputStream(fileInput);
            }
        } else {
            LOGGER.error("error :sqlSessionFactory or mapperfiles is null");
        }
    }

    private void closeInputStream(InputStream fileInput) {
        if(fileInput != null) {
            try {
                fileInput.close();
            } catch(IOException e) {
                LOGGER.error("FileInputStream close error", e);
            }
        }
    }
}
