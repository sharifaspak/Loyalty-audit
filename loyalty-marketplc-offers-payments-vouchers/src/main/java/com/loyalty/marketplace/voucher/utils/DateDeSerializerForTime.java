package com.loyalty.marketplace.voucher.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeSerializerForTime extends StdDeserializer<Date> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateDeSerializerForTime() {
		super(Date.class);
	}

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String date = p.readValueAs(String.class);
		Date validDOB = null;
		Pattern pattern = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$");
		if (date != null && !date.isEmpty()) {
			boolean status = pattern.matcher(date).matches();
			if (status) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					sdf.setLenient(false);
					validDOB = sdf.parse(date);
				} catch (ParseException e) {
					return validDOB;
				}
			}
		}
		return validDOB;
	}

}