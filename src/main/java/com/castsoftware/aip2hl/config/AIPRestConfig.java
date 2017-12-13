package com.castsoftware.aip2hl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AIPRestConfig {
	@Autowired private AEDConfig aedConfig;
	@Autowired private AADConfig aadConfig;
	
	private RestTemplate aedRestTemplate;
	private RestTemplate aadRestTemplate;

	@Bean
	public RestTemplate aedRestTemplate(RestTemplateBuilder builder) {
		aedRestTemplate = builder.build();
		aedRestTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(aedConfig.getUserName(), aedConfig.getPassword()));
		return aedRestTemplate;
	}
	public RestTemplate getAEDRestTemplate()
	{
		return aedRestTemplate;
	}
	public String getAEDBaseURL()
	{
		return aedConfig.getUrl();
	}

	@Bean
	public RestTemplate aadRestTemplate(RestTemplateBuilder builder) {
		aadRestTemplate = builder.build();
		aadRestTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(aadConfig.getUserName(), aadConfig.getPassword()));
		return aadRestTemplate;
	}
	public RestTemplate getAADRestTemplate()
	{
		return aadRestTemplate;
	}
	public String getAADBaseURL()
	{
		return aadConfig.getUrl();
	}

}
