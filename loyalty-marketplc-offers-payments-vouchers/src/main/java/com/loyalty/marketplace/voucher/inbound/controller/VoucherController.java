package com.loyalty.marketplace.voucher.inbound.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.ws.rs.QueryParam;

import org.apache.commons.csv.CSVPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.gifting.outbound.dto.GiftingHistoryResult;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.domain.model.PurchaseDomain;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SMSRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.constants.VoucherStatus;
import com.loyalty.marketplace.voucher.domain.VoucherActionDomain;
import com.loyalty.marketplace.voucher.domain.VoucherDomain;
import com.loyalty.marketplace.voucher.domain.VoucherUploadFileDomain;
import com.loyalty.marketplace.voucher.inbound.dto.AccountsDto;
import com.loyalty.marketplace.voucher.inbound.dto.BurnCashVoucher;
import com.loyalty.marketplace.voucher.inbound.dto.BurnCashVoucherRequest;
import com.loyalty.marketplace.voucher.inbound.dto.BurnVoucherRequest;
import com.loyalty.marketplace.voucher.inbound.dto.ListVoucherRequest;
import com.loyalty.marketplace.voucher.inbound.dto.RollBackVoucherBurnRequest;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherActionDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherActionRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherBalanceRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherCancelRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherCodesDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherRequestDto;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherTransferRequest;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherUploadDto;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse;
import com.loyalty.marketplace.voucher.outbound.database.entity.OfferInfo;
import com.loyalty.marketplace.voucher.outbound.database.entity.Voucher;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherUploadFile;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherActionRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepositoryHelper;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherUploadFileRepository;
import com.loyalty.marketplace.voucher.outbound.dto.AccountWithActiveVouchersResult;
import com.loyalty.marketplace.voucher.outbound.dto.BurnVoucher;
import com.loyalty.marketplace.voucher.outbound.dto.CancelVoucherResponse;
import com.loyalty.marketplace.voucher.outbound.dto.FileContentDto;
import com.loyalty.marketplace.voucher.outbound.dto.FreeVoucherUploadHandbackFileResponse;
import com.loyalty.marketplace.voucher.outbound.dto.ListAccountWithActiveVouchersResponse;
import com.loyalty.marketplace.voucher.outbound.dto.ListVoucherStatusResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherBalanceResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherContent;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherGiftDetailsResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherReconciliationDtoResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherTransfer;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadFileContentResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadFileContentResult;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadList;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadListResponse;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherUploadResponse;
import com.loyalty.marketplace.voucher.outbound.service.CarreFourService;
import com.loyalty.marketplace.voucher.outbound.service.MafService;
import com.loyalty.marketplace.voucher.service.VoucherService;
import com.loyalty.marketplace.voucher.utils.Utils;
import com.loyalty.marketplace.voucher.utils.VoucherCsvGenerator;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.voucher.utils.VoucherValidator;
import com.mongodb.client.result.UpdateResult;
import com.opencsv.CSVWriter;

import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/marketplace")
@RefreshScope
public class VoucherController {

	@Autowired
	Validator validator;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	VoucherActionRepository voucherActionRepository;

	@Autowired
	VoucherActionDomain voucherActionDomain;
	
	@Autowired
	VoucherUploadFileDomain voucherUploadFileDomain;

	@Autowired
	VoucherDomain voucherDomain;

	@Autowired
	VoucherControllerHelper voucherControllerHelper;

	@Autowired
	VoucherRepository voucherRepository;
	
	@Autowired
	VoucherRepositoryHelper voucherRepositoryHelper;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	OfferRepository offerRepository;	

	@Autowired
	MemberManagementService memberManagementService;

	@Autowired
	private MafService mafService;
	
	@Autowired
	private CarreFourService carreFourService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	ProgramManagement programManagement;
	
	@Autowired
	private VoucherUploadFileRepository voucherUploadFileRepository;
	
	@Autowired
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Autowired
	private EventHandler eventHandler;
	
	@Autowired
	private VoucherService voucherService;
		
	@Value("${initializecarrefourapi.uri}")
	private String initializecarrefourapiUrl;

	@Value("${voucher.handback.file.location}")
	private String fileLocation;

	@Value("${voucher.pdf.file.location}")
	private String pdfLocation;

	@Value("${voucher.pdf.downlaod}")
	private Boolean isDownload;

	@Value("${voucher.pdf.save}")
	private Boolean save;
	
	@Value("#{'${ygag.smsalert.destination.numbers}'.split(',')}")
    private List<String> ygagAlertdestinationNumbers;
	
	@Value("#{'${crfr.smsalert.destination.numbers}'.split(',')}")
    private List<String> crfrAlertdestinationNumbers;
	
	@Value("#{'${admin.portal.partners}'.split(',')}")
	private List<String> adminPortalPartners;

	@Value("${programManagement.defaultProgramCode}")
	private String defaultProgramCode;

	@Autowired
	MongoOperations mongoOperations;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	PurchaseDomain purchaseDomain;
	
	private static final int RECONCILE_PAGE = 1;
	private static final int RECONCILE_LIMIT = 100;

