package com.loyalty.marketplace.banners.constants;

public enum BannerConstants {
	
    CREATE_BANNER_METHOD("createBanner"),
    UPDATE_BANNER_METHOD("updateBanner"),
    LIST_BANNERS_METHOD("listBanners"), 
    LIST_SPECIFIC_BANNER_METHOD("listSpecificBanner"), 
    DELETE_BANNER_METHOD("deleteBanner"), 
    BANNER_SUCCESS_CODE("200"), 
    BANNER_AUTHENTICATION("Authentication"),
    BANNER_CREATION("BannerCreate"),
    BANNER_UPDATION("BannerUpdate"),
    BANNER_LISTING("BannerList"),
    BANNER_DETAIL("BannerDetails"),
    BANNER_DELETION("BannerDelete"),
    COMMA_OPERATOR(", "),
    MESSAGE_SEPARATOR(" : ")
	;
	
	private final String constant;

	BannerConstants(String constant) {
		this.constant = constant;
	}

	public String get() {
		return this.constant;
	}

}
