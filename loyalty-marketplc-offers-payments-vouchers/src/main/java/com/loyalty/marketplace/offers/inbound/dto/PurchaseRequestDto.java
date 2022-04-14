package com.loyalty.marketplace.offers.inbound.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.*;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PurchaseRequestDto {

	@NotEmpty(message = "{validation.purchaseRequestDto.selectedPaymentItem.notEmpty.msg}")
	private String selectedPaymentItem;
	@NotEmpty(message = "{validation.purchaseRequestDto.selectedOption.notEmpty.msg}")
	private String selectedOption;
	@Min(value=0, message="{validation.purchaseRequestDto.spentPoints.min.msg}")
	private Integer spentPoints;
	@Min(value=0, message="{validation.purchaseRequestDto.spentAmount.min.msg}")
	private Double spentAmount;
	private String cardNumber;
	private String cardType;
	private String cardSubType;
	private String cardToken;
	private String cardExpiryDate;
	private String msisdn;
    private String authorizationCode;
    private String paymentType; //For DCB
    private String offerId;
	private String subOfferId;
	@Min(value=0, message="{validation.purchaseRequestDto.couponQuantity.min.msg}")
	private Integer couponQuantity;
	@Min(value=0, message="{validation.purchaseRequestDto.voucherDenomination.min.msg}")
    private Integer voucherDenomination;
	@NotEmpty(message = "{validation.purchaseRequestDto.accountNumber.notEmpty.msg}")
    private String accountNumber;
	private String membershipCode;
    private String atgUserName;
    private String level;
    private String preferredNumber;
    private String extTransactionId;
    private String epgTransactionId;
    private String partialTransactionId;
    private String promoCode;
    private String uiLanguage;
    private String offerTitle;
    private String orderId;
    private String subscriptionCatalogId;
    private String subscriptionMethod;
    private int freeSubscriptionDays;
    private String startDate;
    private String additionalParams;
    private String masterEPGTransactionId;

}
