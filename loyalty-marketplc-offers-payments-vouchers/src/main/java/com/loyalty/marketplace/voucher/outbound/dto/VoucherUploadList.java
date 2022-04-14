package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class VoucherUploadList {
	
	private String id;
	private String fileName;
	private Date uploadedDate;
	private String handbackFile;

	public VoucherUploadList(String id, String fileName, Date uploadedDate, String handbackFile) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.uploadedDate = uploadedDate;
		this.handbackFile = handbackFile;
	}

}
