package com.loyalty.marketplace.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	private static final String NUMBER = "0123456789";
	private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";
	private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
	private static final String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
	private static final String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;
	private static final String VALIDATION_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$&%^*&!+=])(?=\\S+$).{8,12}$";
	private static SecureRandom random = new SecureRandom();
		
	private static Random rnd = new Random(System.currentTimeMillis());
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static String removeSpecialChars(String input) {
		if (input != null) {
			input = input.replaceAll("[\\x00-\\x09]", "");
			input = input.replaceAll("[\\x11-\\x19]", "");
			input = input.replaceAll("[<>\\\\={}^]", "");
		}
		return input;
	}
	public static boolean sendMail(String userName, String password, String string) {
		return true;
	}
	
	public static boolean validatePassword(final String password) {
        return Pattern.compile(VALIDATION_PATTERN).matcher(password).matches();
    }
	public static String generatePassword() {

	       String password = null; 
	       int count=0;
	       do  {
	    	   if (count>10) {
	    		   password=null;
	    	   break;
	    	   }
	        	password = generateRandomPassword(12);
	        	count++;
	        	
	        }
	        while(!validatePassword(password));
	        return password;   
	      

	    }
	public static String generateRandomPassword(int length) {
		if (length < 1)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {

			int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
			char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);
			sb.append(rndChar);

		}
		return sb.toString();
	}

	public static String shuffleString(String string) {
		List<String> letters = Arrays.asList(string.split(""));
		Collections.shuffle(letters);
		return letters.stream().collect(Collectors.joining());
	}
	
	public static Integer generateRandomNumber() {
		return 100000 + rnd.nextInt(900000);
	}
	
	public static Integer generateRandomNumber(int length) {
		return ( length * 2000)+ rnd.nextInt(length * 4000);
	}
	
	public static Map<String, String>jsonToMap(String json) throws IOException {
		return mapper.readValue(json, Map.class);      
	}

	public static boolean sendCredentailsToLdap(String userName, String password, String email) {
		return true;
	}

	public static Integer validatePagination(Integer page, Integer limit) {
		
		if(null != page && null == limit) {
			return 1;
		}
		
		if(null != limit && null == page) {
			return 2;
		}
		
		if(null == limit && null == page) {
			return 3;
		}
		
		if(null != limit && null != page) {
			return 4;
		}
		
		return 5;
		
	}
	
}
