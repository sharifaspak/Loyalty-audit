package  com.loyalty.marketplace.outbound.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.constants.MarketPlaceConstants;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.constants.UserConstants;
import com.loyalty.marketplace.merchants.domain.model.MerchantContactPersonDomain;
import com.loyalty.marketplace.outbound.dto.Result;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.outbound.service.dto.UserManagementResponse;
import com.loyalty.marketplace.stores.domain.model.StoreContactPersonDomain;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;

@Service
@RefreshScope
public class UserManagementService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserManagementService.class);
	
	@Value("${userManagement.uri}")
    private String userUri;
	@Value("${userManagement.saveUser.path}")
	private String userPath;
	@Value("${userManagement.saveUser.realm}")
	private String realm;
	@Value("${userManagement.saveUser.program.realm}")
	private String programRealm;
	@Value("${userManagement.saveMerchant.role}")
	private String merchantRole;
	@Value("${userManagement.saveStore.role}")
	private String StoreRole;
	
	@Value("${userManagement.getAuthenticationToken.keycloak.uri}")
	private String keycloakUri;
	@Value("${userManagement.getAuthenticationToken.keycloak.username}")
	private String keycloakUsername;
	@Value("${userManagement.getAuthenticationToken.credential}")
	private String keycloakCredential;
	@Value("${userManagement.getAuthenticationToken.grant_type}")
	private String grantType;
	@Value("${userManagement.getAuthenticationToken.client_id}")
	private String clientId;
	@Value("${userManagement.getAuthenticationToken.client_secret}")
	private String clientSecret;
	
	@Value("${userManagement.getProgramCode}")
	private String newProgramCode;

	@Autowired
	private RestTemplate restTemplate;
	
	public String getAuthenticationToken(ResultResponse resultResponse) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();

