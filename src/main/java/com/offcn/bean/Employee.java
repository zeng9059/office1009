package com.offcn.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.offcn.util.BasePage;

import java.util.Date;
import java.util.List;

public class Employee extends BasePage {

    private List<Role> roleList;

    private  String rids;
    private Integer eid;

    private String ename;

    private Integer esex;

    private Integer eage;

    private String phone;


    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date hireDate;

    private String jobNumber;

    private String password;

    private String remark1;

    private String remark2;

    private String remark3;

    @Override
    public String toString() {
        return "Employee{" +
                "roleList=" + roleList +
                ", rids='" + rids + '\'' +
                ", eid=" + eid +
                ", ename='" + ename + '\'' +
                ", esex=" + esex +
                ", eage=" + eage +
                ", phone='" + phone + '\'' +
                ", hireDate=" + hireDate +
                ", jobNumber='" + jobNumber + '\'' +
                ", password='" + password + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                '}';
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public Integer getEsex() {
        return esex;
    }

    public void setEsex(Integer esex) {
        this.esex = esex;
    }

    public Integer getEage() {
        return eage;
    }

    public void setEage(Integer eage) {
        this.eage = eage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber == null ? null : jobNumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1 == null ? null : remark1.trim();
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2 == null ? null : remark2.trim();
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3 == null ? null : remark3.trim();
    }
}