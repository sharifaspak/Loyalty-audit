package com.loyalty.marketplace.gifting.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberActivityPaymentDto {

	private String membershipCode;
	private String accountNumber;
	private String partnerCode;
	private String activityCode;
	private String eventDate;
	private double spendValue;
	private String externalReferenceNumber;
	private String redemptionType;
	private Integer pointsValue;
	private Integer reservationTime;
	private String unitTime;
	private String reserveTransactionId;
	private String offerId;
	private String subscriptionId;
	private String pointsGiftingId;
	private GetMemberResponseDto memberResponse;

}
