package com.loyalty.marketplace.offers.outbound.database.entity;

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
public class WhatYouGet {
	
	@Field("English")
	private String whatYouGetEn;
	@Field("Arabic")
	private String whatYouGetAr;
	
}
