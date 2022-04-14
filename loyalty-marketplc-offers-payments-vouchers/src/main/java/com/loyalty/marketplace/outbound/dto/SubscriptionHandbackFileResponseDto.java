package com.loyalty.marketplace.outbound.dto;

import java.util.Date;
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
public class SubscriptionHandbackFileResponseDto {
	

	private String programCode;
	private String fileId;
	private String fileName;
	private Date uploadedDate;
	private String fileProcessingStatus;
	private List<SubscriptionHandbackFileDto> subscriptionHandbackFileContent;
	
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean handbackFileContentPresent;
	private Date updatedDate;
	private String updatedUser;
}
