package com.loyalty.marketplace.voucher.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.drools.core.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.inbound.controller.VoucherControllerHelper;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherUploadFile;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherUploadFileRepository;
import com.loyalty.marketplace.voucher.outbound.dto.FreeVoucherHandbackFileDto;
import com.loyalty.marketplace.voucher.outbound.dto.FreeVoucherUploadHandbackFileResponse;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Component
public class VoucherUploadFileDomain {

	@Autowired
	VoucherUploadFileRepository voucherUploadFileRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuditService auditService;
	
	@Autowired
	VoucherControllerHelper voucherControllerHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(VoucherUploadFileDomain.class);

	private String programCode;
	private String id;
	private String fileName;
	private String merchantCode;
	private String offerId;
	private Date uploadedDate;
	private String fileType;
	private String fileContent;
	private String fileProcessingStatus;
	private String reason;
	private String handbackFile;
	private String transactionId;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;

	public String getProgramCode() {
		return programCode;
	}

	public String getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getOfferId() {
		return offerId;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public String getFileType() {
		return fileType;
	}

	public String getFileContent() {
		return fileContent;
	}

	public String getFileProcessingStatus() {
		return fileProcessingStatus;
	}

	public VoucherUploadFileRepository getVoucherUploadFileRepository() {
		return voucherUploadFileRepository;
	}

	public VoucherControllerHelper getVoucherControllerHelper() {
		return voucherControllerHelper;
	}

	public String getReason() {
		return reason;
	}

	public String getHandbackFile() {
		return handbackFile;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public VoucherUploadFileDomain() {

	}

	public VoucherUploadFileDomain(VoucherUploadFileBuilder voucherBuilder) {
		this.programCode = voucherBuilder.programCode;
		this.id = voucherBuilder.id;
		this.fileName = voucherBuilder.fileName;
		this.merchantCode = voucherBuilder.merchantCode;
		this.offerId = voucherBuilder.offerId;
		this.uploadedDate = voucherBuilder.uploadedDate;
		this.fileType = voucherBuilder.fileType;
		this.fileContent = voucherBuilder.fileContent;
		this.fileProcessingStatus = voucherBuilder.fileProcessingStatus;
		this.reason = voucherBuilder.reason;
		this.handbackFile = voucherBuilder.handbackFile;
		this.transactionId = voucherBuilder.transactionId;
		this.createdDate = voucherBuilder.createdDate;
		this.createdUser = voucherBuilder.createdUser;
		this.updatedDate = voucherBuilder.updatedDate;
		this.updatedUser = voucherBuilder.updatedUser;
	}

	public static class VoucherUploadFileBuilder {

		private String programCode;
		private String id;
		private String fileName;
		private String merchantCode;
		private String offerId;
		private Date uploadedDate;

		private String fileType;
		private String fileContent;
		private String fileProcessingStatus;
		private String reason;
		
		private String handbackFile;
		private String transactionId;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;

		public VoucherUploadFileBuilder(String programCode, String fileName, String merchantCode, String offerId, Date uploadedDate,
				String fileType, String fileContent, String fileProcessingStatus, String handbackFile, Date updatedDate,
				String updatedUser) {
			this.programCode = programCode;
			this.fileName = fileName;
			this.merchantCode = merchantCode;
			this.offerId = offerId;
			this.uploadedDate = uploadedDate;

			this.fileType = fileType;
			this.fileContent = fileContent;
			this.fileProcessingStatus = fileProcessingStatus;

			this.handbackFile = handbackFile;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
		}

		public VoucherUploadFileBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public VoucherUploadFileBuilder id(String id) {
			this.id = id;
			return this;
		}

		public VoucherUploadFileBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public VoucherUploadFileBuilder merchantCode(String merchantCode) {
			this.merchantCode = merchantCode;
			return this;
		}
		
		public VoucherUploadFileBuilder offerId(String offerId) {
			this.offerId = offerId;
			return this;
		}

		public VoucherUploadFileBuilder uploadedDate(Date uploadedDate) {
			this.uploadedDate = uploadedDate;
			return this;
		}

		public VoucherUploadFileBuilder fileType(String fileType) {
			this.fileType = fileType;
			return this;
		}

		public VoucherUploadFileBuilder fileContent(String fileContent) {
			this.fileContent = fileContent;
			return this;
		}

		public VoucherUploadFileBuilder fileProcessingStatus(String fileProcessingStatus) {
			this.fileProcessingStatus = fileProcessingStatus;
			return this;
		}
		
		public VoucherUploadFileBuilder reason(String reason) {
			this.reason = reason;
			return this;
		}

		public VoucherUploadFileBuilder handbackFile(String handbackFile) {
			this.handbackFile = handbackFile;
			return this;
		}
		
		public VoucherUploadFileBuilder transactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public VoucherUploadFileBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public VoucherUploadFileBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public VoucherUploadFileBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public VoucherUploadFileBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public VoucherUploadFileDomain build() {
			return new VoucherUploadFileDomain(this);
		}

	}

	public VoucherUploadFile saveVoucherUpload(VoucherUploadFileDomain vouDom, String externalTransactionId, String userName) throws VoucherManagementException {
		
		try {
			VoucherUploadFile voucherToSave = modelMapper.map(vouDom, VoucherUploadFile.class);

			this.voucherUploadFileRepository.insert(voucherToSave);
//			auditService.insertDataAudit(DBConstants.VOUCHER_UPLOAD, voucherToSave,
//					VoucherRequestMappingConstants.VOUCHER_UPLOAD, externalTransactionId, userName);

			LOG.info("uploadVoucher : voucherToSave{}", voucherToSave);
			
			return voucherToSave;
			
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "uploadVoucher", e.getMessage(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED);
		}
	}
	
    public void updateVoucherUpload(VoucherUploadFileDomain vouDom, VoucherUploadFile uploadedFile, String externalTransactionId, String userName) throws VoucherManagementException {
		
		try {
			VoucherUploadFile voucherToSave = modelMapper.map(vouDom, VoucherUploadFile.class);

			this.voucherUploadFileRepository.save(voucherToSave);
			auditService.updateDataAudit(DBConstants.VOUCHER_UPLOAD, voucherToSave, VoucherRequestMappingConstants.ACCOUNT_UPLOAD_FREE_VOUCHER, uploadedFile, externalTransactionId, userName);
			LOG.info("uploadVoucher : voucherToSave{}", voucherToSave);
			
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "uploadVoucher", e.getMessage(),
					VoucherManagementCode.VOUCHER_UPLOAD_FAILED);
		}
	}


	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param offerId 
	 * @param program 
	 * @param resultResponse 
	 * @return list of handback files for vouchers uploaded within input date range
	 * @throws VoucherManagementException 
	 */
	public void getFreeVoucherUploadHandbackFileList(String startDate, String endDate,
			String offerId, String program, FreeVoucherUploadHandbackFileResponse resultResponse) throws VoucherManagementException {
		
		List<FreeVoucherHandbackFileDto> handbackFileList  = null;
		try {
		
			Date fromDate = Utilities.changeStringToDateWithTimeFormat(startDate,
					OfferConstants.FROM_DATE_TIME.get());
			Date toDate = Utilities.changeStringToDateWithTimeFormat(endDate,
					OfferConstants.END_DATE_TIME.get());
			
			List<VoucherUploadFile> fileList  = !StringUtils.isEmpty(offerId)
					? voucherUploadFileRepository.findByProgramCodeIgnoreCaseAndOfferIdAndUploadedDateBetweenAndFileTypeIgnoreCase(program, offerId, fromDate, toDate, VoucherConstants.GENERATE_FREE_VOUCHER)
					: voucherUploadFileRepository.findByProgramCodeIgnoreCaseAndUploadedDateBetweenAndFileTypeIgnoreCase(program, fromDate, toDate, VoucherConstants.GENERATE_FREE_VOUCHER);
			
			if(!CollectionUtils.isEmpty(fileList)) {
			    handbackFileList = new ArrayList<>(fileList.size());
				
			    for(VoucherUploadFile file : fileList) {
			    	
			    	FreeVoucherHandbackFileDto fileDto = modelMapper.map(file, FreeVoucherHandbackFileDto.class);
			    	fileDto.setFileContentPresent(!StringUtils.isEmpty(file.getFileContent()));
			    	fileDto.setHandbackFileContentPresent(!StringUtils.isEmpty(file.getHandbackFile()));
			    	fileDto.setFileUploadedContent(null);
			    	fileDto.setHandbackFileContent(null);
			    	handbackFileList.add(fileDto);
			    }
			    
				resultResponse.setResult(VoucherManagementCode.HANDBACK_FILES_LISTED_SUCCESSFULLY.getId(), VoucherManagementCode.HANDBACK_FILES_LISTED_SUCCESSFULLY.getMsg());	
			} else {
				
				resultResponse.addErrorAPIResponse(VoucherManagementCode.HANDBACK_FILES_DATA_NOT_FOUND_IN_RANGE.getIntId(), VoucherManagementCode.HANDBACK_FILES_DATA_NOT_FOUND_IN_RANGE.getMsg());
				resultResponse.setResult(VoucherManagementCode.HANDBACK_FILES_LISTING_FAILED.getId(), VoucherManagementCode.HANDBACK_FILES_LISTING_FAILED.getMsg());
			}
			
			
		} catch (Exception e) {
			
			throw new VoucherManagementException(this.getClass().toString(), "getFreeVoucherUploadHandbackFileList", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			
		}
		
		resultResponse.setFreeVoucherUploadHandbackFileList(handbackFileList);
		
	}

	/**
	 * Returns specific handback file with given file id
	 * @param fileId
	 * @param program 
	 * @param resultResponse
	 * @throws VoucherManagementException 
	 */
	public void getFreeVoucherUploadSpecificHandbackFile(String fileId,
			String program, FreeVoucherUploadHandbackFileResponse resultResponse) throws VoucherManagementException {
		
		List<FreeVoucherHandbackFileDto> handbackFileList  = null;
		try {
		
			VoucherUploadFile file  = voucherUploadFileRepository.findByIdAndProgramCodeIgnoreCaseAndFileTypeIgnoreCase(fileId, program, VoucherConstants.GENERATE_FREE_VOUCHER);
			
			if(!ObjectUtils.isEmpty(file)) {
			   
				handbackFileList = new ArrayList<>(1);
				FreeVoucherHandbackFileDto handbackFileDto = modelMapper.map(file, FreeVoucherHandbackFileDto.class);
				handbackFileDto.setFileContentPresent(!StringUtils.isEmpty(file.getFileContent()));
				handbackFileDto.setHandbackFileContentPresent(!StringUtils.isEmpty(file.getHandbackFile()));
		    	voucherControllerHelper.setFileContents(handbackFileDto, file);
				handbackFileList.add(handbackFileDto);
			    resultResponse.setResult(VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTED_SUCCESSFULLY.getId(), VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTED_SUCCESSFULLY.getMsg());	
			
			} else {
				
				resultResponse.addErrorAPIResponse(VoucherManagementCode.HANDBACK_SPECIFIC_FILE_DATA_NOT_FOUND.getIntId(), VoucherManagementCode.HANDBACK_SPECIFIC_FILE_DATA_NOT_FOUND.getMsg());
				resultResponse.setResult(VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTING_FAILED.getId(), VoucherManagementCode.HANDBACK_SPECIFIC_FILE_LISTING_FAILED.getMsg());
			}
			
			
		} catch (Exception e) {
			
			throw new VoucherManagementException(this.getClass().toString(), "getFreeVoucherUploadSpecificHandbackFile", e.getMessage(),
					VoucherManagementCode.GENERIC_RUNTIME_EXCEPTION);
			
		}
		
		resultResponse.setFreeVoucherUploadHandbackFileList(handbackFileList);
		
	}
	
}
