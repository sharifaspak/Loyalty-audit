package com.loyalty.marketplace.equivalentpoints.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:equivalentPointsMessages.properties")
public class ErrorMessages {
	
	@Value("${success.conversion.rate.create}")
	public String conversionRateCreated;

	@Value("${success.conversion.rate.insert}")
	public String conversionRateInserted;
	
	@Value("${error.conversion.rate.present}")
	public String duplicateConversionRate;
	
	@Value("${error.invalid.conversion.rate.partnerCode}")
	public String invalidPartnerCode;
	
	@Value("${error.invalid.conversion.rate.partnerCode.exist}")
	public String noPartnerCode;

	@Value("${error.null.conversion.rate.partnerCode}")
	public String nullPartnerCode;
	
	@Value("${error.invalid.conversion.rate.valuePerPoint}")
	public String invalidValuePerPoint;
	
	@Value("${program.code}")
	public String defaultProgramCode;
	
	@Value("${error.invalid.denomination.range}")
	public String invalidDenominationRange;

	@Value("${error.null.denomination.range}")
	public String nullDenominationRange;
	
	@Value("${error.null.conversion.rate.valuePerPoint}")
	public String nullValuePerPoint;

	@Value("${error.null.conversion.rate.parameters}")
	public String nullParameters;
	
	@Value("${error.invalid.input}")
	public String invalidInput;
	
	@Value("${error.partner.management.uri}")
	public String partnerMangementNotFound;
	
	@Value("${success.conversion.rate.equivalentPoints}")
	public String calculatedEquivalentPoints;
	
	@Value("${error.empty.conversionRate}")
	public String conversionRateEmpty;
	
	@Value("${error.invalid.conversion.rate.operationType}")
	public String invalidOperationType;
	
	@Value("${error.equivalent.points.invalid.coefficientA}")
	public String invalidCoefficientA;
	
	@Value("${error.equivalent.points.invalid.activityCode}")
	public String invalidActivityCode;

	@Value("${error.invalid.conversion.rate.parametersForEquivalentPoints}")
	public String invalidParametersForEquivalentPoints;
	
	@Value("${error.invalid.conversion.rate.partnerActivity}")
	public String invalidPartnerActivity;
	
	@Value("${error.partner.activity.uri}")
	public String memberActivityNotFound;
	
	@Value("${error.invalid.conversion.rate.baseRate}")
	public String invalidBaseRate;

	@Value("${error.invalid.conversion.rate.amount}")
	public String invalidAmount;
	
	@Value("${error.invalid.tier.bonus.customerTierLevel}")
	public String invalidTierLevel;
	
	@Value("${error.getmemberdetails.exception}")
	public String exceptionforMemberdetails;
	
	@Value("${error.invalid.conversion.rate.calculationForEquivalentPoints}")
	public String invalidCalculationForEquivalentPoints;
}
