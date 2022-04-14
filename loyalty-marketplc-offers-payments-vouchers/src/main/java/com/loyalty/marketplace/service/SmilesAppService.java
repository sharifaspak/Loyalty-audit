package com.loyalty.marketplace.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.banners.outbound.service.BannerService;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.domain.model.marketplace.RefundTransactionDomain;
import com.loyalty.marketplace.payment.outbound.database.entity.RefundTransaction;
import com.loyalty.marketplace.service.dto.BillsAndRechargesDto;
import com.loyalty.marketplace.service.dto.BillsListDto;
import com.loyalty.marketplace.service.dto.CreditCardDetailDto;
import com.loyalty.marketplace.service.dto.PaymentReversalListRequest;
import com.loyalty.marketplace.service.dto.PaymentReversalRequetsDto;
import com.loyalty.marketplace.service.dto.PaymentReversalResponseDto;
import com.loyalty.marketplace.service.dto.RechargesListDto;
import com.loyalty.marketplace.service.dto.SelectedItemDto;
import com.loyalty.marketplace.service.dto.SelectedPaymentItemBillRechargeDto;
import com.loyalty.marketplace.service.dto.SelectedPaymentOptionFullCCDto;
import com.loyalty.marketplace.service.dto.SelectedPaymentOptionPartialPointCCDto;
import com.loyalty.marketplace.service.dto.SmilesPaymentReversalListRequestDto;
import com.loyalty.marketplace.utils.Utils;

@Component
public class SmilesAppService {
	private static final Logger LOG = LoggerFactory.getLogger(SmilesAppService.class);
	private static final String CUSTOM_HEADER = "CUSTOM_HEADER";
	private static final String TRANSACTION_ID = "transactionId";
	private static final String TOKEN = "token";
	private static final String PAYMENT_REVERSAL = "PaymentReversal";
	private static final String SUCCESS = "SUCCESS";
	private static final String BILL_AND_RECHARGE = "billsAndRecharges";
	private static final String SUBSCRIPTION_SELECTEDITEM = "lifestyle";

	@Value("${smiles.payment.reversal.api}")
	private String smilesPaymentReversalAPI;
	
	@Value("${smiles.payment.reversal.request.subscription.transactionDescription}")
	private String subscriptionDescription;
	
	@Value("${smiles.payment.reversal.customHeaderValue}")
	private String customHeaderValue;
	
	@Autowired
	BannerService bannerService;
	
	@Autowired
	RetryTemplate retryTemplate;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EventHandler eventHandler;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RefundTransactionDomain refundDomain;
	
	@Autowired
	ExceptionLogsService exceptionLogService;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	MongoOperations mongoOperations;
	
