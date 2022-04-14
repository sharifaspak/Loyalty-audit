package com.loyalty.marketplace.offers.promocode.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.ApiStatus;
import com.loyalty.marketplace.offers.decision.manager.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.member.management.outbound.dto.GetMemberResponse;
import com.loyalty.marketplace.offers.outbound.database.entity.CustomerType;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.entity.PurchaseHistory;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.database.repository.PurchaseRepository;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeConstants;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeErrorCodes;
import com.loyalty.marketplace.offers.promocode.constants.codes.PromoCodeSuccessCodes;
import com.loyalty.marketplace.offers.promocode.inbound.dto.DecisionManagerResponseDto;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PartnerPromocodeRequest;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeDetails;
import com.loyalty.marketplace.offers.promocode.inbound.dto.PromoCodeRequest;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PartnerPromoCode;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoLevels;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoRules;
import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoTypes;
import com.loyalty.marketplace.offers.promocode.outbound.database.repository.PartnerPromoCodeRepository;
import com.loyalty.marketplace.offers.promocode.outbound.database.repository.PromoCodeRepository;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PartnerPromoCodeDetails;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoCodeDMRequest;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoCodeResult;
import com.loyalty.marketplace.offers.promocode.outbound.dto.PromoDetails;
import com.loyalty.marketplace.offers.promocode.outbound.dto.ValidPromoCodeDetails;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApply;
import com.loyalty.marketplace.payment.inbound.dto.PromoCodeApplyAndValidate;
import com.loyalty.marketplace.payment.utils.Utils;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherController;
import com.loyalty.marketplace.voucher.inbound.dto.BurnVoucherRequest;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponse;
import com.loyalty.marketplace.voucher.member.management.outbound.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;
import com.loyalty.marketplace.voucher.outbound.service.VoucherMemberManagementService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class PromoCodeDomain {

	private static final Logger LOG = LoggerFactory.getLogger(PromoCodeDomain.class);

	@Getter(AccessLevel.NONE)
	@Autowired
	Validator validator;

	@Autowired
	JmsTemplate jmsTemplate;

	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;

	@Getter(AccessLevel.NONE)
	@Autowired
	PromoCodeRepository promoCodeRepository;

	@Getter(AccessLevel.NONE)
	@Autowired
	OfferRepository offerRepository;

	@Getter(AccessLevel.NONE)
	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	PartnerPromoCodeRepository partnerPromoCodeRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	FetchServiceValues fetchServiceValues;

	@Autowired
	private VoucherMemberManagementService voucherMemberManagementService;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	VoucherController voucherController;
	
	@Autowired
	ExceptionLogsService exceptionLogsService;

	@Value("${decisionManager.uri}")
	private String decisionManagerUri;

	@Value("${decisionManager.promoCode.rule}")
	private String eligibilityCheck;

	private String id;
	private String program;
	private String promoCode;
	private String description;
	private List<String> offerIds;
	private int value;
	private PromoLevels promoLevel;
	private PromoTypes promoType;
	private PromoRules ruleId;
	private Date startDate;
	private Date endDate;
	private int totalCount;
	private int redeemedCount;
	private int duration;
	private List<Integer> denominationAmount;
	private String offerType;
	private List<String> subcscriptions;
	private int userRedeemedAccountLimit;
	private List<CustomerType> customerType;
	private List<String> accountNumber;
	private String partner;
	private String partnerRef;
	private Date dtCreated;
	private String usrCreated;
	private Date dtUpdated;
	private String usrUpdated;

	public PromoCodeDomain(PromoCodeBuilder promoCode) {
		this.id = promoCode.id;
		this.program = promoCode.program;
		this.promoCode = promoCode.promoCode;
		this.accountNumber = promoCode.accountNumber;
		this.partner = promoCode.partner;
		this.partnerRef = promoCode.partnerRef;
		this.customerType = promoCode.customerType;
		this.denominationAmount = promoCode.denominationAmount;
		this.description = promoCode.description;
		this.duration = promoCode.duration;
		this.offerIds = promoCode.offerIds;
		this.offerType = promoCode.offerType;
		this.promoLevel = promoCode.promoLevel;
		this.promoType = promoCode.promoType;
		this.redeemedCount = promoCode.redeemedCount;
		this.ruleId = promoCode.ruleId;
		this.startDate = promoCode.startDate;
		this.endDate = promoCode.endDate;
		this.subcscriptions = promoCode.subcscriptions;
		this.totalCount = promoCode.totalCount;
		this.userRedeemedAccountLimit = promoCode.userRedeemedAccountLimit;
		this.value = promoCode.value;
		this.dtCreated = promoCode.dtCreated;
		this.dtUpdated = promoCode.dtUpdated;
		this.usrCreated = promoCode.usrCreated;
		this.usrUpdated = promoCode.usrUpdated;
	}

	public static class PromoCodeBuilder {
		private String id;
		private String program;
		private String promoCode;
		private String description;
		private List<String> offerIds;
		private int value;
		private PromoLevels promoLevel;
		private PromoTypes promoType;
		private PromoRules ruleId;
		private Date startDate;
		private Date endDate;
		private int totalCount;
		private int redeemedCount;
		private int duration;
		private List<Integer> denominationAmount;
		private String offerType;
		private List<String> subcscriptions;
		private int userRedeemedAccountLimit;
		private List<CustomerType> customerType;
		private List<String> accountNumber;
		private String partner;
		private String partnerRef;
		private Date dtCreated;
		private String usrCreated;
		private Date dtUpdated;
		private String usrUpdated;

		public PromoCodeBuilder(String program, String promoCode, String description, List<String> offerIds, int value,
				PromoLevels promoLevel, PromoTypes promoType, PromoRules ruleId, Date startDate, Date endDate,
				int totalCount, int redeemedCount, int duration, List<Integer> denominationAmount, String offerType,
				List<String> subcscriptions, int userRedeemedAccountLimit, List<CustomerType> customerType,
				List<String> accountNumber, String partner, String partnerRef) {
			this.program = program;
			this.promoCode = promoCode;
			this.description = description;
			this.offerIds = offerIds;
			this.value = value;
			this.promoLevel = promoLevel;
			this.promoType = promoType;
			this.ruleId = ruleId;
			this.startDate = startDate;
			this.endDate = endDate;
			this.totalCount = totalCount;
			this.redeemedCount = redeemedCount;
			this.duration = duration;
			this.denominationAmount = denominationAmount;
			this.offerType = offerType;
			this.subcscriptions = subcscriptions;
			this.userRedeemedAccountLimit = userRedeemedAccountLimit;
			this.customerType = customerType;
			this.accountNumber = accountNumber;
			this.partner = partner;
			this.partnerRef = partnerRef;
		}
		public PromoCodeBuilder id(String id) {
			this.id = id;
			return this;
		}

		public PromoCodeBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}

		public PromoCodeBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public PromoCodeBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public PromoCodeBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}

		public PromoCodeDomain build() {
			return new PromoCodeDomain(this);
		}
	}

	/**
	 * @param promoCodeRequest
	 * @param resultResponse
	 * @param program
	 * @param user
	 * @return
	 */
	public ResultResponse createPromoCode(PromoCodeRequest promoCodeRequest, ResultResponse resultResponse,
			String program, String user) {
		LOG.info("Inside CreatePromoCode Method");
		try {
			PromoCodeDomain promoCodeDomain = null;
			List<String> offerDomainList = new ArrayList<String>();
			List<String> subscriptionCatalogList = new ArrayList<String>();
			OfferCatalog offer = null;
			List<PromoCode> promoCodeEntity = promoCodeRepository.findByPromoCode(promoCodeRequest.getPromoCodeDetails().getPromoCode());
			if(!promoCodeEntity.isEmpty()){
				resultResponse.setErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_EXIST.getIntId(),
						PromoCodeErrorCodes.PROMOCODE_EXIST.getMsg());
				return resultResponse;
			}
			if (null != promoCodeRequest && promoCodeRequest.getOfferId() != null) {
				offer = offerRepository.findByOfferId(promoCodeRequest.getOfferId());
			}
			if (null != promoCodeRequest.getOfferId() && offer == null) {
				resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.OFFER_NOT_PRESENT.getIntId(),
						OfferErrorCodes.OFFER_NOT_PRESENT.getMsg());
				resultResponse.setResult(PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getId(),
						PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getMsg());
				return resultResponse;
			} else {
				PromoCodeDetails promoCodeDetails = promoCodeRequest.getPromoCodeDetails();
				if (null != promoCodeRequest.getOfferId()) {
					offerDomainList.add(promoCodeRequest.getOfferId());
				}
				if (null != promoCodeRequest.getSubscriptionCatalogId()) {
					subscriptionCatalogList.add(promoCodeRequest.getSubscriptionCatalogId());
				}
				promoLevel = new PromoLevels();
				if (promoCodeDetails.getPromoCodeLevel() != null) {
					if (promoCodeDetails.getPromoCodeLevel().equalsIgnoreCase(PromoCodeConstants.INSTANCE.get())) {
						promoLevel.setLevelId(30);
						promoLevel.setDescription(PromoCodeConstants.INSTANCE_PROMOTION.get());
					}
				} else {
					promoLevel.setLevelId(31);
					promoLevel.setDescription(PromoCodeConstants.UMBRELLA_PROMOTION.get());
				}
				promoType = new PromoTypes();
				promoType.setType(promoCodeDetails.getPromoCodeType());
				ruleId = new PromoRules();
				ruleId.setRuleId(promoCodeRequest.getRuleId());
				ruleId.setIsWelcomeGift("N");
				if(promoCodeRequest.getPromoCodeDetails().isWelcomeGift()) {
					ruleId.setIsWelcomeGift("Y");
				}
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				denominationAmount = new ArrayList<Integer>();
				if (promoCodeDetails.getPromoDenominationAmount() != null
						&& !promoCodeDetails.getPromoDenominationAmount().isEmpty()) {
					for (Integer denomination : promoCodeDetails.getPromoDenominationAmount()) {
						denominationAmount.add(denomination);
					}
				}
				Date startDate = null;
				Date endDate = null;
				if ( null != promoCodeDetails.getStartDate() ) {
					startDate = df.parse(promoCodeDetails.getStartDate()) ;
					endDate = df.parse(promoCodeDetails.getEndDate());
				}
				
				customerType = new ArrayList<CustomerType>();
				CustomerType customerTypeNew = new CustomerType();
				customerTypeNew.setEligibleCustomerTypes(promoCodeRequest.getCustomerType());
				customerType.add(customerTypeNew);
				promoCodeDomain = new PromoCodeDomain.PromoCodeBuilder(program, promoCodeDetails.getPromoCode(),
						promoCodeDetails.getPromoCodeDescription(), offerDomainList, promoCodeDetails.getValue(),
						promoLevel, promoType, ruleId, startDate, endDate, promoCodeDetails.getPromoCodeTotalCount(),
						promoCodeDetails.getPromoUserRedeemCountLimit(), promoCodeDetails.getPromoCodeDuration(),
						denominationAmount, promoCodeRequest.getOfferType(), subscriptionCatalogList,
						promoCodeRequest.getPromoCodeDetails().getPromoUserRedeemCountLimit(), customerType,
						promoCodeRequest.getAccountNumber(), promoCodeRequest.getPartner(), promoCodeRequest.getPartnerRef()).dtCreated(new Date())
								.dtUpdated(new Date()).usrCreated(user).usrUpdated(user).build();
				PromoCode promoCodeToSave = modelMapper.map(promoCodeDomain, PromoCode.class);
				LOG.info("Promo Code to save : " + promoCodeToSave.toString());
				promoCodeRepository.save(promoCodeToSave);
				resultResponse.setResult(PromoCodeSuccessCodes.PROMOCODE_CREATED_SUCCESSFULLY.getId(),
						PromoCodeSuccessCodes.PROMOCODE_CREATED_SUCCESSFULLY.getMsg());
				LOG.info("PromoCode created successfully, Exiting CreatePromoCode Method.");
				return resultResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					PromoCodeErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getId(),
					PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getMsg());
			return resultResponse;
		}
	}

	public ResultResponse createPromoCodeInBulk(MultipartFile file, ResultResponse resultResponse, String program,
			String user, Headers header) throws IOException {

		LOG.info("Inside CreatePromoCodeInBulk Method");
		List<String> uniquePromoCode = new ArrayList<String>();
		List<PromoCodeRequest> listpromoCodeRequest = new ArrayList<PromoCodeRequest>();
		Multimap<String, String> promoandaccno = ArrayListMultimap.create();
		BufferedReader br;
		List<String> fileContent = new ArrayList<>();
		String line;
		InputStream is = file.getInputStream();
		br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			if (!StringUtils.isEmpty(line)) {
				fileContent.add(line);
			}
		}

		String[] data = fileContent.get(1).split(",");
		if (data[5].isEmpty() || data[5].equals(" ") || null == data[5]) {
			LOG.info("In Scenario1:Promo code not provided...generate unique code for each account ");
			for (int i = 1; i < fileContent.size(); i++) {

				data = fileContent.get(i).split(",");

				PromoCodeRequest promoCodeRequest = EachRecordInfo(data);
				promoCodeRequest.getPromoCodeDetails().setPromoCode("PromoCode" + Math.abs(new Random().nextInt()));
				LOG.info("Generated Promo code : " + promoCodeRequest.getPromoCodeDetails().getPromoCode());

				LOG.info("promoCodeRequest : " + promoCodeRequest.toString());
				listpromoCodeRequest.add(promoCodeRequest);
				resultResponse = createPromoCode(promoCodeRequest, resultResponse, program, user);

			}
			LOG.info("listpromoCodeRequest" + listpromoCodeRequest);
			sendSMS(listpromoCodeRequest, header);
		} else {

			promoandaccno = getListOfAccnoWithPromocode(fileContent);
			List<String> promocodes = new ArrayList<String>();
			promocodes.addAll(promoandaccno.keySet());

			if (promocodes.size() != (fileContent.size() - 1)) {

				LOG.info("In Scenario3: common promo code provided for multiple/all records ");
				for (int i = 1; i < fileContent.size(); i++) {
					data = fileContent.get(i).split(",");
					List<String> accountno = new ArrayList<String>();

					PromoCodeRequest promoCodeRequest = EachRecordInfo(data);
					LOG.info("Provided Promo code : " + promoCodeRequest.getPromoCodeDetails().getPromoCode());
					listpromoCodeRequest.add(promoCodeRequest);
					String promoCode = promoCodeRequest.getPromoCodeDetails().getPromoCode();
					if (!uniquePromoCode.contains(promoCode)) {
						uniquePromoCode.add(promoCode);
						accountno = (List<String>) promoandaccno.get(promoCode);
						promoCodeRequest.setAccountNumber(accountno);
						LOG.info("promoCodeRequest : " + promoCodeRequest.toString());
						resultResponse = createPromoCode(promoCodeRequest, resultResponse, program, user);

					}

				}
				LOG.info("listpromoCodeRequest" + listpromoCodeRequest);
				sendSMS(listpromoCodeRequest, header);
			}
			if (promocodes.size() == (fileContent.size() - 1)) {
				LOG.info("In Scenario2: Unique Promocode provided for each record ");
				for (int i = 1; i < fileContent.size(); i++) {
					data = fileContent.get(i).split(",");
					PromoCodeRequest promoCodeRequest = EachRecordInfo(data);
					LOG.info("Provided Promo code : " + promoCodeRequest.getPromoCodeDetails().getPromoCode());
					LOG.info("promoCodeRequest : " + promoCodeRequest.toString());
					listpromoCodeRequest.add(promoCodeRequest);
					resultResponse = createPromoCode(promoCodeRequest, resultResponse, program, user);

				}
				LOG.info("listpromoCodeRequest" + listpromoCodeRequest);
				sendSMS(listpromoCodeRequest, header);

			}
		}
		return resultResponse;
	}
	
	public PartnerPromoCodeDetails createPartnerPromoCodeInBulk(PartnerPromocodeRequest partnerPromocodeReq, ResultResponse resultResponse, String program,
			String user, Headers header) throws IOException {
		PartnerPromoCodeDetails partnerPromoCodeDetails = new PartnerPromoCodeDetails(
				header.getExternalTransactionId());
		LOG.info("Inside PartnerCreatePromoCodeInBulk Method");
		List<String> uniquePromoCode = new ArrayList<String>();
		List<PromoCode> promoCodeList = promoCodeRepository.findByPartnerAndPartnerRef(partnerPromocodeReq.getPartnerCode(),
				partnerPromocodeReq.getPartnerRefNo());
		if (promoCodeList != null && !promoCodeList.isEmpty()) {
			for (PromoCode promo : promoCodeList) {
				uniquePromoCode.add(promo.getPromoCode());
			}
			partnerPromoCodeDetails.setPromoDetails(uniquePromoCode);
			return partnerPromoCodeDetails;
		} else {
			List<PromoCodeRequest> listpromoCodeRequest = new ArrayList<PromoCodeRequest>();
			for (int i = 1; i <= partnerPromocodeReq.getPromoCodeCount(); i++) {
				PromoCodeRequest promoCodeRequest = EachPartnerRecordInfo(partnerPromocodeReq);
				promoCodeRequest.getPromoCodeDetails().setPromoCode(partnerPromocodeReq.getPartnerCode() + "_"
						+ partnerPromocodeReq.getPartnerRefNo() + "_" + Math.abs(new Random().nextInt()));
				LOG.info("Generated Promo code : " + promoCodeRequest.getPromoCodeDetails().getPromoCode());

				LOG.info("promoCodeRequest : " + promoCodeRequest.toString());
				listpromoCodeRequest.add(promoCodeRequest);
				resultResponse = createPromoCode(promoCodeRequest, resultResponse, program, user);
				if (resultResponse.getApiStatus().getStatusCode() == 0) {
					uniquePromoCode.add(promoCodeRequest.getPromoCodeDetails().getPromoCode());
				} else {
					partnerPromoCodeDetails.addErrorAPIResponse(
							PromoCodeErrorCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
							PromoCodeErrorCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
					partnerPromoCodeDetails.setResult(PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getId(),
							PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getMsg());
					return partnerPromoCodeDetails;
				}
			}
			LOG.info("listpromoCodeRequest" + listpromoCodeRequest);
			partnerPromoCodeDetails.setPromoDetails(uniquePromoCode);
			savePartnerPromoCodeDetails(partnerPromocodeReq, user);
			return partnerPromoCodeDetails;
		}
	}

	private void savePartnerPromoCodeDetails(PartnerPromocodeRequest partnerPromocodeReq, String user) {
		PartnerPromoCode partnerPromoCode = new PartnerPromoCode();
		partnerPromoCode.setPartner(partnerPromocodeReq.getPartnerCode());
		partnerPromoCode.setPartnerRef(partnerPromocodeReq.getPartnerRefNo());
		partnerPromoCode.setSubscriptionCatalogId(partnerPromocodeReq.getSubscriptionCatalogId());
		partnerPromoCode.setPromoCodeCount(partnerPromocodeReq.getPromoCodeCount());
		partnerPromoCode.setDtCreated(new Date());
		partnerPromoCode.setDtUpdated(new Date());
		partnerPromoCode.setUsrCreated(user);
		partnerPromoCode.setUsrUpdated(user);
		partnerPromoCodeRepository.save(partnerPromoCode);
	}
	public PromoCodeRequest EachRecordInfo(String[] data) {
		PromoCodeRequest promoCodeRequest = new PromoCodeRequest();
		PromoCodeDetails promoCodeDetails = new PromoCodeDetails();
		List<String> accountno = new ArrayList<String>();
		List<Integer> promoDenominationAmount = new ArrayList<Integer>();
		promoCodeRequest.setOfferId(data[0]);
		promoCodeRequest.setRuleId(data[1]);
		accountno.add(data[2]);
		promoCodeRequest.setAccountNumber(accountno);
		promoCodeRequest.setOfferType(data[3]);
		promoCodeRequest.setSubscriptionCatalogId(data[4]);
		promoCodeRequest.setCustomerType(new ArrayList<String>());
		promoCodeDetails.setPromoCode(data[5]);
		promoCodeDetails.setPromoCodeType(data[6]);
		if (data[7].isEmpty() || data[7].equals(" ") || null == data[7]) {
			promoCodeDetails.setValue(0);
		} else {
			promoCodeDetails.setValue(Integer.parseInt(data[7]));
		}
		promoCodeDetails.setStartDate(data[8]);
		promoCodeDetails.setEndDate(data[9]);
		promoCodeDetails.setPromoCodeDuration(Integer.parseInt(data[10]));
		if (data[11].isEmpty() || data[11].equals(" ") || null == data[11]) {
			promoCodeDetails.setPromoCodeTotalCount(0);
		} else {
			promoCodeDetails.setPromoCodeTotalCount(Integer.parseInt(data[11]));
		}
		if (data[12].isEmpty() || data[12].equals(" ") || null == data[12]) {
			promoDenominationAmount.add(0);
		} else {
			promoDenominationAmount.add(Integer.parseInt(data[12]));
		}
		promoCodeDetails.setPromoDenominationAmount(promoDenominationAmount);

		promoCodeDetails.setPromoUserRedeemCountLimit(Integer.parseInt(data[13]));
		promoCodeRequest.setPartner(data[14]);

		promoCodeDetails.setPromoCodeDescription("Instance Promocode");
		promoCodeDetails.setPromoCodeLevel("Instance");

		promoCodeRequest.setPromoCodeDetails(promoCodeDetails);

		return promoCodeRequest;
	}
	
	public PromoCodeRequest EachPartnerRecordInfo(PartnerPromocodeRequest request) {
		String startDateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	
		PromoCodeRequest promoCodeRequest = new PromoCodeRequest();
		PromoCodeDetails promoCodeDetails = new PromoCodeDetails();
		List<Integer> promoDenominationAmount = new ArrayList<Integer>();
		promoCodeRequest.setRuleId("4");
		promoCodeRequest.setSubscriptionCatalogId(request.getSubscriptionCatalogId());
		promoCodeRequest.setCustomerType(new ArrayList<String>());
		promoCodeDetails.setPromoCode("");
		promoCodeDetails.setPromoCodeType("Free");
		promoCodeDetails.setValue(0);
		promoCodeDetails.setStartDate(startDateStr);
		promoCodeDetails.setEndDate(nextYear());
		promoCodeDetails.setPromoCodeDuration(365);
		promoCodeDetails.setPromoCodeTotalCount(1);
		promoCodeDetails.setPromoDenominationAmount(promoDenominationAmount);

		promoCodeDetails.setPromoUserRedeemCountLimit(1);
		promoCodeRequest.setPartner(request.getPartnerCode());
		promoCodeRequest.setPartnerRef(request.getPartnerRefNo());
		promoCodeDetails.setPromoCodeDescription("Instance Promocode");
		promoCodeDetails.setPromoCodeLevel("Instance");
		promoCodeRequest.setPromoCodeDetails(promoCodeDetails);
		return promoCodeRequest;
	}

	private String nextYear() {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date nextYear = cal.getTime();
		String endDateStr = new SimpleDateFormat("dd-MM-yyyy").format(nextYear);
		return endDateStr;
	}
	
	public Multimap<String, String> getListOfAccnoWithPromocode(List<String> fileContent) {

		Multimap<String, String> promoandaccno = ArrayListMultimap.create();
		for (int i = 1; i < fileContent.size(); i++) {
			String[] data = fileContent.get(i).split(",");
			promoandaccno.put(data[5], data[2]);
		}
		return promoandaccno;
	}

	public void sendSMS(List<PromoCodeRequest> listpromoCodeRequest, Headers header) {
		LOG.info("sending sms");
		try {
			List<String> accounts = new ArrayList<>();
			for (PromoCodeRequest promoCodeRequest : listpromoCodeRequest)
				accounts.add(promoCodeRequest.getAccountNumber().get(0));
			LOG.info("request to voucherMemberManagementService " + accounts);
			GetListMemberResponse listMemberResponse = voucherMemberManagementService.getListMemberDetails(accounts,
					header);
			LOG.info("Response from voucherMemberManagementService " + listMemberResponse.toString());
			List<GetListMemberResponseDto> memberList = listMemberResponse.getListMember();

			for (PromoCodeRequest promoCodeRequest : listpromoCodeRequest) {
				for (GetListMemberResponseDto member : memberList) {
					if (promoCodeRequest.getAccountNumber().get(0).equals(member.getAccountNumber())) {
						SMSRequestDto smsRequestDto = new SMSRequestDto();
						List<String> destinationNumber = new ArrayList<>();
						destinationNumber = promoCodeRequest.getAccountNumber();
						smsRequestDto.setTemplateId("138");
						smsRequestDto.setNotificationId("138");
						Map<String, String> additionalParam = new HashMap<>();
						additionalParam.put("FIRST_NAME", member.getFirstName());
						additionalParam.put("Code", promoCodeRequest.getPromoCodeDetails().getPromoCode());
						additionalParam.put("Offer_Description", promoCodeRequest.getOfferType());
						smsRequestDto.setAdditionalParameters(additionalParam);
						smsRequestDto.setDestinationNumber(destinationNumber);
						if(member.getLanguage().equalsIgnoreCase("English")) {
							smsRequestDto.setLanguage("en");
						} else {
							smsRequestDto.setLanguage("ar");
						}
						
						LOG.info("smsRequestDto" + smsRequestDto.toString());

						jmsTemplate.convertAndSend("smsQueue", smsRequestDto);
					}
				}
			}
		} catch (Exception jme) {

			jme.printStackTrace();
		}

	}

	public ValidPromoCodeDetails validatePromoCode(String promoCode, ValidPromoCodeDetails resultResponse,
			String offerId, String offerTypeId, String subscriptionCatalogId, String accountNumber, int denomination, Headers headers,
			GetMemberResponse memberResponse) {
		try {
			LOG.info("Inside Validate PromoCode");
			LOG.info("accountNumber1 : {}",accountNumber);
			List<PromoCode> promoCodeEntityList = new ArrayList<PromoCode>();
			PromoCode promoCodeEntity = new PromoCode();
			List<String> subscriptionList = new ArrayList<String>();
			if (null != promoCode) {
				promoCodeEntityList = promoCodeRepository.findByPromoCode(promoCode);
			} else {
				if (subscriptionCatalogId != null) {
					subscriptionList.add(subscriptionCatalogId);
					List<PromoCode> promoCodeList = promoCodeRepository.findBySubcscriptionsIn(subscriptionList);
					if (!promoCodeList.isEmpty()) {
						for (PromoCode promo : promoCodeList) {
							if (promo.getPromoLevel().getLevelId() == 31) {
								promoCodeEntity = promo;
								break;
							}
						}
					}
				}
			}
			LOG.info("accountNumber2 : {}",accountNumber);
			if (promoCodeEntityList.isEmpty()) {
				LOG.debug("Promo Code is not valid");
				resultResponse.setErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_VALIDATION_FAILED.getIntId(),
						PromoCodeErrorCodes.PROMOCODE_VALIDATION_FAILED.getMsg());
				return resultResponse;
			}else{
				promoCodeEntity = promoCodeEntityList.get(0);
			}
			LOG.info("AccountNumber3:{}",accountNumber);
			LOG.info("PromoCode Object : " + promoCodeEntity.toString());
			OfferCatalog offer = null;
			PromoDetails promoDetails = new PromoDetails();
			if (offerTypeId == null && offerId != null) {
				offer = offerRepository.findByOfferId(offerId);
				if (offer != null) {
					offerTypeId = offer.getOfferType().getOfferTypeId();
				}
			}
			LOG.info("AccountNumber4:{}",accountNumber);
			String customerType= null;
			if (accountNumber != null) {
				GetMemberResponse memberDetails = !ObjectUtils.isEmpty(memberResponse) ? memberResponse
						: fetchServiceValues.getMemberDetails(accountNumber, resultResponse, headers);// Add headers
																										// here
				LOG.info("AccountNumber getmemberresponse:{}",memberDetails.getAccountNumber());
				if (null == memberDetails) {
					return resultResponse;
				}
				customerType = memberDetails.getCustomerType().get(0);
			}
			LOG.info("AccountNumber5:{}",accountNumber);
			if (promoCode == null) {
				promoCode = promoCodeEntity.getPromoCode();
			}
			LOG.info("AccountNumber6:{}",accountNumber);
			List<PurchaseHistory> accountNumberUsedPromoCode = purchaseRepository
					.findByAccountNumberAndPromoCodeAndStatusIgnoreCase(accountNumber, promoCode, "Success");
			LOG.info("accountNumber7 : {}",accountNumber);
			List<PurchaseHistory> promoCodeUsed = purchaseRepository.findByPromoCodeAndStatusIgnoreCase(promoCode, "Success");
			LOG.info("accountNumber8 : {}",accountNumber);
			PromoCodeResult promoCodeResult = new PromoCodeResult();
			promoCodeResult.setPromoCode(promoCodeEntity.getPromoCode());
			if (promoCodeEntity.getAccountNumber() != null && !promoCodeEntity.getAccountNumber().isEmpty()) {
				promoCodeResult.setAccountNumber(promoCodeEntity.getAccountNumber());
			}
			LOG.info("accountNumber9 : {}",accountNumber);
			if (promoCodeEntity.getDenominationAmount() != null && !promoCodeEntity.getDenominationAmount().isEmpty()) {
				promoCodeResult.setDenominationAmount(promoCodeEntity.getDenominationAmount());
			}
			if (promoCodeEntity.getOfferIds() != null && !promoCodeEntity.getOfferIds().isEmpty()) {
				promoCodeResult.setOfferId(promoCodeEntity.getOfferIds().get(0));
			}
			promoCodeResult.setOfferType(promoCodeEntity.getOfferType());
			if (promoCodeUsed != null && !promoCodeUsed.isEmpty()) {
				promoCodeResult.setRedeemedCount(promoCodeUsed.size());
			} else {
				promoCodeResult.setRedeemedCount(0);
			}
			promoCodeResult.setTotalCount(promoCodeEntity.getTotalCount());
			if (accountNumberUsedPromoCode != null && !accountNumberUsedPromoCode.isEmpty()) {
				promoCodeResult.setUsageRedeemedCount(accountNumberUsedPromoCode.size());
			} else {
				promoCodeResult.setUsageRedeemedCount(0);
			}
			promoCodeResult.setUsageCount(promoCodeEntity.getUserRedeemedAccountLimit());
			promoCodeResult.setStartDate(promoCodeEntity.getStartDate());
			promoCodeResult.setEndDate(promoCodeEntity.getEndDate());
			if (promoCodeEntity.getCustomerType() != null && !promoCodeEntity.getCustomerType().isEmpty()) {
				promoCodeResult.setCustomerType(promoCodeEntity.getCustomerType().get(0).getEligibleCustomerTypes());
			}
			if (promoCodeEntity.getSubcscriptions() != null && !promoCodeEntity.getSubcscriptions().isEmpty()) {
				promoCodeResult.setSubscriptionCatalogId(promoCodeEntity.getSubcscriptions().get(0));
			}LOG.info("accountNumber10 : {}",accountNumber);
			PromoCodeDMRequest promoCodeDMRequest = new PromoCodeDMRequest();
			promoCodeDMRequest.setOfferId(offerId);
			promoCodeDMRequest.setOfferType(offerTypeId);
			promoCodeDMRequest.setAccountNumber(accountNumber);
			promoCodeDMRequest.setDenominationAmount(denomination);
			LOG.info("accountNumber11 : {}",accountNumber);
			LOG.info("promoCodeDMRequest AccountNumber : {}",promoCodeDMRequest.getAccountNumber());
			if (null != promoCode) {
				promoCodeDMRequest.setPromoCode(promoCode);
			} else {
				promoCodeDMRequest.setPromoCode(promoCodeEntity.getPromoCode());
			}
			promoCodeDMRequest.setPromoCodeResult(promoCodeResult);
			promoCodeDMRequest.setSubscriptionCatalogId(subscriptionCatalogId);
			promoCodeDMRequest.setCustomerType(customerType);
			LOG.info("accountNumber12 : {}",accountNumber);
			LOG.info("Promo Code DM Request : " + promoCodeDMRequest.toString());
			DecisionManagerResponseDto decisionManagerResponseDTO = callDecisionManager(promoCodeDMRequest,
					resultResponse);
			LOG.info("accountNumber13 : {}",accountNumber);
			if (decisionManagerResponseDTO != null) {
				LOG.info("DecisionManager response :" + decisionManagerResponseDTO.isPromoEligibilityStatus());
				if (decisionManagerResponseDTO.isPromoEligibilityStatus()) {
					resultResponse.setResult(PromoCodeSuccessCodes.PROMOCODE_VALIDATED_SUCCESSFULLY.getId(),
							PromoCodeSuccessCodes.PROMOCODE_VALIDATED_SUCCESSFULLY.getMsg());

					promoDetails.setPromoCode(promoCodeEntity.getPromoCode());
					promoDetails.setPromoValue(promoCodeEntity.getValue());
					if (promoCodeEntity.getPromoType().getType().equalsIgnoreCase("Free")) {
						promoDetails.setPromoType("1");
					} else if (promoCodeEntity.getPromoType().getType().equalsIgnoreCase("Percentage")) {
						promoDetails.setPromoType("2");
					} else if (promoCodeEntity.getPromoType().getType().equalsIgnoreCase("Flat Rate")) {
						promoDetails.setPromoType("3");
					} else if (promoCodeEntity.getPromoType().getType().equalsIgnoreCase("Free Duration")) {
						promoDetails.setPromoType("4"); 
					}
					else {
						promoDetails.setPromoType(promoCodeEntity.getPromoType().getType());
					}
					promoDetails.setDuration(promoCodeEntity.getDuration());
					resultResponse.setPromoDetails(promoDetails);

				} else {
					resultResponse.setErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_VALIDATION_FAILED.getIntId(),
							decisionManagerResponseDTO.getReason());
				}
			}
			return resultResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return resultResponse;
		}
	}
	
	public List<PromoCode> getPromocodeAll(String page,String size)
	{
		Page<PromoCode> output = null;
		List<PromoCode> promocodes=new ArrayList<PromoCode>();
		 if  ((null != page) && (null != size)) 
			{
			 output=promoCodeRepository.findAll( PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)));
			 if(null != output) {
				 List<PromoCode> outputContent =output.getContent().isEmpty()?null:output.getContent();
				 
				 return outputContent;
			 }
			
			
			}
		 else
		 {
			 
			 promocodes= promoCodeRepository.findAll(); 
		 }
		return promocodes;
	}

	public ResultResponse updatePromoCodeDetails(PromoCode promoCodeRequest,ResultResponse resultResponse,String user,Headers header)
	{
		try
		{
		PromoCodeDomain promoCodeDomain = null;
		
		List<PromoCode> promocodeList= promoCodeRepository.findByPromoCode(promoCodeRequest.getPromoCode());
		PromoCode promocode = new PromoCode();
		if(!promocodeList.isEmpty())
		{
			promocode = promocodeList.get(0);
			LOG.info("promocode",promocode);
		
		if(null==promoCodeRequest.getRuleId().getRuleId() || null==promoCodeRequest.getPromoCode() || null==promoCodeRequest.getPromoLevel().getLevelId())
		{
			LOG.info("Null data");
			resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getIntId(),PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getMsg());
		}
		
		else 
		{
			if(!promocode.getRuleId().getRuleId().equals(promoCodeRequest.getRuleId().getRuleId()) || !promocode.getPromoCode().equals(promoCodeRequest.getPromoCode()) || 
					!promocode.getPromoLevel().getLevelId().equals(promoCodeRequest.getPromoLevel().getLevelId()))
		{
				LOG.info("Invalid data");
			resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.INVALID_PROMOCODE_DETAILS.getIntId(), PromoCodeErrorCodes.INVALID_PROMOCODE_DETAILS.getMsg());
			resultResponse.setResult(PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getId(),PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getMsg());
		}
		else
		{
			
			promoCodeDomain = new PromoCodeDomain.PromoCodeBuilder(promoCodeRequest.getProgram(), promoCodeRequest.getPromoCode(),
					promoCodeRequest.getDescription(), promoCodeRequest.getOfferIds(), promoCodeRequest.getValue(),
					promoCodeRequest.getPromoLevel(), promoCodeRequest.getPromoType(), promoCodeRequest.getRuleId(), promoCodeRequest.getStartDate(),
					promoCodeRequest.getEndDate(), promoCodeRequest.getTotalCount(),
					promoCodeRequest.getUserRedeemedAccountLimit(), promoCodeRequest.getDuration(),
					promoCodeRequest.getDenominationAmount(), promoCodeRequest.getOfferType(), promoCodeRequest.getSubcscriptions(),
					promoCodeRequest.getUserRedeemedAccountLimit(), promoCodeRequest.getCustomerType(),
					promoCodeRequest.getAccountNumber(), promoCodeRequest.getPartner(), promoCodeRequest.getPartnerRef()).dtCreated(new Date())
							.dtUpdated(new Date()).usrCreated(user).usrUpdated(user).id(promocode.getId()).build();
			PromoCode promoCodeToUpdate = modelMapper.map(promoCodeDomain, PromoCode.class);
			
			promoCodeRepository.save(promoCodeToUpdate);
			resultResponse.setResult(PromoCodeSuccessCodes.PROMOCODE_UPDATED_SUCCESSFULLY.getId(),PromoCodeSuccessCodes.PROMOCODE_UPDATED_SUCCESSFULLY.getMsg());
			
		}
		}
		}
		else
		{
			LOG.info("Promocode deatils not found for given info");
			resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getIntId(), PromoCodeErrorCodes.PROMOCODE_DETAILS_NOT_FOUND.getMsg());
			resultResponse.setResult(PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getId(),PromoCodeErrorCodes.PROMOCODE_UPDATION_FAILED.getMsg());
		}
		LOG.info("response",resultResponse);
		return resultResponse;
		}
		catch (Exception e) {
			e.printStackTrace();
			return resultResponse;
		}
	}
	
	
	
	private DecisionManagerResponseDto callDecisionManager(PromoCodeDMRequest promoCodeDMRequest,
			ResultResponse resultResponse) {
		String url = decisionManagerUri + eligibilityCheck;
		RestTemplate restTemplate = new RestTemplate();
		DecisionManagerResponseDto decisionManagerResponseDTO = null;

		LOG.info(OfferConstants.HITTING_URL.get() + OfferConstants.RULES_CHECK_DESC.get()
				+ OfferConstants.MESSAGE_SEPARATOR.get() + OfferConstants.SINGLE_MESSAGE.get(), url);

		try {

			LOG.info(
					OfferConstants.REQUEST_PARAMS_FOR.get() + OfferConstants.RULES_CHECK_DESC.get()
							+ OfferConstants.MESSAGE_SEPARATOR.get() + OfferConstants.SINGLE_MESSAGE.get(),
					promoCodeDMRequest);

			HttpEntity<PromoCodeDMRequest> requestEntity = new HttpEntity<>(promoCodeDMRequest);

			ResponseEntity<CommonApiStatus> response = retryCallForCallDecisionManager(url, requestEntity);

			CommonApiStatus commonApiStatus = response.getBody();
			LOG.info(
					OfferConstants.RESPONSE_PARAMS_FOR.get() + OfferConstants.RULES_CHECK_DESC.get()
							+ OfferConstants.MESSAGE_SEPARATOR.get() + OfferConstants.SINGLE_MESSAGE.get(),
					commonApiStatus);

			if (null != commonApiStatus) {

				ApiStatus apiStatus = commonApiStatus.getApiStatus();

				if (apiStatus.getStatusCode() == 0) {

					decisionManagerResponseDTO = (DecisionManagerResponseDto) serviceHelper
							.convertToObject(response.getBody().getResult(), DecisionManagerResponseDto.class);

				} else {

					resultResponse.setErrorAPIResponse(apiStatus.getStatusCode(), apiStatus.getOverallStatus());
				}
			} else {

				resultResponse.addErrorAPIResponse(OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getIntId(),
						OfferErrorCodes.DECISION_MANAGER_RESPONSE_NOT_RECEIVED.getMsg());
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();

		}

		LOG.info("inside checkAllRulesForOffers decisionManagerResult {}", decisionManagerResponseDTO);
		return decisionManagerResponseDTO;

		// TODO Auto-generated method stub

	}

	private ResponseEntity<CommonApiStatus> retryCallForCallDecisionManager(String url, HttpEntity<PromoCodeDMRequest> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForCallDecisionManager method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return restTemplate.exchange(url, HttpMethod.POST, requestEntity, CommonApiStatus.class);
		});		
	}
	
	public PromoCode createPromoCodeWelcomeGift(PromoCodeRequest promoCodeRequest, ResultResponse resultResponse,
			String program2, String user) {
		LOG.info("Inside CreatePromoCode Method");
		try {
			PromoCodeDomain promoCodeDomain = null;
			List<String> offerDomainList = new ArrayList<String>();
			OfferCatalog offer = null;
			if (null != promoCodeRequest && promoCodeRequest.getOfferId() != null) {
				offer = offerRepository.findByOfferId(promoCodeRequest.getOfferId());
			}
			if (null != promoCodeRequest.getOfferId() && offer == null) {
				resultResponse.addErrorAPIResponse(PromoCodeErrorCodes.OFFER_NOT_PRESENT.getIntId(),
						OfferErrorCodes.OFFER_NOT_PRESENT.getMsg());
				resultResponse.setResult(PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getId(),
						PromoCodeErrorCodes.PROMOCOD_CREATION_FAILED.getMsg());
			} else {
				PromoCodeDetails promoCodeDetails = promoCodeRequest.getPromoCodeDetails();
				if (null != promoCodeRequest.getOfferId()) {
					offerDomainList.add(promoCodeRequest.getOfferId());
				}
				promoLevel = new PromoLevels();
				if (promoCodeDetails.getPromoCodeLevel().equalsIgnoreCase(PromoCodeConstants.INSTANCE.get())) {
					promoLevel.setLevelId(30);
					promoLevel.setDescription(PromoCodeConstants.INSTANCE_PROMOTION.get());
				} else {
					promoLevel.setLevelId(31);
					promoLevel.setDescription(PromoCodeConstants.UMBRELLA_PROMOTION.get());
				}
				promoType = new PromoTypes();
				promoType.setType(promoCodeDetails.getPromoCodeType());
				ruleId = new PromoRules();
				ruleId.setRuleId(promoCodeRequest.getRuleId());
				ruleId.setIsWelcomeGift("Y");
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				denominationAmount = new ArrayList<Integer>();
				if (promoCodeDetails.getPromoDenominationAmount() != null
						&& !promoCodeDetails.getPromoDenominationAmount().isEmpty()) {
					for (Integer denomination : promoCodeDetails.getPromoDenominationAmount()) {
						denominationAmount.add(denomination);
					}
				}
				customerType = new ArrayList<CustomerType>();
				CustomerType customerTypeNew = new CustomerType();
				customerTypeNew.setEligibleCustomerTypes(promoCodeRequest.getCustomerType());
				customerType.add(customerTypeNew);
				promoCodeDomain = new PromoCodeDomain.PromoCodeBuilder(program, promoCodeDetails.getPromoCode(),
						promoCodeDetails.getPromoCodeDescription(), offerDomainList, promoCodeDetails.getValue(),
						promoLevel, promoType, ruleId, df.parse(promoCodeDetails.getStartDate()),
						df.parse(promoCodeDetails.getEndDate()), promoCodeDetails.getPromoCodeTotalCount(), 0, 0,
						denominationAmount, promoCodeRequest.getOfferType(), null, 2, customerType,
						promoCodeRequest.getAccountNumber(), promoCodeRequest.getPartner(),promoCodeRequest.getPartnerRef()).dtCreated(new Date())
								.dtUpdated(new Date()).usrCreated(user).usrUpdated(user).build();
				PromoCode promoCodeToSave = modelMapper.map(promoCodeDomain, PromoCode.class);
				LOG.info("Promo Code to save : " + promoCodeToSave.toString());
				promoCodeRepository.save(promoCodeToSave);
				return promoCodeToSave;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public PromoCodeApplyAndValidate applyPromoCode(String promoCode, String subscriptionCatalogId, String offerId, double cost,
			int pointsValue, String accountNumber, int denomination, Headers headers, GetMemberResponse memberResponse,
			ResultResponse result) {
		PromoCodeApplyAndValidate promoCodeApplyAndValidate = new PromoCodeApplyAndValidate();
		PromoCodeApply promoCodeApply = new PromoCodeApply();
		ValidPromoCodeDetails resultResponse = new ValidPromoCodeDetails(null);
		LOG.info("PromoCodeDomain.applyPromoCode : accountNumber: {}",accountNumber);
		resultResponse = validatePromoCode(promoCode, resultResponse, offerId, null, subscriptionCatalogId,
				accountNumber, denomination, headers, memberResponse);
		if (resultResponse.getApiStatus().getStatusCode() == 0) {
			if (resultResponse.getPromoDetails().getPromoType().equals("1")) {
				promoCodeApply.setCost(0);
				promoCodeApply.setPointsValue(0);
				promoCodeApply.setDuration(resultResponse.getPromoDetails().getDuration());
				promoCodeApply.setStatus(0);
				promoCodeApply.setPromoType(resultResponse.getPromoDetails().getPromoType());
			} else if (resultResponse.getPromoDetails().getPromoType().equals("2")) {
				cost = cost - cost * resultResponse.getPromoDetails().getPromoValue() / 100.0;
				double pointsValueUpdated = Utils
						.round(pointsValue * resultResponse.getPromoDetails().getPromoValue() / 100.0, 0);
				pointsValue = pointsValue - (int) pointsValueUpdated;
				promoCodeApply.setCost(cost);
				promoCodeApply.setPointsValue(pointsValue);
				promoCodeApply.setDuration(resultResponse.getPromoDetails().getDuration());
				promoCodeApply.setStatus(0);
				promoCodeApply.setPromoType(resultResponse.getPromoDetails().getPromoType());
			} else if (resultResponse.getPromoDetails().getPromoType().equals("3")) {
				cost = cost - resultResponse.getPromoDetails().getPromoValue();
				Double value = pointsValue - resultResponse.getPromoDetails().getPromoValue() / 0.008;
				pointsValue = value.intValue();
				promoCodeApply.setCost(cost);
				promoCodeApply.setPointsValue(pointsValue);
				promoCodeApply.setDuration(resultResponse.getPromoDetails().getDuration());
				promoCodeApply.setStatus(0);
				promoCodeApply.setPromoType(resultResponse.getPromoDetails().getPromoType());
			} else if (resultResponse.getPromoDetails().getPromoType().equals("4")) {
				promoCodeApply.setCost(0);
				promoCodeApply.setPointsValue(0);
				promoCodeApply.setDuration(resultResponse.getPromoDetails().getDuration());
				promoCodeApply.setStatus(0);
				promoCodeApply.setPromoType(resultResponse.getPromoDetails().getPromoType());
			}
		} else {
			promoCodeApply.setStatus(1);
			result.addErrorAPIResponse(PromoCodeErrorCodes.PROMOCODE_VALIDATION_FAILED.getIntId(),
					PromoCodeErrorCodes.PROMOCODE_VALIDATION_FAILED.getMsg());
		}
		promoCodeApplyAndValidate.setPromoCodeApply(promoCodeApply);
		promoCodeApplyAndValidate.setValidatePromoCodeResponse(resultResponse);
		return promoCodeApplyAndValidate;
	}
	
	@Async
	public void burnSubsPromoVoucher(PurchaseRequestDto purchaseRequestDto, Headers headers) {
		try {
			LOG.info("Entering burnSubsPromoVoucher method");
			OfferCatalog offer = new OfferCatalog();
			Voucher voucher = voucherRepository.findByVoucherCode(purchaseRequestDto.getPromoCode());
			if(!ObjectUtils.isEmpty(voucher) && !ObjectUtils.isEmpty(voucher.getOfferInfo()) && !ObjectUtils.isEmpty(voucher.getOfferInfo().getOffer())) {
				offer = offerRepository.findByOfferId(voucher.getOfferInfo().getOffer());
			}
			if(!ObjectUtils.isEmpty(offer) && !ObjectUtils.isEmpty(offer.getOfferType()) && !ObjectUtils.isEmpty(offer.getOfferType().getOfferTypeId()) && Checks.checkIsDiscountVoucher(offer.getOfferType().getOfferTypeId()) && !ObjectUtils.isEmpty(offer.getSubscriptionDetails())
					&& !ObjectUtils.isEmpty(offer.getSubscriptionDetails().getSubscPromo()) && offer.getSubscriptionDetails().getSubscPromo()) {
				if(!ObjectUtils.isEmpty(voucher) && purchaseRequestDto.getPromoCode().equalsIgnoreCase(voucher.getVoucherCode())) {
					BurnVoucherRequest burnVoucherRequest = new BurnVoucherRequest();
					VoucherResultResponse response = null;
					LOG.info("headers : {}",headers);
					String program = !StringUtils.isEmpty(headers.getProgram()) ? headers.getProgram() : null;
					String authorization = !StringUtils.isEmpty(headers.getAuthorization()) ? headers.getAuthorization() : null;
					String externalTransactionId = !StringUtils.isEmpty(headers.getExternalTransactionId()) ? headers.getExternalTransactionId() : null;
					String userName = !StringUtils.isEmpty(headers.getUserName()) ? headers.getUserName() : null;
					String sessionId = !StringUtils.isEmpty(headers.getSessionId()) ? headers.getSessionId() : null;
					String userPrev = !StringUtils.isEmpty(headers.getUserPrev()) ? headers.getUserPrev() : null;
					String channelId = !StringUtils.isEmpty(headers.getChannelId()) ? headers.getChannelId() : null;
					String userRole = null;
					String systemId = !StringUtils.isEmpty(headers.getSystemId()) ? headers.getSystemId() : null;
					String systemPassword = !StringUtils.isEmpty(headers.getSystemPassword()) ? headers.getSystemPassword() : null;
					String token = !StringUtils.isEmpty(headers.getToken()) ? headers.getToken() : null;
					String transactionId = !StringUtils.isEmpty(headers.getTransactionId()) ? headers.getTransactionId() : null;
					List<String> voucherCode = new ArrayList<String>();
					voucherCode.add(voucher.getVoucherCode());
					burnVoucherRequest.setVoucherCodes(voucherCode);
					burnVoucherRequest.setInvoiceId("");
					burnVoucherRequest.setMerchantCode(voucher.getMerchantCode());
					burnVoucherRequest.setPartnerCode(voucher.getPartnerCode());
					burnVoucherRequest.setRemarks("burn");
					Integer storePin = null;
					if(!ObjectUtils.isEmpty(offer.getOfferStores()) && !ObjectUtils.isEmpty(offer.getOfferStores().get(0).getContactPersons())
							&& !ObjectUtils.isEmpty(offer.getOfferStores().get(0).getContactPersons().get(0).getPin())) {
						storePin = offer.getOfferStores().get(0).getContactPersons().get(0).getPin();
					}
					burnVoucherRequest.setStorePin(storePin);
					LOG.info("burnVoucherRequest : {}",burnVoucherRequest);
					response = voucherController.burnVouchers(burnVoucherRequest,
							program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, userRole, systemId, systemPassword,
							token, transactionId);
					LOG.info("response : {}",response);
				}
			}
			LOG.info("Exiting burnSubsPromoVoucher method");
		} catch (Exception e) {
			e.printStackTrace();
			exceptionLogsService.saveExceptionsToExceptionLogs(e, headers.getExternalTransactionId(), null, headers.getUserName());
		}
	}

}
