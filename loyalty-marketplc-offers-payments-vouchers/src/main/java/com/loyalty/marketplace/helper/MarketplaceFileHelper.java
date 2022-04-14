package com.loyalty.marketplace.helper;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Path;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.loyalty.marketplace.domain.model.UploadedFileContentDomain;
import com.loyalty.marketplace.domain.model.UploadedFileInfoDomain;
import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.helper.FetchServiceValues;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.ProcessValues;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileContent;
import com.loyalty.marketplace.outbound.database.entity.UploadedFileInfo;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileContentRepository;
import com.loyalty.marketplace.outbound.database.repository.UploadedFileInfoRepository;
import com.loyalty.marketplace.outbound.dto.BogoBulkUploadResponse;
import com.loyalty.marketplace.outbound.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.service.MemManagementService;
import com.loyalty.marketplace.service.dto.ApiError;
import com.loyalty.marketplace.subscription.constants.SubscriptionManagementConstants;
import com.loyalty.marketplace.subscription.inbound.controller.SubscriptionManagementController;
import com.loyalty.marketplace.subscription.outbound.dto.HbFileDto;
import com.loyalty.marketplace.utils.MarketPlaceFileValidator;
import com.opencsv.CSVWriter;

@Component
public class MarketplaceFileHelper {

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
	UploadedFileInfoDomain uploadedFileInfoDomain;

	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	MemManagementService memManagementService;
	
	@Autowired
	EventHandler eventHandler;
	
	@Value("${etisalat.update.offercatalog.fileupload.location}")
	private String updateOfferCatalogFileUploadLocation;
	
	@Value("#{'${renewal.report.email.list}'.split(',')}")
	private List<String> reportEmailList;
	 
	@Value("${renewal.report.email.template.id}")
	private String reportEmailTemplateId;
	    
	@Value("${renewal.report.email.notification.id}")
	private String reportEmailNotificationId;
	

	private static final Logger log = LoggerFactory.getLogger(MarketplaceFileHelper.class);

	// @EnableTransactionManagement -> EnableTransactionManagement at this location
	public void changeStateToProcessing(List<UploadedFileInfo> uploadedFileInfoList) {

		uploadedFileInfoDomain.updateInfoStatus(uploadedFileInfoList, ApplicationConstants.PROCESSING);
		List<String> uploadedFileIdList = uploadedFileInfoList.stream().map(UploadedFileInfo::getId).collect(Collectors.toList());
		log.info("UploadedFileInfo Id List: {}", uploadedFileIdList);

		uploadedFileContentDomain.updateContentStatusToProcessing(uploadedFileIdList);
		log.info("Status Changed to Processing for UploadedFileContents");
	}

	public UploadedFileContent updateFailedFileContent(UploadedFileContent uploadedFileContent, ApiError apiError, Headers headers) {
		log.info("Inside updateFailedFileContent {} : ", uploadedFileContent);

		uploadedFileContent.setStatus(ApplicationConstants.FAILURE);
		uploadedFileContent.setErrorCode(apiError.getCode().toString());
		uploadedFileContent.setErrorMessage(apiError.getMessage());

		return uploadedFileContentRepository.save(uploadedFileContent);
	}

	public UploadedFileContent updateSuccessFileContent(UploadedFileContent uploadedFileContent, Headers headers) {
		log.info("Inside updateSuccessFileContent {} : ", uploadedFileContent);

		uploadedFileContent.setStatus(ApplicationConstants.SUCCESS);
		return uploadedFileContentRepository.save(uploadedFileContent);
	}
	
	public void updateHBFlagAndPath(UploadedFileInfo uploadedFileInfo) {
		log.info("Inside updateHBFlagAndPath {} : ", uploadedFileInfo);
		
		uploadedFileInfo.setIshandback(true);
		uploadedFileInfo.setSuccessHandBackFileName(getFileNameWithoutExtension(uploadedFileInfo.getFileName()).concat("_Handback.csv"));
		uploadedFileInfo.setHandbackFilePath(FileSystems.getDefault().getPath(updateOfferCatalogFileUploadLocation, 
				ApplicationConstants.HANDBACKFILE, getFileNameWithoutExtension(uploadedFileInfo.getFileName()).concat("_Handback.csv")).toString());
		uploadedFileInfoRepository.save(uploadedFileInfo);
	}
	
