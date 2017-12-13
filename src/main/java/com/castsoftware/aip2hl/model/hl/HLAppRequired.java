package com.castsoftware.aip2hl.model.hl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HLAppRequired {
	String name;
	HLDomain[] domains;
	
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
	
}
