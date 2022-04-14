package com.loyalty.marketplace.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class OfferCatalogHandbackFileResponseDto {
	
	private String fileId;
	private String fileName;
	private Date uploadedDate;
	private String fileProcessingStatus;
	private boolean handbackFileContentPresent;

}
