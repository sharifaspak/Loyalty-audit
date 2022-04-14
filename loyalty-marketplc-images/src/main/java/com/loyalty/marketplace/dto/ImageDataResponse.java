package com.loyalty.marketplace.dto;

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
public class ImageDataResponse extends ResultResponse {

	public ImageDataResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	int totalFileCount;
	
	int dataGeneratedCount;
	
	List<String> imagesList = new ArrayList<>();
	
	List<ImageDataResult> imageData = new ArrayList<>();

}
