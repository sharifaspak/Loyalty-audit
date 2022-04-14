package com.loyalty.marketplace.gifting.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class MemberInfoDomain {

	private String accountNumber;
	private String membershipCode;
	private String firstName;
	private String lastName;
	private String email;

	public MemberInfoDomain(MemberInfoBuilder memberInfoBuilder) {
		this.accountNumber = memberInfoBuilder.accountNumber;
		this.membershipCode = memberInfoBuilder.membershipCode;
		this.firstName = memberInfoBuilder.firstName;
		this.lastName = memberInfoBuilder.lastName;
		this.email = memberInfoBuilder.email;
	}

	public static class MemberInfoBuilder {

		private String accountNumber;
		private String membershipCode;
		private String firstName;
		private String lastName;
		private String email;

		public MemberInfoBuilder(String accountNumber, String membershipCode) {
			super();
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
		}

		public MemberInfoBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public MemberInfoBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public MemberInfoBuilder email(String email) {
			this.email = email;
			return this;
		}
		
		public MemberInfoDomain build() {
			return new MemberInfoDomain(this);
		}
		
	}
	
}

