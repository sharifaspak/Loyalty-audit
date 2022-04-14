package com.loyalty.marketplace.voucher.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.outbound.database.entity.PendingFreeVouchersInfo;
import com.loyalty.marketplace.voucher.outbound.database.repository.PendingFreeVouchersInfoRepository;

import lombok.Getter;
import lombok.ToString;

@Getter
@Component
@ToString
public class PendingFreeVouchersInfoDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(PendingFreeVouchersInfoDomain.class);

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	PendingFreeVouchersInfoRepository pendingFreeVouchersInfoRepository;
	
	@Autowired
	AuditService auditService;
	
	private String id;
	private String programCode;	
	private String accountNumber;
	private String membershipCode;
	private String offerId;
	private String partyId;
	private String voucherType;
	private Integer denomination;
	private String externalTransactionId;
	private Date expiryDate;
	private String transactionId;
	private String voucherCode;
	private String status;
	private String reason;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
	
	public PendingFreeVouchersInfoDomain() {

	}
	
	public PendingFreeVouchersInfoDomain(PendingFreeVouchersInfoBuilder pendingFreeVouchersInfoBuilder) {
		this.id = pendingFreeVouchersInfoBuilder.id;
		this.programCode = pendingFreeVouchersInfoBuilder.programCode;
		this.accountNumber = pendingFreeVouchersInfoBuilder.accountNumber;
		this.membershipCode = pendingFreeVouchersInfoBuilder.membershipCode;
		this.offerId = pendingFreeVouchersInfoBuilder.offerId;
		this.partyId = pendingFreeVouchersInfoBuilder.partyId;
		this.voucherType = pendingFreeVouchersInfoBuilder.voucherType;
		this.denomination = pendingFreeVouchersInfoBuilder.denomination;
		this.externalTransactionId = pendingFreeVouchersInfoBuilder.externalTransactionId;
		this.expiryDate = pendingFreeVouchersInfoBuilder.expiryDate;
		this.transactionId = pendingFreeVouchersInfoBuilder.transactionId;
		this.voucherCode = pendingFreeVouchersInfoBuilder.voucherCode;
		this.status = pendingFreeVouchersInfoBuilder.status;
		this.reason = pendingFreeVouchersInfoBuilder.reason;
		this.createdDate = pendingFreeVouchersInfoBuilder.createdDate;
		this.createdUser = pendingFreeVouchersInfoBuilder.createdUser;
		this.updatedDate = pendingFreeVouchersInfoBuilder.updatedDate;
		this.updatedUser = pendingFreeVouchersInfoBuilder.updatedUser;
		
	}

	public static class PendingFreeVouchersInfoBuilder {
		
		private String id;
		private String programCode;	
		private String accountNumber;
		private String membershipCode;
		private String offerId;
		private String partyId;
		private String voucherType;
		private Integer denomination;
		private String externalTransactionId;
		private Date expiryDate;
		private String transactionId;
		private String voucherCode;
		private String status;
		private String reason;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;

		public PendingFreeVouchersInfoBuilder(String accountNumber, String membershipCode, String offerId,
				String partyId, String voucherType, String externalTransactionId) {
			
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.offerId = offerId;
			this.partyId = partyId;
			this.voucherType = voucherType;
			this.externalTransactionId = externalTransactionId;
			
		}
		
		public PendingFreeVouchersInfoBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder denomination(Integer denomination) {
			this.denomination = denomination;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder expiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder transactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder voucherCode(String voucherCode) {
			this.voucherCode = voucherCode;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder reason(String reason) {
			this.reason = reason;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public PendingFreeVouchersInfoBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public PendingFreeVouchersInfoDomain build() {
			return new PendingFreeVouchersInfoDomain(this);
		}
	}	
	
	
	/**
	 * 
	 * @param pendingVoucherInfoDomainList
	 * @param headers
	 */
	public void saveParkedVouchersInfoList(List<PendingFreeVouchersInfoDomain> pendingVoucherInfoDomainList,
			Headers headers) {
		
		try {
			LOG.info("Inside saveParkedVouchersInfoList in VoucherControllerHelper");
			List<PendingFreeVouchersInfo> parkedVouchersToSave = new ArrayList<>(pendingVoucherInfoDomainList.size());
			pendingVoucherInfoDomainList.forEach(p->parkedVouchersToSave.add(modelMapper.map(p, PendingFreeVouchersInfo.class)));
			pendingFreeVouchersInfoRepository.saveAll(parkedVouchersToSave);
			LOG.info("Saved details of free vouchers at {}", new Date());
			LOG.info("Leaving saveParkedVouchersInfoList in VoucherControllerHelper");
		} catch(Exception e) {
			
			e.printStackTrace();
			LOG.error("Error occured in saving parked vouchers to db : {} ", e.getMessage());
		}
		
	}
	
}
