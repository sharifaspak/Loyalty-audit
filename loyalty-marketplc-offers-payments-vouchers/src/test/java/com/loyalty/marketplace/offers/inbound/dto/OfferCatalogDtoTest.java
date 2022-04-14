package com.loyalty.marketplace.offers.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OfferCatalogDto.class)
@ActiveProfiles("unittest")
public class OfferCatalogDtoTest {

	private OfferCatalogDto offerCatalogDto;
	
	@Before
	public void setUp(){
		offerCatalogDto = new OfferCatalogDto();
		offerCatalogDto.setOfferCode("");
		offerCatalogDto.setOfferTypeId("");
		offerCatalogDto.setOfferLabelEn("");
		offerCatalogDto.setOfferLabelAr("");
		offerCatalogDto.setOfferTitleEn("");
		offerCatalogDto.setOfferTitleAr("");
		offerCatalogDto.setOfferTitleDescriptionEn("");
		offerCatalogDto.setOfferTitleDescriptionAr("");
		offerCatalogDto.setOfferDetailMobileEn("");
		offerCatalogDto.setOfferDetailMobileAr("");
		offerCatalogDto.setOfferDetailWebEn("");
		offerCatalogDto.setOfferDetailWebAr("");
		offerCatalogDto.setBrandDescriptionEn("");
		offerCatalogDto.setBrandDescriptionAr("");
		offerCatalogDto.setTermsAndConditionsEn("");
		offerCatalogDto.setTermsAndConditionsAr("");
		offerCatalogDto.setAdditionalTermsAndConditionsEn("");
		offerCatalogDto.setAdditionalTermsAndConditionsAr("");
		offerCatalogDto.setTagsEn("");
		offerCatalogDto.setTagsAr("");
		offerCatalogDto.setWhatYouGetEn("");
		offerCatalogDto.setWhatYouGetAr("");
		offerCatalogDto.setOfferStartDate("");
		offerCatalogDto.setOfferEndDate("");
		offerCatalogDto.setTrendingRank(0);
		offerCatalogDto.setStatus("");
		offerCatalogDto.setAvailableInPortals(new ArrayList<>(1));
		offerCatalogDto.setNewOffer("");
		offerCatalogDto.setIsGift("");
		offerCatalogDto.setGiftChannels(new ArrayList<>());
		offerCatalogDto.setGiftSubCardTypes(new ArrayList<>());
		offerCatalogDto.setIsDod("");
		offerCatalogDto.setIsFeatured("");
		offerCatalogDto.setPointsValue(0);
		offerCatalogDto.setCost(1.0);
		offerCatalogDto.setDiscountPerc(0);
		offerCatalogDto.setEstSavings(100.0);
        offerCatalogDto.setLimit(new ArrayList<>(1));
		offerCatalogDto.setSharing("");
		offerCatalogDto.setSharingBonus(0);
		offerCatalogDto.setVatPercentage(100.0);
		offerCatalogDto.setVoucherExpiryDate("");
		offerCatalogDto.setPartnerCode("");
		offerCatalogDto.setMerchantCode("");
		offerCatalogDto.setStoreCodes(new ArrayList<String>());
		offerCatalogDto.setCategoryId("");
		offerCatalogDto.setSubCategoryId("");
		offerCatalogDto.setDynamicDenomination("");
		offerCatalogDto.setGroupedFlag("");
		offerCatalogDto.setMinDenomination(0);
		offerCatalogDto.setMaxDenomination(0);
		offerCatalogDto.setIncrementalValue(0);
		offerCatalogDto.setCustomerTypes(new ListValuesDto());
		offerCatalogDto.setCustomerSegments(new ListValuesDto());
		offerCatalogDto.setDenominations(new ArrayList<String>());
		offerCatalogDto.setVoucherAction("");
		offerCatalogDto.setRules(new ArrayList<String>());
		offerCatalogDto.setProvisioningChannel("");
		offerCatalogDto.setRatePlanCode("");
		offerCatalogDto.setRtfProductCode("");
		offerCatalogDto.setRtfProductType("");
		offerCatalogDto.setVasCode("");
		offerCatalogDto.setVasActionId("");
		offerCatalogDto.setAction("");
		offerCatalogDto.setSubOffer(new ArrayList<SubOfferDto>());
		offerCatalogDto.setEarnMultiplier(100.0);
		offerCatalogDto.setAccrualActivityCode("");
		offerCatalogDto.setRedemptionActivityCode("");
		offerCatalogDto.setVoucherAmount(0.0);
		offerCatalogDto.setPromotionalPeriod(0);
		offerCatalogDto.setFeature("");
		offerCatalogDto.setServiceId("");
		offerCatalogDto.setActivityId("");
		offerCatalogDto.setPackName("");
		offerCatalogDto.setStaticRating(0);
		offerCatalogDto.setOfferRating("");
		offerCatalogDto.setVoucherExpiryPeriod(0);
		offerCatalogDto.setIsBirthdayGift("");
		offerCatalogDto.setAccrualId("");
		offerCatalogDto.setAccrualCodeDescriptionEn("");
		offerCatalogDto.setAccrualCodeDescriptionAr("");
		offerCatalogDto.setRedemptionId("");
		offerCatalogDto.setRedemptionCodeDescriptionEn("");
		offerCatalogDto.setRedemptionCodeDescriptionAr("");

		
	}
	
