package com.loyalty.marketplace.voucher.outbound.dto;

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
public class FreeVoucherHandbackFileDto {

	private String programCode;
	private String id;
	private String fileName;
	private String merchantCode;
	private String offerId;
	private Date uploadedDate;
	private String fileType;
	private boolean fileContentPresent;
	private List<FileContentDto> fileUploadedContent;
	private String fileProcessingStatus;
	private boolean handbackFileContentPresent;
	private List<HandbackFileDto> handbackFileContent;
	private Date updatedDate;
	private String updatedUser;
}
