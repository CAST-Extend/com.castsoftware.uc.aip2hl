package com.castsoftware.aip2hl.model.aad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.castsoftware.aip2hl.Application;
import com.castsoftware.aip2hl.config.AEDConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AADApplication {
	private String href;
	private String name;
	private List <String> technologies;
	private String systems;
	private AADPlaceHolder modules;
	private AADPlaceHolder snapshots;
	private AADPlaceHolder results;
	private String origin;
	private String adgDatabase;
	private String adgWebSite;
	private String adgLocalId;
	private String adgVersion;
	
	@Autowired
	AEDConfig aadConfig;

	
	public AADApplication()
	{
		
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List <String> getTechnologies() {
		return technologies;
	}
	public String getTechnologiesAsString() {
		return String.join(",", technologies);
	}
	public void setTechnologies(List <String>technologies) {
		this.technologies = technologies;
	}
	public String getSystems() {
		return systems;
	}
	public void setSystems(String systems) {
		this.systems = systems;
	}
	public AADPlaceHolder getSnapshots() {
		return snapshots;
	}
	public void setSnapshots(AADPlaceHolder snapshots) {
		this.snapshots = snapshots;
	}
	public AADPlaceHolder getResults() {
		return results;
	}
	public void setResults(AADPlaceHolder results) {
		this.results = results;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getAdgDatabase() {
		return adgDatabase;
	}
	public void setAdgDatabase(String adgDatabase) {
		this.adgDatabase = adgDatabase;
	}
	public String getAdgWebSite() {
		return adgWebSite;
	}
	public void setAdgWebSite(String adgWebSite) {
		this.adgWebSite = adgWebSite;
	}
	public String getAdgLocalId() {
		return adgLocalId;
	}
	
	public AADPlaceHolder getModules() {
		return modules;
	}

	public void setModules(AADPlaceHolder modules) {
		this.modules = modules;
	}

	public void setAdgLocalId(String adgLocalId) {
		this.adgLocalId = adgLocalId;
	}
	public String getAdgVersion() {
		return adgVersion;
	}
	public void setAdgVersion(String adgVersion) {
		this.adgVersion = adgVersion;
	}

	public String getLocalSchema() {
		return adgDatabase.replace("central", "local");
	}

	public String getMngtSchema() {
		return adgDatabase.replace("central", "mngt");
	}


	@Override
	public String toString() {
		return "AADApplication [href=" + href + ", name=" + name + ", technologies=" + technologies + ", systems="
				+ systems + ", modules=" + modules + ", snapshots=" + snapshots + ", results=" + results + ", origin="
				+ origin + ", adgDatabase=" + adgDatabase + ", adgWebSite=" + adgWebSite + ", adgLocalId=" + adgLocalId
				+ ", adgVersion=" + adgVersion + ", aadConfig=" + aadConfig + "]";
	}

}
