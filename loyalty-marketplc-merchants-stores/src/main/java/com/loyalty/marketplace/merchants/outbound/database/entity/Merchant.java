package com.loyalty.marketplace.merchants.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.merchants.constants.DBConstants;
import com.loyalty.marketplace.outbound.database.entity.Category;

@Document(collection = DBConstants.MERCHANT)
public class Merchant {

	@Field("ProgramCode")
	private String programCode;
	
	@Id
	private String id;

	@Indexed
	@Field("MerchantCode")
	private String merchantCode;

	@Indexed
	@Field("Name")
	private MerchantName merchantName;

	@Field("PartnerCode")
	private String partnerCode;

	@Field("Category")
	@DBRef
	private Category category;
	
	@Field("BarcodeType")
	@DBRef
	private Barcode barcodeType;

	@Field("Status")
	private String status;
	
	@Field("WhatYouGet")
	private WhatYouGet whatYouGet;
	
	@Field("TAndC")
	private TAndC tnC;
	
	@Field("MerchantDescription")
	private MerchantDescription merchantDescription;
	
	@Field("ExternalName")
	private String externalName;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;

	@Field("ContactPersons")
	private List<ContactPerson> contactPersons;

	@Field("DiscountBillingRates")
	private List<MerchantBillingRate> billingRates;
	
	@Field("ImageUrl")
	private String imageUrl;
	
	@Field("OfferCount")
	private long offerCount;
	
	
	public long getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(long offerCount) {
		this.offerCount = offerCount;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the programCode
	 */
	public String getProgramCode() {
		return programCode;
	}

	/**
	 * @param programCode the programCode to set
	 */
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	/**
	 * @return the merchantCode
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * @param merchantCode the merchantCode to set
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the merchantName
	 */
	public MerchantName getMerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(MerchantName merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * @return the partnerCode
	 */
	public String getPartnerCode() {
		return partnerCode;
	}

	/**
	 * @param partnerCode the partnerCode to set
	 */
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the barcodeType
	 */
	public Barcode getBarcodeType() {
		return barcodeType;
	}

	/**
	 * @param barcodeType the barcodeType to set
	 */
	public void setBarcodeType(Barcode barcodeType) {
		this.barcodeType = barcodeType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the whatYouGet
	 */
	public WhatYouGet getWhatYouGet() {
		return whatYouGet;
	}

	/**
	 * @param whatYouGet the whatYouGet to set
	 */
	public void setWhatYouGet(WhatYouGet whatYouGet) {
		this.whatYouGet = whatYouGet;
	}

	/**
	 * @return the tnC
	 */
	public TAndC getTnC() {
		return tnC;
	}

	/**
	 * @param tnC the tnC to set
	 */
	public void setTnC(TAndC tnC) {
		this.tnC = tnC;
	}

	/**
	 * @return the merchantDescription
	 */
	public MerchantDescription getMerchantDescription() {
		return merchantDescription;
	}

	/**
	 * @param merchantDescription the merchantDescription to set
	 */
	public void setMerchantDescription(MerchantDescription merchantDescription) {
		this.merchantDescription = merchantDescription;
	}

	/**
	 * @return the externalName
	 */
	public String getExternalName() {
		return externalName;
	}

	/**
	 * @param externalName the externalName to set
	 */
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	/**
	 * @return the usrCreated
	 */
	public String getUsrCreated() {
		return usrCreated;
	}

	/**
	 * @param usrCreated the usrCreated to set
	 */
	public void setUsrCreated(String usrCreated) {
		this.usrCreated = usrCreated;
	}

	/**
	 * @return the usrUpdated
	 */
	public String getUsrUpdated() {
		return usrUpdated;
	}

	/**
	 * @param usrUpdated the usrUpdated to set
	 */
	public void setUsrUpdated(String usrUpdated) {
		this.usrUpdated = usrUpdated;
	}

	/**
	 * @return the dtCreated
	 */
	public Date getDtCreated() {
		return dtCreated;
	}

	/**
	 * @param dtCreated the dtCreated to set
	 */
	public void setDtCreated(Date dtCreated) {
		this.dtCreated = dtCreated;
	}

	/**
	 * @return the dtUpdated
	 */
	public Date getDtUpdated() {
		return dtUpdated;
	}

	/**
	 * @param dtUpdated the dtUpdated to set
	 */
	public void setDtUpdated(Date dtUpdated) {
		this.dtUpdated = dtUpdated;
	}

	/**
	 * @return the contactPersons
	 */
	public List<ContactPerson> getContactPersons() {
		return contactPersons;
	}

	/**
	 * @param contactPersons the contactPersons to set
	 */
	public void setContactPersons(List<ContactPerson> contactPersons) {
		this.contactPersons = contactPersons;
	}

	/**
	 * @return the billingRates
	 */
	public List<MerchantBillingRate> getBillingRates() {
		return billingRates;
	}

	/**
	 * @param billingRates the billingRates to set
	 */
	public void setBillingRates(List<MerchantBillingRate> billingRates) {
		this.billingRates = billingRates;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Merchant [programCode=" + programCode + ", merchantCode=" + merchantCode + ", merchantName="
				+ merchantName + ", partnerCode=" + partnerCode + ", category=" + category + ", barcodeType="
				+ barcodeType + ", status=" + status + ", whatYouGet=" + whatYouGet + ", tnC=" + tnC
				+ ", merchantDescription=" + merchantDescription + ", externalName=" + externalName + ", usrCreated="
				+ usrCreated + ", usrUpdated=" + usrUpdated + ", dtCreated=" + dtCreated + ", dtUpdated=" + dtUpdated
				+ ", contactPersons=" + contactPersons + ", billingRates=" + billingRates + "]";
	}

	
	
	
}