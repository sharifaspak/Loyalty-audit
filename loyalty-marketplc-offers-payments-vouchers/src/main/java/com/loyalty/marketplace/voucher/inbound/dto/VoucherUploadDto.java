package com.loyalty.marketplace.voucher.inbound.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VoucherUploadDto {

	private String voucherCode;
	private Date uploadDate;
	private Double denomination;
	private String merchantCode;
	private Date expiryDate;
	private String offerId;
	private String subOfferId;
	private Date startDate;
	private Date endDate;
	private String status;
	private String error;
	private String merchantName;
	private String partnerCode;
	private String offerType;
}
