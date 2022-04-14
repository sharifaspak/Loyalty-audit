package com.loyalty.marketplace.banners.outbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerServiceWithListResponse {
	
	private String responseCode;
	private String responseMsg;
	private List<BannerListResponseDto> banners;

}
