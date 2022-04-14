package com.loyalty.marketplace.inbound.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BogoBulkUploadRequest {

	private MultipartFile file;
	private String subscriptionCatalogId;
	private String partnerCode;
	private String invoiceReferenceNumber;
}