	public boolean checkForDuplicateFileName(String fileName, BogoBulkUploadResponse resultResponse) {

		log.info("inside MarketplaceFileUploadHelper.checkDuplicateFileName(): FileName: {}", fileName);
		boolean isExists = mongoOperations.exists(query(where("FileName").is(fileName)), UploadedFileInfo.class);
		log.info("inside MarketplaceFileUploadHelper.checkDuplicateFileName() with isExists={}", isExists);

		return isExists;

	}

	public boolean checkForDuplicateInvoiceReferenceNumber(String invoiceReferenceNumber,
			BogoBulkUploadResponse resultResponse) {
		log.info("inside MarketplaceFileUploadHelper.checkForDuplicateInvoiceReferenceNumber(): FileName: {}",
				invoiceReferenceNumber);

		boolean isExists = mongoOperations.exists(query(where("InvoiceReferenceNumber").is(invoiceReferenceNumber)),
				UploadedFileInfo.class);
		log.info("inside MarketplaceFileUploadHelper.checkForDuplicateInvoiceReferenceNumber() with isExists={}",
				isExists);
		return isExists;
	}
	
	public UploadedFileInfo updateStatusAndRecordCountInFileInfo(UploadedFileInfo uploadedFileInfo, int recordCount) {
		log.info("Inside updateStatusAndRecordCountInFileInfo {} : ", uploadedFileInfo);		
		uploadedFileInfo.setProcessingStatus("Processed");
		uploadedFileInfo.setRecordsCount(recordCount);
		return uploadedFileInfoRepository.save(uploadedFileInfo);		
	}
	
	public void emailReport(UploadedFileInfo uploadedFileInfo) {
		log.info("Inside emailRenewalReport fileLocation:{}", uploadedFileInfo.getHandbackFilePath());	
			if(!ObjectUtils.isEmpty(uploadedFileInfo) && !StringUtils.isEmpty(uploadedFileInfo.getFileName()) 
					&& !StringUtils.isEmpty(uploadedFileInfo.getHandbackFilePath()) && !CollectionUtils.isEmpty(reportEmailList)) {				
				for(String email : reportEmailList) {				
					EmailRequestDto emailRequestDto = new EmailRequestDto();
					emailRequestDto.setEmailId(email);
					emailRequestDto.setTemplateId(reportEmailTemplateId);
					emailRequestDto.setNotificationId(reportEmailNotificationId);
					emailRequestDto.setLanguage(ApplicationConstants.ENGLISH_LANGUAGE);
					
					final Map<String, String> additionalParameters = new HashMap<>();
					additionalParameters.put(ApplicationConstants.FILE_PATH, uploadedFileInfo.getHandbackFilePath());
					additionalParameters.put(ApplicationConstants.FILE_NAME, getFileNameWithoutExtension(uploadedFileInfo.getFileName()));
					additionalParameters.put(ApplicationConstants.REPORT_NAME, uploadedFileInfo.getFileSource());
					additionalParameters.put(ApplicationConstants.FILE_TYPE, ApplicationConstants.FILE_CONTENT_TYPE);
					emailRequestDto.setAdditionalParameters(additionalParameters);
					
					eventHandler.publishEmail(emailRequestDto);					
				}				
				log.info("Sent emails for {} report", uploadedFileInfo.getFileSource());
			}			
		}
			
