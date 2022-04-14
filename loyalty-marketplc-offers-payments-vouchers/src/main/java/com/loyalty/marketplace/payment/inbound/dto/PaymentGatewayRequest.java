package com.loyalty.marketplace.payment.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PaymentGatewayRequest {

	@NotBlank
	@NotNull(message = "selectedPaymentItem is mandatory")
	private String selectedPaymentItem;
	@NotBlank
	@NotNull(message = "dirhamValue is mandatory")
	private Double dirhamValue;
	@NotBlank
	@NotNull(message = "pointsValue is mandatory")
	private Long pointsValue;
	@NotBlank
	@NotNull(message = "selectedOption is mandatory")
	private String selectedOption;
	@NotBlank
	@NotNull(message = "offerId is mandatory")
	private String offerId;
	
	private Long voucherDenomination;
	@NotBlank
	@NotNull(message = "accountNumber is mandatory")
	private String accountNumber;
	@NotBlank
	@NotNull(message = "promoCode is mandatory")
	private String promoCode;
	
	private String language;
	
	private String atgUsername;
	private String offerTitle;
	@NotBlank
	@NotNull(message = "count is mandatory")
	private Integer count;
	
	
	public String getSelectedPaymentItem() {
		return selectedPaymentItem;
	}
	public void setSelectedPaymentItem(String selectedPaymentItem) {
		this.selectedPaymentItem = selectedPaymentItem;
	}
	public Double getDirhamValue() {
		return dirhamValue;
	}
	public void setDirhamValue(Double dirhamValue) {
		this.dirhamValue = dirhamValue;
	}
	public Long getPointsValue() {
		return pointsValue;
	}
	public void setPointsValue(Long pointsValue) {
		this.pointsValue = pointsValue;
	}
	public String getSelectedOption() {
		return selectedOption;
	}
	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	/*
	 * public String getChannelId() { return channelId; } public void
	 * setChannelId(String channelId) { this.channelId = channelId; }
	 */
	public Long getVoucherDenomination() {
		return voucherDenomination;
	}
	public void setVoucherDenomination(Long voucherDenomination) {
		this.voucherDenomination = voucherDenomination;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	/*
	 * public String getToken() { return token; } public void setToken(String token)
	 * { this.token = token; }
	 */
	public String getAtgUsername() {
		return atgUsername;
	}
	public void setAtgUsername(String atgUsername) {
		this.atgUsername = atgUsername;
	}
	public String getOfferTitle() {
		return offerTitle;
	}
	public void setOfferTitle(String offerTitle) {
		this.offerTitle = offerTitle;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
