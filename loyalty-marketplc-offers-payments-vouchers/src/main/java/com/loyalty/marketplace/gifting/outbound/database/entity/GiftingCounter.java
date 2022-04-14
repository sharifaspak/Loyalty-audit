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
@Document(collection = GiftingConfigurationConstants.GIFTING_COUNTER)
public class GiftingCounter {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("GiftType")
	private String giftType;
	
	@Field("Level")
	private String level;
	
	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("SentDayCount")
	private Double sentDayCount;
	
	@Field("SentWeekCount")
	private Double sentWeekCount;
	
	@Field("SentMonthCount")
	private Double sentMonthCount;
	
	@Field("ReceivedDayCount")
	private Double receivedDayCount;
	
	@Field("ReceivedWeekCount")
	private Double receivedWeekCount;
	
	@Field("ReceivedMonthCount")
	private Double receivedMonthCount;
	
	@Field("LastResetDate")
	private Date lastResetDate;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
