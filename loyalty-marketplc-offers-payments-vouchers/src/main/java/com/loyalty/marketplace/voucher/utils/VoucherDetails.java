package com.loyalty.marketplace.voucher.utils;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDetails {

	@Field("VoucherCode")
	private String voucherCode;
	
	@Field("OfferId")
	private String offer;
	
	@Field("MerchantCode")
	private String merchantCode;
	
	@Field("MerchantName")
	private String merchantName;
	
	@Field("Status")
	private String status;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("ExpiryDate")
	private Date expiryDate;
	
}
