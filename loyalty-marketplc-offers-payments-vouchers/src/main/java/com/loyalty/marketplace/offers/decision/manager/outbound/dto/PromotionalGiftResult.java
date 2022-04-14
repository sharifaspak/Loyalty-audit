package com.loyalty.marketplace.offers.decision.manager.outbound.dto;
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
public class PromotionalGiftResult {

	private boolean status;
	private int countOfVouchers;
	private String subscriptionId;
	private String offerId;
	private String reason;  

}