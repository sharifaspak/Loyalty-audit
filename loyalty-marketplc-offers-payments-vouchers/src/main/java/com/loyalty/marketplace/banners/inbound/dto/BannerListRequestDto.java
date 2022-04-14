package com.loyalty.marketplace.banners.inbound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BannerListRequestDto {

	private List<BannerRequestDto> banners;
	
}