	public String getFileNameWithoutExtension(String fileName) {
		int indexOfExtension = fileName.indexOf(ApplicationConstants.FILE_EXTENSTION);
		String fileNameWithoutExtension = fileName.substring(0, indexOfExtension);
		return fileNameWithoutExtension;
	}
	
	
	public void createHandbackFileAndUpdateInfoForOfferCatalog(List<HbFileDto> hbFileDtoList, UploadedFileInfo uploadedFileInfo, String fileSource) {
		try {
			writeObjectToCSVForOfferCatalog(hbFileDtoList, 
						FileSystems.getDefault().getPath(updateOfferCatalogFileUploadLocation, ApplicationConstants.HANDBACKFILE, 
						getFileNameWithoutExtension(uploadedFileInfo.getFileName()).concat("_Handback.csv")));
				updateHBFlagAndPath(uploadedFileInfo);
		} catch(Exception e) {
			log.error("inside MarketplaceFileHelper.createHandbackFileAndUpdateInfo() Exception {}",
					e.getMessage());
		}
		
	}
	
	public void writeObjectToCSVForOfferCatalog(List<HbFileDto> hbFileDtoList, Path handbackFilePath) {
		
			log.info("inside MarketPlaceFileHelper.writeObjectToCSVForOfferCatalog(): handbackFilePath:{}", handbackFilePath);

			List<String[]> csvStringArrayList = hbFileDtoList.stream()
					.map(hbContent -> new String[] { hbContent.getExternalReferenceNumber(), hbContent.getStatus(), hbContent.getErrorMessage() })
					.collect(Collectors.toList());
			log.info(
					"inside MarketPlaceFileHelper.writeObjectToCSVForOfferCatalog() for file UpLoad Activity csvStringArrayList {} ",
					csvStringArrayList);

			String[] headerRecord = { ApplicationConstants.CSV_FILE_HEADER_EXTREFNUM, ApplicationConstants.CSV_FILE_HEADER_STATUS, ApplicationConstants.CSV_FILE_HEADER_ERROR_MESSAGE};
			
			try (CSVWriter csvwriter = new CSVWriter(new FileWriter(handbackFilePath.toString()),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);) {
				csvwriter.writeNext(headerRecord);
				csvwriter.writeAll(csvStringArrayList);
				log.info("Data entered");
			} catch (IOException e) {	
				log.info("inside MarketPlaceFileHelper.writeObjectToCSVForOfferCatalog() of class Marketplace {}",
						e.getMessage());
			}

		}
	
	public HbFileDto populateHBFile(UploadedFileInfo uploadedFileInfo, UploadedFileContent uploadedFileContent) {
		log.info("Inside MarketPlaceFileHelper populateHBFile");
		HbFileDto hbFileDto = new HbFileDto();
		hbFileDto.setAccountNumber(uploadedFileContent.getAccountNumber());
		hbFileDto.setMembershipCode(uploadedFileContent.getMembershipCode());
		hbFileDto.setExternalReferenceNumber(uploadedFileContent.getExternalReferenceNumber());
		hbFileDto.setPartnerCode(uploadedFileInfo.getPartnerCode());
		hbFileDto.setInvoiceRefNumber(uploadedFileInfo.getInvoiceReferenceNumber());
		hbFileDto.setSubscriptionCatalogId(uploadedFileInfo.getSubscriptionCatalogId());
		hbFileDto.setStatus(uploadedFileContent.getStatus());
		hbFileDto.setErrorMessage(uploadedFileContent.getErrorMessage());
		log.info("Exit from populateHBFile: hbFileDto {}", hbFileDto);
		return hbFileDto;		
	}
	
	public boolean checkForDuplicateExternalReferenceNumber(String externalReferenceNumber, UploadedFileContent uploadedFileContent) {
		log.info("ExternalReferenceNumber : {}", externalReferenceNumber);
		Query query = new Query();
		query.addCriteria(
			Criteria.where("ExternalReferenceNumber").is(externalReferenceNumber)
			.and("FileType").is(uploadedFileContent.getFileType())
			.orOperator(
	                Criteria.where("Status").is(ApplicationConstants.FAILURE),
	                Criteria.where("Status").is(ApplicationConstants.SUCCESS))
		);
		long count = mongoOperations.count(query, UploadedFileContent.class);
		log.info("Count : {}", count);
		boolean isExists = count > 0 ? true : false;
		log.info("isExists : {}", isExists);
		return isExists;
	}
	
}
