package com.loyalty.marketplace.equivalentpoints.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.ConversionRateDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.EquivalentPointsDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.event.HeaderDto;
import com.loyalty.marketplace.equivalentpoints.inbound.helper.EquivalentPointsHelperBase;
import com.loyalty.marketplace.equivalentpoints.outbound.database.entity.ConversionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.database.repository.ConversionRateRepository;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiError;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.GetMemberResponseDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ListRedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.PartnerActivityDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.PartnerDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.PartnerResultResponse;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionPointsOverlapRangeList;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionPointsValueChangeList;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.TierBonus;
import com.loyalty.marketplace.equivalentpoints.utils.AbstractController;
import com.loyalty.marketplace.equivalentpoints.utils.Channel;
import com.loyalty.marketplace.equivalentpoints.utils.EquivalentPointsException;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorCodes;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorMessages;
import com.loyalty.marketplace.equivalentpoints.utils.Product;
import com.loyalty.marketplace.equivalentpoints.utils.ProductWithDenominations;

import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;

import lombok.AccessLevel;
import lombok.Getter;

@Component
public class EquivalentPointsDomain extends ErrorMessages implements AbstractController {

	private static final Logger log = LoggerFactory.getLogger(EquivalentPointsDomain.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConversionRateRepository conversionRateRepository;

	@Autowired
	private Channel channel;

	@Autowired
	private Product product;

	@Autowired
	private ProductWithDenominations productWithDenominations;

	@Autowired
	private EquivalentPointsHelperBase memberActivityHelperBase;

	@Getter(AccessLevel.NONE)
	@Autowired
	AuditService auditService;

	@Autowired
	private RetryTemplate retryTemplate;
	
	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	@Value("${partnerManagement.uri}")
	private String partnerMgmtUrl;

	@Value("${partnerCodeUrl}")
	private String partnerCodeUrl;

	@Value("${memberActivity.uri}")
	private String partnerActivityUrl;

	@Value("${memberActivity.partnerActivity.path}")
	private String getPartnerActivityUrl;

	@Value("${equivalentpoint.pointsstart.range1}")
	private String pointsStart1;

	@Value("${equivalentpoint.pointsstart.range2}")
	private String pointsStart2;

	@Value("${equivalentpoint.pointsend.range1}")
	private String pointsEnd1;

	@Value("${equivalentpoint.pointsend.range2}")
	private String pointsEnd2;

	@Value("${equivalentpoint.aedstart.range1}")
	private String aedStart1;

	@Value("${equivalentpoint.aedstart.range2}")
	private String aedStart2;

	@Value("${equivalentpoint.aedend.range1}")
	private String aedEnd1;

	@Value("${equivalentpoint.aedend.range2}")
	private String aedEnd2;

	@Value("${equivalentpoint.cofficientA.range1}")
	private String cofficientA1;

	@Value("${equivalentpoint.cofficientA.range2}")
	private String cofficientA2;

	@Value("${equivalentpoint.cofficientB.range1}")
	private String cofficientB1;

	@Value("${equivalentpoint.cofficientB.range2}")
	private String cofficientB2;

	private String partnerURL = "Get partner request url: {}";
	private String responseGenerated = "Response: {}";

	/**
	 * Validation for Conversion Rate
	 * 
	 * @param conversionRateDto
	 * @param userName
	 * @param program
	 * @throws MemberActivityException
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	public void insertConversionRate(ConversionRateDto conversionRateDto, String userName, String program, String token,
			String externalTransactionId) throws EquivalentPointsException {
		String partnerType = null;
		Double lowValue = null;
		Double highValue = null;
		Double pointStart = null;
		Double pointEnd = null;
		Double coefficientA = null;
		Double coefficientB = null;
		String partnerCodeVal = conversionRateDto.getPartnerCode();
		String channelVal = conversionRateDto.getChannel();
		String productItem = conversionRateDto.getProductItem();
		Optional<ConversionRate> conversionRate = conversionRateRepository
				.findByPartnerCodeAndChannelAndProductItemAndLowValueAndHighValue(partnerCodeVal, channelVal,
						productItem, conversionRateDto.getDenominationRange().getLowValue(),
						conversionRateDto.getDenominationRange().getHighValue());
		if (userName == null || userName.isEmpty()) {
			userName = RequestMappingConstants.USER_NAME;
		}

		if (conversionRate.isPresent()) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE_PRESENT,
					duplicateConversionRate);
		}

		validatePartnerCode(userName, token, partnerType, partnerCodeVal);
		if (conversionRateDto.getValuePerPoint() != null && conversionRateDto.getValuePerPoint() < 0) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidValuePerPoint);
		}
		if (program == null || program.isEmpty()) {
			program = defaultProgramCode;
		}

		validateConversionRate(conversionRateDto, channelVal, productItem);

		if (conversionRateDto != null && conversionRateDto.getValuePerPoint() != null
				&& ((channelVal != null && !channelVal.isEmpty())
						|| (partnerCodeVal != null && !partnerCodeVal.isEmpty())
						|| (productItem != null && !productItem.isEmpty()))) {
			if (conversionRateDto.getDenominationRange() != null) {
				lowValue = conversionRateDto.getDenominationRange().getLowValue();
				highValue = conversionRateDto.getDenominationRange().getHighValue();
				pointStart = conversionRateDto.getDenominationRange().getPointStart();
				pointEnd = conversionRateDto.getDenominationRange().getPointEnd();
				coefficientA = conversionRateDto.getDenominationRange().getCoefficientA();
				coefficientB = conversionRateDto.getDenominationRange().getCoefficientB();
			}
			ConversionRateDomain conversionRateDomain = new ConversionRateDomain.ConversionRateBuilder(
					conversionRateDto.getValuePerPoint()).programCode(program).partnerCode(partnerCodeVal)
							.channel(channelVal).productItem(productItem).lowValue(lowValue).highValue(highValue)
							.createdDate(new Date()).lastUpdated(new Date()).createdUser(userName).updatedUser(userName)
							.pointStart(pointStart).pointEnd(pointEnd).coefficientA(coefficientA)
							.coefficientB(coefficientB).build();
			insertRate(conversionRateDomain, userName, externalTransactionId);
		} else if (conversionRateDto != null && conversionRateDto.getValuePerPoint() == null) {
			log.error("ValuePerPoint is null");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					nullValuePerPoint);
		} else if (conversionRateDto != null && (channelVal == null || channelVal.isEmpty())
				&& (partnerCodeVal == null || partnerCodeVal.isEmpty())
				&& (productItem == null || productItem.isEmpty())) {
			log.error("Provide values for Channel or PartnerCode or ProductItem");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, nullParameters);
		} else {
			log.error("Invalid Parameters");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidInput);
		}

	}

	/**
	 * called from insertConversionRate()
	 * 
	 * @param conversionRateDto
	 * @param channelVal
	 * @param productItem
	 * @throws EquivalentPointsException
	 */
	private void validateConversionRate(ConversionRateDto conversionRateDto, String channelVal, String productItem)
			throws EquivalentPointsException {
		if (((channelVal != null && enumContainsChannel(channelVal))
				|| (productItem != null && enumContainsProduct(productItem)))
				&& conversionRateDto.getDenominationRange() != null
				&& (conversionRateDto.getDenominationRange().getHighValue() != null
						&& conversionRateDto.getDenominationRange().getLowValue() != null
						&& conversionRateDto.getDenominationRange().getPointStart() != null
						&& conversionRateDto.getDenominationRange().getPointEnd() != null)) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_DENOMINATIONRANGE,
					invalidDenominationRange);
		}

