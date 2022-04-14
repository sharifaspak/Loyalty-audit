package com.loyalty.marketplace.offers.promocode.outbound.database.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "PromoCodeRules")
public class PromoRules {
	@Id
	private String id;
	@Field("RuleId")
	private String ruleId;
	@Field("Description")
	private String description;
	@Field("IsWelcomeGift")
	private String isWelcomeGift;
	@Field("CreatedDate")
	private Date dtCreated;
	@Field("CreatedUser")
	private String usrCreated;
	@Field("UpdatedDate")
	private Date dtUpdated;
	@Field("UpdatedUser")
	private String usrUpdated;
}
