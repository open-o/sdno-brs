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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;
import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdno.brs.validator.rules.AbstractRuleValidator;
import org.openo.sdno.brs.validator.rules.IntRule;
import org.openo.sdno.brs.validator.rules.IntRuleValidator;
import org.openo.sdno.brs.validator.rules.IpRule;
import org.openo.sdno.brs.validator.rules.IpRuleValidator;
import org.openo.sdno.brs.validator.rules.StrEnumRule;
import org.openo.sdno.brs.validator.rules.StrEnumRuleValidator;
import org.openo.sdno.brs.validator.rules.StrRule;
import org.openo.sdno.brs.validator.rules.StrRuleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator class for input parameter.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-20
 */
public final class InputParaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputParaValidator.class);

    /**
     * Mapping attribute validator by object class type.
     */
    private final Map<Class, List<AttrValidateInfo>> mapObjAttrInfo = new HashMap<Class, List<AttrValidateInfo>>();

    /**
     * Mapping rule validator by annotation configured on the attribute.
     */
    private final Map<Class, Class<? extends AbstractRuleValidator>> mapValidator =
            new HashMap<Class, Class<? extends AbstractRuleValidator>>();

    /**
     * Constructor.<br>
     * 
     * @since SDNO 0.5
     */
    public InputParaValidator() {
        mapValidator.put(StrRule.class, StrRuleValidator.class);
        mapValidator.put(StrEnumRule.class, StrEnumRuleValidator.class);
        mapValidator.put(IntRule.class, IntRuleValidator.class);
        mapValidator.put(IpRule.class, IpRuleValidator.class);
    }

    /**
     * Get attribute validator by object class type.<br>
     * 
     * @param type Class type
     * @return Collection of attribute validator info.
     * @since SDNO 0.5
     */
    public List<AttrValidateInfo> getAttrValidateInfoList(Class type) {
        return mapObjAttrInfo.get(type);
    }

    /**
     * validate the specific attribute of the given class.<br>
     * 
     * @param field field to check.
     * @param value data need to check.
     * @param type type of the class.
     * @return List<RuleErrorInfo>
     * @since SDNO 0.5
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<RuleErrorInfo> validateField(String field, Object value, Class<?> type) {
        List<AttrValidateInfo> attrValidateInfoList = mapObjAttrInfo.get(type);
        ValidateTask task = new ValidateTask(this, null);
        if(attrValidateInfoList == null) {
            attrValidateInfoList = new ArrayList<AttrValidateInfo>();
            initAttrInfoList(task, type, attrValidateInfoList);
        }

        for(AttrValidateInfo attrValidateInfo : attrValidateInfoList) {
            if(attrValidateInfo.getParamName().equals(field)) {
                List<AbstractRuleValidator> ruleValidators = attrValidateInfo.getLstRuleValidator();
                for(AbstractRuleValidator ruleValidator : ruleValidators) {
                    ruleValidator.validate(task, field, value, false);
                }
            }
        }

        return task.getLstErr();
    }

    /**
     * Validate the input object.<br>
     * 
     * @param validData The object to be validated
     * @return Collection of validating results
     * @since SDNO 0.5
     */
    public List<RuleErrorInfo> validate(Object validData) {
        return validate(validData, false);
    }

    /**
     * Validate the input object.<br>
     * 
     * @param validData The object to be validated
     * @param isEdit Edit flag, some rules need special processing when editing, such as
     *            authentication parameters can be empty when editing.
     * @return Collection of validating results
     * @since SDNO 0.5
     */
    public List<RuleErrorInfo> validate(Object validData, boolean isEdit) {
        if(validData == null) {
            return null;
        }

        List<RuleErrorInfo> lstErr = null;

        // Depth traversal to check all attributes.
        ValidateTask task = new ValidateTask(this, validData);
        while(!task.getStackValidTrace().isEmpty()) {
            ValidateTraceItem curValidDataTraceItem = task.getStackValidTrace().lastElement();

            // If this type of object is not initialized, to initialize validate record.
            List<AttrValidateInfo> lstAttrValidInfo = curValidDataTraceItem.getLstAttrValidInfo();
            if(lstAttrValidInfo == null) {
                initTraceItem(task, curValidDataTraceItem);
                lstAttrValidInfo = curValidDataTraceItem.getLstAttrValidInfo();
            }

            // If there is no content left, description validation of the object has been completed,
            // continue to the next task.
            if(lstAttrValidInfo.isEmpty()) {
                task.getStackValidTrace().pop();
                continue;
            }

            // Choose one attribute to validate.
            int lastAttrIndex = lstAttrValidInfo.size() - 1;
            AttrValidateInfo attrValidInfo = lstAttrValidInfo.get(lastAttrIndex);
            lstAttrValidInfo.remove(lastAttrIndex);
            validateAttribute(task, attrValidInfo, curValidDataTraceItem.getValidData(), isEdit);
        }
        lstErr = task.getLstErr();

        return lstErr;
    }

    /**
     * Validate the input object.<br>
     * 
     * @param task Current task
     * @param attrValidateInfo The attribute to be validated
     * @param validateData The object to be validated
     * @param isEdit Edit flag, some rules need special processing when editing, such as
     *            authentication parameters can be empty when editing.
     * @since SDNO 0.5
     */
    private void validateAttribute(ValidateTask task, AttrValidateInfo attrValidateInfo, Object validateData,
            boolean isEdit) {
        String attributeNameKey = attrValidateInfo.getAttributeName();
        try {
            Object attrValue = attrValidateInfo.getAttrValue(validateData);

            // The executed validator, for processing scene.
            Map<Class<? extends AbstractRuleValidator>, String> mapExecutedValidator =
                    new HashMap<Class<? extends AbstractRuleValidator>, String>();

            // Traversal check every rule
            for(AbstractRuleValidator validator : attrValidateInfo.getLstRuleValidator()) {
                // Recording this validator has been executed.
                mapExecutedValidator.put(validator.getClass(), "");

                validator.validate(task, attributeNameKey, attrValue, isEdit);
            }
        } catch(IllegalArgumentException | ReflectiveOperationException e) {
            LOGGER.error(e.getMessage(), e);
            task.addError(attributeNameKey, ErrorMessageKey.GET_VALUE_FAILED, attrValidateInfo.getParamName());
        }
    }

    /**
     * Initialize a trace item of validator.<br>
     * 
     * @param task Current task
     * @param validDataTraceItem The trace item to be initialized
     * @since SDNO 0.5
     */
    private void initTraceItem(ValidateTask task, ValidateTraceItem validDataTraceItem) {
        List<AttrValidateInfo> lstAttrValidInfo = mapObjAttrInfo.get(validDataTraceItem.getValidData().getClass());

        // If the validator info of attribute is not initialized, to initialize it.
        if(lstAttrValidInfo == null) {
            lstAttrValidInfo = initObjAttrInfo(task, validDataTraceItem.getValidData().getClass());
            mapObjAttrInfo.put(validDataTraceItem.getValidData().getClass(), lstAttrValidInfo);
        }

        // If the trace item of validator is not initialized, to initialize it.
        if(validDataTraceItem.getLstAttrValidInfo() == null) {
            validDataTraceItem.setLstAttrValidInfo(new ArrayList<AttrValidateInfo>());
        }
        validDataTraceItem.getLstAttrValidInfo().clear();
        validDataTraceItem.getLstAttrValidInfo().addAll(lstAttrValidInfo);
    }

    /**
     * Init validator list by object class type.<br>
     * 
     * @param task Current task
     * @param validDataType Object class type
     * @return Validator list
     * @since SDNO 0.5
     */
    private List<AttrValidateInfo> initObjAttrInfo(ValidateTask task, Class<? extends Object> validDataType) {
        List<AttrValidateInfo> lstAttrValidInfo = new ArrayList<AttrValidateInfo>();

        // Look for the super class from current class, initialize the validator info of attribute.
        for(Class curType = validDataType; curType != null
                && curType.getPackage().getName().startsWith("org.openo.sdno"); curType = curType.getSuperclass()) {
            initAttrInfoList(task, curType, lstAttrValidInfo);
        }

        return lstAttrValidInfo;
    }

    private void initAttrInfoList(ValidateTask task, Class<? extends Object> validDataType,
            List<AttrValidateInfo> lstAttrValidInfo) {
        String fieldPre = validDataType.getName() + '.';

        // Traverse all attributes of the object. If there is supported annotation on the attribute,
        // collect the annotation.
        for(Field field : validDataType.getDeclaredFields()) {
            // All annotations.
            List<AbstractRuleValidator> lstRule = null;
            String paramName = null;
            for(Annotation ann : field.getDeclaredAnnotations()) {
                Class<? extends AbstractRuleValidator> validatorClass = mapValidator.get(ann.annotationType());

                // If validator class is null, is not validating rules, ignore it.
                if(validatorClass == null) {
                    continue;
                }

                try {
                    Constructor<? extends AbstractRuleValidator> constructor =
                            validatorClass.getConstructor(ann.annotationType());
                    AbstractRuleValidator validator = constructor.newInstance(ann);
                    if(lstRule == null) {
                        lstRule = new ArrayList<AbstractRuleValidator>();
                    }
                    paramName = validator.getParamName();
                    lstRule.add(validator);
                } catch(ReflectiveOperationException e) {
                    LOGGER.error(MessageFormat.format("Create validator failed.type:{0};attribute:{1};ann:{2}",
                            validDataType, field.getName(), ann), e);
                    task.addError(null, ErrorMessageKey.CREATE_VALIDATOR_FAILED, validDataType.toString(), null, null);
                }
            }

            // If no annotation is configured, ignore it.
            if(lstRule == null) {
                continue;
            }

            String pre = field.getType() == Boolean.class ? "is" : "get";
            String methodName = pre + upFirstChar(field.getName());
            Method getMethod = null;
            try {
                getMethod = validDataType.getMethod(methodName);
                lstAttrValidInfo.add(new AttrValidateInfo(fieldPre + field.getName(), getMethod, lstRule, paramName));
            } catch(NoSuchMethodException e) {
                LOGGER.error(MessageFormat.format("init attribute {0} failed for not find method {1}.", field.getName(),
                        methodName), e);
                task.addError(null, ErrorMessageKey.INIT_ATTR_FAILED, null);
            }
        }
    }

    private String upFirstChar(String name) {
        char ch = name.charAt(0);
        char ch1 = name.charAt(1);

        if(ch < 'a' || ch > 'z') {
            return name;
        } else {
            if(ch1 >= 'A' && ch1 <= 'Z') {
                return name;
            }
            ch -= ('a' - 'A');
        }
        if(name.length() < 2) {
            return String.valueOf(ch);
        }

        return ch + name.substring(1);
    }

    /**
     * Class of input parameter check.<br>
     * 
     * @author
     * @version SDNO 0.5 2016-5-20
     */
    public static class InputParaCheck {

        private InputParaCheck() {
        }

        /**
         * Validate parameters of input object.<br>
         * 
         * @param validData The object to be validated
         * @throws ServiceException if validate parameters failed
         * @since SDNO 0.5
         */
        public static void inputParamsCheck(Object validData) throws ServiceException {
            inputParamsCheck(validData, false);
        }

        /**
         * Validate parameters of input object.<br>
         * 
         * @param validData The object to be validated
         * @param isEdit Edit flag, some rules need special processing when editing, such as
         *            authentication parameters can be empty when editing.
         * @throws ServiceException if validate parameters failed
         * @since SDNO 0.5
         */
        public static void inputParamsCheck(Object validData, boolean isEdit) throws ServiceException {
            InputParaValidator ipv = new InputParaValidator();
            List<RuleErrorInfo> errList = ipv.validate(validData, isEdit);
            checkErrList(errList);
        }

        /**
         * validate the given attribute.<br>
         * 
         * @param attrName name of the attribute.
         * @param attrValue value to check.
         * @param clazz class
         * @throws ServiceException if error parameter is null.
         * @since SDNO 0.5
         */
        public static void inputAttrCheck(String attrName, Object attrValue, Class<?> clazz) throws ServiceException {
            InputParaValidator ipv = new InputParaValidator();
            List<RuleErrorInfo> errList = ipv.validateField(attrName, attrValue, clazz);
            checkErrList(errList);
        }

        private static void checkErrList(List<RuleErrorInfo> errList) throws ServiceException {
            if(errList != null && !errList.isEmpty()) {
                RuleErrorInfo info = errList.get(0);
                List<String> errParam = info.getLstErrParam();
                ExceptionArgs args = new ExceptionArgs();
                args.setDescArgs(new String[] {info.getParamName()});

                if(null != errParam) {
                    args.setDetailArgs(errParam.toArray(new String[errParam.size()]));
                }
                throw new ServiceException(info.getErrorMsgKey(), HttpStatus.BAD_REQUEST_400, args);
            }
        }

    }
}
