package com.loyalty.marketplace.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.outbound.database.entity.image.MarketplaceImage;

public interface ImageRepository extends MongoRepository<MarketplaceImage, String> {
	
	Optional<MarketplaceImage> findById(String id);
	
	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.Channel': ?1, 'MerchantOfferImage.DomainName': ?2, 'MerchantOfferImage.ImageType': ?3}")
	List<MarketplaceImage> findByImageCategoryAndChannelAndDomainNameAndImageType(String channel, String domainName, String imageType);
	
	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.DomainId': ?1, 'MerchantOfferImage.ImageType': ?2, 'MerchantOfferImageAvailableInChannel': ?3}")
	MarketplaceImage findByImageCategoryAndDomainIdAndImageTypeAndAvailableInChannel(String imageCategory, String domainId, String imageType, String availableInChannel);

	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.DomainId': ?1}")
	List<MarketplaceImage> findByImageCategoryAndDomainId(String imageCategory, String domainId);
	
	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.DomainId': ?1, 'ProgramCode': {$regex:?2,$options:'i'}}")
	List<MarketplaceImage> findByImageCategoryAndDomainIdAndProgramCodeIgnoreCase(String imageCategory, String domainId, String programCode);
	
	MarketplaceImage findFirst1ByImageCategoryOrderByGiftingImage_ImageIdDesc(String imageCategory);
	
	MarketplaceImage findByImageCategoryAndOriginalFileName(String imageCategory, String originalFileName);
	
	List<MarketplaceImage> findByImageCategoryAndStatus(String imageCategory, String status);
	
	List<MarketplaceImage> findByImageCategory(String imageCategory);
	
	List<MarketplaceImage> findByImageCategoryAndMerchantOfferImageDomainIdIn(String imageCategory, List<String> domainId);
	
}
