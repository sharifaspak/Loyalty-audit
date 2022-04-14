package com.loyalty.marketplace.payment.inbound.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberActivityDto {

	private String loyaltyId;

	// @NotNull(message = "Please provide a membership code.")
	private String membershipCode;
	// @NotNull(message = "Please provide a account number.")
	// @Positive(message = "Account Number must be greater than zero.")
	private String accountNumber;
	// @NotEmpty(message = "Please provide a activity code.")
	private String activityCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	// @NotNull(message = "Please provide a event date.")
	private Date eventDate;

	// @NotEmpty(message = "Please provide a partner code.")
	private String partnerCode;

	private double spendValue;
	private String externalReferenceNumber;
	private String differentiator;
	private String redemptionType;
	private Integer pointsValue;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private int reservationTime;
	private String reserveTransactionId;
	private String toPartnerCode;
	private String unitTime;
	private Long toAccNumber;
	private String destinationEquivPoints;
	private Long offerId;
	private String ticketNumber;
	private String agentName;
	private String uiLanguage;
	@JsonProperty("additionalParams")
	private AdditionalParamsDto additionalParams;
	@JsonProperty("orderDetails")
	private OrderDetailsDto orderDetailsDto;

	// Comments ----- Below Attributes are not used anymore

	// private String quantity;
	// private String msisdn;
	// private String subCategory;
	// private String preferredNumber;
	// private String customertype;
	// private String activityIdentifier;
	// private String action;
	// private String mediaCode;
	// private String level;
	// private double voucherAmount;
	// private String paymentDate;
	// private String location;
	// private String productClass;
	// private String productBasis;
}
