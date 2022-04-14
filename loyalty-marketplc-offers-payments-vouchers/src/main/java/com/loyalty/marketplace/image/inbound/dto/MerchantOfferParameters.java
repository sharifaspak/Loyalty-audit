package com.loyalty.marketplace.image.inbound.dto;

import javax.validation.constraints.NotEmpty;

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
public class MerchantOfferParameters {
	
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
	@NotEmpty(message = "{validation.merchantOfferParameters.imageType.notEmpty.msg}")
	private String imageType;
	@NotEmpty(message = "{validation.merchantOfferParameters.domainId.notEmpty.msg}")
	private String domainId;
	@NotEmpty(message = "{validation.merchantOfferParameters.domainName.notEmpty.msg}")
	private String domainName;
	@NotEmpty(message = "{validation.merchantOfferParameters.availableInChannel.notEmpty.msg}")
	private String availableInChannel;
	
	private int length;
	private int height;
	private String path;
	private String imageUrlDr;
	private String imageUrlProd;
	
	public MerchantOfferParameters(String program, String authorization, String externalTransactionId, String userName,
			String sessionId, String userPrev, String channelId, String systemId, String systemPassword, String token,
			String transactionId, MultipartFile file, String imageType, String domainId,
			String domainName, String availableInChannel) {
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
		this.imageType = imageType;
		this.domainId = domainId;
		this.domainName = domainName;
		this.availableInChannel = availableInChannel;
	}
	
}
