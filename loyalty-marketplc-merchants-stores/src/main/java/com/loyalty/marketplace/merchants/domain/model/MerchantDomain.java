package com.loyalty.marketplace.merchants.domain.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.constants.DBConstants;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.constants.MerchantConstants;
import com.loyalty.marketplace.merchants.constants.MerchantRequestMappingConstants;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Barcode;
import com.loyalty.marketplace.merchants.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.repository.BarcodeRepository;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.merchants.outbound.service.MerchantService;
import com.loyalty.marketplace.outbound.database.entity.Category;
import com.loyalty.marketplace.outbound.database.repository.CategoryRepository;
import com.loyalty.marketplace.outbound.dto.Errors;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;

@Component
public class MerchantDomain {

	private static final Logger LOG = LoggerFactory.getLogger(MerchantDomain.class);

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	MerchantContactPersonDomain contactPersonDomain;
	
	@Autowired
	MerchantBillingRateDomain billingRateDomain;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	BarcodeRepository barcodeRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	MerchantService merchantService;
	
	private String programCode;
	
	private String id;

	private String merchantCode;

	private MerchantNameDomain merchantName;

	private String partnerCode;

	private CategoryDomain category;

	private BarcodeDomain barcodeType;

	private String status;

	private WhatYouGetDomain whatYouGet;

	private TAndCDomain tnC;

	private MerchantDescriptionDomain merchantDescription;

	private String externalName;

	private String usrCreated;

	private String usrUpdated;

	private Date dtCreated;

	private Date dtUpdated;

	private List<MerchantContactPersonDomain> contactPersons;

	private List<MerchantBillingRateDomain> billingRates;
	
	private long offerCount;
	
	public static Logger getLog() {
		return LOG;
	}

	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public MerchantRepository getMerchantRepository() {
		return merchantRepository;
	}
	
