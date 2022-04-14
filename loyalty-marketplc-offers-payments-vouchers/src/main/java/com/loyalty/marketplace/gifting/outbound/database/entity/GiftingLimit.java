package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = GiftingConfigurationConstants.GIFTING_LIMIT)
public class GiftingLimit {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("GiftType")
	private String giftType;
	
	@Field("Fee")
	private Double fee;
	
	@Field("AccountLimits")
	private Limits accountLimits;
	
	@Field("MembershipLimits")
	private Limits membershipLimits;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
