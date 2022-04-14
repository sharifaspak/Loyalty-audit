package com.loyalty.marketplace.offers.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OfferDateDomain {
	
	private Date offerStartDate;
	private Date offerEndDate;
	
}
