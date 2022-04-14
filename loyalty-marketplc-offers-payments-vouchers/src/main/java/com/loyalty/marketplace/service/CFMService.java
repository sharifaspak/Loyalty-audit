package com.loyalty.marketplace.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.loyalty.marketplace.constants.CFMConstants;
import com.loyalty.marketplace.equivalentpoints.domain.EquivalentPointsDomain;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.utils.Utilities;
import com.loyalty.marketplace.payment.constants.MarketplaceConstants;
import com.loyalty.marketplace.payment.utils.Utils;
import com.loyalty.marketplace.service.dto.CFMRequestDto;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.ServiceCallLogsDto;
import com.responsetek.rt.EnqueueInvites;
import com.responsetek.rt.EnqueueInvitesResponse;
import com.responsetek.rt.RTConsumer;
import com.responsetek.rt.RTCustom;
import com.responsetek.rt.RTInvitation;
import com.responsetek.rt.RTInvite;
import com.responsetek.rt.RTInviteSoap;
import com.responsetek.rt.RTKeyValuePair;
import com.responsetek.rt.RTXmlMessage;

@RefreshScope
@Service
public class CFMService {

	private static final Logger LOG = LoggerFactory.getLogger(CFMService.class);
	
	@Value("${cmf.wsdl.uri}")
	private String cmfUri;
	
	@Value("${cmf.clientId}")
	private String cmfClientId;
	
	@Value("${cmf.configId}")
	private String cmfConfigId;
	
	@Value("${cmf.username}")
	private String cmfUsername;
	
	@Value("${cmf.password}")
	private String cmfPassword;
	
	@Value("${cmf.posting}")
	private String cfmPosting;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	EquivalentPointsDomain equivalentPointsDomain;
	
