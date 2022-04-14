package com.loyalty.marketplace.offers.helper.dto;

import com.loyalty.marketplace.offers.constants.OfferErrorCodes;
import com.loyalty.marketplace.offers.constants.OfferExceptionCodes;
import com.loyalty.marketplace.utils.MarketplaceException;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ExceptionInfo {
	
	private String currentClassName;
	private String currentMethodName;
	private Exception exception;
	private MarketplaceException marketplaceException;
	private OfferErrorCodes errorResult;
	private OfferExceptionCodes exceptionError;
	private OfferErrorCodes marketplaceError;
	private String exceptionLogId;
	
}
