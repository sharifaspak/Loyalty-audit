package com.loyalty.marketplace.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.loyalty.marketplace.offers.constants.OffersDBConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = OffersDBConstants.DENOMINATION)
public class Denomination {
	
	@Id
	private String denominationId;

	@Field("ProgramCode")
	private String programCode;
	
	@Field("Description")
	private DenominationDescription denominationDescription;
	
	@Field("Value")
	private DenominationValue denominationValue;
	
	@Field("CreatedUser")
	private String usrCreated;

	@Field("UpdatedUser")
	private String usrUpdated;

	@Field("CreatedDate")
	private Date dtCreated;

	@Field("UpdatedDate")
	private Date dtUpdated;
	
}
