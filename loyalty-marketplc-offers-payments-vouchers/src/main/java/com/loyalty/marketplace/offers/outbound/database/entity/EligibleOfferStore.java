package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreAddress;
import com.loyalty.marketplace.offers.stores.outbound.database.entity.StoreDescription;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author jaya.shukla
 *
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class EligibleOfferStore {

	@Field("Id")
	private String id;
	
	@Field("Code")
	private String storeCode;

	@Field("Description")
	private StoreDescription description;

	@Field("Address")
	private StoreAddress address;

	@Field("CoordinatesLatitude")
	private String latitude;

	@Field("CoordinatesLongitude")
	private String longitude;

	@Field("MerchantCode")
	private String merchantCode;

	@Field("ContactPersons")
	private List<ContactPerson> contactPersons;

	@Field("Status")
	private String status;

}
