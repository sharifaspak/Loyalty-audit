package com.loyalty.marketplace.outbound.events.eventobject;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -788807361113246163L;
	private String membershipCode;
	private List<String> accountNumList;

}
