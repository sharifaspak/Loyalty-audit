package com.loyalty.marketplace.image.outbound.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class GenerateMetaDataResult {
	
	private String domainName;
	
	private String availableInChannel;
	
	private String imageType;
	
	private int generatedDataCount;
	
	private int totalFileCount;
	
	private List<String> failedDataGeneration = new ArrayList<>();
	
}
