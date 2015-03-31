package com.example.rkucentrallibrary;

import android.app.Application;

public class Global extends Application {
	
	private String[] emails;
	private String member;
	private String accno;
	private String title;
	private String duedate;
	private String author;
	private String usr="KMP1";
	private String pwd="PATEL1";
	private String usr_issue="RKU";
	private String pwd_issue="LIB";
	
	
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

	public void setEmails(String[] emailArray){
		
		this.emails=emailArray;
	}
	
	public String[] getEmails(){
		
		return this.emails;
	}

}
