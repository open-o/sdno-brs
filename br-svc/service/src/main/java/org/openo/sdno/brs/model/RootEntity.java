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

package org.openo.sdno.brs.model;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.openo.sdno.brs.constant.Constant;
import org.openo.sdno.brs.validator.rules.StrRule;

/**
 * Father class of all the resource entity.<br>
 * 
 * @author
 * @version SDNO 0.5 2016-5-19
 */
public class RootEntity {

    protected String id;

    @StrRule(range = "0-255", paramName = "description")
    protected String description;

    protected Long createtime;

    protected Long updatetime;

    @JsonIgnore
    private String auditOperLogSn;

    private String auditUserName;

    private String auditUserID;

    private String auditTerminal;

    private String auditDomianName;

    private String auditDomianID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    public String getAuditOperLogSn() {
        return auditOperLogSn;
    }

    public void setAuditOperLogSn(String auditOperLogSn) {
        this.auditOperLogSn = auditOperLogSn;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getAuditUserID() {
        return auditUserID;
    }

    public void setAuditUserID(String auditUserID) {
        this.auditUserID = auditUserID;
    }

    public String getAuditTerminal() {
        return auditTerminal;
    }

    public void setAuditTerminal(String auditTerminal) {
        this.auditTerminal = auditTerminal;
    }

    public String getAuditDomianName() {
        return auditDomianName;
    }

    public void setAuditDomianName(String auditDomianName) {
        this.auditDomianName = auditDomianName;
    }

    public String getAuditDomianID() {
        return auditDomianID;
    }

    public void setAuditDomianID(String auditDomianID) {
        this.auditDomianID = auditDomianID;
    }

    public void setUser(HttpServletRequest httpRequest) {
        this.auditUserID = httpRequest.getHeader(Constant.USER_ID);
        this.auditUserName = httpRequest.getHeader(Constant.USER_NAME);

        this.auditDomianName = httpRequest.getHeader(Constant.DOMAIN_NAME);
        this.auditDomianID = httpRequest.getHeader(Constant.DOMAIN_ID);
    }

    @Override
    public String toString() {
        return "RootEntity [id=" + id + ", description=" + description + ", createtime=" + createtime + ", updatetime="
                + updatetime + "]";
    }
}
