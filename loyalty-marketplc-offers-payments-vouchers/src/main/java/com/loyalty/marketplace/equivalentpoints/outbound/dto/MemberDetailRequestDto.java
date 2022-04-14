package com.loyalty.marketplace.equivalentpoints.outbound.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDetailRequestDto {

	private String accountNumber;
	private String documentId;
	private String membershipCode;
	private String emailId;
	private String loyaltyId;
	private String atgUserName;
	private boolean includeCrmInfo;
	private boolean includeEligiblePaymentMethods;
	private boolean includeEligibilityMatrix;
	private boolean incldeLinkedAccount;
	private boolean includeAccountInterest;
	private boolean includeCancelledAccount;

	private MemberDetailRequestDto(MemberDetailRequestDtoBuilder memberDetailRequestDtoBuilder) {
		super();
		this.accountNumber = memberDetailRequestDtoBuilder.accountNumber;
		this.documentId = memberDetailRequestDtoBuilder.documentId;
		this.membershipCode = memberDetailRequestDtoBuilder.membershipCode;
		this.emailId = memberDetailRequestDtoBuilder.emailId;
		this.loyaltyId = memberDetailRequestDtoBuilder.loyaltyId;
		this.atgUserName = memberDetailRequestDtoBuilder.atgUserName;
		this.includeCrmInfo = memberDetailRequestDtoBuilder.includeCrmInfo;
		this.includeEligiblePaymentMethods = memberDetailRequestDtoBuilder.includeEligiblePaymentMethods;
		this.includeEligibilityMatrix = memberDetailRequestDtoBuilder.includeEligibilityMatrix;
		this.incldeLinkedAccount = memberDetailRequestDtoBuilder.incldeLinkedAccount;
		this.includeAccountInterest = memberDetailRequestDtoBuilder.includeAccountInterest;
		this.includeCancelledAccount = memberDetailRequestDtoBuilder.includeCancelledAccount;
	}

	public static class MemberDetailRequestDtoBuilder {

		private String accountNumber;
		private String documentId;
		private String membershipCode;
		private String emailId;
		private String loyaltyId;
		private String atgUserName;
		private boolean includeCrmInfo;
		private boolean includeEligiblePaymentMethods;
		private boolean includeEligibilityMatrix;
		private boolean incldeLinkedAccount;
		private boolean includeAccountInterest;
		private boolean includeCancelledAccount;

		public MemberDetailRequestDtoBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public MemberDetailRequestDtoBuilder documentId(String documentId) {
			this.documentId = documentId;
			return this;
		}

		public MemberDetailRequestDtoBuilder membershipCode(String membershipCode) {
			this.membershipCode = membershipCode;
			return this;
		}

		public MemberDetailRequestDtoBuilder emailId(String emailId) {
			this.emailId = emailId;
			return this;
		}

		public MemberDetailRequestDtoBuilder loyaltyId(String loyaltyId) {
			this.loyaltyId = loyaltyId;
			return this;
		}

		public MemberDetailRequestDtoBuilder atgUserName(String atgUserName) {
			this.atgUserName = atgUserName;
			return this;
		}

		public MemberDetailRequestDtoBuilder includeCrmInfo(boolean includeCrmInfo) {
			this.includeCrmInfo = includeCrmInfo;
			return this;
		}

		public MemberDetailRequestDtoBuilder includeEligiblePaymentMethods(boolean includeEligiblePaymentMethods) {
			this.includeEligiblePaymentMethods = includeEligiblePaymentMethods;
			return this;
		}

		public MemberDetailRequestDtoBuilder includeEligibilityMatrix(boolean includeEligibilityMatrix) {
			this.includeEligibilityMatrix = includeEligibilityMatrix;
			return this;
		}

		public MemberDetailRequestDtoBuilder incldeLinkedAccount(boolean incldeLinkedAccount) {
			this.incldeLinkedAccount = incldeLinkedAccount;
			return this;
		}

		public MemberDetailRequestDtoBuilder includeAccountInterest(boolean includeAccountInterest) {
			this.includeAccountInterest = includeAccountInterest;
			return this;
		}

		public MemberDetailRequestDtoBuilder includeCancelledAccount(boolean includeCancelledAccount) {
			this.includeCancelledAccount = includeCancelledAccount;
			return this;
		}

		public MemberDetailRequestDto build() {
			return new MemberDetailRequestDto(this);
		}

	}
}
