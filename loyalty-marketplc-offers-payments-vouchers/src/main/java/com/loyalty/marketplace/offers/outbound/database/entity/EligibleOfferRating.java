package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

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
public class EligibleOfferRating {

	@Field("Id")
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("OfferId")
	private String offerId;
	
	@Field("MemberRating")
	private List<MemberRating> memberRatings;
	
	@Field("AverageRating")
	private Double averageRating;
	
	@Field("RatingCount")
	private Integer ratingCount;
	
	@Field("CommentCount")
	private Integer commentCount;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
