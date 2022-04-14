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
public class PurchaseResponseDto {
	
	private String paymentStatus;
	private String transactionNo;
	private String activityCode;
	private Double earnPointsRate;
	private String partnerCode;
	private String epgTransactionId;
	List<String> voucherCodes;
	private Date subscriptionEndDate;
	private String subscriptionId;
	private String additionalParams;
	private String subsCatId;
	private Boolean refundFlag;

}
