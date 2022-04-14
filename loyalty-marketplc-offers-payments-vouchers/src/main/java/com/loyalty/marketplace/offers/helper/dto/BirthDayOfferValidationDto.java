package com.loyalty.marketplace.offers.helper.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class BirthDayOfferValidationDto {

	@Field("AccountNumber")
	private String accountNumber;
	@Field("MembershipCode")
	private String membershipCode;
	@Field("lastGiftReceivedDate")
	private String lastGiftReceivedDate;
	@Field("UpdatedDate")
	private Date updatedAt;
}
