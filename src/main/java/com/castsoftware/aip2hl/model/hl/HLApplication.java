package com.castsoftware.aip2hl.model.hl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HLApplication  {
	int id;
	String name;
	String clientRef;
	HLContributors[] contributors;
	HLDomain[] domains;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HLDomain[] getDomains() {
		return domains;
	}
	public void setDomains(HLDomain[] domains) {
		this.domains = domains;
	}
	@Override
	public String toString() {
		return "HighlightApplication [id=" + id + ", name=" + name + ", domains=" + domains + "]";
	}
	
}
