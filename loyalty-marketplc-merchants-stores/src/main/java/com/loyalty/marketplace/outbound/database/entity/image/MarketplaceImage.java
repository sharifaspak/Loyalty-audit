package com.loyalty.marketplace.outbound.database.entity.image;

import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Document("MarketplaceImage")
public class MarketplaceImage {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("ImageCategory")
	private String imageCategory;
	
	@Field("Status")
	private String status;
	
	@Field("Image")
	private Binary image;
	
	@Field("ImageUrl")
	private String imageUrl;
	
	@Field("ImageUrlDR")
	private String imageUrlDr;
	
	@Field("ImageUrlProd")
	private String imageUrlProd;
	
	@Field("OriginalFileName")
	private String originalFileName;

	@Field("MerchantOfferImage")
	private MerchantOfferImage merchantOfferImage;
	
	@Field("BannerImage")
	private BannerImage bannerImage;
	
	@Field("GiftingImage")
	private GiftingImage giftingImage;
	
	@Field("UploadedUser")
	private String uploadedUser;
	
	@Field("UploadedDate")
	private Date uploadedDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
