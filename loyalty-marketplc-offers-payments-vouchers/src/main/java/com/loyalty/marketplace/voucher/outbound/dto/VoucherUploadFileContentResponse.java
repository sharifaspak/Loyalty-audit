package com.loyalty.marketplace.voucher.outbound.dto;

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
public class VoucherUploadFileContentResponse extends ResultResponse {

	public VoucherUploadFileContentResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	private VoucherUploadFileContentResult voucherUploadResult = new VoucherUploadFileContentResult(null,null, null, null,
			null, null, null, null, null, null, null, null);

}
