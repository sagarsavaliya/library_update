package com.example.rkucentrallibrary;

import android.app.Application;

public class Global extends Application {

    private String[] emails;
    private String member;
    private String accno;
    private String SA = "http://172.172.98.98/webopac/webservicedemo.asmx";
    private String SAS = "http://172.172.98.98/webopac/securewebservice.asmx";
    private String title;
    private String duedate;
    private String author;
    private String usr = "KMP1";
    private String pwd = "PATEL1";
    private String usr_issue = "RKU";
    private String pwd_issue = "LIB";


    public String getusr() {
        return usr;
    }

    public String getpwd() {
        return pwd;
    }

    public String getusr_issue() {
        return usr_issue;
    }

    public String getpwd_issue() {
        return pwd_issue;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String s) {
        this.author = s;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String[] getEmails() {

        return this.emails;
    }

    public void setEmails(String[] emailArray) {

        this.emails = emailArray;
    }

    public String getSA() {
        return SA;
    }

    public void setSA(String SA_temp) {
        this.SA = SA_temp;
    }

    public String getSAS() {
        return SAS;
    }

    public void setSAS(String SAS) {
        this.SAS = SAS;
    }
}
