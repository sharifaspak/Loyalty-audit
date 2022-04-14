package com.loyalty.marketplace.merchants.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class GetMerchantNameResult {

	private String merchantCode;

	private String merchantNameEn;

	private String merchantNameAr;

}
