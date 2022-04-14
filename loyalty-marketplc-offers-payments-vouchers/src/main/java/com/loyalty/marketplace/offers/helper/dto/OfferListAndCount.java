package com.loyalty.marketplace.offers.helper.dto;

import java.util.List;

import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;

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
@NoArgsConstructor
@ToString
public class OfferListAndCount {
	
	private List<OfferCatalog> offerList;
	private Integer count;
	
}
