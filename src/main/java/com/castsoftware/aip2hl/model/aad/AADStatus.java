package com.castsoftware.aip2hl.model.aad;

import org.springframework.web.client.RestTemplate;

import com.castsoftware.aip2hl.Application;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AADStatus {
	private String href;
	private String name;
	private String dbType;
	private String version;
	private String schema;
	private String dbmsVersion;
	private String localSchema;
	private String mngtSchema;	
	private AADPlaceHolder applications;
	private AADPlaceHolder configurations;
	private AADPlaceHolder results;
	private AADPlaceHolder commonCategories;

	private String sourceFolder;	

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDbmsVersion() {
		return dbmsVersion;
	}

	public void setDbmsVersion(String dbmsVersion) {
		this.dbmsVersion = dbmsVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AADPlaceHolder getApplications() {
		return applications;
	}

	public void setApplications(AADPlaceHolder applications) {
		this.applications = applications;
	}

	public AADPlaceHolder getConfigurations() {
		return configurations;
	}

	public void setConfigurations(AADPlaceHolder configurations) {
		this.configurations = configurations;
	}

	public AADPlaceHolder getResults() {
		return results;
	}

	public void setResults(AADPlaceHolder results) {
		this.results = results;
	}

	public AADPlaceHolder getCommonCategories() {
		return commonCategories;
	}

	public void setCommonCategories(AADPlaceHolder commonCategories) {
		this.commonCategories = commonCategories;
	}

	public String getLocalSchema() {
		return schema.replace("central", "local");
	}

	public String getMngtSchema() {
		return schema.replace("central", "mngt");
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public void setLocalSchema(String localSchema) {
		this.localSchema = localSchema;
	}

	public void setMngtSchema(String mngtSchema) {
		this.mngtSchema = mngtSchema;
	}

	@Override
	public String toString() {
		return "AADStatus [href=" + href + ", name=" + name + ", dbType=" + dbType + ", version=" + version
				+ ", schema=" + schema + ", dbmsVersion=" + dbmsVersion + ", localSchema=" + localSchema
				+ ", mngtSchema=" + mngtSchema + ", applications=" + applications + ", configurations=" + configurations
				+ ", results=" + results + ", commonCategories=" + commonCategories + ", sourceFolder=" + sourceFolder
				+ "]";
	}


	
}
