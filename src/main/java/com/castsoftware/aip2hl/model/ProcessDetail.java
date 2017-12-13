package com.castsoftware.aip2hl.model;

public class ProcessDetail {
	String applName;
	ProcessBuilder pb;
	String status;
	String step;
	String adgVersion;

	public ProcessDetail() {}
	
	public ProcessDetail(String applName, String adgVersion, ProcessBuilder pb) {
		super();
		this.applName = applName;
		this.pb = pb;
		this.adgVersion = adgVersion;
		this.status = "Queue";
	}
	
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	public ProcessBuilder getPb() {
		return pb;
	}
	public void setPb(ProcessBuilder pb) {
		this.pb = pb;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}

	public String getAdgVersion() {
		return adgVersion;
	}

	public void setAdgVersion(String adgVersion) {
		this.adgVersion = adgVersion;
	}
}
