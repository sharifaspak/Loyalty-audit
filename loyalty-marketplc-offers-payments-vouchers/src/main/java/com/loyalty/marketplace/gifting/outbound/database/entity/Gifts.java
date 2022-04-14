package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = GiftingConfigurationConstants.GIFTS)
public class Gifts {
	
	@Id
	private String id;
	@Field("ProgramCode")
	private String programCode;
	@Field("GiftType")
	private String giftType;
	@Field("GiftDetails")
	private GiftValues giftDetails;
	@Field("OfferValues")
	private List<OfferGiftValues> offerValues;
	@Field("IsActive")
	private Boolean isActive;
	@Field("CreatedUser")
	private String createdUser;
	@Field("CreatedDate")
	private Date createdDate;
	@Field("UpdatedUser")
	private String updatedUser;
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
