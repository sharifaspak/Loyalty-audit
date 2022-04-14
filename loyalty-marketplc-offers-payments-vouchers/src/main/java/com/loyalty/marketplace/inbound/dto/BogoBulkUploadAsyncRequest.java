package com.loyalty.marketplace.inbound.dto;

import java.util.List;

import lombok.Data;

@Data
public class BogoBulkUploadAsyncRequest {
	
	private List<String> existingExternalReferenceNumber;
	private List<String> duplicateExternalReferenceNumber;

}
