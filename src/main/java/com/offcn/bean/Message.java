package com.offcn.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.offcn.util.BasePage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class Message extends BasePage {
    private  String  mdateStr;

    private MultipartFile mfile;

    private Integer mid;

    private String mtitle;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date mdate;

    private Integer mstatu;

    private String sender;

    private String reciver;

    private Integer mtype;

    private String message;

    private String annex;

    private String remark1;

    private String remark2;

    public String getMdateStr() {
        return mdateStr;
    }

    public void setMdateStr(String mdateStr) {
        this.mdateStr = mdateStr;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mfile=" + mfile +
                ", mid=" + mid +
                ", mtitle='" + mtitle + '\'' +
                ", mdate=" + mdate +
                ", mstatu=" + mstatu +
                ", sender='" + sender + '\'' +
                ", reciver='" + reciver + '\'' +
                ", mtype=" + mtype +
                ", message='" + message + '\'' +
                ", annex='" + annex + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                '}';
    }

    public MultipartFile getMfile() {
        return mfile;
    }

    public void setMfile(MultipartFile mfile) {
        this.mfile = mfile;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle == null ? null : mtitle.trim();
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    public Integer getMstatu() {
        return mstatu;
    }

    public void setMstatu(Integer mstatu) {
        this.mstatu = mstatu;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver == null ? null : reciver.trim();
    }

    public Integer getMtype() {
        return mtype;
    }

    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex == null ? null : annex.trim();
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
}