	public void cfmPosting(CFMRequestDto requestDto, String externalTransactionId, String selectedPaymentItem) throws MarketplaceException {
		boolean isCFMPosting = Boolean.parseBoolean(cfmPosting);
		LOG.info("isCFMPosting {}", isCFMPosting);
		if ( !(MarketplaceConstants.SUBSCRIPTION.getConstant().equalsIgnoreCase(selectedPaymentItem)) && isCFMPosting) {
		LOG.info("inside CFMService.cfmPosting() {}");
		LOG.info("CFMRequestDto :  {}", requestDto);
		URL wsdlUrl = null;
		 try {
			 wsdlUrl = new URL(cmfUri);
	        } catch (MalformedURLException e) {
	        	LOG.info("Cannot load wsdl from : " + cmfUri);
	        }
		

		RTInvite service = new RTInvite(wsdlUrl);
	    RTInviteSoap post = service.getRTInviteSoap();
		BindingProvider provider = (BindingProvider) post;
		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, cmfUri);

		
		EnqueueInvites cmfRequest = new EnqueueInvites(); 
		EnqueueInvitesResponse cmfResponse = new EnqueueInvitesResponse(); // TBD
		
		String emailId = ( null != requestDto.getEmail()) ? requestDto.getEmail() : "";
		String subCategoryName = ( null != requestDto.getSubCatagory()) ? requestDto.getSubCatagory() : "";
		String partnerName = ( null != requestDto.getPartnerName()) ? requestDto.getPartnerName() : "";
		String numberType = (null != requestDto.getNumberType()) ? requestDto.getNumberType() : "";
		Date date = new Date();
		String dateCovertedToGstTimeZone = null;
		
		try {
			date = (Utilities.setGstTimeZone(date, CFMConstants.DATEFORMAT.get()));
            SimpleDateFormat formatter = new SimpleDateFormat(CFMConstants.DATEFORMAT.get());
            dateCovertedToGstTimeZone = formatter.format(date);
            
			LOG.info("dateCovertedToGstTimeZone: {}",dateCovertedToGstTimeZone);
		} catch (ParseException e) {
			LOG.error(
					"inside  CFMService.cfmPosting() of class CFMService date conversion to GST time zone got error {} ",
					e);
		}
		
		RTXmlMessage rtXmlMes = new RTXmlMessage();
		rtXmlMes.setRtClientId(cmfClientId);
		rtXmlMes.setRtConfigId(cmfConfigId);
		rtXmlMes.setUsername(cmfUsername);
		rtXmlMes.setPassword(cmfPassword);
		
	try {
			
		RTInvitation rtInvitation =  new RTInvitation();
		
		rtInvitation.setCollectionPoint(CFMConstants.LIFESTYLEANDENTERTAINMENT.get());
		rtInvitation.setInviteType(CFMConstants.SMS.get());
		rtInvitation.setInviteTstamp(dateCovertedToGstTimeZone);
		
		RTCustom rtCustom = new RTCustom();
		rtCustom.setText3("");
		rtCustom.setText4("");
		rtCustom.setText5("");
		rtCustom.setText6(numberType);
		rtCustom.setOption1(partnerName);
		rtCustom.setOption2(requestDto.getPaymentType());
		rtCustom.setOption3("");
		rtCustom.setOption5("");
		rtCustom.setOption8("");
		
		
		rtInvitation.setCustomFields(rtCustom);
		
		RTConsumer rtConsumer = new RTConsumer();
		rtConsumer.setPhoneNumber(requestDto.getAccountNumber());
		rtConsumer.setEmail(emailId);
		rtConsumer.setFirstname("");
		rtConsumer.setLastname("");
		rtConsumer.setId("");
		
		String customerAge = null;
		if(null != requestDto.getCustomerDOB()) {
			Period period = Period.between((requestDto.getCustomerDOB()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
			customerAge = String.valueOf(period.getYears());
		}
		
		String language = null;
		if(CFMConstants.ENGLISH.get().equalsIgnoreCase(requestDto.getLanguage())){
			language = "EN";
		}
		if(CFMConstants.ARABIC.get().equalsIgnoreCase(requestDto.getLanguage())){
			language = "AR";
		}
		
		RTCustom rtCustom2 = new RTCustom();
		rtCustom2.setText1(customerAge);
		rtCustom2.setText2("");
		rtCustom2.setText3(requestDto.getDiscount());
		rtCustom2.setText4("");
		rtCustom2.setText6("");
		rtCustom2.setText7(language); 
		rtCustom2.setOption1(CFMConstants.SMS.get());
		rtCustom2.setOption2(requestDto.getNationality());
		rtCustom2.setOption3("");
		rtCustom2.setOption4(CFMConstants.ETISALATOFFERS.get());
		rtCustom2.setOption5(CFMConstants.MOBILEAPP.get());
		rtCustom2.setOption6(subCategoryName);
		rtCustom2.setOption7("");
		
		rtConsumer.setCustom(rtCustom2);
		rtInvitation.setConsumer(rtConsumer);
		
		rtXmlMes.getInvitation().add(rtInvitation);
		
		cmfRequest.setMessage(rtXmlMes);
		
		LOG.info("-----------CFM Posting Request Start--------------");
		LOG.debug(Utils.toXML(cmfRequest));
		LOG.info("-----------CFM Posting Request End--------------");

		ServiceCallLogsDto serviceCallLogsDto=new ServiceCallLogsDto();	
		String response = null;
		long start = System.currentTimeMillis();
			try {
				response = post.enqueueInvites(rtXmlMes);
				LOG.info("CFM Posting Called");
				LOG.info("CFM Posting response: {}", response);
			}
			catch (Exception e) {
				 LOG.error("Exception occured while calling CFM posting : {}", e.getMessage());
				 long end = System.currentTimeMillis();	
					
				 	serviceCallLogsDto.setCreatedDate(new Date());
					serviceCallLogsDto.setAction("CFM");
					serviceCallLogsDto.setTransactionId(externalTransactionId);
					serviceCallLogsDto.setServiceType("Outbound");
					serviceCallLogsDto.setRequest(Utils.toXML(cmfRequest));
					serviceCallLogsDto.setResponse(e.getMessage());
					serviceCallLogsDto.setStatus("Failed");
					
					serviceCallLogsDto.setResponseTime((end-start));
					saveRequestResponse(serviceCallLogsDto);
					LOG.info("CFM posting Failed saved Exception to serviceCallLogs, externalTransactionId : {}", externalTransactionId);
			}
		
			long end = System.currentTimeMillis();	
				
			 	
				serviceCallLogsDto.setCreatedDate(new Date());
				serviceCallLogsDto.setAction("CFM");
				serviceCallLogsDto.setTransactionId(externalTransactionId);
				serviceCallLogsDto.setServiceType("Outbound");
				serviceCallLogsDto.setRequest(Utils.toXML(cmfRequest));
				serviceCallLogsDto.setResponse(response);
				serviceCallLogsDto.setStatus("Success");
				
				serviceCallLogsDto.setResponseTime((end-start));
				saveRequestResponse(serviceCallLogsDto);
				
				LOG.info("CFM posting sucess record saved to serviceCallLogs, externalTransactionId : {}", externalTransactionId);
		}
		catch(Exception e) {
			LOG.info("Exception Occured while cfmPosting()  : {}", e.getMessage());
			e.printStackTrace();
		}
		}
		
	}
	
	private RTKeyValuePair seRTKeyValuePair(
			String name, String value) {
		RTKeyValuePair rtKeyValuePair = new RTKeyValuePair();
		rtKeyValuePair.setKey(name);
		rtKeyValuePair.setValue(value);
		return rtKeyValuePair;
	}
	
	public void saveRequestResponse(ServiceCallLogsDto callLog) {
		try{
			LOG.info("Requestand response saved :" + callLog.toString());
			jmsTemplate.convertAndSend("serviceCallLogQueue", callLog);
		} catch(Exception e){
			LOG.info("JMS Exception Occured");
		}
	}
	
}
