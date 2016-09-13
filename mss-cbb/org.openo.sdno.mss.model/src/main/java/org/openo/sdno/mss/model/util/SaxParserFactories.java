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

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Generate the parser factory class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 2016-3-26
 */
public final class SaxParserFactories {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaxParserFactories.class);

    private SaxParserFactories() {
    }

    /**
     * Initialize the parser factory with online file <br>
     * 
     * @return parser factory instance.
     * @since SDNO 0.5
     */
    public static SAXParserFactory newSecurityInstance() {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        } catch(SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException ex) {
            LOGGER.error("FAILED to set feature http://xml.org/sax/features/external-general-entities to false", ex);
        }

        try {
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch(SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException ex) {
            LOGGER.error("FAILED to set feature http://xml.org/sax/features/external-parameter-entities to false", ex);
        }

        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch(SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException ex) {
            LOGGER.error("FAILED to set feature http://apache.org/xml/features/disallow-doctype-decl to false", ex);
        }

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch(SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException ex) {
            LOGGER.error("FAILED to set feature XMLConstants.FEATURE_SECURE_PROCESSING to true", ex);
        }

        factory.setNamespaceAware(true);
        factory.setXIncludeAware(false);
        factory.setValidating(false);

        return factory;
    }
}
