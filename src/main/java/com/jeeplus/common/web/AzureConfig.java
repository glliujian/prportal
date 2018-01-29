package com.jeeplus.common.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {
	@Value("${azure.mediaAcount}")
	public String mediaAccount;
	
	@Value("${azure.mediaSecret}")
	public String mediaSecret;
	
	@Value("${azure.scope}")
	public String scope;
	
	@Value("${azure.mediaServiceUri}")
	public String mediaServiceUrl;
	
	@Value("${azure.storageAccount}")
	public String storageAccount;
	
	@Value("${azure.storageSecret}")
	public String storageSecret;
	
	@Value("${azure.defaultEndpointsProtocol}")
	public String defaultEndpointsProtocol;
	
	@Value("${azure.locatorPrefix}")
	public String locatorPrefix;
	
	@Value("${azure.blobUrl}")
	public String blobUrl;
	
	@Value("${azure.cdnUrl}")
	public String cdnUrl;
    
	@Value("${azure.tokenUrl}")
	public String tokenUrl;
	
	@Value("${azure.assetUrl}")
	public String assetUrl;
	
	@Value("${azure.policyUrl}")
	public String policyUrl;
	
	@Value("${azure.locatorUrl}")
	public String locatorUrl;
	
	@Value("${azure.assetFileUrl}")
	public String assetFileUrl;
	
	@Value("${azure.hostName}")
	public String hostName;
	
	@Value("${azure.accessHome}")
	public String accessHome;
	
	@Value("${azure.durationMinutes}")
	public String durationMinutes;
}