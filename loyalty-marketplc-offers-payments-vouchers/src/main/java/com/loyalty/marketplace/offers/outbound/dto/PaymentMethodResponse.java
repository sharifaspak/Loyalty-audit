package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

/**
 * 
 * @author jaya.shukla
 *
 */
@JsonInclude(Include.NON_NULL)
public class PaymentMethodResponse extends ResultResponse{

	List<String> paymentMethods;
	
	public PaymentMethodResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	@Override
	public String toString() {
		return "PaymentMethodResponse [paymentMethods=" + paymentMethods + "]";
	}

	public List<String> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<String> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}
