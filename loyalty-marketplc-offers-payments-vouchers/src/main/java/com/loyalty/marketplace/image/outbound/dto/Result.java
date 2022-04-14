package com.loyalty.marketplace.image.outbound.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Result {

	private String response;

	private String description;
	
	private Object status;

	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Result [response=" + response + ", description=" + description + "]";
	}
	
	
}
