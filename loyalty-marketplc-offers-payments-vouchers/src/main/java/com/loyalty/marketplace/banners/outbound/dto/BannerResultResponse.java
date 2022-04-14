package com.loyalty.marketplace.banners.outbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.outbound.dto.ResultResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class BannerResultResponse extends ResultResponse{
	
	private List<BannerResponseDto> bannerList;

	public BannerResultResponse(String externalTransactionId) {
		super(externalTransactionId);
	}

}
