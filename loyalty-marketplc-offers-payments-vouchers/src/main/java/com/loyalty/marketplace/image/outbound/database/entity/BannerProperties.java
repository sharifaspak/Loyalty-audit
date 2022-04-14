package com.loyalty.marketplace.image.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Document(collection = ImageConfigurationConstants.DB_BANNER_PROPERTIES)
public class BannerProperties {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("TopBannerLimit")
	private Integer topBannerLimit;
	
	@Field("MiddleBannerLimit")
	private Integer middleBannerLimit;
	
	@Field("BottomBannerLimit")
	private Integer bottomBannerLimit;
	
	@Field("IncludeRedeemedOffers")
	private boolean includeRedeemedOffers;
	
	@Field("PersonalizeBannerCount")
	private Integer personalizeBannerCount;
	
	@Field("FixedBannerCount")
	private Integer fixedBannerCount;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
