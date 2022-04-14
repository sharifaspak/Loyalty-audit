package com.loyalty.marketplace.merchants.outbound.dto;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.stores.outbound.dto.StoreResult;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class MerchantStoreOfferResult {

	private String merchantCode;	
	
	private String merchantNameEn;	

	private String merchantNameAr;
	
	private String externalName;
	
	private String whatYouGetEn;
	
	private String whatYouGetAr;
	
	private String tnCEn;
	
	private String tnCAr;
	
	private String merchantDescEn;

	private String merchantDescAr;

	private String partnerCode;

	private String category;
	
    private String categoryId;
	
	private String categoryNameEn;
	
	private String categoryNameAr;

	private String barcodeType;
	
	private String barcodeId;
	
	private String status;
	
	private List<ImageInfo> imageInfo;

	private List<MerchantBillingRateDto> billingRates;

	private List<ContactPersonDto> contactPersons;
	
	private List<StoreResult> stores;
	
	private List<MerchantOffers> offers;

	public List<ImageInfo> getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(List<ImageInfo> imageInfo) {
		this.imageInfo = imageInfo;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantNameEn() {
		return merchantNameEn;
	}

	public void setMerchantNameEn(String merchantNameEn) {
		this.merchantNameEn = merchantNameEn;
	}

	public String getMerchantNameAr() {
		return merchantNameAr;
	}

	public void setMerchantNameAr(String merchantNameAr) {
		this.merchantNameAr = merchantNameAr;
	}

	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public String getWhatYouGetEn() {
		return whatYouGetEn;
	}

	public void setWhatYouGetEn(String whatYouGetEn) {
		this.whatYouGetEn = whatYouGetEn;
	}

	public String getWhatYouGetAr() {
		return whatYouGetAr;
	}

	public void setWhatYouGetAr(String whatYouGetAr) {
		this.whatYouGetAr = whatYouGetAr;
	}

	public String getTnCEn() {
		return tnCEn;
	}

	public void setTnCEn(String tnCEn) {
		this.tnCEn = tnCEn;
	}

	public String getTnCAr() {
		return tnCAr;
	}

	public void setTnCAr(String tnCAr) {
		this.tnCAr = tnCAr;
	}

	public String getMerchantDescEn() {
		return merchantDescEn;
	}

	public void setMerchantDescEn(String merchantDescEn) {
		this.merchantDescEn = merchantDescEn;
	}

	public String getMerchantDescAr() {
		return merchantDescAr;
	}

	public void setMerchantDescAr(String merchantDescAr) {
		this.merchantDescAr = merchantDescAr;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<MerchantBillingRateDto> getBillingRates() {
		return billingRates;
	}

	public void setBillingRates(List<MerchantBillingRateDto> billingRates) {
		this.billingRates = billingRates;
	}

	public List<ContactPersonDto> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ContactPersonDto> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public List<StoreResult> getStores() {
		return stores;
	}

	public void setStores(List<StoreResult> stores) {
		this.stores = stores;
	}

	public List<MerchantOffers> getOffers() {
		return offers;
	}

	public void setOffers(List<MerchantOffers> offers) {
		this.offers = offers;
	}

	@Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getBarcodeType());
        builder.append(getCategory());
        builder.append(getContactPersons());
        builder.append(getBillingRates());
        builder.append(getExternalName());
        builder.append(getMerchantCode());
        builder.append(getMerchantDescAr());
        builder.append(getMerchantDescEn());
        builder.append(getMerchantNameAr());
        builder.append(getMerchantNameEn());
        builder.append(getOffers());
        builder.append(getPartnerCode());
        builder.append(getStatus());
        builder.append(getStores());
        builder.append(getWhatYouGetAr());
        builder.append(getWhatYouGetEn());
        return builder.hashCode();
    }

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof MerchantStoreOfferResult)) {
            return false;
        }
        MerchantStoreOfferResult merchant  = (MerchantStoreOfferResult) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getBarcodeType(), merchant.getBarcodeType());
        builder.append(getCategory(), merchant.getCategory());
        builder.append(getContactPersons(), merchant.getContactPersons());
        builder.append(getExternalName(), merchant.getExternalName());
        builder.append(getBillingRates(), merchant.getBillingRates());
        builder.append(getMerchantCode(), merchant.getMerchantCode());
        builder.append(getMerchantDescAr(), merchant.getMerchantDescAr());
        builder.append(getMerchantDescEn(), merchant.getMerchantDescEn());
        builder.append(getMerchantNameAr(), merchant.getMerchantNameAr());
        builder.append(getMerchantNameEn(), merchant.getMerchantNameEn());
        builder.append(getOffers(), merchant.getOffers());
        builder.append(getPartnerCode(), merchant.getPartnerCode());
        builder.append(getStatus(), merchant.getStatus());
        builder.append(getStores(), merchant.getStores());
        builder.append(getTnCAr(), merchant.getTnCAr());
        builder.append(getTnCEn(), merchant.getTnCEn());
        builder.append(getWhatYouGetAr(), merchant.getWhatYouGetAr());
        builder.append(getWhatYouGetEn(), merchant.getWhatYouGetEn());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "MerchantStoreOfferResult [merchantCode=" + merchantCode + ", merchantNameEn=" + merchantNameEn
				+ ", merchantNameAr=" + merchantNameAr + ", externalName=" + externalName + ", whatYouGetEn="
				+ whatYouGetEn + ", whatYouGetAr=" + whatYouGetAr + ", tnCEn=" + tnCEn + ", tnCAr=" + tnCAr
				+ ", merchantDescEn=" + merchantDescEn + ", merchantDescAr=" + merchantDescAr + ", partnerCode="
				+ partnerCode + ", category=" + category + ", barcodeType=" + barcodeType + ", status=" + status
				+ ", discountBillingRates=" + billingRates + ", contactPersons=" + contactPersons + ", stores="
				+ stores + ", offers=" + offers + "]";
	}
	
}
