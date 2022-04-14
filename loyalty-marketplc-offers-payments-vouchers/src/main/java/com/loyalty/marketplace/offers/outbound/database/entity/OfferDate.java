package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

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
public class OfferDate {
	
	@Field("StartDate")
	private Date offerStartDate;
	@Field("EndDate")
	private Date offerEndDate;
		
}
