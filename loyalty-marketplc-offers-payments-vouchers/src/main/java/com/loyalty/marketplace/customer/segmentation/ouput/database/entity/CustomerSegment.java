//package com.loyalty.marketplace.customer.segmentation.ouput.database.entity;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import com.loyalty.marketplace.offers.constants.OffersDBConstants;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@ToString
//@Document(collection = OffersDBConstants.CUSTOMER_SEGMENTATION)
//public class CustomerSegment {
//	
//	@Id
//	private String id;
//	
//	@Field("Name")
//	private String name;
//		
//	@Field("ChannelId")
//	private ListValues channelId;
//	@Field("TierLevel")
//	private ListValues tierLevel;
//	@Field("Gender")
//    private ListValues gender;
//	@Field("Nationality")
//	private ListValues nationality;
//	@Field("NumberType")
//	private ListValues numberType;
//	@Field("AccountStatus")
//	private ListValues accountStatus;
//	@Field("EmailVerificationStatus")
//	private ListValues emailVerificationStatus;
//	
//	@Field("Days")
//	private ListValues days;
//	
//	@Field("CustomerType")
//	private ListValues customerType;
//	@Field("CobrandedCards")
//	private ListValues cobrandedCards;
//	
//	@Field("TotalTierPoints")
//	private IntegerValues totalTierPoints;
//	@Field("TotalAccountPoints")
//	private IntegerValues totalAccountPoints;
//	
//	@Field("DOB")
//	private DateValues dob;
//	@Field("AccountStartDate")
//	private DateValues accountStartDate;
//		
//	@Field("IsFirstAccess")
//	private boolean isFirstAccess;
//	@Field("IsCoBranded")
//	private boolean isCoBranded;
//	@Field("IsSubscribed")
//	private boolean isSubscribed;
//	@Field("IsPrimaryAccount")
//	private boolean isPrimaryAccount;
//	@Field("IsTop3Account")
//	private boolean isTop3Account;
//		
//	@Field("PurchaseValues")
//	private List<PurchaseValues> purchaseItems;
//	
//	@Field("CreatedDate")
//	private Date createdDate;
//	@Field("CreatedUser")
//	private String createdUser;
//	@Field("UpdatedDate")
//	private Date updatedDate;
//	@Field("UpdatedUser")
//	private String updatedUser;
//	
//}
