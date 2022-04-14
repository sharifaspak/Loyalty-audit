package com.loyalty.marketplace.voucher.smls.inbound.dto;

import com.loyalty.marketplace.voucher.smls.outbound.dto.DownloadSMLSVoucherResponse;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class DownloadSMLSVoucherRequest {
	private String accountNumber;
	private String apiKey;
	private String apiSecret;
	private String promoId;
	private String validFrom;
	private String validTill;
	private String transactionId;
}
