package com.loyalty.marketplace.subscription.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	
	private static final Logger log = LoggerFactory.getLogger(FileUpload.class);
	
	@Autowired
	private RetryTemplate retryTemplate;
	
	
	public String getEmployees()
	{
	    final String uri = "http://10.0.212.77:8081/test";

	    RestTemplate restTemplate = new RestTemplate();
	    return retryTemplate.execute(context -> {
			log.info("inside Retry block using retryTemplate of getEmployees method of class {}", this.getClass().getName());
	    return restTemplate.getForObject(uri, String.class);
	    });

	}
	
	public String uploadImage(MultipartFile file) throws IOException 
	{
	    final String uri = "http://10.0.212.77:8081/upload";
	    
	    MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(convert(file)));
        
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

	    RestTemplate restTemplate = new RestTemplate();
	    return retryTemplate.execute(context -> {
			log.info("inside Retry block using retryTemplate of uploadImage method of class {}", this.getClass().getName());
	    ResponseEntity<String> response = restTemplate.exchange(uri,
                HttpMethod.POST, requestEntity, String.class);
	    
	    return response.getBody();
	    });

	}
	
	public static File convert(MultipartFile file)
	  {    
	    File convFile = new File(file.getOriginalFilename());
	    try {
	        convFile.createNewFile();
	          FileOutputStream fos = new FileOutputStream(convFile); 
	            fos.write(file.getBytes());
	            fos.close(); 
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 

	    return convFile;
	 }
}
