package com.loyalty.marketplace.outbound.dto;

public class Result {

	private String response;

	private String description;

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
