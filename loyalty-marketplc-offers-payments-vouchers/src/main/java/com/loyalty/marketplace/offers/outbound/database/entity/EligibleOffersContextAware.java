package com.loyalty.marketplace.offers.outbound.database.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;
import com.loyalty.marketplace.offers.helper.OffersHelper;
import com.loyalty.marketplace.offers.helper.RepositoryHelper;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.outbound.dto.EligibleOffersResultResponse;
import com.loyalty.marketplace.offers.utils.Checks;
import com.loyalty.marketplace.utils.Logs;

import lombok.Data;

@Data
@Component
public class EligibleOffersContextAware {
	
	@Autowired
	private ServletContext servletContext;
		
	@Autowired
	private OffersHelper offersHelper;
	
	@Autowired
	RepositoryHelper repositoryHelper;
	
	@Value(OffersConfigurationConstants.IS_BATCH_TOGGLE)
	private boolean batchToggle;
	
	private List<EligibleOffers> eligibleOffersList;
	
	private static final Logger LOG = LoggerFactory.getLogger(EligibleOffersContextAware.class);
	 
	/**
	 * Populates the recent value of eligible offers from db to servlet context on application
	 * startup
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void populateEligibleOffersList() { 
		
		if(!batchToggle) {
			
			String log = Logs.logForConstantStatment(OfferConstants.ELIGIBLE_OFFERS_POPULATING_AT_STARTUP.get());
			LOG.info(log);
			
			EligibleOffersResultResponse eligibleOffersResultResponse = offersHelper.getAndSaveEligibleOffersSync(new Headers());
			
			if(!CollectionUtils.isEmpty(eligibleOffersResultResponse.getEligibleOfferList())) {
				eligibleOffersList = new ArrayList<>(eligibleOffersResultResponse.getEligibleOfferList().size());
				eligibleOffersList.addAll(eligibleOffersResultResponse.getEligibleOfferList());
				servletContext.setAttribute(OfferConstants.ELIGIBLE_OFFER_LIST_CONTEXT.get(), eligibleOffersList);
			}
			
			log = Logs.logForConstantStatment(OfferConstants.ELIGIBLE_OFFERS_POPULATED_AT_STARTUP.get());
			LOG.info(log);
			
		}
		
	}
	
	/**
	 * 
	 * @return Eligible offer list from servlet context/DB
	 */
	@SuppressWarnings(OffersConfigurationConstants.UNCHECKED)
	public List<EligibleOffers> fetchEligibleOfferList() {
		
		String log = Logs.logForConstantStatment(OfferConstants.FETCH_ELIGIBLE_OFFERS_FROM_SERVLET_CONTEXT.get()); 
		LOG.info(log);
		List<EligibleOffers> offersList = (List<EligibleOffers>)servletContext.getAttribute(OfferConstants.ELIGIBLE_OFFER_LIST_CONTEXT.get());
		
		Date contextUpdatedDate = !CollectionUtils.isEmpty(offersList)
				? offersList.get(0).getLastUpdateDate()
				: null;
		Date dbUpdatedDate = repositoryHelper.getLatestUpdateForEligibleOffers();
		
		log = Logs.logForTwoVariables(OfferConstants.CONTEXT_UPDATE_DATE_VARIABLE.get(), 
				contextUpdatedDate, OfferConstants.DB_UPDATE_DATE_VARIABLE.get(), dbUpdatedDate);
		LOG.info(log);
		
		boolean dbFetchRequired = ObjectUtils.isEmpty(contextUpdatedDate) 
				|| Checks.checkDateFirstGreaterThanSecond(dbUpdatedDate, contextUpdatedDate);
		
		if(dbFetchRequired) {
			
			log = Logs.logForConstantStatment(OfferConstants.FETCH_ELIGIBLE_OFFERS_FROM_DB.get()); 
			LOG.info(log);
			servletContext.setAttribute(OfferConstants.ELIGIBLE_OFFER_LIST_CONTEXT.get(), repositoryHelper.findAllEligibleOffers());
			offersList = (List<EligibleOffers>) servletContext.getAttribute(OfferConstants.ELIGIBLE_OFFER_LIST_CONTEXT.get());
			
		}
		
		log = Logs.logForConstantStatment(OfferConstants.RETURNING_ELIGIBLE_OFFERS_FROM_SERVLET_CONTEXT.get()); 
		LOG.info(log);
		return offersList;
	}
	
	/**
	 * Sets the value of input list to servlet context for eligible offers
	 * @param eligibleOffersList
	 */
	public void addEligibleOfferListToContext(List<EligibleOffers> eligibleOffersList) {
		
		String log = Logs.logForConstantStatment(OfferConstants.SETTING_ELIGIBLE_OFFERS_TO_SERVLET_CONTEXT_START.get());
		LOG.info(log);
		
		servletContext.setAttribute(OfferConstants.ELIGIBLE_OFFER_LIST_CONTEXT.get(), eligibleOffersList);
		
		log = Logs.logForConstantStatment(OfferConstants.SETTING_ELIGIBLE_OFFERS_TO_SERVLET_CONTEXT_END.get());
		LOG.info(log);
		
	}
	
}
