package com.castsoftware.aip2hl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HLRestConfig {
	@Autowired private HLConfig hlConfig;
	
	private RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		restTemplate = builder.build();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(hlConfig.getUserName(), hlConfig.getPassword()));
		return restTemplate;
	}
	public RestTemplate getRestTemplate()
	{
		return restTemplate;
	}
	public String getBaseURL()
	{
		return hlConfig.getRESTUrl();
	}
	
	public HLConfig getHLConfig() {
		return hlConfig;
	}

	
}
