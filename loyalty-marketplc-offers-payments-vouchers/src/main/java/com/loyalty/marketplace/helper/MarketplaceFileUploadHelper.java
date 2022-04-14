package com.loyalty.marketplace.helper;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketplaceConfigurationConstants;
import com.loyalty.marketplace.domain.model.UploadedFileContentDomain;
import com.loyalty.marketplace.domain.model.UploadedFileInfoDomain;
import com.loyalty.marketplace.inbound.dto.BogoBulkCSVRequest;
import com.loyalty.marketplace.inbound.dto.BogoBulkUploadAsyncRequest;
import com.loyalty.marketplace.inbound.dto.BogoBulkUploadRequest;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.inbound.dto.PurchaseRequestDto;
import com.loyalty.marketplace.offers.outbound.dto.PurchaseResultResponse;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileInfo;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileContentRepository;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileInfoRepository;
import com.loyalty.marketplace.outbound.dto.BogoBulkUploadResponse;
import com.loyalty.marketplace.outbound.dto.OfferCatalogHandbackFileResponse;
import com.loyalty.marketplace.outbound.dto.OfferCatalogHandbackFileResponseDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.dto.SubscriptionHandbackFileDto;
import com.loyalty.marketplace.outbound.dto.SubscriptionHandbackFileResponse;
import com.loyalty.marketplace.outbound.dto.SubscriptionHandbackFileResponseDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.payment.constants.MarketplaceCode;
import com.loyalty.marketplace.service.MemManagementService;
import com.loyalty.marketplace.service.dto.GetListMemberResponse;
import com.loyalty.marketplace.service.dto.GetListMemberResponseDto;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.loyalty.marketplace.utils.MarketPlaceFileValidator;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.welcomegift.outbound.dto.WelcomeGiftRequestDto;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;

@Component
public class MarketplaceFileUploadHelper {

	@Autowired
	UploadedFileInfoRepository uploadedFileInfoRepository;

	@Autowired
	UploadedFileContentRepository uploadedFileContentRepository;

	@Autowired
	SubscriptionManagementController subscriptionManagementController;

	@Autowired
	ModelMapper modelMapper;
		
	@Autowired
	FetchServiceValues fetchServiceValues;
	
	@Autowired
	MarketPlaceFileValidator marketPlaceFileValidator;
	
	@Autowired
	UploadedFileContentDomain uploadedFileContentDomain;

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	MemManagementService memManagementService;
	
	@Autowired
	UploadedFileInfoDomain uploadedFileInfoDomain;
	
	@Autowired
	EventHandler eventHandler;
	
	@Value("#{'${renewal.report.email.list}'.split(',')}")
	private List<String> reportEmailList;
	 
	@Value("${renewal.report.email.template.id}")
	private String reportEmailTemplateId;
	    
	@Value("${renewal.report.email.notification.id}")
	private String reportEmailNotificationId;
	
	
	private static final Logger LOG = LoggerFactory.getLogger(MarketplaceFileUploadHelper.class);

