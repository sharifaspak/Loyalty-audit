package com.loyalty.marketplace.image.outbound.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.loyalty.marketplace.image.outbound.database.entity.MarketplaceImage;

public interface ImageRepository extends MongoRepository<MarketplaceImage, String> {
	
	Optional<MarketplaceImage> findById(String id);
	
	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.AvailableInChannel': ?1, 'MerchantOfferImage.DomainName': ?2, 'MerchantOfferImage.ImageType': ?3}")
	List<MarketplaceImage> findByImageCategoryAndChannelAndDomainNameAndImageType(String imageCategory, String channel, String domainName, String imageType);
	
	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.DomainId': ?1, 'MerchantOfferImage.ImageType': ?2, 'MerchantOfferImage.AvailableInChannel': ?3}")
	MarketplaceImage findByImageCategoryAndDomainIdAndImageTypeAndAvailableInChannel(String imageCategory, String domainId, String imageType, String availableInChannel);

	@Query(value = "{'ImageCategory': ?0, 'MerchantOfferImage.DomainId': ?1}")
	List<MarketplaceImage> findByImageCategoryAndDomainId(String imageCategory, String domainId);
	
	MarketplaceImage findFirst1ByImageCategoryOrderByGiftingImage_ImageIdDesc(String imageCategory);
	
	MarketplaceImage findByImageCategoryAndOriginalFileName(String imageCategory, String originalFileName);
	
	List<MarketplaceImage> findByImageCategoryAndStatus(String imageCategory, String status);
	
	List<MarketplaceImage> findByImageCategory(String imageCategory);
	
	@Query(value = "{'GiftingImage.ImageId': ?0}")
	MarketplaceImage findByImageId(Integer imageId);
	
	@Query(value = "{'imageCategory': ?0, 'merchantOfferImage.domainId': {$in : ?1}}")
	List<MarketplaceImage> findByImageCategoryAndDomainIdList(String imageCategory, List<String> domainId);
	
	@Query(value = "{'imageCategory': ?0, 'merchantOfferImage.domainId': {$in : ?1}}, 'programCode': {$regex:?2,$options:'i'}")
	List<MarketplaceImage> findByImageCategoryAndDomainIdListAndProgramCodeIgnoreCase(String imageCategory, List<String> domainId, String program);
	
	@Query(value = "{'MerchantOfferImage.DomainId': ?0, 'MerchantOfferImage.AvailableInChannel': ?1, 'MerchantOfferImage.DomainName': ?2, 'MerchantOfferImage.ImageType': ?3}")
	MarketplaceImage findByDomainIdAndDomainNameAndChannelAndImageType(String domainId, String channel, String domainName, String imageType);
	
	@Query(value = "{'OriginalFileName': ?0, 'MerchantOfferImage.AvailableInChannel': ?1, 'MerchantOfferImage.DomainName': ?2, 'MerchantOfferImage.ImageType': ?3, 'ImageCategory': ?4}")
	MarketplaceImage findByOriginalFileNameAndDomainNameAndChannelAndImageTypeAndImageCategory(String originalFileName, String channel, String domainName, String imageType, String imageCategory);
	
	
	@Query(value = "{'id' : $0}", delete = true)
	void deleteById(String id);
	
}
