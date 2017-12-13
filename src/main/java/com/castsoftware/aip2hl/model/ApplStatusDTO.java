package com.castsoftware.aip2hl.model;

import com.castsoftware.aip2hl.model.aad.AADApplication;
import com.castsoftware.aip2hl.model.hl.HLApplication;

public class ApplStatusDTO {

	private String applName;
	private String applSource;
	private String versionFilter;
	private String message;
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	public String getVersionFilter() {
		return versionFilter;
	}
	public void setVersionFilter(String versionFilter) {
		this.versionFilter = versionFilter;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getApplSource() {
		return applSource;
	}
	public void setApplSource(String applSource) {
		this.applSource = applSource;
	}
	@Override
	public String toString() {
		return "ApplStatusDTO [applName=" + applName + ", applSource=" + applSource + ", versionFilter=" + versionFilter
				+ ", message=" + message + "]";
	}
	
	
}
