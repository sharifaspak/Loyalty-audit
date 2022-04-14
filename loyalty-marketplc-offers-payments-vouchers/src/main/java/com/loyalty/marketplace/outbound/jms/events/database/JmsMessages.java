package com.loyalty.marketplace.outbound.jms.events.database;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Accenture
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "JMSMessages")
@Component
public class JmsMessages {

	@Id
	private String id;
	@Field("CorrelationId")
	private String correlationId;
	@Field("Data")
	private Object data;
	@Field("QueueName")
	private String queueName;
	@Field("ReplyQueueName")
	private String replyQueueName;
	@Field("Status")
	private String status;
	@Field("StatusDetail")
	private String statusDetail;
	@Field(value= "CreatedUser")
	private String createdUser;
	@Field(value= "Createddate")
	private Date createdDate;
	@Field(value= "UpdatedUser")
	private String updatedUser;
	@Field(value= "Updateddate")
	private Date updatedDate;

}
