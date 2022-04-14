package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

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
@Document(collection = OffersDBConstants.WISHLIST)
public class WishlistEntity {
	
	@Id
	private String id;
	@Field("ProgramCode")
	private String program;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("OfferDetails")
	private List<String> offers;
	@Field("CreatedDate")
	private Date dtCreated;
	@Field("CreatedUser")
	private String usrCreated;
	@Field("UpdatedDate")
	private Date dtUpdated;
	@Field("UpdatedUser")
	private String usrUpdated;
	
}
