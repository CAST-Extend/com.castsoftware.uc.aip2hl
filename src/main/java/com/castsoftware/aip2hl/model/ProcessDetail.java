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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProcessDetail)
		{
			ProcessDetail pd = (ProcessDetail)obj;
			
			if (this.applName.equals(pd.getApplName()) &&
				this.adgVersion.equals(pd.getAdgVersion()))	
				return true;
			else 
				return false;
		} else {
			return false;
		}
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