		if ((productItem != null && enumContainsProductWithDenominations(productItem))
				&& conversionRateDto.getDenominationRange() != null
				&& (conversionRateDto.getDenominationRange().getHighValue() == null)
				&& (conversionRateDto.getDenominationRange().getLowValue() == null)
				&& (conversionRateDto.getDenominationRange().getPointStart() != null)
				&& (conversionRateDto.getDenominationRange().getPointEnd() != null)) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_DENOMINATIONRANGE,
					nullDenominationRange);
		}
	}

	/**
	 * called from insertConversionRate()
	 * 
	 * @param userName
	 * @param token
	 * @param partnerType
	 * @param partnerCodeVal
	 * @throws EquivalentPointsException
	 */
	private void validatePartnerCode(String userName, String token, String partnerType, String partnerCodeVal)
			throws EquivalentPointsException {
		if (partnerCodeVal != null && !partnerCodeVal.isEmpty()) {
			PartnerResultResponse partnerResultResponse = validatePartnerCode(partnerCodeVal, userName, token);
			List<PartnerDto> partners = partnerResultResponse.getPartners();
			if (partners != null) {
				for (PartnerDto partner : partners) {
					partnerType = partner.getPartnerType();
				}
				if (partnerType != null && !(partnerType.equalsIgnoreCase(ApplicationConstants.PARTNER_TYPE_REDEMPTION)
						|| partnerType.equalsIgnoreCase(ApplicationConstants.PARTNER_TYPE_BOTH))) {
					throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_PARTNERCODE,
							invalidPartnerCode);
				}
			} else {
				throw new EquivalentPointsException(ErrorCodes.PARTNER_NULL_PARAMETERS_PARTNERCODE, noPartnerCode);
			}
		} else {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_NULL_PARAMETERS_PARTNERCODE, nullPartnerCode);
		}
	}

	/**
	 * Inserting into ConversionRate table
	 * 
	 * @param conversionRateDomain
	 */
	public void insertRate(ConversionRateDomain conversionRateDomain, String userName, String externalTransactionId) {
		log.debug("Inside create() of EquivalentDomain inserting ConversionRate document {} ", conversionRateDomain);
		ConversionRate rate = modelMapper.map(conversionRateDomain, ConversionRate.class);
//		auditService.insertDataAudit(ApplicationConstants.CONVERSION_RATE, rate,
//				ApplicationConstants.CREATE_CONVERSION_RATE, externalTransactionId, userName);
		conversionRateRepository.insert(rate);
	}

	/**
	 * Setting headers for RestTemplate
	 * 
	 * @return
	 */
	private static MultiValueMap<String, String> getHeaders(String userName, String token) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/json");
		headers.add(RequestMappingConstants.USER_NAME, userName);
		headers.add(RequestMappingConstants.TOKEN, token);
		return headers;
	}

	/**
	 * Call for PartnerCode validation from PartnerManagement Application
	 * 
	 * @param partnerCode
	 * @return
	 * @throws MemberActivityException
	 */
	public PartnerResultResponse validatePartnerCode(String partnerCode, String userName, String token)
			throws EquivalentPointsException {
		String codeUrl = partnerMgmtUrl + partnerCodeUrl + partnerCode;
		MultiValueMap<String, String> header = getHeaders(userName, token);
		HttpEntity<String> requestEntity = new HttpEntity<>(partnerCode, header);
		ResponseEntity<PartnerResultResponse> response = null;
		try {
			log.info("Get partner request url: {}", codeUrl);
			log.info("Get partner request details: {}", requestEntity);
			response = retryCallForValidatePartnerCode(codeUrl, requestEntity);
			log.info("Response: {}", response.getBody());
		} catch (RestClientException e) {
			throw new EquivalentPointsException(ErrorCodes.NOT_FOUND, partnerMangementNotFound);
		}
		return response.getBody();
	}

	/***
	 * 
	 * @param requestEntity
	 * @param url
	 * @return
	 */
	private ResponseEntity<PartnerResultResponse> retryCallForValidatePartnerCode(String url,
			HttpEntity<String> requestEntity) {

		log.info("inside Retry block using retryTemplate of retryCallForValidatePartnerCode method of class {}",
				this.getClass().getName());
		try {
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), null,
						requestEntity.toString(), null);
				return restTemplate.exchange(url, HttpMethod.GET, requestEntity, PartnerResultResponse.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, null, null,
					null, url.toString());
		}
		return null;

	}

	/**
	 * Check if the input value in present in the CustomerTier List from
	 * application.properties
	 * 
	 * @param value
	 * @return
	 */
	private boolean enumContainsChannel(String value) {
		return channel.getChannels().contains(value.toUpperCase()) ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean enumContainsProduct(String value) {
		return product.getProducts().contains(value.toUpperCase()) ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean enumContainsProductWithDenominations(String value) {
		return productWithDenominations.getProductWithDenominations().contains(value.toUpperCase()) ? Boolean.TRUE
				: Boolean.FALSE;
	}

	/**
	 * @return
	 * @throws MemberActivityException
	 */
	public ListRedemptionRate getAllEquivalentPoints() throws EquivalentPointsException {
		ListRedemptionRate listRedemptionRate = new ListRedemptionRate();
		List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList = new ArrayList<>();
		List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList = new ArrayList<>();
		List<ConversionRate> conversionRateList = conversionRateRepository.findAll();
		if (!conversionRateList.isEmpty()) {
			Map<String, List<ConversionRate>> conversionRateMap = conversionRateList.stream().collect(Collectors
					.groupingBy(p -> p.getPartnerCode() + p.getChannel() + p.getProductItem(), Collectors.toList()));
			log.info("Map for get all equivalent points: {}", conversionRateMap);
			for (Map.Entry<String, List<ConversionRate>> map : conversionRateMap.entrySet()) {
				getBothConversionRates(map.getValue(), redemptionPointsValueChangeList,
						redemptionPointsOverlapRangeList);
			}
		} else {
			log.error("No ConversionRates available");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_CONVERSIONRATE_EMPTY, conversionRateEmpty);
		}
		listRedemptionRate.setRedemptionPointsOverlapRangeList(redemptionPointsOverlapRangeList);
		listRedemptionRate.setRedemptionPointsValueChangeList(redemptionPointsValueChangeList);
		return listRedemptionRate;
	}

	/**
	 * @param conversionRateList
	 * @param listRedemptionRate
	 * @param redemptionPointsValueChangeList
	 * @param redemptionPointsOverlapRangeList
	 * @throws MemberActivityException
	 */
	public void getBothConversionRates(List<ConversionRate> conversionRateList,
			List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList,
			List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList) {
		// sort conversionRateList wrt to pointstart value in ConversionRate
		conversionRateList.sort((r1, r2) -> r1.getPointStart().compareTo(r2.getPointStart()));
		// log.info("ConversionRate List : {} ", conversionRateList);
		ConversionRate conversionRate2 = null;
		ConversionRate conversionRate3 = null;

		List<Double> pointStart = new ArrayList<Double>();
		pointStart.add(new Double(pointsStart1));
		pointStart.add(new Double(pointsStart2));

		List<Double> pointEnd = new ArrayList<Double>();
		pointEnd.add(new Double(pointsEnd1));
		pointEnd.add(new Double(pointsEnd2));

		List<Double> aedStart = new ArrayList<Double>();
		aedStart.add(new Double(aedStart1));
		aedStart.add(new Double(aedStart2));

		List<Double> aedEnd = new ArrayList<Double>();
		aedEnd.add(new Double(aedEnd1));
		aedEnd.add(new Double(aedEnd2));

		List<Double> coEffecientA = new ArrayList<Double>();
		coEffecientA.add(new Double(cofficientA1));
		coEffecientA.add(new Double(cofficientA2));

		List<Double> coEffecientB = new ArrayList<Double>();
		coEffecientB.add(new Double(cofficientB1));
		coEffecientB.add(new Double(cofficientB2));

		for (int i = 0; i < conversionRateList.size(); i++) {
			ConversionRate conversionRate1 = conversionRateList.get(i);
			RedemptionPointsOverlapRangeList overlapList = null;
			// if the element is the last element in the list then break
			if (i == conversionRateList.size() - 1) {
				break;
				// else continue and check the ith element end value is >= (i+1)th element's low
				// value
			} else if (conversionRateList.get(i).getPointEnd() >= conversionRateList.get(i + 1).getPointStart()) {
				// log.info("Overlapping Points :{}", conversionRateList.get(i));
				// if so add it to the overlapping list
				overlapList = new RedemptionPointsOverlapRangeList(conversionRate1.getId(), pointStart.get(i),
						pointEnd.get(i), aedStart.get(i), aedEnd.get(i), coEffecientA.get(i), coEffecientB.get(i),
						conversionRate1.getProductItem(), conversionRate1.getChannel(),
						conversionRate1.getPartnerCode(), conversionRate1.getUpdatedUser(),
						conversionRate1.getLastUpdated());
				// add the (i+1)th element to the conversionRate2 object
				conversionRate2 = conversionRateList.get(i + 1);
				redemptionPointsOverlapRangeList.add(overlapList);
			}
			// else add the ith element to the nonOverlapList
			// log.info("Non Overlapping Point :{}", conversionRateList.get(i));
			RedemptionPointsValueChangeList nonOverlapList = new RedemptionPointsValueChangeList(
					conversionRate1.getId(), conversionRate1.getValuePerPoint(), conversionRate1.getLowValue(),
					conversionRate1.getHighValue(), conversionRate1.getPointStart(), conversionRate1.getPointEnd(),
					conversionRate1.getProductItem(), conversionRate1.getChannel(), conversionRate1.getPartnerCode(),
					conversionRate1.getUpdatedUser(), conversionRate1.getLastUpdated());
			// and the (i+1)th to the conversionRate3 object
			conversionRate3 = conversionRateList.get(i + 1);
			redemptionPointsValueChangeList.add(nonOverlapList);
		}
		RedemptionPointsOverlapRangeList overlapList1 = null;
		RedemptionPointsValueChangeList nonOverlapList1 = null;
		/*
		 * if (conversionRate2 != null) { // the above saved conversionRate2 object is
		 * assigned and saved to overlapList overlapList1 = new
		 * RedemptionPointsOverlapRangeList(conversionRate2.getId(),
		 * conversionRate2.getPointStart(), conversionRate2.getPointEnd(),
		 * conversionRate2.getLowValue(), conversionRate2.getHighValue(),
		 * conversionRate2.getCoefficientA(), conversionRate2.getCoefficientB(),
		 * conversionRate2.getProductItem(), conversionRate2.getChannel(),
		 * conversionRate2.getPartnerCode(), conversionRate2.getUpdatedUser(),
		 * conversionRate2.getLastUpdated());
		 * redemptionPointsOverlapRangeList.add(overlapList1); }
		 */
		if (conversionRate3 != null) {
			// the above saved conversionRate3 object is assigned and saved to
			// nonOverlapList
			nonOverlapList1 = new RedemptionPointsValueChangeList(conversionRate3.getId(),
					conversionRate3.getValuePerPoint(), conversionRate3.getLowValue(), conversionRate3.getHighValue(),
					conversionRate3.getPointStart(), conversionRate3.getPointEnd(), conversionRate3.getProductItem(),
					conversionRate3.getChannel(), conversionRate3.getPartnerCode(), conversionRate3.getUpdatedUser(),
					conversionRate3.getLastUpdated());
			redemptionPointsValueChangeList.add(nonOverlapList1);
		}
		// since both overlap and nonoverlap lists have duplicate entries so iteration
		// is happening over nonoverlap list and the overlapped value with
		// the as the search key is removed from the nonoverlap list
		/*
		 * if (redemptionPointsValueChangeList != null) { for
		 * (RedemptionPointsValueChangeList listVal : redemptionPointsValueChangeList) {
		 * if (overlapList1 != null && listVal.getPaymentFactorRangeId() != null &&
		 * overlapList1.getOverlapId() != null &&
		 * listVal.getPaymentFactorRangeId().equalsIgnoreCase(overlapList1.getOverlapId(
		 * ))) { redemptionPointsValueChangeList.remove(listVal); } } }
		 */
		singleConversionRate(conversionRateList, redemptionPointsValueChangeList);
	}

	/**
	 * single conversionRate in the list and with null amount and null point value
	 * 
	 * @param conversionRateList
	 * @param redemptionPointsValueChangeList
	 */
	private void singleConversionRate(List<ConversionRate> conversionRateList,
			List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList) {
		if (conversionRateList.size() == 1 && redemptionPointsValueChangeList != null) {
			RedemptionPointsValueChangeList redemptionPointsValueChangeList1 = new RedemptionPointsValueChangeList(
					conversionRateList.get(0).getId(), conversionRateList.get(0).getValuePerPoint(),
					conversionRateList.get(0).getLowValue(), conversionRateList.get(0).getHighValue(),
					conversionRateList.get(0).getPointStart(), conversionRateList.get(0).getPointEnd(),
					conversionRateList.get(0).getProductItem(), conversionRateList.get(0).getChannel(),
					conversionRateList.get(0).getPartnerCode(), conversionRateList.get(0).getUpdatedUser(),
					conversionRateList.get(0).getLastUpdated());
			redemptionPointsValueChangeList.add(redemptionPointsValueChangeList1);
		}
	}

	/**
	 * @param equivalentPointsDto
	 * @param headerDto
	 * @return
	 * @throws MemberActivityException
	 */
	public List<RedemptionRate> calculateEquivalentAmount(EquivalentPointsDto equivalentPointsDto)
			throws EquivalentPointsException {
		DecimalFormat f = new DecimalFormat("##.00");
		List<RedemptionRate> redemptionRateList = new ArrayList<>();
		if ((equivalentPointsDto.getPartnerCode() == null || equivalentPointsDto.getPartnerCode().isEmpty())
				&& (equivalentPointsDto.getChannel() == null || equivalentPointsDto.getChannel().isEmpty())
				&& (equivalentPointsDto.getOfferType() == null || equivalentPointsDto.getOfferType().isEmpty())) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidParametersForEquivalentPoints);
		}
		if (equivalentPointsDto.getActivityCode() != null) {
			log.error("No Activity Code in ConversionRate Collection");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidActivityCode);
		}
		if (equivalentPointsDto.getOperationType() != null && equivalentPointsDto.getOperationType()
				.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_REDEEMING)) {
			pointsCalculation(equivalentPointsDto, f, redemptionRateList);
		} else {
			log.error("Invalid Parameters For OperationType for Points Conversion to AED");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidOperationType);
		}
		return redemptionRateList;
	}

	/**
	 * called from calculateEquivalentAmount()
	 * 
	 * @param equivalentPointsDto
	 * @param f
	 * @param redemptionRateList
	 * @throws EquivalentPointsException
	 */
	private void pointsCalculation(EquivalentPointsDto equivalentPointsDto, DecimalFormat f,
			List<RedemptionRate> redemptionRateList) throws EquivalentPointsException {
		Double calculatedValue;
		RedemptionRate redemptionRate;
		List<ConversionRate> conversionRateList = conversionRateRepository
				.findByPartnerCodeAndChannelAndProductItemAndPoint(equivalentPointsDto.getPartnerCode(),
						equivalentPointsDto.getChannel(), equivalentPointsDto.getOfferType(),
						equivalentPointsDto.getPoint());
		// overlapping case for points
		if (conversionRateList.size() > 1) {
			log.info("overlapping case for points");
			overlappingPoints(equivalentPointsDto, f, redemptionRateList, conversionRateList);
		} else if (conversionRateList.size() == 1) {
			// non overlapping case for points
			log.info("non overlapping case for points");
			// calculatedValue = equivalentPointsDto.getPoint() *
			// conversionRateList.get(0).getValuePerPoint();
			calculatedValue = BigDecimal.valueOf(equivalentPointsDto.getPoint())
					.multiply(BigDecimal.valueOf(conversionRateList.get(0).getValuePerPoint()), MathContext.DECIMAL32)
					.doubleValue();
			log.info("Calculated Value : " + calculatedValue);
			redemptionRate = new RedemptionRate((Math.floor(calculatedValue * 1e2) / 1e2),
					conversionRateList.get(0).getValuePerPoint());
			redemptionRateList.add(redemptionRate);
		} else {
			// with no input points
			// gets list of overlap and non overlap values
			log.info("with no input points");
			getConversionRateList(equivalentPointsDto, f, redemptionRateList);
		}
	}

	/**
	 * called from pointsCalculation()
	 * 
	 * @param equivalentPointsDto
	 * @param f
	 * @param redemptionRateList
	 * @throws EquivalentPointsException
	 */
	private void getConversionRateList(EquivalentPointsDto equivalentPointsDto, DecimalFormat f,
			List<RedemptionRate> redemptionRateList) throws EquivalentPointsException {
		Double calculatedValue;
		RedemptionRate redemptionRate;
		List<ConversionRate> conversionRateList1 = conversionRateRepository.findByPartnerCodeAndChannelAndProductItem(
				equivalentPointsDto.getPartnerCode(), equivalentPointsDto.getChannel(),
				equivalentPointsDto.getOfferType());
		if (!conversionRateList1.isEmpty()) {
			for (ConversionRate rate : conversionRateList1) {
				calculatedValue = equivalentPointsDto.getPoint() * rate.getValuePerPoint();
				log.info("Calculated Value : " + calculatedValue);
				redemptionRate = new RedemptionRate((Math.floor(calculatedValue * 1e2) / 1e2), rate.getValuePerPoint());
				redemptionRateList.add(redemptionRate);
			}
		} else {
			log.error("No Records found with the input parameters partnerCode={} channel={} offerType={}",
					equivalentPointsDto.getPartnerCode(), equivalentPointsDto.getChannel(),
					equivalentPointsDto.getOfferType());
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidInput);
		}
	}

	/**
	 * called from pointsCalculation()
	 * 
	 * @param equivalentPointsDto
	 * @param f
	 * @param redemptionRate
	 * @param redemptionRateList
	 * @param conversionRateList
	 * @throws EquivalentPointsException
	 */
	private void overlappingPoints(EquivalentPointsDto equivalentPointsDto, DecimalFormat f,
			List<RedemptionRate> redemptionRateList, List<ConversionRate> conversionRateList)
			throws EquivalentPointsException {
		Double calculatedValue;
		Double coefficientA = 0.0;
		double firstAedStart = 444.44;
		double secondAedStart = 900;

		List<Double> pointStart = new ArrayList<Double>();
		pointStart.add(new Double(pointsStart1));
		pointStart.add(new Double(pointsStart2));

		List<Double> pointEnd = new ArrayList<Double>();
		pointEnd.add(new Double(pointsEnd1));
		pointEnd.add(new Double(pointsEnd2));

		List<Double> aedStart = new ArrayList<Double>();
		aedStart.add(new Double(aedStart1));
		aedStart.add(new Double(aedStart2));

		List<Double> aedEnd = new ArrayList<Double>();
		aedEnd.add(new Double(aedEnd1));
		aedEnd.add(new Double(aedEnd2));

		List<Double> coEffecientA = new ArrayList<Double>();
		coEffecientA.add(new Double(cofficientA1));
		coEffecientA.add(new Double(cofficientA2));

		List<Double> coEffecientB = new ArrayList<Double>();
		coEffecientB.add(new Double(cofficientB1));
		coEffecientB.add(new Double(cofficientB2));
		RedemptionRate redemptionRate;
		conversionRateList.sort((r1, r2) -> r1.getPointStart().compareTo(r2.getPointStart()));

		Double coefficientB = 0.0;
		for (int i = 0; i < pointStart.size(); i++) {
			if (equivalentPointsDto.getPoint() >= pointStart.get(i)
					&& equivalentPointsDto.getPoint() <= pointEnd.get(i)) {
				coefficientB = coEffecientB.get(i);
				coefficientA = coEffecientA.get(i);
				break;
			}
		}
		log.info("Equiavalent CoEfficientA data :" + coefficientA);
		log.info("Equiavalent CoEfficientB data :" + coefficientB);
		calculatedValue = (equivalentPointsDto.getPoint() * coefficientA) + coefficientB;
		log.info("Equiavalent Calculated data :" + calculatedValue);
		Optional<ConversionRate> conRate = conversionRateRepository
				.findByPartnerCodeAndChannelAndProductItemAndCalculatedAmount(equivalentPointsDto.getPartnerCode(),
						equivalentPointsDto.getChannel(), equivalentPointsDto.getOfferType(), calculatedValue);
		if (conRate.isPresent()) {
			redemptionRate = new RedemptionRate((Math.floor(calculatedValue * 1e2) / 1e2),
					conRate.get().getValuePerPoint());
		} else {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidCoefficientA);
		}
		redemptionRateList.add(redemptionRate);
	}

	/**
	 * called from pointsCalculation()
	 * 
	 * @param equivalentPointsDto
	 * @param f
	 * @param redemptionRate
	 * @param redemptionRateList
	 * @param conversionRateList
	 * @throws EquivalentPointsException
	 */
	private void overlappingAed(EquivalentPointsDto equivalentPointsDto, DecimalFormat f,
			List<RedemptionRate> redemptionRateList) throws EquivalentPointsException {
		Double calculatedValue;
		Double coefficientA = 0.0;
		double firstAedStart = 444.44;
		double secondAedStart = 900;

		List<Double> pointStart = new ArrayList<Double>();
		pointStart.add(new Double(pointsStart1));
		pointStart.add(new Double(pointsStart2));

		List<Double> pointEnd = new ArrayList<Double>();
		pointEnd.add(new Double(pointsEnd1));
		pointEnd.add(new Double(pointsEnd2));

		List<Double> aedStart = new ArrayList<Double>();
		aedStart.add(new Double(aedStart1));
		aedStart.add(new Double(aedStart2));

		List<Double> aedEnd = new ArrayList<Double>();
		aedEnd.add(new Double(aedEnd1));
		aedEnd.add(new Double(aedEnd2));

		List<Double> coEffecientA = new ArrayList<Double>();
		coEffecientA.add(new Double(cofficientA1));
		coEffecientA.add(new Double(cofficientA2));

		List<Double> coEffecientB = new ArrayList<Double>();
		coEffecientB.add(new Double(cofficientB1));
		coEffecientB.add(new Double(cofficientB2));
		RedemptionRate redemptionRate;
		// conversionRateList.sort((r1, r2) ->
		// r1.getPointStart().compareTo(r2.getPointStart()));

		Double coefficientB = 0.0;
		for (int i = 0; i < aedStart.size(); i++) {
			if (equivalentPointsDto.getAmount() >= aedStart.get(i)
					&& equivalentPointsDto.getAmount() <= aedEnd.get(i)) {
				coefficientB = coEffecientB.get(i);
				coefficientA = coEffecientA.get(i);
				break;
			}
		}
		log.info("Equiavalent CoEfficientA data :" + coefficientA);
		log.info("Equiavalent CoEfficientB data :" + coefficientB);
		calculatedValue = (equivalentPointsDto.getAmount() - coefficientB) / coefficientA;
		log.info("Equiavalent Calculated data :" + calculatedValue);
		Optional<ConversionRate> conRate = conversionRateRepository
				.findByPartnerCodeAndChannelAndProductItemAndCalculatedAmount(equivalentPointsDto.getPartnerCode(),
						equivalentPointsDto.getChannel(), equivalentPointsDto.getOfferType(),
						equivalentPointsDto.getAmount());
		if (conRate.isPresent()) {
			redemptionRate = new RedemptionRate((Math.ceil(calculatedValue)), conRate.get().getValuePerPoint());
		} else {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidCoefficientA);
		}
		redemptionRateList.add(redemptionRate);
	}

	/**
	 * @param equivalentPointsDto
	 * @return
	 * @throws MemberActivityException
	 */
	public ListRedemptionRate getConversionRates(EquivalentPointsDto equivalentPointsDto, String userName, String token)
			throws EquivalentPointsException {
		List<RedemptionRate> baseRateList = new ArrayList<>();
		ListRedemptionRate listRedemptionRate = new ListRedemptionRate();
		List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList = new ArrayList<>();
		List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList = new ArrayList<>();
		if (!equivalentPointsDto.getOperationType().equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)
				&& !equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_REDEEMING)) {
			log.error("Invalid Parameters For OperationType");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidOperationType);
		}

		rateValidation(equivalentPointsDto);
		if (equivalentPointsDto.getOperationType() != null && !equivalentPointsDto.getOperationType().isEmpty()
				&& equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)) {
			if (equivalentPointsDto.getActivityCode() != null && !equivalentPointsDto.getActivityCode().isEmpty()) {
				rateForActivityCode(equivalentPointsDto, baseRateList, userName, token);
			} else {
				log.error("Invalid ActivityCode");
				throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
						invalidActivityCode);
			}
		} else if (equivalentPointsDto.getOperationType() != null && !equivalentPointsDto.getOperationType().isEmpty()
				&& equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_REDEEMING)) {
			getEquivalentPointsRedeeming(equivalentPointsDto, redemptionPointsValueChangeList,
					redemptionPointsOverlapRangeList);

		} else {
			log.error(
					"Invalid Parameters For Equivalent Points Calculation For Overlapping And Non Overlapping changes");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidInput);
		}
		listRedemptionRate.setRedemptionPointsOverlapRangeList(redemptionPointsOverlapRangeList);
		listRedemptionRate.setRedemptionPointsValueChangeList(redemptionPointsValueChangeList);
		return listRedemptionRate;
	}

	/**
	 * called from getConversionRates
	 * 
	 * @param equivalentPointsDto
	 * @param redemptionPointsValueChangeList
	 * @param redemptionPointsOverlapRangeList
	 * @throws EquivalentPointsException
	 */
	private void getEquivalentPointsRedeeming(EquivalentPointsDto equivalentPointsDto,
			List<RedemptionPointsValueChangeList> redemptionPointsValueChangeList,
			List<RedemptionPointsOverlapRangeList> redemptionPointsOverlapRangeList) throws EquivalentPointsException {
		if (equivalentPointsDto.getActivityCode() != null && !equivalentPointsDto.getActivityCode().isEmpty()) {
			log.error("Invalid ActivityCode");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidActivityCode);
		}
		List<ConversionRate> conversionRateList = conversionRateRepository.findByPartnerCodeAndChannelAndProductItem(
				equivalentPointsDto.getPartnerCode(), equivalentPointsDto.getChannel(),
				equivalentPointsDto.getOfferType());

		if (!conversionRateList.isEmpty()) {
			getBothConversionRates(conversionRateList, redemptionPointsValueChangeList,
					redemptionPointsOverlapRangeList);
		} else {
			log.error("No Records found with the input parameters partnerCode={} channel={} offerType={}",
					equivalentPointsDto.getPartnerCode(), equivalentPointsDto.getChannel(),
					equivalentPointsDto.getOfferType());
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidInput);
		}
	}

	/**
	 * @param equivalentPointsDto
	 * @throws MemberActivityException
	 */
	private void rateValidation(EquivalentPointsDto equivalentPointsDto) throws EquivalentPointsException {
		if ((equivalentPointsDto.getOfferType() != null && !equivalentPointsDto.getOfferType().isEmpty()
				|| equivalentPointsDto.getChannel() != null && !equivalentPointsDto.getChannel().isEmpty()
				|| equivalentPointsDto.getPartnerCode() != null && !equivalentPointsDto.getPartnerCode().isEmpty()
						&& (equivalentPointsDto.getActivityCode() == null
								|| equivalentPointsDto.getActivityCode().isEmpty()))
				&& !equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_REDEEMING)) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidOperationType);
		}
	}

	/**
	 * Returns the rate for the request body having neither account number nor
	 * amount and for ActivityCode
	 * 
	 * @param equivalentPointsDto
	 * @param baseRateList
	 * @throws MemberActivityException
	 */
	private void rateForActivityCode(EquivalentPointsDto equivalentPointsDto, List<RedemptionRate> baseRateList,
			String userName, String token) throws EquivalentPointsException {
		List<PartnerActivityDto> partnerActivityList = getPartnerActivity(equivalentPointsDto.getPartnerCode(),
				userName, token);
		Optional<PartnerActivityDto> partnerActivityDto = partnerActivityList.stream()
				.filter(p -> p.getActivityCode().getCode().equalsIgnoreCase(equivalentPointsDto.getActivityCode()))
				.findFirst();
		if (partnerActivityDto.isPresent()) {
			baseRateList.add(new RedemptionRate(null, partnerActivityDto.get().getBaseRate()));
		} else {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidPartnerActivity);
		}
	}

	/**
	 * Gets PartnerActivity List
	 * 
	 * @param partnerCode
	 * @return
	 * @throws FinancialManagementException
	 */
	public List<PartnerActivityDto> getPartnerActivity(String partnerCode, String userName, String token)
			throws EquivalentPointsException {
		String partnerActivityUrl1 = partnerActivityUrl + "/" + partnerCode + getPartnerActivityUrl;
		List<PartnerActivityDto> list = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(RequestMappingConstants.USER_NAME, userName);
			headers.add(RequestMappingConstants.TOKEN, token);
			HttpEntity<?> entity = new HttpEntity<>(null, headers);
			log.info(partnerURL, partnerActivityUrl1);
			ResponseEntity<CommonApiStatus> response = retryCallForGetPartnerActivity(partnerActivityUrl1, entity);
			log.info(responseGenerated, response.getBody());
			CommonApiStatus commonApiStatus = response.getBody();

			log.info("Exiting getPartnerActivity method of commonApiStatus {}", response);
			if (commonApiStatus.getApiStatus().getStatusCode() == 0) {
				log.info("PartnerActivity Details Retrieved Successfully");
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, PartnerActivityDto> resultLinkedHashMap = (LinkedHashMap<String, PartnerActivityDto>) commonApiStatus
						.getResult();
				Object result = resultLinkedHashMap.get("listPartnerActivity");
				ObjectMapper mapper = new ObjectMapper();
				list = mapper.convertValue(result, new TypeReference<List<PartnerActivityDto>>() {
				});
				log.info("PartnerActivity Object Converted Successfully");
			} else {
				ApiStatus apiStatus = response.getBody().getApiStatus();
				ApiError apiError = apiStatus.getErrors().get(0);
				log.error(apiError.getMessage(), " : ", partnerCode);
				throw new EquivalentPointsException(ErrorCodes.PARTNER_ACTIVITY_GET_ACTIVITY_DETAILS_ERROR,
						apiError.getMessage());
			}
		} catch (RestClientException e) {
			log.info("Failed to connect to MemberActivity URL");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_ACTIVITY_GET_ACTIVITY_DETAILS_ERROR,
					memberActivityNotFound);
		}
		return list;
	}

	/***
	 * 
	 * @param requestEntity
	 * @param url
	 * @return
	 */
	private ResponseEntity<CommonApiStatus> retryCallForGetPartnerActivity(String url, HttpEntity<?> requestEntity) {

		log.info("inside Retry block using retryTemplate of retryCallForGetPartnerActivity method of class {}",
				this.getClass().getName());

		try {
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), null,
						requestEntity.toString(), null);
				return restTemplate.exchange(url, HttpMethod.GET, requestEntity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, null, null,
					null, url.toString());
		}
		return null;

	}

	/**
	 * Calculating Equivalent Points
	 * 
	 * @param equivalentPointsDto
	 * @param userName
	 * @param program
	 * @return
	 * @throws MemberActivityException
	 */
	public List<RedemptionRate> calculateEquivalentPoints(EquivalentPointsDto equivalentPointsDto, HeaderDto headerDto,
			String userName, String token) throws EquivalentPointsException {
		Double amount = equivalentPointsDto.getAmount();
		List<RedemptionRate> redemptionRateList = new ArrayList<>();
		GetMemberResponseDto getMemberResponseDto = null;
		DecimalFormat f = new DecimalFormat("##.00");
		f.setRoundingMode(RoundingMode.DOWN);
		if (equivalentPointsDto.getAmount() != null && equivalentPointsDto.getPoint() != null) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidCalculationForEquivalentPoints);
		}

		// Validating RequestBody for EquivalentPoints
		if ((equivalentPointsDto.getActivityCode() == null || equivalentPointsDto.getActivityCode().isEmpty())
				&& (equivalentPointsDto.getPartnerCode() == null || equivalentPointsDto.getPartnerCode().isEmpty())
				&& (equivalentPointsDto.getChannel() == null || equivalentPointsDto.getChannel().isEmpty())
				&& (equivalentPointsDto.getOfferType() == null || equivalentPointsDto.getOfferType().isEmpty())) {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidParametersForEquivalentPoints);
		}

		if (equivalentPointsDto.getOperationType().equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)) {
			MemberDetailRequestDto memberDetailRequestDto = new MemberDetailRequestDto.MemberDetailRequestDtoBuilder()
					.accountNumber(equivalentPointsDto.getAccountNumber()).build();
			getMemberResponseDto = memberActivityHelperBase
					.invokeMemberManagementforMemberDetails(memberDetailRequestDto, headerDto);
		}

		operationTypeValidation(equivalentPointsDto, userName, token, amount, redemptionRateList, getMemberResponseDto,
				f);

		return redemptionRateList;
	}

	/**
	 * called from calculateEquivalentPoints()
	 * 
	 * @param equivalentPointsDto
	 * @param userName
	 * @param token
	 * @param amount
	 * @param redemptionRateList
	 * @param getMemberResponseDto
	 * @param f
	 * @throws EquivalentPointsException
	 */
	private void operationTypeValidation(EquivalentPointsDto equivalentPointsDto, String userName, String token,
			Double amount, List<RedemptionRate> redemptionRateList, GetMemberResponseDto getMemberResponseDto,
			DecimalFormat f) throws EquivalentPointsException {
		if (equivalentPointsDto.getOperationType() != null && !equivalentPointsDto.getOperationType().isEmpty()
				&& equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)) {
			earningOperationType(equivalentPointsDto, userName, token, amount, redemptionRateList,
					getMemberResponseDto);
		} else if (equivalentPointsDto.getOperationType() != null && !equivalentPointsDto.getOperationType().isEmpty()
				&& equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_REDEEMING)) {
			redeemingOperationType(equivalentPointsDto, redemptionRateList, f);
		} else {
			log.error("Invalid Parameters For OperationType for AED Conversion to Points");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidOperationType);
		}
	}

	/**
	 * for redeeming operation type -------- called from operationTypeValidation()
	 * 
	 * @param equivalentPointsDto
	 * @param redemptionRateList
	 * @param f
	 * @throws EquivalentPointsException
	 */
	private void redeemingOperationType(EquivalentPointsDto equivalentPointsDto,
			List<RedemptionRate> redemptionRateList, DecimalFormat f) throws EquivalentPointsException {
		RedemptionRate redemptionRate;
		Double calculatedValue;
		if (equivalentPointsDto.getActivityCode() != null && !equivalentPointsDto.getActivityCode().isEmpty()) {
			log.error("Invalid Activity Code");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidActivityCode);
		}
		boolean overlapSituation = false;
		if ((equivalentPointsDto.getAmount() >= 444.44 && equivalentPointsDto.getAmount() <= 562.49)
				|| (equivalentPointsDto.getAmount() >= 1000 && equivalentPointsDto.getAmount() <= 1111.1)) {
			overlapSituation = true;
		}
		Optional<ConversionRate> conversionRate = conversionRateRepository
				.findByPartnerCodeAndChannelAndProductItemAndAmount(equivalentPointsDto.getPartnerCode(),
						equivalentPointsDto.getChannel(), equivalentPointsDto.getOfferType(),
						equivalentPointsDto.getAmount());
		if (conversionRate.isPresent()) {
			if ((equivalentPointsDto.getOfferType().equalsIgnoreCase("billRecharge")) && overlapSituation) {
				log.info("overlapping case for aed");
				overlappingAed(equivalentPointsDto, f, redemptionRateList);
			} else {
				calculatedValue = Math.ceil(equivalentPointsDto.getAmount() / conversionRate.get().getValuePerPoint());
				redemptionRate = new RedemptionRate(Double.parseDouble(f.format(calculatedValue)),
						conversionRate.get().getValuePerPoint());
				redemptionRateList.add(redemptionRate);
			}
		} else {
			List<ConversionRate> conversionRateList = conversionRateRepository
					.findByPartnerCodeAndChannelAndProductItem(equivalentPointsDto.getPartnerCode(),
							equivalentPointsDto.getChannel(), equivalentPointsDto.getOfferType());
			if (!conversionRateList.isEmpty()) {
				for (ConversionRate rate : conversionRateList) {
					calculatedValue = Math.ceil(equivalentPointsDto.getAmount() / rate.getValuePerPoint());
					redemptionRate = new RedemptionRate(Double.parseDouble(f.format(calculatedValue)),
							rate.getValuePerPoint());
					redemptionRateList.add(redemptionRate);
				}
			} else {
				log.error("Invalid Parameters For Equivalent Points Calculation");
				throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidInput);
			}
		}
	}

	/**
	 * for earning operation type ------- called from operationTypeValidation()
	 * 
	 * @param equivalentPointsDto
	 * @param userName
	 * @param token
	 * @param amount
	 * @param redemptionRateList
	 * @param getMemberResponseDto
	 * @throws EquivalentPointsException
	 */
	private void earningOperationType(EquivalentPointsDto equivalentPointsDto, String userName, String token,
			Double amount, List<RedemptionRate> redemptionRateList, GetMemberResponseDto getMemberResponseDto)
			throws EquivalentPointsException {
		if (equivalentPointsDto.getActivityCode() != null && !equivalentPointsDto.getActivityCode().isEmpty()) {
			pointsForActivityCode(equivalentPointsDto, redemptionRateList, amount, getMemberResponseDto, userName,
					token);
		} else {
			log.error("Invalid ActivityCode.");
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidActivityCode);
		}
	}

	/**
	 * Calculating Equivalent Points For Activity Code from PartnerActivity table
	 * Having rate same as Base Rate/value
	 * 
	 * @param equivalentPointsDto
	 * @param redemptionRate
	 * @param amount
	 * @throws MemberActivityException
	 */
	private void pointsForActivityCode(EquivalentPointsDto equivalentPointsDto, List<RedemptionRate> redemptionRateList,
			Double amount, GetMemberResponseDto getMemberResponseDto, String userName, String token)
			throws EquivalentPointsException {
		double baseRate;
		double tierBonusRate = 0;
		List<PartnerActivityDto> partnerActivityList = getPartnerActivity(equivalentPointsDto.getPartnerCode(),
				userName, token);
		log.info("PartnerActivityList:{}", partnerActivityList);
		Optional<PartnerActivityDto> partnerActivityDto = partnerActivityList.stream()
				.filter(p -> p.getActivityCode().getCode() != null)
				.filter(p -> p.getActivityCode().getCode().equalsIgnoreCase(equivalentPointsDto.getActivityCode()))
				.findFirst();
		if (partnerActivityDto.isPresent()) {
			log.info("PartnerActivity DTO:{}", partnerActivityDto.get());
			baseRate = partnerActivityDto.get().getBaseRate();
			log.info("PartnerActivity baseRate:{}", baseRate);
			if (baseRate >= 0 && amount >= 0) {
				RedemptionRate redemptionRate = new RedemptionRate();
				// for accrual, equivalent points calculation
				if (partnerActivityDto.get().getTierBonus() == null
						|| partnerActivityDto.get().getTierBonus().isEmpty()) {
					calculateEquivalentPointForAccrual(redemptionRate, amount, baseRate);
					redemptionRateList.add(redemptionRate);
				} else {
					// for accrual and a tier bonus associated with the partner code
					// equivalent points calculations
					equivalentPointAccrual(redemptionRate, amount, baseRate, tierBonusRate, partnerActivityDto,
							getMemberResponseDto);
					redemptionRateList.add(redemptionRate);
				}
				log.info("RedemptionRateList:{}", redemptionRateList);
			} else if (baseRate <= 0) {
				throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
						invalidBaseRate);
			} else if (amount < 0) {
				throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
						invalidAmount);
			}
		} else {
			throw new EquivalentPointsException(ErrorCodes.PARTNER_INVALID_PARAMETERS_CONVERSIONRATE,
					invalidPartnerActivity);
		}
	}

	/**
	 * Calculate Equivalent Points for Accrual with TierBonus Value in
	 * PartnerActivity
	 * 
	 * @param equivalentPointsDto
	 * @param redemptionRate
	 * @param amount
	 * @param listRedemtionRate
	 * @param baseRate
	 * @param tierBonusRate
	 * @param partnerActivity
	 * @throws MemberActivityException
	 */
	private void equivalentPointAccrual(RedemptionRate redemptionRate, Double amount, double baseRate,
			double tierBonusRate, Optional<PartnerActivityDto> partnerActivity,
			GetMemberResponseDto getMemberResponseDto) throws EquivalentPointsException {
		double equivalentPoint;
		double basePoints;
		double tierBonusPoints = 0.00;
		// gets the tier level from member-management table for the particular Account
		// Number from the request
		String tierLevel = getMemberResponseDto.getMemberInfo().getTierLevel();
		log.info("Memebr Tier Level " + tierLevel);
		List<TierBonus> tierBonusList = null;
		if (partnerActivity.isPresent()) {
			tierBonusList = partnerActivity.get().getTierBonus();
		}
		// checks if the tierLevel from member management matches with the customerTier
		// from PartnerActivity's tierBonus list
		for (TierBonus t : tierBonusList) {
			if (t.getCustomerTier() != null && t.getCustomerTier().equalsIgnoreCase(tierLevel)) {
				tierBonusRate = t.getTierBonusRate();
			}
		}
		/*
		 * if (tierBonusRate <= 0) { throw new EquivalentPointsException(ErrorCodes.
		 * PARTNER_INVALID_PARAMETERS_CONVERSIONRATE, invalidTierLevel); }
		 */
		basePoints = amount * baseRate;
		if (tierBonusRate > 0) {
			tierBonusPoints = amount * tierBonusRate;
		}
		log.info("Tier Bonus Points : " + tierBonusPoints);
		equivalentPoint = basePoints + tierBonusPoints;
		redemptionRate.setEquivalentPoint(equivalentPoint);
		redemptionRate.setRate(baseRate);
	}

	/**
	 * Calculate Equivalent Points for Accrual with no tierbonus value
	 * 
	 * @param redemptionRate
	 * @param amount
	 * @param listRedemtionRate
	 * @param baseRate
	 */
	private void calculateEquivalentPointForAccrual(RedemptionRate redemptionRate, Double amount, double baseRate) {
		double equivalentPoint;
		equivalentPoint = amount * baseRate;
		redemptionRate.setEquivalentPoint(equivalentPoint);
		redemptionRate.setRate(baseRate);
	}
}
