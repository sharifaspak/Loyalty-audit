package com.loyalty.marketplace.image.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class ImageDimensions {

	//MERCHANT - WEB
	public static int MERCHANT_WEB_LISTING_L;
	public static int MERCHANT_WEB_LISTING_H;
	public static int MERCHANT_WEB_DETAIL_L;
	public static int MERCHANT_WEB_DETAIL_H;
	
	@Value("${web.merchant.listing.length}")
    public void setMerchantWebListingL(String dimension) {
		MERCHANT_WEB_LISTING_L = Integer.parseInt(dimension);
	}
	
	@Value("${web.merchant.listing.height}")
    public void setMerchantWebListingH(String dimension) {
		MERCHANT_WEB_LISTING_H = Integer.parseInt(dimension);
	}
	
	@Value("${web.merchant.detail.length}")
    public void setMerchantWebDetailL(String dimension) {
		MERCHANT_WEB_DETAIL_L = Integer.parseInt(dimension);
	}
	
	@Value("${web.merchant.detail.height}")
    public void setMerchantWebDetailH(String dimension) {
		MERCHANT_WEB_DETAIL_H = Integer.parseInt(dimension);
	}
    
	//MERCHANT - APP
	public static int MERCHANT_APP_COUPONS_L;
	public static int MERCHANT_APP_COUPONS_H;
	public static int MERCHANT_APP_DETAIL_L;
	public static int MERCHANT_APP_DETAIL_H;
	public static int MERCHANT_APP_LISTING_L;
	public static int MERCHANT_APP_LISTING_H;
	public static int MERCHANT_APP_LIST_LOGO_L;
	public static int MERCHANT_APP_LIST_LOGO_H;
	
	@Value("${app.merchant.coupons.length}")
    public void setMerchantAppCouponsL(String dimension) {
		MERCHANT_APP_COUPONS_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.coupons.height}")
    public void setMerchantAppCouponsH(String dimension) {
		MERCHANT_APP_COUPONS_H = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.detail.length}")
    public void setMerchantAppDetailL(String dimension) {
		MERCHANT_APP_DETAIL_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.detail.height}")
    public void setMerchantAppDetailH(String dimension) {
		MERCHANT_APP_DETAIL_H = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.listing.length}")
    public void setMerchantAppListingL(String dimension) {
		MERCHANT_APP_LISTING_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.listing.height}")
    public void setMerchantAppListingH(String dimension) {
		MERCHANT_APP_LISTING_H = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.listlogo.length}")
    public void setMerchantAppListLogoL(String dimension) {
		MERCHANT_APP_LIST_LOGO_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.merchant.listlogo.height}")
    public void setMerchantAppListLogoH(String dimension) {
		MERCHANT_APP_LIST_LOGO_H = Integer.parseInt(dimension);
	}
	
	//OFFER - WEB
	public static int OFFER_WEB_DETAIL_L;
	public static int OFFER_WEB_DETAIL_H;
	public static int OFFER_WEB_LISTING_L;
	public static int OFFER_WEB_LISTING_H;
	public static int OFFER_WEB_MY_COUPONS_L;
	public static int OFFER_WEB_MY_COUPONS_H;
	public static int OFFER_WEB_FEATURED_L;
	public static int OFFER_WEB_FEATURED_H;
	
	@Value("${web.offer.detail.length}")
    public void setOfferWebDetailL(String dimension) {
		OFFER_WEB_DETAIL_L = Integer.parseInt(dimension);
	}
	
	@Value("${web.offer.detail.height}")
    public void setOfferWebDetailH(String dimension) {
		OFFER_WEB_DETAIL_H = Integer.parseInt(dimension);
	}
	
	@Value("${web.offer.listing.length}")
    public void setOfferWebListingL(String dimension) {
		OFFER_WEB_LISTING_L = Integer.parseInt(dimension);
	}

	@Value("${web.offer.listing.height}")
    public void setOfferWebListingH(String dimension) {
		OFFER_WEB_LISTING_H = Integer.parseInt(dimension);
	}
	
	@Value("${web.offer.mycoupons.length}")
    public void setOfferWebMyCouponsL(String dimension) {
		OFFER_WEB_MY_COUPONS_L = Integer.parseInt(dimension);
	}
	
	@Value("${web.offer.mycoupons.height}")
    public void setOfferWebMyCouponsH(String dimension) {
		OFFER_WEB_MY_COUPONS_H = Integer.parseInt(dimension);
	}
	
	@Value("${web.offer.featured.length}")
    public void setOfferWebFeaturedL(String dimension) {
		OFFER_WEB_FEATURED_L = Integer.parseInt(dimension);
	}

	@Value("${web.offer.featured.height}")
    public void setOfferWebFeaturedH(String dimension) {
		OFFER_WEB_FEATURED_H = Integer.parseInt(dimension);
	}
	
	//OFFER - APP
	public static int OFFER_APP_DOD_LISTING_NEW_L;
	public static int OFFER_APP_DOD_LISTING_NEW_H;
	public static int OFFER_APP_SMALL_HORIZONTAL_NEW_L;
	public static int OFFER_APP_SMALL_HORIZONTAL_NEW_H;
//	public static int OFFER_APP_DETAIL_NEW_L;
//	public static int OFFER_APP_DETAIL_NEW_H;
	public static int OFFER_APP_DETAIL_L;
	public static int OFFER_APP_DETAIL_H;
	public static int OFFER_APP_LUCKY_OFFER_NEW_L;
	public static int OFFER_APP_LUCKY_OFFER_NEW_H;
	
	@Value("${app.offer.dodlistingnew.length}")
    public void setOfferAppDodListingNewL(String dimension) {
		OFFER_APP_DOD_LISTING_NEW_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.dodlistingnew.height}")
    public void setOfferAppDodListingNewH(String dimension) {
		OFFER_APP_DOD_LISTING_NEW_H = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.smallhorizontalnew.length}")
    public void setOfferAppSmallHorizontalNewL(String dimension) {
		OFFER_APP_SMALL_HORIZONTAL_NEW_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.smallhorizontalnew.height}")
    public void setOfferAppSmallHorizontalNewH(String dimension) {
		OFFER_APP_SMALL_HORIZONTAL_NEW_H = Integer.parseInt(dimension);
	}
	
//	@Value("${app.offer.detailnew.length}")
//    public void setOfferAppDetailNewL(String dimension) {
//		OFFER_APP_DETAIL_NEW_L = Integer.parseInt(dimension);
//	}
//	
//	@Value("${app.offer.detailnew.height}")
//    public void setOfferAppDetailNewH(String dimension) {
//		OFFER_APP_DETAIL_NEW_H = Integer.parseInt(dimension);
//	}
	
	@Value("${app.offer.detail.length}")
    public void setOfferAppDetailL(String dimension) {
		OFFER_APP_DETAIL_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.detail.height}")
    public void setOfferAppDetailH(String dimension) {
		OFFER_APP_DETAIL_H = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.luckyoffernew.length}")
    public void setOfferLuckyOfferNewL(String dimension) {
		OFFER_APP_LUCKY_OFFER_NEW_L = Integer.parseInt(dimension);
	}
	
	@Value("${app.offer.luckyoffernew.height}")
    public void setOfferLuckyOfferNewH(String dimension) {
		OFFER_APP_LUCKY_OFFER_NEW_H = Integer.parseInt(dimension);
	}
	
	//BANNER IMAGES
	public static int BANNER_POSITION_TOP_L;
	public static int BANNER_POSITION_TOP_H;
	public static int BANNER_POSITION_MIDDLE_L;
	public static int BANNER_POSITION_MIDDLE_H;
	public static int BANNER_POSITION_BOTTOM_L;
	public static int BANNER_POSITION_BOTTOM_H;
	
	@Value("${banner.top.length}")
    public void setBannerTopL(String dimension) {
		BANNER_POSITION_TOP_L = Integer.parseInt(dimension);
	}
	
	@Value("${banner.top.height}")
    public void setBannerTopH(String dimension) {
		BANNER_POSITION_TOP_H = Integer.parseInt(dimension);
	}
	
	@Value("${banner.middle.length}")
    public void setBannerMiddleL(String dimension) {
		BANNER_POSITION_MIDDLE_L = Integer.parseInt(dimension);
	}
	
	@Value("${banner.middle.height}")
    public void setBannerMiddleH(String dimension) {
		BANNER_POSITION_MIDDLE_H = Integer.parseInt(dimension);
	}
	
	@Value("${banner.bottom.length}")
    public void setBannerBottomL(String dimension) {
		BANNER_POSITION_BOTTOM_L = Integer.parseInt(dimension);
	}
	
	@Value("${banner.bottom.height}")
    public void setBannerBottomH(String dimension) {
		BANNER_POSITION_BOTTOM_H = Integer.parseInt(dimension);
	}
	
	//GIFTING IMAGES
	public static int GIFTING_IMAGE_L;
	public static int GIFTING_IMAGE_H;
	
	@Value("${gifting.length}")
    public void setGiftingL(String dimension) {
		GIFTING_IMAGE_L = Integer.parseInt(dimension);
	}
	
	@Value("${gifting.height}")
    public void setGiftingH(String dimension) {
		GIFTING_IMAGE_H = Integer.parseInt(dimension);
	}
	
}
