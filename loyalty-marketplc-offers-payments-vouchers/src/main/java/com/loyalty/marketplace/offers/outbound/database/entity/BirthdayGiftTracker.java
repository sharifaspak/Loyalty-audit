package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
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
@Document(collection = OffersDBConstants.BIRTHDAY_HELPER)
public class BirthdayGiftTracker {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("AccountNumber")
	private String accountNumber;
	@Field("BirthDate")
	private Date birthDate;
	@Field("LastViewedDate")
	private Date lastViewedDate;
	@Field("CreatedDate")
	private Date createdAt;
	@Field("CreatedUser")
	@CreatedBy
	private String createdBy;
	@Field("UpdatedDate")
	@LastModifiedDate
	private Date updatedAt;
	@Field("UpdatedUser")
	@LastModifiedBy
	private String updatedBy;

}