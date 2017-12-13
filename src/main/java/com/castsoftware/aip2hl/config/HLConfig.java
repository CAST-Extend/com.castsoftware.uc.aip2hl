package com.castsoftware.aip2hl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "highlight")
public class HLConfig  {
	String url;
	String userName;
	String password;
	int defaultDomain;
	int allApplDomain;
	String jar;
	String resultsFolder;
	String perlFolder;
	String javaBinFolder;
	String proxyHost;
	String proxyPort;
	
	private String getUrl() {
		return url;
	}
	public String getServerUrl() {
		return url;
	}
	public String getRESTUrl() {
		return url+"/WS2/";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDefaultDomain() {
		return defaultDomain;
	}

	public void setDefaultDomain(int defaultDomain) {
		this.defaultDomain = defaultDomain;
	}

	public int getAllApplDomain() {
		return allApplDomain;
	}

	public void setAllApplDomain(int allApplDomain) {
		this.allApplDomain = allApplDomain;
	}

	public String getJar() {
		return jar;
	}

	public void setJar(String jar) {
		this.jar = jar;
	}

	public String getResultsFolder() {
		return resultsFolder;
	}

	public void setResultsFolder(String resultsFolder) {
		this.resultsFolder = resultsFolder;
	}

	public String getPerlFolder() {
		return perlFolder;
	}

	public void setPerlFolder(String perlFolder) {
		this.perlFolder = perlFolder;
	}

	public String getJavaBinFolder() {
		return javaBinFolder;
	}

	public void setJavaBinFolder(String javaBinFolder) {
		this.javaBinFolder = javaBinFolder;
	}

	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	@Override
	public String toString() {
		return "HLConfig [url=" + url + ", userName=" + userName + ", password=" + password + ", defaultDomain="
				+ defaultDomain + ", allApplDomain=" + allApplDomain + ", jar=" + jar + ", resultsFolder="
				+ resultsFolder + ", perlFolder=" + perlFolder + ", javaBinFolder=" + javaBinFolder + ", proxyHost="
				+ proxyHost + ", proxyPort=" + proxyPort + "]";
	}

}
