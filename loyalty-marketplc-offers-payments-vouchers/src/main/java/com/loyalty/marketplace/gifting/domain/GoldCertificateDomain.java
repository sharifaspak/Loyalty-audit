package com.loyalty.marketplace.gifting.domain;

import java.util.Date;
import java.util.List;

import javax.validation.Validator;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.gifting.constants.GiftingCodes;
import com.loyalty.marketplace.gifting.constants.GiftingConfigurationConstants;
import com.loyalty.marketplace.gifting.constants.GiftingConstants;
import com.loyalty.marketplace.gifting.helper.GiftingControllerHelper;
import com.loyalty.marketplace.gifting.outbound.database.entity.GoldCertificate;
import com.loyalty.marketplace.gifting.outbound.database.repository.GoldCertificateRepository;
import com.loyalty.marketplace.gifting.outbound.dto.ListGoldTransactionResponse;
import com.loyalty.marketplace.gifting.outbound.dto.ListGoldTransactionResult;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class GoldCertificateDomain {

	private static final Logger LOG = LoggerFactory.getLogger(GoldCertificateDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;

	@Getter(AccessLevel.NONE)
	@Autowired
	ProgramManagement programManagement;

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Getter(AccessLevel.NONE)
	@Autowired
	FetchServiceValues fetchServiceValues;

	@Getter(AccessLevel.NONE)
	@Autowired
	GoldCertificateRepository goldCertificateRepository;
		
	@Autowired
	GiftingControllerHelper giftingControllerHelper;
	
	private String id;
	private String programCode;
	private String accountNumber;
	private String membershipCode;
	private Double totalGoldBalance;
	private Integer totalPointAmount;
	private Double totalSpentAmount;
	private List<GoldCertificateDetailsDomain> certificateDetails;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;

	public GoldCertificateDomain(GoldCertificateBuilder goldCertificate) {

		this.id = goldCertificate.id;
		this.programCode = goldCertificate.programCode;
		this.accountNumber = goldCertificate.accountNumber;
		this.membershipCode = goldCertificate.membershipCode;
		this.totalGoldBalance = goldCertificate.totalGoldBalance ;
		this.totalPointAmount = goldCertificate.totalPointAmount;
		this.totalSpentAmount = goldCertificate.totalSpentAmount;
		this.certificateDetails = goldCertificate.certificateDetails;
		this.createdUser = goldCertificate.createdUser;
		this.createdDate = goldCertificate.createdDate;
		this.updatedUser = goldCertificate.updatedUser;
		this.updatedDate = goldCertificate.updatedDate;

	}

	public static class GoldCertificateBuilder {

		private String id;
		private String programCode;
		private String accountNumber;
		private String membershipCode;
		private Double totalGoldBalance;
		private Integer totalPointAmount;
		private Double totalSpentAmount;
		private List<GoldCertificateDetailsDomain> certificateDetails;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;

		public GoldCertificateBuilder(String id) {

			this.id = id;
			
		}
		
		public GoldCertificateBuilder(String accountNumber,
				String membershipCode ) {

			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
		}

		public GoldCertificateBuilder id(String id) {
			this.id = id;
			return this;
		}

		public GoldCertificateBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public GoldCertificateBuilder totalGoldBalance(Double totalGoldBalance) {
			this.totalGoldBalance = totalGoldBalance;
			return this;
		}

		public GoldCertificateBuilder totalPointAmount(Integer totalPointAmount) {
			this.totalPointAmount = totalPointAmount;
			return this;
		}
		
		public GoldCertificateBuilder totalSpentAmount(Double totalSpentAmount) {
			this.totalSpentAmount = totalSpentAmount;
			return this;
		}

		public GoldCertificateBuilder certificateDetails(List<GoldCertificateDetailsDomain> certificateDetails) {
			this.certificateDetails = certificateDetails;
			return this;
		}

		public GoldCertificateBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public GoldCertificateBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public GoldCertificateBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public GoldCertificateBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public GoldCertificateDomain build() {
			return new GoldCertificateDomain(this);
		}

	}

	/**
	 * This method is used to save/update GoldTransaction.
	 * @param goldCertificateDomain
	 * @param action
	 * @param headers
	 * @param existingGoldCertificate
	 * @param api
	 * @return
	 * @throws MarketplaceException
	 */
	public GoldCertificate saveUpdateGoldCertificate(GoldCertificateDomain goldCertificateDomain, String action,
			Headers headers, GoldCertificate existingGoldCertificate, String api) throws MarketplaceException {

		try {
			
			LOG.info("action : {}", action);
            
			GoldCertificate goldCertificate = modelMapper.map(goldCertificateDomain, GoldCertificate.class);
			GoldCertificate savedGoldCertificate = goldCertificateRepository.save(goldCertificate);
			
			if (action.equalsIgnoreCase(OfferConstants.INSERT_ACTION.get())) {

//				auditService.insertDataAudit(OffersDBConstants.GOLD_CERTIFICATE, savedGoldCertificate,
//						api, headers.getExternalTransactionId(), headers.getUserName());

			} else if (action.equalsIgnoreCase(OfferConstants.UPDATE_ACTION.get())) {

				auditService.updateDataAudit(OffersDBConstants.GOLD_CERTIFICATE, savedGoldCertificate,
						api, existingGoldCertificate, headers.getExternalTransactionId(), headers.getUserName());
			}
			
			return savedGoldCertificate;

		} catch (MongoWriteException mongoException) {

			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.SAVE_GOLD_CERTIFICATE_REPOSITORY_METHOD.get(),
					mongoException.getClass() + mongoException.getMessage(), OfferErrorCodes.OFFER_RATING_FAILED);

		} catch (ValidationException validationException) {

			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.SAVE_GOLD_CERTIFICATE_REPOSITORY_METHOD.get(),
					validationException.getClass() + validationException.getMessage(),
					OfferExceptionCodes.VALIDATION_EXCEPTION);

		} catch (Exception e) {

			OfferExceptionCodes exception = ObjectUtils.isEmpty(goldCertificateDomain.getId())
					? OfferExceptionCodes.SAVE_GOLD_CERTIFICATE_DOMAIN_RUNTIME_EXCEPTION
					: OfferExceptionCodes.UPDATE_GOLD_CERTIFICATE_DOMAIN_RUNTIME_EXCEPTION;

			throw new MarketplaceException(this.getClass().toString(),
					OfferConstants.SAVE_GOLD_CERTIFICATE_REPOSITORY_METHOD.get(), e.getClass() + e.getMessage(), exception);

		}

	}

	/**
	 * This method is used to list gold transaction list.
	 * @param listGoldTransactionResponse
	 * @param accountNumber
	 * @param headers
	 * @return
	 */
	public ListGoldTransactionResponse listGoldTransactions(ListGoldTransactionResponse listGoldTransactionResponse, String accountNumber, Headers headers) {
		
		try {

			if(!giftingControllerHelper.validateListGoldTransactionRequestParameters(accountNumber, listGoldTransactionResponse)) return listGoldTransactionResponse;
			
			GetMemberResponse memberDetails = fetchServiceValues.getMemberDetails(accountNumber, listGoldTransactionResponse, headers);
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_MEMBER_DETAILS, this.getClass(),
					GiftingConfigurationConstants.GIFTING_CERTIFICATE_DOMAIN_SAVE_DB, memberDetails);
			
			if(null == memberDetails) {
				listGoldTransactionResponse.addErrorAPIResponse(
						GiftingCodes.LISTING_GOLD_TRANSACTION_MEMBER_DOES_NOT_EXIST.getIntId(),
						GiftingCodes.LISTING_GOLD_TRANSACTION_MEMBER_DOES_NOT_EXIST.getMsg()
								+ accountNumber);
				listGoldTransactionResponse.setResult(GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getId(),
						GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getMsg());
				return listGoldTransactionResponse;
			}
			
			GoldCertificate memberGoldTransaction = goldCertificateRepository.findByAccountNumberAndMembershipCode(accountNumber, memberDetails.getMembershipCode());
			LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.RETRIEVED_ENTITY, this.getClass(),
					GiftingConfigurationConstants.GIFTING_CERTIFICATE_DOMAIN_SAVE_DB, memberGoldTransaction);
			
			if (null != memberGoldTransaction) {

				ListGoldTransactionResult goldTransaction = modelMapper.map(memberGoldTransaction, ListGoldTransactionResult.class);
				LOG.info(GiftingConstants.CLASS_NAME + GiftingConstants.METHOD_NAME + GiftingConstants.MAPPED_ENTITY, this.getClass(),
						GiftingConfigurationConstants.GIFTING_CERTIFICATE_DOMAIN_SAVE_DB, goldTransaction);
				
				listGoldTransactionResponse.setGoldTransaction(goldTransaction);
				listGoldTransactionResponse.setResult(GiftingCodes.LIST_GOLD_TRANSACTION_SUCCESS.getId(),
						GiftingCodes.LIST_GOLD_TRANSACTION_SUCCESS.getMsg());

			} else {
				listGoldTransactionResponse.addErrorAPIResponse(GiftingCodes.NO_GOLD_TRANSACTION_FOR_MEMBER.getIntId(),
						GiftingCodes.NO_GOLD_TRANSACTION_FOR_MEMBER.getMsg() + accountNumber + GiftingConstants.COMMA_SEPARATOR + membershipCode);
				listGoldTransactionResponse.setResult(GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getId(),
						GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getMsg());
			}

		} catch (Exception e) {
			listGoldTransactionResponse.addErrorAPIResponse(GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					GiftingCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			listGoldTransactionResponse.setResult(GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getId(),
					GiftingCodes.LIST_GOLD_TRANSACTION_FAILURE.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), GiftingConfigurationConstants.GIFTING_CERTIFICATE_DOMAIN_SAVE_DB,
					e.getClass() + e.getMessage(), GiftingCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return listGoldTransactionResponse;
		
	}

}
