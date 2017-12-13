package com.castsoftware.aip2hl.model;

import java.sql.Timestamp;


public class CodeSourceFolderDTO {
	int objectId;
	String serverPath;
	String deployPath;
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public String getDeployPath() {
		return deployPath;
	}
	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	@Override
	public String toString() {
		return "CodeSourceFolderDTO [objectId=" + objectId + ", serverPath=" + serverPath
				+ ", deployPath=" + deployPath + "]";
	}

	
}
