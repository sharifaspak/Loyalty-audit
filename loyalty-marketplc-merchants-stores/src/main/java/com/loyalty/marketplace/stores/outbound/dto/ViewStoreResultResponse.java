package com.loyalty.marketplace.stores.outbound.dto;

import com.loyalty.marketplace.outbound.dto.CommonApiStatus;

public class ViewStoreResultResponse {
	
	private CommonApiStatus apiStatus;
	
	private ViewStoreResult viewPartnerResult;

	public CommonApiStatus getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(CommonApiStatus apiStatus) {
		this.apiStatus = apiStatus;
	}

	public ViewStoreResult getViewPartnerResult() {
		return viewPartnerResult;
	}

	public void setViewPartnerResult(ViewStoreResult viewPartnerResult) {
		this.viewPartnerResult = viewPartnerResult;
	}

	@Override
	public String toString() {
		return "ViewPartnerResultResponse [apiStatus=" + apiStatus + ", viewPartnerResult=" + viewPartnerResult + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiStatus == null) ? 0 : apiStatus.hashCode());
		result = prime * result + ((viewPartnerResult == null) ? 0 : viewPartnerResult.hashCode());
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
		ViewStoreResultResponse other = (ViewStoreResultResponse) obj;
		if (apiStatus == null) {
			if (other.apiStatus != null)
				return false;
		} else if (!apiStatus.equals(other.apiStatus))
			return false;
		if (viewPartnerResult == null) {
			if (other.viewPartnerResult != null)
				return false;
		} else if (!viewPartnerResult.equals(other.viewPartnerResult))
			return false;
		return true;
	}
	
	

}