try {
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		map.add(UserConstants.KEYCLOAK_AUTH_USERNAME.get(), keycloakUsername);
		map.add(UserConstants.KEYCLOAK_AUTH_PASSWORD.get(), keycloakCredential);
		map.add(UserConstants.KEYCLOAK_AUTH_GRANT_TYPE.get(), grantType);
		map.add(UserConstants.KEYCLOAK_AUTH_CLIENT_ID.get(), clientId);
		map.add(UserConstants.KEYCLOAK_AUTH_CLIENT_SECRET.get(),clientSecret);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response=null;
		response = restTemplate.postForEntity( keycloakUri, request , String.class );
		
		if (response != null && response.getStatusCodeValue()==200 && 
				response.getBody() != null) {
			Map<String, String> responseMap=Utils.jsonToMap(response.getBody());
			
			if (responseMap!=null && responseMap.get(UserConstants.KEYCLOAK_AUTH_ACCESS_TOKEN.get())!=null) {
				return responseMap.get(UserConstants.KEYCLOAK_AUTH_ACCESS_TOKEN.get()).trim();
			}
		}
     }
		catch(RestClientException rce) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.TOKEN_EXCPETION.getIntId(), 
					MarketPlaceCode.TOKEN_EXCPETION.getMsg()+ MarketPlaceConstants.EXCEPTION.get() + rce.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),MarketPlaceConstants.GET_AUTHENTICATION_TOKEN.get() ,
					rce.getClass() + rce.getMessage(), MarketPlaceCode.TOKEN_EXCPETION)
							.printMessage());
		} catch(NullPointerException npe) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.TOKEN_EXCPETION.getIntId(), 
					MarketPlaceCode.TOKEN_EXCPETION.getMsg()+MarketPlaceConstants.EXCEPTION.get() + npe.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.GET_AUTHENTICATION_TOKEN.get(),
					npe.getClass() + npe.getMessage(), MarketPlaceCode.TOKEN_EXCPETION)
							.printMessage());
		}
		catch(Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.TOKEN_EXCPETION.getIntId(), 
					MarketPlaceCode.TOKEN_EXCPETION.getMsg()+MarketPlaceConstants.EXCEPTION.get()+ e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.GET_AUTHENTICATION_TOKEN.get(),
					e.getClass() + e.getMessage(), MarketPlaceCode.TOKEN_EXCPETION)
							.printMessage());
		}

		return null;
		
	}
	
	public ResultResponse saveMerchant(List<MerchantContactPersonDomain> contactPersonDomainToSave, boolean OptInOrOut,
			ResultResponse resultResponse, String token, String program) {
		List<Map<String, String>> userDto = new ArrayList<Map<String, String>>();
		for (MerchantContactPersonDomain contactPerson : contactPersonDomainToSave) {
			Map<String, String> userMap=new HashMap<String, String>();
			userMap.put(UserConstants.KEYCLOAK_USERNAME.get(), contactPerson.getUserName());
			userMap.put(UserConstants.KEYCLOAK_USERSECRET.get(),contactPerson.getPassword());
			userMap.put(UserConstants.KEYCLOAK_FNAME.get(),contactPerson.getFirstName());
			userMap.put(UserConstants.KEYCLOAK_LNAME.get(),contactPerson.getLastName());
			userMap.put(UserConstants.KEYCLOAK_EMAIL.get(),contactPerson.getEmailId());
			userMap.put(UserConstants.KEYCLOAK_OPTINOUT_FLAG.get(),OptInOrOut+"");
			
			if(null != program && program.equals(newProgramCode)) {
				userMap.put(UserConstants.KEYCLOAK_REALM.get(), programRealm);
			} else {
				userMap.put(UserConstants.KEYCLOAK_REALM.get(), realm);
			}
			
			userMap.put(UserConstants.KEYCLOAK_USER_ROLE.get(), merchantRole);
			userDto.add(userMap);
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(RequestMappingConstants.TOKEN, token);
			headers.set(RequestMappingConstants.PROGRAM, program);
			HttpEntity<Object> userRequest=new HttpEntity<>(userDto, headers);
			ResponseEntity<String> res = restTemplate.exchange(userUri + userPath, 
					HttpMethod.POST, 
					userRequest, 
					String.class);
			String resBody=res.getBody();
			ObjectMapper mapper = new ObjectMapper();
			UserManagementResponse response =mapper.readValue(resBody, UserManagementResponse.class);
			if(response.getApiStatus().getErrors().isEmpty()) {
			resultResponse.setResult(resultResponse.getResult().getResponse(),resultResponse.getResult().getDescription() 
					+ " and "+MarketPlaceCode.USER_CREATED_SUCCESSFULLY.getMsg() );
			}
			else {
				resultResponse.setBulkErrorAPIResponse(response.getApiStatus().getErrors());
				resultResponse.setResult(resultResponse.getResult().getResponse(),resultResponse.getResult().getDescription()+" but one or more Users creation failed");
			}
		} catch(RestClientException rce) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+ MarketPlaceConstants.EXCEPTION.get() + rce.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),MarketPlaceConstants.SAVE_MERCHANT.get() ,
					rce.getClass() + rce.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		} catch(NullPointerException npe) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+MarketPlaceConstants.EXCEPTION.get() + npe.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.SAVE_MERCHANT.get(),
					npe.getClass() + npe.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		}
		catch(Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+MarketPlaceConstants.EXCEPTION.get()+ e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.SAVE_MERCHANT.get(),
					e.getClass() + e.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		}
		
		return resultResponse;
		
	}
	
	
	public ResultResponse updateMerchantPassword(MerchantContactPersonDomain contactPersonDomainToSave,
			boolean OptInOrOut, ResultResponse resultResponse, String token) {
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put(UserConstants.KEYCLOAK_USER_NAME.get(), contactPersonDomainToSave.getUserName());
		userMap.put(UserConstants.KEYCLOAK_PASSWORD.get(), contactPersonDomainToSave.getPassword());
		List<Map<String,String>> userList = new ArrayList<Map<String,String>>();
		userList.add(userMap);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(RequestMappingConstants.TOKEN, token);
			LOG.info("headers :: {}",headers);
			HttpEntity<Object> userRequest = new HttpEntity<>(userMap, headers);
			LOG.info("userRequest :: {}",userRequest);
			String finalUrl = userUri + "/keycloak/resetUser";
			LOG.info("finalUrl :: {}",finalUrl);
			ResponseEntity<String> res = restTemplate.exchange(finalUrl, HttpMethod.POST, userRequest, String.class);
			String resBody = res.getBody();
			LOG.info("resBody :: {}",resBody);
			ObjectMapper mapper = new ObjectMapper();
			Result result = new Result();
			result.setDescription("user password updated successfully");
			//UserManagementResponse response = mapper.readValue(resBody, UserManagementResponse.class);
			if (resBody.equals("NO_CONTENT")) {
				resultResponse.setResult(resultResponse.getResult().getResponse(),
						resultResponse.getResult().getDescription() + " and user password updated successfully");
				resultResponse.setResult(result);
			} else {
				
				resultResponse.setResult(resultResponse.getResult().getResponse(),
						resultResponse.getResult().getDescription() + " but user password updation failed");
			}
		} catch (RestClientException rce) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getIntId(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getMsg() + MarketPlaceConstants.EXCEPTION.get()
							+ rce.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceConstants.UPDATE_MERCHANT_PASSWORD.get(), rce.getClass() + rce.getMessage(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION).printMessage());
		} catch (NullPointerException npe) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getIntId(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getMsg() + MarketPlaceConstants.EXCEPTION.get()
							+ npe.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceConstants.UPDATE_MERCHANT_PASSWORD.get(), npe.getClass() + npe.getMessage(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION).printMessage());
		} catch (Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getIntId(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION.getMsg() + MarketPlaceConstants.EXCEPTION.get()
							+ e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),
					MarketPlaceConstants.UPDATE_MERCHANT_PASSWORD.get(), e.getClass() + e.getMessage(),
					MarketPlaceCode.USER_PASSWORD_UPDATE_EXCEPTION).printMessage());
		}

		return resultResponse;

	}



	public ResultResponse saveStore(List<StoreContactPersonDomain> contactPersonDomainToSave, boolean OptInOrOut,
			ResultResponse resultResponse, String token, String program) {
		List<Map<String, String>> userDto = new ArrayList<Map<String, String>>();

		for(StoreContactPersonDomain contactPerson : contactPersonDomainToSave) {
			Map<String, String> userMap=new HashMap<String, String>();
			userMap.put(UserConstants.KEYCLOAK_USERNAME.get(), contactPerson.getUserName());
			userMap.put(UserConstants.KEYCLOAK_USERSECRET.get(),contactPerson.getPassword());
			userMap.put(UserConstants.KEYCLOAK_FNAME.get(),contactPerson.getFirstName());
			userMap.put(UserConstants.KEYCLOAK_LNAME.get(),contactPerson.getLastName());
			userMap.put(UserConstants.KEYCLOAK_EMAIL.get(),contactPerson.getEmailId());
			userMap.put(UserConstants.KEYCLOAK_OPTINOUT_FLAG.get(),OptInOrOut+"");
			
			if(null != program && program.equals(newProgramCode)) {
				userMap.put(UserConstants.KEYCLOAK_REALM.get(), programRealm);
			} else {
				userMap.put(UserConstants.KEYCLOAK_REALM.get(), realm);
			}
			
			//userMap.put(UserConstants.KEYCLOAK_REALM.get(),realm);
			userMap.put(UserConstants.KEYCLOAK_USER_ROLE.get(), StoreRole);
			userMap.put(UserConstants.PIN.get(),String.valueOf(contactPerson.getPin()));
			userDto.add(userMap);
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(RequestMappingConstants.TOKEN, token);
			headers.set(RequestMappingConstants.PROGRAM, program);
			HttpEntity<Object> userRequest=new HttpEntity<>(userDto, headers);
			ResponseEntity<String> res = restTemplate.exchange(userUri + userPath, 
					HttpMethod.POST, 
					userRequest, 
					String.class);
			String resBody=res.getBody();
			ObjectMapper mapper = new ObjectMapper();
			UserManagementResponse response =mapper.readValue(resBody, UserManagementResponse.class);
			if(response.getApiStatus().getErrors().isEmpty()) {
			resultResponse.setResult(resultResponse.getResult().getResponse(),resultResponse.getResult().getDescription() 
					+ " and "+MarketPlaceCode.USER_CREATED_SUCCESSFULLY.getMsg() );
			}
			else {
				resultResponse.setBulkErrorAPIResponse(response.getApiStatus().getErrors());
				resultResponse.setResult(resultResponse.getResult().getResponse(),resultResponse.getResult().getDescription()+" but one or more Users creation failed");
			}
		} catch(RestClientException rce) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+ MarketPlaceConstants.EXCEPTION.get() + rce.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(),MarketPlaceConstants.SAVE_MERCHANT.get() ,
					rce.getClass() + rce.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		} catch(NullPointerException npe) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+MarketPlaceConstants.EXCEPTION.get() + npe.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.SAVE_MERCHANT.get(),
					npe.getClass() + npe.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		}
		catch(Exception e) {
			resultResponse.addErrorAPIResponse(MarketPlaceCode.USER_CREATION_EXCEPTION.getIntId(), 
					MarketPlaceCode.USER_CREATION_EXCEPTION.getMsg()+MarketPlaceConstants.EXCEPTION.get()+ e.getMessage());
			LOG.error(new MarketplaceException(this.getClass().toString(), MarketPlaceConstants.SAVE_MERCHANT.get(),
					e.getClass() + e.getMessage(), MarketPlaceCode.USER_CREATION_EXCEPTION)
							.printMessage());
		}
		
		return resultResponse;
		
	}

	

}
