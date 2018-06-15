package com.sinosteel.domain;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;


/**
 * @author SongJunju
 */

/**
 * 测试报告
 */
@Entity
@Table(name = "TBL_SYS_TESTREPORTS")
public class TestReport extends BaseEntity {

    /**
     * 详细字段未定
     * 暂时先用来存储
     */
    @Column(name = "BODY")
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * processInstanceID
     */
    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceID;

    public String getProcessInstanceID() {
        return processInstanceID;
    }

    public void setProcessInstanceID(String processInstanceID) {
        this.processInstanceID = processInstanceID;
    }

    /**
     * 连接Project的外键
     */
    @OneToOne(mappedBy = "testReport")
    @JoinColumn(name = "TESTREPORT_ID",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    @JSONField(serialize = false)
    private Project project;

    public Project getProject(){
        return project;
    }
    public void setProject(Project project){
        this.project = project;
    }

}
