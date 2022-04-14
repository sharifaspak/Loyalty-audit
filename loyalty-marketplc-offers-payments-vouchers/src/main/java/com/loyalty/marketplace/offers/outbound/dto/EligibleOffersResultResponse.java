package com.loyalty.marketplace.offers.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.offers.outbound.database.entity.EligibleOffers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class EligibleOffersResultResponse extends ResultResponse{
	
	private Integer totalRecordCount;
	private List<EligibleOffers> eligibleOfferList;

	public EligibleOffersResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	

}