	private static final Logger LOG = LoggerFactory.getLogger(VoucherController.class);
	private static final String RESPONSE_LOG = "generate response parameters : {}";
	/**
	 * This method is used to generate vouchers 
	 * @param voucherRequestDto
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return VoucherResponse
	 */
	@PostMapping(value = VoucherRequestMappingConstants.GENERATE_VOUCHERS, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResponse generateVoucher(@RequestBody VoucherRequestDto voucherRequestDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("generateVoucher input parameters : {}", voucherRequestDto);
		VoucherResponse resultResponse = new VoucherResponse(externalTransactionId);
		Headers headers = null;
		try {
			program = programManagement.getProgramCode(program);
			
			if (null == program) program = defaultProgramCode;
			headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);
			if (!VoucherValidator.validateDto(voucherRequestDto, validator, resultResponse)) {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				return resultResponse;
			}

			Optional<VoucherAction> voucherAction = voucherActionRepository
					.findById(voucherRequestDto.getVoucherAction());
			if (!voucherAction.isPresent()) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_VOUCHER_ACTION.getIntId(),
						voucherRequestDto.getVoucherAction() + ":"
								+ VoucherManagementCode.INVALID_VOUCHER_ACTION.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				return resultResponse;
			}

			OfferInfo offerInfo = new OfferInfo();
			offerInfo.setOffer(voucherRequestDto.getOfferId());
			offerInfo.setSubOffer(voucherRequestDto.getSubOfferId());
			Date expiryDate;
			Calendar todayEnd = Calendar.getInstance();
			todayEnd.setTime(new Date());
			todayEnd.set(Calendar.HOUR_OF_DAY, 23);  
			todayEnd.set(Calendar.MINUTE, 59);  
			todayEnd.set(Calendar.SECOND, 59);  
			todayEnd.set(Calendar.MILLISECOND, 999); 
			if (null == voucherRequestDto.getVoucherExpiryDate()) {
				expiryDate = voucherControllerHelper.getVoucherExpirydate(voucherRequestDto.getOfferTypeId(),
						voucherRequestDto.getVoucherExpiryPeriod());
			} else if (voucherRequestDto.getVoucherExpiryDate().before(todayEnd.getTime())){
				expiryDate = voucherControllerHelper.getVoucherExpirydate(voucherRequestDto.getOfferTypeId(),
						voucherRequestDto.getVoucherExpiryPeriod());
			}else {
				expiryDate = voucherRequestDto.getVoucherExpiryDate();
			}
			
			double cost = (double) voucherRequestDto.getCost() / voucherRequestDto.getNumberOfVoucher();
			long points =   voucherRequestDto.getPointsValue() / voucherRequestDto.getNumberOfVoucher();
			if (voucherRequestDto.getVoucherAction().equalsIgnoreCase(VoucherConstants.GENERATE)) {
				resultResponse = voucherControllerHelper.voucherGenerate(voucherRequestDto, program, offerInfo, points,
						expiryDate, userName, token, cost, voucherDomain, resultResponse, externalTransactionId, channelId);

			} else if (voucherRequestDto.getVoucherAction().equalsIgnoreCase(VoucherConstants.SEARCH)) {
				resultResponse = voucherControllerHelper.voucherSearch(voucherRequestDto, resultResponse, voucherDomain,
					userName, token, expiryDate, cost, points, externalTransactionId, channelId);
			} else if (voucherRequestDto.getVoucherAction().equalsIgnoreCase(VoucherConstants.ASK)) {
				resultResponse = voucherControllerHelper.voucherAsk(voucherRequestDto, resultResponse,
						program, offerInfo, points, expiryDate, userName, token, cost, voucherDomain, 
						externalTransactionId, channelId);
			} else {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_VOUCHER_ACTION.getIntId(),
						VoucherManagementCode.INVALID_VOUCHER_ACTION.getMsg());
			}
		} catch (VoucherManagementException vme) {
			exceptionLogService.saveExceptionsToExceptionLogs(vme, externalTransactionId, voucherRequestDto.getAccountNumber(), userName);
			resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			exceptionLogService.saveExceptionsToExceptionLogs(e, externalTransactionId, voucherRequestDto.getAccountNumber(), userName);
			resultResponse.setResult(VoucherManagementCode.VOUCHER_GENERATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_GENERATION_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "generateVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		LOG.info(RESPONSE_LOG, resultResponse);
		if(voucherRequestDto.isSubscPromo() && 
				resultResponse.getResult().getDescription().equalsIgnoreCase(VoucherManagementCode.VOUCHER_GENERATED_SUCCESSFULLY.getMsg())) {
			voucherControllerHelper.voucherPromoCode(voucherRequestDto, headers, resultResponse);
		}
		return resultResponse;
	}
	
	/**
	 * This method returns balance for Carrefour voucher
	 * @param voucherBalanceRequestDto
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return voucher balance from Carrefour
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_BALANCE_FROM_CARREFOUR, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherBalanceResponse voucherBalanceFromCarrefour(@RequestBody VoucherBalanceRequestDto voucherBalanceRequestDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("voucherBalanceFromCarrefour input parameters : {}", voucherBalanceRequestDto);
		VoucherBalanceResponse resultResponse = new VoucherBalanceResponse(externalTransactionId);
		try {
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if (!VoucherValidator.validateDto(voucherBalanceRequestDto, validator, resultResponse)) {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_BALANCE_FROM_CARREFOUR_FAILED.getId(),
						VoucherManagementCode.VOUCHER_BALANCE_FROM_CARREFOUR_FAILED.getMsg());
				return resultResponse;
			}
						
			BigDecimal balance = carreFourService.getEvoucherBalanceFromCarrefour(voucherBalanceRequestDto.getCrfrTransId(), voucherBalanceRequestDto.getCardNumber(), voucherBalanceRequestDto.getAccountNumber(),
					voucherBalanceRequestDto.getAmount(), voucherBalanceRequestDto.getUuid());
			resultResponse.setVoucherBalance(balance.toString());
			
		} catch(Exception e) {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_BALANCE_FROM_CARREFOUR_FAILED.getId(),
					VoucherManagementCode.VOUCHER_BALANCE_FROM_CARREFOUR_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "voucherBalanceFromCarrefour",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		LOG.info(RESPONSE_LOG, resultResponse);
		return resultResponse;
	}

	/**
	 * This method adds a specified voucher action
	 * @param voucherActionDto
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_ACTION, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse configureVoucherAction(@RequestBody VoucherActionRequestDto voucherActionDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("configureVoucherAction input parameters : {}", voucherActionDto);
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		if (!VoucherValidator.validateDto(voucherActionDto, validator, resultResponse)) {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getMsg());
			return resultResponse;
		}
		try {
			program = programManagement.getProgramCode(program);
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			List<VoucherActionDomain> voucherActionToSave = new ArrayList<>();
			List<VoucherAction> existingVoucherAction = voucherActionRepository.findAll();
			List<String> voucherRedemption = new ArrayList<>();

			for (VoucherAction voucherAction : existingVoucherAction) {
				voucherRedemption.add(voucherAction.getRedemptionMethod());
			}
			for (VoucherActionDto vouActionDto : voucherActionDto.getVoucherActionDto()) {

				if (!voucherRedemption.contains(vouActionDto.getRedemptionMethod())) {
					VoucherActionDomain vouActionDomain = new VoucherActionDomain.VoucherActionBuilder(program,
							vouActionDto.getAction(), vouActionDto.getRedemptionMethod(), vouActionDto.getLabel())
									.createdUser(null != userName ? userName : token).createdDate(new Date())
									.id(vouActionDto.getId()).build();
					voucherActionToSave.add(vouActionDomain);

				} else {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.DUPLICATE_VOUCHER_ACTION.getIntId(),
							VoucherManagementCode.DUPLICATE_VOUCHER_ACTION.getMsg() + VoucherConstants.MESSAGE_SEPARATOR
									+ vouActionDto.getRedemptionMethod());
				}
			}

			if (resultResponse.getApiStatus().getErrors() == null
					|| resultResponse.getApiStatus().getErrors().isEmpty()) {
				voucherActionDomain.saveVoucherAction(voucherActionToSave,  externalTransactionId, userName);
				resultResponse.setResult(VoucherManagementCode.VOUCHER_ACTION_CREATED_SUCCESSFULLY.getId(),
						VoucherManagementCode.VOUCHER_ACTION_CREATED_SUCCESSFULLY.getMsg());

			} else {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getId(),
						VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getMsg());
			}
		}

		catch (VoucherManagementException vme) {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getId(),
					VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "configureVoucherAction",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		LOG.info("configureVoucherAction response parameters : {}", resultResponse);
		return resultResponse;

	}

	/**
	 * This is used to reset VoucherAction collection
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_ACTION_RESET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void resetVoucherAction() {

		voucherActionRepository.deleteAll();
	
	}

	/**
	 * This returns all the voucher actions from VoucherAction collection
	 * @return list of VoucherAction
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_ACTION, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VoucherAction> getVoucherAction() {

		return voucherActionRepository.findAll();

	}

	/**
	 * This method cancels a voucher
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param purchaseId
	 * @param action
	 * @return VoucherResultResponse
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_CANCEL, consumes = MediaType.ALL_VALUE)
	public VoucherResultResponse cancelVouchers(@RequestBody VoucherCancelRequestDto voucherCancelRequestDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId ) {
			//@PathVariable(value = VoucherRequestMappingConstants.PURCHASE_ID, required = true) String purchaseId
			//@RequestHeader(value = VoucherRequestMappingConstants.ACTION, required = true) String action) {
		
		VoucherResultResponse cancelVoucherResponse = new VoucherResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		CancelVoucherResponse cancelVoucher = new CancelVoucherResponse();
		cancelVoucher.setStatus(true);
		cancelVoucherResponse.setResult(cancelVoucher);
		if (!voucherCancelRequestDto.getAction().equalsIgnoreCase(VoucherConstants.CANCEL_ACTION)) {
			cancelVoucherResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_ACTION.getIntId(),
					VoucherManagementCode.INVALID_ACTION.getMsg());
			return cancelVoucherResponse;

		}
		try {
			List<Voucher> voucherList = new ArrayList<>();
			if ( null != voucherCancelRequestDto.getVoucherCode()) {
				voucherList = voucherRepository.findByVoucherCodeIn(Arrays.asList(voucherCancelRequestDto.getVoucherCode()));
				LOG.info("Updating purchase history");
				purchaseDomain.updateFailedPurchase(Arrays.asList(voucherList.get(0).getUuid().getId()), voucherCancelRequestDto.getActionReason());
			} else {
				voucherList = voucherRepository.findByUuid(voucherCancelRequestDto.getPurchaseId());
			} 
			LOG.info("voucherList : {}", voucherList);
			if (null == voucherList || voucherList.isEmpty()) {
				cancelVoucherResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_VOUCHERS_AVAILABLE.getMsg());
				return cancelVoucherResponse;
			}
			for (final Voucher vou : voucherList) {
				if (vou.getStatus().equalsIgnoreCase(VoucherStatus.BURNT)) {
					cancelVoucherResponse.setResult(cancelVoucher);
					cancelVoucherResponse.getApiStatus()
							.setMessage(VoucherManagementCode.VOUCHER_BURNT_ALREADY.getMsg());
					return cancelVoucherResponse;
				}
				if (voucherControllerHelper.checkIfInvoiced(vou.getVoucherCode(),headers)) {
					cancelVoucherResponse.setResult(cancelVoucher);
					cancelVoucherResponse.getApiStatus()
							.setMessage(VoucherManagementCode.VOUCHER_INVOICE_ALREADY.getMsg());
					return cancelVoucherResponse;
				}
			}

			voucherDomain.cancelVoucher(null != userName ? userName : token, voucherList, externalTransactionId);
			cancelVoucher.setStatus(false);
			cancelVoucherResponse.setResult(cancelVoucher);
			cancelVoucherResponse.getApiStatus()
					.setMessage(VoucherManagementCode.VOUCHER_CANCELLED_SUCCESSFULLY.getMsg());

		} catch (VoucherManagementException vme) {
			cancelVoucherResponse.setResult(cancelVoucher);
			cancelVoucherResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			cancelVoucherResponse.setResult(cancelVoucher);
			cancelVoucherResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "cancelVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("cancelVoucherResponse response parameters : {}", cancelVoucherResponse);
		return cancelVoucherResponse;
	}

	/**
	 * This method lists vouchers by the specified status
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param accountNumber
	 * @param voucherStatus
	 * @return list of vouchers
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_STATUS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListVoucherStatusResponse listVouchersByStatus(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestParam(value = VoucherRequestMappingConstants.ACCOUNT_NUMBER, required = true) String accountNumber,
			@RequestParam(value = VoucherRequestMappingConstants.OFFER_ID, required = false) String offerId,
			@RequestParam(value = VoucherRequestMappingConstants.PAGE, required = false) Integer page,
			@RequestParam(value = VoucherRequestMappingConstants.LIMIT, required = false) Integer limit,
			@RequestParam(value = VoucherRequestMappingConstants.CHANNEL_CHECK, required = false) boolean channelCheck,
			@PathVariable(value = VoucherRequestMappingConstants.VOUCHER_STATUS, required = true) String voucherStatus) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.REQUEST_PARAMS_LIST_VOUCHER_STATUS,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_STATUS, accountNumber, voucherStatus);

		ListVoucherStatusResponse voucherListResponse = new ListVoucherStatusResponse(externalTransactionId);
		
		if (null == program) program = defaultProgramCode;
		
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);


		try {

			voucherListResponse = voucherControllerHelper.listVoucherByStatus(voucherStatus, accountNumber, headers,
					voucherListResponse, offerId, channelCheck, page, limit);

		} catch (Exception e) {
			e.printStackTrace();
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FAILURE.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_STATUS);

		
		return voucherListResponse;
	}
	
	/**
	 * This method lists vouchers by the specified business id
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param businessId
	 * @return list of vouchers
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_BUSINESSID, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersByBusinessId(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@PathVariable(value = VoucherRequestMappingConstants.BUSINESS_ID, required = true) String businessId) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID, businessId);
		
		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		try {

			if (null == program) program = defaultProgramCode;
			voucherListResponse = voucherControllerHelper.listVoucherByBusinessId(businessId, voucherListResponse,
					headers, VoucherRequestMappingConstants.VOUCHER_LIST_BY_BUSINESSID, channelId, program);

		} catch (Exception e) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID);
		
		return voucherListResponse;
	}
	
	/**
	 * This method lists vouchers for the specified list of business ids
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param listVoucherRequest
	 * 
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_LIST_OF_BUSINESSID, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersByBusinessIdList(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestBody ListVoucherRequest listVoucherRequest) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST, listVoucherRequest);
		
		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		if (null == program) program = defaultProgramCode;
		
		try {

			voucherListResponse = voucherControllerHelper.listVoucherByBusinessIdList(listVoucherRequest,
					voucherListResponse, headers, VoucherRequestMappingConstants.VOUCHER_LIST_BY_LIST_OF_BUSINESSID,
					channelId, program);

		} catch (Exception e) {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_TRANSACTION_FAILED.getMsg());
			LOG.error(e.getMessage());
		}
		
		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_BUSINESS_ID_LIST);
		
		return voucherListResponse;
	
	}

	/**
	 * This method returns a voucher by the specified voucher code
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param voucherCode
	 * @param storeCode
	 * @return voucher object
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_CODE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersByCode(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = VoucherRequestMappingConstants.ACCOUNT_NUMBER, required = false) String accountNumber,
			@PathVariable(value = VoucherRequestMappingConstants.VOUCHER_CODE, required = true) String voucherCode,
			@RequestParam(value = VoucherRequestMappingConstants.STORE_CODE, required = false) String storeCode,
			@RequestParam(value = VoucherRequestMappingConstants.CHANNEL_CHECK, required = false) boolean channelCheck) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.REQUEST_PARAMS_LIST_VOUCHER_BY_CODE,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_CODE, voucherCode, accountNumber, storeCode);

		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);

		if (null == program) program = defaultProgramCode;
		
		try {

			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);

			String[] userNameList = null;
			
			if(null != userName) {
				LOG.info("list voucher by code :: User name is {} ", userName);                
	            userNameList = userName.split(",");
	            if(userNameList.length==0) {
	            	voucherListResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_USER_NAME.getIntId(),
	                                VoucherManagementCode.INVALID_USER_NAME.getMsg());
	            	return voucherListResponse;
	            } else {
	            	voucherListResponse = voucherControllerHelper.listVoucherByCode(accountNumber, voucherCode, storeCode,
	    					voucherListResponse, headers, userNameList[0], channelId, channelCheck);
	            }
			}
		} catch (VoucherManagementException vme) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
			voucherListResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());
		} catch (Exception e) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_SPECIFIC_VOUCHERS_FAILURE.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_CODE);
		
		return voucherListResponse;

	}

	/**
	 * This method returns voucher by the specified id
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param voucherId
	 * @return voucher object
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_ID, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersById(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@PathVariable(value = VoucherRequestMappingConstants.VOUCHER_ID, required = true) String voucherId,
			@RequestParam(value = VoucherRequestMappingConstants.CHANNEL_CHECK, required = false) boolean channelCheck) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + VoucherConstants.REQUEST_PARAMS,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_ID, voucherId);
		
		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);

		if (null == program) program = defaultProgramCode;
		
		try {

			voucherListResponse = voucherControllerHelper.listVoucherById(voucherId, voucherListResponse, channelId, program, channelCheck);

		} catch (Exception e) {
			voucherListResponse.getApiStatus().setMessage(VoucherManagementCode.LISTING_VOUCHERS_FOR_VOUCHER_ID_FAILURE.getMsg());
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_LIST_VOUCHER_BY_ID);

		return voucherListResponse;

	}

	/**
	 * This method sets the status of specified vouchers to burnt
	 * @param burnVoucherRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_BURN, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse burnVouchers(@RequestBody BurnVoucherRequest burnVoucherRequest,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = true) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = true) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("burnVouchers input parameters : {}", burnVoucherRequest);
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			List<BurnVoucher> burnVoucherList = new ArrayList<>();
			if (!VoucherValidator.validateVoucherDto(burnVoucherRequest, validator, resultResponse)) {
				return resultResponse;
			}
			Store store = new Store();
			LOG.info("burnVoucher :: User name is {} ",userName);			
			String[] userNameList = userName.split(",");
			if(userNameList.length==0) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_USER_NAME.getIntId(),
						VoucherManagementCode.INVALID_USER_NAME.getMsg());
				return resultResponse;
			}
			LOG.info("burnVoucher :: User name after splitting is {} and user role is :: {} and channel id passed is :: {}",userNameList[0], userRole, channelId);
			LOG.info("List of channel ids in config are :: {}",adminPortalPartners);
			String role ="";
			if (adminPortalPartners.contains(channelId.toUpperCase())) {				
				if(voucherControllerHelper.validateUser(userRole)) {					
					role=VoucherConstants.ADMIN;					
				}	
				else {
					role= VoucherConstants.OTHERS;
				}
			} else {
				
				if (burnVoucherRequest.getStorePin() == null) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_STORE_PIN.getIntId(),
							VoucherManagementCode.INVALID_STORE_PIN.getMsg());
					return resultResponse;
				} 
			}			
			
			Calendar dateToday = Calendar.getInstance();  
            dateToday.setTime(new Date());  
            dateToday.set(Calendar.HOUR_OF_DAY, 0);  
            dateToday.set(Calendar.MINUTE, 0);  
            dateToday.set(Calendar.SECOND, 0);  
            dateToday.set(Calendar.MILLISECOND, 0);
            
            List<Voucher> vouchersByCode = voucherRepository.findByVoucherCodeAndDate(burnVoucherRequest.getVoucherCodes(), dateToday.getTime());
            if(null != channelId && !channelId.equalsIgnoreCase("SAPP"))
            {
            	List<Voucher> burntVouchers = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.BURNT)).collect(Collectors.toList());
            	LOG.info("Burnt Voucher List:{}", burntVouchers);  
            	voucherDomain.updateEnquiryDetails(burntVouchers.stream().map(Voucher::getVoucherCode)
            			.collect(Collectors.toList()), userName);
            }
            
			for (final String voucherCode : burnVoucherRequest.getVoucherCodes()) {
				Voucher voucher = null;
				if(role.equalsIgnoreCase(VoucherConstants.ADMIN)) {
					//voucher = voucherRepository.findByCodeAndStatusAndDate(voucherCode, VoucherStatus.ACTIVE, dateToday.getTime());
					Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
							&& p.getVoucherCode().equalsIgnoreCase(voucherCode)).findFirst();
					voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
					
				}
				else if(role.equalsIgnoreCase(VoucherConstants.OTHERS)) {
					if(channelId.equalsIgnoreCase("YGAG")) {
						//voucher = voucherRepository.findByCodeAndPartnerCodeAndStatusAndDate(voucherCode, "YGAG", VoucherStatus.ACTIVE, dateToday.getTime());
						Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
								&& p.getVoucherCode().equalsIgnoreCase(voucherCode) && p.getPartnerCode().equalsIgnoreCase("YGAG")).findFirst();
						voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
					}
					
					else {
					store = storeRepository.findByUserName(userNameList[0]);
					if (null == store) {
						LOG.info("Store is null : voucher code is : {} ",voucherCode);
						store = new Store();
						if(burnVoucherRequest.getMerchantCode()!=null) {
							
							//voucher = voucherRepository.findByCodeAndMerchantCodeAndStatusAndDate(voucherCode, burnVoucherRequest.getMerchantCode(), VoucherStatus.ACTIVE, dateToday.getTime());
							Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
									&& p.getVoucherCode().equalsIgnoreCase(voucherCode) && p.getMerchantCode().equalsIgnoreCase(burnVoucherRequest.getMerchantCode())).findFirst();
							voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
						}
						else if(burnVoucherRequest.getPartnerCode()!=null) {
							//voucher = voucherRepository.findByCodeAndPartnerCodeAndStatusAndDate(voucherCode, burnVoucherRequest.getPartnerCode(), VoucherStatus.ACTIVE, dateToday.getTime());
							Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
									&& p.getVoucherCode().equalsIgnoreCase(voucherCode) && p.getPartnerCode().equalsIgnoreCase(burnVoucherRequest.getPartnerCode())).findFirst();
							voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
						}
						else {
							resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_STORE.getIntId(),
								VoucherManagementCode.INVALID_STORE.getMsg());
							continue;
						}
					}
					else {
						//voucher = voucherRepository.findByCodeAndMerchantCodeAndStatusAndDate(voucherCode, store.getMerchantCode(), VoucherStatus.ACTIVE, dateToday.getTime());
						String merchantCode = store.getMerchantCode();
						Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getMerchantCode().equalsIgnoreCase(merchantCode) 
								&&  p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
								&& p.getVoucherCode().equalsIgnoreCase(voucherCode)).findFirst();
						voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
					}
					}
				}
				else {
					//voucher = voucherRepository.findByCodeAndStatusAndDate(voucherCode, VoucherStatus.ACTIVE, dateToday.getTime());
					
					Optional<Voucher> voucherOpt = vouchersByCode.stream().filter(p -> p.getStatus().equalsIgnoreCase(VoucherStatus.ACTIVE)
							&& p.getVoucherCode().equalsIgnoreCase(voucherCode)).findFirst();
					voucher = voucherOpt.isPresent() ? voucherOpt.get() : null;
					
					if (null == voucher) {
						resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getIntId(),
								voucherCode + VoucherConstants.MESSAGE_SEPARATOR
										+ VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getMsg());
						continue;
					}
					store = storeRepository.findByPinAndMerchantCode(burnVoucherRequest.getStorePin(), voucher.getMerchantCode());
					if (null == store) {
						LOG.info("Store is null : voucher code is : {} ",voucherCode);
						resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_STORE.getIntId(),
								VoucherManagementCode.INVALID_STORE.getMsg());
						continue;
					}
					List<ContactPerson> user =store.getContactPersons().stream().filter(m -> (m.getPin().equals(burnVoucherRequest.getStorePin()))).collect(Collectors.toList());
					userNameList[0] = (user!=null && !user.isEmpty())?user.get(0).getUserName():userNameList[0];
				}
				
				if (null == voucher) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getIntId(),
							voucherCode + VoucherConstants.MESSAGE_SEPARATOR
									+ VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getMsg());
					continue;
				} else if (voucher.getExpiryDate().before(dateToday.getTime())) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_EXPIRED.getIntId(), voucherCode
							+ VoucherConstants.MESSAGE_SEPARATOR + VoucherManagementCode.VOUCHER_EXPIRED.getMsg());
					continue;
				}
				
				BurnVoucher burnVoucher = new BurnVoucher();
				voucherDomain.burnVoucher(voucher, burnVoucherRequest, userNameList[0], burnVoucher, store.getStoreCode(),
						externalTransactionId);
				burnVoucherList.add(burnVoucher);
			}
			if (!burnVoucherList.isEmpty()) {
				resultResponse.setResult(burnVoucherList);
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setMessage(VoucherManagementCode.VOUCHER_BURN_SUCCESSFULLY.getMsg());
			}
		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "cancelVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("burnVouchers response parameters : {}", resultResponse);
		return resultResponse;

	}

	/**
	 * This method transfers a voucher from one account to another
	 * @param voucherTransferRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 */
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_TRANSFER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse transferVoucher(@RequestBody VoucherTransferRequest voucherTransferRequest,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {
		LOG.info("transferVoucher input parameters : {}", voucherTransferRequest);
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if (!VoucherValidator.validateVoucherDto(voucherTransferRequest, validator, resultResponse)) {
				return resultResponse;
			}
			Voucher voucher = voucherRepository.findByCodeAndStatusAndExpiryDate(
					voucherTransferRequest.getVoucherCode(), VoucherStatus.ACTIVE, new Date());
			if (null == voucher) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.NO_VOUCHERS_AVAILABLE.getIntId(),
						VoucherManagementCode.NO_VOUCHERS_AVAILABLE.getMsg());
				return resultResponse;
			}
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);
			String memberShipCode = voucherControllerHelper
					.getMemberShipCode(voucherTransferRequest.getTargetAccountNumber(),headers);
			if (null == memberShipCode) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.TARGETACCOUNT_NUMBER_NOT_EXISTS.getIntId(),
						VoucherManagementCode.TARGETACCOUNT_NUMBER_NOT_EXISTS.getMsg());
				return resultResponse;
			}
			voucherDomain.transferVoucher(voucher, voucherTransferRequest, userName, memberShipCode, externalTransactionId);

			VoucherTransfer voucherTransfer = new VoucherTransfer(voucher.getUuid()!=null?voucher.getUuid().getId():null,
					voucherTransferRequest.getAgentName(), memberShipCode);
			resultResponse.getApiStatus().setMessage(VoucherManagementCode.VOUCHER_TRANSFERED_SUCESSFULLY.getMsg());
			resultResponse.setResult(voucherTransfer);
		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "transferVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("transferVoucher response parameters : {}", resultResponse);
		return resultResponse;

	}

	/**
	 * This method generates a pdf with voucher code
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param voucherCode
	 * @param accountNumber
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_GENERATE_PDF, consumes = MediaType.ALL_VALUE)
	public Object generatePdf(@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestParam(value = VoucherRequestMappingConstants.VOUCHER_CODE, required = true) String voucherCode,
			@RequestParam(value = VoucherRequestMappingConstants.ACCOUNT_NUMBER, required = false) String accountNumber) {
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		LOG.info("generatePdf input parameters : {}", voucherCode);
		ByteArrayInputStream bis = null;
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			Voucher voucher = null;
			if (null == accountNumber) {
				voucher = voucherRepository.findByCodeAndStatusAndExpiryDate(voucherCode, VoucherStatus.ACTIVE,
						new Date());
			} else {
				voucher = voucherRepository.findByCodeAndAccountNumberAndStatusAndExpiryDate(voucherCode, accountNumber,
						VoucherStatus.ACTIVE, new Date());
			}
			
			if (null == voucher) {
				resultResponse.setErrorAPIResponse(
						VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getIntId(),
						VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getMsg());
				return resultResponse;
			}
			if (voucher.getType().equalsIgnoreCase(VoucherConstants.DEAL_OFFER)) {
				OfferCatalog offer = offerRepository.findByOfferId(voucher.getOfferInfo().getOffer());
				bis = voucherControllerHelper.generatePDF(voucher,
						offer.getOffer().getOfferTitleDescription().getOfferTitleDescriptionEn());
			} else {
				bis = voucherControllerHelper.generatePDF(voucher, null);
			}

			if (save) {
				Utils.copyFile(bis, pdfLocation + System.currentTimeMillis() + VoucherConstants.UNDERSCORE
						+ voucher.getVoucherCode() + VoucherConstants.PDFCONSTANT);
				VoucherContent voucherContent = new VoucherContent();
				voucherContent.setFilePath(pdfLocation + System.currentTimeMillis() + VoucherConstants.UNDERSCORE
						+ voucher.getVoucherCode() + VoucherConstants.PDFCONSTANT);
				resultResponse.setResult(voucherContent);
			}

			if (isDownload) {

				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition",
						"attachment; filename=" + voucher.getVoucherCode() + VoucherConstants.PDFCONSTANT);
				resultResponse.getApiStatus()
						.setMessage(VoucherManagementCode.VOUCHER_PDF_GENERATED_SUCCESSFULLY.getMsg());
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
						.body(new InputStreamResource(bis));
			}
			resultResponse.getApiStatus().setMessage(VoucherManagementCode.VOUCHER_PDF_GENERATED_SUCCESSFULLY.getMsg());

		} catch (VoucherManagementException vme) {
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_PDF_GENERATION_FAILED.getIntId(),
					VoucherManagementCode.VOUCHER_PDF_GENERATION_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "generatePdf",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		LOG.info("generatePdf response parameters : {}", resultResponse);
		return resultResponse;
	}

	/**
	 * This method uploads a file with list of vouchers
	 * @param file
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * 
	 */
	@PostMapping(value=VoucherRequestMappingConstants.VOUCHER_UPLOAD, consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	public VoucherUploadResponse uploadVoucherFile(@RequestParam(value = "file", required = true) MultipartFile fileReceived,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		VoucherUploadResponse resultResponse = new VoucherUploadResponse(externalTransactionId);
		LOG.info("Voucher upload file:  {}",fileReceived.getContentType());		
        
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		String uploadVoucher = "uploadVoucher";
		List<String> fileContent = new ArrayList<>();
		if (!fileReceived.isEmpty() && fileReceived.getContentType() != null
				&& (fileReceived.getContentType().contains(VoucherConstants.TEXT_CSV) 
						|| fileReceived.getContentType().contains(VoucherConstants.APP_OCTET_STREAM)
						|| fileReceived.getContentType().contains(VoucherConstants.APP_MS_EXCEL))) {
			LOG.info("upload voucher input parameters : {}", fileReceived.getOriginalFilename());
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(fileReceived.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					fileContent.add(line);
				}
				LOG.info("upload voucher file content StandardCharsets : {}", fileContent);
				Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
						channelId, systemId, systemPassword, token, transactionId);
				program = programManagement.getProgramCode(headers.getProgram());
		        headers.setProgram(program);
				resultResponse = voucherControllerHelper.prepareAndUploadVoucherData(fileContent, fileReceived.getOriginalFilename(),
						voucherDomain, resultResponse, headers);

			} catch (ParseException pe) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getIntId(),
						VoucherConstants.ERROR_PARSE_CSV);
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getId(),
						VoucherConstants.ERROR_PARSE_CSV);
				LOG.error(new VoucherManagementException(this.getClass().toString(), uploadVoucher,
						pe.getClass() + pe.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION)
								.printMessage());

			} catch (VoucherManagementException vme) {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getMsg());
				resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
						vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

			} catch (Exception e) {		
				resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
						e.getMessage());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getId(),
						VoucherManagementCode.VOUCHER_UPLOAD_FAILED.getMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), uploadVoucher,
						e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
			}
		} else {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getMsg());
			return resultResponse;
		}
		LOG.info(RESPONSE_LOG, resultResponse);
		return resultResponse;
	}

	/**
	 * This method returns a list of uploaded files for a specified merchant code
	 * @param merchantCode
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @return list of uploaded files
	 */
	@GetMapping(value= VoucherRequestMappingConstants.VOUCHER_LIST)
	public VoucherUploadListResponse listOfUploadedFiles(
			@QueryParam(value = VoucherRequestMappingConstants.MERCHANT_CODE) String merchantCode,
			@QueryParam(value = VoucherRequestMappingConstants.PARTNER_CODE) String partnerCode,
			@RequestParam(value = VoucherRequestMappingConstants.PAGE, required = true) Integer page,
			@RequestParam(value = VoucherRequestMappingConstants.LIMIT, required = false) Integer limit,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName) {
		
		LOG.info("list uploaded files, input parameters : {}", merchantCode);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, null, null, channelId,
				systemId, systemPassword, token, externalTransactionId);
		
		if (null == program) program = defaultProgramCode;
		
		VoucherUploadListResponse resultResponse = new VoucherUploadListResponse(externalTransactionId);
		if(limit==null) {
			limit=Integer.MAX_VALUE;
		}
		Pageable pageNumberWithElements = PageRequest.of(page,limit);
		Page<VoucherUploadFile> output = null;

		if (null != merchantCode) {
			//output = voucherUploadFileRepository.findByMerchantCode(merchantCode, pageNumberWithElements);
			output = voucherUploadFileRepository.findByMerchantCodeAndProgramCodeIgnoreCase(merchantCode, program, pageNumberWithElements);
		} else if (null != partnerCode) {
			List<String> merchantCodes;
			try {
				merchantCodes = voucherService.getMerchantsForPartner(partnerCode, headers, resultResponse);
				if (null != merchantCodes) {
					//output = voucherUploadFileRepository.findByMerchantCodeIn(merchantCodes, pageNumberWithElements);
					output = voucherUploadFileRepository.findByProgramCodeIgnoreCaseAndMerchantCodeIn(merchantCodes, program, pageNumberWithElements);
				}
			} catch (VoucherManagementException e) {
				e.printStackTrace();
			}
		} else {
			//output = voucherUploadFileRepository.findAll(pageNumberWithElements);
			output = voucherUploadFileRepository.findByProgramCodeIgnoreCase(program, pageNumberWithElements);
		}
		
		if(null != output) {
			List<VoucherUploadFile> outputContent =output.getContent().isEmpty()?null:output.getContent();
			
			if (outputContent!=null && !outputContent.isEmpty()) {
				List<VoucherUploadList> resp = outputContent.stream().map(
						e -> new VoucherUploadList(e.getId(), e.getFileName(), e.getUpdatedDate(), e.getHandbackFile() ))
						.collect(Collectors.toList());
				resultResponse.setVoucherUploadListResult(resp);
				resultResponse.setTotalRecords(output.getTotalElements());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
			} else {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_NO_DATA.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_NO_DATA.getMsg());			
			}
		}
		
		LOG.info("list uploaded files, response parameters : {}", resultResponse);
		return resultResponse;
	}
	
	/**
	 * This method returns the detailed content of uploaded file for Vouchers 
	 * @param fileId
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @return detailed information about uploaded file 
	 */
	@GetMapping(value=VoucherRequestMappingConstants.VOUCHER_UPLOAD_CONTENT)
	public VoucherUploadFileContentResponse downloadVoucherFileObj(@PathVariable(value = "fileId") String fileId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("Uploaded file content, input parameters fileId : {}", fileId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		VoucherUploadFileContentResponse resultResponse = new VoucherUploadFileContentResponse(externalTransactionId);
		Optional<VoucherUploadFile> output = voucherUploadFileRepository.findById(fileId);

		if (output.isPresent()) {
			VoucherUploadFileContentResult res = output
					.map(e -> new VoucherUploadFileContentResult(e.getProgramCode(), e.getId(), e.getFileName(),
							e.getMerchantCode(), e.getOfferId(), e.getUploadedDate(), e.getFileType(), e.getFileContent(),
							e.getFileProcessingStatus(), e.getHandbackFile(), e.getUpdatedDate(), e.getUpdatedUser()))
					.get();
			resultResponse.setVoucherUploadResult(res);
			resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
					VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
		} else {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_CONTENT_NO_DATA.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_CONTENT_NO_DATA.getMsg());
		}
		LOG.info("Uploaded file content, input parameters fileId : {}", resultResponse);
		return resultResponse;
	}

	// return type :VoucherUploadFileContentResponse
	/**
	 * This method downloads the handback csv file for the specified uploaded file id
	 * @param fileId
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param response
	 * @throws IOException
	 * @throws ParseException
	 */
	@GetMapping(value=VoucherRequestMappingConstants.VOUCHER_UPLOAD_DOWNLOADFILE)
	public void downloadVoucherFile(@PathVariable(value = "fileId") String fileId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			HttpServletResponse response) throws IOException, ParseException {
		LOG.info("Uploaded file as csv, input parameters fileId : {}", fileId);
		VoucherUploadFileContentResponse resultResponse = new VoucherUploadFileContentResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		Optional<VoucherUploadFile> output = voucherUploadFileRepository.findById(fileId);

		List<String[]> fileContentList = new ArrayList<>();
		List<VoucherUploadDto> res = new ArrayList<>();
		if (output.isPresent()) {
			res = voucherControllerHelper.prepareDataForHandbackDownload(output.get(), fileContentList, res);
		} else {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FILE_CONTENT_NO_DATA.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FILE_CONTENT_NO_DATA.getMsg());
		}

		if (!fileLocation.equalsIgnoreCase("null")) {

			VoucherCsvGenerator gen = new VoucherCsvGenerator();

			try (CSVPrinter pr = gen.generateCsv(res, fileLocation)) {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());

			} catch (IOException e1) {
				resultResponse.addErrorAPIResponse(e1.hashCode(), e1.getMessage());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_DOWNLOAD_CSV_FAILED.getId(),
						VoucherManagementCode.VOUCHER_DOWNLOAD_CSV_FAILED.getMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), "download handback file",
						e1.getClass() + e1.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION)
								.printMessage());

			} catch (Exception e) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
						VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_DOWNLOAD_CSV_FAILED.getId(),
						VoucherManagementCode.VOUCHER_DOWNLOAD_CSV_FAILED.getMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), "download handback file",
						e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());

			}

		}

		else {
			response.setContentType(VoucherConstants.TEXT_CSV);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=handback_" + new Date() + ".csv");

			CSVWriter writer = new CSVWriter(response.getWriter());
			String[] csvHeaders = VoucherConstants.HEADERS;

			writer.writeNext(csvHeaders);
			writer.writeAll(fileContentList);
			writer.close();
			resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
					VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());

		}

	}

	/**
	 * This method generates report for burnt vouchers
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param userRole
	 * @param merchantCode
	 * @param partnerCode
	 * @param storeCode
	 * @param fromDate
	 * @param toDate
	 * @param formatType
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_GENERATE_BURN_REPORT, consumes = MediaType.ALL_VALUE)
	public VoucherResultResponse generateBurnVoucherReport(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestParam(value = VoucherRequestMappingConstants.MERCHANT_CODE, required = false) String merchantCode,
			@RequestParam(value = VoucherRequestMappingConstants.PARTNER_CODE, required = false) String partnerCode,	
			@RequestParam(value = VoucherRequestMappingConstants.STORE_CODE, required = false) String storeCode,
			@RequestParam(value = VoucherRequestMappingConstants.FROM_DATE, required = true) String fromDate,
			@RequestParam(value = VoucherRequestMappingConstants.TO_DATE, required = true) String toDate,
			@RequestParam(value = VoucherRequestMappingConstants.FORMAT_TYPE, required = true) String formatType) {

		LOG.info("Enter generateBurnVoucherReport :: userRole : {}",userRole);
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		String role = "";
		
		if (null == program) program = defaultProgramCode;
		
		LOG.info("burnVoucher report :: User name is {} ", userName);
		String[] userNameList = userName.split(",");
		if(userNameList.length==0) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_USER_NAME.getIntId(),
					VoucherManagementCode.INVALID_USER_NAME.getMsg());
			resultResponse.getApiStatus().setMessage(VoucherManagementCode.INVALID_USER_NAME.getMsg());
			return resultResponse;
		}
		LOG.info("burnVoucher report: User is {}",userNameList[0]);
		if (!formatType.equals(VoucherConstants.CSV) && !formatType.equals(VoucherConstants.PDF)) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_FORMAT_TYPE.getIntId(),
					VoucherManagementCode.INVALID_FORMAT_TYPE.getMsg());
			resultResponse.getApiStatus().setMessage(VoucherManagementCode.INVALID_FORMAT_TYPE.getMsg());
			return resultResponse;

		}
		if(voucherControllerHelper.validateUser(userRole)) {
			LOG.info("generateBurnVoucherReport :: validateUser : {}",userRole);
			role=VoucherConstants.ADMIN;
		}
		if(!role.equals(VoucherConstants.ADMIN) && merchantCode==null && partnerCode==null && storeCode==null) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_SELECTED_TYPE.getIntId(),
					VoucherManagementCode.INVALID_SELECTED_TYPE.getMsg());
			resultResponse.getApiStatus().setMessage(VoucherManagementCode.INVALID_SELECTED_TYPE.getMsg());
			return resultResponse;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if (!voucherControllerHelper.validateBurnReportDates(fromDate, toDate, resultResponse)) {
			return resultResponse;
		}
		try {
			VoucherContent voucherContent = new VoucherContent();
			StringBuilder fileContent = new StringBuilder();
			fileContent.append(
					"voucher code,status,partner code,merchant code,offer id,username,invoice id,voucher amount (AED),generation date,burn date,burned,burn_id,expiry date,expired");
			List<Voucher> vouchersList ;
			Calendar fromDateToPass = Calendar.getInstance();
			fromDateToPass.setTime(format.parse(fromDate));
			fromDateToPass.set(Calendar.HOUR_OF_DAY, 0);  
			fromDateToPass.set(Calendar.MINUTE, 0);  
			fromDateToPass.set(Calendar.SECOND, 0);  
			fromDateToPass.set(Calendar.MILLISECOND, 0); 
			
			Calendar toDateToPass = Calendar.getInstance();
			toDateToPass.setTime(format.parse(toDate));
			toDateToPass.set(Calendar.HOUR_OF_DAY, 23);
			toDateToPass.set(Calendar.MINUTE, 59);
			toDateToPass.set(Calendar.SECOND, 59);
			toDateToPass.set(Calendar.MILLISECOND, 999);
			LOG.info("From date is : {}", fromDateToPass.getTime());
			LOG.info("To date is : {}", toDateToPass.getTime());
			if (role.equalsIgnoreCase(VoucherConstants.ADMIN)) {
//				vouchersList = voucherRepository.findAdminBurntStatusAndDate(VoucherStatus.BURNT,
//						fromDateToPass.getTime(), toDateToPass.getTime());
				vouchersList = voucherRepository.findAdminBurntStatusAndDateAndProgramCodeIgnoreCase(VoucherStatus.BURNT,
						fromDateToPass.getTime(), toDateToPass.getTime(), program);
			} else if (partnerCode != null) {
//				vouchersList = voucherRepository.findByPartnerCodeAndBurntStatusAndDate(partnerCode,
//						VoucherStatus.BURNT, fromDateToPass.getTime(), toDateToPass.getTime());
				vouchersList = voucherRepository.findByPartnerCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(partnerCode,
						VoucherStatus.BURNT, fromDateToPass.getTime(), toDateToPass.getTime(), program);
			} else if (merchantCode != null) {
//				vouchersList = voucherRepository.findByMerchantCodeAndBurntStatusAndDate(merchantCode,
//						VoucherStatus.BURNT, fromDateToPass.getTime(), toDateToPass.getTime());
				vouchersList = voucherRepository.findByMerchantCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(merchantCode,
						VoucherStatus.BURNT, fromDateToPass.getTime(), toDateToPass.getTime(), program);
			} else if (storeCode != null) {
//				vouchersList = voucherRepository.findByStoreCodeAndBurntStatusAndDate(storeCode, VoucherStatus.BURNT,
//						userNameList[0], fromDateToPass.getTime(), toDateToPass.getTime());
				vouchersList = voucherRepository.findByStoreCodeAndBurntStatusAndDateAndProgramCodeIgnoreCase(storeCode, VoucherStatus.BURNT,
						userNameList[0], fromDateToPass.getTime(), toDateToPass.getTime(), program);
			} else {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_SELECTED_TYPE.getIntId(),
						VoucherManagementCode.INVALID_SELECTED_TYPE.getMsg());
				resultResponse.getApiStatus().setMessage(VoucherManagementCode.INVALID_SELECTED_TYPE.getMsg());
				return resultResponse;
			}
			LOG.info("Voucher list:{} ",vouchersList.size());
			voucherControllerHelper.convertBurntVoucherToFileContent(fileContent, vouchersList);
			voucherContent.setContent(fileContent.toString());
			voucherContent.setFileName("voucherList." + formatType);
			voucherContent.setFormatType(formatType);
			resultResponse.setResult(voucherContent);
			resultResponse.getApiStatus()
					.setMessage(VoucherManagementCode.BURNT_VOUCHER_REPORT_GENERATED_SUCCESSFULLY.getMsg());
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.BURNT_VOUCHER_REPORT_GENERATION_FAILED.getIntId(),
					VoucherManagementCode.BURNT_VOUCHER_REPORT_GENERATION_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "generateBurnVoucherReport",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

		LOG.info("generateBurnVoucherReport response parameters : {}", resultResponse.getApiStatus());
		return resultResponse;
	}

	/**
	 * This method lists vouchers for a partner code
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param partnerCode
	 * @param merchantCode
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_PARTNERCODE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersByPartnerAndMerchantCode(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestParam(value = VoucherRequestMappingConstants.PARTNER_CODE, required = false) String partnerCode,
			@RequestParam(value = VoucherRequestMappingConstants.MERCHANT_CODE, required = false) String merchantCode) {

		LOG.info("Entering listVouchersByPartnerAndMerchantCode");
		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);		
		try {
			voucherListResponse.setResult(voucherControllerHelper.listVouchersByPartnerMerchant(partnerCode, merchantCode, headers, voucherListResponse));
		} catch (Exception e) {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info("Leaving listVouchersByPartnerAndMerchantCode");
		
		return voucherListResponse;

	}
	
	
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_VOUCHERCODELIST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse listVouchersByVoucherCodeList(@RequestBody VoucherCodesDto voucherCodesDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("Entering listVouchersByVoucherCodeList : {}", voucherCodesDto);
		VoucherResultResponse voucherListResponse = new VoucherResultResponse(externalTransactionId);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);		
		try {
			voucherListResponse.setResult(voucherControllerHelper.listVouchersByVoucherCodes(voucherCodesDto.getVoucherCodes(), headers, voucherListResponse));
		} catch (Exception e) {
			voucherListResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}

		LOG.info("Leaving listVouchersByVoucherCodeList");
		
		return voucherListResponse;

	}

	/**
	 * This method runs voucher reconciliation process for MAF with fixed set of parameters
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_RECONCILIATION_FIXED_PARAMS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherReconciliationDtoResponse reconciliationProcessFixedTime(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {
		VoucherReconciliationDtoResponse voucherReconciliationResponse = new VoucherReconciliationDtoResponse(
				externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		SimpleDateFormat requestedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String currDate = requestedDateFormat.format(new Date());
		try {
			cal.setTime(requestedDateFormat.parse(currDate));			
		} catch (ParseException e) {			
			voucherReconciliationResponse.addErrorAPIResponse(
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
			return voucherReconciliationResponse;
		}
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		String dateToPass =requestedDateFormat.format(cal.getTime());
		return this.reconciliationProcess(program, authorization, externalTransactionId, channelId, systemId,
				systemPassword, token, userName, transactionId, sessionId, userPrev, dateToPass,
				RECONCILE_PAGE, RECONCILE_LIMIT);
	}

	/**
	 * This method runs voucher reconciliation process for MAF
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param fromDate
	 * @param toDate
	 * @param page
	 * @param limit
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_RECONCILIATION, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherReconciliationDtoResponse reconciliationProcess(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestParam(value = VoucherRequestMappingConstants.DATE, required = true) String date,
			@RequestParam(value = VoucherRequestMappingConstants.PAGE, required = true) Integer page,
			@RequestParam(value = VoucherRequestMappingConstants.LIMIT, required = true) Integer limit) {

		LOG.info("reconciliationProcess input parameters : {}, {}, {} ", date, page, limit);
		VoucherReconciliationDtoResponse voucherReconciliationResponse = new VoucherReconciliationDtoResponse(
				externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		VoucherReconciliationResponse mafResponse;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {				
			format.setLenient(false);
			format.parse(date);
			mafResponse = mafService.getMafVoucherReconciliation(date, page, limit);
		} catch (ParseException e2) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.INVALID_DATE_FORMAT.getId(),
					VoucherManagementCode.INVALID_DATE_FORMAT.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_DATE_FORMAT.getIntId(),
					VoucherManagementCode.INVALID_DATE_FORMAT.getMsg());
			LOG.error(e2.getMessage());
			return voucherReconciliationResponse;

		}	catch (VoucherManagementException e) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.MAF_EXTERNAL_EXCEPTION.getId(),
					VoucherManagementCode.MAF_EXTERNAL_EXCEPTION.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(VoucherManagementCode.MAF_EXTERNAL_EXCEPTION.getIntId(),
					VoucherManagementCode.MAF_EXTERNAL_EXCEPTION.getMsg());
			return voucherReconciliationResponse;
		} catch (Exception e) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getId(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
			return voucherReconciliationResponse;
		}

		try {
			if (mafResponse.getAckMessage().getStatus().equalsIgnoreCase(VoucherConstants.SUCCESS)) {		
				voucherControllerHelper.processReconciliation(mafResponse, format, date, userName, program, externalTransactionId,voucherReconciliationResponse );
			} else {
				voucherReconciliationResponse.setResult(VoucherManagementCode.MAF_SERVICE_ERROR.getId(),
						VoucherManagementCode.MAF_SERVICE_ERROR.getMsg());
				voucherReconciliationResponse.addErrorAPIResponse(VoucherManagementCode.MAF_SERVICE_ERROR.getIntId(),
						VoucherManagementCode.MAF_SERVICE_ERROR.getMsg());				
			}
			return voucherReconciliationResponse;
		}

		catch (ParseException e1) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getId(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(e1.getErrorOffset(), e1.getMessage());
			LOG.error(e1.getMessage());
			return voucherReconciliationResponse;
		} catch (VoucherManagementException e) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getId(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), e.getMethodName(),
					e.getClass() + e.getMessage(), e.getErrorCodeInt(), e.getErrorMsg()).printMessage());
			return voucherReconciliationResponse;
		} catch (Exception e) {
			voucherReconciliationResponse.setResult(VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getId(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL.getMsg());
			voucherReconciliationResponse.addErrorAPIResponse(
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
			return voucherReconciliationResponse;
		}

	}

	/**
	 * This method sends notification for vouchers that are expiring soon
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_EXPIRY_NOTIFICATION, consumes = MediaType.ALL_VALUE)
	public ResultResponse voucherExpiryNotification(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_EXPIRY);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		resultResponse = voucherControllerHelper.notifyExpiredVouchers(resultResponse, headers);

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_EXPIRY);

		return resultResponse;

	}

	/**
	 * This method provides the number of vouchers sold for customer tiers : rewards, gold, silver and co-branded card.
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_STATISTICS_NOTIFICATION, consumes = MediaType.ALL_VALUE)
	public ResultResponse voucherStatisticsNotification(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_STATISTICS);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		resultResponse = voucherControllerHelper.notifyVoucherStatistics(resultResponse, headers);

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_STATISTICS);

		return resultResponse;
	}

	/**
	 * This method checks if available voucher count is less than 15% or 0
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_COUNT_NOTIFICATION, consumes = MediaType.ALL_VALUE)
	public ResultResponse voucherCountNotification(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info(VoucherConstants.LOG_ENTERING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_COUNT);

		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		resultResponse = voucherControllerHelper.notifyVoucherCount(resultResponse, headers);

		LOG.info(VoucherConstants.LOG_LEAVING + VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME,
				this.getClass().getSimpleName(), VoucherConstants.CONTROLLER_VOUCHER_COUNT);

		return resultResponse;
	}

	/**
	 * This method uploads a file with list of account, membershipcode, loyalty id and generates free voucher for each account
	 * @param file
	 * @param offerId
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@PostMapping(value = "/freeVoucher", consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	public VoucherUploadResponse uploadAccountFreeVoucherFile(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = VoucherRequestMappingConstants.OFFER_ID, required = true) String offerId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {
		
		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + " request : {} ", this.getClass(),
				VoucherConstants.VOUCHER_FREE_GENERATE, offerId);
		LOG.info("Upload free voucher file:  {}",file.getContentType());
		VoucherUploadResponse resultResponse = new VoucherUploadResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		String uploadVoucher = "uploadAccountFreeVoucher";
		List<String> fileContent = new ArrayList<>();

		if (file != null && !file.isEmpty() && file.getContentType() != null
				&& (file.getContentType().contains(VoucherConstants.TEXT_CSV) 
						|| file.getContentType().contains(VoucherConstants.APP_OCTET_STREAM)
						|| file.getContentType().contains(VoucherConstants.APP_MS_EXCEL))) {
			LOG.info("upload Account free voucher input parameters : {}", file.getOriginalFilename());
			try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {
					fileContent.add(line);
				}
				LOG.info("upload free voucher file content : {}", fileContent);
				Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
						channelId, systemId, systemPassword, token, transactionId);
				program = programManagement.getProgramCode(headers.getProgram());
		        headers.setProgram(program);
		        
		        resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
				voucherControllerHelper.prepareAndGenerateFreeVoucher(file, fileContent, offerId, resultResponse, headers);
			} catch (VoucherManagementException vme) {
				resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
						VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
				resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
						vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());				
			} catch (Exception e) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
						e.getMessage());
				resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
						VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
				LOG.error(new VoucherManagementException(this.getClass().toString(), uploadVoucher,
						e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
			}
		} else {
			resultResponse.setResult(VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getId(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED_INVALID_FILE.getMsg());
			return resultResponse;
		}
		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME, this.getClass(),
				VoucherConstants.VOUCHER_FREE_GENERATE, resultResponse);

		return resultResponse;
	}
	
		/**
	 * This method lists the files uploaded for free voucher generation for the specified offer id
	 * @param offerId
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_LIST_BY_OFFER_ID)
	public VoucherUploadListResponse listOfUploadedFilesByOfferId(@PathVariable(value = "offerId") String offerId,
				@RequestParam(value = VoucherRequestMappingConstants.PAGE, required = true) Integer page,
				@RequestParam(value = VoucherRequestMappingConstants.LIMIT, required = false) Integer limit,
				@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
				@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
				@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
				@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
				@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
				@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
				@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
				@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName) {
			LOG.info("list uploaded files by offer id, input parameters : {}", offerId);
			VoucherUploadListResponse resultResponse = new VoucherUploadListResponse(externalTransactionId);
			
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			if(limit==null) {
				limit=Integer.MAX_VALUE;
			}
			Pageable pageNumberWithElements = PageRequest.of(page,limit);			
			Page<VoucherUploadFile> output = voucherUploadFileRepository.findByOfferId(offerId,pageNumberWithElements);
			List<VoucherUploadFile> outputContent =output.getContent().isEmpty()?null:output.getContent();
			
			if (outputContent!=null && !outputContent.isEmpty()) {
				List<VoucherUploadList> resp = outputContent.stream().map(
						e -> new VoucherUploadList(e.getId(), e.getFileName(), e.getUpdatedDate(), e.getHandbackFile()))
						.collect(Collectors.toList());
				resultResponse.setVoucherUploadListResult(resp);
				resultResponse.setTotalRecords(output.getTotalElements());
				resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
			} else {
				resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_NO_DATA.getId(),
						VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES_NO_DATA.getMsg());
			}
			LOG.info("list uploaded files by offer id, response parameters : {}", resultResponse);
			return resultResponse;
		}

	/**
	 * This method gives a detailed information about the voucher gifted for the specified gift id
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @param giftId
	 * 
	 */
	@GetMapping(value = VoucherRequestMappingConstants.GIFT_VOUCHER_DETAILS, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public VoucherGiftDetailsResponse listVoucherGiftDetails(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@PathVariable(value = VoucherRequestMappingConstants.VOUCHER_GIFT_ID, required = true) String giftId) {

		LOG.info("listVoucherGiftDetails input parameters : {}", giftId);
		VoucherGiftDetailsResponse voucherGiftResponse = new VoucherGiftDetailsResponse(externalTransactionId);
		
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			GiftingHistoryResult voucherGiftResult= voucherControllerHelper.getVoucherGiftDetails(null != userName ? userName : token, giftId, externalTransactionId);			
			voucherGiftResponse.setVoucherGiftResult(voucherGiftResult);	
			voucherGiftResponse.setResult(VoucherManagementCode.VOUCHER_GIFT_DETAILS_SUCCESS.getId(),
					VoucherManagementCode.VOUCHER_GIFT_DETAILS_SUCCESS.getMsg());
		} catch (VoucherManagementException ve) {
			voucherGiftResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_GIFT_NOT_PRESENT.getIntId(),
					VoucherManagementCode.VOUCHER_GIFT_NOT_PRESENT.getMsg());
			LOG.error(ve.getMessage());
		} catch (Exception e) {
			voucherGiftResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}
		LOG.info("listVoucherGiftDetails response parameters: {}", voucherGiftResponse);
		return voucherGiftResponse;

	}

	/**
	 * This method lists accounts that have active vouchers associated with them
	 * @param accountsDto
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * @return list of accounts
	 */
	@PostMapping(value = VoucherRequestMappingConstants.ACCOUNT_WITH_ACTIVE_VOUCHER, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ListAccountWithActiveVouchersResponse listAccountWithActiveVouchers(@RequestBody AccountsDto accountsRequestDto,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info("listAccountWithActiveVouchers input parameters : {}", accountsRequestDto);
		
		ListAccountWithActiveVouchersResponse accountWithActiveVouchersResponse = new ListAccountWithActiveVouchersResponse(externalTransactionId);
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			List<AccountWithActiveVouchersResult> accountWithActiveVouchersResult = new ArrayList<>();
						
			List<Voucher> voucherList = voucherRepositoryHelper.findVoucherForAccountList(accountsRequestDto.getAccountNumber());
			for(String accountNumber : accountsRequestDto.getAccountNumber()) {
				AccountWithActiveVouchersResult accountWithActiveVouchers =  new AccountWithActiveVouchersResult();
				
				LOG.info("Request Account Number : {}", accountNumber);								
				accountWithActiveVouchers.setAccountNumber(accountNumber);
				accountWithActiveVouchers.setActive(null != voucherList && voucherList.stream().anyMatch(o -> o.getAccountNumber().equals(accountNumber)));
				
				accountWithActiveVouchersResult.add(accountWithActiveVouchers);
			}

			accountWithActiveVouchersResponse.getResult().setAccountWithActiveVouchers(accountWithActiveVouchersResult);

		} catch (Exception e) {
			accountWithActiveVouchersResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			LOG.error(e.getMessage());
		}
		LOG.info("listAccountWithActiveVouchers response parameters: {}", accountWithActiveVouchersResponse);
		return accountWithActiveVouchersResponse;
	}
	
	@GetMapping(value = VoucherRequestMappingConstants.CARREFOUR_FAILURE_ALERT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse carrefourFailureAlert(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME, this.getClass(),
				VoucherConstants.CONTROLLER_CARREFOUR_FAILURE_ALERT);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		int failureCount = voucherControllerHelper.carrefourExpiryAlertHelper(resultResponse);
		LOG.info("carrefourFailureAlert :: failureCount : {}",failureCount);
		if(failureCount > 0) {
			Map<String, String> additionalParameters = new HashMap<>();
			additionalParameters.put("V_CRFR", String.valueOf(failureCount));
			SMSRequestDto smsEvent = voucherControllerHelper.createSmsEvent(externalTransactionId, "132", "132", "00",
					additionalParameters, "en", crfrAlertdestinationNumbers);
			eventHandler.publishAlertSms(smsEvent);
		}
		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME, this.getClass(),
				VoucherConstants.CONTROLLER_CARREFOUR_FAILURE_ALERT, resultResponse);

		resultResponse.setResult(VoucherManagementCode.CRFR_ALERT_SUCCESS.getId(),
				VoucherManagementCode.CRFR_ALERT_SUCCESS.getMsg());
		return resultResponse;

	}

	@GetMapping(value = VoucherRequestMappingConstants.YGAG_FAILURE_ALERT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultResponse ygagFailureAlert(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {

		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME, this.getClass(),
				VoucherConstants.CONTROLLER_YGAG_FAILURE_ALERT);
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		int failureCount = voucherControllerHelper.ygagExpiryAlertHelper(resultResponse);
		LOG.info("ygagFailureAlert :: failureCount : {}", failureCount);
		if (failureCount > 0) {
			Map<String, String> additionalParameters = new HashMap<>();
			additionalParameters.put("V_YGAG", String.valueOf(failureCount));
			SMSRequestDto smsEvent = voucherControllerHelper.createSmsEvent(externalTransactionId, "131", "131", "00",
					additionalParameters, "en", ygagAlertdestinationNumbers);
			eventHandler.publishAlertSms(smsEvent);
		}
		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME, this.getClass(),
				VoucherConstants.CONTROLLER_CARREFOUR_FAILURE_ALERT, resultResponse);

		resultResponse.setResult(VoucherManagementCode.YGAG_ALERT_SUCCESS.getId(),
				VoucherManagementCode.YGAG_ALERT_SUCCESS.getMsg());
		return resultResponse;

	}

	@PostMapping(value = "/updateExpiredVouchers", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateExpiredVouchers(
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {
		
		LOG.info("Enter updateExpiredVouchers");
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			LOG.info("voucherControllerHelper :: updateExpiredVouchers");
			UpdateResult updateResult = voucherRepositoryHelper.updateExpiredVouchers(Utilities.setUtcTimeZone(new Date(),OfferConstants.DATE_FORMAT.get()));
			LOG.info("voucherControllerHelper :: UpdateResult : {}", updateResult);
		} catch (Exception e) {
			LOG.error(new VoucherManagementException(this.getClass().toString(), "updateExpiredVouchers",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}		
		
		LOG.info("Exit updateExpiredVouchers");
	}
	
	/**
	 * This method sets the status of specified mamba vouchers to burnt
	 * @param burnVoucherRequest
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 */
	@PostMapping(value = VoucherRequestMappingConstants.BURN_CASH_VOUCHER, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse burnCashVouchers(@RequestBody BurnCashVoucherRequest burnVoucherRequest,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("burnCashVouchers input parameters : {}", burnVoucherRequest);
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			List<BurnVoucher> burnVoucherList = new ArrayList<>();
			if (!VoucherValidator.validateVoucherDto(burnVoucherRequest, validator, resultResponse)) {
				return resultResponse;
			}
			for(BurnCashVoucher burnCashVoucher : burnVoucherRequest.getBurnCashVoucher()) {
				if (!VoucherValidator.validateVoucherDto(burnCashVoucher, validator, resultResponse)) {
					return resultResponse;
				}
			}
			
			LOG.info("burnVoucher :: User name is {} ",userName);			
			String[] userNameList = userName.split(",");
			if(userNameList.length==0) {
				resultResponse.addErrorAPIResponse(VoucherManagementCode.INVALID_USER_NAME.getIntId(),
						VoucherManagementCode.INVALID_USER_NAME.getMsg());
				return resultResponse;
			}
			
			
			Calendar dateToday = Calendar.getInstance();  
            dateToday.setTime(new Date());  
            dateToday.set(Calendar.HOUR_OF_DAY, 0);  
            dateToday.set(Calendar.MINUTE, 0);  
            dateToday.set(Calendar.SECOND, 0);  
            dateToday.set(Calendar.MILLISECOND, 0);
            for(BurnCashVoucher burnCashVoucher : burnVoucherRequest.getBurnCashVoucher()) {
				Voucher voucher = voucherRepository.findByCodeAndStatusAndDate(burnCashVoucher.getVoucherCode(), VoucherStatus.ACTIVE, dateToday.getTime());
				
				DecimalFormat df = new DecimalFormat("###.##");

				if (null == voucher) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getIntId(),
							burnCashVoucher.getVoucherCode() + VoucherConstants.MESSAGE_SEPARATOR
									+ VoucherManagementCode.VOUCHERS_NOT_AVAILABLE_OR_NOT_ACTIVE.getMsg());
					continue;
					
				} else if (voucher.getExpiryDate().before(dateToday.getTime())) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_EXPIRED.getIntId(), burnCashVoucher.getVoucherCode()
							+ VoucherConstants.MESSAGE_SEPARATOR + VoucherManagementCode.VOUCHER_EXPIRED.getMsg());
					continue;
					
				}
				else if(Double.parseDouble(df.format(burnCashVoucher.getAmountToDeduct()))> Double.parseDouble(df.format(voucher.getVoucherBalance()))) {
					resultResponse.addErrorAPIResponse(VoucherManagementCode.VOUCHER_LESS_BALANCE.getIntId(), burnCashVoucher.getVoucherCode()
							+ VoucherConstants.MESSAGE_SEPARATOR + VoucherManagementCode.VOUCHER_LESS_BALANCE.getMsg()+ " "+voucher.getVoucherBalance());
					continue;
				}
				
				BurnVoucher burnVoucher = new BurnVoucher();
				voucherDomain.burnCashVoucher(voucher, burnCashVoucher, userNameList[0], burnVoucher, externalTransactionId);
				burnVoucherList.add(burnVoucher);
            }
			if (!burnVoucherList.isEmpty()) {
				resultResponse.setResult(burnVoucherList);
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setMessage(VoucherManagementCode.VOUCHER_BURN_SUCCESSFULLY.getMsg());
			}
            
		} catch (VoucherManagementException vme) {
			vme.printStackTrace();
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "cancelVoucher",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("burnVouchers response parameters : {}", resultResponse);
		return resultResponse;

	}
	
	
	@PostMapping(value = VoucherRequestMappingConstants.VOUCHER_BURN_ROLLBACK, consumes = MediaType.APPLICATION_JSON_VALUE)
	public VoucherResultResponse rollBackVoucherBurn(@RequestBody RollBackVoucherBurnRequest rollBackVoucherBurnRequest,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("rollBackVoucherBurn input parameters : {}", rollBackVoucherBurnRequest);
		VoucherResultResponse resultResponse = new VoucherResultResponse(externalTransactionId);
		BurnVoucher burnVoucher = new BurnVoucher();
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
		
		try {
			if(!VoucherValidator.validateVoucherDto(rollBackVoucherBurnRequest, validator, resultResponse)) {
				return resultResponse;
			}
						
			if(voucherControllerHelper.rollBackVoucherBurn(rollBackVoucherBurnRequest, headers, burnVoucher, resultResponse)) {
				List<BurnVoucher> burnVoucherList = new ArrayList<>();
				burnVoucherList.add(burnVoucher);
				
				resultResponse.setResult(burnVoucherList);
				resultResponse.setSuccessAPIResponse();
				resultResponse.getApiStatus().setMessage(VoucherManagementCode.VOUCHER_BURN_ROLLBACK_SUCCESSFUL.getMsg());			
			}
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "rollBackVoucherBurn",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		LOG.info("rollBackVoucherBurn response parameters : {}", resultResponse);
		return resultResponse;
	}
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param userRole
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @return list of handback files for free vouchers uploaded between start date and end date
	 */
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_UPLOAD_HANDBACK_LIST)
	public FreeVoucherUploadHandbackFileResponse freeVoucherUploadHandbackFileList(@RequestParam(value = VoucherRequestMappingConstants.START_DATE, required = true) String startDate,
			@RequestParam(value = VoucherRequestMappingConstants.END_DATE, required = true) String endDate,
			@RequestParam(value = VoucherRequestMappingConstants.OFFER_ID, required = false) String offerId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("freeVoucherUploadHandbackFileList input parameters : startDate - {}, endDate - {}, offerId - {}", startDate, endDate, offerId);
		FreeVoucherUploadHandbackFileResponse resultResponse = new FreeVoucherUploadHandbackFileResponse(externalTransactionId);
		
		try {
			if (StringUtils.isEmpty(program)) {program = defaultProgramCode;}
			voucherUploadFileDomain.getFreeVoucherUploadHandbackFileList(startDate, endDate, offerId, program, resultResponse);
			
		} catch (VoucherManagementException vme) {
			vme.printStackTrace();
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "freeVoucherUploadHandbackFileList",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
	}
	
	@GetMapping(value = VoucherRequestMappingConstants.VOUCHER_UPLOAD_HANDBACK_SINGLE)
	public FreeVoucherUploadHandbackFileResponse freeVoucherUploadSpecificHandbackFile(
			@RequestParam(value = VoucherRequestMappingConstants.ID, required = false) String id,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.USER_ROLE, required = false) String userRole,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId) {

		LOG.info("freeVoucherUploadSpecificHandbackFile input parameters : id - {}", id);
		FreeVoucherUploadHandbackFileResponse resultResponse = new FreeVoucherUploadHandbackFileResponse(externalTransactionId);
		
		try {
			if (StringUtils.isEmpty(program)) {program = defaultProgramCode;}
			voucherUploadFileDomain.getFreeVoucherUploadSpecificHandbackFile(id, program, resultResponse);
			
		} catch (VoucherManagementException vme) {
			vme.printStackTrace();
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getDetailMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());

		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new VoucherManagementException(this.getClass().toString(), "freeVoucherUploadSpecificHandbackFile",
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
	}
	
	
	/**
	 * This method uploads a file with list of account, membershipcode, loyalty id and generates free voucher for each account
	 * @param file
	 * @param offerId
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param userName
	 * @param transactionId
	 * @param sessionId
	 * @param userPrev
	 * 
	 */
	@PostMapping(value = VoucherRequestMappingConstants.ACCOUNT_UPLOAD_FREE_VOUCHER, consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	public VoucherUploadResponse uploadAccountFreeVoucher(@RequestParam(value = VoucherRequestMappingConstants.FILE, required = true) MultipartFile file,
			@RequestParam(value = VoucherRequestMappingConstants.OFFER_ID, required = true) String offerId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev) {
		
		LOG.info(VoucherConstants.CLASS_NAME + VoucherConstants.METHOD_NAME + " request : {} ", this.getClass(),
				VoucherConstants.VOUCHER_FREE_GENERATE, offerId);
		LOG.info("Upload free voucher file:  {}",file.getContentType());
		VoucherUploadResponse resultResponse = new VoucherUploadResponse(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
				
		String uploadVoucher = "uploadAccountFreeVoucher";
				
		try {
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
					channelId, systemId, systemPassword, token, transactionId);
			program = programManagement.getProgramCode(headers.getProgram());
	        headers.setProgram(program);
	        
			if(voucherControllerHelper.validateFreeVoucherUploadFile(file, resultResponse)) {
				
				OfferCatalog offerDetails = offerRepository.findByOfferIdStatusFree(offerId, OfferConstants.ACTIVE_STATUS.get());
				
				if(voucherControllerHelper.validateOfferForFreeVoucherGifting(offerDetails, resultResponse)) {
					
					List<FileContentDto> fileContentList = voucherControllerHelper.readDataFromVoucherUploadFile(file, resultResponse);
					LOG.info("fileContentList : {}", fileContentList);
					
					if(Checks.checkNoErrors(resultResponse)) {
						
						VoucherUploadFile uploadedFile = voucherControllerHelper.saveFileContentsForFreeVoucherUpload(file, fileContentList, offerDetails, headers, resultResponse);
						resultResponse.setResult(VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getId(),
								VoucherManagementCode.VOUCHER_LIST_UPLOAD_FILES.getMsg());
						voucherControllerHelper.giftFreeVoucherToEligibleAccounts(fileContentList, offerDetails, headers, uploadedFile, resultResponse);
					
					} 
					
				} 
				
			} 
			
		} catch (VoucherManagementException vme) {
			
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(vme.getErrorCodeInt(), vme.getErrorMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), vme.getMethodName(),
					vme.getClass() + vme.getMessage(), vme.getErrorCodeInt(), vme.getErrorMsg()).printMessage());				
		
		} catch (Exception e) {
		
			resultResponse.addErrorAPIResponse(VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getId(),
					VoucherManagementCode.ACCOUNT_UPLOAD_FREE_VOUCHER_FAILED.getMsg());
			LOG.error(new VoucherManagementException(this.getClass().toString(), uploadVoucher,
					e.getClass() + e.getMessage(), VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		
		}
		
		return resultResponse;
	}


}