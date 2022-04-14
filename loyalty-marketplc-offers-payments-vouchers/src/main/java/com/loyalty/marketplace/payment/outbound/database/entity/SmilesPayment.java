package com.loyalty.marketplace.payment.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SmilesPayment")
public class SmilesPayment {

	@Id
	private String smilesPaymentNo;	
	
	@Indexed
	private String orderId;
	
	@Indexed
	private String accountNumber;
	
	private String channelId;
	
	private String offerId;
	
	private String language;
	
	private Double pointsValue;
	
	private Double dirhamValue;
	
	private String paymentItem;
	
	private String typeOfPayment;
	
	private String epgTransactionId;
	
	private Integer reserveTxnId;
	
	private String ccNumber;
	
	private String ccExpDate;
	
	private String approvalCode;
	
	private Integer voucherDenomination;
	
	private String promoCode;
	
	private String ccToken;
	
	private String atgUsername;
	
	private String ccBrand;
	
	private String ccTxnId;
	
	private String token;
	
	private String subOfferId;
	
	private String count;
	
	private Date createdDate;
	
	private String createdUser;
	
	private String updatedUser;
	
	private Date dateLastUpdated;
	
	
	public String getSmilesPaymentNo() {
		return smilesPaymentNo;
	}
	public void setSmilesPaymentNo(String id) {
		this.smilesPaymentNo = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Double getPointsValue() {
		return pointsValue;
	}
	public void setPointsValue(Double pointsValue) {
		this.pointsValue = pointsValue;
	}
	public Double getDirhamValue() {
		return dirhamValue;
	}
	public void setDirhamValue(Double dirhamValue) {
		this.dirhamValue = dirhamValue;
	}
	public String getPaymentItem() {
		return paymentItem;
	}
	public void setPaymentItem(String paymentItem) {
		this.paymentItem = paymentItem;
	}
	public String getTypeOfPayment() {
		return typeOfPayment;
	}
	public void setTypeOfPayment(String typeOfPayment) {
		this.typeOfPayment = typeOfPayment;
	}
	public String getEpgTransactionId() {
		return epgTransactionId;
	}
	public void setEpgTransactionId(String epgTransactionId) {
		this.epgTransactionId = epgTransactionId;
	}
	public Integer getReserveTxnId() {
		return reserveTxnId;
	}
	public void setReserveTxnId(Integer reserveTxnId) {
		this.reserveTxnId = reserveTxnId;
	}
	public String getCcNumber() {
		return ccNumber;
	}
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	public String getCcExpDate() {
		return ccExpDate;
	}
	public void setCcExpDate(String ccExpDate) {
		this.ccExpDate = ccExpDate;
	}
	public String getApprovalCode() {
		return approvalCode;
	}
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	public Integer getVoucherDenomination() {
		return voucherDenomination;
	}
	public void setVoucherDenomination(Integer voucherDenomination) {
		this.voucherDenomination = voucherDenomination;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public String getCcToken() {
		return ccToken;
	}
	public void setCcToken(String ccToken) {
		this.ccToken = ccToken;
	}
	public String getAtgUsername() {
		return atgUsername;
	}
	public void setAtgUsername(String atgUsername) {
		this.atgUsername = atgUsername;
	}
	public String getCcBrand() {
		return ccBrand;
	}
	public void setCcBrand(String ccBrand) {
		this.ccBrand = ccBrand;
	}
	public String getCcTxnId() {
		return ccTxnId;
	}
	public void setCcTxnId(String ccTxnId) {
		this.ccTxnId = ccTxnId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSubOfferId() {
		return subOfferId;
	}
	public void setSubOfferId(String subOfferId) {
		this.subOfferId = subOfferId;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}
	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}
	
	@Override
	public String toString() {
		return "SmilesPayment [id=" + smilesPaymentNo + ", orderId=" + orderId + ", accountNumber=" + accountNumber + ", channelId="
				+ channelId + ", offerId=" + offerId + ", language=" + language + ", pointsValue=" + pointsValue
				+ ", dirhamValue=" + dirhamValue + ", paymentItem=" + paymentItem + ", typeOfPayment=" + typeOfPayment
				+ ", epgTransactionId=" + epgTransactionId + ", reserveTxnId=" + reserveTxnId + ", ccNumber=" + ccNumber
				+ ", ccExpDate=" + ccExpDate + ", approvalCode=" + approvalCode + ", voucherDenomination="
				+ voucherDenomination + ", promoCode=" + promoCode + ", ccToken=" + ccToken + ", atgUsername="
				+ atgUsername + ", ccBrand=" + ccBrand + ", ccTxnId=" + ccTxnId + ", token=" + token + ", subOfferId="
				+ subOfferId + ", count=" + count + ", createdDate=" + createdDate + ", createdUser=" + createdUser
				+ ", updatedUser=" + updatedUser + ", dateLastUpdated=" + dateLastUpdated + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((approvalCode == null) ? 0 : approvalCode.hashCode());
		result = prime * result + ((atgUsername == null) ? 0 : atgUsername.hashCode());
		result = prime * result + ((ccBrand == null) ? 0 : ccBrand.hashCode());
		result = prime * result + ((ccExpDate == null) ? 0 : ccExpDate.hashCode());
		result = prime * result + ((ccNumber == null) ? 0 : ccNumber.hashCode());
		result = prime * result + ((ccToken == null) ? 0 : ccToken.hashCode());
		result = prime * result + ((ccTxnId == null) ? 0 : ccTxnId.hashCode());
		result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((createdUser == null) ? 0 : createdUser.hashCode());
		result = prime * result + ((dateLastUpdated == null) ? 0 : dateLastUpdated.hashCode());
		result = prime * result + ((dirhamValue == null) ? 0 : dirhamValue.hashCode());
		result = prime * result + ((epgTransactionId == null) ? 0 : epgTransactionId.hashCode());
		result = prime * result + ((smilesPaymentNo == null) ? 0 : smilesPaymentNo.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((paymentItem == null) ? 0 : paymentItem.hashCode());
		result = prime * result + ((pointsValue == null) ? 0 : pointsValue.hashCode());
		result = prime * result + ((promoCode == null) ? 0 : promoCode.hashCode());
		result = prime * result + ((reserveTxnId == null) ? 0 : reserveTxnId.hashCode());
		result = prime * result + ((subOfferId == null) ? 0 : subOfferId.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((typeOfPayment == null) ? 0 : typeOfPayment.hashCode());
		result = prime * result + ((updatedUser == null) ? 0 : updatedUser.hashCode());
		result = prime * result + ((voucherDenomination == null) ? 0 : voucherDenomination.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmilesPayment other = (SmilesPayment) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (approvalCode == null) {
			if (other.approvalCode != null)
				return false;
		} else if (!approvalCode.equals(other.approvalCode))
			return false;
		if (atgUsername == null) {
			if (other.atgUsername != null)
				return false;
		} else if (!atgUsername.equals(other.atgUsername))
			return false;
		if (ccBrand == null) {
			if (other.ccBrand != null)
				return false;
		} else if (!ccBrand.equals(other.ccBrand))
			return false;
		if (ccExpDate == null) {
			if (other.ccExpDate != null)
				return false;
		} else if (!ccExpDate.equals(other.ccExpDate))
			return false;
		if (ccNumber == null) {
			if (other.ccNumber != null)
				return false;
		} else if (!ccNumber.equals(other.ccNumber))
			return false;
		if (ccToken == null) {
			if (other.ccToken != null)
				return false;
		} else if (!ccToken.equals(other.ccToken))
			return false;
		if (ccTxnId == null) {
			if (other.ccTxnId != null)
				return false;
		} else if (!ccTxnId.equals(other.ccTxnId))
			return false;
		if (channelId == null) {
			if (other.channelId != null)
				return false;
		} else if (!channelId.equals(other.channelId))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (createdUser == null) {
			if (other.createdUser != null)
				return false;
		} else if (!createdUser.equals(other.createdUser))
			return false;
		if (dateLastUpdated == null) {
			if (other.dateLastUpdated != null)
				return false;
		} else if (!dateLastUpdated.equals(other.dateLastUpdated))
			return false;
		if (dirhamValue == null) {
			if (other.dirhamValue != null)
				return false;
		} else if (!dirhamValue.equals(other.dirhamValue))
			return false;
		if (epgTransactionId == null) {
			if (other.epgTransactionId != null)
				return false;
		} else if (!epgTransactionId.equals(other.epgTransactionId))
			return false;
		if (smilesPaymentNo == null) {
			if (other.smilesPaymentNo != null)
				return false;
		} else if (!smilesPaymentNo.equals(other.smilesPaymentNo))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (paymentItem == null) {
			if (other.paymentItem != null)
				return false;
		} else if (!paymentItem.equals(other.paymentItem))
			return false;
		if (pointsValue == null) {
			if (other.pointsValue != null)
				return false;
		} else if (!pointsValue.equals(other.pointsValue))
			return false;
		if (promoCode == null) {
			if (other.promoCode != null)
				return false;
		} else if (!promoCode.equals(other.promoCode))
			return false;
		if (reserveTxnId == null) {
			if (other.reserveTxnId != null)
				return false;
		} else if (!reserveTxnId.equals(other.reserveTxnId))
			return false;
		if (subOfferId == null) {
			if (other.subOfferId != null)
				return false;
		} else if (!subOfferId.equals(other.subOfferId))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (typeOfPayment == null) {
			if (other.typeOfPayment != null)
				return false;
		} else if (!typeOfPayment.equals(other.typeOfPayment))
			return false;
		if (updatedUser == null) {
			if (other.updatedUser != null)
				return false;
		} else if (!updatedUser.equals(other.updatedUser))
			return false;
		if (voucherDenomination == null) {
			if (other.voucherDenomination != null)
				return false;
		} else if (!voucherDenomination.equals(other.voucherDenomination))
			return false;
		return true;
	}

}
