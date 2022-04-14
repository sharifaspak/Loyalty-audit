package com.loyalty.marketplace.offers.promocode.outbound.database.entity;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.outbound.database.entity.CustomerType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "PartnerPromoCode")
public class PartnerPromoCode {

	@Id
	private String id;
	@Field("Partner")
	private String partner;
	@Field("PartnerRef")
	private String partnerRef;
	@Field("SubscriptionCatalogId")
	private String subscriptionCatalogId;
	@Field("PromoCodeCount")
	private int promoCodeCount;
	@Field("CreatedDate")
	private Date dtCreated;
	@Field("CreatedUser")
	private String usrCreated;
	@Field("UpdatedDate")
	private Date dtUpdated;
	@Field("UpdatedUser")
	private String usrUpdated;
}
