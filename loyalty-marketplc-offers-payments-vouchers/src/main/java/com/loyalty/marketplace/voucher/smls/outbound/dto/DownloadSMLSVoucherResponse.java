package com.loyalty.marketplace.voucher.smls.outbound.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
public class DownloadSMLSVoucherResponse {

	Integer statusCode ;
	String promoCode;
	String externalTransactionId;
	String message;
}
