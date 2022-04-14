package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
public class PurchasePaymentMethodResponse extends ResultResponse{
	
	private List<PurchasePaymentMethodsDto> purchasePaymentMethods;
	
	public PurchasePaymentMethodResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	public List<PurchasePaymentMethodsDto> getPurchasePaymentMethods() {
		return purchasePaymentMethods;
	}

	public void setPurchasePaymentMethods(List<PurchasePaymentMethodsDto> purchasePaymentMethods) {
		this.purchasePaymentMethods = purchasePaymentMethods;
	}

	@Override
	public String toString() {
		return "PurchasePaymentMethodResponse [purchasePaymentMethods=" + purchasePaymentMethods + "]";
	}

}