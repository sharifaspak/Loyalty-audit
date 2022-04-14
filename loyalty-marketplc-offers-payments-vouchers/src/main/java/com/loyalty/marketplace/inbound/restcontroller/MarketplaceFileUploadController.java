package com.loyalty.marketplace.inbound.restcontroller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.helper.MarketplaceFileUploadHelper;
import com.loyalty.marketplace.inbound.dto.BogoBulkUploadRequest;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.BogoBulkUploadResponse;
import com.loyalty.marketplace.outbound.dto.OfferCatalogHandbackFileResponse;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SubscriptionHandbackFileResponse;
import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.constants.SubscriptionRequestMappingConstants;
import com.loyalty.marketplace.utils.MarketplaceException;

import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/marketplace")
public class MarketplaceFileUploadController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceFileUploadController.class);

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ProgramManagement programManagement;	
	
	@Autowired
	MarketplaceFileUploadHelper fileUploadHelper;
	
	@Value("${program.code}")
	public String defaultProgramCode;
	
	@Value("${etisalat.update.offercatalog.fileupload.location}")
	private String updateOfferCatalogFileUploadLocation;
	
	@PostMapping(value = "/file/subscription", consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	public BogoBulkUploadResponse uploadSubscription(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = SubscriptionRequestMappingConstants.SUBSCRIPTION_CATALOG_ID, required = true) String subscriptionCatalogId,
			@RequestParam(value = SubscriptionRequestMappingConstants.PARTNER_CODE, required = false) String partnerCode,
			@RequestParam(value = SubscriptionRequestMappingConstants.INVOICE_REFERENCE_NUMBER, required = false) String invoiceReferenceNumber,
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
		LOG.info("Enter uploadSubscription:: Request File:  {}",file.getContentType());
		
		BogoBulkUploadResponse resultResponse = new BogoBulkUploadResponse(externalTransactionId);		
		
		try {
			if (null == program) program = defaultProgramCode;
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));
			BogoBulkUploadRequest bogoBulkUploadRequest = new BogoBulkUploadRequest(file, subscriptionCatalogId, partnerCode, invoiceReferenceNumber);	
			fileUploadHelper.parseUploadedBogoFile(bogoBulkUploadRequest, headers, resultResponse);			
			
		} catch(Exception e) {
			e.printStackTrace();
			resultResponse.setErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getId(),
					SubscriptionManagementCode.SUBSCRIPTION_CREATION_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), "createbulkSubscription",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Exit uploadSubscription");
		resultResponse.getResult().getDescription();
		return resultResponse;
	}
	
	@GetMapping(value = "/subscription/handbackFile")
	public SubscriptionHandbackFileResponse subscriptionUploadHandbackFileList(
			@RequestParam(value = SubscriptionRequestMappingConstants.START_DATE, required = true) String startDate,
			@RequestParam(value = SubscriptionRequestMappingConstants.END_DATE, required = true) String endDate,
			@RequestParam(value = SubscriptionRequestMappingConstants.SUBSCRIPTION_CATALOG_ID, required = true) String subscriptionCatalogId,
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

		LOG.info("subscriptionUploadHandbackFileList input parameters : startDate - {}, endDate - {}, subscriptionId - {}", startDate, endDate, subscriptionCatalogId);
		SubscriptionHandbackFileResponse resultResponse = new SubscriptionHandbackFileResponse(externalTransactionId);
		
		try {
			if (StringUtils.isEmpty(program)) {program = defaultProgramCode;}
			fileUploadHelper.populateSubscriptionUploadHandbackFileList(startDate, endDate, subscriptionCatalogId, program, resultResponse);
			
		} catch (Exception e) {
			LOG.info("Inside catch block of subscriptionUploadHandbackFileList");
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), "subscriptionUploadHandbackFileList",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Result Response:{}",resultResponse);
		LOG.info("Exit from subscriptionUploadHandbackFileList");
		return resultResponse;
	}
	
	@GetMapping(value = "/subscription/specificHandbackFile")
	public SubscriptionHandbackFileResponse subscriptionProcessSpecificHandbackFile(
			@RequestParam(value = SubscriptionRequestMappingConstants.UPLOADED_FILE_INFO_ID, required = true) String uploadedFileInfoId,
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

		LOG.info("subscriptionSpecificHandbackFile input parameters : FileId {}", uploadedFileInfoId);
		SubscriptionHandbackFileResponse resultResponse = new SubscriptionHandbackFileResponse(externalTransactionId);
		
		try {
			if (StringUtils.isEmpty(program)) {program = defaultProgramCode;}
			fileUploadHelper.getUploadSubscriptionSpecificHBFile(uploadedFileInfoId, program, resultResponse);
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), "subscriptionSpecificHandbackFile",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return resultResponse;
	}
	
	@PostMapping(value = "/file/bulkOfferCatalogUpdate")
	public ResultResponse offerCatalogBulkUpdate(@RequestParam(value = OffersRequestMappingConstants.FILE, required = true) MultipartFile file,
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
    	
		LOG.info("Entering offerCatalogBulkUpdate");
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		try {
			if (StringUtils.isEmpty(program)) program = defaultProgramCode;
			Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev, channelId, systemId, systemPassword, token, transactionId);
			fileUploadHelper.uploadFile(file, headers, updateOfferCatalogFileUploadLocation, resultResponse);
		} catch (Exception e) {
			LOG.info("Inside catch block of offerCatalogBulkUpdate");
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
			resultResponse.setResult(MarketPlaceCode.FILE_UPLOAD_FAILED.getId(),
					MarketPlaceCode.FILE_UPLOAD_FAILED.getMsg());
		}
		LOG.info("Leaving offerCatalogBulkUpdate");
    	return resultResponse;
    }
	
	@GetMapping(value = "/updateOfferCatalog/handbackFile")
	public OfferCatalogHandbackFileResponse offerCatalogUpdateHandbackFileList(
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

		LOG.info("Enter offerCatalogUpdateHandbackFileList");
		OfferCatalogHandbackFileResponse resultResponse = new OfferCatalogHandbackFileResponse(externalTransactionId);
		
		try {
			if (StringUtils.isEmpty(program)) {program = defaultProgramCode;}
			fileUploadHelper.populateOfferCatalogUpdateHandbackFileList(program, resultResponse);
			
		} catch (Exception e) {
			LOG.info("Inside catch block of offerCatalogUpdateHandbackFileList");
			e.printStackTrace();
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), "offerCatalogUpdateHandbackFileList",
					e.getClass() + e.getMessage(), MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		LOG.info("Result Response:{}",resultResponse);
		LOG.info("Exit from  offerCatalogUpdateHandbackFileList");
		return resultResponse;
	}
	
	@GetMapping(value = "/updateOfferCatalog/downloadHandbackFile")
	public ResponseEntity<Resource> downloadHandbackFile(
			@RequestParam(value = SubscriptionRequestMappingConstants.UPLOADED_FILE_INFO_ID, required = true) String uploadedFileInfoId,
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
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			HttpServletRequest request) throws MarketplaceException{
			try {
				if(null == program) {
					program = defaultProgramCode;
				}
				Resource resource = fileUploadHelper.downloadHandbackFile(uploadedFileInfoId);
				String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
				if(contentType == null) {
		            contentType = "application/octet-stream";
		        }
		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(contentType))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		                .body(resource);
			        
			} catch (Exception e) {
				e.printStackTrace();
				throw new MarketplaceException(this.getClass().toString(), "downloadHandbackFile",
						e.getClass() + e.getMessage(), MarketplaceCode.FILE_NOT_FOUND);
			}
	}

}
