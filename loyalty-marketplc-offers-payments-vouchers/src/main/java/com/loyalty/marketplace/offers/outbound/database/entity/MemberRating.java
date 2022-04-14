package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_NULL)
public class MemberRating {

	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("FirstName")
	private String firstName;
	
	@Field("LastName")
	private String lastName;
	
	@Field("Comments")
	private List<MemberComment> comments;
	
}
