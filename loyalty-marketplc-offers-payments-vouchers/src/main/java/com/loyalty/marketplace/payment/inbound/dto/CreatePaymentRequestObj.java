package com.loyalty.marketplace.payment.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreatePaymentRequestObj {

	@NotBlank
	@NotNull(message = "selectedPaymentItem is mandatory")
	private String selectedPaymentItem;
	
	@NotBlank
	@NotNull(message = "selectedOption is mandatory")
	private String selectedOption;
	
	@NotBlank
	@NotNull(message = "pointsValue is mandatory")
	private Integer pointsValue;
	
	@NotBlank
	@NotNull(message = "dirhamValue is mandatory")
	private Double dirhamValue;
	@NotBlank
	@NotNull(message = "cardNumber is mandatory")
	private String cardNumber;
	@NotBlank
	@NotNull(message = "cardType is mandatory")
	private String cardType;
	@NotBlank
	@NotNull(message = "cardSubType is mandatory")
	private String cardSubType;
	@NotBlank
	@NotNull(message = "cardToken is mandatory")
	private String cardToken;
	@NotBlank
	@NotNull(message = "cardExpiryDate is mandatory")
	private String cardExpiryDate;
	
    private String msisdn;
    @NotBlank
	@NotNull(message = "authorizationCode is mandatory")
    private String authorizationCode;
    @NotBlank
	@NotNull(message = "paymentType is mandatory")
    private String paymentType;
    @NotBlank
	@NotNull(message = "offerId is mandatory")
    private String offerId;
    @NotBlank
	@NotNull(message = "voucherDenomination is mandatory")
    private Integer voucherDenomination;
    //private String token;
    @NotBlank
	@NotNull(message = "accountNumber is mandatory")
    private String accountNumber;
    @NotBlank
   	@NotNull(message = "atgUsername is mandatory")
    private String atgUsername;
    private String level;
    private String preferredNumber;
    private String extTransactionId;
    private String partialTransactionId;
    private String promoCode;
    private String uiLanguage;
    private String offerTittle;
    private String epgTransactionID;
    @NotBlank
   	@NotNull(message = "membershipCode is mandatory")
    private String membershipCode;
    @NotBlank
   	@NotNull(message = "orderId is mandatory")
    private String orderId;
    private String additionalParams;
    
    
	public String getSelectedPaymentItem() {
		return selectedPaymentItem;
	}
	public void setSelectedPaymentItem(String selectedPaymentItem) {
		this.selectedPaymentItem = selectedPaymentItem;
	}
	public String getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}
	public Integer getPointsValue() {
		return pointsValue;
	}
	public void setPointsValue(Integer pointsValue) {
		this.pointsValue = pointsValue;
	}
	public Double getDirhamValue() {
		return dirhamValue;
	}
	public void setDirhamValue(Double dirhamValue) {
		this.dirhamValue = dirhamValue;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardSubType() {
		return cardSubType;
	}
	public void setCardSubType(String cardSubType) {
		this.cardSubType = cardSubType;
	}
	public String getCardToken() {
		return cardToken;
	}
	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}
	public String getCardExpiryDate() {
		return cardExpiryDate;
	}
	public void setCardExpiryDate(String cardExpiryDate) {
		this.cardExpiryDate = cardExpiryDate;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public Integer getVoucherDenomination() {
		return voucherDenomination;
	}
	public void setVoucherDenomination(Integer voucherDenomination) {
		this.voucherDenomination = voucherDenomination;
	}

	/*
	 * public String getToken() { return token; } public void setToken(String token)
	 * { this.token = token; }
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAtgUsername() {
		return atgUsername;
	}
	public void setAtgUsername(String atgUsername) {
		this.atgUsername = atgUsername;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPreferredNumber() {
		return preferredNumber;
	}
	public void setPreferredNumber(String preferredNumber) {
		this.preferredNumber = preferredNumber;
	}
	public String getExtTransactionId() {
		return extTransactionId;
	}
	public void setExtTransactionId(String extTransactionId) {
		this.extTransactionId = extTransactionId;
	}
	public String getPartialTransactionId() {
		return partialTransactionId;
	}
	public void setPartialTransactionId(String partialTransactionId) {
		this.partialTransactionId = partialTransactionId;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public String getUiLanguage() {
		return uiLanguage;
	}
	public void setUiLanguage(String uiLanguage) {
		this.uiLanguage = uiLanguage;
	}
	public String getOfferTittle() {
		return offerTittle;
	}
	public void setOfferTittle(String offerTittle) {
		this.offerTittle = offerTittle;
	}
	public String getEpgTransactionID() {
		return epgTransactionID;
	}
	public void setEpgTransactionID(String epgTransactionID) {
		this.epgTransactionID = epgTransactionID;
	}
	public String getMembershipCode() {
		return membershipCode;
	}
	public void setMembershipCode(String membershipCode) {
		this.membershipCode = membershipCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAdditionalParams() {
		return additionalParams;
	}
	public void setAdditionalParams(String additionalParams) {
		this.additionalParams = additionalParams;
	}
  
}
