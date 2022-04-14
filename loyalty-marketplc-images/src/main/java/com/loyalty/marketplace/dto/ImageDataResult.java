package com.loyalty.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ImageDataResult {

	private String imageName;
	
	private String domainId;
	
	private String path;
	
	private byte[] byteArray;

	private int width;
	
	private int height;
	
	private int imageSize;
	
}
