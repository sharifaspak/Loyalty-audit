package com.loyalty.marketplace.subscription.domain;


import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.subscription.constants.SubscriptionManagementCode;
import com.loyalty.marketplace.subscription.outbound.database.entity.SubscriptionPayment;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionPaymentRepository;
import com.loyalty.marketplace.subscription.utils.SubscriptionManagementException;
import com.mongodb.MongoWriteException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Component
@Getter
@ToString
public class SubscriptionPaymentDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPaymentDomain.class);
	
	@Getter(AccessLevel.NONE)
	@Autowired
	ModelMapper modelMapper;
	
	@Getter(AccessLevel.NONE)
	@Autowired
	SubscriptionPaymentRepository subscriptionPaymentRepository;
	
	private String id;
	private String subscriptionId;
	private List<PaymentMethodDetailsDomain> paymentMethodDetails;
	private Date createdDate;
	private String createdUser;
	private Date updatedDate;
	private String updatedUser;
		
	public SubscriptionPaymentDomain() {
		
	}
	
	public SubscriptionPaymentDomain(SubscriptionPaymentBuilder subscriptionPaymentBuilder) {
		super();
		this.id = subscriptionPaymentBuilder.id;
		this.subscriptionId = subscriptionPaymentBuilder.subscriptionId;
		this.paymentMethodDetails = subscriptionPaymentBuilder.paymentMethodDetails;
		this.createdDate = subscriptionPaymentBuilder.createdDate;
		this.createdUser = subscriptionPaymentBuilder.createdUser;
		this.updatedDate = subscriptionPaymentBuilder.updatedDate;
		this.updatedUser = subscriptionPaymentBuilder.updatedUser;
		
	}

	
	public static class SubscriptionPaymentBuilder {
		private String id;
		private String subscriptionId;
		private List<PaymentMethodDetailsDomain> paymentMethodDetails;
		private Date createdDate;
		private String createdUser;
		private Date updatedDate;
		private String updatedUser;
		
		
		public SubscriptionPaymentBuilder(String id, String subscriptionId, List<PaymentMethodDetailsDomain> paymentMethodDetails,
				Date createdDate, String createdUser, Date updatedDate, String updatedUser) {
			super();
			this.id = id;
			this.subscriptionId = subscriptionId;
			this.paymentMethodDetails = paymentMethodDetails;
			this.createdDate = createdDate;
			this.createdUser = createdUser;
			this.updatedDate = updatedDate;	
			this.updatedUser = updatedUser;
		}
		
		public SubscriptionPaymentDomain build() {
			return new SubscriptionPaymentDomain(this);
		}
	}
	
	public SubscriptionPayment saveSubscriptionPayment(SubscriptionPaymentDomain subscriptionPaymentDomain) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Persisted: {}", subscriptionPaymentDomain);
		try {
			SubscriptionPayment subscriptionPayment = modelMapper.map(subscriptionPaymentDomain, SubscriptionPayment.class);			
			this.subscriptionPaymentRepository.insert(subscriptionPayment);				
			LOG.info("Persisted Object: {}", subscriptionPayment);
			return subscriptionPayment;
			
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionPayment",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTIONCATALOG_CREATION_FAILED);	
		} catch (Exception e) {		
			LOG.error("Exception occured while saving subscription payment to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "saveSubscriptionPayment",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		
		}
	}
	
	
	public void updateSubscriptionPaymentMethod(SubscriptionPaymentDomain subscriptionPaymentDomain, SubscriptionPayment updateSubscriptionPayment) throws SubscriptionManagementException {
		LOG.info("Domain Object To Be Updated: {}", subscriptionPaymentDomain);
		try {
			this.subscriptionPaymentRepository.save(modelMapper.map(subscriptionPaymentDomain, SubscriptionPayment.class));
			LOG.info("Persisted Object: {}", updateSubscriptionPayment);
		} catch (MongoWriteException mongoException) {
			LOG.error("MongoDB persist exception occured while saving merchant to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionPaymentMethod",
					mongoException.getClass() + mongoException.getMessage(),
					SubscriptionManagementCode.SUBSCRIPTION_PAYMENT_RENEWAL_FAILED);	
		} catch (Exception e) {
			LOG.error("Exception occured while updating subscription payment to database.");
			throw new SubscriptionManagementException(this.getClass().toString(), "updateSubscriptionPaymentMethod",
					e.getClass() + e.getMessage(), SubscriptionManagementCode.GENERIC_RUNTIME_EXCEPTION);
		}
	}
	
	
	
	
}
