package com.loyalty.marketplace.offers.member.management.outbound.dto;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tier {
	@Field(value = "TierLevelName")
    private String tierLevelName;
    @Field(value = "TierInArabic")
    private String tierInArabic;
    @Field(value = "TierProgram")
    private String tierProgram;
    @Field(value = "MaximumTierPoints")
    private int maximumPoints;
    @Field(value = "MinimumTierPoints")
    private int minimumPoints;
    @Field(value = "TierExpiryPeriod")
    private int expiryPeriod;
}
