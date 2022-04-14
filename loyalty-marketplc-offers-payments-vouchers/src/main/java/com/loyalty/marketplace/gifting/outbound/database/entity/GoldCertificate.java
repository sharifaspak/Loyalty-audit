package com.loyalty.marketplace.gifting.outbound.database.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "GoldTransaction")
public class GoldCertificate {

	@Id
	private String id;
	
	@Field("ProgramCode")
	private String programCode;
	
	@Field("AccountNumber")
	private String accountNumber;
	
	@Field("MembershipCode")
	private String membershipCode;
	
	@Field("TotalGoldBalance")
	private Double totalGoldBalance;
	
	@Field("TotalPointAmount")
	private Integer totalPointAmount;
	
	@Field("TotalSpentAmount")
	private Double totalSpentAmount;
	
	@Field("CertificateDetails")
	private List<GoldCertificateTransaction> certificateDetails;
	
	@Field("CreatedUser")
	private String createdUser;
	
	@Field("CreatedDate")
	private Date createdDate;
	
	@Field("UpdatedUser")
	private String updatedUser;
	
	@Field("UpdatedDate")
	private Date updatedDate;
	
}
