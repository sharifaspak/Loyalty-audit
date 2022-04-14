package com.loyalty.marketplace.voucher.domain;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliation;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherReconciliationRepository;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Component
public class VoucherReconciliationDomain {
	@Autowired
	VoucherReconciliationRepository voucherReconciliationRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuditService auditService;
	
	private static final Logger LOG = LoggerFactory.getLogger(VoucherReconciliationDomain.class);

	private String programCode;
	private String id;
	private String partnerCode;
	private Date startDateTime;
	private Date endDateTime;
	private ReconciliationInfoDomain loyalty;
	private ReconciliationInfoDomain partner;
	private VoucherReconciliationDataDomain reconciliationRefId;
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

	public String getPartnerCode() {
		return partnerCode;
	}

	public VoucherReconciliationDataDomain getReconciliationRefId() {
		return reconciliationRefId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public ReconciliationInfoDomain getLoyalty() {
		return loyalty;
	}

	public ReconciliationInfoDomain getPartner() {
		return partner;
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

	public VoucherReconciliationDomain() {

	}

	public VoucherReconciliationDomain(VoucherReconciliationBuilder voucherBuilder) {
		this.programCode = voucherBuilder.programCode;
		this.partnerCode = voucherBuilder.partnerCode;
		this.startDateTime = voucherBuilder.startDateTime;
		this.endDateTime = voucherBuilder.endDateTime;
		this.loyalty = voucherBuilder.loyalty;
		this.partner = voucherBuilder.partner;
		this.reconciliationRefId = voucherBuilder.reconciliationRefId;
		this.createdDate = voucherBuilder.createdDate;
		this.createdUser = voucherBuilder.createdUser;
		this.updatedDate = voucherBuilder.updatedDate;
		this.updatedUser = voucherBuilder.updatedUser;

	}

	public static class VoucherReconciliationBuilder {

		private String programCode;
		private String partnerCode;
		private Date startDateTime;
		private Date endDateTime;
		private ReconciliationInfoDomain loyalty;
		private ReconciliationInfoDomain partner;
		private VoucherReconciliationDataDomain reconciliationRefId;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;

		public VoucherReconciliationBuilder(String programCode, String partnerCode, Date startDateTime,
				Date endDateTime, ReconciliationInfoDomain loyalty, ReconciliationInfoDomain partner,
				VoucherReconciliationDataDomain reconciliationRefId, Date createdDate, String createdUser,
				Date updatedDate, String updatedUser) {
			super();
			this.programCode = programCode;
			this.partnerCode = partnerCode;
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;
			this.loyalty = loyalty;
			this.partner = partner;
			this.reconciliationRefId = reconciliationRefId;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
		}

		public VoucherReconciliationBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public VoucherReconciliationBuilder partnerCode(String partnerCode) {
			this.partnerCode = partnerCode;
			return this;
		}

		public VoucherReconciliationBuilder startDateTime(Date startDateTime) {
			this.startDateTime = startDateTime;
			return this;
		}

		public VoucherReconciliationBuilder endDateTime(Date endDateTime) {
			this.endDateTime = endDateTime;
			return this;
		}

		public VoucherReconciliationBuilder loyalty(ReconciliationInfoDomain loyalty) {
			this.loyalty = loyalty;
			return this;
		}

		public VoucherReconciliationBuilder partner(ReconciliationInfoDomain partner) {
			this.partner = partner;
			return this;
		}

		public VoucherReconciliationBuilder reconciliationRefId(VoucherReconciliationDataDomain reconciliationRefId) {
			this.reconciliationRefId = reconciliationRefId;
			return this;
		}

		public VoucherReconciliationBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public VoucherReconciliationBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public VoucherReconciliationBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public VoucherReconciliationBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public VoucherReconciliationDomain build() {
			return new VoucherReconciliationDomain(this);
		}

	}

	public String saveVoucherReconcile(VoucherReconciliationDomain vouDom, String externalTransactionId, String userName) throws VoucherManagementException {
		LOG.info("voucherReconciliation VoucherReconciliationDomain: {}", vouDom);
		try {
			VoucherReconciliation voucherReconciliationToSave = modelMapper.map(vouDom, VoucherReconciliation.class);

			VoucherReconciliation result = this.voucherReconciliationRepository.insert(voucherReconciliationToSave);
//			auditService.insertDataAudit(DBConstants.VOUCHER_RECONCILIATION, voucherReconciliationToSave,
//					VoucherRequestMappingConstants.VOUCHER_RECONCILIATION, externalTransactionId, userName);

			LOG.info("voucherReconciliation : voucherReconciliationToSave{}", voucherReconciliationToSave);
			return result.getId();
		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "voucherReconciliation", e.getMessage(),
					VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL);
		}
	}
}
