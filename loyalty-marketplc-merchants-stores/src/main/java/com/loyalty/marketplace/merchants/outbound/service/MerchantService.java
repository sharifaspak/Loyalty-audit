package  com.loyalty.marketplace.merchants.outbound.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jms.JmsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.merchants.constants.MerchantCodes;
import com.loyalty.marketplace.merchants.constants.MerchantConstants;
import com.loyalty.marketplace.merchants.outbound.service.dto.EmailRequestDto;
import com.loyalty.marketplace.outbound.jms.events.EventHandler;
import com.loyalty.marketplace.utils.MarketplaceException;

@Service
@RefreshScope
public class MerchantService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MerchantService.class);
	
	@Value("${partnerManagement.uri}")
    private String uri;
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @Qualifier("getTemplateBean")
    private RestTemplate getRestTemplate;
	
	@Autowired
	private EventHandler eventHandler;

	@Value("${email.queue}")
	private String emailQueue;
	
	@Value("${marketplace.replyQueue}")
	private String replyQueue;
	
	public boolean getPartnerDetails(String partnerCode,String token) {

		String url = uri + "partnerManagement/partner/" + partnerCode;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(RequestMappingConstants.TOKEN, token);
		HttpEntity<Object> request=new HttpEntity<>(headers);
		return getRestTemplate.exchange(url,HttpMethod.GET, request, Boolean.class).getBody();

	}
	
	public void sendEmailMerchantStatusUpdate(String firstName, String merchantCode, String status, String email,
			String externalTransactionId) throws MarketplaceException {

		EmailRequestDto emailDto = new EmailRequestDto();

		emailDto.setEmailId(email);
		emailDto.setTransactionId(externalTransactionId);
		emailDto.setTemplateId(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_TEMPLATE_ID.get());
		emailDto.setNotificationId(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_NOTIFICATION_ID.get());
		emailDto.setNotificationCode(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_NOTIFICATION_CODE.get());
		emailDto.setLanguage(MerchantConstants.NOTIFICATION_LANGUAGE_EN.get());
		
		Map<String, String> additionalParam = new HashMap<>();
		additionalParam.put(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_FIRST_NAME.get(), firstName);
		additionalParam.put(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_MERCHANT_CODE.get(), merchantCode);
		additionalParam.put(MerchantConstants.EMAIL_MERCHANT_STATUS_UPDATE_STATUS.get(), status);
		emailDto.setAdditionalParameters(additionalParam);
		
		LOG.info("Inside sendEmailMerchantStatusUpdate method in MerchantService class, Email Request DTO: {}", emailDto);
		
		try {
			
			eventHandler.publishEmail(emailDto);
		
		} catch (JmsException jme) {
			
			throw new MarketplaceException(this.getClass().toString(), "sendEmailMerchantStatusUpdate",
					jme.getClass() + jme.getMessage(), MerchantCodes.FAILED_TO_SEND_EMAIL_MERCHANT_STATUS_UPDATE);
		
		}

	}	
	
}
