package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
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
public class MemberComment {

	@Field("Rating")
	private Integer rating;
	
	@Field("Comment")
	private String comment;
	
	@Field("ReviewDate")
	private Date reviewDate;
	
}

