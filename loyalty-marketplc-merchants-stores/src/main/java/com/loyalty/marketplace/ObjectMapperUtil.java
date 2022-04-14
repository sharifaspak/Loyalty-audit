package com.loyalty.marketplace;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public enum ObjectMapperUtil {

	INSTANCE;
	private final ObjectMapper mapper = new ObjectMapper();

	private final Logger log = LoggerFactory.getLogger(ObjectMapperUtil.class);

	private ObjectMapperUtil() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
	}

	public ObjectMapper getObjectMapper() {
		return mapper;
	}

	public ObjectWriter getObjectWriter() {
		return mapper.writer();

	}

	public ObjectReader getObjectReader(Class<?> clazz) {
		return mapper.readerFor(clazz);
	}

	public ObjectReader getObjectReader() {
		return mapper.reader();
	}

	public String convertToJSon(Object obj) {
		log.info("inside objectToJson method of class ObjectMapperUtil and data {} to convert", obj);
		String result = "";
		try {
			result = getObjectWriter().writeValueAsString(obj);
		} catch (IOException e) {
			log.error("inside objectToJson method of class ObjectMapperUtil and error {} ", e.getLocalizedMessage());
		}
		return result;
	}

	public Object convertToObject(Object result, Class<?> classz) {
		log.info("inside convertToObject method of class ObjectMapperUtil");
		try {
			return mapper.convertValue(result, classz);
		} catch (Exception ex) {
			log.error("inside convertToObject method ObjectMapperUtil  got exception", ex);

		}
		return null;
	}

	public Object convertValue(Object result, TypeReference<?> typeReference) {
		log.info("inside convertToObject method of class ObjectMapperUtil");
		try {
			return mapper.convertValue(result, typeReference);
		} catch (Exception ex) {
			log.error("inside convertToObject method ObjectMapperUtil  got exception", ex);

		}
		return null;
	}
}