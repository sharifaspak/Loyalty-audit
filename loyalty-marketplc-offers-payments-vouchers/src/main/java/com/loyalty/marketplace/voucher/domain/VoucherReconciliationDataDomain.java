package com.loyalty.marketplace.voucher.domain;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.voucher.constants.DBConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.constants.VoucherRequestMappingConstants;
import com.loyalty.marketplace.voucher.maf.outbound.dto.VoucherReconciliationResponse.ResponseData.VoucherDetails.Data.Orders;
import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherReconciliationData;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherReconciliationDataRepository;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;

@Component
public class VoucherReconciliationDataDomain {
	@Autowired
	VoucherReconciliationDataRepository voucherReconciliationDataRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuditService auditService;
	
	private static final Logger LOG = LoggerFactory.getLogger(VoucherReconciliationDataDomain.class);

	private String id;
	private String programCode;
	private String reconciliationLevel;
	private List<ReconciliationDataInfoDomain> loyaltyReconciliationData;
	private List<ReconciliationDataInfoDomain> partnerReconciliationData;
	private List<Orders> partnerContent;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;

	public String getId() {
		return id;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getReconciliationLevel() {
		return reconciliationLevel;
	}

	public List<ReconciliationDataInfoDomain> getLoyaltyReconciliationData() {
		return loyaltyReconciliationData;
	}

	public List<ReconciliationDataInfoDomain> getPartnerReconciliationData() {
		return partnerReconciliationData;
	}

	public List<Orders> getPartnerContent() {
		return partnerContent;
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

	public VoucherReconciliationDataDomain() {

	}

	public VoucherReconciliationDataDomain(VoucherReconciliationDataBuilder voucherReconciliationDataBuilder) {
		this.programCode = voucherReconciliationDataBuilder.programCode;
		this.reconciliationLevel = voucherReconciliationDataBuilder.reconciliationLevel;
		this.loyaltyReconciliationData = voucherReconciliationDataBuilder.loyaltyReconciliationData;
		this.partnerReconciliationData = voucherReconciliationDataBuilder.partnerReconciliationData;
		this.partnerContent = voucherReconciliationDataBuilder.partnerContent;
		this.createdDate = voucherReconciliationDataBuilder.createdDate;
		this.createdUser = voucherReconciliationDataBuilder.createdUser;
		this.updatedDate = voucherReconciliationDataBuilder.updatedDate;
		this.updatedUser = voucherReconciliationDataBuilder.updatedUser;
		this.id = voucherReconciliationDataBuilder.id;
	}

	public static class VoucherReconciliationDataBuilder {

		private String programCode;
		private String reconciliationLevel;
		private List<ReconciliationDataInfoDomain> loyaltyReconciliationData;
		private List<ReconciliationDataInfoDomain> partnerReconciliationData;
		private List<Orders> partnerContent;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		private String id;

		public VoucherReconciliationDataBuilder(String programCode, String reconciliationLevel,
				List<ReconciliationDataInfoDomain> loyaltyReconciliationData,
				List<ReconciliationDataInfoDomain> partnerReconciliationData, List<Orders> partnerContent,
				Date createdDate, String createdUser, Date updatedDate, String updatedUser, String id) {
			super();
			this.programCode = programCode;
			this.reconciliationLevel = reconciliationLevel;
			this.loyaltyReconciliationData = loyaltyReconciliationData;
			this.partnerReconciliationData = partnerReconciliationData;
			this.partnerContent = partnerContent;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;
			this.updatedUser = updatedUser;
			this.id = id;
		}

		public VoucherReconciliationDataBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public VoucherReconciliationDataBuilder partnerCode(String reconciliationLevel) {
			this.reconciliationLevel = reconciliationLevel;
			return this;
		}

		public VoucherReconciliationDataBuilder loyaltyReconciliationData(
				List<ReconciliationDataInfoDomain> loyaltyReconciliationData) {
			this.loyaltyReconciliationData = loyaltyReconciliationData;
			return this;
		}

		public VoucherReconciliationDataBuilder partnerReconciliationData(
				List<ReconciliationDataInfoDomain> partnerReconciliationData) {
			this.partnerReconciliationData = partnerReconciliationData;
			return this;
		}

		public VoucherReconciliationDataBuilder partnerContent(List<Orders> partnerContent) {
			this.partnerContent = partnerContent;
			return this;
		}

		public VoucherReconciliationDataBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public VoucherReconciliationDataBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}

		public VoucherReconciliationDataBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public VoucherReconciliationDataBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}

		public VoucherReconciliationDataBuilder id(String id) {
			this.id = id;
			return this;
		}

		public VoucherReconciliationDataDomain build() {
			return new VoucherReconciliationDataDomain(this);
		}

	}

	public String saveVoucherReconcileData(VoucherReconciliationDataDomain vouDom,
			String externalTransactionId, String userName) throws VoucherManagementException {
		LOG.info("voucherReconciliationdata VoucherReconciliationDataDomain: {}", vouDom);
		try {
			VoucherReconciliationData voucherReconciliationDataToSave = modelMapper.map(vouDom,
					VoucherReconciliationData.class);
			LOG.info("voucherReconciliationData : voucherReconciliationDataToSave{}", voucherReconciliationDataToSave);
			VoucherReconciliationData result = this.voucherReconciliationDataRepository
					.insert(voucherReconciliationDataToSave);
//			auditService.insertDataAudit(DBConstants.VOUCHER_RECONCILIATION_DATA, voucherReconciliationDataToSave,VoucherRequestMappingConstants.VOUCHER_RECONCILIATION,
//					externalTransactionId, userName);

			return result.getId();

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), "voucherReconciliationData",
					e.getMessage(), VoucherManagementCode.VOUCHER_RECONCILIATION_FAIL);
		}
	}

}
