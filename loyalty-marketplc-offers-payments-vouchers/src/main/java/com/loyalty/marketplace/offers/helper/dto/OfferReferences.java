package com.loyalty.marketplace.offers.helper.dto;

import java.util.List;

import com.loyalty.marketplace.offers.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferType;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.entity.Denomination;
import com.loyalty.marketplace.outbound.database.entity.PaymentMethod;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OfferReferences {
	
	private OfferType offerType;
	private Category category;
	private Category subCategory;
	private Merchant merchant;
	private List<Store> store;
	private List<Denomination> denominations;
	private Integer size;
    private Headers header;
    private List<PaymentMethod> paymentMethods;
	
}
