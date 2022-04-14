package com.loyalty.marketplace.banners.constants;

import com.loyalty.marketplace.constants.RequestMappingConstants;

public class BannerRequestMappingConstants  extends RequestMappingConstants {

	public static final String CREATE_BANNER = "/banners";
	public static final String UPDATE_BANNER = "/banners/{id}";
	public static final String FETCH_BANNER_LIST = "/banners";
	public static final String FETCH_SPECIFIC_BANNER = "/banners/{id}";
	public static final String DELETE_BANNER = "/banners/{id}";

	public static final String ID = "id";
}
