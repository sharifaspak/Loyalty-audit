package com.loyalty.marketplace.voucher.inbound.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.loyalty.marketplace.voucher.utils.DateDeSerializerForTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsDTO {
	//private String status;
	private List<String> template;
	private String smsText; 
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonProperty
	@NotNull(message = "Scheduled date cannot be empty")
	@JsonDeserialize(using = DateDeSerializerForTime.class)
	private Date scheduledTime;
}
