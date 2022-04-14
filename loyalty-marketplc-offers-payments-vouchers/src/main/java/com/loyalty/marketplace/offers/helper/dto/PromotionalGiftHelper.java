package com.loyalty.marketplace.offers.helper.dto;

import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PromotionalGiftHelper {
	 private boolean isSubscription;
	 private boolean isOffer;
	 private boolean subscriptionGifted;
	 private boolean voucherGifted;
	 private PurchaseResultResponse subscriptionResponse;
	 private PurchaseResultResponse voucherResponse;
}
     
