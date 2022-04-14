package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

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
@Document(collection = OffersDBConstants.BIRTHDAY_INFO)
public class BirthdayInfo {

	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("BirthdayTitle")
	private BirthdayTitle title;
	@Field("BirthdaySubTitle")
	private BirthdaySubTitle subTitle;
	@Field("BirthdayDescription")
	private BirthdayDescription description;
	@Field("BirthdayIconText")
	private BirthdayIconText iconText;
	@Field("BirthdayWeekIcon")
	private BirthdayWeekIcon weekIcon;
	@Field("PurchaseLimit")
	private Integer purchaseLimit;
	@Field("ThresholdPlusValue")
	private Integer thresholdPlusValue;
	@Field("ThresholdMinusValue")
	private Integer thresholdMinusValue;
	@Field("DisplayLimit")
	private Integer displayLimit;
	@Field("CreatedUser")
	private String createdUser;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
