package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersDBConstants;
import com.loyalty.marketplace.offers.outbound.database.repository.EligibleOfferRepository;
import com.loyalty.marketplace.utils.Logs;

import lombok.ToString;

@ToString
@Component
@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EligibleOfferList {
	
	private static final Logger LOG = LoggerFactory.getLogger(EligibleOfferList.class);
	
	@Autowired
	EligibleOfferRepository eligibleOfferRepository;
		
	private static EligibleOfferList singleInstance = null;
	private List<EligibleOffers> eligibleOffersList;
	
	private EligibleOfferList() {
		
	  	
	}
	
	public static EligibleOfferList getList(){
		
		 if (singleInstance == null) 
			 singleInstance = new EligibleOfferList(); 
	  
	     return singleInstance; 
		
	}
	
    public void setOfferList(){
		
       LOG.info(OfferConstants.SINGELTON_SETTER_FROM_DB_LOG_START.get());	
       String log = Logs.logAfterHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
 	   LOG.info(log);	
	   eligibleOffersList =  eligibleOfferRepository.findAll();
	   log = Logs.logAfterHittingDB(OffersDBConstants.ELIGIBLE_OFFERS);
	   LOG.info(log);
	   LOG.info(OfferConstants.SINGELTON_SETTER_FROM_DB_LOG_END.get());
	}
    
    public void setOfferListWithValues(List<EligibleOffers> offerList){
		
    	LOG.info(OfferConstants.SINGELTON_SETTER_AFTER_CHANGE_LOG_START.get());
    	eligibleOffersList =  offerList;
    	LOG.info(OfferConstants.SINGELTON_SETTER_AFTER_CHANGE_LOG_END.get());
    }
	
	public List<EligibleOffers> getOfferList(){
		
		return eligibleOffersList;
	}
	
	
	
	
	

}
