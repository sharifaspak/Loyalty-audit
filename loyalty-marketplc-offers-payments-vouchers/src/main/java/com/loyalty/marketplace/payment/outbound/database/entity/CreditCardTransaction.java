package com.loyalty.marketplace.payment.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "CreditCardTransaction")
public class CreditCardTransaction {

		@Id
		private String id;	
		
		@Indexed
		@Field("OrderId")
		private String orderId;
		@Indexed
		@Field("AccountNumber")
		private String accountNumber;
		@Field("ChannelId")
		private String channelId;
		@Field("OfferId")
		private String offerId;
		@Field("Language")
		private String language;
		@Field("PaymentValues")
		private PaymentValues paymentValues;
		@Field("PaymentItem")
		private String paymentItem;
		@Field("TypeOfPayment")
		private String typeOfPayment;
		@Field("EPGTransactionId")
		private String epgTransactionId;
		@Field("ReserveTxnId")
		private Integer reserveTxnId;
		@Field("CCNumber")
		private String ccNumber;
		@Field("CCExpDate")
		private String ccExpDate;
		@Field("ApprovalCode")
		private String approvalCode;
		@Field("VoucherDenomination")
		private Integer voucherDenomination;
		@Field("PromoCode")
		private String promoCode;
		@Field("CCToken")
		private String ccToken;
		@Field("ATGUsername")
		private String atgUsername;
		@Field("CCBrand")
		private String ccBrand;
		@Field("CCTxnId")
		private String ccTxnId;
		@Field("Token")
		private String token;
		@Field("SubOfferId")
		private String subOfferId;
		@Field("Count")
		private String count;
		@Field("CreatedDate")
		private Date createdDate;
		@Field("CreatedUser")
		private String createdUser;
		@Field("UpdatedUser")
		private String updatedUser;
		@Field("UpdatedDate")
		private Date dateLastUpdated;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CreditCardTransaction [id=" + id + ", orderId=" + orderId + ", accountNumber=" + accountNumber
					+ ", channelId=" + channelId + ", offerId=" + offerId + ", language=" + language
					+ ", paymentValues=" + paymentValues + ", paymentItem=" + paymentItem + ", typeOfPayment="
					+ typeOfPayment + ", epgTransactionId=" + epgTransactionId + ", reserveTxnId=" + reserveTxnId
					+ ", ccNumber=" + ccNumber + ", ccExpDate=" + ccExpDate + ", approvalCode=" + approvalCode
					+ ", voucherDenomination=" + voucherDenomination + ", promoCode=" + promoCode + ", ccToken="
					+ ccToken + ", atgUsername=" + atgUsername + ", ccBrand=" + ccBrand + ", ccTxnId=" + ccTxnId
					+ ", token=" + token + ", subOfferId=" + subOfferId + ", count=" + count + ", createdDate="
					+ createdDate + ", createdUser=" + createdUser + ", updatedUser=" + updatedUser
					+ ", dateLastUpdated=" + dateLastUpdated + "]";
		}

		
}
