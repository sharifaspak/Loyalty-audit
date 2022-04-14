package com.loyalty.marketplace.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class BogoBulkUploadResponse extends ResultResponse{
	

	private String uploadedFileInfoId;
	
	public BogoBulkUploadResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	

}
