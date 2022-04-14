package com.loyalty.marketplace.voucher.domain;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherActionRepository;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Component
public class VoucherActionDomain {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	VoucherActionRepository voucherActionRepository;
	
	@Autowired
	AuditService auditService;

	private static final Logger LOG = LoggerFactory.getLogger(VoucherActionDomain.class);
	
	private String id;
	private String program;
	private String action;
	private String redemptionMethod;
	private String label;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;

	public String getProgram() {
		return program;
	}

	public String getId() {
		return id;
	}
	public String getAction() {
		return action;
	}
	public String getRedemptionMethod() {
		return redemptionMethod;
	}
	public String getLabel() {
		return label;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public VoucherActionDomain() {}
	
	public VoucherActionDomain(VoucherActionBuilder voucherActionBuilder) {
		this.id = voucherActionBuilder.id;
		this.action = voucherActionBuilder.action;
		this.redemptionMethod = voucherActionBuilder.redemptionMethod;
		this.label = voucherActionBuilder.label;
		this.createdUser = voucherActionBuilder.createdUser;
		this.createdDate = voucherActionBuilder.createdDate;
		this.updatedUser = voucherActionBuilder.updatedUser;
		this.updatedDate = voucherActionBuilder.updatedDate;
		this.program = voucherActionBuilder.program;
	}

	public static class VoucherActionBuilder{
		
		private String program;
		private String id;
		private String action;
		private String redemptionMethod;
		private String label;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;
		
		public VoucherActionBuilder(String program,String action, String redemptionMethod, String label) {
			super();
			this.program = program;
			this.action = action;
			this.redemptionMethod = redemptionMethod;
			this.label = label;
		}
		
		public VoucherActionBuilder id(String id) {
			this.id = id;
			return this;
		}

		public VoucherActionBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public VoucherActionBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public VoucherActionBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public VoucherActionBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public VoucherActionDomain build() {
			return new VoucherActionDomain(this);
		}
		
		
	}

	public void saveVoucherAction(List<VoucherActionDomain> voucherActionToSave, String externalTransactionId, String userName) throws VoucherManagementException {
		
		LOG.info("saveVoucherAction voucherActionDomain: {}", voucherActionToSave);
		try {
			
			List<VoucherAction> voucherResult = voucherActionToSave.stream()
					.map(u -> {
						return modelMapper.map(u, VoucherAction.class);
					}).collect(Collectors.toList());
			this.voucherActionRepository.saveAll(voucherResult);
//			auditService.insertDataAudit(DBConstants.VOUCHER_ACTION, voucherResult,VoucherRequestMappingConstants.VOUCHER_ACTION, externalTransactionId, userName);
			LOG.info("saveFunctionality functionality: {}", voucherResult);
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "saveVoucherAction", e.getMessage(),
					VoucherManagementCode.VOUCHER_ACTION_CREATION_FAILED);
		}
	}
}
