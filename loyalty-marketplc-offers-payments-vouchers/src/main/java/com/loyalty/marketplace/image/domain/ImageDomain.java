package com.loyalty.marketplace.image.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.image.constants.ImageCodes;
import com.loyalty.marketplace.image.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.image.constants.ImageConstants;
import com.loyalty.marketplace.image.helper.ImageControllerHelper;
import com.loyalty.marketplace.image.helper.ImageValidator;
import com.loyalty.marketplace.image.helper.Utility;
import com.loyalty.marketplace.image.inbound.dto.BannerParameters;
import com.loyalty.marketplace.image.inbound.dto.GiftingParameters;
import com.loyalty.marketplace.image.inbound.dto.ImageParameters;
import com.loyalty.marketplace.image.inbound.dto.MerchantOfferParameters;
import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;
import com.loyalty.marketplace.image.outbound.database.repository.ImageRepository;
import com.loyalty.marketplace.image.outbound.dto.ListImageResponse;
import com.loyalty.marketplace.image.outbound.dto.ListImageResult;
import com.loyalty.marketplace.image.outbound.dto.ResultResponse;
import com.loyalty.marketplace.offers.domain.model.OfferCatalogDomain;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.utils.MarketplaceException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ImageDomain {

	private static final Logger LOG = LoggerFactory.getLogger(ImageDomain.class);
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	Validator validator;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	ProgramManagement programManagement;
	
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ImageControllerHelper imageControllerHelper;
	
	@Autowired
	OfferCatalogDomain offerCatalogDomain;
	
	private String id;
	private String programCode;
	private String imageCategory;
	private String imageUrl;
	private String imageUrlDr;
	private String imageUrlProd;
	private String status;
	private Binary image;
	private String originalFileName;
	private MerchantOfferImageDomain merchantOfferImageDomain;
	private BannerImageDomain bannerImageDomain;
	private GiftingImageDomain giftingImageDomain;
	private String createdUser;
	private Date createdDate;
	private String updatedUser;
	private Date updatedDate;
	
	public ImageDomain(ImageBuilder imageBuilder) {
		this.id = imageBuilder.id;
		this.programCode = imageBuilder.programCode;
		this.imageCategory = imageBuilder.imageCategory;
		this.imageUrl = imageBuilder.imageUrl;
		this.imageUrlDr = imageBuilder.imageUrlDr;
		this.imageUrlProd = imageBuilder.imageUrlProd;
		this.status = imageBuilder.status;
		this.originalFileName = imageBuilder.originalFileName;
		this.image = imageBuilder.image;
		this.merchantOfferImageDomain = imageBuilder.merchantOfferImageDomain;
		this.bannerImageDomain =  imageBuilder.bannerImageDomain;
		this.giftingImageDomain = imageBuilder.giftingImageDomain;
		this.createdUser = imageBuilder.createdUser;
		this.createdDate = imageBuilder.createdDate;
		this.updatedUser = imageBuilder.updatedUser;
		this.updatedDate = imageBuilder.updatedDate;
	}
	
	public static class ImageBuilder {
		
		private String id;
		private String programCode;
		private String imageCategory;
		private String imageUrl;
		private String imageUrlDr;
		private String imageUrlProd;
		private String status;
		private Binary image;
		private String originalFileName;
		private MerchantOfferImageDomain merchantOfferImageDomain;
		private BannerImageDomain bannerImageDomain;
		private GiftingImageDomain giftingImageDomain;
		private String createdUser;
		private Date createdDate;
		private String updatedUser;
		private Date updatedDate;
		
		public ImageBuilder(String imageCategory, String imageUrl) {
			super();
			this.imageCategory = imageCategory;
			this.imageUrl = imageUrl;
		}
		
		public ImageBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public ImageBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public ImageBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public ImageBuilder imageUrlDr(String imageUrlDr) {
			this.imageUrlDr = imageUrlDr;
			return this;
		}
		
		public ImageBuilder imageUrlProd(String imageUrlProd) {
			this.imageUrlProd = imageUrlProd;
			return this;
		}
		
		public ImageBuilder image(Binary image) {
			this.image = image;
			return this;
		}
		
		public ImageBuilder originalFileName(String originalFileName) {
			this.originalFileName = originalFileName;
			return this;
		}

		public ImageBuilder merchantOfferImageDomain(MerchantOfferImageDomain merchantOfferImageDomain) {
			this.merchantOfferImageDomain = merchantOfferImageDomain;
			return this;
		}
		
		public ImageBuilder bannerImageDomain(BannerImageDomain bannerImageDomain) {
			this.bannerImageDomain = bannerImageDomain;
			return this;
		}
		
		public ImageBuilder giftingImageDomain(GiftingImageDomain giftingImageDomain) {
			this.giftingImageDomain = giftingImageDomain;
			return this;
		}
		
		public ImageBuilder createdUser(String createdUser) {
			this.createdUser = createdUser;
			return this;
		}
		
		public ImageBuilder createdDate(Date createdDate) {
			this.createdDate = createdDate;
			return this;
		}
		
		public ImageBuilder updatedUser(String updatedUser) {
			this.updatedUser = updatedUser;
			return this;
		}
		
		public ImageBuilder updatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}
		
		public ImageDomain build() {
			return new ImageDomain(this);
		}
		
	}

	/**
	 * Method to configure Merchant-Offer image.
	 * @param merchantOfferParameters
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse validateUploadMerchantOfferImage(MerchantOfferParameters merchantOfferParameters,
			ResultResponse resultResponse, Headers header) {
	
		try {
			
			merchantOfferParameters.setProgram(programManagement.getProgramCode(merchantOfferParameters.getProgram()));
			
			if(!ImageValidator.validateRequestParameters(merchantOfferParameters, validator, resultResponse)) {
				resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
				return resultResponse;
			}
			
			if(!imageControllerHelper.validateFileField(merchantOfferParameters.getFile(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateImageFormat(merchantOfferParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateChannel(merchantOfferParameters.getAvailableInChannel(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateDomainName(merchantOfferParameters.getDomainName(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.checkMerchantOfferExists(merchantOfferParameters.getDomainName(), merchantOfferParameters.getDomainId(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.checkImageExistsMerchantOffer(merchantOfferParameters.getDomainId(), merchantOfferParameters.getImageType().toUpperCase(), merchantOfferParameters.getAvailableInChannel().toUpperCase(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateImageTypeForDomain(merchantOfferParameters, resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateImageDimensionsMerchantOffer(merchantOfferParameters, resultResponse)) return resultResponse;
			if(!imageControllerHelper.saveImageToDirectoryPath(merchantOfferParameters, ImageConstants.IMAGE_CATEGORY_MERCHANT_OFFER, ImageConstants.ACTION_INSERT, null, null, header, resultResponse)) return resultResponse;
			
			imageControllerHelper.createMerchantOfferImageDomain(merchantOfferParameters, resultResponse);
			
			if (merchantOfferParameters.getDomainName().equalsIgnoreCase(ImageConstants.DOMAIN_NAME_MERCHANT)) {
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_MERCHANT_SUCCESS.getId(),
						ImageCodes.IMAGE_UPLOAD_MERCHANT_SUCCESS.getMsg());
			} else {
				
				resultResponse.setResult(ImageCodes.IMAGE_UPLOAD_OFFER_SUCCESS.getId(),
						ImageCodes.IMAGE_UPLOAD_OFFER_SUCCESS.getMsg());
				
				offerCatalogDomain.getAndSaveEligibleOffers(header);
				
			}
		
        } catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (ParseException e) {
			resultResponse.addErrorAPIResponse(ImageCodes.DATE_STRING_PARSE_ERROR.getIntId(),
					ImageCodes.DATE_STRING_PARSE_ERROR.getMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
		} catch (Exception e) {
        	resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
        			ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_MERCHANT_OFFER_IMAGE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
        }
		
		return resultResponse;
	}
	
	/**
	 * Method to configure Banner image.
	 * @param bannerParameters
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse validateUploadBannerImage(BannerParameters bannerParameters, ResultResponse resultResponse, Headers header) {
		
		try {
			
			bannerParameters.setProgram(programManagement.getProgramCode(bannerParameters.getProgram()));
			
			if(!ImageValidator.validateRequestParameters(bannerParameters, validator, resultResponse)) {
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				return resultResponse;
			}
			
			if (!Utility.validateBannerBooleanParameters(bannerParameters.getIsStaticBanner(),
					bannerParameters.getIsBogoOffer(), resultResponse)) {
				resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
				return resultResponse;
			}
			
			if(!imageControllerHelper.validateFileField(bannerParameters.getFile(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateBannerImageRequestParameters(bannerParameters, resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateImageFormat(bannerParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateBannerCategories(bannerParameters.getBannerPosition(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.checkBannerOfferExists(bannerParameters, resultResponse)) return resultResponse; 
			if(!imageControllerHelper.checkImageExistsBannerGifting(ImageConstants.IMAGE_CATEGORY_BANNER, bannerParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
			if(!imageControllerHelper.validateImageDimensionsBanner(bannerParameters, resultResponse)) return resultResponse;
			if(!imageControllerHelper.saveImageToDirectoryPath(bannerParameters, ImageConstants.IMAGE_CATEGORY_BANNER, ImageConstants.ACTION_INSERT, null, null, header, resultResponse)) return resultResponse; 
			
			imageControllerHelper.createBannerImageDomain(bannerParameters, resultResponse);

			resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_SUCCESS.getId(),
					ImageCodes.BANNER_IMAGE_UPLOAD_SUCCESS.getMsg());
			
        } catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (ParseException e) {
			resultResponse.addErrorAPIResponse(ImageCodes.DATE_STRING_PARSE_ERROR.getIntId(),
					ImageCodes.DATE_STRING_PARSE_ERROR.getMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
		} catch (Exception e) {
        	resultResponse.setResult(ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getId(),
        			ImageCodes.BANNER_IMAGE_UPLOAD_FAILED.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_BANNER_IMAGE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
        }
		
		return resultResponse;
	}
	
	/**
	 * Method to configure Gifting image.
	 * @param giftingParameters
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse validateUploadGiftingImage(GiftingParameters giftingParameters, ResultResponse resultResponse, Headers header) {
		
		try {
        
			giftingParameters.setProgram(programManagement.getProgramCode(giftingParameters.getProgram()));
			
			if(!ImageValidator.validateRequestParameters(giftingParameters, validator, resultResponse)) {
				resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
				return resultResponse;
			}
			
			if(!imageControllerHelper.validateGiftingImageRequestParameters(giftingParameters, resultResponse)) return resultResponse;
			if(giftingParameters.getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_IMAGE)) {
				if(!imageControllerHelper.validateImageFormat(giftingParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
				if(!imageControllerHelper.checkImageExistsBannerGifting(ImageConstants.IMAGE_CATEGORY_GIFTING, giftingParameters.getFile().getOriginalFilename(), resultResponse)) return resultResponse;
				if(!imageControllerHelper.validateImageDimensionsGifting(giftingParameters, resultResponse)) return resultResponse;
				if(!imageControllerHelper.saveImageToDirectoryPath(giftingParameters, ImageConstants.IMAGE_CATEGORY_GIFTING, ImageConstants.ACTION_INSERT, null, null, header, resultResponse)) return resultResponse;
			}
			
			imageControllerHelper.generateImageIdGifting(giftingParameters);
			imageControllerHelper.createGiftingImageDomain(giftingParameters, resultResponse);
			
			resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_SUCCESS.getId(),
					ImageCodes.GIFTING_IMAGE_UPLOAD_SUCCESS.getMsg());
			
        } catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (ParseException e) {
			resultResponse.addErrorAPIResponse(ImageCodes.DATE_STRING_PARSE_ERROR.getIntId(),
					ImageCodes.DATE_STRING_PARSE_ERROR.getMsg());
			resultResponse.setResult(ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getId(),
					ImageCodes.MARKETPLACE_IMAGE_UPLOAD_FAILED.getMsg());
		} catch (Exception e) {
        	resultResponse.setResult(ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getId(),
        			ImageCodes.GIFTING_IMAGE_UPLOAD_FAILED.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_GIFTING_IMAGE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
        }
		
		return resultResponse;
	}
		
	/** 
	 * Method to update Banner & Gifting image.
	 * @param imageParameters
	 * @param id
	 * @param resultResponse
	 * @return
	 */
	public ResultResponse updateBannerGiftingImage(ImageParameters imageParameters, String id, ResultResponse resultResponse, Headers header) {
		
		try {
			
			Optional<MarketplaceImage> image = imageRepository.findById(id);
			if(!image.isPresent()) {
				resultResponse.addErrorAPIResponse(ImageCodes.UPDATE_IMAGE_NOT_FOUND.getIntId(),
						ImageCodes.UPDATE_IMAGE_NOT_FOUND.getMsg());
				resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
	        			ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
				return resultResponse;
			}
			
			if(image.get().getImageCategory().equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)) imageControllerHelper.updateBannerImage(image, imageParameters, resultResponse, header);
			if(image.get().getImageCategory().equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_GIFTING)) {
				if(image.get().getGiftingImage().getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_IMAGE)) {
					imageControllerHelper.updateGiftingImageType(image, imageParameters, resultResponse, header);
				} 
				if(image.get().getGiftingImage().getType().equalsIgnoreCase(ImageConstants.GIFTING_TYPE_SOLID)) {
					imageControllerHelper.updateGiftingSolidType(image, imageParameters, resultResponse);
				} 
			}
			
			if(resultResponse.getApiStatus().getErrors().isEmpty()) {
				resultResponse.setResult(ImageCodes.IMAGE_UPDATE_SUCCESS.getId(), ImageCodes.IMAGE_UPDATE_SUCCESS.getMsg());
			} else {
				resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(), ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
			}
			
        } catch (MarketplaceException me) {
			resultResponse.addErrorAPIResponse(me.getErrorCodeInt(), me.getErrorMsg());
			resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
					ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), me.getMethodName(),
					me.getClass() + me.getMessage(), me.getErrorCodeInt(), me.getErrorMsg()).printMessage());
		} catch (ParseException e) {
			resultResponse.addErrorAPIResponse(ImageCodes.DATE_STRING_PARSE_ERROR.getIntId(),
					ImageCodes.DATE_STRING_PARSE_ERROR.getMsg());
			resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
					ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
		} catch (Exception e) {
        	resultResponse.setResult(ImageCodes.IMAGE_UPDATE_FAILED.getId(),
        			ImageCodes.IMAGE_UPDATE_FAILED.getMsg());
        	resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_UPDATE_IMAGE,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
        }
		
		return resultResponse;
	}
	
	/**
	 * Method to list images.
	 * @param listImageResponse
	 * @param imageType
	 * @param status
	 * @return
	 */
	public ListImageResponse listAllImages(ListImageResponse listImageResponse, String imageType, String status) {
		
		try {
			
			List<ListImageResult> imagesResponseList = new ArrayList<>();
			List<MarketplaceImage> allImages = new ArrayList<>();
			
			if(!Utility.validateListingInputParameters(listImageResponse, imageType, status)) return listImageResponse;
			
			if (modelMapper.getTypeMap(MarketplaceImage.class, ListImageResult.class) == null) {
				modelMapper.addMappings(Utility.imageFieldMapping);
			}
			
			if (null != imageType && !imageType.isEmpty()) {
				imageType = imageType.equalsIgnoreCase(ImageConstants.IMAGE_CATEGORY_BANNER)
						? ImageConstants.IMAGE_CATEGORY_BANNER
						: ImageConstants.IMAGE_CATEGORY_GIFTING;
			} else {
				allImages = imageRepository.findAll();
			}
			
			if (null != imageType && !imageType.isEmpty() && null != status && status.equalsIgnoreCase(ImageConstants.STATUS_ACTIVE)) {
				allImages = imageRepository.findByImageCategoryAndStatus(imageType, ImageConstants.STATUS_ACTIVE);
			} else if(null != imageType && !imageType.isEmpty()) {
				allImages = imageRepository.findByImageCategory(imageType);
			}

			if (allImages.isEmpty()) {
				listImageResponse.addErrorAPIResponse(ImageCodes.NO_IMAGES_AVAILABLE_TO_LIST.getIntId(),
						ImageCodes.NO_IMAGES_AVAILABLE_TO_LIST.getMsg());
				listImageResponse.setResult(ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getId(),
						ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getMsg());
				return listImageResponse;
			}
			
			for (MarketplaceImage listImage : allImages) {
				ListImageResult imageResult = modelMapper.map(listImage, ListImageResult.class);
				imagesResponseList.add(imageResult);
			}
			
			listImageResponse.setImages(imagesResponseList);
			listImageResponse.setResult(ImageCodes.LISTING_MARKETPLACE_IMAGES_SUCCESS.getId(),
					ImageCodes.LISTING_MARKETPLACE_IMAGES_SUCCESS.getMsg());
			
		} catch (Exception e) {
			listImageResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(),
					ImageCodes.GENERIC_RUNTIME_EXCEPTION.getMsg());
			listImageResponse.setResult(ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getId(),
					ImageCodes.LISTING_MARKETPLACE_IMAGES_FAILED.getMsg());
			LOG.error(new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_LIST_IMAGES,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}
		
		return listImageResponse;
		
	}

	/**
	 * This method is used to retrieve image stored in DB in binary format and return it in the API response.
	 * @param id
	 * @param httpServletResponse
	 * @param resultResponse
	 */
	public void displayImage(String id, HttpServletResponse httpServletResponse, ResultResponse resultResponse) {

		Gson gson = new Gson();
		Optional<MarketplaceImage> retrievedImage = imageRepository.findById(id);

		try {

			if (retrievedImage.isPresent()) {

				MarketplaceImage displayIimage = retrievedImage.get();
				InputStream targetStream = new ByteArrayInputStream(displayIimage.getImage().getData());
				httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
				IOUtils.copy(targetStream, httpServletResponse.getOutputStream());

			} else {

				resultResponse.addErrorAPIResponse(ImageCodes.NO_IMAGE_AVAILABLE_FOR_ID.getIntId(),
						ImageCodes.NO_IMAGE_AVAILABLE_FOR_ID.getMsg() + id);
				resultResponse.setResult(ImageCodes.DISPLAY_IMAGE_FAILED.getId(),
						ImageCodes.DISPLAY_IMAGE_FAILED.getMsg());

				String responseJson = gson.toJson(resultResponse);
				PrintWriter out = httpServletResponse.getWriter();
				httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				out.print(responseJson);
				out.flush();

			}

		} catch (Exception e) {
			resultResponse.setResult(ImageCodes.DISPLAY_IMAGE_FAILED.getId(),
					ImageCodes.DISPLAY_IMAGE_FAILED.getMsg());
			resultResponse.addErrorAPIResponse(ImageCodes.GENERIC_RUNTIME_EXCEPTION.getIntId(), e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					ImageConfigurationConstants.IMAGE_DOMAIN_DISPLAY_IMG, e.getClass() + e.getMessage(),
					ImageCodes.GENERIC_RUNTIME_EXCEPTION).printMessage());
		}

	}
	
	/**
	 * Method to upload images and other attributes to MarketplaceImage DB.
	 * @param imageDomain
	 * @param action
	 * @param externalTransactionId
	 * @param userName
	 * @param api
	 * @param existingEntity
	 * @return
	 * @throws MarketplaceException
	 */
	public MarketplaceImage uploadImageToDatabase(ImageDomain imageDomain, String action, String externalTransactionId, String userName, String api, MarketplaceImage existingEntity) throws MarketplaceException {
		
		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DOMAIN_TO_SAVE,
				this.getClass().getName(), ImageConfigurationConstants.IMAGE_DOMAIN_SAVE_DB, imageDomain);
		
		try {
			
			MarketplaceImage imageToUpload = modelMapper.map(imageDomain, MarketplaceImage.class);
			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.ENTITY_TO_SAVE,
    				this.getClass().getName(), ImageConfigurationConstants.IMAGE_DOMAIN_SAVE_DB, imageToUpload);
			
			MarketplaceImage uploadedImage = imageRepository.save(imageToUpload);
        	LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.SAVED_ENTITY,
    				this.getClass().getName(), ImageConfigurationConstants.IMAGE_DOMAIN_SAVE_DB, uploadedImage);
        	      	
        	if (action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {

//				auditService.insertDataAudit(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE, uploadedImage,
//						api, externalTransactionId, userName);

			} else if (action.equalsIgnoreCase(ImageConstants.ACTION_UPDATE)) {

				auditService.updateDataAudit(ImageConfigurationConstants.DB_MARKETPLACE_IMAGE, uploadedImage,
						api, existingEntity, externalTransactionId, userName);
			
			}
        	
        	return imageToUpload;
		
		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(), ImageConfigurationConstants.IMAGE_DOMAIN_SAVE_DB,
					e.getClass() + e.getMessage(), ImageCodes.GENERIC_RUNTIME_EXCEPTION);
		}
		
	}
	
}