	public void parseUploadedBogoFile(BogoBulkUploadRequest bogoBulkUploadRequest, Headers headers,
			BogoBulkUploadResponse resultResponse) throws MarketplaceException {
		LOG.info("Enter parseUploadedBogoFile");

		if (Utils.validateFileFormat(bogoBulkUploadRequest.getFile())) {

			if (checkForDuplicateFileName(bogoBulkUploadRequest.getFile().getOriginalFilename(), resultResponse)) {
				resultResponse.addErrorAPIResponse(
						Integer.parseInt(MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_FILENAME.getId()),
						MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_FILENAME.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_FILENAME.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_FILENAME.getMsg());
				return;
			}

			if (checkForDuplicateInvoiceReferenceNumber(bogoBulkUploadRequest.getInvoiceReferenceNumber(),
					resultResponse)) {
				resultResponse.addErrorAPIResponse(
						Integer.parseInt(MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_INVOICEREFNUM.getId()),
						MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_INVOICEREFNUM.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_INVOICEREFNUM.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_HAS_DUPLICATE_INVOICEREFNUM.getMsg());
				return;
			}

			UploadedFileInfoDomain uploadedFileInfoDomain = new UploadedFileInfoDomain.UploadedFileInfoBuilder(
					headers.getProgram(), bogoBulkUploadRequest.getFile().getOriginalFilename(), new Date(),
					MarketplaceConfigurationConstants.STATUS_SUCCESS, "Upload",
					MarketplaceConfigurationConstants.STATUS_PENDING,
					bogoBulkUploadRequest.getFile().getOriginalFilename(),
					bogoBulkUploadRequest.getFile().getOriginalFilename(),
					bogoBulkUploadRequest.getSubscriptionCatalogId(), bogoBulkUploadRequest.getPartnerCode(),
					bogoBulkUploadRequest.getInvoiceReferenceNumber(), headers.getExternalTransactionId(), 0,
					new Date(), headers.getUserName(), headers.getUserName(), null, null).build();

			UploadedFileInfo savedUploadedFileInfo = saveUploadedFileInfo(uploadedFileInfoDomain);
			List<String> duplicateExternalReferenceNumber = new ArrayList<>();
			List<BogoBulkCSVRequest> bogoBulkCSVRequestList = createBogoBulkRequestList(bogoBulkUploadRequest.getFile(),
					duplicateExternalReferenceNumber, headers, resultResponse);
			LOG.info("bogoBulkCSVRequestList : {}", bogoBulkCSVRequestList);

			int recordCount = bogoBulkCSVRequestList.size();
			LOG.info("Record Count in Upload File info: {}", recordCount);
			if (recordCount == 0) {
				resultResponse.addErrorAPIResponse(Integer.parseInt(MarketPlaceCode.SUBSCRIPTION_FILE_IS_EMPTY.getId()),
						MarketPlaceCode.SUBSCRIPTION_FILE_IS_EMPTY.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_IS_EMPTY.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_IS_EMPTY.getMsg());
				return;
			}
			savedUploadedFileInfo.setRecordsCount(recordCount);
			List<String> extRefList = bogoBulkCSVRequestList.stream()
					.map(BogoBulkCSVRequest::getExternalReferenceNumber).collect(Collectors.toList());
			List<String> existingExternalReferenceNumber = uploadedFileContentRepository
					.findByExternalReferenceNumberIn(extRefList).stream()
					.map(UploadedFileContent::getExternalReferenceNumber).collect(Collectors.toList());

			List<UploadedFileContent> uploadedFileContentList = new ArrayList<>();
			for (BogoBulkCSVRequest bogoBulkCSVRequest : bogoBulkCSVRequestList) {
				UploadedFileContentDomain uploadedFileContentDomain = new UploadedFileContentDomain();

				if (bogoBulkCSVRequest.isValidContent()) {
					uploadedFileContentDomain = new UploadedFileContentDomain.UploadedFileContentBuilder()
							.programCode(headers.getProgram()).uploadedFileInfoId(savedUploadedFileInfo.getId())
							.fileName(savedUploadedFileInfo.getFileName())
							.membershipCode(bogoBulkCSVRequest.getMembershipCode())
							.accountNumber(bogoBulkCSVRequest.getAccountNumber())
							.subscriptionCatalogId(bogoBulkUploadRequest.getSubscriptionCatalogId())
							.externalReferenceNumber(bogoBulkCSVRequest.getExternalReferenceNumber())
							.fileType(MarketplaceConfigurationConstants.BOGO_BULK_UPLOAD)
							.status(MarketplaceConfigurationConstants.STATUS_PROCESSING).createdDate(new Date())
							.uploadedDate(savedUploadedFileInfo.getUploadedDate()).createdUser(headers.getUserName())
							.build();
				} else {
					uploadedFileContentDomain = new UploadedFileContentDomain.UploadedFileContentBuilder()
							.programCode(headers.getProgram()).uploadedFileInfoId(savedUploadedFileInfo.getId())
							.fileName(savedUploadedFileInfo.getFileName())
							.membershipCode(bogoBulkCSVRequest.getMembershipCode())
							.accountNumber(bogoBulkCSVRequest.getAccountNumber())
							.subscriptionCatalogId(bogoBulkUploadRequest.getSubscriptionCatalogId())
							.externalReferenceNumber(bogoBulkCSVRequest.getExternalReferenceNumber())
							.fileType(MarketplaceConfigurationConstants.BOGO_BULK_UPLOAD)
							.status(MarketplaceConfigurationConstants.STATUS_FAILED)
							.errorCode(MarketPlaceCode.SUBSCRIPTION_FILE_CONTENT_STRING_INVALID.getId())
							.errorMessage(MarketPlaceCode.SUBSCRIPTION_FILE_CONTENT_STRING_INVALID.getMsg())
							.createdDate(new Date()).uploadedDate(savedUploadedFileInfo.getUploadedDate())
							.createdUser(headers.getUserName()).build();

				}
				UploadedFileContent uploadedFileContent = modelMapper.map(uploadedFileContentDomain,
						UploadedFileContent.class);
				uploadedFileContentList.add(uploadedFileContent);
			}
			List<UploadedFileContent> savedUploadedFileContentList = saveUploadedFileContent(uploadedFileContentList);

			BogoBulkUploadAsyncRequest bogoBulkUploadAsyncRequest = new BogoBulkUploadAsyncRequest();
			bogoBulkUploadAsyncRequest.setDuplicateExternalReferenceNumber(duplicateExternalReferenceNumber);
			bogoBulkUploadAsyncRequest.setExistingExternalReferenceNumber(existingExternalReferenceNumber);

			doBulkBogoUploadAsyncCall(bogoBulkUploadRequest, savedUploadedFileInfo, savedUploadedFileContentList,
					bogoBulkUploadAsyncRequest, headers);
			resultResponse.setUploadedFileInfoId(savedUploadedFileInfo.getId());
			resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_SUCCESS.getId(),
					MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_SUCCESS.getMsg());
			resultResponse.setSuccessAPIResponse();

		} else {
			resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_INVALID.getId(),
					MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_INVALID.getMsg());
		}

	}

	public boolean checkForDuplicateReferenceNumberinDB(List<String> externalTransId) {
		boolean flag = false;
		flag = uploadedFileContentRepository.existsUploadedFileContentByExternalReferenceNumberIn(externalTransId);
		LOG.info("External Transaction Id : {}", externalTransId);
		return flag;

	}

	public UploadedFileInfo saveUploadedFileInfo(UploadedFileInfoDomain uploadedFileInfoDomain)
			throws MarketplaceException {
		LOG.info("Domain Object To Be Persisted: {}", uploadedFileInfoDomain);
		try {
			UploadedFileInfo uploadedFileInfo = modelMapper.map(uploadedFileInfoDomain, UploadedFileInfo.class);

			LOG.info("Entity Object To Be Persisted: {}", uploadedFileInfo);
			UploadedFileInfo savedUploadedFileInfo = this.uploadedFileInfoRepository.insert(uploadedFileInfo);
			LOG.info("Persisted Object: {}", savedUploadedFileInfo);
			return savedUploadedFileInfo;
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving file info.");
			throw new MarketplaceException(this.getClass().toString(), "saveUploadedFileInfo",
					mongoException.getClass() + mongoException.getMessage(), MarketplaceCode.ERROR_STATUS);
		} catch (Exception e) {
			LOG.error("Exception occured while saving info to database.");
			e.printStackTrace();
			throw new MarketplaceException(this.getClass().toString(), "saveUploadedFileInfo",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}
	}

	public List<BogoBulkCSVRequest> createBogoBulkRequestList(MultipartFile file,
			List<String> duplicateExternalReferenceNumber, Headers headers, BogoBulkUploadResponse resultResponse)
			throws MarketplaceException {
		List<String> fileContent = new ArrayList<>();
		List<BogoBulkCSVRequest> bogoBulkCSVRequestList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
			LOG.info("File content : {}", fileContent);

			List<String> externalRefNumberList = new ArrayList<>();
			if (null != fileContent && !fileContent.isEmpty()) {
				for (String splitForExternalRefNumber : fileContent) {
					String[] splitted = splitForExternalRefNumber.split(";");
					if (splitted.length != 3) {
						continue;
					}
					externalRefNumberList.add(splitted[2]);
				}
			}
			LOG.info("External Reference Number List : {}", externalRefNumberList);

			for (String externalRefNumber : externalRefNumberList) {
				LOG.info("Frequency of {} : {}", externalRefNumber,
						Collections.frequency(externalRefNumberList, externalRefNumber));
				if (Collections.frequency(externalRefNumberList, externalRefNumber) > 1) {
					duplicateExternalReferenceNumber.add(externalRefNumber);
				}

			}
			for (String contentString : fileContent) {
				LOG.info("contentString : {}", contentString);
				BogoBulkCSVRequest bogoBulkCSVRequest = new BogoBulkCSVRequest();
				bogoBulkCSVRequest.setContentString(contentString);

				String[] splitted = Arrays.stream(contentString.split(";")).map(String::trim).toArray(String[]::new);
				if (splitted.length != 3) {
					bogoBulkCSVRequest.setValidContent(false);
				} else {
					bogoBulkCSVRequest.setAccountNumber(splitted[0]);
					bogoBulkCSVRequest.setMembershipCode(splitted[1]);
					bogoBulkCSVRequest.setExternalReferenceNumber(splitted[2]);
					bogoBulkCSVRequest.setValidContent(true);
				}

				LOG.info("bogoBulkCSVRequest : {}", bogoBulkCSVRequest);
				bogoBulkCSVRequestList.add(bogoBulkCSVRequest);

			}
			return bogoBulkCSVRequestList;
		} catch (Exception e) {
			LOG.error("Exception occured while saving info catalog to database.");
			e.printStackTrace();
			throw new MarketplaceException(this.getClass().toString(), "createBogoBulkRequestList",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}
	}

	public List<UploadedFileContent> saveUploadedFileContent(List<UploadedFileContent> uploadedFileContentList)
			throws MarketplaceException {
		try {

			LOG.info("Entity Object To Be Persisted: {}", uploadedFileContentList);
			List<UploadedFileContent> savedUploadedFileContent = this.uploadedFileContentRepository
					.saveAll(uploadedFileContentList);
			LOG.info("Persisted Object: {}", savedUploadedFileContent);
			return savedUploadedFileContent;
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving file content.");
			throw new MarketplaceException(this.getClass().toString(), "saveUploadedFileContent",
					mongoException.getClass() + mongoException.getMessage(), MarketplaceCode.ERROR_STATUS);
		} catch (Exception e) {
			LOG.error("Exception occured while saving file content to database.");
			e.printStackTrace();
			throw new MarketplaceException(this.getClass().toString(), "saveUploadedFileContent",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}
	}

	public void populateSubscriptionUploadHandbackFileList(String startDate, String endDate, String subscriptionCatalogId,
			String program, SubscriptionHandbackFileResponse resultResponse) throws MarketplaceException {

		List<SubscriptionHandbackFileResponseDto> handbackFileList = new ArrayList<>();
		List<UploadedFileInfo> uploadedFileInfoList = null;
		List<String> fileInfoIdListFromUploadedFileContent = null;
		
		try {
			Date fromDate = Utilities.changeStringToDateWithTimeFormat(startDate, OfferConstants.FROM_DATE_TIME.get());
			Date toDate = Utilities.changeStringToDateWithTimeFormat(endDate, OfferConstants.END_DATE_TIME.get());

			if (!StringUtils.isEmpty(subscriptionCatalogId)) {
				try {
					uploadedFileInfoList = uploadedFileInfoRepository
							.findByProgramCodeIgnoreCaseAndSubscriptionCatalogIdAndCreatedDateBetween(program, subscriptionCatalogId, fromDate, toDate);
					LOG.info("Uploaded File Info List:", uploadedFileInfoList);
					
					List<String> listOfIdsFromUploadedFileInfo = uploadedFileInfoList.stream().map(UploadedFileInfo::getId).collect(Collectors.toList());
					LOG.info("getSubscriptionUploadHandbackFileList:listOfIdsFromUploadedFileInfo:{}",listOfIdsFromUploadedFileInfo);

					fileInfoIdListFromUploadedFileContent = uploadedFileContentRepository
							.findByUploadedFileInfoIdIn(listOfIdsFromUploadedFileInfo).stream()
							.map(UploadedFileContent::getUploadedFileInfoId).collect(Collectors.toList());
					LOG.info("getSubscriptionUploadHandbackFileList:fileInfoIdListFromUploadedFileContent:{}", fileInfoIdListFromUploadedFileContent);
					
				} catch (MongoException mongoException) {
					mongoException.printStackTrace();
				}
			}

			if (!CollectionUtils.isEmpty(uploadedFileInfoList)) {

				for (UploadedFileInfo uploadedFileInfo : uploadedFileInfoList) {
					SubscriptionHandbackFileResponseDto subscriptionHandbackFileResponseDto = new SubscriptionHandbackFileResponseDto();
					
					LOG.info("Each File Info: {}", uploadedFileInfo);

					if (fileInfoIdListFromUploadedFileContent.contains(uploadedFileInfo.getId()))
						subscriptionHandbackFileResponseDto.setHandbackFileContentPresent(true);

					subscriptionHandbackFileResponseDto.setFileId(uploadedFileInfo.getId());
					subscriptionHandbackFileResponseDto.setFileName(uploadedFileInfo.getFileName());
					subscriptionHandbackFileResponseDto.setFileProcessingStatus(uploadedFileInfo.getProcessingStatus());
					subscriptionHandbackFileResponseDto.setUploadedDate(uploadedFileInfo.getUploadedDate());
					LOG.info("File Name: {} : Uploaded Date:{}", uploadedFileInfo.getFileName(), uploadedFileInfo.getUploadedDate());

					handbackFileList.add(subscriptionHandbackFileResponseDto);
				}

				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_LISTED_SUCCESS.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_LISTED_SUCCESS.getMsg());
			} else {

				resultResponse.addErrorAPIResponse(
						Integer.parseInt(MarketPlaceCode.SUBSCRIPTION_FILE_NOT_FOUND.getId()),
						MarketPlaceCode.SUBSCRIPTION_FILE_NOT_FOUND.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_LISTING_FAILED.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_LISTING_FAILED.getMsg());
			}

		} catch (Exception e) {
			LOG.error("Exception occured while retrieving data from UploadFileContent");
			throw new MarketplaceException(this.getClass().toString(), "getSubscriptionUploadHandbackFileList",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}
		resultResponse.setSubscriptionHandbackFileResponseList(handbackFileList);

	}

	public void getUploadSubscriptionSpecificHBFile(String fileId, String program, SubscriptionHandbackFileResponse resultResponse) throws MarketplaceException {

		List<SubscriptionHandbackFileDto> handbackFileContentList = new ArrayList<>();
		List<UploadedFileContent> fileContentList = new ArrayList<>();
		List<SubscriptionHandbackFileResponseDto> handbackFileResponseList = new ArrayList<>();

		SubscriptionHandbackFileResponseDto subscriptionHandbackFileResponseDto = new SubscriptionHandbackFileResponseDto();

		try {
			if (!StringUtils.isEmpty(fileId)) {
				fileContentList = uploadedFileContentRepository
						.findByUploadedFileInfoIdAndProgramCodeIgnoreCaseAndFileTypeIgnoreCase(fileId, program,
								MarketplaceConfigurationConstants.BOGO_BULK_UPLOAD);
//				LOG.info("getUploadSubscriptionSpecificHBFile:fileContentList {}", fileContentList);
			}

			if (null != fileContentList && !ObjectUtils.isEmpty(fileContentList)) {

				for (UploadedFileContent file : fileContentList) {

					SubscriptionHandbackFileDto subscriptionHandbackFileDto = new SubscriptionHandbackFileDto();
//					LOG.info("Inside For Loop:Each UploadedFileContent Object {}", file);

					subscriptionHandbackFileDto.setAccountNumber(file.getAccountNumber());
					if (null != file.getMembershipCode() && !file.getMembershipCode().isEmpty())
						subscriptionHandbackFileDto.setMembershipCode(file.getMembershipCode());
					subscriptionHandbackFileDto.setExternalReferenceNumber(file.getExternalReferenceNumber());
					subscriptionHandbackFileDto.setStatus(file.getStatus());
					if (null != file.getErrorMessage() && !file.getErrorMessage().isEmpty())
						subscriptionHandbackFileDto.setFailureReason(file.getErrorMessage());
					handbackFileContentList.add(subscriptionHandbackFileDto);
//					LOG.info("handbackFileContentList:{}", handbackFileContentList);
				}

				if (!handbackFileContentList.isEmpty()) {
					subscriptionHandbackFileResponseDto.setSubscriptionHandbackFileContent(handbackFileContentList);
					// subscriptionHandbackFileResponseDto.setHandbackFileContentPresent(true);
				}
				handbackFileResponseList.add(subscriptionHandbackFileResponseDto);
				LOG.info("handbackfile generated successfully");
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HBFILE_LISTED_SUCCESS.getId(),
						MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HBFILE_LISTED_SUCCESS.getMsg());

//				handbackFileList = new ArrayList<>(1);
//				FreeVoucherHandbackFileDto handbackFileDto = modelMapper.map(file, FreeVoucherHandbackFileDto.class);
//				handbackFileDto.setFileContentPresent(!StringUtils.isEmpty(file.getFileContent()));
//				handbackFileDto.setHandbackFileContentPresent(!StringUtils.isEmpty(file.getHandbackFile()));
//		    	voucherControllerHelper.setFileContents(handbackFileDto, file);
//				handbackFileList.add(handbackFileDto);
//			    resultResponse.setResult(VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTED_SUCCESSFULLY.getId(), VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTED_SUCCESSFULLY.getMsg());	

			} else {

				resultResponse.addErrorAPIResponse(
						Integer.parseInt(MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_NOT_FOUND.getId()),
						MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_NOT_FOUND.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_LISTING_FAILED.getId(),
						MarketPlaceCode.SUBSCRIPTION_SPECIFIC_HB_FILE_CONTENT_LISTING_FAILED.getMsg());
			}

		} catch (Exception e) {

			e.printStackTrace();
			LOG.error("Exception occured while retrieving specific data from UploadFileContent using file id");
			throw new MarketplaceException(this.getClass().toString(), "getUploadSubscriptionSpecificHBFile",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}

		resultResponse.setSubscriptionHandbackFileResponseList(handbackFileResponseList);

	}

	public void doBulkBogoUploadAsyncCall(BogoBulkUploadRequest bogoBulkUploadRequest,
			UploadedFileInfo savedUploadedFileInfo, List<UploadedFileContent> savedUploadedFileContentList,
			BogoBulkUploadAsyncRequest bogoBulkUploadAsyncRequest, Headers headers) {
		CompletableFuture.supplyAsync(() -> {
			try {
				doBulkBogoUpload(bogoBulkUploadRequest, savedUploadedFileInfo, savedUploadedFileContentList,
						bogoBulkUploadAsyncRequest, headers);
			} catch (MarketplaceException | VoucherManagementException e) {
				e.printStackTrace();
			} catch (SubscriptionManagementException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	public void doBulkBogoUpload(BogoBulkUploadRequest bogoBulkUploadRequest, UploadedFileInfo savedUploadedFileInfo,
			List<UploadedFileContent> savedUploadedFileContentList,
			BogoBulkUploadAsyncRequest bogoBulkUploadAsyncRequest, Headers headers)
			throws MarketplaceException, VoucherManagementException, SubscriptionManagementException {
		LOG.info("Enter doBulkBogoUpload");
		try {
			List<String> accountNumberList = savedUploadedFileContentList.stream().map(UploadedFileContent :: getAccountNumber).collect(Collectors.toList());
			GetListMemberResponse memberResponse = memManagementService.getListMemberDetails(accountNumberList, MarketplaceConfigurationConstants.STATUS_ACT, headers);
			Map<String, String> accountMemberMap = memberResponse.getListMember().stream()
                    .collect(Collectors.toMap(GetListMemberResponseDto::getAccountNumber, GetListMemberResponseDto::getMembershipCode));
			
			accountMemberMap.entrySet().forEach(entry -> {
				LOG.info("accountMemberMap: Account Number {} : MembershipCode {}", entry.getKey(), entry.getValue());
			});
			for (UploadedFileContent savedUploadedFileContent : savedUploadedFileContentList) {
				if (savedUploadedFileContent.getStatus().equalsIgnoreCase(MarketplaceConfigurationConstants.STATUS_FAILED)) {
					continue;
				}
				try {
					if (marketPlaceFileValidator.validateAccountNumber(savedUploadedFileContent) 
							&& marketPlaceFileValidator.validateDuplicateReferenceNumber(bogoBulkUploadAsyncRequest, savedUploadedFileContent)
							&& marketPlaceFileValidator.validateMembershipCode(accountMemberMap, savedUploadedFileContent)) {				
			
						PurchaseRequestDto purchaseRequestDto = marketPlaceFileValidator.populatePurchaseRequestDto( savedUploadedFileContent, accountMemberMap.get(savedUploadedFileContent.getAccountNumber()), bogoBulkUploadRequest);
						WelcomeGiftRequestDto welcomeGiftRequestDto = marketPlaceFileValidator.populateWelcomeGiftRequestDto(savedUploadedFileContent, bogoBulkUploadRequest);		
						headers.setChannelId(bogoBulkUploadRequest.getPartnerCode());
						headers.setExternalTransactionId(savedUploadedFileContent.getExternalReferenceNumber());	
						
						PurchaseResultResponse purchaseResultResponse = subscriptionManagementController.createWelcomeGiftSubscription(purchaseRequestDto, welcomeGiftRequestDto, headers);		
						LOG.info("purchaseResultResponse : {}", purchaseResultResponse);
						if (null != purchaseResultResponse && purchaseResultResponse.getApiStatus().getOverallStatus().equalsIgnoreCase(MarketplaceConfigurationConstants.STATUS_SUCCESS) 
								&& null != purchaseResultResponse.getPurchaseResponseDto().getSubscriptionId()) {
							savedUploadedFileContent.setStatus(MarketplaceConfigurationConstants.STATUS_SUCCESS);
							savedUploadedFileContent.setSubscriptionId(purchaseResultResponse.getPurchaseResponseDto().getSubscriptionId());
							this.uploadedFileContentRepository.save(savedUploadedFileContent);
						} else {
							LOG.info("Upload File Content Failed : {}", purchaseResultResponse);
							uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, purchaseResultResponse);
						}
					} 
				} catch(Exception e) {
					e.printStackTrace();
					LOG.error("Exception in doBulkBogoUpload inside child try:{}", e.getMessage());
					uploadedFileContentDomain.saveFailedFileContent(savedUploadedFileContent, MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION);
				}
			}
			savedUploadedFileInfo.setProcessingStatus(MarketplaceConfigurationConstants.STATUS_PROCESSED);
			this.uploadedFileInfoRepository.save(savedUploadedFileInfo);
			LOG.info("Exit doBulkBogoUpload");

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception in doBulkBogoUpload inside main try:{}", e.getMessage());
		}		
	}

	public boolean checkForDuplicateFileName(String fileName, BogoBulkUploadResponse resultResponse) {

		LOG.info("inside MarketplaceFileUploadHelper.checkDuplicateFileName(): FileName: {}", fileName);

		boolean isExists = mongoOperations.exists(query(where("FileName").is(fileName)), UploadedFileInfo.class);
		LOG.info("inside MarketplaceFileUploadHelper.checkDuplicateFileName() with isExists={}", isExists);

		return isExists;

	}

	private boolean checkForDuplicateInvoiceReferenceNumber(String invoiceReferenceNumber,
			BogoBulkUploadResponse resultResponse) {
		LOG.info("inside MarketplaceFileUploadHelper.checkForDuplicateInvoiceReferenceNumber(): FileName: {}",
				invoiceReferenceNumber);

		boolean isExists = mongoOperations.exists(query(where("InvoiceReferenceNumber").is(invoiceReferenceNumber)),
				UploadedFileInfo.class);
		LOG.info("inside MarketplaceFileUploadHelper.checkForDuplicateInvoiceReferenceNumber() with isExists={}",
				isExists);
		return isExists;
	}
	
	public void uploadFile(MultipartFile multipartFile, Headers headers, String location, ResultResponse resultResponse) throws Exception {
		
		boolean isFileWriteSuccessful = false;
		try {
			if (Utils.validateFileFormat(multipartFile)) {
				if(uploadFileToMountLocation(multipartFile, isFileWriteSuccessful, location)) {
					resultResponse.setResult(MarketPlaceCode.FILE_UPLOAD_SUCCESS.getId(),
							MarketPlaceCode.FILE_UPLOAD_SUCCESS.getMsg());
				} else {
					resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
							MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getMsg());
					resultResponse.setResult(MarketPlaceCode.FILE_UPLOAD_FAILED.getId(),
							MarketPlaceCode.FILE_UPLOAD_FAILED.getMsg());
				}
			} else {
				resultResponse.addErrorAPIResponse(MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_INVALID.getIntId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_UPLOADED_INVALID.getMsg());
				resultResponse.setResult(MarketPlaceCode.FILE_UPLOAD_FAILED.getId(),
						MarketPlaceCode.FILE_UPLOAD_FAILED.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception in file upload : {}", e.getMessage());
			resultResponse.addErrorAPIResponse(MarketPlaceCode.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			resultResponse.setResult(MarketPlaceCode.FILE_UPLOAD_FAILED.getId(),
					MarketPlaceCode.FILE_UPLOAD_FAILED.getMsg());
		}
	}
	
	public boolean uploadFileToMountLocation(MultipartFile file, boolean isFileWriteSuccessful, String location) throws IOException, MarketplaceException {
		LOG.info("Inside uploadFileToMountLocation");
		List<String> fileContent = new ArrayList<>();
		LOG.info("File Location with file name :{}", Paths.get(location + file.getOriginalFilename()).toString());
		FileWriter fileWriter = new FileWriter(Paths.get(location + file.getOriginalFilename()).toString());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
		LOG.info("File content : {}", fileContent);  
        for (int i=0; i< fileContent.size(); i++)
		{
        	LOG.info("Each content: {}", fileContent.get(i));				
			bufferedWriter.write(fileContent.get(i));
			bufferedWriter.newLine();
			isFileWriteSuccessful = true;
		} 
        LOG.info("Writing in a file in mount location is completed:{}", location + file.getOriginalFilename());
		} catch (Exception ex) {
			throw new MarketplaceException(MarketPlaceCode.FILE_WRITE_ERROR);
		}
		
		finally {
			bufferedWriter.flush();
			bufferedWriter.close();
		}
		return isFileWriteSuccessful;
	}	
	
	public void populateOfferCatalogUpdateHandbackFileList(String program, OfferCatalogHandbackFileResponse resultResponse) throws MarketplaceException {

		List<OfferCatalogHandbackFileResponseDto> handbackFileList = new ArrayList<>();
		List<UploadedFileInfo> uploadedFileInfoList = null;
		
		try {
			uploadedFileInfoList = uploadedFileInfoRepository.findByProgramCodeIgnoreCaseAndFileSource(program, "updateOfferCatalog");
			LOG.info("Uploaded File Info List:", uploadedFileInfoList);
			
			List<String> listOfIdsFromUploadedFileInfo = uploadedFileInfoList.stream().map(UploadedFileInfo::getId).collect(Collectors.toList());
			LOG.info("getOfferCatalogUpdateHandbackFileList:listOfIdsFromUploadedFileInfo:{}",listOfIdsFromUploadedFileInfo);

			if (!CollectionUtils.isEmpty(uploadedFileInfoList)) {

				for (UploadedFileInfo uploadedFileInfo : uploadedFileInfoList) {
					
					OfferCatalogHandbackFileResponseDto offerCatalogHandbackFileResponseDto = new OfferCatalogHandbackFileResponseDto();
					LOG.info("Each File Info: {}", uploadedFileInfo);
					
					offerCatalogHandbackFileResponseDto.setHandbackFileContentPresent(uploadedFileInfo.isIshandback());
					offerCatalogHandbackFileResponseDto.setFileId(uploadedFileInfo.getId());
					offerCatalogHandbackFileResponseDto.setFileName(uploadedFileInfo.getFileName());
					offerCatalogHandbackFileResponseDto.setFileProcessingStatus(uploadedFileInfo.getProcessingStatus());
					offerCatalogHandbackFileResponseDto.setUploadedDate(uploadedFileInfo.getUploadedDate());
					
					handbackFileList.add(offerCatalogHandbackFileResponseDto);
				}

				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_LISTED_SUCCESS.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_LISTED_SUCCESS.getMsg());
			} else {

				resultResponse.addErrorAPIResponse(
						Integer.parseInt(MarketPlaceCode.FILE_NOT_FOUND.getId()),
						MarketPlaceCode.FILE_NOT_FOUND.getMsg());
				resultResponse.setResult(MarketPlaceCode.SUBSCRIPTION_FILE_LISTING_FAILED.getId(),
						MarketPlaceCode.SUBSCRIPTION_FILE_LISTING_FAILED.getMsg());
			}

		} catch (Exception e) {
			LOG.error("Exception occured while retrieving data from UploadFileContent");
			throw new MarketplaceException(this.getClass().toString(), "populateOfferCatalogUpdateHandbackFileList",
					e.getClass() + e.getMessage(), MarketplaceCode.GENERIC_RUNTIME_EXCEPTION);

		}
		resultResponse.setOfferCatalogHandbackFileResponseList(handbackFileList);

	}

	public Resource downloadHandbackFile(String fileId)  throws MarketplaceException {
		
		try {
	
			if(!StringUtils.isEmpty(fileId)) {
				
				Optional<UploadedFileInfo> uploadedFileInfo = uploadedFileInfoRepository.findById(fileId);
				LOG.info("uploadedFileInfo : {}", uploadedFileInfo.get());
				if(!ObjectUtils.isEmpty(uploadedFileInfo)) {
					
					String fileName =  uploadedFileInfo.get().getHandbackFilePath();
					LOG.info("fileName : {}", fileName);
					Path filePath = Paths.get(fileName);
					LOG.info("filePath : {}", filePath);
					Resource resource = new UrlResource(filePath.toUri());
					LOG.info("resource : {}, exists :{}", resource, resource.exists());
					if (resource.exists()) {
						LOG.info("resource : {}", resource);
						return resource;
					}
				} else {
					throw new MarketplaceException(MarketplaceCode.FILE_ID_EMPTY);
				}
			} else {
				throw new MarketplaceException(MarketplaceCode.FILE_ID_INVALID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketplaceException(this.getClass().toString(), "downloadHandbackFile",
					e.getClass() + e.getMessage(), MarketplaceCode.FILE_NOT_FOUND);
		}
	    return null;
		
	}
	
	
}
