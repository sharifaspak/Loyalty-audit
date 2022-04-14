package com.loyalty.marketplace.offers.promocode.outbound.dto;

import java.util.List;

import com.loyalty.marketplace.offers.promocode.outbound.database.entity.PromoCode;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PromoCodeResponse extends ResultResponse{
	public PromoCodeResponse(String externalTransactionId) {
		super(externalTransactionId);
	}
	
	List<PromoCode> promoCoderesult;

}

