package com.loyalty.marketplace.offers.merchants.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Getter
@ToString
@AllArgsConstructor
public class BarcodeDomain {

	private String id;
	private String name;
	private String description;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;

	
}