	@Async(MarketplaceConfigurationConstants.THREAD_POOL_EXECUTOR_REFUND)
	public ResultResponse smilesPaymentReversal(List<PaymentReversalRequetsDto> paymentRequestDto, boolean retryingFlag, Headers header) {
		LOG.info("Inside SmilesPaymentReversal of class {}", this.getClass().getName());
		
		ResultResponse resultResponse = new ResultResponse(header.getExternalTransactionId());
		PaymentReversalListRequest request = getPaymentReversalRequest(paymentRequestDto);
		try {
			String token = bannerService.fetchBannerToken(resultResponse, header);
			PaymentReversalResponseDto response = callSmilesPaymentReversalAPI(token, request, resultResponse, header);
			if(!retryingFlag) {
				saveNewRefundTransaction(paymentRequestDto.get(0).getPurchaseRequestDto(), response, paymentRequestDto.get(0).getAccountType(), header);
			} else {
				refundDomain.updateRetriedRefundTransaction(response, header);
			}
		} catch (Exception e) {
			LOG.error("Inside SmilesPaymentReversal Exception occured: {}", e.getMessage());
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName());
			LOG.info("Exception Saved to ExceptionLogs collection");
			e.printStackTrace();
		}
		return resultResponse;
	}
	
	public PaymentReversalResponseDto callSmilesPaymentReversalAPI(String token,  PaymentReversalListRequest request, ResultResponse resultResponse, Headers header) {
		
		LOG.info("Inside CallSmilesPaymentReversalAPI in class {}", this.getClass().getName());
		HttpEntity<?> requestEntity = new HttpEntity<>(request, getsmilesHeaders(header, token));
		LOG.info("URL : {}", smilesPaymentReversalAPI);
		LOG.info("REQUEST : {}", requestEntity);
		PaymentReversalResponseDto response = null;
			try {
			long start = System.currentTimeMillis();
			ResponseEntity<PaymentReversalResponseDto> responseEntity = smilesPaymentReversalWithRetry(smilesPaymentReversalAPI, requestEntity);
			long end = System.currentTimeMillis();
			LOG.info("RESPONSE ENTITY : {}", responseEntity);
			LOG.info("RESPONSE BODY : {}", responseEntity.getBody());
			response = modelMapper.map(responseEntity.getBody(), PaymentReversalResponseDto.class);
			boolean isSuccess = false;
			if( !ObjectUtils.isEmpty(response) && (response.getResponseMsg()).equalsIgnoreCase(SUCCESS)) {
				isSuccess = true;
				resultResponse.setSuccessAPIResponse();
			} else {
				resultResponse.setErrorAPIResponse(MarketplaceCode.SMILES_PAYMENT_REVERSAL_API_FAILED.getIntId(), MarketplaceCode.SMILES_PAYMENT_REVERSAL_API_FAILED.getMsg());
			}
	
			eventHandler.publishServiceCallLogs(Utils.createServiceCallLogsDto(header, PAYMENT_REVERSAL, end-start, true, isSuccess, requestEntity,
					!ObjectUtils.isEmpty(responseEntity) ? responseEntity.getBody() : null));
			return response;
			
		} catch(Exception e) {
			LOG.error("Inside callSmilesPaymentReversalAPI Exception occures: {}", e.getMessage());
			exceptionLogService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null, header.getUserName());
			LOG.info("Exception Saved to ExceptionLogs collection");
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 
	 * @param purchaseRequestDto
	 * @param accountType
	 * @param header
	 * @return PaymentReversalRequest
	 */
	public PaymentReversalListRequest getPaymentReversalRequest(List<PaymentReversalRequetsDto> purchaseRequestDto) {
		LOG.info("Inside getPaymentReversalRequest");  
		PaymentReversalListRequest reversalRequest = new PaymentReversalListRequest();
		List<SmilesPaymentReversalListRequestDto> reversalRequestList = new ArrayList<>();
		
		for(PaymentReversalRequetsDto purchaserequest : purchaseRequestDto) {
			SmilesPaymentReversalListRequestDto request = createPaymmentReversalRequest(purchaserequest);
			reversalRequestList.add(request);
		}
		reversalRequest.setPayment(reversalRequestList);
		LOG.info("Inside getPaymentReversalRequest, request : {}", reversalRequest);
        return reversalRequest;
    }
	
	/**
	 * 
	 * @param header
	 * @param token
	 * @return header for rest call
	 */
	public HttpHeaders getsmilesHeaders(Headers header, String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		if(!ObjectUtils.isEmpty(header)){
			headers.set(CUSTOM_HEADER, customHeaderValue);
			headers.set(TRANSACTION_ID, header.getExternalTransactionId());
		}
		headers.set(TOKEN, token);
		return headers;
	}
	/**
	 * 
	 * @param url
	 * @param requestEntity
	 * @return responseEntity
	 */
	private ResponseEntity<PaymentReversalResponseDto> smilesPaymentReversalWithRetry(String url, HttpEntity<?> requestEntity) {
		return retryTemplate.execute(context -> {
			LOG.info("inside Retry block using retryTemplate of createNewBannerWithRetry in class {}", this.getClass().getName());
			return restTemplate.exchange(url, HttpMethod.POST, requestEntity, PaymentReversalResponseDto.class);
		});
	}
	
	public void saveNewRefundTransaction(PurchaseRequestDto request, PaymentReversalResponseDto response, String accountType, Headers header) {
		String voucherDenomination = null != request.getVoucherDenomination() ? request.getVoucherDenomination().toString() : null;
		RefundTransactionDomain refundTransactionDomain = new RefundTransactionDomain.RefundTransactionDomainBuilder(request.getExtTransactionId(),
				request.getAccountNumber(), request.getSpentAmount().toString(), request.getEpgTransactionId(),
				null != response ? SmilesAppServiceHelper.getRefundStatus(response) : null,
				null != response ? SmilesAppServiceHelper.getResponseMessage(response) : null,
				null != response ? SmilesAppServiceHelper.getResponseCode(response) : null,
				null != response ? SmilesAppServiceHelper.getEpgReversalDesc(response) : null,
				null != response ? SmilesAppServiceHelper.getEpgReversalCode(response) : null,
				null != response ? SmilesAppServiceHelper.getEpgRefundCode(response) : null,
				null != response ? SmilesAppServiceHelper.getEpgRefundMsg(response) : null,
				null != response ? SmilesAppServiceHelper.getErrorMessage(response) : "NA")
				.accountType(accountType).authorizationCode(request.getAuthorizationCode()).cardNumber(request.getCardNumber())
				.cardSubType(request.getCardSubType()).cardToken(request.getCardToken()).cardType(request.getCardType())
				.createdDate(new Date()).createdUser(header.getUserName()).dirhamValue(request.getSpentAmount().toString())
				.externalTransactionId(header.getExternalTransactionId())
				.isNotificationTrigger(null != response ? SmilesAppServiceHelper.isNotificationTrigger(response) : null)
				.language(request.getUiLanguage()).msisdn(request.getMsisdn()).offerId(request.getOfferId())
				.orderId(request.getOrderId()).programCode(header.getProgram()).quantity(request.getCouponQuantity())
				.selectedPaymentItem(request.getSelectedPaymentItem()).selectedPaymentOption(request.getSelectedOption())
				.voucherDenomination(voucherDenomination).partialTransactionId(request.getPartialTransactionId())
				.pointsValue(request.getSpentPoints().toString()).reprocessedFlag(false).transactionDescription(getTransactionDesriptin(request))
				.dateLastUpdated(new Date()).updatedUser(header.getUserName())
				.build();
		refundDomain.saveRefundTransaction(refundTransactionDomain);
	}
	
	public ResultResponse bulkPaymentReversal(Headers header) {
		LOG.info("inside bulkPaymentReversal flow....");
		List<RefundTransaction> failedTransactions = refundDomain.fetchRecentFailedTransactionsList();
		
		List<PaymentReversalRequetsDto> reversalRequestList = new ArrayList<>();
		for(RefundTransaction refundTransaction : failedTransactions) {
			PaymentReversalRequetsDto reversalRequetsDto = new PaymentReversalRequetsDto();
			PurchaseRequestDto request = new PurchaseRequestDto();
			request.setMsisdn(refundTransaction.getMsisdn());
			request.setUiLanguage(refundTransaction.getLanguage());
			request.setOfferId(refundTransaction.getOfferId());
			request.setCouponQuantity(refundTransaction.getQuantity());
			request.setExtTransactionId(refundTransaction.getTransactionId());
			request.setVoucherDenomination((null != refundTransaction.getVoucherDenomination()) ? Integer.valueOf(refundTransaction.getVoucherDenomination()) : null);
			request.setSelectedPaymentItem(refundTransaction.getSelectedPaymentItem());
			request.setSelectedOption(refundTransaction.getSelectedPaymentOption());
			request.setSpentAmount((null != refundTransaction.getAmount()) ? Double.valueOf(refundTransaction.getAmount()) : null);
			request.setAccountNumber(refundTransaction.getAccountNumber());
			request.setSpentPoints((null != refundTransaction.getPointsValue()) ? Integer.valueOf(refundTransaction.getPointsValue()) : null);
			request.setPartialTransactionId(refundTransaction.getPartialTransactionId());
			request.setAuthorizationCode(refundTransaction.getAuthorizationCode());
			request.setCardNumber(refundTransaction.getCardNumber());
			request.setCardSubType(refundTransaction.getCardSubType());
			request.setCardToken(refundTransaction.getCardToken());
			request.setCardType(refundTransaction.getCardType());
			request.setEpgTransactionId(refundTransaction.getEpgTransactionId());
			request.setOrderId(refundTransaction.getOrderId());
			
			//transactionDescription and account Type
			reversalRequetsDto.setPurchaseRequestDto(request);
			reversalRequetsDto.setAccountType(refundTransaction.getAccountType());
			reversalRequetsDto.setTransactionDescription(refundTransaction.getTransactionDescription());
			reversalRequestList.add(reversalRequetsDto);
		}
		
		return smilesPaymentReversal(reversalRequestList, true, header);
	}
	
    public String fetchActivityDescripion(String offerId) {
    	OfferCatalog offerCatalog = offerRepository.findByOfferId(offerId);
    	String activityDescription = null;
    	if (null != offerCatalog && !ObjectUtils.isEmpty(offerCatalog.getActivityCode()) && !ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode())
    			&& !ObjectUtils.isEmpty(offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription())) {
    		activityDescription = offerCatalog.getActivityCode().getRedemptionActivityCode().getActivityCodeDescription().getActivityCodeDescriptionEn();
    	}
    	return activityDescription;
    }
    
    public String getTransactionDesriptin(PurchaseRequestDto request) {
		String description = null;
		if ((MarketplaceConstants.SUBSCRIPTION.getConstant()).equalsIgnoreCase(request.getSelectedPaymentItem())){
			description = subscriptionDescription;
		}
		else {
			description = fetchActivityDescripion(request.getOfferId());
		}
		return description;
    }
    
    public SmilesPaymentReversalListRequestDto createPaymmentReversalRequest(PaymentReversalRequetsDto request) {
    	
    	SmilesPaymentReversalListRequestDto reversalRequest = new SmilesPaymentReversalListRequestDto();
		reversalRequest.setAccountType(request.getAccountType());
		reversalRequest.setLang(request.getPurchaseRequestDto().getUiLanguage());
		reversalRequest.setMsisdn(request.getPurchaseRequestDto().getAccountNumber());
		reversalRequest.setOfferId(request.getPurchaseRequestDto().getOfferId());
		reversalRequest.setQuantity(request.getPurchaseRequestDto().getCouponQuantity());
		reversalRequest.setTransactionId(request.getPurchaseRequestDto().getExtTransactionId());
		reversalRequest.setVoucherDenomination(null != request.getPurchaseRequestDto().getVoucherDenomination() ? request.getPurchaseRequestDto().getVoucherDenomination().toString() : null );
		reversalRequest.setTransactionDescription(getTransactionDesriptin(request.getPurchaseRequestDto()));
		
		if ((MarketplaceConstants.BILLPAYMENT.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedPaymentItem())
				|| (MarketplaceConstants.RECHARGES.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedPaymentItem())) {
			SelectedPaymentItemBillRechargeDto selectedPaymentItem =  new SelectedPaymentItemBillRechargeDto();
			selectedPaymentItem.setSelectedItem(BILL_AND_RECHARGE);
			BillsAndRechargesDto billsAndRechargesDto = new BillsAndRechargesDto();
			if ((MarketplaceConstants.BILLPAYMENT.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedPaymentItem())) {
				LOG.info("Setting billpayment details for reversal");
				List<BillsListDto> billsListDtos = new ArrayList<>(1);
				billsListDtos.add(new BillsListDto());
				billsListDtos.get(0).setAccountType(request.getAccountType());
				billsListDtos.get(0).setBillAmount(request.getPurchaseRequestDto().getSpentAmount());
				billsListDtos.get(0).setMsisdn(request.getPurchaseRequestDto().getAccountNumber());
				billsListDtos.get(0).setOfferId(request.getPurchaseRequestDto().getOfferId());
				billsAndRechargesDto.setBillsList(billsListDtos);
			}
			if ((MarketplaceConstants.RECHARGES.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedPaymentItem())) {
				LOG.info("Setting recharge details for reversal");
				List<RechargesListDto> rechargesListDtos = new ArrayList<>(1);
				rechargesListDtos.add(new RechargesListDto());
				rechargesListDtos.get(0).setAccountType(request.getAccountType());
				rechargesListDtos.get(0).setMsisdn(request.getPurchaseRequestDto().getAccountNumber());
				rechargesListDtos.get(0).setRechargesAmount(request.getPurchaseRequestDto().getSpentAmount());
				rechargesListDtos.get(0).setOfferId(request.getPurchaseRequestDto().getOfferId());
				billsAndRechargesDto.setRechargesList(rechargesListDtos);
			}
			selectedPaymentItem.setBillsAndRecharges(billsAndRechargesDto);
			reversalRequest.setSelectedPaymentItem(selectedPaymentItem);
		} else if ((MarketplaceConstants.SUBSCRIPTION.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedPaymentItem())) {
			SelectedItemDto selectedPaymentItem = new SelectedItemDto();
			selectedPaymentItem.setSelectedItem(SUBSCRIPTION_SELECTEDITEM);
			reversalRequest.setSelectedPaymentItem(selectedPaymentItem);
		} else {
			SelectedItemDto selectedPaymentItem = new SelectedItemDto();
			selectedPaymentItem.setSelectedItem(request.getPurchaseRequestDto().getSelectedPaymentItem());
			reversalRequest.setSelectedPaymentItem(selectedPaymentItem);
		}
		if((MarketplaceConstants.PARTIALPOINTSCC.getConstant()).equalsIgnoreCase(request.getPurchaseRequestDto().getSelectedOption())) {
			SelectedPaymentOptionPartialPointCCDto selectedPaymentOption = new SelectedPaymentOptionPartialPointCCDto();
			selectedPaymentOption.setDirhamValue(request.getPurchaseRequestDto().getSpentAmount().toString());
			selectedPaymentOption.setPaymentType(request.getPurchaseRequestDto().getSelectedOption());
			selectedPaymentOption.setPointsValue(request.getPurchaseRequestDto().getSpentPoints().toString());
			selectedPaymentOption.setPartialTransactionId(request.getPurchaseRequestDto().getPartialTransactionId());
			reversalRequest.setSelectedPaymentOption(selectedPaymentOption);
		}
		else {
			SelectedPaymentOptionFullCCDto selectedPaymentOption = new SelectedPaymentOptionFullCCDto();
			selectedPaymentOption.setDirhamValue(request.getPurchaseRequestDto().getSpentAmount().toString());
			selectedPaymentOption.setPaymentType(request.getPurchaseRequestDto().getSelectedOption());
			reversalRequest.setSelectedPaymentOption(selectedPaymentOption);
		}
		
		CreditCardDetailDto creditCardDetail = new CreditCardDetailDto();
		creditCardDetail.setAuthorizationCode(request.getPurchaseRequestDto().getAuthorizationCode());
		creditCardDetail.setCardNumber(request.getPurchaseRequestDto().getCardNumber());
		creditCardDetail.setCardSubType(request.getPurchaseRequestDto().getCardSubType());
		creditCardDetail.setCardToken(request.getPurchaseRequestDto().getCardToken());
		creditCardDetail.setCardType(request.getPurchaseRequestDto().getCardType());
		creditCardDetail.setEpgTransactionId(request.getPurchaseRequestDto().getEpgTransactionId());
		creditCardDetail.setOrderId(request.getPurchaseRequestDto().getOrderId());
		reversalRequest.setCreditCardDetails(creditCardDetail);
		
		return reversalRequest;
    }
    
}




