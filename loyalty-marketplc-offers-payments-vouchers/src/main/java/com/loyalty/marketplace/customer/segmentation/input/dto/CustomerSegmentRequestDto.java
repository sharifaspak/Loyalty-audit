//package com.loyalty.marketplace.customer.segmentation.input.dto;
//
//import java.util.List;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotBlank;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@ToString
//@JsonInclude(Include.NON_NULL)
//public class CustomerSegmentRequestDto {
//	
//	private String id;
//	
//	@NotBlank(message = "{validation.customerSegmentRequestDto.name.notBlank.msg}")
//	private String name;
//	@Valid
//	private ListValuesDto channelId;
//	@Valid
//	private ListValuesDto tierLevel;
//	@Valid
//	private ListValuesDto gender;
//	@Valid
//	private ListValuesDto nationality;
//	@Valid
//	private ListValuesDto numberType;
//	@Valid
//	private ListValuesDto accountStatus;
//	@Valid
//	private ListValuesDto emailVerificationStatus;
//	@Valid
//	private ListValuesDto days;
//	@Valid
//	private ListValuesDto customerType;
//	@Valid
//	private ListValuesDto cobrandedCards;
//	@Valid
//	private IntegerValuesDto totalTierPoints;
//	@Valid
//	private IntegerValuesDto totalAccountPoints;
//	@Valid
//	private DateValuesDto dob;
//	@Valid
//	private DateValuesDto accountStartDate;
//	private boolean isFirstAccess;
//	private boolean isCoBranded;
//	private boolean isSubscribed;
//	private boolean isPrimaryAccount;
//	private boolean isTop3Account;
//	@Valid
//	private List<PurchaseValuesDto> purchaseItems;	
//	
//}
