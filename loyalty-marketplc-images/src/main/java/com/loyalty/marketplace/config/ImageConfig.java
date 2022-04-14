package com.loyalty.marketplace.config;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loyalty.marketplace.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.constants.ImageConstants;

public class ImageConfig {

	private static final Logger LOG = LoggerFactory.getLogger(ImageConfig.class);
	
	/**
	 * Add @Configuration at class level and @Bean at createImageDirectories() method level 
	 * to create directories on application startup.
	 * 
	 * import org.springframework.context.annotation.Bean;
	 * import org.springframework.context.annotation.Configuration;
	 * 
	 */
	
	public void createImagesDirectories()	 {

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.CREATING_IMAGE_DIRECTORIES,
				this.getClass(), ImageConfigurationConstants.CONFIG_CREATE_DIRECTORIES);
		
		createWebAppDirectories();
		createMerchantDirectories();
		createOfferDirectories();
		createBannerDirectories(); 
		createGiftingDirectories();
		
	}

	private void createWebAppDirectories() {
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_WEB).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_APP).toString()).mkdirs();
	}
	
	private void createMerchantDirectories() {
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_WEB).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_WEB_LISTING).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_WEB_DETAIL).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_COUPONS).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_DETAIL).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_LISTING).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_MERCHANT_APP_LIST_LOGO).toString()).mkdirs();
	}
	
	private void createOfferDirectories() {
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_DETAIL).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_LISTING).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_MY_COUPONS).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_WEB_FEATURED).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_APP).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_DOD_LISTING_NEW).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_SMALL_HORIZONTAL_NEW).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_DETAIL).toString()).mkdirs();
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_OFFER_APP_LUCKY_OFFER_NEW).toString()).mkdirs();
	}
		
	private void createBannerDirectories() {
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_BANNER).toString()).mkdirs();
	}
	
	private void createGiftingDirectories() {
		new File(Paths.get(ImageConstants.PATH_FOR_IMAGE_GIFTING).toString()).mkdirs();
	}
	
}