	public String getId() {
		return id;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public MerchantNameDomain getMerchantName() {
		return merchantName;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public CategoryDomain getCategory() {
		return category;
	}

	public BarcodeDomain getBarcodeType() {
		return barcodeType;
	}

	public String getStatus() {
		return status;
	}

	public WhatYouGetDomain getWhatYouGet() {
		return whatYouGet;
	}

	public TAndCDomain getTnC() {
		return tnC;
	}

	public MerchantDescriptionDomain getMerchantDescription() {
		return merchantDescription;
	}

	public String getExternalName() {
		return externalName;
	}

	public String getUsrCreated() {
		return usrCreated;
	}

	public String getUsrUpdated() {
		return usrUpdated;
	}

	public Date getDtCreated() {
		return dtCreated;
	}

	public Date getDtUpdated() {
		return dtUpdated;
	}

	public List<MerchantContactPersonDomain> getContactPersons() {
		return contactPersons;
	}

	public List<MerchantBillingRateDomain> getBillingRates() {
		return billingRates;
	}
	
	public long getOfferCount() {
		return offerCount;
	}
	public MerchantDomain() {

	}

	public MerchantDomain(MerchantBuilder merchantBuilder) {
		super();
		this.programCode = merchantBuilder.programCode;
		this.merchantCode = merchantBuilder.merchantCode;
		this.merchantName = merchantBuilder.merchantName;
		this.partnerCode = merchantBuilder.partnerCode;
		this.category = merchantBuilder.category;
		this.barcodeType = merchantBuilder.barcodeType;
		this.status = merchantBuilder.status;
		this.whatYouGet = merchantBuilder.whatYouGet;
		this.tnC = merchantBuilder.tnC;
		this.merchantDescription = merchantBuilder.merchantDescription;
		this.externalName = merchantBuilder.externalName;
		this.dtCreated = merchantBuilder.dtCreated;
		this.dtUpdated = merchantBuilder.dtUpdated;
		this.usrCreated = merchantBuilder.usrCreated;
		this.usrUpdated = merchantBuilder.usrUpdated;
		this.contactPersons = merchantBuilder.contactPersons;
		this.billingRates = merchantBuilder.billingRates;
		this.id = merchantBuilder.id;
		this.offerCount = merchantBuilder.offerCount;

	}

	public static class MerchantBuilder {

		private String id;

		private String programCode;

		private String merchantCode;

		private MerchantNameDomain merchantName;

		private String partnerCode;

		private CategoryDomain category;

		private BarcodeDomain barcodeType;

		private String status;

		private WhatYouGetDomain whatYouGet;

		private TAndCDomain tnC;

		private MerchantDescriptionDomain merchantDescription;

		private String externalName;

		private String usrCreated;

		private String usrUpdated;

		private Date dtCreated;

		private Date dtUpdated;

		private List<MerchantContactPersonDomain> contactPersons;

		private List<MerchantBillingRateDomain> billingRates;
		
		private long offerCount;
		
		public MerchantBuilder() {
			
		}

		public MerchantBuilder(String merchantCode, MerchantNameDomain merchantName, String partnerCode,
				CategoryDomain category, BarcodeDomain barcodeType, List<MerchantContactPersonDomain> contactPersons,
				List<MerchantBillingRateDomain> billingRates, String status, long offerCount) {
			super();
			this.merchantCode = merchantCode;
			this.merchantName = merchantName;
			this.partnerCode = partnerCode;
			this.category = category;
			this.barcodeType = barcodeType;
			this.contactPersons = contactPersons;
			this.billingRates = billingRates;
			this.status = status;
			this.offerCount = offerCount;
		}

		public MerchantBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public MerchantBuilder id(String id) {
			this.id = id;
			return this;
		}

		public MerchantBuilder status(String status) {
			this.status = status;
			return this;
		}

		public MerchantBuilder whatYouGet(WhatYouGetDomain whatYouGet) {
			this.whatYouGet = whatYouGet;
			return this;
		}

		public MerchantBuilder tnC(TAndCDomain tnC) {
			this.tnC = tnC;
			return this;
		}

		public MerchantBuilder merchantDescription(MerchantDescriptionDomain merchantDescription) {
			this.merchantDescription = merchantDescription;
			return this;
		}

		public MerchantBuilder externalName(String externalName) {
			this.externalName = externalName;
			return this;
		}

		public MerchantBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public MerchantBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public MerchantBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public MerchantBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}

		public MerchantDomain build() {
			return new MerchantDomain(this);
		}

	}
	
	private void sendCredentialsToLdap(MerchantDomain merchant) throws MarketplaceException {
		try {
			for (MerchantContactPersonDomain contactPerson : merchant.getContactPersons()) {
				Utils.sendCredentailsToLdap(contactPerson.getUserName(), contactPerson.getPassword(),
						contactPerson.getEmailId());
			} 
		} catch (Exception e) {
			LOG.error("Exception occured while sending credentials to LDAP.");
			throw new MarketplaceException(this.getClass().toString(), "sendCredentialsToLdap",
					e.getClass() + e.getMessage(),
					MerchantCodes.CREDENTIALS_SAVE_TO_LDAP_FAILED);
		}
	}

	public void sendMailToContactPersons(MerchantDomain merchant) throws MarketplaceException {
		try {
			for (MerchantContactPersonDomain contactPerson : merchant.getContactPersons()) {
				Utils.sendMail(contactPerson.getUserName(), contactPerson.getPassword(), contactPerson.getEmailId());
			} 
		} catch (Exception e) {
			LOG.error("Exception occured while mailing credentials to contact person.");
			throw new MarketplaceException(this.getClass().toString(), "sendMailToContactPersons",
					e.getClass() + e.getMessage(),
					MerchantCodes.CREDENTIALS_MAIL_TO_CONTACT_FAILED);
		}
	}

	public List<MerchantContactPersonDomain> createContactPersonDomain(List<ContactPersonDto> contactPersons,
			List<ContactPersonDto> invalidContactPersons,boolean optInorOut, String merchantCode) {
		LOG.info("Inside createContactPersonDomain :optInorOut :: {}" , optInorOut);
		return contactPersonDomain.createContactPersons(contactPersons, invalidContactPersons,optInorOut, merchantCode);
		
	}
	
	public List<MerchantBillingRateDomain> createBillingRateDomain(List<MerchantBillingRateDto> billingRate, List<MerchantBillingRateDto> invalidBillingRateDtoList) throws ParseException {
		return billingRateDomain.createBillingRates(billingRate , invalidBillingRateDtoList);
	}
	
	public void saveMerchant(MerchantDomain merchantDomain,String transactionId) throws MarketplaceException {
		
		LOG.info("Domain Object To Be Persisted: {}", merchantDomain);

		try {
			
			Merchant merchant = modelMapper.map(merchantDomain, Merchant.class);
			
			if (MerchantConstants.MERCHANT_ACTIVE_STATUS.get().equalsIgnoreCase(merchant.getStatus())) {
				merchant.setStatus(MerchantConstants.MERCHANT_ACTIVE_STATUS.get());
			} else if(MerchantConstants.MERCHANT_DEFAULT_STATUS.get().equalsIgnoreCase(merchant.getStatus())) {
				merchant.setStatus(MerchantConstants.MERCHANT_DEFAULT_STATUS.get());
			}
			merchant.setOfferCount(0);
			this.merchantRepository.insert(merchant);
//			auditService.insertDataAudit(DBConstants.MERCHANT, merchant,MerchantRequestMappingConstants.CREATE_MERCHANT,transactionId,merchantDomain.getUsrCreated());
			sendCredentialsToLdap(merchantDomain);
			sendMailToContactPersons(merchantDomain);
		
			LOG.info("Persisted Object: {}", merchant);
			
		} catch (MongoWriteException mongoException) {
			
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_MERCHANT.getConstant(),
					mongoException.getClass() + mongoException.getMessage(),
					MerchantCodes.MERCHANT_CREATION_FAILED);
		
		} catch (ValidationException validationException) {
		
			LOG.error("Exception occured while mapping merchant domain to merchant entity.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_MERCHANT.getConstant(),
					validationException.getClass() + validationException.getMessage(),
					MerchantCodes.MERCHANT_CREATION_FAILED);
		
		} catch (MarketplaceException e) {
		
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_MERCHANT.getConstant(),
					e.getClass() + e.getMessage(),
					MerchantCodes.MERCHANT_CREATION_FAILED);
		
		} catch (Exception e) {
		
			LOG.error("Exception occured while saving merchant to database.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_MERCHANT.getConstant(),
					e.getClass() + e.getMessage(), MerchantCodes.GENERIC_RUNTIME_EXCEPTION);
		
		}
		
	}

	public void configureContactPerson(Merchant merchant, List<MerchantContactPersonDomain> contactPersonDomain,
			String userName , String program, String configureConatactPerson,String transactionId) {

		List<ContactPerson> contactPersonList = new ArrayList<>();
		 Gson gson = new Gson();
		 Merchant oldMerchant  = gson.fromJson(gson.toJson(merchant), Merchant.class);
		LOG.info("Existing merchant object : {}", oldMerchant);
		for (final MerchantContactPersonDomain contactDomain : contactPersonDomain) {
			contactPersonList.add(modelMapper.map(contactDomain, ContactPerson.class));
		}

		merchant.getContactPersons().addAll(contactPersonList);
		merchant.setDtUpdated(new Date());
		merchant.setProgramCode(program);
		merchant.setUsrUpdated(userName);

		this.merchantRepository.save(merchant);
		LOG.info("After updating the merchant : {}", merchant);
		auditService.updateDataAudit(DBConstants.MERCHANT, merchant, configureConatactPerson,oldMerchant,transactionId,userName);

		for (MerchantContactPersonDomain contactPerson : contactPersonDomain) {
			Utils.sendCredentailsToLdap(contactPerson.getUserName(), contactPerson.getPassword(),
					contactPerson.getEmailId());
			Utils.sendMail(contactPerson.getUserName(), contactPerson.getPassword(), contactPerson.getEmailId());
		}

	}
	
	public Merchant updateMerchantStatus(Merchant merchant, String status, String userName, String program,
			String activateDeactivateMerchant, String transactionId) throws MarketplaceException {
		try {
			
			 Gson gson = new Gson();
			 Merchant oldMerchant  = gson.fromJson(gson.toJson(merchant), Merchant.class);
			LOG.info("Existing merchant odject : {}", oldMerchant);
			if (MerchantConstants.MERCHANT_ACTIVE_STATUS.get().equalsIgnoreCase(status)) {
				merchant.setStatus(MerchantConstants.MERCHANT_ACTIVE_STATUS.get());
			} else if(MerchantConstants.MERCHANT_DEFAULT_STATUS.get().equalsIgnoreCase(status)) {
				merchant.setStatus(MerchantConstants.MERCHANT_DEFAULT_STATUS.get());
			}
			merchant.setProgramCode(null != merchant.getProgramCode() ? merchant.getProgramCode() : program);
			merchant.setDtUpdated(new Date());
			merchant.setUsrUpdated(userName);
			Merchant saveMerchant = this.merchantRepository.save(merchant);
			LOG.info("After updating the status : {}", saveMerchant);
			
			for (ContactPerson contact : saveMerchant.getContactPersons()) {
				merchantService.sendEmailMerchantStatusUpdate(contact.getFirstName(), saveMerchant.getMerchantCode(),
						saveMerchant.getStatus(), contact.getEmailId(), transactionId);
			}
			
			auditService.updateDataAudit(DBConstants.MERCHANT, saveMerchant, activateDeactivateMerchant,oldMerchant,transactionId,userName);
			return saveMerchant;

		} catch (MongoException mongoException) {
		
			LOG.info("Exception occured while updating merchant status.");
			throw new MarketplaceException(this.getClass().toString(), "updateMerchantStatus",
					mongoException.getClass() + mongoException.getMessage(), MerchantCodes.MERCHANT_STATUS_UPDATION_FAILED);
		
		} 
	}
	
	public void updateMerchant(MerchantDomain merchantDomain, Merchant merchantDetails,String updateMerchant,String transactionId) throws MarketplaceException {
		try {
			Merchant merchant = modelMapper.map(merchantDomain, Merchant.class);
			if (MerchantConstants.MERCHANT_ACTIVE_STATUS.get().equalsIgnoreCase(merchant.getStatus())) {
				merchant.setStatus(MerchantConstants.MERCHANT_ACTIVE_STATUS.get());
			} else if(MerchantConstants.MERCHANT_DEFAULT_STATUS.get().equalsIgnoreCase(merchant.getStatus())) {
				merchant.setStatus(MerchantConstants.MERCHANT_DEFAULT_STATUS.get());
			}
			this.merchantRepository.save(merchant);
			auditService.updateDataAudit(DBConstants.MERCHANT, merchant, updateMerchant,merchantDetails,transactionId,merchantDomain.getUsrCreated());

		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(), "updateMerchant",
					e.getClass() + e.getMessage(), MerchantCodes.MERCHANT_STATUS_UPDATION_FAILED);
		}
	}

	public boolean validateBillingRateType(List<MerchantBillingRateDto> discountBillingRates,
			ResultResponse resultResponse) throws MarketplaceException {
		List<Errors> errorList = new ArrayList<>();
		try {
			for (MerchantBillingRateDto discountBillingRate : discountBillingRates) {
				if (null != discountBillingRate.getRateType()) {
					RateType ratetype = billingRateDomain.getTypeByRateType(discountBillingRate.getRateType());
					if (null == ratetype) {
						Errors error = new Errors();
						error.setCode(MerchantCodes.INVALID_RATE_TYPE.getIntId());
						error.setMessage(
								discountBillingRate.getRateType() + ":" + MerchantCodes.INVALID_RATE_TYPE.getMsg());
						errorList.add(error);
					}
				}
			}
		} catch (MarketplaceException e) {
			throw new MarketplaceException(this.getClass().toString(), "validateBillingRateType",
					e.getClass() + e.getMessage(), MarketPlaceCode.TYPE_UNAVAILABLE_FOR_BILLING_RATE);
		}
		resultResponse.setBulkErrorAPIResponse(errorList);
		if (resultResponse.getApiStatus().getErrors() == null || resultResponse.getApiStatus().getErrors().isEmpty()) {
			return true;
		}
		return false;
		
	}

	public boolean checkEmailExists(String merchantCode, String emailId, ResultResponse resultResponse, String username) {
		Optional<Merchant> merchnat = merchantRepository.findByMerchantCodeAndEmailIdAndUserName(merchantCode, emailId,username);
		if (merchnat.isPresent()) {
			
			resultResponse.addErrorAPIResponse(MerchantCodes.EMAILID_EXITS.getIntId(),emailId +" : " + MerchantCodes.EMAILID_EXITS.getMsg() );
			return false;
			
		} else {
			return true;
		}

	}

	public boolean validateCategoryBarcode(String category, String barcodeType, ResultResponse resultResponse) {
		Optional<Category> categry = categoryRepository.findById(category);
		Optional<Barcode> barcde = barcodeRepository.findById(barcodeType);
		 if(!categry.isPresent()) {
			 resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_CATEGORY.getIntId(), MerchantCodes.INVALID_CATEGORY.getMsg());
		 }
		 if(!barcde.isPresent()) {
			 resultResponse.addErrorAPIResponse(MerchantCodes.INVALID_BARCODE.getIntId(), MerchantCodes.INVALID_BARCODE.getMsg());
		 }
		 resultResponse.setResult(MerchantCodes.MERCHANT_CREATION_FAILED.getId(), MerchantCodes.MERCHANT_CREATION_FAILED.getMsg());

	  if (resultResponse.getApiStatus().getErrors() != null && 
			resultResponse.getApiStatus().getErrors().isEmpty()) {
		return true;
	}
	return false;
	}
	
}
