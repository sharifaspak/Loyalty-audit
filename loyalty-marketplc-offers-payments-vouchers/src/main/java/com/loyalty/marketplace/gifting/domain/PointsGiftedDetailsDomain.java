package com.loyalty.marketplace.gifting.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Component
public class PointsGiftedDetailsDomain {

	private Integer pointsGifted;
	private String senderTransactionRefId;
	private String receiverTransactionRefId;

	public PointsGiftedDetailsDomain(PointsGiftedDetailsBuilder pointsGiftedDetailsBuilder) {
		this.pointsGifted = pointsGiftedDetailsBuilder.pointsGifted;
		this.senderTransactionRefId = pointsGiftedDetailsBuilder.senderTransactionRefId;
		this.receiverTransactionRefId = pointsGiftedDetailsBuilder.receiverTransactionRefId;
	}

	public static class PointsGiftedDetailsBuilder {

		private Integer pointsGifted;
		private String senderTransactionRefId;
		private String receiverTransactionRefId;

		public PointsGiftedDetailsBuilder(Integer pointsGifted) {
			super();
			this.pointsGifted = pointsGifted;
		}
		
		public PointsGiftedDetailsBuilder pointsGifted(Integer pointsGifted) {
			this.pointsGifted = pointsGifted;
			return this;
		}

		public PointsGiftedDetailsBuilder senderTransactionRefId(String senderTransactionRefId) {
			this.senderTransactionRefId = senderTransactionRefId;
			return this;
		}

		public PointsGiftedDetailsBuilder receiverTransactionRefId(String receiverTransactionRefId) {
			this.receiverTransactionRefId = receiverTransactionRefId;
			return this;
		}
		
		public PointsGiftedDetailsDomain build() {
			return new PointsGiftedDetailsDomain(this);
		}
		
	}
	
}
