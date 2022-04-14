package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
@NoArgsConstructor
@ToString
@Document(collection = "OfferType")
public class OfferType {

	@Id
	private String offerTypeId;

	@Field("ProgramCode")
	private String programCode;
	
	@Field("Description")
	private OfferTypeDescription offerDescription;
	
	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;
}
