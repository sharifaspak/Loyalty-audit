package com.loyalty.marketplace.voucher.outbound.dto;

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
public class VoucherUploadFileContentResult {
	private String programCode;	
	private String id;
	private String fileName;
	private String merchantCode;
	private String offerId;
	private Date uploadedDate;
	private String fileType;	
	private String fileContent;	
	private String fileProcessingStatus;
	private String handbackFile;
	private Date updatedDate;
	private String updatedUser;
	public VoucherUploadFileContentResult(String programCode, String id, String fileName, String merchantCode, String offerId,
			Date uploadedDate, String fileType, String fileContent, String fileProcessingStatus, String handbackFile,
			Date updatedDate, String updatedUser) {
		super();
		this.programCode = programCode;
		this.id = id;
		this.fileName = fileName;
		this.merchantCode = merchantCode;
		this.offerId = offerId;
		this.uploadedDate = uploadedDate;
		this.fileType = fileType;
		this.fileContent = fileContent;
		this.fileProcessingStatus = fileProcessingStatus;
		this.handbackFile = handbackFile;
		this.updatedDate = updatedDate;
		this.updatedUser = updatedUser;
	}

	
}
