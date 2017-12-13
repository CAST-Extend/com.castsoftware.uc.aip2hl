package com.castsoftware.aip2hl.model.aad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AADPlaceHolder {
	private String href;
	private String name;

	public AADPlaceHolder() {
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

	@Override
	public String toString() {
		return "AADModule [href=" + href + ", name=" + name + "]";
	}


}
