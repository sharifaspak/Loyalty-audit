package com.loyalty.marketplace.image.helper;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.validation.Validator;

import org.apache.commons.io.FilenameUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.domain.BannerImageDomain;
import com.loyalty.marketplace.image.domain.GiftingImageDomain;
import com.loyalty.marketplace.image.domain.ImageDomain;
import com.loyalty.marketplace.image.domain.LanguageDomain;
import com.loyalty.marketplace.image.domain.MerchantOfferImageDomain;
import com.loyalty.marketplace.image.inbound.dto.BannerParameters;
import com.loyalty.marketplace.image.inbound.dto.GenerateMetaDataRequest;
import com.loyalty.marketplace.image.inbound.dto.GiftingParameters;
import com.loyalty.marketplace.image.inbound.dto.ImageParameters;
import com.loyalty.marketplace.image.inbound.dto.MerchantOfferParameters;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.entity.MerchantOfferImage;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.image.outbound.dto.GenerateMetaDataResponse;
import com.loyalty.marketplace.image.outbound.dto.GenerateMetaDataResult;
import com.loyalty.marketplace.image.outbound.dto.ImageDataResponse;
import com.loyalty.marketplace.image.outbound.dto.ImageDataResult;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.outbound.database.entity.OfferCatalog;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@RefreshScope
@Component
public class ImageControllerHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ImageControllerHelper.class);
	
	@Autowired
	Validator validator;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ImageDomain imageDomain;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	ServiceHelper serviceHelper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	ProgramManagement programManagement;
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	//CALL TO NGINX MICROSERVICE URLS
	
	@Value("${imageUpload.nginx.uri}")
	private String imageUploadBase;
	
	@Value("${imageUpload.nginx.uri.dr.prod}")
	private String imageUploadBaseProduction;

	@Value("${imageUpload.nginx.upload.uri}")
	private String uploadImage;
	
	@Value("${imageUpload.nginx.retrieve.metadata.uri}")
	private String retrieveImageData;
	
	@Value("${imageUpload.nginx.delete.image.uri}")
	private String deleteImage;
	
	//BASE URL TO APPEND IN DB
	
	@Value("${imageUpload.directory.url}")
	private String saveImageToDirectoryUrl;
	
	@Value("${imageUpload.directory.url.dr}")
	private String saveImageToDirectoryUrlDr;
	
	@Value("${imageUpload.directory.url.prod}")
	private String saveImageToDirectoryUrlProd;
	
	//PATH ON NGINX TO SAVE THE IMAGE TO
	
	@Value("${imageUpload.directory.path}")
	private String imageDirectoryPath;
	
	@Value("${imageUpload.directory.path.dr.prod}")
	private String imageDirectoryPathProduction;
		
	//FLAG TO CHECK IF API IS CALLED FROM DR OR PRODUCTION ENVIRONMENTS
	
	@Value("${imageUpload.prod.env}")
	private String productionEnv;
	
	//OTHER PROPERTIES
	
	@Value("${marketplace.offers.update.eligibleOffers}")
	private String updateEligibleOfferUrl;
	
	@Value("#{'${imageUpload.allowed.image.formats}'.split(',')}")
	private List<String> allowedImageFormats;
	
	/* GENERAL METHODS */
	
	/**
	 * Method to check if input file is empty or null.
	 * @param file
	 * @param resultResponse
	 * @return
	 */
	public boolean validateFileField(MultipartFile file, ResultResponse resultResponse) {
		if(null == file || file.isEmpty()) {
			resultResponse.addErrorAPIResponse(ImageCodes.IMAGE_REQUEST_FILE_MANDATORY.getIntId(),
					ImageCodes.IMAGE_REQUEST_FILE_MANDATORY.getMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}
	
	/**
	 * Validates if input color code and direction fields are null.
	 * @param giftingParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateGiftingSolidField(GiftingParameters giftingParameters, ResultResponse resultResponse) {
		if (null == giftingParameters.getColorCode() || giftingParameters.getColorCode().isEmpty()) {
			resultResponse.addErrorAPIResponse(ImageCodes.GIFTING_REQUEST_COLOUR_CODE_MANDATORY.getIntId(),
					ImageCodes.GIFTING_REQUEST_COLOUR_CODE_MANDATORY.getMsg());
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
		} 
		if (null == giftingParameters.getColorDirection() || giftingParameters.getColorDirection().isEmpty()) {
			resultResponse.addErrorAPIResponse(ImageCodes.GIFTING_REQUEST_COLOUR_DIRECTION_MANDATORY.getIntId(),
					ImageCodes.GIFTING_REQUEST_COLOUR_DIRECTION_MANDATORY.getMsg());
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();
	}
	
	/**
	 * Checks if input image format is from list of allowed image formats.
	 * @param originalFileName
	 * @param resultResponse
	 * @return
	 */
	public boolean validateImageFormat(String originalFileName, ResultResponse resultResponse) {
		
		String extension = FilenameUtils.getExtension(originalFileName);
		
		List<String> allowedFormats = allowedImageFormats.stream().map(String::toUpperCase)
				.collect(Collectors.toList());

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.ALLOWED_IMAGE_FORMATS,
				Utility.class, ImageConfigurationConstants.CONTROLLER_HELPER_VALIDATE_IMG_FORMAT, allowedImageFormats);
		
		if (null == extension || !allowedFormats.contains(extension.toUpperCase())) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_IMAGE_FORMAT.getIntId(),
					ImageCodes.INVALID_IMAGE_FORMAT.getMsg() + allowedImageFormats);
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		
		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IMAGE_EXTENSION,
				Utility.class, ImageConfigurationConstants.CONTROLLER_HELPER_VALIDATE_IMG_FORMAT, extension);
		
		return true;
	
	}
	
	/* MERCHANT-OFFER IMAGE METHODS */
	
	/**
	 * This method checks if merchantCode or offerId exists.
	 * @param domainName
	 * @param domainId
	 * @param resultResponse
	 * @return
	 */
	public boolean checkMerchantOfferExists(String domainName, String domainId,
			ResultResponse resultResponse) {

		if (domainName.equalsIgnoreCase(ImageConstants.DOMAIN_NAME_MERCHANT)
				&& null == merchantRepository.findByMerchantCode(domainId)) {
			resultResponse.addErrorAPIResponse(ImageCodes.MERCHANT_NOT_AVAILABLE.getIntId(),
					ImageCodes.MERCHANT_NOT_AVAILABLE.getMsg() + domainId);
			resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getId(),
					ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getMsg());
			return false;
		}
		if (domainName.equalsIgnoreCase(ImageConstants.DOMAIN_NAME_OFFER)
				&& null == offerRepository.findByOfferId(domainId)) {
			resultResponse.addErrorAPIResponse(ImageCodes.OFFER_NOT_AVAILABLE.getIntId(),
					ImageCodes.OFFER_NOT_AVAILABLE.getMsg() + domainId);
			resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getId(),
					ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getMsg());
			return false;
		}

		return true;
	}
	
	/**
	 * Checks if an image already exists for the given merchantCode or offerId
	 * @param domainId
	 * @param imageType
	 * @param availableInChannel
	 * @param resultResponse
	 * @return
	 */
	public boolean checkImageExistsMerchantOffer(String domainId, String imageType, String availableInChannel, ResultResponse resultResponse) {
		
		if (null != imageRepository.findByImageCategoryAndDomainIdAndImageTypeAndAvailableInChannel(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER, domainId, imageType.toUpperCase(), availableInChannel)) {
			resultResponse.addErrorAPIResponse(ImageCodes.IMAGE_EXISTS_FOR_IMAGE_TYPE.getIntId(),
					ImageCodes.IMAGE_EXISTS_FOR_IMAGE_TYPE.getMsg() + domainId + ImageConstants.COMMA_SEPARATOR + imageType.toUpperCase() + ImageConstants.COMMA_SEPARATOR + availableInChannel.toUpperCase());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input channel is SAPP or SWEB
	 * @param availableInChannel
	 * @param resultResponse
	 * @return
	 */
	public boolean validateChannel(String availableInChannel, ResultResponse resultResponse) {
		if(!ImageConstants.CHANNEL_LIST.contains(availableInChannel.toUpperCase())) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_CHANNEL.getIntId(),
					ImageCodes.INVALID_CHANNEL.getMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}
	
	/**
	 * Validates if input domainName field id 'Merchant' or 'Offer'
	 * @param domainName
	 * @param resultResponse
	 * @return
	 */
	public boolean validateDomainName(String domainName, ResultResponse resultResponse) {
		if(!ImageConstants.DOMAIN_NAME_LIST.contains(domainName.toUpperCase())) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_DOMAIN_NAME.getIntId(),
					ImageCodes.INVALID_DOMAIN_NAME.getMsg() + domainName);
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}
	
	/**
	 * Validates if the input image type is valid for the domain provided.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateImageTypeForDomain(MerchantOfferParameters merchantOfferParameters, ResultResponse resultResponse) {
		
		switch (merchantOfferParameters.getDomainName().toUpperCase()) {

		case ImageConstants.DOMAIN_NAME_MERCHANT:
			
			merchantOfferParameters.setDomainName(ImageConstants.DOMAIN_NAME_MERCHANT);
			if(!validateMerchantImageType(merchantOfferParameters, resultResponse)) return false;
			merchantOfferParameters.setImageType(merchantOfferParameters.getImageType().toUpperCase());
			break;

		case ImageConstants.DOMAIN_NAME_OFFER:
		
			merchantOfferParameters.setDomainName(ImageConstants.DOMAIN_NAME_OFFER);
			if(!validateOfferImageType(merchantOfferParameters, resultResponse)) return false;
			merchantOfferParameters.setImageType(merchantOfferParameters.getImageType().toUpperCase());
			break;

		default:
		
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_DOMAIN_NAME.getIntId(),
					ImageCodes.INVALID_DOMAIN_NAME.getMsg() + merchantOfferParameters.getDomainName());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());

		}
		
		return true;
	}

	/**
	 * This method validates merchant image type.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @return
	 */
	private boolean validateMerchantImageType(MerchantOfferParameters merchantOfferParameters, ResultResponse resultResponse) {
		
		if (merchantOfferParameters.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SWEB)) {
			merchantOfferParameters.setAvailableInChannel(ImageConstants.CHANNEL_SWEB);
			if (!ImageConstants.MERCHANT_SWEB_IMAGE_TYPE_LIST.contains(merchantOfferParameters.getImageType().toUpperCase())) {
				resultResponse.addErrorAPIResponse(ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_WEB.getIntId(),
						ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_WEB.getMsg());
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getId(),
						ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getMsg());
				return false;
			}
		}
		if (merchantOfferParameters.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SAPP)) {
			merchantOfferParameters.setAvailableInChannel(ImageConstants.CHANNEL_SAPP);
			if (!ImageConstants.MERCHANT_SAPP_IMAGE_TYPE_LIST.contains(merchantOfferParameters.getImageType().toUpperCase())) {
				resultResponse.addErrorAPIResponse(ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_APP.getIntId(),
						ImageCodes.MERCHANT_INVALID_IMAGE_TYPE_APP.getMsg());
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getId(),
						ImageCodes.IMAGE_UPLOAD_MERCHANT_FAILED.getMsg());
				return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * This method validates offer image type.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @return
	 */
	private boolean validateOfferImageType(MerchantOfferParameters merchantOfferParameters, ResultResponse resultResponse) {
		
		if (merchantOfferParameters.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SWEB)) {
			merchantOfferParameters.setAvailableInChannel(ImageConstants.CHANNEL_SWEB);
			if (!ImageConstants.OFFER_SWEB_IMAGE_TYPE_LIST.contains(merchantOfferParameters.getImageType().toUpperCase())) {
				resultResponse.addErrorAPIResponse(ImageCodes.OFFER_INVALID_IMAGE_TYPE_WEB.getIntId(),
						ImageCodes.OFFER_INVALID_IMAGE_TYPE_WEB.getMsg());
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getId(),
						ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getMsg());
				return false;
			}
		}
		if (merchantOfferParameters.getAvailableInChannel().equalsIgnoreCase(ImageConstants.CHANNEL_SAPP)) {
			merchantOfferParameters.setAvailableInChannel(ImageConstants.CHANNEL_SAPP);
			if (!ImageConstants.OFFER_SAPP_IMAGE_TYPE_LIST.contains(merchantOfferParameters.getImageType().toUpperCase())) {
				resultResponse.addErrorAPIResponse(ImageCodes.OFFER_INVALID_IMAGE_TYPE_APP.getIntId(),
						ImageCodes.OFFER_INVALID_IMAGE_TYPE_APP.getMsg());
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getId(),
						ImageCodes.IMAGE_UPLOAD_OFFER_FAILED.getMsg());
				return false;
			}
		}
		
		return true;
		
	}
	
	/**
	 * Validates if the input image is of right dimension for Merchant & Offer image category.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 */
	public boolean validateImageDimensionsMerchantOffer(MerchantOfferParameters merchantOfferParameters,
			ResultResponse resultResponse) throws IOException {

		setImageDimensions(merchantOfferParameters, ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER);
		switch (merchantOfferParameters.getDomainName().toUpperCase()) {

		case ImageConstants.DOMAIN_NAME_MERCHANT:
			if (CheckDimensions
					.getDimensionMerchantOffer(merchantOfferParameters.getImageType(), merchantOfferParameters.getDomainName(),
							merchantOfferParameters.getAvailableInChannel())
					.validateDimensionsMerchant(merchantOfferParameters, resultResponse))
				return false;
			break;

		case ImageConstants.DOMAIN_NAME_OFFER:
			if (CheckDimensions
					.getDimensionMerchantOffer(merchantOfferParameters.getImageType(), merchantOfferParameters.getDomainName(),
							merchantOfferParameters.getAvailableInChannel())
					.validateDimensionsOffer(merchantOfferParameters, resultResponse))
				return false;

			break;

		default:
			break;

		}

		return true;
	}
	
	/* BANNER IMAGE METHODS */
	
	/**
	 * Validates if offer exists & if provided offer type matches the actual.
	 * @param bannerParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean checkBannerOfferExists(BannerParameters bannerParameters, ResultResponse resultResponse) {
		
		if (null != bannerParameters.getOfferId()) {
			OfferCatalog offerCatalog = offerRepository.findByOfferId(bannerParameters.getOfferId());
			
			if(null == offerCatalog) {
				resultResponse.addErrorAPIResponse(ImageCodes.BANNER_OFFER_UNAVAILABLE.getIntId(),
						ImageCodes.BANNER_OFFER_UNAVAILABLE.getMsg() + bannerParameters.getOfferId());
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				return false;
			} 
			
			if(null != offerCatalog.getOfferType() && null != offerCatalog.getOfferType().getOfferDescription()
					&& null != offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn()
					&& !offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn().equalsIgnoreCase(bannerParameters.getOfferType())) {
				resultResponse.addErrorAPIResponse(ImageCodes.BANNER_OFFER_TYPE_MISMATCH.getIntId(),
						ImageCodes.BANNER_OFFER_TYPE_MISMATCH.getMsg() + bannerParameters.getOfferType());
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				return false;
			}
			
			if(null == offerCatalog.getOfferType() || null == offerCatalog.getOfferType().getOfferDescription()
					|| null == offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn()) {
				resultResponse.addErrorAPIResponse(ImageCodes.BANNER_NO_ASSOCIATED_OFFER_TYPE.getIntId(),
						ImageCodes.BANNER_NO_ASSOCIATED_OFFER_TYPE.getMsg() + bannerParameters.getOfferId());
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				return false;
			}
			
			bannerParameters.setOfferType(offerCatalog.getOfferType().getOfferDescription().getTypeDescriptionEn());
		}
		
		return true;
	}
	
	/**
	 * Validates input parameters for banner image upload API.
	 * @param bannerParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateBannerImageRequestParameters(BannerParameters bannerParameters, ResultResponse resultResponse) {
		
		if(null != bannerParameters.getOfferId()) {
			if(null == bannerParameters.getOfferType() || bannerParameters.getOfferType().isEmpty()) {
				resultResponse.addErrorAPIResponse(ImageCodes.BANNER_REQUEST_OFFER_TYPE_MANDATORY.getIntId(),
						ImageCodes.BANNER_REQUEST_OFFER_TYPE_MANDATORY.getMsg());
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
			} 
			if(null == bannerParameters.getDeepLink() || bannerParameters.getDeepLink().isEmpty()) {
				resultResponse.addErrorAPIResponse(ImageCodes.BANNER_REQUEST_OFFER_DEEPLINK_MANDATORY.getIntId(),
						ImageCodes.BANNER_REQUEST_OFFER_DEEPLINK_MANDATORY.getMsg());
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
			}
		}

		if (null != bannerParameters.getStatus()
				&& !bannerParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
				&& !bannerParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_INACTIVE)) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_STATUS.getIntId(),
					ImageCodes.INVALID_STATUS.getMsg());
			resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();

	}

	/**
	 * Validates if correct banner position (TOP, MIDDLE, BOTTOM) is provided in request.
	 * @param bannerPosition
	 * @param resultResponse
	 * @return
	 */
	public boolean validateBannerCategories(String bannerPosition, ResultResponse resultResponse) {
		if(null != bannerPosition.toUpperCase() && !ImageConstants.BANNER_POSITION_LIST.contains(bannerPosition.toUpperCase())) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_BANNER_POSITION.getIntId(),
					ImageCodes.INVALID_BANNER_POSITION.getMsg());
			resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}
	
	/**
	 * Validates input image dimensions for banner image.
	 * @param bannerParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 */
	public boolean validateImageDimensionsBanner(BannerParameters bannerParameters, ResultResponse resultResponse) throws IOException {
		setImageDimensions(bannerParameters, ImageConstants.IMAGE_CATEGORY_BANNER);
		return !CheckDimensions.getDimensionBannerGifting(bannerParameters.getBannerPosition()).validateDimensionsBanner(bannerParameters, resultResponse);
	}
	
	/**
	 * Method to delete existing image from nginx server while updating banner image.
	 * @param originalFileName
	 * @param imageCategory
	 * @param resultResponse
	 * @throws IOException
	 */
	private String getExistingImageUrl(String originalFileName, String imageCategory) {
		
		if(imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)) {
			return imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + ImageConstants.PATH_FOR_IMAGE_BANNER + originalFileName;
		}
		
		if(imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING)) {
			return imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + ImageConstants.PATH_FOR_IMAGE_GIFTING + originalFileName;
		}
		
		return null;
	
	}
	
	/**
	 * Method to delete existing image from nginx server while updating banner image - for production.
	 * @param originalFileName
	 * @param imageCategory
	 * @param resultResponse
	 * @throws IOException
	 */
	private String getExistingImageUrlProduction(String originalFileName, String imageCategory) {
		
		if(imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)) {
			return imageDirectoryPathProduction + ImageConstants.IMAGES_DIRECTORY + ImageConstants.PATH_FOR_IMAGE_BANNER + originalFileName;
		}
		
		if(imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING)) {
			return imageDirectoryPathProduction + ImageConstants.IMAGES_DIRECTORY + ImageConstants.PATH_FOR_IMAGE_GIFTING + originalFileName;
		}
		
		return null;
	
	}
	
	/* GIFTING IMAGE METHODS */
	
	/** 
	 * Validates gifting image upload input paramters.
	 * @param giftingParameters
	 * @param resultResponse
	 * @return
	 */
	public boolean validateGiftingImageRequestParameters(GiftingParameters giftingParameters, ResultResponse resultResponse) {
		
		if(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_SOLID)) {
			validateGiftingSolidField(giftingParameters, resultResponse);
		} else if(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_IMAGE)) {
			validateFileField(giftingParameters.getFile(), resultResponse);
		} else {
			resultResponse.addErrorAPIResponse(ImageCodes.GIFTING_INVALID_IMAGE_TYPE.getIntId(),
					ImageCodes.GIFTING_INVALID_IMAGE_TYPE.getMsg());
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
		}
		
		if(!giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE) && !giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_INACTIVE)) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_STATUS.getIntId(),
					ImageCodes.INVALID_STATUS.getMsg());
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
		}
		
		return resultResponse.getApiStatus().getErrors().isEmpty();

	}
	
	/**
	 * Validates input image dimension for gifting image.
	 * @param giftingParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 */
	public boolean validateImageDimensionsGifting(GiftingParameters giftingParameters, ResultResponse resultResponse) throws IOException {
		setImageDimensions(giftingParameters, ImageConstants.IMAGE_CATEGORY_GIFTING);
		return !CheckDimensions.getDimensionBannerGifting(ImageConstants.IMAGE_CATEGORY_GIFTING).validateDimensionsGifting(giftingParameters, resultResponse);
	}

	/**
	 * Method to generate sequential gifting image id.
	 * @param giftingParameters
	 */
	public void generateImageIdGifting(GiftingParameters giftingParameters) {
		MarketplaceImage image = imageRepository.findFirst1ByImageCategoryOrderByGiftingImage_ImageIdDesc(ImageConstants.IMAGE_CATEGORY_GIFTING);
		giftingParameters.setImageIdGenerated(null != image ? image.getGiftingImage().getImageId() + 1 : 1);
	}
		
	/**
	 * Validates if an image already exists with the same file name.
	 * @param imageCategory
	 * @param originalFileName
	 * @param resultResponse
	 * @return
	 */
	public boolean checkImageExistsBannerGifting(String imageCategory, String originalFileName, ResultResponse resultResponse) {
		if (null != imageRepository.findByImageCategoryAndOriginalFileName(imageCategory, originalFileName)) {
			resultResponse.addErrorAPIResponse(
					imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)
							? ImageCodes.BANNER_IMAGE_EXISTS_FOR_GIVEN_NAME.getIntId()
							: ImageCodes.GIFTING_IMAGE_EXISTS_FOR_GIVEN_NAME.getIntId(),
					imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)
							? ImageCodes.BANNER_IMAGE_EXISTS_FOR_GIVEN_NAME.getMsg() + originalFileName
							: ImageCodes.GIFTING_IMAGE_EXISTS_FOR_GIVEN_NAME.getMsg() + originalFileName);
			resultResponse.setResult(
					imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)
							? ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId()
							: ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
					imageCategory.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)
							? ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg()
							: ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
			return false;
		}
		return true;
	}
	
	/* IMAGE UPDATE METHODS */
	
	/**
	 * Method to update banner image.
	 * @param image
	 * @param imageParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws MarketplaceException 
	 */
	public ResultResponse updateBannerImage(Optional<MarketplaceImage> image, ImageParameters imageParameters,
			ResultResponse resultResponse, Headers header) throws IOException, ParseException, MarketplaceException {
	
		if (!Utility.validateBannerBooleanParameters(imageParameters.getIsStaticBanner(),
				imageParameters.getIsBogoOffer(), resultResponse)) {
			resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
					ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
			return resultResponse;
		}
		
		BannerParameters bannerParameters = modelMapper.map(imageParameters, BannerParameters.class);
		MarketplaceImage existingImage = image.get();
		boolean updateBannerPosition = null != bannerParameters.getBannerPosition() && !bannerParameters.getBannerPosition().isEmpty();
		String bannerPos = updateBannerPosition ? bannerParameters.getBannerPosition() : existingImage.getBannerImage().getBannerPosition();
		bannerParameters.setBannerPosition(bannerPos);
		
		if (!validateBannerUpdateParameters(bannerParameters, bannerPos, updateBannerPosition, existingImage, imageParameters, resultResponse)) return resultResponse;
		
		if(null != imageParameters.getFile() && !imageParameters.getFile().isEmpty()) {
			if(!checkImageExistsBannerGifting(ImageConstants.IMAGE_CATEGORY_BANNER, bannerParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!validateImageFormat(bannerParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!validateImageDimensionsBanner(bannerParameters, resultResponse)) return resultResponse;
			String existingUrl = getExistingImageUrl(existingImage.getOriginalFileName(), ImageConstants.IMAGE_CATEGORY_BANNER);
			String existingUrlProd = getExistingImageUrlProduction(existingImage.getOriginalFileName(), ImageConstants.IMAGE_CATEGORY_BANNER);
			if(!saveImageToDirectoryPath(bannerParameters, ImageConstants.IMAGE_CATEGORY_BANNER, ImageConstants.ACTION_UPDATE, existingUrl, existingUrlProd, header, resultResponse)) return resultResponse;
		}
		
		updateBannerImageDomain(existingImage, bannerParameters, resultResponse);
		
		return resultResponse;
		
	}
	
	/**
	 * This method validates banner parameters for banner update functionality.
	 * @param bannerParameters
	 * @param bannerPos
	 * @param updateBannerPosition
	 * @param existingImage
	 * @param imageParameters
	 * @param resultResponse
	 * @return
	 */
	private boolean validateBannerUpdateParameters(BannerParameters bannerParameters, String bannerPos,
			boolean updateBannerPosition, MarketplaceImage existingImage, ImageParameters imageParameters,
			ResultResponse resultResponse) {

		if(!validateBannerImageRequestParameters(bannerParameters, resultResponse)) return false;
		if(!checkBannerOfferExists(bannerParameters, resultResponse)) return false; 
		if(!validateBannerCategories(bannerPos, resultResponse)) return false;
		
		if (updateBannerPosition && null == imageParameters.getFile() && !existingImage.getBannerImage().getBannerPosition().equalsIgnoreCase(bannerParameters.getBannerPosition())) {
			resultResponse.addErrorAPIResponse(ImageCodes.IMAGE_MANDATORY_FOR_BANNER_POSITION_CHANGE.getIntId(),
					ImageCodes.IMAGE_MANDATORY_FOR_BANNER_POSITION_CHANGE.getMsg());
			resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
					ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * Method to update gifting image of imageType - IMAGE.
	 * @param image
	 * @param imageParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws MarketplaceException 
	 */
	public ResultResponse updateGiftingImageType(Optional<MarketplaceImage> image, ImageParameters imageParameters,
			ResultResponse resultResponse, Headers header) throws IOException, ParseException, MarketplaceException {
	
		GiftingParameters giftingParameters = modelMapper.map(imageParameters, GiftingParameters.class);
		MarketplaceImage existingImage = image.get();
		
		if (null != giftingParameters.getStatus()
				&& !giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
				&& !giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_INACTIVE)) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_STATUS.getIntId(),
					ImageCodes.INVALID_STATUS.getMsg());
			return resultResponse;
		}
	
		if (null != imageParameters.getFile() && !imageParameters.getFile().isEmpty()) {
			if(!checkImageExistsBannerGifting(ImageConstants.IMAGE_CATEGORY_GIFTING, giftingParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!validateImageFormat(giftingParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!validateImageDimensionsGifting(giftingParameters, resultResponse)) return resultResponse;
			String existingUrl = getExistingImageUrl(existingImage.getOriginalFileName(), ImageConstants.IMAGE_CATEGORY_GIFTING);
			String existingUrlProd = getExistingImageUrlProduction(existingImage.getOriginalFileName(), ImageConstants.IMAGE_CATEGORY_GIFTING);
			if(!saveImageToDirectoryPath(giftingParameters, ImageConstants.IMAGE_CATEGORY_GIFTING, ImageConstants.ACTION_UPDATE, existingUrl, existingUrlProd, header, resultResponse)) return resultResponse;
		}
		
		updateGiftingImageDomain(existingImage, giftingParameters, resultResponse);
		
		return resultResponse;
	
	}
	
	/**
	 * Method to update gifting image of imageType - SOLID.
	 * @param image
	 * @param imageParameters
	 * @param resultResponse
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ResultResponse updateGiftingSolidType(Optional<MarketplaceImage> image, ImageParameters imageParameters, ResultResponse resultResponse) throws IOException, ParseException {
		
		GiftingParameters giftingParameters = modelMapper.map(imageParameters, GiftingParameters.class);
		MarketplaceImage existingImage = image.get();
	
		if (null != giftingParameters.getStatus()
				&& !giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
				&& !giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_INACTIVE)) {
			resultResponse.addErrorAPIResponse(ImageCodes.INVALID_STATUS.getIntId(),
					ImageCodes.INVALID_STATUS.getMsg());
			return resultResponse;
		}
		
		updateGiftingSolidDomain(existingImage, giftingParameters, resultResponse);
		
		return resultResponse;
	
	}
	
	/* SET PARAMETERS & IMAGE SAVE/UPLOAD METHODS */
	
	/**
	 * Method to calculate and set image dimensions to DTO.
	 * @param parameters
	 * @param imageCategory
	 * @throws IOException
	 */
	public void setImageDimensions(Object parameters, String imageCategory) throws IOException {
		
		BufferedImage bufferedImage = null;
		switch (imageCategory) {

		case ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER:
			MerchantOfferParameters merchantOfferParameters = (MerchantOfferParameters) parameters;
			bufferedImage = ImageIO.read(new BufferedInputStream(merchantOfferParameters.getFile().getInputStream()));	
			if(null != bufferedImage) {
				merchantOfferParameters.setLength(bufferedImage.getWidth());
				merchantOfferParameters.setHeight(bufferedImage.getHeight());
			}
			
			break;

		case ImageConstants.IMAGE_CATEGORY_BANNER:
			BannerParameters bannerParameters = (BannerParameters) parameters;
			bufferedImage = ImageIO.read(new BufferedInputStream(bannerParameters.getFile().getInputStream()));
			if (null != bufferedImage) {
				bannerParameters.setLength(bufferedImage.getWidth());
				bannerParameters.setHeight(bufferedImage.getHeight());
			}
			break;

		case ImageConstants.IMAGE_CATEGORY_GIFTING:
			GiftingParameters giftingParameters = (GiftingParameters) parameters;
			bufferedImage = ImageIO.read(new BufferedInputStream(giftingParameters.getFile().getInputStream()));
			if (null != bufferedImage) {
				giftingParameters.setLength(bufferedImage.getWidth());
				giftingParameters.setHeight(bufferedImage.getHeight());
			}
			break;
		
		default:
			break;
			
		}
		
		if(null != bufferedImage) LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IMAGE_DIMENSION,
				this.getClass(), ImageConfigurationConstants.CONTROLLER_HELPER_SET_DIMENSIONS, bufferedImage.getWidth(), bufferedImage.getHeight());
		
	}
	
	/**
	 * Method to generate path to which image needs to be uploaded to and upload to server functionality.
	 * @param parameters
	 * @param imageCategory
	 * @throws IOException
	 * @throws MarketplaceException 
	 */
	public boolean saveImageToDirectoryPath(Object parameters, String imageCategory, String action, String existingPath, String existingPathProd, Headers header, ResultResponse resultResponse) throws MarketplaceException {
	
		Path path = null;				
		String productionPath = null;
		
		switch (imageCategory) {

		case ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER:
			MerchantOfferParameters merchantOfferParameters = (MerchantOfferParameters) parameters;
			
			String extension = FilenameUtils.getExtension(merchantOfferParameters.getFile().getOriginalFilename());
			
			path = Paths.get(PathBuilder.getStringPathMerchantOffer(merchantOfferParameters.getImageType(),
					merchantOfferParameters.getDomainName(), merchantOfferParameters.getAvailableInChannel())
					+ merchantOfferParameters.getDomainId() + ImageConstants.APPEND_EXTENSION_PERIOD + extension);

			merchantOfferParameters.setPath(saveImageToDirectoryUrl + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
			if(null != productionEnv && productionEnv.equalsIgnoreCase(ImageConstants.YES)) {
				merchantOfferParameters.setImageUrlDr(saveImageToDirectoryUrlDr + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				merchantOfferParameters.setImageUrlProd(saveImageToDirectoryUrlProd + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				productionPath = imageDirectoryPathProduction + ImageConstants.IMAGES_DIRECTORY + path.toString();
			}
			
			if (!uploadImage(merchantOfferParameters.getFile(), imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + path.toString(),
					productionPath, ImageConstants.ACTION_INSERT, existingPath, existingPathProd, header, resultResponse)) {
				resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
				return false;
			}
			
			break;

		case ImageConstants.IMAGE_CATEGORY_BANNER:
			BannerParameters bannerParameters = (BannerParameters) parameters;
			path = Paths.get(PathBuilder.getStringPathBannerGifting(ImageConstants.BANNER_IMAGE) + bannerParameters.getFile().getOriginalFilename());				
			
			bannerParameters.setPath(saveImageToDirectoryUrl + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
			if(null != productionEnv && productionEnv.equalsIgnoreCase(ImageConstants.YES)) {
				bannerParameters.setImageUrlDr(saveImageToDirectoryUrlDr + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				bannerParameters.setImageUrlProd(saveImageToDirectoryUrlProd + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				productionPath = imageDirectoryPathProduction + ImageConstants.IMAGES_DIRECTORY + path.toString();
			}
			
			if (!uploadImage(bannerParameters.getFile(),
					imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + path.toString(), productionPath, action,
					existingPath, existingPathProd, header, resultResponse)) {

				if(action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {
					resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
							ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				} else {
					resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
							ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
				}
				
				return false;
			}
			
			
			break;

		case ImageConstants.IMAGE_CATEGORY_GIFTING:
			GiftingParameters giftingParameters = (GiftingParameters) parameters;	
			path = Paths.get(PathBuilder.getStringPathBannerGifting(ImageConstants.GIFTING_IMAGE) + giftingParameters.getFile().getOriginalFilename());				

			giftingParameters.setPath(saveImageToDirectoryUrl + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
			if(null != productionEnv && productionEnv.equalsIgnoreCase(ImageConstants.YES)) {
				giftingParameters.setImageUrlDr(saveImageToDirectoryUrlDr + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				giftingParameters.setImageUrlProd(saveImageToDirectoryUrlProd + ImageConstants.IMAGE_PATH_DIRECTORY + ImageConstants.FORWARD_SLASH + path.toString());
				productionPath = imageDirectoryPathProduction + ImageConstants.IMAGES_DIRECTORY + path.toString();
			}
			
			if (!uploadImage(giftingParameters.getFile(),
					imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + path.toString(), productionPath, action,
					existingPath, existingPathProd, header, resultResponse)) {
	
				if(action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {
					resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
							ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
				} else {
					resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
							ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
				}
				
				return false;
				
			}
			
			break;

		default:
			break;
		}
		
		return true;
		
	}

	/**
	 * Create domain object to save to DB for Merchant-Offer image.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @throws IOException
	 */
	public void createMerchantOfferImageDomain(MerchantOfferParameters merchantOfferParameters, ResultResponse resultResponse) throws IOException {
		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, merchantOfferParameters.getProgram());
		commonAttributes.put(ImageConstants.USERNAME, merchantOfferParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, merchantOfferParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER);
		commonAttributes.put(ImageConstants.IMAGE_PATH, merchantOfferParameters.getPath());
		commonAttributes.put(ImageConstants.IMAGE_PATH_DR, merchantOfferParameters.getImageUrlDr());
		commonAttributes.put(ImageConstants.IMAGE_PATH_PROD, merchantOfferParameters.getImageUrlProd());
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME,
				merchantOfferParameters.getDomainId() + ImageConstants.APPEND_EXTENSION_PERIOD
						+ FilenameUtils.getExtension(merchantOfferParameters.getFile().getOriginalFilename()));
		Binary imageToSave =  new Binary(BsonBinarySubType.BINARY, merchantOfferParameters.getFile().getBytes());
		MerchantOfferImageDomain merchantOfferDomainToSave = new MerchantOfferImageDomain.MerchantOfferImageBuilder(
				merchantOfferParameters.getDomainId(), merchantOfferParameters.getDomainName(), merchantOfferParameters.getAvailableInChannel(),
				merchantOfferParameters.getImageType())
				.imageSize(merchantOfferParameters.getFile().getBytes().length)
				.imageLength(merchantOfferParameters.getLength())
				.imageHeight(merchantOfferParameters.getHeight())
				.build();
		
		uploadImageToDatabase(commonAttributes, imageToSave, merchantOfferDomainToSave, resultResponse,
				merchantOfferParameters.getExternalTransactionId(),
				ImageConfigurationConstants.UPLOAD_IMAGE_MERCHANT_OFFER);
	}
	
	/**
	 * Create domain object to save to DB for Banner image.
	 * @param bannerParameters
	 * @param resultResponse
	 * @throws IOException
	 * @throws ParseException
	 */
	public void createBannerImageDomain(BannerParameters bannerParameters, ResultResponse resultResponse) throws IOException, ParseException {
		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, bannerParameters.getProgram());
		commonAttributes.put(ImageConstants.USERNAME, bannerParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, bannerParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_BANNER);
		commonAttributes.put(ImageConstants.IMAGE_PATH, bannerParameters.getPath());
		commonAttributes.put(ImageConstants.IMAGE_PATH_DR, bannerParameters.getImageUrlDr());
		commonAttributes.put(ImageConstants.IMAGE_PATH_PROD, bannerParameters.getImageUrlProd());
		commonAttributes.put(ImageConstants.IMAGE_STATUS, bannerParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE) ? ImageConstants.STATUS_ACTIVE : ImageConstants.STATUS_INACTIVE);
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME, bannerParameters.getFile().getOriginalFilename());
		Binary imageToSave =  new Binary(BsonBinarySubType.BINARY, bannerParameters.getFile().getBytes());
		BannerImageDomain bannerImageDomain = new BannerImageDomain.BannerImageBuilder(
				bannerParameters.getIsStaticBanner().equalsIgnoreCase(ImageConstants.YES) ? ImageConstants.YES : ImageConstants.NO,
				bannerParameters.getBannerOrder(),
				new SimpleDateFormat(ImageConstants.DATE_FORMAT).parse(bannerParameters.getStartDate()),
				null != bannerParameters.getEndDate()
						? new SimpleDateFormat(ImageConstants.DATE_FORMAT).parse(bannerParameters.getEndDate())
						: null,
				bannerParameters.getIsBogoOffer().equalsIgnoreCase(ImageConstants.YES) ? ImageConstants.YES : ImageConstants.NO, 
				bannerParameters.getCoBrandedPartner(),
				bannerParameters.getBannerPosition().toUpperCase())
			.offerId(bannerParameters.getOfferId())
			.offerType(bannerParameters.getOfferType())
			.deepLink(bannerParameters.getDeepLink())
			.build();
		
		uploadImageToDatabase(commonAttributes, imageToSave, bannerImageDomain, resultResponse, bannerParameters.getExternalTransactionId(), ImageConfigurationConstants.UPLOAD_IMAGE_BANNER);
	}
	
	/**
	 * Create domain object to save to DB for Gifting image.
	 * @param giftingParameters
	 * @param resultResponse
	 * @throws IOException
	 */
	public void createGiftingImageDomain(GiftingParameters giftingParameters, ResultResponse resultResponse) throws IOException {
		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, giftingParameters.getProgram());
		commonAttributes.put(ImageConstants.USERNAME, giftingParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, giftingParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_GIFTING);
		commonAttributes.put(ImageConstants.IMAGE_PATH, giftingParameters.getPath());
		commonAttributes.put(ImageConstants.IMAGE_PATH_DR, giftingParameters.getImageUrlDr());
		commonAttributes.put(ImageConstants.IMAGE_PATH_PROD, giftingParameters.getImageUrlProd());
		commonAttributes.put(ImageConstants.IMAGE_STATUS, giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE) ? ImageConstants.STATUS_ACTIVE : ImageConstants.STATUS_INACTIVE);
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME, giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_IMAGE)
				? giftingParameters.getFile().getOriginalFilename()
				: null);
		Binary imageToSave =  giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_IMAGE)
				? new Binary(BsonBinarySubType.BINARY, giftingParameters.getFile().getBytes())
				: null;
		giftingParameters.setType(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_SOLID)
				? ImageConstants.GIFTING_TYPE_SOLID
				: ImageConstants.GIFTING_TYPE_IMAGE);
		GiftingImageDomain giftingImageDomain = new GiftingImageDomain.GiftingImageBuilder(giftingParameters.getImageIdGenerated(),
				new LanguageDomain(giftingParameters.getNameEn(), giftingParameters.getNameAr()),
				giftingParameters.getType(), giftingParameters.getPriority(),
				giftingParameters.getBackgroundPriority(),
				new LanguageDomain(giftingParameters.getGreetingMessageEn(),
						giftingParameters.getGreetingMessageAr()))
		.colorCode(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_SOLID) ? giftingParameters.getColorCode() : null)
		.colorDirection(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_SOLID) ? giftingParameters.getColorDirection()	: null)
		.build();
		
		uploadImageToDatabase(commonAttributes, imageToSave, giftingImageDomain, resultResponse, giftingParameters.getExternalTransactionId(), ImageConfigurationConstants.UPLOAD_IMAGE_GIFTING);
	}
	
	/**
	 * Create domain object to update image in DB for Banner image.
	 * @param existingImage
	 * @param bannerParameters
	 * @param resultResponse
	 * @throws IOException
	 * @throws ParseException
	 */
	public void updateBannerImageDomain(MarketplaceImage existingImage, BannerParameters bannerParameters,
			ResultResponse resultResponse) throws IOException, ParseException {

		String status = null;
		if (null != bannerParameters.getStatus()) {
			status = bannerParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
					? ImageConstants.STATUS_ACTIVE
					: ImageConstants.STATUS_INACTIVE;
		}

		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, null != bannerParameters.getProgram() ? bannerParameters.getProgram() : existingImage.getProgramCode());
		commonAttributes.put(ImageConstants.USERNAME, bannerParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, bannerParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_BANNER);
		commonAttributes.put(ImageConstants.IMAGE_PATH, null != bannerParameters.getFile() && !bannerParameters.getFile().isEmpty() ? bannerParameters.getPath() : existingImage.getImageUrl());
		commonAttributes.put(ImageConstants.IMAGE_PATH_DR, null != bannerParameters.getFile() && !bannerParameters.getFile().isEmpty() ? bannerParameters.getImageUrlDr() : existingImage.getImageUrlDr());
		commonAttributes.put(ImageConstants.IMAGE_PATH_PROD, null != bannerParameters.getFile() && !bannerParameters.getFile().isEmpty() ? bannerParameters.getImageUrlProd() : existingImage.getImageUrlProd());
		commonAttributes.put(ImageConstants.IMAGE_STATUS, null != bannerParameters.getStatus() ? status : existingImage.getStatus());
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME, null != bannerParameters.getFile() && !bannerParameters.getFile().isEmpty() ? bannerParameters.getFile().getOriginalFilename() : existingImage.getOriginalFileName());
		
		Binary imageToSave = new Binary(BsonBinarySubType.BINARY, null != bannerParameters.getFile() && !bannerParameters.getFile().isEmpty() ? bannerParameters.getFile().getBytes() : existingImage.getImage().getData());
			
		BannerImageDomain bannerImageDomain = createBannerDomainForUpdate(bannerParameters, existingImage);

		updateImageToDatabase(existingImage, commonAttributes, imageToSave, bannerImageDomain, resultResponse, bannerParameters.getExternalTransactionId(), ImageConfigurationConstants.UPDATE_IMAGE);
	
	}
	
	/**
	 * This method is used to create banner image domain object for image update functionality.
	 * @param bannerParameters
	 * @param existingImage
	 * @return
	 * @throws ParseException
	 */
	private BannerImageDomain createBannerDomainForUpdate(BannerParameters bannerParameters, MarketplaceImage existingImage) throws ParseException {
		
		String staticBanner = bannerParameters.getIsStaticBanner().equalsIgnoreCase(ImageConstants.YES) ? ImageConstants.YES : ImageConstants.NO;
		String bogoOffer = bannerParameters.getIsBogoOffer().equalsIgnoreCase(ImageConstants.YES) ? ImageConstants.YES : ImageConstants.NO;
		
		return new BannerImageDomain.BannerImageBuilder(
				null != bannerParameters.getIsStaticBanner() ? staticBanner : existingImage.getBannerImage().getIsStaticBanner(), 
				null != bannerParameters.getBannerOrder() ? bannerParameters.getBannerOrder() : existingImage.getBannerImage().getBannerOrder(),
				null != bannerParameters.getStartDate() ? new SimpleDateFormat(ImageConstants.DATE_FORMAT).parse(bannerParameters.getStartDate()) : existingImage.getBannerImage().getStartDate(),
				null != bannerParameters.getEndDate() ? new SimpleDateFormat(ImageConstants.DATE_FORMAT).parse(bannerParameters.getEndDate()) : existingImage.getBannerImage().getEndDate(),
				null != bannerParameters.getIsBogoOffer() ? bogoOffer : existingImage.getBannerImage().getIsBogoOffer(), 
				null != bannerParameters.getCoBrandedPartner() ? bannerParameters.getCoBrandedPartner() : existingImage.getBannerImage().getCoBrandedPartner(),
				null != bannerParameters.getBannerPosition() ? bannerParameters.getBannerPosition().toUpperCase() : existingImage.getBannerImage().getBannerPosition())
				.offerId(null != bannerParameters.getOfferId() ? bannerParameters.getOfferId() : existingImage.getBannerImage().getOfferId())
				.offerType(null != bannerParameters.getOfferType() ? bannerParameters.getOfferType() : existingImage.getBannerImage().getOfferType())
				.deepLink(null != bannerParameters.getDeepLink() ? bannerParameters.getDeepLink() : existingImage.getBannerImage().getDeepLink())
				.build();
		
	}
	
	/**
	 * Create domain object to update image in DB for Gifting image - imageType - IMAGE.
	 * @param existingImage
	 * @param giftingParameters
	 * @param resultResponse
	 * @throws IOException
	 * @throws ParseException
	 */
	public void updateGiftingImageDomain(MarketplaceImage existingImage, GiftingParameters giftingParameters,
			ResultResponse resultResponse) throws IOException, ParseException {

		String status = null;
		Binary imageToSave = null;
		
		if (null != giftingParameters.getStatus()) {
			status = giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
					? ImageConstants.STATUS_ACTIVE
					: ImageConstants.STATUS_INACTIVE;
		}

		giftingParameters.setType(existingImage.getGiftingImage().getType());
		
		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, null != giftingParameters.getProgram() ? giftingParameters.getProgram() : existingImage.getProgramCode());
		commonAttributes.put(ImageConstants.USERNAME, giftingParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, giftingParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_GIFTING);
		commonAttributes.put(ImageConstants.IMAGE_PATH, null != giftingParameters.getFile() && !giftingParameters.getFile().isEmpty()
										? giftingParameters.getPath()
										: existingImage.getImageUrl());
		commonAttributes.put(ImageConstants.IMAGE_PATH_DR, null != giftingParameters.getFile() && !giftingParameters.getFile().isEmpty()
				? giftingParameters.getImageUrlDr()
				: existingImage.getImageUrlDr());
		commonAttributes.put(ImageConstants.IMAGE_PATH_PROD, null != giftingParameters.getFile() && !giftingParameters.getFile().isEmpty()
				? giftingParameters.getImageUrlProd()
				: existingImage.getImageUrlProd());
		commonAttributes.put(ImageConstants.IMAGE_STATUS, null != giftingParameters.getStatus() ? status : existingImage.getStatus());
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME, null != giftingParameters.getFile() && !giftingParameters.getFile().isEmpty()
										? giftingParameters.getFile().getOriginalFilename()
										: existingImage.getOriginalFileName());
		
		imageToSave = new Binary(BsonBinarySubType.BINARY, null != giftingParameters.getFile() && !giftingParameters.getFile().isEmpty() ? giftingParameters.getFile().getBytes() : existingImage.getImage().getData());
		
		GiftingImageDomain giftingImageDomain = createGiftingDomainForUpdateImageType(existingImage, giftingParameters);

		updateImageToDatabase(existingImage, commonAttributes, imageToSave, giftingImageDomain, resultResponse, giftingParameters.getExternalTransactionId(), ImageConfigurationConstants.UPDATE_IMAGE);
	
	}
	
	/**
	 * This method is used to create gifting image domain object for image update functionality - image type.
	 * @param existingImage
	 * @param giftingParameters
	 * @return
	 */
	private GiftingImageDomain createGiftingDomainForUpdateImageType(MarketplaceImage existingImage, GiftingParameters giftingParameters) {
		
		return new GiftingImageDomain.GiftingImageBuilder(
				existingImage.getGiftingImage().getImageId(),
				new LanguageDomain(
						null != giftingParameters.getNameEn() && !giftingParameters.getNameEn().isEmpty() ? giftingParameters.getNameEn() : existingImage.getGiftingImage().getName().getEnglish(), 
						null != giftingParameters.getNameAr() && !giftingParameters.getNameAr().isEmpty() ? giftingParameters.getNameAr() : existingImage.getGiftingImage().getName().getArabic()
						),
				existingImage.getGiftingImage().getType(), 
				null != giftingParameters.getPriority() ? giftingParameters.getPriority() : existingImage.getGiftingImage().getPriority(),
				null != giftingParameters.getBackgroundPriority() ? giftingParameters.getBackgroundPriority() : existingImage.getGiftingImage().getBackgroundPriority(),
				new LanguageDomain(
						null != giftingParameters.getGreetingMessageEn() && !giftingParameters.getGreetingMessageEn().isEmpty() ? giftingParameters.getGreetingMessageEn() : existingImage.getGiftingImage().getGreetingMessage().getEnglish(), 
						null != giftingParameters.getGreetingMessageAr() && !giftingParameters.getGreetingMessageAr().isEmpty() ? giftingParameters.getGreetingMessageAr() : existingImage.getGiftingImage().getGreetingMessage().getArabic()
						))
		.colorCode(null)
		.colorDirection(null)
		.build();
		
	}
	
	/**
	 * Create domain object to update image in DB for Gifting image - imageType - SOLID.
	 * @param existingImage
	 * @param giftingParameters
	 * @param resultResponse
	 * @throws IOException
	 * @throws ParseException
	 */
	public void updateGiftingSolidDomain(MarketplaceImage existingImage, GiftingParameters giftingParameters,
			ResultResponse resultResponse) {

		String status = null;
		Binary imageToSave = null;
		
		if (null != giftingParameters.getStatus()) {
			status = giftingParameters.getStatus().equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)
					? ImageConstants.STATUS_ACTIVE
					: ImageConstants.STATUS_INACTIVE;
		}

		giftingParameters.setType(existingImage.getGiftingImage().getType());
		
		HashMap<String, String> commonAttributes = new HashMap<>();
		commonAttributes.put(ImageConstants.PROGRAM_CODE, null != giftingParameters.getProgram() ? giftingParameters.getProgram() : existingImage.getProgramCode());
		commonAttributes.put(ImageConstants.USERNAME, giftingParameters.getUserName());
		commonAttributes.put(ImageConstants.TOKEN, giftingParameters.getToken());
		commonAttributes.put(ImageConstants.IMAGE_CATEGORY, ImageConstants.IMAGE_CATEGORY_GIFTING);
		commonAttributes.put(ImageConstants.IMAGE_PATH, null);
		commonAttributes.put(ImageConstants.IMAGE_STATUS, null != giftingParameters.getStatus() ? status : existingImage.getStatus());
		commonAttributes.put(ImageConstants.ORIGINAL_FILE_NAME, null);

		GiftingImageDomain giftingImageDomain = createGiftingDomainForUpdateSolidType(existingImage, giftingParameters);

		updateImageToDatabase(existingImage, commonAttributes, imageToSave, giftingImageDomain, resultResponse, giftingParameters.getExternalTransactionId(), ImageConfigurationConstants.UPDATE_IMAGE);
	
	}
	
	/**
	 * This method is used to create gifting image domain object for image update functionality - solid type.
	 * @param existingImage
	 * @param giftingParameters
	 * @return
	 */
	private GiftingImageDomain createGiftingDomainForUpdateSolidType(MarketplaceImage existingImage, GiftingParameters giftingParameters) {
		
		return new GiftingImageDomain.GiftingImageBuilder(
				existingImage.getGiftingImage().getImageId(),
				new LanguageDomain(
						null != giftingParameters.getNameEn() && !giftingParameters.getNameEn().isEmpty() ? giftingParameters.getNameEn() : existingImage.getGiftingImage().getName().getEnglish(), 
						null != giftingParameters.getNameAr() && !giftingParameters.getNameAr().isEmpty() ? giftingParameters.getNameAr() : existingImage.getGiftingImage().getName().getArabic()
						),
				existingImage.getGiftingImage().getType(), 
				null != giftingParameters.getPriority() ? giftingParameters.getPriority() : existingImage.getGiftingImage().getPriority(),
				null != giftingParameters.getBackgroundPriority() ? giftingParameters.getBackgroundPriority() : existingImage.getGiftingImage().getBackgroundPriority(),
				new LanguageDomain(
						null != giftingParameters.getGreetingMessageEn() && !giftingParameters.getGreetingMessageEn().isEmpty() ? giftingParameters.getGreetingMessageEn() : existingImage.getGiftingImage().getGreetingMessage().getEnglish(), 
						null != giftingParameters.getGreetingMessageAr() && !giftingParameters.getGreetingMessageAr().isEmpty() ? giftingParameters.getGreetingMessageAr() : existingImage.getGiftingImage().getGreetingMessage().getArabic()
						))
		.colorCode(null != giftingParameters.getColorCode() ? giftingParameters.getColorCode() : existingImage.getGiftingImage().getColorCode())
		.colorDirection(null != giftingParameters.getColorDirection() ? giftingParameters.getColorDirection()	: existingImage.getGiftingImage().getColorDirection())
		.build();
		
	}
	
	/**
	 * Method to save image to DB.
	 * @param commonAttributes
	 * @param imageToSave
	 * @param domainToSave
	 * @param resultResponse
	 * @param externalTransactionId
	 * @param api
	 */
	private void uploadImageToDatabase(HashMap<String, String> commonAttributes, Binary imageToSave, Object domainToSave, ResultResponse resultResponse, String externalTransactionId, String api) {
		try {
			imageDomain.uploadImageToDatabase(new ImageDomain.ImageBuilder(commonAttributes.get(ImageConstants.IMAGE_CATEGORY), commonAttributes.get(ImageConstants.IMAGE_PATH))
					.programCode(commonAttributes.get(ImageConstants.PROGRAM_CODE))
					.status(!commonAttributes.get(ImageConstants.IMAGE_CATEGORY)
							.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER)
									? commonAttributes.get(ImageConstants.IMAGE_STATUS)
									: null)
					.image(imageToSave)
					.imageUrlDr(commonAttributes.get(ImageConstants.IMAGE_PATH_DR))
					.imageUrlProd(commonAttributes.get(ImageConstants.IMAGE_PATH_PROD))
					.originalFileName(commonAttributes.get(ImageConstants.ORIGINAL_FILE_NAME))
					.merchantOfferImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER) ? (MerchantOfferImageDomain) domainToSave : null)
					.bannerImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER) ? (BannerImageDomain) domainToSave : null)
					.giftingImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING) ? (GiftingImageDomain) domainToSave : null)
					.createdDate(new Date())
					.createdUser(null != commonAttributes.get(ImageConstants.USERNAME) ? commonAttributes.get(ImageConstants.USERNAME) : commonAttributes.get(ImageConstants.TOKEN))
					.build(), ImageConstants.ACTION_INSERT, externalTransactionId, commonAttributes.get(ImageConstants.USERNAME), api, null);
		} catch (MarketplaceException e) {
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.CONTROLLER_HELPER_UPLOAD_DB,
					e.getClass() + e.getMessage(), ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED).printMessage());
		}
	}
	
	/**
	 * Method to update image in DB.
	 * @param existingImage
	 * @param commonAttributes
	 * @param imageToSave
	 * @param domainToSave
	 * @param resultResponse
	 * @param externalTransactionId
	 * @param api
	 */
	private void updateImageToDatabase(MarketplaceImage existingImage, HashMap<String, String> commonAttributes,
			Binary imageToSave, Object domainToSave, ResultResponse resultResponse, String externalTransactionId, String api) {
		try {
			imageDomain.uploadImageToDatabase(new ImageDomain.ImageBuilder(commonAttributes.get(ImageConstants.IMAGE_CATEGORY), commonAttributes.get(ImageConstants.IMAGE_PATH))
					.id(existingImage.getId())
					.programCode(commonAttributes.get(ImageConstants.PROGRAM_CODE))
					.status(!commonAttributes.get(ImageConstants.IMAGE_CATEGORY)
							.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER)
									? commonAttributes.get(ImageConstants.IMAGE_STATUS)
									: null)
					.image(imageToSave)
					.imageUrlDr(commonAttributes.get(ImageConstants.IMAGE_PATH_DR))
					.imageUrlProd(commonAttributes.get(ImageConstants.IMAGE_PATH_PROD))
					.originalFileName(commonAttributes.get(ImageConstants.ORIGINAL_FILE_NAME))
					.merchantOfferImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER) ? (MerchantOfferImageDomain) domainToSave : null)
					.bannerImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER) ? (BannerImageDomain) domainToSave : null)
					.giftingImageDomain(commonAttributes.get(ImageConstants.IMAGE_CATEGORY).equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING) ? (GiftingImageDomain) domainToSave : null)
					.createdDate(existingImage.getCreatedDate())
					.createdUser(existingImage.getCreatedUser())
					.updatedDate(new Date())
					.updatedUser(null != commonAttributes.get(ImageConstants.USERNAME) ? commonAttributes.get(ImageConstants.USERNAME) : commonAttributes.get(ImageConstants.TOKEN))
					.build(), ImageConstants.ACTION_UPDATE, externalTransactionId, commonAttributes.get(ImageConstants.USERNAME), api, existingImage);
		} catch (MarketplaceException e) {
			resultResponse.addErrorAPIResponse(e.getErrorCodeInt(), e.getErrorMsg());
			resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
					ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.CONTROLLER_HELPER_UPDATE_DB,
					e.getClass() + e.getMessage(), ImageCodes.IMAGE_UPDATE_FAILED).printMessage());
		}
	}
		
	/* GENERATE META DATA FOR MERCHANT & OFFER IMAGES */
	
	/**
	 * This method is used to generate meta data and store to DB.
	 * @param generateMetaDataRequest
	 * @param generateMetaDataResponse
	 * @param headers
	 * @return
	 */
	public GenerateMetaDataResponse generateMetaData(GenerateMetaDataRequest generateMetaDataRequest,
			GenerateMetaDataResponse generateMetaDataResponse, Headers headers) {
		
		if(!ImageValidator.validateMetaDataRequestParameters(generateMetaDataRequest, validator, generateMetaDataResponse)) {
			generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
			return generateMetaDataResponse;
		}
		
		if(!Utility.validateGenerateMetaDataRequestParameters(generateMetaDataRequest, generateMetaDataResponse)) {
			generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
			return generateMetaDataResponse;
		}
		
		try {
			
			headers.setProgram(programManagement.getProgramCode(headers.getProgram()));

			List<String> imagesNotMigrated = new ArrayList<>();
			
			Path path = Paths.get(PathBuilder.getStringPathMerchantOffer(generateMetaDataRequest.getImageType(),
					generateMetaDataRequest.getDomainName(), generateMetaDataRequest.getAvailableInChannel()));
	
			String directoryToGenerateMetaData = imageDirectoryPath + ImageConstants.IMAGES_DIRECTORY + path.toString();
			
			ImageDataResponse imageDataResponse = retrieveImageDataFromNginx(directoryToGenerateMetaData, headers, generateMetaDataResponse);
			
			if (null != imageDataResponse && null != imageDataResponse.getApiStatus() && null != imageDataResponse.getApiStatus().getErrors()
					&& !imageDataResponse.getApiStatus().getErrors().isEmpty()) {
				generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
				return generateMetaDataResponse;
			}
			
			if(null != imageDataResponse && null == imageDataResponse.getImageData() || imageDataResponse.getImageData().isEmpty()) {
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.DIRECTORY_NOT_FOUND.getIntId(),
						ImageCodes.DIRECTORY_NOT_FOUND.getMsg());
				generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
				return generateMetaDataResponse;
			}
			
			List<String> dataNotGenerated = imageDataResponse.getImagesList();
			int existingImage = 0;
			int totalImages = dataNotGenerated.size();
			int totalMetaSize = imageDataResponse.getImageData().size();
			
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.TOTAL_FILE_COUNT,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_GENERATE_META_DATA, totalMetaSize);			
		
			List<String> listOfImageDataGenerated = new ArrayList<>(totalMetaSize);
			for(ImageDataResult data : imageDataResponse.getImageData()) {
				
				if(checkImageExists(generateMetaDataRequest, data.getImageName())) {
					existingImage++;
					continue;
				}
				
				String imageUrl = saveImageToDirectoryUrl + ImageConstants.IMAGE_PATH_DIRECTORY
						+ ImageConstants.FORWARD_SLASH + path.toString() + ImageConstants.FORWARD_SLASH
						+ data.getImageName();
				
				String imageUrlDr = null;
				String imageUrlProd = null;
				
				if(null != productionEnv && productionEnv.equalsIgnoreCase(ImageConstants.YES)) {
					
					imageUrlDr = saveImageToDirectoryUrlDr + ImageConstants.IMAGE_PATH_DIRECTORY
							+ ImageConstants.FORWARD_SLASH + path.toString() + ImageConstants.FORWARD_SLASH
							+ data.getImageName();
					
					imageUrlProd = saveImageToDirectoryUrlProd + ImageConstants.IMAGE_PATH_DIRECTORY
							+ ImageConstants.FORWARD_SLASH + path.toString() + ImageConstants.FORWARD_SLASH
							+ data.getImageName();
	
				}
				
				MarketplaceImage imageToSave = new MarketplaceImage();
				MerchantOfferImage merchantOfferImage = new MerchantOfferImage();

				merchantOfferImage.setDomainId(data.getDomainId());
				merchantOfferImage.setDomainName(generateMetaDataRequest.getDomainName());
				merchantOfferImage.setImageSize(data.getImageSize());
				merchantOfferImage.setImageLength(data.getWidth());
				merchantOfferImage.setImageHeight(data.getHeight());
				merchantOfferImage.setAvailableInChannel(generateMetaDataRequest.getAvailableInChannel());
				merchantOfferImage.setImageType(generateMetaDataRequest.getImageType());
				
				imageToSave.setProgramCode(headers.getProgram());
				imageToSave.setImageCategory(ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER);
				imageToSave.setImage(new Binary(BsonBinarySubType.BINARY, data.getByteArray()));
				imageToSave.setImageUrl(imageUrl);
				imageToSave.setImageUrlDr(imageUrlDr);
				imageToSave.setImageUrlProd(imageUrlProd);
				imageToSave.setOriginalFileName(data.getImageName());
				imageToSave.setMerchantOfferImage(merchantOfferImage);
				imageToSave.setCreatedUser(headers.getUserName());
				imageToSave.setCreatedDate(new Date());
							
				imageRepository.save(imageToSave);

				listOfImageDataGenerated.add(data.getImageName());
				
			}
			
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IMAGE_MIRGRATION_ERRORS,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_GENERATE_META_DATA, imagesNotMigrated);			
		
			dataNotGenerated.removeAll(listOfImageDataGenerated);
			
			GenerateMetaDataResult generateMetaDataResult = new GenerateMetaDataResult();
			generateMetaDataResult.setDomainName(generateMetaDataRequest.getDomainName());
			generateMetaDataResult.setAvailableInChannel(generateMetaDataRequest.getAvailableInChannel());
			generateMetaDataResult.setImageType(generateMetaDataRequest.getImageType());
			generateMetaDataResult.setTotalFileCount(imageDataResponse.getTotalFileCount());
			generateMetaDataResult.setGeneratedDataCount(listOfImageDataGenerated.size());
			generateMetaDataResponse.setGeneratedMetaDataStats(generateMetaDataResult);
			
			if (imageDataResponse.getDataGeneratedCount() != existingImage) {
				generateMetaDataResult.setFailedDataGeneration(
						!dataNotGenerated.isEmpty() && existingImage != totalImages ? dataNotGenerated : null);
			} else {
				generateMetaDataResult.setFailedDataGeneration(new ArrayList<>());
			}
			
			if(imageDataResponse.getDataGeneratedCount() == listOfImageDataGenerated.size()) {
			
				generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_SUCCESS.getId(),
						ImageCodes.BULK_IMAGE_UPLOAD_SUCCESS.getMsg());
		
			} else if(imageDataResponse.getDataGeneratedCount() == existingImage){ 
				
				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.META_DATA_ALREADY_GENERATED.getIntId(),
						ImageCodes.META_DATA_ALREADY_GENERATED.getMsg() + generateMetaDataRequest.getImageType());
				generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
				
			} else {
			
				generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
			}
			
		} catch (MarketplaceException me) {
			generateMetaDataResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (Exception e) {
			generateMetaDataResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					ImageCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			generateMetaDataResponse.setResult(ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.BULK_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.CONTROLLER_HELPER_GENERATE_META_DATA,
					e.getClass() + e.getMessage(), ImageCodes.BULK_IMAGE_UPLOAD_FAILED).printMessage());
		}
		
		return generateMetaDataResponse;

	}
	
	/**
	 * This method checks if an image already exists with the given name.
	 * @param generateMetaDataRequest
	 * @param originalFileName
	 * @return
	 */
	public boolean checkImageExists(GenerateMetaDataRequest generateMetaDataRequest, String originalFileName) {
			
		MarketplaceImage imageExists = imageRepository
				.findByOriginalFileNameAndDomainNameAndChannelAndImageTypeAndImageCategory(originalFileName,
						generateMetaDataRequest.getAvailableInChannel(), generateMetaDataRequest.getDomainName(),
						generateMetaDataRequest.getImageType(), ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER);

		return null != imageExists;
		
	}
		
	/* IMAGE UPLOAD TO NGINX SERVER */
	
	/**
	 * Call to nginx server to upload image.
	 * @param file
	 * @param path
	 * @throws MarketplaceException 
	 */
	public boolean uploadImage(MultipartFile file, String path, String productionPath, String action, String existingPath, String existingPathProd, Headers header, ResultResponse resultResponse) throws MarketplaceException {

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IN_PROD_SERVER,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_UPLOAD_NGINX, productionEnv);
		
		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.UPLOADING_TO_NGINX_SERVER,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_UPLOAD_NGINX);
		
		final String uri = imageUploadBase + uploadImage;
			try {
	
				Resource fileResource = file.getResource();
				LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
				parts.add(ImageConfigurationConstants.FILE, fileResource);
				parts.add(ImageConfigurationConstants.PATH, path);
				parts.add(ImageConfigurationConstants.ACTION, action);
				parts.add(ImageConfigurationConstants.EXISTING_PATH, existingPath);
				HttpHeaders headers = serviceHelper.getHeader(header);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);
				ResultResponse responseFromNginx = retryCallForUploadImage(uri, requestEntity);
	
				if (null != responseFromNginx.getApiStatus() && responseFromNginx.getApiStatus().getStatusCode() == 1) {
	
					resultResponse.addErrorAPIResponse(ImageCodes.NGINX_ERROR_RESPONSE.getIntId(),
							ImageCodes.NGINX_ERROR_RESPONSE.getMsg());
					return false;
	
				}
				
				if(null != productionEnv && productionEnv.equalsIgnoreCase(ImageConstants.YES)) {
					
					final String uriProd = imageUploadBaseProduction + uploadImage;
					
					try {
	
						Resource fileResourceProd = file.getResource();
						LinkedMultiValueMap<String, Object> partsProd = new LinkedMultiValueMap<>();
						partsProd.add(ImageConfigurationConstants.FILE, fileResourceProd);
						partsProd.add(ImageConfigurationConstants.PATH, productionPath);
						partsProd.add(ImageConfigurationConstants.ACTION, action);
						partsProd.add(ImageConfigurationConstants.EXISTING_PATH, existingPathProd);
						HttpHeaders headersProd = serviceHelper.getHeader(header);
						headersProd.setContentType(MediaType.MULTIPART_FORM_DATA);
						HttpEntity<LinkedMultiValueMap<String, Object>> requestEntityProd = new HttpEntity<>(partsProd,
								headersProd);
						ResultResponse responseFromNginxProd = retryCallForUploadImage(uriProd, requestEntityProd);
	
						if (null != responseFromNginxProd.getApiStatus()
								&& responseFromNginxProd.getApiStatus().getStatusCode() == 1) {
	
							resultResponse.addErrorAPIResponse(ImageCodes.SECOND_NGINX_UPLOAD_ERROR_RESPONSE.getIntId(),
									ImageCodes.SECOND_NGINX_UPLOAD_ERROR_RESPONSE.getMsg());
	
							deleteImageFromNginx(header, path, imageUploadBase, ImageConstants.ACTION_ERROR, resultResponse);
	
							return false;
	
						}
	
						if (action.equalsIgnoreCase(ImageConstants.ACTION_UPDATE)) {
	
							deleteImageFromNginx(header, existingPath, imageUploadBase, ImageConstants.ACTION_UPDATE, resultResponse);
							deleteImageFromNginx(header, existingPathProd, imageUploadBaseProduction, ImageConstants.ACTION_UPDATE, resultResponse);
	
						}
	
					} catch (RestClientException e) {
						deleteImageFromNginx(header, path, imageUploadBase, ImageConstants.ACTION_ERROR, resultResponse);
						resultResponse.addErrorAPIResponse(ImageCodes.NGINX_SERVER_SECOND_CONNECTION_ERROR.getIntId(),
								ImageCodes.NGINX_SERVER_SECOND_CONNECTION_ERROR.getMsg());
						return false;
					}
					
				} else {
					
					if (action.equalsIgnoreCase(ImageConstants.ACTION_UPDATE)) {
	
						deleteImageFromNginx(header, existingPath, imageUploadBase, ImageConstants.ACTION_UPDATE, resultResponse);
	
					}
					
				}
				
			} catch (RestClientException e) {
				throw new MarketplaceException(this.getClass().toString(),
						ImageConfigurationConstants.CONTROLLER_HELPER_UPLOAD_NGINX, e.getClass() + e.getMessage(),
						ImageCodes.NGINX_SERVER_CONNECTION_ERROR);
			} catch (Exception e) {
				LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.CONTROLLER_HELPER_UPLOAD_NGINX,
						e.getClass() + e.getMessage(), ImageCodes.NGINX_SERVER_CONNECTION_ERROR).printMessage());
			}
			
			return true;
	}
	
	private ResultResponse retryCallForUploadImage(String url, HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForUploadImage method of class {}", this.getClass().getName());
		return retryTemplate.execute(context ->{
			return restTemplate.postForObject(url, requestEntity, ResultResponse.class);
		});		
	}
	
	/**
	 * This method is used to delete from first nginx server, if upload to the second server fails.
	 * @param headers
	 * @param path
	 * @param resultResponse
	 * @return
	 */
	private void deleteImageFromNginx(Headers headers, String path, String baseUrl, String action, ResultResponse resultResponse) {
		
		if(null != action && action.equalsIgnoreCase(ImageConstants.ACTION_ERROR)) {
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DELETNG_IMAGE_NGINX_SERVER_CAUSE_ERROR,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_DELETE_IMAGE_NGINX);
		} else {
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DELETNG_IMAGE_NGINX_SERVER_CAUSE_UPDATE,
					this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_HELPER_DELETE_IMAGE_NGINX);
		}
		
		final String deleteImgeUri = baseUrl + deleteImage;
		
		HttpHeaders httpHeaders = serviceHelper.getHeader(headers);
		httpHeaders.add(ImageConstants.IMAGE_PATH, path);          
		HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(httpHeaders);			
		ResultResponse responseFromNginxProd = restTemplate.postForObject(deleteImgeUri, requestEntity, ResultResponse.class);
		
		if (null != responseFromNginxProd.getApiStatus() && responseFromNginxProd.getApiStatus().getStatusCode() == 1) {

			resultResponse.addErrorAPIResponse(ImageCodes.NGINX_ERROR_RESPONSE_DELETE_IMAGE.getIntId(),
					ImageCodes.NGINX_ERROR_RESPONSE_DELETE_IMAGE.getMsg());

		}
		
	}
	
	/**
	 * This method makes a REST call to Nginx server API to retrieve meta data for a list of images.
	 * @param path
	 * @param header
	 * @param generateMetaDataResponse
	 * @return
	 * @throws MarketplaceException
	 */
	private ImageDataResponse retrieveImageDataFromNginx(String path, Headers header, GenerateMetaDataResponse generateMetaDataResponse) throws MarketplaceException {

		final String uri = imageUploadBase + retrieveImageData;
		ImageDataResponse responseFromNginx = null;
		try {
	
			HttpHeaders headers = serviceHelper.getHeader(header);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(ImageConfigurationConstants.PATH, path);
			HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

			responseFromNginx = retryCallForRetrieveImageDataFromNginx(uri, requestEntity);

			if (null != responseFromNginx.getApiStatus() && responseFromNginx.getApiStatus().getStatusCode() == 1) {

				generateMetaDataResponse.addErrorAPIResponse(ImageCodes.NGINX_ERROR_RESPONSE.getIntId(),
						ImageCodes.NGINX_ERROR_RESPONSE.getMsg());
				return responseFromNginx;

			}

		} catch (RestClientException e) {
			throw new MarketplaceException(this.getClass().toString(),
					ImageConfigurationConstants.CONTROLLER_HELPER_RETRIEVE_DATA_NGINX, e.getClass() + e.getMessage(),
					ImageCodes.NGINX_SERVER_CONNECTION_ERROR_META_DATA);
		} catch (Exception e) {
			LOG.error(new MarketplaceException(this.getClass().toString(),
					ImageConfigurationConstants.CONTROLLER_HELPER_RETRIEVE_DATA_NGINX, e.getClass() + e.getMessage(),
					ImageCodes.NGINX_SERVER_CONNECTION_ERROR_META_DATA).printMessage());
		}

		return responseFromNginx;
		
	}
	
	
	private ImageDataResponse retryCallForRetrieveImageDataFromNginx(String url, HttpEntity<HttpHeaders> requestEntity) {
		
		LOG.info("inside Retry block using retryTemplate of retryCallForRetrieveImageDataFromNginx method of class {}", this.getClass().getName());
		return retryTemplate.execute(context -> {
			return restTemplate.postForObject(url, requestEntity, ImageDataResponse.class);
		});		
	}
	
}
