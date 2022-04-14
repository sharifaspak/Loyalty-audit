package com.loyalty.marketplace.gifting.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class LimitsDomain {

	private Double senderDayLimit;
	private Double senderWeekLimit;
	private Double senderMonthLimit;
	private Double receiverDayLimit;
	private Double receiverWeekLimit;
	private Double receiverMonthLimit;
	
	public LimitsDomain(LimitsBuilder limitsBuilder) {
		this.senderDayLimit = limitsBuilder.senderDayLimit;
		this.senderWeekLimit = limitsBuilder.senderWeekLimit;
		this.senderMonthLimit = limitsBuilder.senderMonthLimit;
		this.receiverDayLimit = limitsBuilder.receiverDayLimit;
		this.receiverWeekLimit = limitsBuilder.receiverWeekLimit;
		this.receiverMonthLimit = limitsBuilder.receiverMonthLimit;
	}

	public static class LimitsBuilder {

		private Double senderDayLimit;
		private Double senderWeekLimit;
		private Double senderMonthLimit;
		private Double receiverDayLimit;
		private Double receiverWeekLimit;
		private Double receiverMonthLimit;

		public LimitsBuilder() { super(); }

		public LimitsBuilder senderDayLimit(Double senderDayLimit) {
			this.senderDayLimit = senderDayLimit;
			return this;
		}

		public LimitsBuilder senderWeekLimit(Double senderWeekLimit) {
			this.senderWeekLimit = senderWeekLimit;
			return this;
		}

		public LimitsBuilder senderMonthLimit(Double senderMonthLimit) {
			this.senderMonthLimit = senderMonthLimit;
			return this;
		}
		
		public LimitsBuilder receiverDayLimit(Double receiverDayLimit) {
			this.receiverDayLimit = receiverDayLimit;
			return this;
		}

		public LimitsBuilder receiverWeekLimit(Double receiverWeekLimit) {
			this.receiverWeekLimit = receiverWeekLimit;
			return this;
		}

		public LimitsBuilder receiverMonthLimit(Double receiverMonthLimit) {
			this.receiverMonthLimit = receiverMonthLimit;
			return this;
		}
		
		public LimitsDomain build() {
			return new LimitsDomain(this);
		}
		
	}
	
}
