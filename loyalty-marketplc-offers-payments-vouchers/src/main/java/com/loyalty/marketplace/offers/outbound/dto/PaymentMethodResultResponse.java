package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.inbound.dto.PaymentMethodDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
public class PaymentMethodResultResponse extends ResultResponse{
	
	private List<PaymentMethodDto> paymentMethods;
	
	public PaymentMethodResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

	public List<PaymentMethodDto> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethodDto> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	
	@Override
	public String toString() {
		return "PaymentMethodResultResponse [paymentMethods=" + paymentMethods + "]";
	}

}
