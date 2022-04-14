package com.loyalty.marketplace.offers.outbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(classes = OfferCatalogResultResponseDto.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class OfferCatalogResultResponseDtoTest {

	private OfferCatalogResultResponseDto offerCatalogResultResponseDto;

	@Before
	public void setUp() {
		offerCatalogResultResponseDto = new OfferCatalogResultResponseDto();
		offerCatalogResultResponseDto.setOfferId("");
		offerCatalogResultResponseDto.setOfferCode("");
		offerCatalogResultResponseDto.setOfferTypeId("");
		offerCatalogResultResponseDto.setTypeDescriptionAr("");
		offerCatalogResultResponseDto.setTypeDescriptionEn("");
		offerCatalogResultResponseDto.setPaymentMethods(new ArrayList<PaymentMethodDto>());
		offerCatalogResultResponseDto.setOfferLabelEn("");
		offerCatalogResultResponseDto.setOfferLabelAr("");
		offerCatalogResultResponseDto.setOfferTitleAr("");
		offerCatalogResultResponseDto.setOfferTitleEn("");
		offerCatalogResultResponseDto.setOfferTitleDescriptionAr("");
		offerCatalogResultResponseDto.setOfferTitleDescriptionEn("");
		offerCatalogResultResponseDto.setOfferDetailMobileAr("");
		offerCatalogResultResponseDto.setOfferDetailMobileEn("");
		offerCatalogResultResponseDto.setOfferDetailWebAr("");
		offerCatalogResultResponseDto.setOfferDetailWebEn("");
		offerCatalogResultResponseDto.setBrandDescriptionAr("");
		offerCatalogResultResponseDto.setBrandDescriptionEn("");
		offerCatalogResultResponseDto.setTermsAndConditionsAr("");
		offerCatalogResultResponseDto.setTermsAndConditionsEn("");
		offerCatalogResultResponseDto.setAdditionalTermsAndConditionsAr("");
		offerCatalogResultResponseDto.setAdditionalTermsAndConditionsEn("");
		offerCatalogResultResponseDto.setTagsAr("");
		offerCatalogResultResponseDto.setTagsEn("");
		offerCatalogResultResponseDto.setWhatYouGetAr("");
		offerCatalogResultResponseDto.setWhatYouGetEn("");
		offerCatalogResultResponseDto.setOfferStartDate(new Date());
		offerCatalogResultResponseDto.setOfferEndDate(new Date());
		offerCatalogResultResponseDto.setVoucherExpiryDate(new Date());
		offerCatalogResultResponseDto.setVoucherExpiryPeriod(1);
		offerCatalogResultResponseDto.setTrendingRank(1);
		offerCatalogResultResponseDto.setStatus("");
		offerCatalogResultResponseDto.setAvailableInPortals(new ArrayList<String>());
		offerCatalogResultResponseDto.setNewOffer("");
		offerCatalogResultResponseDto.setIsGift("");
		offerCatalogResultResponseDto.setIsFeatured("");
		offerCatalogResultResponseDto.setIsDod("");
		offerCatalogResultResponseDto.setPointsValue(1);
		offerCatalogResultResponseDto.setCost(1.0);
		offerCatalogResultResponseDto.setDiscountPerc(1);
		offerCatalogResultResponseDto.setEstSavings(1.0);
		offerCatalogResultResponseDto.setSharing("");
		offerCatalogResultResponseDto.setSharingBonus(1);
		offerCatalogResultResponseDto.setVatPercentage(1.0);
		offerCatalogResultResponseDto.setOfferLimit(new ArrayList<>());
		offerCatalogResultResponseDto.setPartnerCode("");
		offerCatalogResultResponseDto.setMerchantCode("");
		offerCatalogResultResponseDto.setMerchantNameAr("");
		offerCatalogResultResponseDto.setMerchantNameEn("");
		offerCatalogResultResponseDto.setOfferStores(new ArrayList<StoreDto>());
		offerCatalogResultResponseDto.setCategoryId("");
		offerCatalogResultResponseDto.setCategoryNameAr("");
		offerCatalogResultResponseDto.setCategoryNameEn("");
		offerCatalogResultResponseDto.setSubCategoryId("");
		offerCatalogResultResponseDto.setSubCategoryNameAr("");
		offerCatalogResultResponseDto.setSubCategoryNameEn("");
		offerCatalogResultResponseDto.setDynamicDenomination("");
		offerCatalogResultResponseDto.setGroupedFlag("");
		offerCatalogResultResponseDto.setMinDenomination(1);
		offerCatalogResultResponseDto.setMaxDenomination(1);
		offerCatalogResultResponseDto.setIncrementalValue(1);
		offerCatalogResultResponseDto.setEligibleCustomerTypes(new ArrayList<>());
		offerCatalogResultResponseDto.setExclusionCustomerTypes(new ArrayList<>());
		offerCatalogResultResponseDto.setEligibleCustomerSegments(new ArrayList<>());
		offerCatalogResultResponseDto.setExclusionCustomerSegments(new ArrayList<>());
		offerCatalogResultResponseDto.setDenominations(new ArrayList<DenominationDto>());
		offerCatalogResultResponseDto.setVoucherAction("");
		offerCatalogResultResponseDto.setRules(new ArrayList<String>());
		offerCatalogResultResponseDto.setSubOffer(new ArrayList<SubOfferDto>());
		offerCatalogResultResponseDto.setEarnMultiplier(1.0);
		offerCatalogResultResponseDto.setAccActivityCd("");
		offerCatalogResultResponseDto.setRedActivityCd("");
		offerCatalogResultResponseDto.setProvisioningChannel("");
		offerCatalogResultResponseDto.setRatePlanCode("");
		offerCatalogResultResponseDto.setRtfProductCode("");
		offerCatalogResultResponseDto.setRtfProductType("");
		offerCatalogResultResponseDto.setVasCode("");
		offerCatalogResultResponseDto.setVasActionId("");
		offerCatalogResultResponseDto.setEligibility(new Eligibility());
        offerCatalogResultResponseDto.setImageUrlList(new ArrayList<>());
        offerCatalogResultResponseDto.setVoucherAmount(0.0);
        offerCatalogResultResponseDto.setStaticRating(0);
        offerCatalogResultResponseDto.setAverageRating(0.0);
        offerCatalogResultResponseDto.setAccActivityCodeDescriptionAr("");
        offerCatalogResultResponseDto.setAccActivityCodeDescriptionEn("");
        offerCatalogResultResponseDto.setAccActivityId("");
        offerCatalogResultResponseDto.setRedActivityCodeDescriptionAr("");
        offerCatalogResultResponseDto.setRedActivityCodeDescriptionEn("");
        offerCatalogResultResponseDto.setRedActivityId("");
        offerCatalogResultResponseDto.setBirthdayGiftAvailed("");
        offerCatalogResultResponseDto.setActivityIdRbt("");
        offerCatalogResultResponseDto.setFeature("");
        offerCatalogResultResponseDto.setPackName("");
        offerCatalogResultResponseDto.setGiftChannels(new ArrayList<>());
        offerCatalogResultResponseDto.setGiftSubCardTypes(new ArrayList<>());
        offerCatalogResultResponseDto.setIsBirthdayGift("");
        offerCatalogResultResponseDto.setMemberRatings(new ArrayList<>());
        offerCatalogResultResponseDto.setPromotionalPeriod(0);
        offerCatalogResultResponseDto.setServiceId("");
        offerCatalogResultResponseDto.setSoldCount(0);
        
	}

	@Test
	public void testGetters() {

		assertNotNull(offerCatalogResultResponseDto.getAccActivityCd());
		assertNotNull(offerCatalogResultResponseDto.getAdditionalTermsAndConditionsAr());
		assertNotNull(offerCatalogResultResponseDto.getAdditionalTermsAndConditionsEn());
		assertNotNull(offerCatalogResultResponseDto.getAvailableInPortals());
		assertNotNull(offerCatalogResultResponseDto.getBrandDescriptionAr());
		assertNotNull(offerCatalogResultResponseDto.getBrandDescriptionEn());
		assertNotNull(offerCatalogResultResponseDto.getCategoryId());
		assertNotNull(offerCatalogResultResponseDto.getCategoryNameAr());
		assertNotNull(offerCatalogResultResponseDto.getCategoryNameEn());
		assertNotNull(offerCatalogResultResponseDto.getCost());
		assertNotNull(offerCatalogResultResponseDto.getDenominations());
		assertNotNull(offerCatalogResultResponseDto.getDiscountPerc());
		assertNotNull(offerCatalogResultResponseDto.getDynamicDenomination());
		assertNotNull(offerCatalogResultResponseDto.getOfferLimit());
		assertNotNull(offerCatalogResultResponseDto.getEarnMultiplier());
		assertNotNull(offerCatalogResultResponseDto.getEligibility());
		assertNotNull(offerCatalogResultResponseDto.getEligibleCustomerTypes());
		assertNotNull(offerCatalogResultResponseDto.getExclusionCustomerTypes());
		assertNotNull(offerCatalogResultResponseDto.getEligibleCustomerSegments());
		assertNotNull(offerCatalogResultResponseDto.getExclusionCustomerSegments());
		assertNotNull(offerCatalogResultResponseDto.getEstSavings());
		assertNotNull(offerCatalogResultResponseDto.getGroupedFlag());
		assertNotNull(offerCatalogResultResponseDto.getIncrementalValue());
		assertNotNull(offerCatalogResultResponseDto.getIsDod());
		assertNotNull(offerCatalogResultResponseDto.getIsFeatured());
		assertNotNull(offerCatalogResultResponseDto.getIsGift());
		assertNotNull(offerCatalogResultResponseDto.getMaxDenomination());
		assertNotNull(offerCatalogResultResponseDto.getMerchantCode());
		assertNotNull(offerCatalogResultResponseDto.getMerchantNameAr());
		assertNotNull(offerCatalogResultResponseDto.getMerchantNameEn());
		assertNotNull(offerCatalogResultResponseDto.getMinDenomination());
		assertNotNull(offerCatalogResultResponseDto.getNewOffer());
		assertNotNull(offerCatalogResultResponseDto.getOfferCode());
		assertNotNull(offerCatalogResultResponseDto.getOfferDetailMobileAr());
		assertNotNull(offerCatalogResultResponseDto.getOfferDetailMobileEn());
		assertNotNull(offerCatalogResultResponseDto.getOfferDetailWebAr());
		assertNotNull(offerCatalogResultResponseDto.getOfferDetailWebEn());
		assertNotNull(offerCatalogResultResponseDto.getOfferEndDate());
		assertNotNull(offerCatalogResultResponseDto.getOfferId());
		assertNotNull(offerCatalogResultResponseDto.getOfferLabelAr());
		assertNotNull(offerCatalogResultResponseDto.getOfferLabelEn());
		assertNotNull(offerCatalogResultResponseDto.getOfferStartDate());
		assertNotNull(offerCatalogResultResponseDto.getOfferStores());
		assertNotNull(offerCatalogResultResponseDto.getOfferTitleAr());
		assertNotNull(offerCatalogResultResponseDto.getOfferTitleDescriptionAr());
		assertNotNull(offerCatalogResultResponseDto.getOfferTitleDescriptionEn());
		assertNotNull(offerCatalogResultResponseDto.getOfferTitleEn());
		assertNotNull(offerCatalogResultResponseDto.getOfferTypeId());
		assertNotNull(offerCatalogResultResponseDto.getPartnerCode());
		assertNotNull(offerCatalogResultResponseDto.getPaymentMethods());
		assertNotNull(offerCatalogResultResponseDto.getPointsValue());
		assertNotNull(offerCatalogResultResponseDto.getProvisioningChannel());
		assertNotNull(offerCatalogResultResponseDto.getRatePlanCode());
		assertNotNull(offerCatalogResultResponseDto.getRedActivityCd());
		assertNotNull(offerCatalogResultResponseDto.getRtfProductCode());
		assertNotNull(offerCatalogResultResponseDto.getRtfProductType());
		assertNotNull(offerCatalogResultResponseDto.getRules());
		assertNotNull(offerCatalogResultResponseDto.getSharing());
		assertNotNull(offerCatalogResultResponseDto.getSharingBonus());
		assertNotNull(offerCatalogResultResponseDto.getStatus());
		assertNotNull(offerCatalogResultResponseDto.getSubCategoryId());
		assertNotNull(offerCatalogResultResponseDto.getSubCategoryNameAr());
		assertNotNull(offerCatalogResultResponseDto.getSubCategoryNameEn());
		assertNotNull(offerCatalogResultResponseDto.getSubOffer());
		assertNotNull(offerCatalogResultResponseDto.getTagsAr());
		assertNotNull(offerCatalogResultResponseDto.getTagsEn());
		assertNotNull(offerCatalogResultResponseDto.getTermsAndConditionsAr());
		assertNotNull(offerCatalogResultResponseDto.getTermsAndConditionsEn());
		assertNotNull(offerCatalogResultResponseDto.getTrendingRank());
		assertNotNull(offerCatalogResultResponseDto.getTrendingRank());
		assertNotNull(offerCatalogResultResponseDto.getTypeDescriptionAr());
		assertNotNull(offerCatalogResultResponseDto.getTypeDescriptionEn());
		assertNotNull(offerCatalogResultResponseDto.getVasActionId());
		assertNotNull(offerCatalogResultResponseDto.getVasCode());
		assertNotNull(offerCatalogResultResponseDto.getVatPercentage());
		assertNotNull(offerCatalogResultResponseDto.getVoucherAction());
		assertNotNull(offerCatalogResultResponseDto.getVoucherExpiryDate());
		assertNotNull(offerCatalogResultResponseDto.getVoucherExpiryPeriod());
		assertNotNull(offerCatalogResultResponseDto.getWhatYouGetAr());
		assertNotNull(offerCatalogResultResponseDto.getWhatYouGetEn());
		assertNotNull(offerCatalogResultResponseDto.getImageUrlList());
		assertNotNull(offerCatalogResultResponseDto.getVoucherAmount());
		assertNotNull(offerCatalogResultResponseDto.getStaticRating());
		assertNotNull(offerCatalogResultResponseDto.getAverageRating());
		assertNotNull(offerCatalogResultResponseDto.getAccActivityCodeDescriptionAr());
		assertNotNull(offerCatalogResultResponseDto.getAccActivityCodeDescriptionEn());
		assertNotNull(offerCatalogResultResponseDto.getAccActivityId());
		assertNotNull(offerCatalogResultResponseDto.getRedActivityCodeDescriptionAr());
		assertNotNull(offerCatalogResultResponseDto.getRedActivityCodeDescriptionEn());
		assertNotNull(offerCatalogResultResponseDto.getRedActivityId());
		assertNotNull(offerCatalogResultResponseDto.getBirthdayGiftAvailed());
		assertNotNull(offerCatalogResultResponseDto.getActivityIdRbt());
		assertNotNull(offerCatalogResultResponseDto.getFeature());
		assertNotNull(offerCatalogResultResponseDto.getPackName());
		assertNotNull(offerCatalogResultResponseDto.getGiftChannels());
		assertNotNull(offerCatalogResultResponseDto.getGiftSubCardTypes());
		assertNotNull(offerCatalogResultResponseDto.getIsBirthdayGift());
		assertNotNull(offerCatalogResultResponseDto.getMemberRatings());
		assertNotNull(offerCatalogResultResponseDto.getPromotionalPeriod());
		assertNotNull(offerCatalogResultResponseDto.getServiceId());
		assertNotNull(offerCatalogResultResponseDto.getSoldCount());
	}

	/**
	 * 
	 * Test ToString
	 * 
	 */
	@Test
	public void testToString() {

		assertNotNull(offerCatalogResultResponseDto.toString());

	}
}
