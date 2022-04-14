package com.loyalty.marketplace.gifting.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class BirthdayReminderListDomain {

	private String accountNumber;
	private String membershipCode;
	private String firstName;
	private String lastName;
	private String remindPrior;
	private String status;
	
	public BirthdayReminderListDomain(BirthdayReminderListBuilder birthdayReminderListBuilder) {
		this.accountNumber = birthdayReminderListBuilder.accountNumber;
		this.membershipCode = birthdayReminderListBuilder.membershipCode;
		this.firstName = birthdayReminderListBuilder.firstName ;
		this.lastName = birthdayReminderListBuilder.lastName;
		this.remindPrior = birthdayReminderListBuilder.remindPrior;
		this.status = birthdayReminderListBuilder.status;
	}
	
	public static class BirthdayReminderListBuilder {

		private String accountNumber;
		private String membershipCode;
		private String firstName;
		private String lastName;
		private String remindPrior;
		private String status;

		public BirthdayReminderListBuilder(String accountNumber,
				String membershipCode, String remindPrior, String status) {
			this.accountNumber = accountNumber;
			this.membershipCode = membershipCode;
			this.remindPrior = remindPrior;
			this.status = status;
		}

		public BirthdayReminderListBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public BirthdayReminderListBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public BirthdayReminderListDomain build() {
			return new BirthdayReminderListDomain(this);
		}

	}
	
}
