package com.loyalty.marketplace;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.constants.UserConstants;
import com.loyalty.marketplace.utils.AppAesUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Component
@RefreshScope
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebFilter implements Filter {
	
	@Value("${jwt.key}")
    private String key;
	
	@Value("${wso2.claims.url}")
    private String claimsUrl;
	
	@Value("${wso2.userlist}") 
	private List<String> userlist;
	
	@Value("${wso2.applicationRole}") 
	private List<String> applicationRole;
	
	@Value("${aes.jwt.key}") 
	private String encodedKey;
	
	@Value("${wso2.tibco.username}") 
	private String tibcoUsername;
	
	@Value("${wso2.tibco.urlsToBypassToken}") 
	private List<String> urlsToBypass;
	
	
	private static final Logger logger = LoggerFactory.getLogger(WebFilter.class);

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwtToken = null;
		HttpServletRequest req = (HttpServletRequest) request;
		
		if(!StringUtils.isEmpty(req.getParameter("live"))){
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_OK,
			UserConstants.UNATHOURIZED_USER.get());
			return;
		}

		if(!StringUtils.isEmpty(req.getParameter("testSystem"))){                          
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,                     
					UserConstants.UNATHOURIZED_USER.get());             
			return;                 
		} 
		
		String xtoken = req.getHeader(UserConstants.JWT_ASSERTION.get());
		logger.info("WSO2 xtoken request parameters: {}", xtoken);
		logger.info("userName from header {} ",req.getHeader(RequestMappingConstants.USER_NAME));
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		jwtToken = req.getHeader(RequestMappingConstants.TOKEN);
		
		//check for if both xtoken and token is null
		if (StringUtils.isEmpty(xtoken) && StringUtils.isEmpty(jwtToken)) {
			logger.info("Unauthorized user : both xtoken and jwt token are null");
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
					UserConstants.UNATHOURIZED_USER.get());
			return;
		}
		if (StringUtils.isEmpty(requestWrapper.getHeader(RequestMappingConstants.EXTERNAL_TRANSACTION_ID))) {
			logger.info("externalTransactionId is null");
			requestWrapper.addHeader(RequestMappingConstants.EXTERNAL_TRANSACTION_ID, "Loyalty_"+new Date().getTime());
		}
		 
		//check whether the token passed by the internal service calls is either jwtToken or xtoken
		if ((StringUtils.isEmpty(xtoken)) && (!StringUtils.isEmpty(jwtToken))) {
			try {
				JSONObject jsonToken = new JSONObject(new String(Base64.decodeBase64(jwtToken.split("[.]")[0])));
				if (!((jsonToken.has(UserConstants.APPLICATION.get()))
						&& jsonToken.getString(UserConstants.APPLICATION.get())
								.equalsIgnoreCase(UserConstants.LOYALTY.get()))) {
					xtoken = jwtToken;
					jwtToken =null; 
				}
			} catch (JSONException e) {
				logger.info("Excpetion while parsing the token from the header:{}", e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, UserConstants.UNATHOURIZED_USER.get());
				return;
			}
		}
		
		
        //Extracting the userName, userRole, apicontext from xToken and validating the application users
		if (!StringUtils.isEmpty(xtoken)) {

			try {
				JSONObject jsonToken = new JSONObject(new String(Base64.decodeBase64(xtoken.split("[.]")[1])));
				logger.info("JWT header value: {}", jsonToken);
				
				if (!StringUtils.isEmpty(jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()))) {
					logger.info("Username from xtoken {}",jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()));
					if(null==req.getHeader(RequestMappingConstants.USER_NAME) || !checkUserNameExists(req.getHeader(RequestMappingConstants.USER_NAME),jsonToken.getString(claimsUrl + UserConstants.USERNAME.get())) ) {
					requestWrapper.addHeader(RequestMappingConstants.USER_NAME,
							jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()));
					}
					requestWrapper.addHeader(RequestMappingConstants.SYSTEM_ID, jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()));

				}
				if (!StringUtils.isEmpty(jsonToken.getString(claimsUrl + UserConstants.ROLE.get()))) {
					logger.info("Role {}",jsonToken.getString(claimsUrl + UserConstants.ROLE.get()));
					requestWrapper.addHeader(RequestMappingConstants.USER_ROLE,
							jsonToken.getString(claimsUrl + UserConstants.ROLE.get()));
				}
				//Enforce the tibco (except for /memeber and / rollback  api)and SmilesAPP to pass the jwttoken
				//check the username  is not empty 
				//check if the user and role contains in the configured properties
				//then bypass the user 
				if (StringUtils.isEmpty(jwtToken)) {
					requestWrapper.addHeader(RequestMappingConstants.TOKEN, xtoken);
					if (!StringUtils.isEmpty(jsonToken.getString(claimsUrl + UserConstants.USERNAME.get())) 
							&& checkRoleExists(requestWrapper.getHeader(RequestMappingConstants.USER_ROLE), applicationRole)
							&&  !userlist.contains(jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()))) 
					{
						if (jsonToken.getString(claimsUrl + UserConstants.USERNAME.get())
								.equalsIgnoreCase(tibcoUsername)
								&& (urlsToBypass.contains(Arrays.asList(requestWrapper.getServletPath().split("/")).get(Arrays.asList(requestWrapper.getServletPath().split("/")).size()-1))
										||urlsToBypass.contains(Arrays.asList(requestWrapper.getServletPath().split("/")).get(Arrays.asList(requestWrapper.getServletPath().split("/")).size()-2)))) {
							logger.info("Valid User {} ",
									jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()));

						} else {
							logger.info("Unauthorized user : token is mandatory {}",
									jsonToken.getString(claimsUrl + UserConstants.USERNAME.get()));
							((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
									UserConstants.UNATHOURIZED_USER.get());
							return;
						}
					}
					
				}
			}
				

           catch (Exception e) {
				logger.error(e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
						UserConstants.UNATHOURIZED_USER.get());
				return;
			}
		}

		//Validating the jwtToken , extracting the userName from jwtToken and adding to the header
		if (!StringUtils.isEmpty(jwtToken)) {

			try {
				byte[] decodedKey = java.util.Base64.getDecoder().decode(encodedKey);
				SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(AppAesUtil.decrypt(key, new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"))));
				String username = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody()
				.get(RequestMappingConstants.USER_NAME).toString();
				if(null==req.getHeader(RequestMappingConstants.USER_NAME) || !checkUserNameExists(req.getHeader(RequestMappingConstants.USER_NAME),username) ) {
					requestWrapper.addHeader(RequestMappingConstants.USER_NAME,username);
				}
				// OK, we can trust this JWT
				logger.info("trusted token");

			} catch (WeakKeyException e) { 
				logger.error("Invalid key :{}", e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Key");
				return;
			}

			catch (ExpiredJwtException e) {
				logger.error("Token expired :{}", e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Token got expired");
				return;
			} catch (MalformedJwtException e) {
				logger.error("Invalid token :{}", e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
				return;
			} catch (Exception e) {
				logger.error("excpetion while parsing the  token:{}", e.getMessage());
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
						"Issue while parsing the token");
				return;
			}
		}
		// Goes to default servlet
		
            chain.doFilter(requestWrapper, response);

	}

	private boolean checkUserNameExists(String headerUserName, String username) {
		try {
			String[] headersList = headerUserName.split(",");
			for (String header : headersList) {
				if (header.equalsIgnoreCase(username)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error("Error while checkUserNameExists {}", e.getMessage());
		}
		return false;
	}
	public boolean checkRoleExists(String wsoRole, List<String> roles){
		try {

			if (StringUtils.isEmpty(wsoRole)) {
				return false;
			}
			String test = wsoRole.replaceAll("\\\\", "");
			test = test.replaceAll("\"", "");
			test = test.substring(1, test.length() - 1);
			String[] tokenRoles = test.split(",");
			for (String role : roles) {
				for (String tokenRole : tokenRoles) {
					if (tokenRole.trim().equals(role)) {
						logger.info("Role exists{}", role);
						return true;
					}
				}
			}

		} catch (Exception e)

		{
			logger.error("Error while extracting the role {}",e.getMessage());
		} // Goes to default servlet
		return false;

	}
	public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
		/**
		 * construct a wrapper for this request
		 *
		 * @param request
		 */
		public HeaderMapRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		private Map<String, String> headerMap = new HashMap<>();

		/**
		 * add a header with given name and value
		 *
		 * @param name
		 * @param value
		 */
		public void addHeader(String name, String value) {
			headerMap.put(name, value);
		}

		@Override
		public String getHeader(String name) {
			String headerValue = super.getHeader(name);
			if (headerMap.containsKey(name)) {
				headerValue = headerMap.get(name);
			}
			return headerValue;
		}

		/**
		 * get the Header names
		 */
		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			for (String name : headerMap.keySet()) {
				names.add(name);
			}
			return Collections.enumeration(names);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> values = Collections.list(super.getHeaders(name));
			if (headerMap.containsKey(name)) {
				values.add(headerMap.get(name));
			}
			return Collections.enumeration(values);
		}
	}

	@Configuration
	public class MyConfig implements WebMvcConfigurer {
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new HttpInterceptor());
		}

		@Component
		public class HttpInterceptor extends HandlerInterceptorAdapter {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
				if(StringUtils.isEmpty(request.getParameter("testSystem")) ||StringUtils.isEmpty(request.getParameter("live"))) {
					logger.debug("userName request parameters: {}", request.getHeader(RequestMappingConstants.USER_NAME));
					logger.debug("externalTransactionId request parameters: {}", request.getHeader(RequestMappingConstants.EXTERNAL_TRANSACTION_ID));
				}
				return true;
			}
		}
	}
}


