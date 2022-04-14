package com.loyalty.marketplace.offers.outbound.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PromotionalGiftResponseDto {
	
	private String voucherStatus;
	private String voucherTransactionNo;
	private String voucherActivityCode;
	private Double voucherEarnPointsRate;
	private String voucherPartnerCode;
	private String voucherEpgTransactionId;
	private List<String> voucherCodes;
	private String voucherAdditionalParams;
	private String subscriptionStatus;
	private String subscriptionTransactionNo;
	private Date subscriptionEndDate;
	private String subscriptionId;
	private String subscriptionAdditionalParams;

}