	@Test
	public void testGetters() {
		assertNotNull(offerCatalogDto.getRedemptionActivityCode());
		assertNotNull(offerCatalogDto.getAccrualActivityCode());
		assertNotNull(offerCatalogDto.getEarnMultiplier());
		assertNotNull(offerCatalogDto.getSubOffer());
		assertNotNull(offerCatalogDto.getAction());
		assertNotNull(offerCatalogDto.getOfferCode());
		assertNotNull(offerCatalogDto.getOfferTypeId());
		assertNotNull(offerCatalogDto.getOfferLabelEn());
		assertNotNull(offerCatalogDto.getOfferLabelAr());
		assertNotNull(offerCatalogDto.getOfferTitleEn());
		assertNotNull(offerCatalogDto.getOfferTitleAr());
		assertNotNull(offerCatalogDto.getOfferTitleDescriptionEn());
		assertNotNull(offerCatalogDto.getOfferTitleDescriptionAr());
		assertNotNull(offerCatalogDto.getOfferDetailMobileEn());
		assertNotNull(offerCatalogDto.getOfferDetailMobileAr());
		assertNotNull(offerCatalogDto.getOfferDetailWebEn());
		assertNotNull(offerCatalogDto.getOfferDetailWebAr());
		assertNotNull(offerCatalogDto.getBrandDescriptionEn());
		assertNotNull(offerCatalogDto.getBrandDescriptionAr());
		assertNotNull(offerCatalogDto.getTermsAndConditionsEn());
		assertNotNull(offerCatalogDto.getTermsAndConditionsAr());
		assertNotNull(offerCatalogDto.getAdditionalTermsAndConditionsEn());
		assertNotNull(offerCatalogDto.getAdditionalTermsAndConditionsAr());
		assertNotNull(offerCatalogDto.getTagsEn());
		assertNotNull(offerCatalogDto.getTagsAr());
		assertNotNull(offerCatalogDto.getWhatYouGetEn());
		assertNotNull(offerCatalogDto.getWhatYouGetAr());
		assertNotNull(offerCatalogDto.getOfferStartDate());
		assertNotNull(offerCatalogDto.getOfferEndDate());
		assertNotNull(offerCatalogDto.getTrendingRank());
		assertNotNull(offerCatalogDto.getStatus());
		assertNotNull(offerCatalogDto.getAvailableInPortals());
		assertNotNull(offerCatalogDto.getNewOffer());
		assertNotNull(offerCatalogDto.getIsGift());
		assertNotNull(offerCatalogDto.getIsDod());
		assertNotNull(offerCatalogDto.getIsFeatured());
		assertNotNull(offerCatalogDto.getPointsValue());
		assertNotNull(offerCatalogDto.getCost());
		assertNotNull(offerCatalogDto.getDiscountPerc());
		assertNotNull(offerCatalogDto.getEstSavings());
		assertNotNull(offerCatalogDto.getLimit());
		assertNotNull(offerCatalogDto.getSharing());
		assertNotNull(offerCatalogDto.getSharingBonus());
		assertNotNull(offerCatalogDto.getVatPercentage());
		assertNotNull(offerCatalogDto.getVoucherExpiryDate());
		assertNotNull(offerCatalogDto.getPartnerCode());
		assertNotNull(offerCatalogDto.getMerchantCode());
		assertNotNull(offerCatalogDto.getStoreCodes());
		assertNotNull(offerCatalogDto.getCategoryId());
		assertNotNull(offerCatalogDto.getSubCategoryId());
		assertNotNull(offerCatalogDto.getDynamicDenomination());
		assertNotNull(offerCatalogDto.getGroupedFlag());
		assertNotNull(offerCatalogDto.getMinDenomination());
		assertNotNull(offerCatalogDto.getMaxDenomination());
		assertNotNull(offerCatalogDto.getIncrementalValue());
		assertNotNull(offerCatalogDto.getCustomerTypes());
		assertNotNull(offerCatalogDto.getCustomerSegments());
		assertNotNull(offerCatalogDto.getDenominations());
		assertNotNull(offerCatalogDto.getVoucherAction());
		assertNotNull(offerCatalogDto.getRules());
		assertNotNull(offerCatalogDto.getProvisioningChannel());
		assertNotNull(offerCatalogDto.getRatePlanCode());
		assertNotNull(offerCatalogDto.getRtfProductCode());
		assertNotNull(offerCatalogDto.getRtfProductType());
		assertNotNull(offerCatalogDto.getVasCode());
		assertNotNull(offerCatalogDto.getVasActionId());
		assertNotNull(offerCatalogDto.getVoucherAmount());
		assertNotNull(offerCatalogDto.getPromotionalPeriod());
		assertNotNull(offerCatalogDto.getFeature());
		assertNotNull(offerCatalogDto.getServiceId());
		assertNotNull(offerCatalogDto.getActivityId());
		assertNotNull(offerCatalogDto.getPackName());
		assertNotNull(offerCatalogDto.getAction());
		assertNotNull(offerCatalogDto.getStaticRating());
		assertNotNull(offerCatalogDto.getOfferRating());
		assertNotNull(offerCatalogDto.getVoucherExpiryPeriod());
		assertNotNull(offerCatalogDto.getIsBirthdayGift());
		assertNotNull(offerCatalogDto.getAccrualId());
		assertNotNull(offerCatalogDto.getAccrualCodeDescriptionEn());
		assertNotNull(offerCatalogDto.getAccrualCodeDescriptionAr());
		assertNotNull(offerCatalogDto.getRedemptionId());
		assertNotNull(offerCatalogDto.getRedemptionCodeDescriptionEn());
		assertNotNull(offerCatalogDto.getRedemptionCodeDescriptionAr());
		assertNotNull(offerCatalogDto.getGiftChannels());
		assertNotNull(offerCatalogDto.getGiftSubCardTypes());
		assertNotNull(offerCatalogDto.getCustomerTypes());
		assertNotNull(offerCatalogDto.getCustomerSegments());
		
	}
	
	@Test
	public void testToString() {
		assertNotNull(offerCatalogDto.toString());
	}
	
}
