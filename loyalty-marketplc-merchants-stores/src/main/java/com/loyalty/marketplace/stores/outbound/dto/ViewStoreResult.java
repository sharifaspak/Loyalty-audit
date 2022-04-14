package com.loyalty.marketplace.stores.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.stores.inbound.dto.StoreDto;

public class ViewStoreResult {

	private String response;

	private String description;

	private String transactionId;

	private String memberId;

	private String memberStatus;
	
	private List<StoreDto> partners;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	public List<StoreDto> getPartners() {
		return partners;
	}

	public void setPartners(List<StoreDto> partners) {
		this.partners = partners;
	}

	@Override
	public String toString() {
		return "ViewPartnerResult [response=" + response + ", description=" + description + ", transactionId="
				+ transactionId + ", memberId=" + memberId + ", memberStatus=" + memberStatus + ", partners=" + partners
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((memberId == null) ? 0 : memberId.hashCode());
		result = prime * result + ((memberStatus == null) ? 0 : memberStatus.hashCode());
		result = prime * result + ((partners == null) ? 0 : partners.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		ViewStoreResult other = (ViewStoreResult) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		if (memberStatus == null) {
			if (other.memberStatus != null)
				return false;
		} else if (!memberStatus.equals(other.memberStatus))
			return false;
		if (partners == null) {
			if (other.partners != null)
				return false;
		} else if (!partners.equals(other.partners))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}
	
	
	
}
