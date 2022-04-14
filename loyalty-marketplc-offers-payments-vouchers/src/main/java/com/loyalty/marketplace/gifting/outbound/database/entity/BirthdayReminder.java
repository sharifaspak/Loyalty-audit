package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;
import java.util.List;

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
@Document(collection = GiftingConfigurationConstants.BIRTHDAY_REMINDER)
public class BirthdayReminder {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("FirstName")
	private String firstName;
	
	@Field("LastName")
	private String lastName;
	
	@Field("DOB")
	private Date dob;
	
	@Field("ReminderList")
	private List<BirthdayReminderList> reminderList;
		
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
