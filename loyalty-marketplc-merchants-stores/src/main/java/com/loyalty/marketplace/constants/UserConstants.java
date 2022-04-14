package com.loyalty.marketplace.constants;

public enum UserConstants {

	KEYCLOAK_REALM("realm"),
	KEYCLOAK_USERNAME("username"),
	KEYCLOAK_USERSECRET("usersecret"),
	KEYCLOAK_FNAME("first_name"),
	KEYCLOAK_LNAME("last_name"),
	KEYCLOAK_EMAIL("email"),
	KEYCLOAK_USER_ROLE("role"),
	
	KEYCLOAK_USER_NAME("keycloakUsername"),
	KEYCLOAK_PASSWORD("password"),
	
	KEYCLOAK_OPTINOUT_FLAG("flag"),
	KEYCLOAK_AUTH_USERNAME("username"),
	KEYCLOAK_AUTH_PASSWORD("password"),
	KEYCLOAK_AUTH_GRANT_TYPE("grant_type"),
	KEYCLOAK_AUTH_CLIENT_ID("client_id"),
	KEYCLOAK_AUTH_CLIENT_SECRET("client_secret"),
	KEYCLOAK_AUTH_ACCESS_TOKEN("access_token"),
	
	USERNAME("username"),
	JWT_ASSERTION("X-JWT-Assertion"),
	APPLICATION("application"),
	LOYALTY("loyalty"),
	UNATHOURIZED_USER("Unauthorized user"), 
	ROLE("role"), 
	PIN("pin"),
;
	
	private String constant;
	
	UserConstants(String constant) {
			    this.constant = constant;
			  }
		
		public String get() {
		    return this.constant;
		  }
}
