package com.loyalty.marketplace.voucher.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class FreeVoucherUploadHandbackFileResponse extends ResultResponse{
	
	private List<FreeVoucherHandbackFileDto> freeVoucherUploadHandbackFileList;
	
	public FreeVoucherUploadHandbackFileResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}
