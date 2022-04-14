package com.loyalty.marketplace.gifting.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.loyalty.marketplace.domain.model.NameDomain;
import com.loyalty.marketplace.gifting.domain.GiftDomain;
import com.loyalty.marketplace.gifting.domain.GiftValuesDomain;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDetailsDomain;
import com.loyalty.marketplace.gifting.domain.GoldCertificateDomain;
import com.loyalty.marketplace.gifting.domain.OfferGiftValuesDomain;
import com.loyalty.marketplace.gifting.helper.dto.GoldCertificateDto;
import com.loyalty.marketplace.gifting.inbound.dto.GiftConfigureRequestDto;
import com.loyalty.marketplace.gifting.inbound.dto.OfferValueDto;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificateTransaction;
import com.loyalty.marketplace.gifting.outbound.database.entity.OfferGiftValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.Checks;

public class GetValues {

	private static final Logger LOG = LoggerFactory.getLogger(GetValues.class);

	GetValues() {

	}

	public static GoldCertificateDomain getGoldCertificateDomain(GoldCertificateDto goldCertificateDto,
			GoldCertificate existingCertificate, Headers header, String action) {

		return new GoldCertificateDomain.GoldCertificateBuilder(goldCertificateDto.getAccountNumber(),
				goldCertificateDto.getMembershipCode())
						.id(Checks.checkIsActionInsert(action) ? null : existingCertificate.getId())
						.programCode(header.getProgram())
						.totalGoldBalance(
								getTotalGoldBalance(goldCertificateDto.getBalance(), existingCertificate, action))
						.totalSpentAmount(getAedAmount(goldCertificateDto.getAedAmount(), existingCertificate, action))
						.totalPointAmount(
								getPointValue(goldCertificateDto.getPointsValue(), existingCertificate, action))
						.certificateDetails(
								getCertificateTransactionList(goldCertificateDto, existingCertificate, action))
						.createdUser(Checks.checkIsActionInsert(action) ? header.getUserName()
								: existingCertificate.getCreatedUser())
						.createdDate(
								Checks.checkIsActionInsert(action) ? new Date() : existingCertificate.getCreatedDate())
						.updatedUser(header.getUserName()).updatedDate(new Date()).build();

	}

	private static List<GoldCertificateDetailsDomain> getCertificateTransactionList(
			GoldCertificateDto goldCertificateDto, GoldCertificate existingCertificate, String action) {

		List<GoldCertificateDetailsDomain> goldCertificateTransactionList = new ArrayList<>(1);

		if (Checks.checkIsActionUpdate(action) && !ObjectUtils.isEmpty(existingCertificate)
				&& !CollectionUtils.isEmpty(existingCertificate.getCertificateDetails())) {

			LOG.info("Inserting gold certificates");
			for (GoldCertificateTransaction goldCertificate : existingCertificate.getCertificateDetails()) {

				goldCertificateTransactionList.add(getGoldCertificateDomainFromEntity(goldCertificate));

			}
		}

		goldCertificateTransactionList.add(getGoldCertificateDomainFromDto(goldCertificateDto));
		return goldCertificateTransactionList;
	}

	private static GoldCertificateDetailsDomain getGoldCertificateDomainFromEntity(
			GoldCertificateTransaction goldCertificateTransaction) {

		return new GoldCertificateDetailsDomain(goldCertificateTransaction.getCertificateId(),
				goldCertificateTransaction.getTransactionId(), goldCertificateTransaction.getTransactionType(),
				goldCertificateTransaction.getPartnerCode(), goldCertificateTransaction.getMerchantCode(),
				new NameDomain(goldCertificateTransaction.getMerchantName().getEnglish(),
						goldCertificateTransaction.getMerchantName().getArabic()),
				goldCertificateTransaction.getOriginalGoldBalance(), goldCertificateTransaction.getCurrentGoldBalance(),
				goldCertificateTransaction.getSpentAmount(), goldCertificateTransaction.getPointAmount(),
				goldCertificateTransaction.getStartDate());

	}

