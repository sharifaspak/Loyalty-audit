package com.loyalty.marketplace.image.inbound.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GiftingParameters {

	private String program;
	private String authorization;
	private String externalTransactionId;
	private String userName;
	private String sessionId;
	private String userPrev;
	private String channelId;
	private String systemId;
	private String systemPassword;
	private String token;
	private String transactionId;
	
	private MultipartFile file;
	@NotEmpty(message = "{validation.giftingParameters.type.notEmpty.msg}")
	private String type;
	@NotNull(message = "{validation.giftingParameters.priority.notNull.msg}")
	private Integer priority;
	@NotNull(message = "{validation.giftingParameters.backgroundPriority.notNull.msg}")
	private Integer backgroundPriority;
	@NotEmpty(message = "{validation.allParameters.status.notEmpty.msg}")
	private String status;	
	@NotEmpty(message = "{validation.giftingParameters.nameEn.notEmpty.msg}")
	private String nameEn;
	@NotEmpty(message = "{validation.giftingParameters.nameAr.notEmpty.msg}")
	private String nameAr;
	private String colorCode;
	private String colorDirection;
	@NotEmpty(message = "{validation.giftingParameters.greetingMessageEn.notEmpty.msg}")
	private String greetingMessageEn;
	@NotEmpty(message = "{validation.giftingParameters.greetingMessageAr.notEmpty.msg}")
	private String greetingMessageAr;
	
	private int length;
	private int height;
	private String path;
	private Integer imageIdGenerated;
	private String imageUrlDr;
	private String imageUrlProd;

	public GiftingParameters(String program, String authorization, String externalTransactionId, String userName,
			String sessionId, String userPrev, String channelId, String systemId, String systemPassword, String token,
			String transactionId, MultipartFile file, String type, Integer priority, Integer backgroundPriority,
			String status, String nameEn, String nameAr, String colorCode, String colorDirection,
			String greetingMessageEn, String greetingMessageAr) {
		super();
		this.program = program;
		this.authorization = authorization;
		this.externalTransactionId = externalTransactionId;
		this.userName = userName;
		this.sessionId = sessionId;
		this.userPrev = userPrev;
		this.channelId = channelId;
		this.systemId = systemId;
		this.systemPassword = systemPassword;
		this.token = token;
		this.transactionId = transactionId;
		this.file = file;
		this.type = type;
		this.priority = priority;
		this.backgroundPriority = backgroundPriority;
		this.status = status;
		this.nameEn = nameEn;
		this.nameAr = nameAr;
		this.colorCode = colorCode;
		this.colorDirection = colorDirection;
		this.greetingMessageEn = greetingMessageEn;
		this.greetingMessageAr = greetingMessageAr;
	}
	
}
