package com.castsoftware.aip2hl.model;

import com.castsoftware.aip2hl.model.aad.AADApplication;
import com.castsoftware.aip2hl.model.hl.HLApplication;

public class AplicationDTO  {
	private AADApplication aipApp;
	private HLApplication hlApp;
	private String csf;
	private boolean selected;
	
	public String getHighlightFound()
	{
		return hlApp!=null?"Yes":"No";
	}
	
	public AADApplication getAipApp() {
		return aipApp;
	}
	public void setAipApp(AADApplication aipApp) {
		this.aipApp = aipApp;
	}
	public HLApplication getHlApp() {
		return hlApp;
	}
	public void setHlApp(HLApplication hlApp) {
		this.hlApp = hlApp;
	}
	public String getSourceFolder() {
		return csf!=null?csf:"No";
	}
	public void setSourceFolder(String csf) {
		this.csf = csf;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	
}
