package com.loyalty.marketplace.offers.error.handler;

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
@Document(collection = OffersDBConstants.ERROR_RECORDS)
public class ErrorRecords {
	
	@Id
	private String id;
	@Field("OfferId")
	private String offerId;
	@Field("Rules")
	private List<String> rules;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("Denomination")
	private Integer denomination;
	@Field("CouponQuantity")
	private Integer couponQuantity;
	@Field("Status")
	private String status;
	@Field("CreatedDate")
	private Date createdDate;
	
}
