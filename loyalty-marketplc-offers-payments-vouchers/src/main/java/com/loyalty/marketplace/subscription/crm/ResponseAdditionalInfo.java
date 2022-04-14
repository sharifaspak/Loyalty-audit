package com.loyalty.marketplace.subscription.crm;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAdditionalInfo implements Serializable {

	private String profileId;
	private String  SUBSCRIPTION_ID;
}