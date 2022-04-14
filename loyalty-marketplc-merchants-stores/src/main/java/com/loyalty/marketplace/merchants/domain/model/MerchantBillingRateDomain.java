package com.loyalty.marketplace.merchants.domain.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.merchants.inbound.dto.MerchantBillingRateDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.RateType;
import com.loyalty.marketplace.merchants.outbound.database.repository.RateTypeRepository;
import com.loyalty.marketplace.utils.MarketplaceException;

@Component
public class MerchantBillingRateDomain {
	
	@Autowired
	RateTypeRepository rateTypeRepository;

	private Double rate;

	private Date startDate;

	private Date endDate;

	private String rateType;

	private String currency;

	public Double getRate() {
		return rate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getRateType() {
		return rateType;
	}

	public String getCurrency() {
		return currency;
	}

	public MerchantBillingRateDomain() {
		
	}
	
	public MerchantBillingRateDomain(MerchantBillingRateBuilder merchantBillingRate) {
		super();
		this.rate = merchantBillingRate.rate;
		this.startDate = merchantBillingRate.startDate;
		this.endDate = merchantBillingRate.endDate;
		this.rateType = merchantBillingRate.rateType;
		this.currency = merchantBillingRate.currency;
	}

	public static class MerchantBillingRateBuilder {

		private Double rate;

		private Date startDate; 

		private Date endDate;

		private String rateType;

		private String currency;

		public MerchantBillingRateBuilder(Double rate, Date startDate, Date endDate, String rateType,
				String currency) {
			super();
			this.rate = rate;
			this.startDate = startDate;
			this.endDate = endDate;
			this.rateType = rateType;
			this.currency = currency;
		}

		public MerchantBillingRateDomain build() {
			return new MerchantBillingRateDomain(this);
		}
	}

	public List<MerchantBillingRateDomain> createBillingRates(List<MerchantBillingRateDto> billingRateDtoList, List<MerchantBillingRateDto> invalidBillingRateDtoList) throws ParseException {
		
		List<MerchantBillingRateDomain> billingRateList = new ArrayList<>();
		
		for(MerchantBillingRateDto billingRate: billingRateDtoList) {
			
			Date billingRateStartDate = (!StringUtils.isEmpty(billingRate.getStartDate())) ? new SimpleDateFormat("dd-MM-yyyy").parse(billingRate.getStartDate()) : null;
			Date billingRateEndDate = (!StringUtils.isEmpty(billingRate.getEndDate())) ? new SimpleDateFormat("dd-MM-yyyy").parse(billingRate.getEndDate()) : null; 
			
			boolean addToBillingRate = true;
			
			for (final MerchantBillingRateDomain billingRateDto : billingRateList) {
				
				if(null != billingRate.getRateType() && billingRate.getRateType().equals(billingRateDto.getRateType())){ //&& (billingRate.getRate().equals(billingRateDto.getRate()))
					addToBillingRate = false;
				}

	 		} 
			if(null != billingRate.getRateType() && addToBillingRate) {
			billingRateList.add(new MerchantBillingRateDomain.MerchantBillingRateBuilder(billingRate.getRate(), billingRateStartDate, billingRateEndDate,
					billingRate.getRateType(), billingRate.getCurrency()).build());
			} else if(null != billingRate.getRateType() && !addToBillingRate) {
				invalidBillingRateDtoList.add(billingRate);
			}
		}

		return billingRateList;
	}
	
	public RateType getTypeByRateType(String rateType) throws MarketplaceException {
		try {
			return rateTypeRepository.findByTypeRate(rateType);
		} catch (Exception e){
			throw new MarketplaceException(this.getClass().toString(), "getTypeByRateType",
					e.getClass() + e.getMessage(), MarketPlaceCode.TYPE_UNAVAILABLE_FOR_BILLING_RATE);
					
		}
		

	}

}
