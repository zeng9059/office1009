package com.offcn.bean;

import com.offcn.util.BasePage;

import java.util.List;

public class Role extends BasePage {

    private List<Function> functionList;


    /**
     * 角色对应的功能id
     */
    private String fids;

    private Integer rid;

    private String rcode;

    private String rname;

    private String remark1;

    private String remark2;



    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode == null ? null : rcode.trim();
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname == null ? null : rname.trim();
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
    public String getFids() {
        return fids;
    }

    public void setFids(String fids) {
        this.fids = fids;
    }

    @Override
    public String toString() {
        return "Role{" +
                "fids='" + fids + '\'' +
                ", rid=" + rid +
                ", rcode='" + rcode + '\'' +
                ", rname='" + rname + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                '}';
    }
}