	private static GoldCertificateDetailsDomain getGoldCertificateDomainFromDto(GoldCertificateDto goldCertificateDto) {

		return new GoldCertificateDetailsDomain(goldCertificateDto.getCertificateId(),
				goldCertificateDto.getTransactionId(), goldCertificateDto.getTransactionType(),
				goldCertificateDto.getPartnerCode(), goldCertificateDto.getMerchantCode(),
				new NameDomain(goldCertificateDto.getMerchantNameEn(), goldCertificateDto.getMerchantNameAr()),
				goldCertificateDto.getBalance(), goldCertificateDto.getBalance(), goldCertificateDto.getAedAmount(),
				goldCertificateDto.getPointsValue(), goldCertificateDto.getStartDate());

	}

	private static Integer getPointValue(Integer pointsValue, GoldCertificate existingCertificate, String action) {

		return Checks.checkIsActionInsert(action) ? pointsValue
				: pointsValue + existingCertificate.getTotalPointAmount();
	}

	private static Double getAedAmount(Double aedAmount, GoldCertificate existingCertificate, String action) {

		return Checks.checkIsActionInsert(action) ? aedAmount : aedAmount + existingCertificate.getTotalSpentAmount();
	}

	private static Double getTotalGoldBalance(Double balance, GoldCertificate existingCertificate, String action) {

		return Checks.checkIsActionInsert(action) ? balance : balance + existingCertificate.getTotalGoldBalance();
	}

	/***
	 * 
	 * @param giftRequestDto
	 * @param headers
	 * @return
	 */
	public static GiftDomain getGiftDomain(GiftConfigureRequestDto giftRequestDto, Headers headers) {
		
		return new GiftDomain.GiftBuilder(headers.getProgram(), giftRequestDto.getGiftType())
				.giftDetails(getGiftDetailsDomain(giftRequestDto))
				.offerValues(getOfferValuesDomain(giftRequestDto.getOfferValues()))
				.createdDate(new Date())
				.createdUser(headers.getUserName())
				.isActive(true)
				.updatedDate(new Date())
				.updatedUser(headers.getUserName())
				.build();
				
	}

	/**
	 * 
	 * @param offerValues
	 * @return
	 */
	private static List<OfferGiftValuesDomain> getOfferValuesDomain(List<OfferValueDto> offerValues) {
		
		if(!CollectionUtils.isEmpty(offerValues)) {
			
			List<OfferGiftValuesDomain> offerValuesDomainList = new ArrayList<>(offerValues.size());
			offerValues.forEach(o->
				offerValuesDomainList.add(new OfferGiftValuesDomain(o.getOfferId(),
						o.getOfferType(), 
						o.getSubOfferId(),
						o.getDenomination(), 
						o.getCouponQuantity()))
			);
			return offerValuesDomainList;
		}
		
		return null;
	}

	/**
	 * 
	 * @param giftRequestDto
	 * @return
	 */
	private static GiftValuesDomain getGiftDetailsDomain(GiftConfigureRequestDto giftRequestDto) {
		
		return new GiftValuesDomain(giftRequestDto.getMinPointValue(),
				giftRequestDto.getMaxPointValue(),
				giftRequestDto.getSubscriptionCatalogId(),
				giftRequestDto.getPromotionalGiftId(),
				giftRequestDto.getChannelId());
	}

	/***
	 * 
	 * @param offerValues
	 * @return
	 */
	public static List<OfferGiftValues> getOfferValuesList(List<OfferValueDto> offerValues) {
		
		if(!CollectionUtils.isEmpty(offerValues)) {
			
			List<OfferGiftValues> offerValuesList = new ArrayList<>(offerValues.size());
			offerValues.forEach(o->
			offerValuesList.add(new OfferGiftValues(o.getOfferId(),
					o.getOfferType(), 
					o.getSubOfferId(),
					o.getDenomination(), 
					o.getCouponQuantity()))
					);
			return offerValuesList;
		}
		
		return null;
	}
	
}
