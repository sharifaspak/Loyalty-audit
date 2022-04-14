package com.loyalty.marketplace.voucher.utils;

import java.util.List;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferDate;
import com.loyalty.marketplace.offers.outbound.database.entity.VoucherInfo;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferRules {

	@Field("OfferId")
	private String offerId;
	
	@Field("Offer")
	private OfferDetails offer;
	
	@Field("Merchant")
	private Merchant merchant;
	
	@Field("Rules")
	private List<String> rules;
	
	@Field("Status")
	private String status;

	@Field("VoucherInfo")
	private VoucherInfo voucherInfo;

	@Field("OfferDate")
	private OfferDate offerDates;
	
}
