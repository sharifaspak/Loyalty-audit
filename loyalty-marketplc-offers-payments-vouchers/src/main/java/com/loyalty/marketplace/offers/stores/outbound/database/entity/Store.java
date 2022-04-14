package com.loyalty.marketplace.offers.stores.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

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
@Document(collection = OffersDBConstants.STORE)
public class Store {

	@Id
	private String id;

	@Field("ProgramCode")
	private String programCode;

	@Indexed
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

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;

	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

}
