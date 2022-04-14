package com.loyalty.marketplace.subscription.inbound.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.loyalty.marketplace.subscription.outbound.database.entity.Subscription;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class AccountSubscription {

	List<Subscription> subscribedSubscription;
	List<Subscription> parkedSubscription;
}
