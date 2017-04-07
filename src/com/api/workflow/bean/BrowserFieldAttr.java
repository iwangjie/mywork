package com.api.workflow.bean;

import java.io.Serializable;

public class BrowserFieldAttr implements Serializable{
	private static final long serialVersionUID = -7294712567290765797L;
	private String browserurl;
	private String linkurl;
	private boolean issingle;
	private String completeurl;
	private String bindevent;
	
	public String getBrowserurl() {
		return browserurl;
	}
	public void setBrowserurl(String browserurl) {
		this.browserurl = browserurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public boolean isIssingle() {
		return issingle;
	}
	public void setIssingle(boolean issingle) {
		this.issingle = issingle;
	}
	public String getCompleteurl() {
		return completeurl;
	}
	public void setCompleteurl(String completeurl) {
		this.completeurl = completeurl;
	}
	public String getBindevent() {
		return bindevent;
	}
	public void setBindevent(String bindevent) {
		this.bindevent = bindevent;
	}
	
}
