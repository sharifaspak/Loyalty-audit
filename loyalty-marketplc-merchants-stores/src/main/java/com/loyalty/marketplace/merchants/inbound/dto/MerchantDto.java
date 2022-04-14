package com.loyalty.marketplace.merchants.inbound.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;

public class MerchantDto {

	@NotEmpty(message = "{validation.merchantDto.merchantCode.notEmpty.msg}")
	private String merchantCode;

	@NotEmpty(message = "{validation.merchantDto.merchantNameEn.notEmpty.msg}")
	private String merchantNameEn;

	@NotEmpty(message = "{validation.merchantDto.merchantNameAr.notEmpty.msg}")
	private String merchantNameAr;

	private String externalName;

	private String whatYouGetEn;

	private String whatYouGetAr;

	private String tnCEn;

	private String tnCAr;

	private String merchantDescEn;

	private String merchantDescAr;
	
	private boolean optInOrOut;

	public boolean isOptInOrOut() {
		return optInOrOut;
	}

	public void setOptInOrOut(boolean optInOrOut) {
		this.optInOrOut = optInOrOut;
	}

	@NotEmpty(message = "{validation.merchantDto.partnerCode.notEmpty.msg}")
	private String partnerCode;

	@NotEmpty(message = "{validation.merchantDto.category.notEmpty.msg}")
	private String category;

	@NotEmpty(message = "{validation.merchantDto.barcodeType.notEmpty.msg}")
	private String barcodeType;

	//@Valid
	private List<MerchantBillingRateDto> discountBillingRates;

	@Valid
	@NotEmpty(message = "{validation.merchantDto.contactPersons.notEmpty.msg}")
	private List<ContactPersonDto> contactPersons;

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

	public List<MerchantBillingRateDto> getDiscountBillingRates() {
		return discountBillingRates;
	}

	public void setDiscountBillingRates(List<MerchantBillingRateDto> discountBillingRates) {
		this.discountBillingRates = discountBillingRates;
	}

	public List<ContactPersonDto> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(List<ContactPersonDto> contactPersons) {
		this.contactPersons = contactPersons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcodeType == null) ? 0 : barcodeType.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((contactPersons == null) ? 0 : contactPersons.hashCode());
		result = prime * result + ((discountBillingRates == null) ? 0 : discountBillingRates.hashCode());
		result = prime * result + ((externalName == null) ? 0 : externalName.hashCode());
		result = prime * result + ((merchantCode == null) ? 0 : merchantCode.hashCode());
		result = prime * result + ((merchantDescAr == null) ? 0 : merchantDescAr.hashCode());
		result = prime * result + ((merchantDescEn == null) ? 0 : merchantDescEn.hashCode());
		result = prime * result + ((merchantNameAr == null) ? 0 : merchantNameAr.hashCode());
		result = prime * result + ((merchantNameEn == null) ? 0 : merchantNameEn.hashCode());
		result = prime * result + ((partnerCode == null) ? 0 : partnerCode.hashCode());
		result = prime * result + ((tnCAr == null) ? 0 : tnCAr.hashCode());
		result = prime * result + ((tnCEn == null) ? 0 : tnCEn.hashCode());
		result = prime * result + ((whatYouGetAr == null) ? 0 : whatYouGetAr.hashCode());
		result = prime * result + ((whatYouGetEn == null) ? 0 : whatYouGetEn.hashCode());
		return result;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof MerchantDto)) {
            return false;
        }
        MerchantDto merchant  = (MerchantDto) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getBarcodeType(), merchant.getBarcodeType());
        builder.append(getCategory(), merchant.getCategory());
        builder.append(getContactPersons(), merchant.getContactPersons());
        builder.append(getDiscountBillingRates(), merchant.getDiscountBillingRates());
        builder.append(getExternalName(), merchant.getExternalName());
        builder.append(getMerchantCode(), merchant.getMerchantCode());
        builder.append(getMerchantDescAr(), merchant.getMerchantDescAr());
        builder.append(getMerchantDescEn(), merchant.getMerchantDescEn());
        builder.append(getMerchantNameAr(), merchant.getMerchantNameAr());
        builder.append(getMerchantNameEn(), merchant.getMerchantNameEn());
        builder.append(getPartnerCode(), merchant.getPartnerCode());
        builder.append(getTnCAr(), merchant.getTnCAr());
        builder.append(getTnCEn(), merchant.getTnCEn());
        builder.append(getWhatYouGetAr(), merchant.getWhatYouGetAr());
        builder.append(getWhatYouGetEn(), merchant.getWhatYouGetEn());
        return builder.isEquals();
    }

	@Override
	public String toString() {
		return "MerchantDto [merchantCode=" + merchantCode + ", merchantNameEn=" + merchantNameEn + ", merchantNameAr="
				+ merchantNameAr + ", externalName=" + externalName + ", whatYouGetEn=" + whatYouGetEn
				+ ", whatYouGetAr=" + whatYouGetAr + ", tnCEn=" + tnCEn + ", tnCAr=" + tnCAr + ", merchantDescEn="
				+ merchantDescEn + ", merchantDescAr=" + merchantDescAr + ", partnerCode=" + partnerCode + ", category="
				+ category + ", barcodeType=" + barcodeType + ", discountBillingRates=" + discountBillingRates
				+ ", contactPersons=" + contactPersons + "]";
	}

}
